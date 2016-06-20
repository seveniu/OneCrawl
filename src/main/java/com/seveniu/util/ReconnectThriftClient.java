package com.seveniu.util;

import com.google.common.collect.Sets;
import org.apache.thrift.TServiceClient;
import org.apache.thrift.transport.TTransport;
import org.apache.thrift.transport.TTransportException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.net.ConnectException;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Created by seveniu on 6/6/16.
 * ReconnectThriftClient
 */
public class ReconnectThriftClient {
    private static final Logger LOG = LoggerFactory.getLogger(ReconnectThriftClient.class);


    /**
     * List of causes which suggest a restart might fix things (defined as constants in {@link org.apache.thrift.transport.TTransportException}).
     */
    private static final Set<Integer> RESTARTABLE_CAUSES = Sets.newHashSet(
            TTransportException.NOT_OPEN,
            TTransportException.END_OF_FILE,
            TTransportException.TIMED_OUT,
            TTransportException.UNKNOWN);


    /**
     * Reflectively wraps a thrift client so that when a call fails due to a networking error, a reconnect is attempted.
     *
     * @param baseClient      the client to wrap
     * @param clientInterface the interface that the client implements (can be inferred by using
     * @param options         options that control behavior of the reconnecting client
     */
    public static <T extends TServiceClient, C> C wrap(T baseClient, Class<C> clientInterface, Options options, Listener listener) {
        Object proxyObject = Proxy.newProxyInstance(clientInterface.getClassLoader(),
                new Class<?>[]{clientInterface},
                new ReconnectingClientProxy<>(baseClient, options.getNumRetries(), options.getTimeBetweenRetries(), listener));

        return (C) proxyObject;
    }

    /**
     * Reflectively wraps a thrift client so that when a call fails due to a networking error, a reconnect is attempted.
     *
     * @param baseClient the client to wrap
     * @param options    options that control behavior of the reconnecting client
     */
    public static <T extends TServiceClient, C> C wrap(T baseClient, Options options, Listener listener) {
        Class<?>[] interfaces = baseClient.getClass().getInterfaces();

        for (Class<?> iface : interfaces) {
            if (iface.getSimpleName().equals("Iface") && iface.getEnclosingClass().equals(baseClient.getClass().getEnclosingClass())) {
                return (C) wrap(baseClient, iface, options, listener);
            }
        }

        throw new RuntimeException("Class needs to implement Iface directly. Use wrap(TServiceClient, Class) instead.");
    }

    public static <T extends TServiceClient, C> C wrap(T baseClient, Class<C> clientInterface, Listener listener) {
        return wrap(baseClient, clientInterface, Options.defaults(), listener);
    }

    public static <T extends TServiceClient, C> C wrap(T baseClient) {
        return wrap(baseClient, Options.defaults(), null);
    }

    public static <T extends TServiceClient, C> C wrap(T baseClient, Listener listener) {
        return wrap(baseClient, Options.defaults(), listener);
    }

    /**
     * Helper proxy class. Attempts to call method on proxy object wrapped in try/catch. If it fails, it attempts a
     * reconnect and tries the method again.
     *
     * @param <T>
     */
    private static class ReconnectingClientProxy<T extends TServiceClient> implements InvocationHandler {
        private final T baseClient;
        private final int maxRetries;
        private final long timeBetweenRetries;
        private Listener listener;
        private final AtomicBoolean isInReConnect = new AtomicBoolean(false);
        private volatile boolean connect = true;

        public ReconnectingClientProxy(T baseClient, int maxRetries, long timeBetweenRetries, Listener listener) {
            this.baseClient = baseClient;
            this.maxRetries = maxRetries;
            this.timeBetweenRetries = timeBetweenRetries;
            this.listener = listener;
        }

        private final Object reconenctLock = new Object();

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws InvocationTargetException, IllegalAccessException, InterruptedException, ConnectException {
            try {
                if (!connect) {
                    throw new ConnectException("connect client failed");
                }
                return method.invoke(baseClient, args);
            } catch (InvocationTargetException e) {
                if (e.getTargetException() instanceof TTransportException) {
                    Throwable ee = e.getTargetException();
                    if (ee instanceof TTransportException) {
                        TTransportException cause = (TTransportException) e.getTargetException();

                        synchronized (isInReConnect) {
                            if (isInReConnect.get()) {
                                isInReConnect.wait();
                            } else {
                                isInReConnect.set(true);
                                if (listener != null) {
                                    listener.reconnect();
                                }
                                reconnectOrThrowException();
                                isInReConnect.wait();
                            }
                        }

                        System.out.println("**************");
                        if (!connect) {
                            Thread.currentThread().interrupt();
                            throw new ConnectException();
                        } else {
                            if (RESTARTABLE_CAUSES.contains(cause.getType())) {
                                return method.invoke(baseClient, args);
                            }
                        }
                    }
                }
                throw e;

            }
        }


        private void reconnectOrThrowException() {

            TTransport transport = baseClient.getInputProtocol().getTransport();
            transport.close();

            new Thread(() -> {
                int errors = 0;

                boolean result = false;
                while (errors < maxRetries) {
                    try {
                        LOG.info("Attempting to reconnect...");
                        transport.open();
                        LOG.info("Reconnection successful");
                        result = true;
                        break;
                    } catch (TTransportException e) {
                        LOG.error("Error while reconnecting:{}", e.getMessage());
                        errors++;

                        if (errors < maxRetries) {
                            try {
                                LOG.info("Sleeping for {} milliseconds before retrying", timeBetweenRetries);

                                TimeUnit.MILLISECONDS.sleep(timeBetweenRetries);
                            } catch (InterruptedException e2) {
                                throw new RuntimeException(e);
                            }
                        }
                    }
                }
                synchronized (isInReConnect) {
                    if (result) {
                        isInReConnect.notifyAll();
                        if (listener != null) {
                            listener.reconnectSuccess();
                        }
                        connect = true;
                    } else {

                        if (listener != null) {
                            listener.reconnectFailed();
                        }
                        connect = false;
                        isInReConnect.notifyAll();
                    }
                    isInReConnect.set(false);
                }
            }).start();
        }
    }

    public static class Options {
        private int numRetries;
        private long timeBetweenRetries;

        /**
         * @param numRetries         the maximum number of times to try reconnecting before giving up and throwing an
         *                           exception
         * @param timeBetweenRetries the number of milliseconds to wait in between reconnection attempts.
         */
        public Options(int numRetries, long timeBetweenRetries) {
            this.numRetries = numRetries;
            this.timeBetweenRetries = timeBetweenRetries;
        }

        private int getNumRetries() {
            return numRetries;
        }

        private long getTimeBetweenRetries() {
            return timeBetweenRetries;
        }

        public Options withNumRetries(int numRetries) {
            this.numRetries = numRetries;
            return this;
        }

        public Options withTimeBetweenRetries(long timeBetweenRetries) {
            this.timeBetweenRetries = timeBetweenRetries;
            return this;
        }

        public static Options defaults() {
            return new Options(5, 10000L);
        }
    }

    public interface Listener {
        void reconnect();

        void reconnectSuccess();

        void reconnectFailed();
    }
}

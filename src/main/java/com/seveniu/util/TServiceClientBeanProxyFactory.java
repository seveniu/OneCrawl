package com.seveniu.util;

import org.apache.commons.pool2.impl.GenericObjectPool;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.apache.thrift.TServiceClient;
import org.apache.thrift.TServiceClientFactory;
import org.apache.thrift.transport.TTransport;
import org.apache.thrift.transport.TTransportException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.net.ConnectException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by seveniu on 6/12/16.
 * TServiceClientBeanProxyFactory
 */

public class TServiceClientBeanProxyFactory {

    private final static Logger logger = LoggerFactory.getLogger(TServiceClientBeanProxyFactory.class);

    private String host;
    private int port;
    private Object clientProxy;
    private Class<?> clazz;
    private GenericObjectPoolConfig poolConfig = new GenericObjectPoolConfig();
    private GenericObjectPool<TServiceClient> pool;
    private volatile boolean isStop;

    //
    private Options option = Options.defaults();
    /**
     * List of causes which suggest a restart might fix things (defined as
     * constants in {@link org.apache.thrift.transport.TTransportException}).
     */
    private static final Set<Integer> RESTARTABLE_CAUSES = new HashSet<Integer>(
            Arrays.asList(TTransportException.NOT_OPEN, TTransportException.END_OF_FILE, TTransportException.TIMED_OUT,
                    TTransportException.UNKNOWN));


    public TServiceClientBeanProxyFactory() {
        // FIXME set the object pool configuration
        poolConfig.setMaxIdle(10);

    }


    @SuppressWarnings("unchecked")
    public <T extends TServiceClient, C> C create(String host, int port, Class<T> clazz) throws ClassNotFoundException, IllegalAccessException, InstantiationException {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        Class<?>[] interfaces = clazz.getInterfaces();
        Class<C> ifaceClass = null;

//        Class<?> ifaceClass = classLoader.loadClass(clazz.getName() + "$Iface");
        for (Class iface : interfaces) {
            if (iface.getSimpleName().equals("Iface") && iface.getEnclosingClass().equals(clazz.getEnclosingClass())) {
                ifaceClass = iface;
                break;
            }
        }
        Class<TServiceClientFactory<T>> factoryClass = (Class<TServiceClientFactory<T>>) classLoader
                .loadClass(clazz.getName() + "$Factory");
        TServiceClientFactory<T> clientFactory = factoryClass.newInstance();
        ThriftClientPoolFactory poolFactory = new ThriftClientPoolFactory(host, port, clientFactory);

        pool = new GenericObjectPool<>(poolFactory, poolConfig);

        clientProxy = Proxy.newProxyInstance(classLoader, new Class[]{ifaceClass}, new InvocationHandler() {
            @Override
            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                if (isStop) {
                    throw new IllegalAccessException("client close");
                }
                TServiceClient client = pool.borrowObject();
                try {
                    return method.invoke(client, args);
                } catch (InvocationTargetException e) {
                    if (e.getTargetException() instanceof TTransportException) {
                        TTransportException te = (TTransportException) e.getTargetException();

                        if (RESTARTABLE_CAUSES.contains(te.getType())) {
//                            reconnectOrThrowException(client.getInputProtocol().getTransport());
                            return method.invoke(client, args);
                        }
                    } else if (e.getTargetException() instanceof ConnectException) {
//                        reconnectOrThrowException(client.getInputProtocol().getTransport());
                        return method.invoke(client, args);
                    }
                    throw e;
                } finally {
                    pool.returnObject(client);
                }
            }

        });
        return (C) clientProxy;
    }

    public void close() {
        pool.close();
        isStop = true;
    }

    private void reconnectOrThrowException(TTransport transport) throws TTransportException {
        int errors = 0;
        transport.close();

        int numRetries = option.getNumRetries();
        while (errors < numRetries) {
            try {
                logger.info("Attempting to reconnect /{}:{}...", host, port);
                transport.open();
                logger.info("Reconnection successful /{}:{}", host, port);
                break;
            } catch (TTransportException e) {
                logger.error("Error while reconnecting /{}:{}:", host, port, e);
                errors++;

                if (errors < numRetries) {
                    try {
                        long timeBetweenRetries = option.getTimeBetweenRetries();
                        logger.debug("Sleeping for %s milliseconds before retrying /{}:{}",
                                timeBetweenRetries, host, port);
                        Thread.sleep(timeBetweenRetries);
                    } catch (InterruptedException e2) {
                        throw new RuntimeException(e);
                    }
                }
            }
        }

        if (errors >= numRetries) {
            throw new TTransportException("Failed to reconnect");
        }
    }


    public static class Options {
        private int numRetries;
        private long timeBetweenRetries;

        /**
         * @param numRetries         the maximum number of times to try reconnecting before
         *                           giving up and throwing an exception
         * @param timeBetweenRetries the number of milliseconds to wait in between reconnection
         *                           attempts.
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
            return new Options(5, 5000L);
        }
    }
}

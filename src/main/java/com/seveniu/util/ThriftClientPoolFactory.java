package com.seveniu.util;

import org.apache.commons.pool2.BasePooledObjectFactory;
import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.impl.DefaultPooledObject;
import org.apache.thrift.TServiceClient;
import org.apache.thrift.TServiceClientFactory;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.transport.TSocket;
import org.apache.thrift.transport.TTransport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by seveniu on 6/12/16.
 */
public class ThriftClientPoolFactory<T extends TServiceClient> extends BasePooledObjectFactory<T> {

    private final static Logger logger = LoggerFactory.getLogger(ThriftClientPoolFactory.class);

    private String host;
    private int port;
    private TServiceClientFactory<T> clientFactory;

    private AtomicInteger counter = new AtomicInteger(1);

    public ThriftClientPoolFactory(String host, int port, TServiceClientFactory<T> clientFactory) {
        this.clientFactory = clientFactory;
        this.host = host;
        this.port = port;
    }

    @Override
    public T create() throws Exception {
        TTransport transport = new TSocket(host, port);
        transport.open();

        TProtocol protocol = new TBinaryProtocol(transport);

        T client = clientFactory.getClient(protocol);

        logger.debug("open new transport-{} on /{}:{}", counter.getAndIncrement(), host, port);

        return client;
    }


    @Override
    public PooledObject<T> wrap(T obj) {
        // FIXME
        return new DefaultPooledObject<>(obj);
    }

    @Override
    public boolean validateObject(PooledObject<T> p) {
        T client = p.getObject();
        TTransport transport = client.getInputProtocol().getTransport();
        return transport.isOpen();
    }

    @Override
    public void destroyObject(PooledObject<T> p) throws Exception {
        TServiceClient client = p.getObject();
        TTransport transport = client.getInputProtocol().getTransport();
        transport.close();
    }

}

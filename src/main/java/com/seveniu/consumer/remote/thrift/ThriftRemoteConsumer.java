package com.seveniu.consumer.remote.thrift;

import com.seveniu.common.json.Json;
import com.seveniu.consumer.ConsumerClient;
import com.seveniu.def.TaskStatus;
import com.seveniu.node.Node;
import com.seveniu.task.TaskStatistic;
import com.seveniu.thriftServer.ConsumerConfig;
import com.seveniu.util.TServiceClientBeanProxyFactory;
import org.apache.thrift.TException;
import org.apache.thrift.transport.TSocket;
import org.apache.thrift.transport.TTransport;
import org.apache.thrift.transport.TTransportException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by seveniu on 5/24/16.
 * RemoteConsumer
 */
public class ThriftRemoteConsumer implements ConsumerClient {
    private Logger logger = LoggerFactory.getLogger(this.getClass());
    private ConsumerConfig remoteConsumerConfig;

    private ConsumerThrift.Iface client;

    public ThriftRemoteConsumer(ConsumerConfig config) throws TTransportException {
        this.remoteConsumerConfig = config;
        this.build(config.getHost(), config.getPort());
    }

    private TServiceClientBeanProxyFactory clientBeanProxyFactory;

    private void build(String host, int port) throws TTransportException {
        TTransport transport = new TSocket(host, port);
        transport.open();

        logger.info("template client connect /{}:{}", host, port);

        try {
            clientBeanProxyFactory = new TServiceClientBeanProxyFactory();
            this.client = clientBeanProxyFactory.create(host, port, ConsumerThrift.Client.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean has(String url) {
        try {
            return client.has(url);
        } catch (Exception e) {
            e.printStackTrace();
            logger.warn("url check warn : {}", e.getMessage());
            return false;
        }
    }

    @Override
    public void done(Node node) {
        String data = Json.toJson(node);
        try {
            client.done(data);
        } catch (Exception e) {
            e.printStackTrace();
            logger.warn("consumer done warn : {}", e.getMessage());
        }
    }

    @Override
    public void statistic(TaskStatistic statistic) {
        try {
            client.statistic(Json.toJson(statistic));
        } catch (Exception e) {
            e.printStackTrace();
            logger.warn("get task statistic warn : {}", e.getMessage());
        }
    }

    @Override
    public void taskStatusChange(String taskId, TaskStatus taskStatus) {
        try {
            client.taskStatusChange(taskId, taskStatus);
        } catch (TException e) {
            logger.warn("get task status change error : {}", e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    public void stop() {
        this.client = null;
        clientBeanProxyFactory.close();
        logger.info("consumer : {} close", remoteConsumerConfig.getName());
    }


}

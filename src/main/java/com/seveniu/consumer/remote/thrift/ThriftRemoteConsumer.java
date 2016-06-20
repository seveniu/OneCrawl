package com.seveniu.consumer.remote.thrift;

import com.seveniu.common.json.Json;
import com.seveniu.consumer.Consumer;
import com.seveniu.consumer.TaskInfo;
import com.seveniu.consumer.remote.RemoteConsumerConfig;
import com.seveniu.node.Node;
import com.seveniu.task.TaskStatistic;
import com.seveniu.util.ReconnectThriftClient;
import com.seveniu.util.TServiceClientBeanProxyFactory;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.transport.TSocket;
import org.apache.thrift.transport.TTransport;
import org.apache.thrift.transport.TTransportException;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by seveniu on 5/24/16.
 * RemoteConsumer
 */
public class ThriftRemoteConsumer extends Consumer {

    private RemoteConsumerConfig remoteConsumerConfig;

    private ConsumerThrift.Iface thriftClient;

    public ThriftRemoteConsumer(RemoteConsumerConfig config) throws TTransportException {
        super(config.getName());
        this.remoteConsumerConfig = config;
        this.build(config.getHost(),config.getPort());
    }

    public void build(String host, int port) throws TTransportException {
        TTransport transport = new TSocket(host, port);
        transport.open();

        logger.info("template client connect /{}:{}", host, port);

        TProtocol protocol = new TBinaryProtocol(transport);
//        this.originClient = new ConsumerThrift.Client(protocol);
        try {
            this.thriftClient = new TServiceClientBeanProxyFactory().create(host,port,ConsumerThrift.Client.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
//        return ReconnectThriftClient.wrap(originClient, new ReconnectThriftClient.Listener() {
//            @Override
//            public void reconnect() {
//
//            }
//
//            @Override
//            public void reconnectSuccess() {
//
//            }
//
//            @Override
//            public void reconnectFailed() {
//                logger.warn("thrift reconnect error");
//                stop();
//            }
//        });
    }

    @Override
    public boolean has(String url) {
        try {
            return thriftClient.has(url);
        } catch (Exception e) {
            logger.warn("url check warn : {}", e.getMessage());
            return false;
        }
    }

    @Override
    public List<TaskInfo> receiveTasks() {
        List<Task> tasks;
        try {
            tasks = thriftClient.getTasks();
            List<TaskInfo> taskInfos = new ArrayList<>();
            for (Task task : tasks) {
                TaskInfo taskInfo = new TaskInfo(String.valueOf(task.getId()), task.getName(),
                        task.getUrls(), String.valueOf(task.getTemplateId()), task.getTemplateType(), task.getTemplate(),
                        task.getThreadNum(), task.getProxy(), task.getJavascript());
                taskInfos.add(taskInfo);
            }
            return taskInfos;
        } catch (Exception e) {
            logger.warn("get task info warn : {}", e.getMessage());
            return null;
        }
    }

    @Override
    public void out(Node node) {
        String data = Json.toJson(node);
        try {
            thriftClient.done(data);
        } catch (Exception e) {
            logger.warn("consumer out warn : {}", e.getMessage());
        }
    }

    @Override
    public void statistic(TaskStatistic statistic) {
        try {
            thriftClient.statistic(Json.toJson(statistic));
        } catch (Exception e) {
            logger.warn("get task statistic warn : {}", e.getMessage());
        }
    }


    @Override
    protected void stop0() {
    }


}

package com.seveniu.thriftServer;

import com.seveniu.consumer.ConsumerManager;
import org.apache.thrift.TException;
import org.apache.thrift.server.TServer;
import org.apache.thrift.server.TThreadPoolServer;
import org.apache.thrift.transport.TServerSocket;
import org.apache.thrift.transport.TTransportException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.net.ConnectException;

/**
 * Created by seveniu on 7/3/16.
 * ThriftServer
 */
@Service
public class ThriftServer {

    @Autowired
    ConsumerManager consumerManager;

    @Autowired
    public ThriftServer(@Value("${thriftServerPort}") int port) {
        startServer(port);
    }

    public void startServer(int port) {
        new Thread(() -> {

            try {
                TServerSocket socket = new TServerSocket(port);
                CrawlThrift.Processor processor = new CrawlThrift.Processor<>(new Server());
                TServer server = new TThreadPoolServer(new TThreadPoolServer.Args(socket).processor(processor));
                server.serve();
            } catch (TTransportException e) {
                e.printStackTrace();
            }
        }, "thrift-server-thread").start();
    }

    private class Server implements CrawlThrift.Iface {


        @Override
        public String reg(ConsumerConfig consumerConfig) throws TException {
            try {
                String uuid = consumerManager.regRemoteConsumer(consumerConfig);
                if (uuid != null) {
                    return uuid;
                }
            } catch (ConnectException e) {
                e.printStackTrace();
            }
            return "";
        }


        @Override
        public boolean addTask(String uuid, TaskInfo taskInfo) throws TException {
            return consumerManager.getConsumer(uuid).getTaskManager().addTask(taskInfo);
        }
    }

}

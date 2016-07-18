package com.seveniu.thriftServer;

import com.seveniu.common.json.Json;
import com.seveniu.consumer.ConsumerManager;
import com.seveniu.task.SpiderRegulate;
import com.seveniu.task.TaskStatistic;
import org.apache.thrift.TException;
import org.apache.thrift.server.TServer;
import org.apache.thrift.server.TThreadPoolServer;
import org.apache.thrift.transport.TServerSocket;
import org.apache.thrift.transport.TTransportException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.net.ConnectException;
import java.util.List;

/**
 * Created by seveniu on 7/3/16.
 * ThriftServer
 */
@Service
public class ThriftServer {
    private Logger logger = LoggerFactory.getLogger(this.getClass());
    private volatile boolean running;

    @Autowired
    ConsumerManager consumerManager;

    @Autowired
    public ThriftServer(@Value("${thriftServerPort}") int port) {
        startServer(port);
    }

    public void startServer(int port) {
        if (running) {
            logger.warn("thrift server has running ");
            return;
        }
        new Thread(() -> {

            try {
                TServerSocket socket = new TServerSocket(port);
                CrawlThrift.Processor processor = new CrawlThrift.Processor<>(new Server());
                TServer server = new TThreadPoolServer(new TThreadPoolServer.Args(socket).processor(processor));
                running = true;
                server.serve();
            } catch (TTransportException e) {
                e.printStackTrace();
                running = false;
            }
        }, "thrift-server-thread").start();
        logger.info("start crawl thrift server at : {}", port);
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

        @Override
        public String getRunningTasks(String uuid) throws TException {
            List<TaskStatistic> taskStatistics = consumerManager.getConsumer(uuid).getTaskManager().getRunningTaskInfo();
            return Json.toJson(taskStatistics);
        }

        @Override
        public String getTaskSummary(String uuid) throws TException {
            SpiderRegulate.SpiderInfo spiderInfo = consumerManager.getConsumer(uuid).getTaskManager().getSpiderInfo();
            return Json.toJson(spiderInfo);
        }

    }

}

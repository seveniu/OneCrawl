package com.seveniu.consumer;

import com.seveniu.consumer.remote.HttpRemoteConsumer;
import com.seveniu.consumer.remote.RemoteConsumerConfig;
import com.seveniu.consumer.remote.thrift.ThriftRemoteConsumer;
import com.seveniu.task.SpiderRegulate;
import org.apache.thrift.transport.TTransportException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.net.ConnectException;
import java.nio.file.Files;
import java.util.Collection;
import java.util.Map;
import java.util.concurrent.*;

/**
 * Created by seveniu on 5/13/16.
 * ConsumerManager
 */
@Component
public class ConsumerManager {

    @Autowired
    SpiderRegulate spiderRegulate;
    private static final String REMOTE_CONFIG_PATH = "consumer/";
    private Logger logger = LoggerFactory.getLogger(ConsumerManager.class);
    private static final int WAIT_THRESHOLD = 1000;
    private ConcurrentHashMap<String, Consumer> consumerMap = new ConcurrentHashMap<>();
    private ScheduledExecutorService monitorSchedule;

    public void start() {

        monitorSchedule = Executors.newSingleThreadScheduledExecutor(r -> new Thread(r, "OutManager-monitor-thread"));
        monitor();
        regRemoteConsumerFromFile();
    }

    public boolean regConsumer(Consumer consumer) {
        if (this.consumerMap.containsKey(consumer.getName())) {
            logger.warn("consumer '{}' has reg", consumer.getName());
            return false;
        } else {
            this.consumerMap.put(consumer.getName(), consumer);
            logger.info("reg consumer : {}", consumer);
            consumer.start();
            return true;
        }
    }

    public boolean regRemoteConsumer(RemoteConsumerConfig remoteConsumerConfig) throws ConnectException, TTransportException {
        String name = remoteConsumerConfig.getName();
        if (this.consumerMap.containsKey(name)) {
            logger.warn("consumer '{}' has reg", name);
            return false;
        }
        Consumer consumer;
        switch (remoteConsumerConfig.getType()) {
            case "http":
                consumer = new HttpRemoteConsumer(remoteConsumerConfig);
                break;
            case "thrift":
                consumer = new ThriftRemoteConsumer(remoteConsumerConfig);
                break;
            default:
                logger.error("consumer is null ");
                throw new IllegalArgumentException("remote type is error. type : " + remoteConsumerConfig.getType());
        }
        return regConsumer(consumer);
    }

    private void regRemoteConsumerFromFile() {

        File file = new File(REMOTE_CONFIG_PATH);
        File[] files = file.listFiles();
        if (files == null) {
            throw new NullPointerException("remote config path is not exist");
        }
        if (files.length == 0) {
            logger.info("remote config is empty");
        } else {
            for (File file1 : files) {

                try {
                    String data = new String(Files.readAllBytes(file1.toPath()));
                    RemoteConsumerConfig remoteConsumerConfig = RemoteConsumerConfig.fromJson(data);
                    try {
                        regRemoteConsumer(remoteConsumerConfig);
                    } catch (TTransportException e) {
                        logger.warn("reg remote consumer : [{}] error : {}", data, e.getMessage());
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void removeConsumer(String consumerName) {
        Consumer consumer = this.consumerMap.remove(consumerName);
        consumer.stop();
    }

    public Consumer getConsumer(String name) {
        return consumerMap.get(name);
    }

    public Collection<Consumer> getAllConsumer() {
        return consumerMap.values();
    }

    private void monitor() {

        monitorSchedule.scheduleWithFixedDelay(() -> {
            for (Map.Entry<String, Consumer> entry : consumerMap.entrySet()) {
                Consumer consumer = entry.getValue();
                int waitSize = consumer.waitSize();
                if (waitSize > WAIT_THRESHOLD) {
                    logger.warn("consumer '{}' has more than threshold,cur wait size : {}", entry.getKey(), consumer.waitSize());
                }
            }
        }, 0, 1, TimeUnit.MINUTES);
    }

}

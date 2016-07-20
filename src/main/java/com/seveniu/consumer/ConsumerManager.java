package com.seveniu.consumer;

import com.seveniu.common.json.Json;
import com.seveniu.consumer.remote.HttpRemoteConsumer;
import com.seveniu.consumer.remote.thrift.ThriftRemoteConsumer;
import com.seveniu.task.SpiderRegulate;
import com.seveniu.thriftServer.ConsumerConfig;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.validator.routines.InetAddressValidator;
import org.apache.commons.validator.routines.UrlValidator;
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
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

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
            Consumer exist = this.consumerMap.get(consumer.getName());
            exist.stop();
            consumerMap.put(consumer.getName(),consumer);
            return false;
        } else {
            this.consumerMap.put(consumer.getName(), consumer);
            logger.info("reg consumer : {}", consumer);
            consumer.start();
            return true;
        }
    }

    public String regRemoteConsumer(ConsumerConfig remoteConsumerConfig) throws ConnectException, TTransportException {
        String name = remoteConsumerConfig.getName();
        if (this.consumerMap.containsKey(name)) {
            logger.warn("consumer '{}' has reg", name);
            return null;
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
        if(regConsumer(consumer)) {
            return consumer.getUuid();
        }
        return null;
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
                    ConsumerConfig remoteConsumerConfig = fromJson(data);
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
    public static ConsumerConfig fromJson(String data) throws IllegalArgumentException {
        ConsumerConfig config = Json.toObject(data, ConsumerConfig.class);
        String type = config.getType();
        if (StringUtils.isEmpty(config.getName())) {
            throw new IllegalArgumentException("name is empty");
        }
        if (StringUtils.isEmpty(type)) {
            throw new IllegalArgumentException("type is empty");
        }
        if (!type.equals("http") && !type.equals("thrift")) {
            throw new IllegalArgumentException("type is error : " + type);
        }

        if (type.equals("http")) {
            UrlValidator urlValidator = new UrlValidator(new String[]{"http", "https"});
            if (StringUtils.isEmpty(config.getStatisticUrl())) {
                throw new IllegalArgumentException("statisticUrl is empty");
            }
            if (StringUtils.isEmpty(config.getDoneUrl())) {
                throw new IllegalArgumentException("doneUrl is empty");
            }
            if (StringUtils.isEmpty(config.getDuplicateUrl())) {
                throw new IllegalArgumentException("duplicateUrl is empty");
            }
            if (!urlValidator.isValid(config.getStatisticUrl())) {
                throw new IllegalArgumentException("statisticUrl is error : " + config.getStatisticUrl());
            }
            if (!urlValidator.isValid(config.getDoneUrl())) {
                throw new IllegalArgumentException("doneUrl is error : " + config.getDoneUrl());
            }
            if (!urlValidator.isValid(config.getDuplicateUrl())) {
                throw new IllegalArgumentException("duplicateUrl is error : " + config.getDuplicateUrl());
            }
        }

        if (type.equals("thrift")) {
            if (StringUtils.isEmpty(config.getHost())) {
                throw new IllegalArgumentException("host is empty");
            }
            if (config.getPort()== 0) {
                throw new IllegalArgumentException("port is not set");
            }
            if (!InetAddressValidator.getInstance().isValid(config.getHost())) {
                throw new IllegalArgumentException("host is error : " + config.getHost());
            }

        }
        return config;
    }
    public void removeConsumer(String uuid) {
        Consumer consumer = this.consumerMap.remove(uuid);
        consumer.stop();
    }

    public Consumer getConsumer(String uuid) {
        return consumerMap.get(uuid);
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
                    logger.warn("consumer '{}' has more than threshold,cur wait size : {}", consumer.getName(), consumer.waitSize());
                }
            }
        }, 0, 1, TimeUnit.MINUTES);
    }

}

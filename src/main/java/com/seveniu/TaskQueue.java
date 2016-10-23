package com.seveniu;

import com.alibaba.fastjson.JSON;
import com.seveniu.consumer.Consumer;
import com.seveniu.consumer.ConsumerManager;
import com.seveniu.thriftServer.TaskInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;

/**
 * Created by seveniu on 10/11/16.
 * *
 */
@Service
public class TaskQueue {

    private static final String PREFIX = "task-";
    private Logger logger = LoggerFactory.getLogger(this.getClass());
    @Value("${spring.redis.host}")
    private String host;
    @Value("${spring.redis.port}")
    private int port;
    private final ConsumerManager consumerManager;

    @Autowired
    public TaskQueue(ConsumerManager consumerManager) {
        logger.info("data queue host : {} , port : {}", host, port);
        this.consumerManager = consumerManager;
    }

    @Scheduled(fixedRate = 30 * 1000, initialDelay = 30 * 1000)
    public void schedule() {
        try {
            Jedis jedis = new Jedis(host, port);
            logger.info("schedule task");
            for (Consumer consumer : this.consumerManager.getAllConsumer()) {
                int canRunNum = consumer.getTaskManager().getMaxRunning() - consumer.getTaskManager().getCurRunningSize();
                for (int i = 0; i < canRunNum; i++) {
                    String s = jedis.lpop(PREFIX + consumer.getName());
                    if (s != null) {
                        TaskInfo taskInfo = JSON.parseObject(s, TaskInfo.class);
                        consumer.getTaskManager().addTask(taskInfo);
                        logger.info("consumer : {} run task : {}", consumer.getName(), taskInfo.getId());
                    } else {
                        logger.info("consumer : {} has no task", consumer.getName());
                        break;
                    }
                }
            }
            jedis.close();
        } catch (Exception e) {
            e.printStackTrace();
            logger.warn("schedule task error : {}", e.getMessage(), e);
        }
    }

    public int getWaitSize(String name) {
        Jedis jedis = new Jedis(host, port);
        return jedis.llen(PREFIX + name).intValue();
    }

}

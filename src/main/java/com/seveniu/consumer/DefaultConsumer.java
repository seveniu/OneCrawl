package com.seveniu.consumer;

import com.seveniu.common.json.Json;
import com.seveniu.consumer.remote.thrift.TaskStatus;
import com.seveniu.node.Node;
import com.seveniu.task.TaskStatistic;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * Created by seveniu on 5/14/16.
 * DefaultConsumer
 */
public class DefaultConsumer extends Consumer {

    public DefaultConsumer(String name) {
        super(name);
    }
    public DefaultConsumer() {
        super("default");
    }


    @Override
    public boolean has(String url) {
        return false;
    }

    @Override
    public void done(Node result) {
        logger.info(Json.toJson(result));
    }

    @Override
    public void statistic(TaskStatistic statistic) {
        logger.info(Json.toJson(statistic));
    }

    @Override
    public void taskStatusChange(String taskId, TaskStatus taskStatus) {

    }

    @Override
    protected void stop0() {

    }

    @Override
    public String getName() {
        return "default";
    }

}

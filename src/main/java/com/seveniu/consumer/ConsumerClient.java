package com.seveniu.consumer;

import com.seveniu.consumer.remote.thrift.TaskStatus;
import com.seveniu.node.Node;
import com.seveniu.task.TaskStatistic;

/**
 * Created by seveniu on 7/5/16.
 */
public interface ConsumerClient {

    boolean has(String url);

    void done(Node node);

    void statistic(TaskStatistic taskStatistic);

    void taskStatusChange(String taskId, TaskStatus taskStatus);

    void stop();
}

package com.seveniu.consumer;

import com.seveniu.common.tools.ShutdownHook;
import com.seveniu.common.tools.ShutdownHookManager;
import com.seveniu.node.Node;
import com.seveniu.task.TaskStatistic;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.concurrent.*;

/**
 * Created by seveniu on 5/13/16.
 * Consumer
 */
public abstract class Consumer implements ShutdownHook {
    protected Logger logger;

    private String name;

    private ConsumerTaskManager taskManager;
    private LinkedBlockingQueue<Runnable> dataQueue;

    private ExecutorService transferService;
    protected volatile STATUS status = STATUS.UN_START;

    public Consumer(String name) {
        this.name = name;
//        this.logger = LoggerFactory.getLogger(this.getClass() + "-" + name);
        this.logger = LoggerFactory.getLogger(this.getClass());
    }


    public String getName() {
        return name;
    }

    public abstract boolean has(String url);

    public abstract void out(Node node);

    public abstract List<TaskInfo> receiveTasks();

    public abstract void statistic(TaskStatistic taskStatistic);

    public ConsumerTaskManager getTaskManager() {
        return taskManager;
    }

    public void start() {
        dataQueue = new LinkedBlockingQueue<>();
        transferService = new ThreadPoolExecutor(6, 6,
                0L, TimeUnit.MILLISECONDS,
                dataQueue,
                r -> new Thread(r, getName() + "-transfer-thread"),
                new ThreadPoolExecutor.CallerRunsPolicy());
        taskManager = new ConsumerTaskManager(this);
        taskManager.start();
        ShutdownHookManager.get().register(this);
        status = STATUS.START;
        logger.info("consumer : {} start", name);
    }

    public void transfer(Node node) {

        transferService.execute(() -> {
            if (Thread.currentThread().isInterrupted()) {
                return;
            }
            out(node);
        });
    }

    int waitSize() {
        return dataQueue.size();
    }

    @Override
    public String toString() {
        return "Consumer{" +
                " name : " + getName() +
                ", type : " + this.getClass() +
                "}";
    }

    public void stop() {
        logger.info("consumer stop ~~~~");
        status = STATUS.STOP;
        transferService.shutdownNow();
        this.taskManager.stop();
        stop0();
    }

    protected abstract void stop0();

    @Override
    public void shutdown() {
        stop();
    }


    public enum STATUS {
        STOP, START, UN_START
    }
}

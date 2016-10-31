package com.seveniu.consumer;

import com.seveniu.def.TaskStatus;
import com.seveniu.spider.MySpider;
import com.seveniu.spider.SpiderFactory;
import com.seveniu.spider.TemplateType;
import com.seveniu.task.SpiderRegulate;
import com.seveniu.task.SpiderTask;
import com.seveniu.task.TaskStatistic;
import com.seveniu.template.PagesTemplate;
import com.seveniu.thriftServer.TaskInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by seveniu on 6/7/16.
 * ConsumerTaskManager
 */
public class ConsumerTaskManager {
    private Logger logger = LoggerFactory.getLogger(this.getClass());
    private static final int CORE_RUNNING = 20;
    private static final int MAX_RUNNING = 30;
    private static final int MAX_WAIT = 1000;
//    private static final int CORE_RUNNING = 1;
//    private static final int MAX_RUNNING = 1;
//    private static final int MAX_WAIT = 1;

    private ThreadPoolExecutor spiderExecServer;
    private ConcurrentHashMap<String, MySpider> runningSpider;
    private ConcurrentHashMap<String, MySpider> allSpider = new ConcurrentHashMap<>();
    private volatile boolean stop = false;
    private int count = 0;

    private Consumer consumer;

    ConsumerTaskManager(Consumer consumer) {
        this.consumer = consumer;
    }

    public void start() {
        runningSpider = new ConcurrentHashMap<>(MAX_RUNNING);

        spiderExecServer = new SpiderThreadPoolExecutor(CORE_RUNNING, MAX_RUNNING,
                1L, TimeUnit.MINUTES,
                new ThreadFactory() {
                    AtomicInteger count = new AtomicInteger();

                    @Override
                    public Thread newThread(Runnable r) {
                        return new Thread(r, consumer.getName() + "-spider-exec-thread-" + count.getAndIncrement());
                    }
                });
    }

    private final Object LOCK = new Object();

    /**
     * 添加任务
     * 返回 任务状态
     */
    public TaskStatus addTask(TaskInfo taskInfo) {

        // 优先级 小于 100 并且 等待队列满的时候, 返回 FULL
        // 如果优先级 大于等于 100, 不管队列是否满, 都立即执行
//        if (taskInfo.getPriority() < 100 && waitTaskQueue.size() >= MAX_WAIT) {
//            logger.warn("wait queue is full");
//            return TaskStatus.FULL;
//        }
        PagesTemplate pagesTemplate;
        try {
            pagesTemplate = PagesTemplate.fromJson(taskInfo.getTemplateId(), taskInfo.getTemplate());
        } catch (Exception e) {
            logger.warn("task: {} ,template : {} parse error", taskInfo.getId(), taskInfo.getTemplateId());
            return TaskStatus.FAIL;
        }
        if (pagesTemplate == null) {
            logger.error("consumer {} 's template {} is error", consumer.getName(), taskInfo.getTemplateId());
            return TaskStatus.FAIL;
        } else {
            synchronized (LOCK) {
                // 如果已存在就返回
                if (runningSpider.containsKey(taskInfo.getId())) {
                    logger.warn("consumer : {} task : {} is running", consumer.getName(), taskInfo.getId());
                    return TaskStatus.RUNNING;
                }

                String spiderId = generateTaskId(consumer, taskInfo);

                if (allSpider.containsKey(spiderId)) {
                    return TaskStatus.WAIT;
                }

                // 构建 并执行

                try {
                    TemplateType templateType = TemplateType.get(taskInfo.getTemplateType().getValue());
                    MySpider mySpider = SpiderFactory.getSpider(spiderId, templateType, taskInfo, consumer, pagesTemplate);
                    allSpider.put(spiderId, mySpider);

                    spiderExecServer.execute(mySpider);
                    return TaskStatus.RUNNING;

                } catch (IllegalArgumentException e) {
                    logger.error("template type error : {}", e.getMessage());
                    return TaskStatus.FAIL;
                }
            }
        }
    }


    void stop() {
        synchronized (LOCK) {
            stop = true;
            if (!stop) {
                for (MySpider mySpider : runningSpider.values()) {
                    mySpider.stop();
                }
                spiderExecServer.shutdownNow();
                allSpider.clear();
                runningSpider.clear();
            }
        }
    }


    private String generateTaskId(Consumer consumer, TaskInfo taskInfo) {
        return consumer.getName() +
                "-" + taskInfo.getId() +
                "-" + taskInfo.getTemplateId();
    }


    public SpiderRegulate.SpiderInfo getSpiderInfo() {
        SpiderRegulate.SpiderInfo spiderInfo = new SpiderRegulate.SpiderInfo();
        spiderInfo.setConsumerName(consumer.getName());
        spiderInfo.setWaitSpiderNum(0);
        spiderInfo.setRunningSpiderNum(runningSpider.size());
        int threadNum = 0;
        for (SpiderTask spiderTask : runningSpider.values()) {
            threadNum += spiderTask.taskInfo().getThreadNum();
        }
        spiderInfo.setRunThreadNum(threadNum);
        return spiderInfo;
    }

    public List<TaskStatistic> getRunningTaskInfo() {
        synchronized (LOCK) {
            List<TaskStatistic> taskStatisticList = new ArrayList<>();
            for (MySpider mySpider : runningSpider.values()) {
                try {
                    taskStatisticList.add(mySpider.getTaskStatistic());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            return taskStatisticList;
        }
    }


    private class SpiderThreadPoolExecutor extends ThreadPoolExecutor {
        SpiderThreadPoolExecutor(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit, ThreadFactory threadFactory) {
            super(corePoolSize, maximumPoolSize, keepAliveTime, unit, new SynchronousQueue<>(), threadFactory,
                    new RejectedExecutionHandler() {
                        @Override
                        public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
                            if (!executor.isShutdown()) {
                                try {
                                    executor.getQueue().put(r);
                                } catch (InterruptedException e) {
                                    // should not be interrupted
                                }
                            }
                        }
                    });
        }

        @Override
        protected void beforeExecute(Thread t, Runnable r) {

            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            MySpider spider = (MySpider) r;
            synchronized (LOCK) {
                runningSpider.put(spider.getTaskInfo().getId(), spider);
            }
            consumer.getClient().taskStatusChange(spider.taskInfo().getId(), TaskStatus.RUNNING);
        }

        @Override
        protected void afterExecute(Runnable r, Throwable t) {
            MySpider spiderTask = (MySpider) r;
            consumer.getClient().taskStatusChange(spiderTask.taskInfo().getId(), TaskStatus.STOP);

            consumer.getClient().statistic(spiderTask.getTaskStatistic());
            synchronized (LOCK) {
                logger.info("spider : {} has done", spiderTask.getId());
                allSpider.remove(spiderTask.getId());
                runningSpider.remove(spiderTask.getTaskInfo().getId());
            }
        }
    }

    public int getMaxRunning() {
        return MAX_RUNNING;
    }

    public int getMaxWait() {
        return MAX_WAIT;
    }


    public int getCurRunningSize() {
        return spiderExecServer.getActiveCount();
    }
}

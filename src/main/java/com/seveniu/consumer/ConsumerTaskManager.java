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
    private static final int THREAD_MAX = 5;

    private ThreadPoolExecutor spiderExecServer;
    private LinkedBlockingQueue<Runnable> waitTaskQueue;
    private LinkedBlockingQueue<MySpider> runningQueue;
    private ConcurrentHashMap<String, MySpider> allSpider = new ConcurrentHashMap<>();
    private volatile boolean stop = false;

    private Consumer consumer;

    ConsumerTaskManager(Consumer consumer) {
        this.consumer = consumer;
    }

    public void start() {
        waitTaskQueue = new LinkedBlockingQueue<>();
        runningQueue = new LinkedBlockingQueue<>();

        spiderExecServer = new SpiderThreadPoolExecutor(THREAD_MAX, THREAD_MAX,
                1L, TimeUnit.MINUTES,
                waitTaskQueue,
                new ThreadFactory() {
                    AtomicInteger count = new AtomicInteger();

                    @Override
                    public Thread newThread(Runnable r) {
                        return new Thread(r, consumer.getName() + "-spider-exec-thread-" + count.getAndIncrement());
                    }
                }, new ThreadPoolExecutor.CallerRunsPolicy());
    }


    private final Object addLock = new Object();

    /**
     * 返回 任务状态
     *
     * @param taskInfo
     * @return
     */
    public TaskStatus addTask(TaskInfo taskInfo) {
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
            synchronized (addLock) {
                // 如果已存在就返回
                for (MySpider mySpider : runningQueue) {
                    if (mySpider.taskInfo().getId().equals(taskInfo.getId())) {
                        logger.warn("consumer : {} task : {} is running", consumer.getName(), taskInfo.getId());
                        return TaskStatus.RUNNING;
                    }
                }

                String spiderId = generateTaskId(consumer, taskInfo);
                SpiderTask spiderTask = allSpider.get(spiderId);

                if (spiderTask != null) {
                    return TaskStatus.WAIT;
                }

                try {
                    TemplateType templateType = TemplateType.get(taskInfo.getTemplateType().getValue());
                    MySpider mySpider = SpiderFactory.getSpider(spiderId, templateType, taskInfo, consumer, pagesTemplate);
                    allSpider.put(spiderId, mySpider);
                    execTask(mySpider);

                    return TaskStatus.WAIT;
                } catch (IllegalArgumentException e) {
                    logger.error("template type error : {}", e.getMessage());
                    return TaskStatus.FAIL;
                }
            }
        }
    }


    private final Object execTaskLock = new Object();

    private void execTask(SpiderTask spiderTask) {
        synchronized (execTaskLock) {

            if (stop) {
                return;
            }

            spiderExecServer.execute(spiderTask);
            logger.info("consumer exec task : {}", spiderTask.getId());
        }
    }


    void stop() {
        stop = false;
        spiderExecServer.shutdownNow();

        waitTaskQueue.clear();
        allSpider.clear();
    }


    private String generateTaskId(Consumer consumer, TaskInfo taskInfo) {
        return consumer.getName() +
                "-" + taskInfo.getId() +
                "-" + taskInfo.getTemplateId();
    }

    public void removerStopSpider(MySpider mySpider) {
        logger.info("spider : {} has done", mySpider.getId());
        allSpider.remove(mySpider.getId());
    }

    public SpiderRegulate.SpiderInfo getSpiderInfo() {
        SpiderRegulate.SpiderInfo spiderInfo = new SpiderRegulate.SpiderInfo();
        spiderInfo.setConsumerName(consumer.getName());
        spiderInfo.setWaitSpiderNum(waitTaskQueue.size());
        spiderInfo.setRunningSpiderNum(runningQueue.size());
        int threadNum = 0;
        for (SpiderTask spiderTask : runningQueue) {
            threadNum += spiderTask.taskInfo().getThreadNum();
        }
        spiderInfo.setRunThreadNum(threadNum);
        return spiderInfo;
    }

    public List<TaskStatistic> getRunningTaskInfo() {
        List<TaskStatistic> taskStatisticList = new ArrayList<>();
        for (MySpider mySpider : runningQueue) {
            taskStatisticList.add(mySpider.getTaskStatistic());
        }
        return taskStatisticList;
    }


    private class SpiderThreadPoolExecutor extends ThreadPoolExecutor {
        SpiderThreadPoolExecutor(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit, BlockingQueue<Runnable> workQueue, ThreadFactory threadFactory, RejectedExecutionHandler handler) {
            super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue, threadFactory, handler);
        }

        @Override
        protected void beforeExecute(Thread t, Runnable r) {

            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            runningQueue.add((MySpider) r);
            SpiderTask spiderTask = (SpiderTask) r;
            consumer.getClient().taskStatusChange(spiderTask.taskInfo().getId(), TaskStatus.RUNNING);
        }

        @Override
        protected void afterExecute(Runnable r, Throwable t) {
            MySpider spiderTask = (MySpider) r;
            consumer.getClient().taskStatusChange(spiderTask.taskInfo().getId(), TaskStatus.STOP);

            consumer.getClient().statistic(spiderTask.getTaskStatistic());
            removerStopSpider(spiderTask);
            runningQueue.remove(r);
        }
    }
}

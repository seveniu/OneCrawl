package com.seveniu.consumer;

import com.seveniu.consumer.remote.thrift.TaskStatus;
import com.seveniu.spider.MySpider;
import com.seveniu.spider.SpiderFactory;
import com.seveniu.spider.TemplateType;
import com.seveniu.task.SpiderRegulate;
import com.seveniu.task.SpiderTask;
import com.seveniu.template.PagesTemplate;
import com.seveniu.thriftServer.TaskInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import us.codecraft.webmagic.Spider;

import java.util.LinkedHashMap;
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
    private LinkedHashMap<String, MySpider> allSpider;
    private volatile boolean stop = false;

    private Consumer consumer;

    ConsumerTaskManager(Consumer consumer) {
        this.consumer = consumer;
    }

    public void start() {
        waitTaskQueue = new LinkedBlockingQueue<>();
        allSpider = new LinkedHashMap<>();

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

    public boolean addTask(TaskInfo taskInfo) {
        PagesTemplate pagesTemplate = PagesTemplate.fromJson(taskInfo.getTemplateId(), taskInfo.getTemplate());
        if (pagesTemplate == null) {
            logger.error("consumer {} 's template {} is error", consumer.getName(), taskInfo.getTemplateId());
            return false;
        } else {
            synchronized (addLock) {

                String spiderTaskId = generateTaskId(consumer, taskInfo);
                SpiderTask spiderTask = allSpider.get(spiderTaskId);
                if (spiderTask != null) {
                    if (spiderTask.getStatus() == Spider.Status.Running) {
                        logger.info("spider task : {}  has running", spiderTaskId);
                        return false;
                    } else if (spiderTask.getStatus() == Spider.Status.Init) {
                        logger.info("spider task : {}  is waiting", spiderTaskId);
                        return false;
                    } else {
                        allSpider.remove(spiderTaskId);
                        logger.info("spider task : {}  is running ....", spiderTaskId);
                        return false;
                    }
                }
                if (hasWait(spiderTaskId)) {
                    return false;
                }
                try {
                    TemplateType templateType = TemplateType.get(taskInfo.getTemplateType().getValue());
                    MySpider mySpider = SpiderFactory.getSpider(spiderTaskId, templateType, taskInfo, consumer, pagesTemplate);
                    allSpider.put(mySpider.getId(), mySpider);
                    execTask(mySpider);
                    return true;
                } catch (IllegalArgumentException e) {
                    logger.error("template type error : {}", e.getMessage());
                    return false;
                }
            }
        }
    }

    private boolean hasWait(String spiderTaskId) {
        for (Runnable runnable : waitTaskQueue) {
            SpiderTask spiderTask = (SpiderTask) runnable;
            if (spiderTask.getId().equals(spiderTaskId)) {
                logger.warn("spiderTask {} in wait", spiderTaskId);
                return true;
            }
        }

        return false;
    }


//    private void schedule() {
//        getTaskScheduled.scheduleAtFixedRate(() -> {
//            logger.info("test get exec Service cur active num {}", spiderExecServer.getActiveCount());
//            logger.debug("consumer task getter ....");
//            if (stop) {
//                return;
//            }
//            List<Task> taskList = consumer.receiveTasks();
//            if (taskList == null) {
//                logger.warn("consumer : {} get task list is null", consumer.getName());
//                return;
//            }
//            for (Task taskInfo : taskList) {
//                addTask(consumer, taskInfo);
//            }
//
//        }, 10, 10, TimeUnit.SECONDS);
//    }

    private void execTask(SpiderTask spiderTask) {
        if (stop) {
            return;
        }
        spiderExecServer.execute(spiderTask);
        logger.info("consumer exec task : {}", spiderTask.getId());
    }


    void stop() {
        stop = false;
        spiderExecServer.shutdownNow();

        for (MySpider mySpider : allSpider.values()) {
            mySpider.stop();
        }
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
        spiderInfo.setSpiderNum(allSpider.size());
        int threadNum = 0;
        for (SpiderTask spiderTask : allSpider.values()) {
            threadNum += spiderTask.taskInfo().getThreadNum();
        }
        spiderInfo.setRunThreadNum(threadNum);
        return spiderInfo;
    }


    private class SpiderThreadPoolExecutor extends ThreadPoolExecutor {
        public SpiderThreadPoolExecutor(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit, BlockingQueue<Runnable> workQueue, ThreadFactory threadFactory, RejectedExecutionHandler handler) {
            super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue, threadFactory, handler);
        }

        @Override
        protected void beforeExecute(Thread t, Runnable r) {
            SpiderTask spiderTask = (SpiderTask)r;
            consumer.taskStatusChange(spiderTask.taskInfo().getId(), TaskStatus.RUNNING);
        }

        @Override
        protected void afterExecute(Runnable r, Throwable t) {
            MySpider spiderTask = (MySpider)r;
            consumer.taskStatusChange(spiderTask.taskInfo().getId(), TaskStatus.STOP);

            consumer.statistic(spiderTask.getTaskStatistic());
            consumer.getTaskManager().removerStopSpider(spiderTask);
        }
    }
}

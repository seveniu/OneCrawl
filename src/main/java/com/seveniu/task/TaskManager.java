package com.seveniu.task;

import com.seveniu.customer.Consumer;
import com.seveniu.customer.ConsumerManager;
import com.seveniu.template.PagesTemplate;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by seveniu on 5/12/16.
 * TaskManager
 */
@Component
public class TaskManager {
    private static final int THREAD_THRESHOLD = 200;
    private Logger logger = LoggerFactory.getLogger(TaskManager.class);
    @Autowired
    ConsumerManager consumerManager;

    private LinkedBlockingQueue<Task> waitTaskQueue = new LinkedBlockingQueue<>();
    private LinkedBlockingQueue<Task> runningTask = new LinkedBlockingQueue<>();
    private AtomicInteger curThread = new AtomicInteger(0);

    public TaskManager() {
        execTask();
        checkTaskStatus();
        StringUtils.isNotEmpty("");
    }

    public void addTask(String consumerName, String templateId, String templates, int thread, String... urls) {
        addTask(consumerName, templateId, templates, thread, false, urls);
    }

    public void addTask(String consumerName, String templateId, String templates, int thread, boolean isJS, String... urls) {
        PagesTemplate pagesTemplate = PagesTemplate.fromJson(templates);
        if (pagesTemplate == null) {
            logger.error("consumer {} 's template {} is error", consumerName, templateId);
        } else {
            Consumer consumer = consumerManager.getCustomer(consumerName);
            if (consumer == null) {
                logger.warn("consumer {} is not registered", consumerName);
                return;
            }
            Task task = new Task(generateTaskId(consumerName, templateId), pagesTemplate, templateId, thread, isJS, consumer, urls);
            task.getTaskStatistic().addCreateUrlCount(urls.length);
            waitTaskQueue.add(task);
        }
    }

    private void checkTaskStatus() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    try {
                        Iterator<Task> iterator = runningTask.iterator();
                        while (iterator.hasNext()) {
                            Task task = iterator.next();
                            if (task.getStatus() == Task.STATUS.STOPPED) {
                                iterator.remove();
                                curThread.addAndGet(0 - task.getThreadNum());
                            }
                        }
                        TimeUnit.SECONDS.sleep(5);
                    } catch (Exception e) {
                        e.printStackTrace();
                        logger.error("check thread is error : {}", e.getMessage());
                        break;
                    }
                }
            }
        }, "taskManager-check-task-thread").start();
    }

    private void execTask() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    try {
                        Task task = waitTaskQueue.take();
                        while (curThread.get() < THREAD_THRESHOLD) {
                            curThread.addAndGet(THREAD_THRESHOLD);
                            runningTask.add(task);
                            task.startTask();
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                        logger.error("exec thread is error : {}", e.getMessage());
                        break;
                    }
                }
            }
        }, "taskManager-exec-task-thread").start();
    }

    private String generateTaskId(String customer, String templateId) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        Date date = new Date();
        return sdf.format(date) + "-" + customer + "-" + templateId;
    }

}

package com.seveniu.spider;

import com.seveniu.spider.pageProcessor.MyPageProcessor;
import com.seveniu.spider.pipeline.MyPipeLine;
import com.seveniu.task.SpiderTask;
import com.seveniu.task.TaskStatistic;
import com.seveniu.thriftServer.TaskInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.pipeline.Pipeline;

import java.io.Closeable;
import java.io.IOException;
import java.util.Date;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;


/**
 * Created by seveniu on 5/12/16.
 * MySpider
 */
public class MySpider extends Spider implements SpiderTask {

    private Logger logger = LoggerFactory.getLogger(MySpider.class);
    private TaskInfo spiderConfig;
    private Date createTime = new Date();
    private Date startTime;
    private Date end;
    private TaskStatistic taskStatistic;
    private TemplateType templateType;
    private String templateId;


    MySpider(String id, TaskInfo config, MyPipeLine myPipeLine, MyPageProcessor pageProcessor, TaskStatistic taskStatistic) {
        super(pageProcessor);
        this.createTime = new Date();

        this.uuid = id;

        this.setDownloader(new MyHttpDownload(taskStatistic));
        this.pipelines.add(myPipeLine);

        this.taskStatistic = taskStatistic;
        this.templateId = config.getTemplateId();
        setConfig(config);
        pageProcessor.setMySpider(this);
    }


    private void setConfig(TaskInfo config) {
        this.spiderConfig = config;
        this.threadNum = config.getThreadNum();
        this.setExecutorService(getExecutor(threadNum));
        config.getUrls().forEach(this::addUrl);
    }

    private ExecutorService getExecutor(int nThreads) {
        return new ThreadPoolExecutor(nThreads, nThreads,
                0L, TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<>(),
                new ThreadFactory() {
                    AtomicInteger count = new AtomicInteger(0);

                    @Override
                    public Thread newThread(Runnable r) {
                        return new Thread(r, "spider-" + getId() + "-crawl-" + count.getAndIncrement());
                    }
                });
    }


    /**
     * 开始执行
     */
    @Override
    public void run() {
        this.taskStatistic.setStartTime(new Date());
        logger.info("spider start : {}  ", getId());
        super.run();
        logger.info("spider end : {}  ", getId());
    }

    /**
     * 主动关闭 用这个借口
     * 不用 stop() 以及 close()
     */
    public void stop() {
        logger.info("stop spider {} ", uuid);
        super.stop();
    }

    @Override
    public void close() {
        this.end = new Date();
        destroyEach(downloader);
        destroyEach(pageProcessor);
        destroyEach(scheduler);
        for (Pipeline pipeline : pipelines) {
            destroyEach(pipeline);
        }
        threadPool.shutdown();

        this.taskStatistic.setEndTime(new Date());

        logger.info("spider stopped    :  {}", toString());
    }

    @Override
    public TaskInfo taskInfo() {
        return this.spiderConfig;
    }


    private void destroyEach(Object object) {
        if (object instanceof Closeable) {
            try {
                ((Closeable) object).close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public String getId() {
        return uuid;
    }

    public TaskInfo getSpiderConfig() {
        return spiderConfig;
    }

    public TaskStatistic getTaskStatistic() {
        return taskStatistic;
    }

    @Override
    public String toString() {
        return "MySpider{" +
                "taskInfo=" + spiderConfig +
                ", id='" + uuid + '\'' +
                ", createTime=" + createTime +
                ", startTime=" + startTime +
                ", end=" + end +
                ", taskStatistic=" + taskStatistic +
                ", spiderType=" + templateType +
                '}';
    }
}

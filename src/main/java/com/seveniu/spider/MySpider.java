package com.seveniu.spider;

import com.seveniu.customer.Consumer;
import com.seveniu.spider.imgParse.ImageProcess;
import com.seveniu.spider.pipeline.MyPipeLine;
import com.seveniu.task.SpiderConfig;
import com.seveniu.task.SpiderTask;
import com.seveniu.task.TaskStatistic;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.pipeline.Pipeline;
import us.codecraft.webmagic.processor.PageProcessor;

import java.io.Closeable;
import java.io.IOException;
import java.util.Date;


/**
 * Created by seveniu on 5/12/16.
 * MySpider
 */
public class MySpider extends Spider implements SpiderTask {

    private Logger logger = LoggerFactory.getLogger(MySpider.class);
    private SpiderConfig spiderConfig;
    private Date createTime = new Date();
    private Date startTime;
    private Date end;
    private TaskStatistic taskStatistic;
    private SpiderType spiderType;
    private Consumer consumer;
    private String templateId;
    private ImageProcess imageProcess;


    MySpider(String id, SpiderConfig config, String templateId, Consumer consumer, PageProcessor pageProcessor, TaskStatistic taskStatistic) {
        super(pageProcessor);
        this.createTime = new Date();

        this.uuid = id;

        this.setDownloader(new MyHttpDownload(taskStatistic));
        this.pipelines.add(new MyPipeLine());

        this.taskStatistic = taskStatistic;
        this.consumer = consumer;
        this.templateId = templateId;
        setConfig(config);
        this.imageProcess = ImageProcess.get();
    }


    private void setConfig(SpiderConfig config) {
        this.spiderConfig = config;
        this.threadNum = config.getThreadNum();
        this.addUrl(config.getUrls());
    }


    /**
     * 开始执行
     */
    public void start() {
        this.startTime = new Date();
        this.runAsync();
    }

    /**
     * 主动关闭 用这个借口
     * 不用 stop() 以及 close()
     */
    public void stopSpider() {
        logger.info("set spider stop");
        stop();
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

        logger.info("spider stopped    :  {}", toString());
    }

    @Override
    public SpiderConfig spiderConfig() {
        return this.spiderConfig;
    }

    @Override
    public String getTemplateId() {
        return this.templateId;
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

    public Consumer getConsumer() {
        return consumer;
    }

    public ImageProcess getImageProcess() {
        return imageProcess;
    }

    @Override
    public String toString() {
        return "MySpider{" +
                "spiderConfig=" + spiderConfig +
                ", id='" + uuid + '\'' +
                ", createTime=" + createTime +
                ", startTime=" + startTime +
                ", end=" + end +
                ", taskStatistic=" + taskStatistic +
                ", spiderType=" + spiderType +
                '}';
    }
}

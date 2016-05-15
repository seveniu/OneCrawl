package com.seveniu.task;

import com.seveniu.customer.Consumer;
import com.seveniu.spider.MySpider;
import com.seveniu.template.PagesTemplate;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by seveniu on 5/12/16.
 * Task
 */
public class Task {
    private String id;
    private PagesTemplate pagesTemplate;
    private String templateId;
    private int threadNum;
    private boolean isJS;
    private Consumer consumer;
    private String[] urls;
    private Date createTime = new Date();
    private Date startTime;
    private Date end;
    private MySpider mySpider;
    private TaskStatistic taskStatistic = new TaskStatistic();
    private STATUS status = STATUS.UN_RUN;

    public Task(String id, PagesTemplate pagesTemplate, String templateId, int threadNum, boolean isJS, Consumer consumer, String[] urls) {
        this.id = id;
        this.pagesTemplate = pagesTemplate;
        this.templateId = templateId;
        this.threadNum = threadNum;
        this.isJS = isJS;
        this.consumer = consumer;
        this.urls = urls;
    }

    void startTask() {
        List<String> notRepeatUrls = new ArrayList<>();
        for (String url : urls) {
            boolean has = consumer.has(url);
            if (!has) {
                notRepeatUrls.add(url);
            }
        }
        if(notRepeatUrls.size() > 0) {
            this.mySpider = new MySpider(this);
            for (String notRepeatUrl : notRepeatUrls) {
                mySpider.addUrl(notRepeatUrl);
            }
            this.mySpider.start();
        } else {
            this.status = STATUS.STOPPED;
        }
    }

    STATUS getStatus() {
//        if (this.mySpider == null) {
//            return STATUS.UN_RUN;
//        }
        MySpider.Status status = this.mySpider.getStatus();
        switch (status) {
            case Running:
                this.status = STATUS.RUNNING;
                break;
            case Init:
                this.status = STATUS.INIT;
                break;
            case Stopped:
                this.status = STATUS.STOPPED;
                break;
        }

        return this.status;
    }

    public String getId() {
        return id;
    }

    public PagesTemplate getPagesTemplate() {
        return pagesTemplate;
    }

    public String getTemplateId() {
        return templateId;
    }

    public int getThreadNum() {
        return threadNum;
    }

    public boolean isJS() {
        return isJS;
    }

    public Consumer getConsumer() {
        return consumer;
    }

    public TaskStatistic getTaskStatistic() {
        return taskStatistic;
    }

    @Override
    public String toString() {
        return "Task{" +
                "id='" + id + '\'' +
                ", pagesTemplate=" + pagesTemplate +
                '}';
    }

    public enum STATUS {
        UN_RUN("unrun"),INIT("init"),RUNNING("running"),STOPPED("stopped"),ERROR("error");

        private String status;
        STATUS(String staus) {
            this.status = staus;
        }

        public String getStatus() {
            return status;
        }
    }
}

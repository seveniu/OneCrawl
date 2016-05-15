package com.seveniu.spider;

import com.seveniu.spider.pageProcessor.MyPageProcessor;
import com.seveniu.spider.pipeline.MyPipeLine;
import com.seveniu.task.Task;
import us.codecraft.webmagic.Spider;


/**
 * Created by seveniu on 5/12/16.
 * MySpider
 */
public class MySpider extends Spider {
    private Task task;
    private Long startTime = System.currentTimeMillis();
    private int threadNum;
    private boolean isJS;

    /**
     * @param task 任务
     */
    public MySpider(Task task) {
        super(new MyPageProcessor(task));
        this.task = task;
        this.threadNum = task.getThreadNum();
        this.isJS = task.isJS();
        this.pipelines.add(new MyPipeLine());
    }

    void httpStatusCodeError(int statusCode) {
        task.getTaskStatistic().addNetErrorUrlCount(1);
    }

    void httpTimeOutError() {
        task.getTaskStatistic().addNetErrorUrlCount(1);
    }

    @Override
    public String toString() {
        return "MySpider{" +
                "task='" + task + '\'' +
                ", startTime=" + startTime +
                ", threadNum=" + threadNum +
                ", isJS=" + isJS +
                '}';
    }

}

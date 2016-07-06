package com.seveniu.spider;

import com.seveniu.consumer.Consumer;
import com.seveniu.spider.pageProcessor.MultiListContentProcessor;
import com.seveniu.spider.pageProcessor.TestSinglePageProcessor;
import com.seveniu.spider.pipeline.MyPipeLine;
import com.seveniu.task.TaskStatistic;
import com.seveniu.template.PagesTemplate;
import com.seveniu.thriftServer.TaskInfo;

/**
 * Created by seveniu on 5/17/16.
 * SpiderFactory
 */
public class SpiderFactory {

    public static MySpider getSpider(String id, TemplateType templateType, TaskInfo taskInfo, Consumer consumer, PagesTemplate pagesTemplate) {
        TaskStatistic taskStatistic = new TaskStatistic(taskInfo.getId());
        taskStatistic.setStartUrlCount(taskInfo.getUrls().size());
        switch (templateType) {
            case MULTI_LAYER_CONTENT:
                return new MySpider(id, taskInfo, new MyPipeLine(consumer), new MultiListContentProcessor(pagesTemplate, consumer), taskStatistic);
            case TEST_SINGLE_PAGE:
                return new MySpider(id, taskInfo, new MyPipeLine(consumer), new TestSinglePageProcessor(pagesTemplate, consumer), taskStatistic);
            default:
                throw new IllegalArgumentException("error spider type");
        }
    }
}

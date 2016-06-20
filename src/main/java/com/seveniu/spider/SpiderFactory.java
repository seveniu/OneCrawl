package com.seveniu.spider;

import com.seveniu.consumer.Consumer;
import com.seveniu.consumer.TaskInfo;
import com.seveniu.spider.pageProcessor.multipleListContentProcessor;
import com.seveniu.task.TaskStatistic;
import com.seveniu.template.PagesTemplate;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by seveniu on 5/17/16.
 * SpiderFactory
 */
public class SpiderFactory {

    public static MySpider getSpider(String id, TemplateType templateType, TaskInfo taskInfo, Consumer consumer, PagesTemplate pagesTemplate) {
        switch (templateType) {
            case MULTI_LAYER_CONTENT:
                TaskStatistic taskStatistic = new TaskStatistic(taskInfo.getId());
                taskStatistic.setStartUrlCount(taskInfo.getUrls().size());
//                id = generateSpiderId(id);
                return new MySpider(id, taskInfo, consumer, new multipleListContentProcessor(pagesTemplate), taskStatistic);
            default:
                throw new IllegalArgumentException("error spider type");
        }
    }


    private static String generateSpiderId(String taskId) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        Date date = new Date();
        return sdf.format(date) + "-" + taskId;
    }
}

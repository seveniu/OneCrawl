package com.seveniu.spider;

import com.seveniu.customer.Consumer;
import com.seveniu.spider.imgParse.ImageProcess;
import com.seveniu.spider.pageProcessor.OneLayerProcessor;
import com.seveniu.task.SpiderConfig;
import com.seveniu.task.TaskStatistic;
import com.seveniu.template.PagesTemplate;
import org.crsh.cli.impl.descriptor.IllegalParameterException;

/**
 * Created by seveniu on 5/17/16.
 * SpiderFactory
 */
public class SpiderFactory {

    public static MySpider getSpider(String id, SpiderType spiderType, SpiderConfig spiderConfig, String templateId, Consumer consumer, PagesTemplate pagesTemplate) throws IllegalParameterException {
        switch (spiderType) {
            case ONE_LAYER:
                TaskStatistic taskStatistic = new TaskStatistic();
                taskStatistic.setStartUrlCount(spiderConfig.getUrls().length);
                return new MySpider(id, spiderConfig, templateId, consumer, new OneLayerProcessor(consumer, pagesTemplate, taskStatistic), taskStatistic);
            default:
                throw new IllegalParameterException("error spider type");
        }
    }
}

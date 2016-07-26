package com.seveniu.spider.pageProcessor;

import com.seveniu.common.json.Json;
import com.seveniu.consumer.Consumer;
import com.seveniu.node.Node;
import com.seveniu.spider.parse.ParseHtml;
import com.seveniu.spider.parse.ParseResult;
import com.seveniu.task.TaskStatistic;
import com.seveniu.template.PagesTemplate;
import com.seveniu.template.def.Template;
import com.seveniu.thriftServer.TaskInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.processor.PageProcessor;

/**
 * Created by seveniu on 5/12/16.
 * MyPageProcessor
 */
public abstract class MyPageProcessor implements PageProcessor {
    public static final String CONTEXT_NODE = "node";
    static final String SERIAL_NUM = "serialNum";
    static final String TEMPLATE = "serialNum";
    Logger logger = LoggerFactory.getLogger(this.getClass());
    protected Consumer consumer;

    PagesTemplate pagesTemplate;
    TaskStatistic statistic;
    String taskId;

    public MyPageProcessor(PagesTemplate pagesTemplate, Consumer consumer) {
        this.pagesTemplate = pagesTemplate;
        this.consumer = consumer;
    }

    public void setMySpider(TaskInfo taskInfo, TaskStatistic statistic) {
        this.statistic = statistic;
        this.taskId = taskInfo.getId();
    }

//    MyPageProcessor(Consumer consumer, PagesTemplate pagesTemplate, TaskStatistic statistic) {
//        this.consumer = consumer;
//        this.pagesTemplate = pagesTemplate;
//        this.statistic = statistic;
//    }

    @Override
    public void process(Page page) {
        String url = page.getUrl().get();
        Template template = getTemplate(page);
        if (template == null) {
            return;
        }


        //解析页面
        ParseResult parseResult = ParseHtml.parseHtml(url, page.getHtml(), template);

        // 统计
        statistic.addSuccessUrlCount(1);
        Node contextNode = (Node) page.getRequest().getExtra(CONTEXT_NODE);
        if (parseResult.hasError()) {
            statistic.addParseErrorCount(new String[]{url});
            if (contextNode != null) {
                statistic.addErrorNodeCount(1);
            }
            logger.warn("parse html error : {}, url:{}", Json.toJson(parseResult.getParseError()), page.getUrl());
        } else {
            statistic.addDoneUrlCount(1);
        }


        process0(page, parseResult);
    }

    abstract void process0(Page page, ParseResult parseResult);


    protected abstract Template getTemplate(Page page);


    @Override
    public Site getSite() {
        return Site.me()
                .setUserAgent("Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/28.0.1500.71 Safari/537.36")
                .setRetryTimes(3);
    }

}

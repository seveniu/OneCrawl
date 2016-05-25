package com.seveniu.spider.pageProcessor;

import com.seveniu.common.json.Json;
import com.seveniu.customer.Consumer;
import com.seveniu.node.Node;
import com.seveniu.parse.PageResult;
import com.seveniu.parse.ParseHtml;
import com.seveniu.parse.ParseResult;
import com.seveniu.task.TaskStatistic;
import com.seveniu.template.PagesTemplate;
import com.seveniu.template.def.Template;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Request;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.processor.PageProcessor;

/**
 * Created by seveniu on 5/12/16.
 * MyPageProcessor
 */
public class OneLayerProcessor implements PageProcessor {
    public static final String CONTEXT_NODE = "node";
    private static final String SERIAL_NUM = "serialNum";
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    private PagesTemplate pagesTemplate;
    private TaskStatistic statistic;
    private Consumer consumer;

    public OneLayerProcessor(Consumer consumer, PagesTemplate pagesTemplate, TaskStatistic statistic) {
        this.consumer = consumer;
        this.pagesTemplate = pagesTemplate;
        this.statistic = statistic;
    }

    @Override
    public void process(Page page) {
        String url = page.getUrl().get();
        // 统计
        statistic.addSuccessUrlCount(1);


        // 获取页面序号
        Integer serialNum = (Integer) page.getRequest().getExtra(SERIAL_NUM);
        if (serialNum == null) {
            serialNum = 0;
            page.getRequest().putExtra(SERIAL_NUM, serialNum);
        }
        // 根据序号找到对应模板
        Template template = pagesTemplate.getTemplate(serialNum);
        if (template == null) {
            logger.error("can't find template by serial num : {}", serialNum);
            return;
        }

        Node contextNode = (Node) page.getRequest().getExtra(CONTEXT_NODE);

        //解析页面
        long start = System.currentTimeMillis();
        ParseResult parseResult = ParseHtml.parseHtml(url, page.getHtml(), template);
        logger.debug("parse cost time : {}", System.currentTimeMillis() - start);
        if (parseResult.getParseError() != null) {
            statistic.addParseErrorCount(1);
            if (contextNode != null) {
                statistic.addErrorNodeCount(1);
            }
            logger.warn("parse html error : {}", Json.toJson(parseResult.getParseError()));
            return;
        } else {
            statistic.addDoneUrlCount(1);
        }


        if (serialNum == 0) { // 跳转页面
            targetPage(page, parseResult);
        } else if (serialNum == 1) {
            contentPage(page, parseResult);
        }

    }

    private void targetPage(Page page, ParseResult parseResult) {
        String url = page.getUrl().get();
        // 处理解析的结果
        // 下一页链接
        if (parseResult.hasNextPageLinks()) {
            for (String next : parseResult.getNextPageLinks()) {
                page.addTargetRequest(new Request(next).putExtra(SERIAL_NUM, 0));
            }
            // 统计
            statistic.addCreateUrlCount(parseResult.getNextPageLinks().size());
        }

        // 跳转链接
        if (parseResult.hasLinks()) {
            for (String targetLink : parseResult.getTargetLinks()) {
                if (consumer.has(targetLink)) { //
                    logger.debug("url is repeat : {} ", targetLink);
                    statistic.addRepeatUrlCount(1);
                } else {
                    page.addTargetRequest(new Request(targetLink).putExtra(CONTEXT_NODE, new Node(targetLink)).putExtra(SERIAL_NUM, 1));
                }
            }
            // 统计
            statistic.addCreateUrlCount(parseResult.getTargetLinks().size());
            statistic.addTargetUrlCount(parseResult.getTargetLinks().size());
        }
    }

    private void contentPage(Page page, ParseResult parseResult) {
        String url = page.getUrl().get();
        // 处理解析的结果
        Node contextNode = (Node) page.getRequest().getExtra(CONTEXT_NODE);
        // 内容
        if (parseResult.getFieldResults() != null && parseResult.getFieldResults().size() > 0) {
            // 有文本字段,就获取 node 添加内容
            if (contextNode == null) {
                logger.error("context Node is null");
            } else {
                statistic.addCreateNodeCount(1);
                contextNode.addPageResult(new PageResult(url, parseResult.getFieldResults()));
            }
        } else {
            logger.error("content page field is null, url : {}", url);
        }

        // 下一页链接
        if (parseResult.hasNextPageLinks()) {
            for (String next : parseResult.getNextPageLinks()) {
                page.addTargetRequest(new Request(next).putExtra(CONTEXT_NODE, contextNode).putExtra(SERIAL_NUM, 1));
            }
            // 统计
            statistic.addCreateUrlCount(parseResult.getNextPageLinks().size());
        } else {
            statistic.addDoneNodeCount(1);
            page.putField(CONTEXT_NODE, contextNode);
//            page.putField(TASK, task);
        }
    }


    @Override
    public Site getSite() {
        return Site.me()
                .setUserAgent("Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/28.0.1500.71 Safari/537.36")
                .setRetryTimes(3);
    }

}

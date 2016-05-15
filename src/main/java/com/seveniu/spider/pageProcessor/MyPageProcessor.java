package com.seveniu.spider.pageProcessor;

import com.seveniu.node.Node;
import com.seveniu.parse.ParseHtml;
import com.seveniu.parse.ParseResult;
import com.seveniu.task.Task;
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
public class MyPageProcessor implements PageProcessor {
    private Task task;
    public static final String CONTEXT_NODE = "node";
    public static final String TASK = "task";
    private static final String SERIAL_NUM = "serialNum";
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    public MyPageProcessor(Task task) {
        this.task = task;
    }

    @Override
    public void process(Page page) {
        // 统计
        task.getTaskStatistic().addSuccessUrlCount(1);


        // 获取页面序号
        Integer serialNum = (Integer) page.getRequest().getExtra(SERIAL_NUM);
        if (serialNum == null) {
            serialNum = 0;
            page.getRequest().putExtra(SERIAL_NUM, serialNum);
        }
        Template template = task.getPagesTemplate().getTemplate(serialNum);
        if (template == null) {
            return;
        }

        //根据序号找到对应模板,解析页面
        ParseResult parseResult = ParseHtml.parseHtml(page.getUrl().get(), page.getHtml(), template);
        if(parseResult.getParseError() != null) {
            task.getTaskStatistic().addParseErrorCount(1);
        } else {
            task.getTaskStatistic().addDoneUrlCount(1);
            return;
        }


        Node contextNode = (Node) page.getRequest().getExtra(CONTEXT_NODE);
        // 处理解析的结果
        // 内容
        if (parseResult.getFieldResults() != null && parseResult.getFieldResults().size() > 0) {
            // 有文本字段,就获取 node 添加内容
            if (contextNode == null) {
                contextNode = new Node(page.getUrl().get());
                page.getRequest().putExtra(CONTEXT_NODE, contextNode);
                task.getTaskStatistic().addCreateNodeCount(1);
            }
            contextNode.addResult(parseResult.getFieldResults());
        }

        // 下一页链接
        if (parseResult.hasNextPageLinks()) {
            for (String next : parseResult.getNextPageLinks()) {
                page.addTargetRequest(new Request(next).putExtra(CONTEXT_NODE, contextNode).putExtra(SERIAL_NUM, serialNum));
            }
            // 统计
            task.getTaskStatistic().addCreateUrlCount(parseResult.getNextPageLinks().size());
        }

        // 跳转链接
        if (parseResult.hasLinks()) {
            for (String targetLink : parseResult.getTargetLinks()) {
                if (contextNode == null && task.getConsumer().has(targetLink)) { //
                    logger.debug("url is repeat : {} ", targetLink);
                    task.getTaskStatistic().addRepeatUrlCount(1);
                } else {
                    page.addTargetRequest(new Request(targetLink).putExtra(CONTEXT_NODE, contextNode).putExtra(SERIAL_NUM, serialNum + 1));
                }
            }
            // 统计
            task.getTaskStatistic().addCreateUrlCount(parseResult.getTargetLinks().size());
        }

        // 没有 跳转链接 也没有 下一页链接, 则完成 node
        if (!parseResult.hasLinks() && !parseResult.hasNextPageLinks()) {
            task.getTaskStatistic().addDoneNodeCount(1);
            page.putField(CONTEXT_NODE, contextNode);
            page.putField(TASK, task);
        }

    }


    @Override
    public Site getSite() {
        return Site.me()
                .setUserAgent("Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/28.0.1500.71 Safari/537.36")
                .setRetryTimes(3);
    }

}

package com.seveniu.spider.pageProcessor;

import com.seveniu.consumer.Consumer;
import com.seveniu.node.Node;
import com.seveniu.spider.parse.Link;
import com.seveniu.spider.parse.PageResult;
import com.seveniu.spider.parse.ParseResult;
import com.seveniu.template.PagesTemplate;
import com.seveniu.template.def.Template;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Request;

/**
 * 多级内容页 大于1级
 * Created by seveniu on 5/12/16.
 * MyPageProcessor
 */
public class MultiListContentProcessor extends MyPageProcessor {
    public MultiListContentProcessor(PagesTemplate pagesTemplate, Consumer consumer) {
        super(pagesTemplate, consumer);
    }

    @Override
    protected Template getTemplate(Page page) {
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
            return null;
        }
        return template;
    }

    @Override
    void process0(Page page, ParseResult parseResult) {

        Integer curSerialNum = (Integer) page.getRequest().getExtra(SERIAL_NUM);
        int pagesNum = pagesTemplate.pagesNum();
        if (curSerialNum < pagesNum - 1) { // 跳转页面
            boolean createNode = false;
            if (curSerialNum + 2 == pagesNum) {// 倒数第二页
                createNode = true;
            }
            targetPage(page, parseResult, curSerialNum, createNode);
        } else if (curSerialNum == pagesNum - 1) {
            contentPage(page, parseResult, curSerialNum);
        }
    }

    private void targetPage(Page page, ParseResult parseResult, Integer curSerialNum, boolean createNode) {
//        String url = page.getUrl().get();
        // 处理解析的结果
        // 下一页链接
        if (parseResult.hasNextPageLinks()) {
            for (Link next : parseResult.getNextPageLinks()) {
                page.addTargetRequest(new Request(next.getUrl()).putExtra(SERIAL_NUM, curSerialNum));
            }
            // 统计
            statistic.addCreateUrlCount(parseResult.getNextPageLinks().size());
        }

        // 跳转链接
        if (parseResult.hasLinks()) {
            for (Link targetLink : parseResult.getTargetLinks()) {
                if (consumer.getClient().has(targetLink.getUrl())) { //
                    logger.debug("url is repeat : {} ", targetLink);
                    statistic.addRepeatUrlCount(1);
                } else {
                    Request request = new Request(targetLink.getUrl()).putExtra(SERIAL_NUM, curSerialNum + 1);
                    if (createNode) {
                        Node node = new Node(targetLink.getUrl(), taskId);
                        request.putExtra(CONTEXT_NODE, node);
                    }
                    page.addTargetRequest(request);
                }
            }
            // 统计
            statistic.addCreateUrlCount(parseResult.getTargetLinks().size());
            statistic.addTargetUrlCount(parseResult.getTargetLinks().size());
        }
    }

    private void contentPage(Page page, ParseResult parseResult, Integer curSerialNum) {
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
                contextNode.addPageResult(new PageResult().setUrl(url).setFieldResults(parseResult.getFieldResults()));
            }
        } else {
            logger.error("content page field is null, url : {}", url);
        }

        // 下一页链接
        if (parseResult.hasNextPageLinks()) {
            for (Link next : parseResult.getNextPageLinks()) {
                page.addTargetRequest(new Request(next.getUrl()).putExtra(CONTEXT_NODE, contextNode).putExtra(SERIAL_NUM, curSerialNum));
            }
            // 统计
            statistic.addCreateUrlCount(parseResult.getNextPageLinks().size());
        } else {
            statistic.addDoneNodeCount(1);
            page.putField(CONTEXT_NODE, contextNode);
//            page.putField(TASK, task);
        }
    }

}

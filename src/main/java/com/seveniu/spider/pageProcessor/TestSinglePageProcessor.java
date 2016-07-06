package com.seveniu.spider.pageProcessor;

import com.seveniu.consumer.Consumer;
import com.seveniu.node.Node;
import com.seveniu.parse.FieldResult;
import com.seveniu.parse.PageResult;
import com.seveniu.parse.ParseResult;
import com.seveniu.template.PagesTemplate;
import com.seveniu.template.def.Field;
import com.seveniu.template.def.FieldType;
import com.seveniu.template.def.Template;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Request;

/**
 * Created by seveniu on 7/5/16.
 * TestSinglePageProcessor
 * 测试用,只解析一页,链接不跳转
 */
public class TestSinglePageProcessor extends MyPageProcessor {


    public TestSinglePageProcessor(PagesTemplate pagesTemplate, Consumer consumer) {
        super(pagesTemplate, consumer);
    }

    @Override
    void process0(Page page, ParseResult parseResult) {
        singlePage(page, parseResult);
    }

    @Override
    protected Template getTemplate(Page page) {
        // 根据序号找到对应模板
        Template template = pagesTemplate.getTemplate(0);
        if (template == null) {
            logger.error("can't find template by serial num : {}", 0);
            return null;
        }
        for (Field field : template.getFields()) {
            if (field.getContentType() == FieldType.TARGET_LINK.getId()
                    || field.getContentType() == FieldType.NEXT_LINK.getId()) {
                field.setContentType(FieldType.TEXT_LINK.getId());
            }
        }
        return template;
    }


    private void singlePage(Page page, ParseResult parseResult) {
        String url = page.getUrl().get();
        // 处理解析的结果
        Node contextNode = (Node) page.getRequest().getExtra(CONTEXT_NODE);
        // 内容
        if (parseResult.getFieldResults() != null && parseResult.getFieldResults().size() > 0) {
            // 有文本字段,就获取 node 添加内容
            if (contextNode == null) {
                contextNode = new Node(url, taskId);
                statistic.addCreateNodeCount(1);
            }
            contextNode.addPageResult(new PageResult().setUrl(url).setFieldResults(parseResult.getFieldResults()));
        } else {
            logger.error("content page field is null, url : {}", url);
        }

    }

}

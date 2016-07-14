package com.seveniu.spider.pipeline;

import com.seveniu.consumer.Consumer;
import com.seveniu.node.Node;
import com.seveniu.parse.FieldResult;
import com.seveniu.parse.PageResult;
import com.seveniu.spider.MySpider;
import com.seveniu.spider.pageProcessor.MyPageProcessor;
import com.seveniu.template.def.FieldType;
import us.codecraft.webmagic.ResultItems;
import us.codecraft.webmagic.Task;
import us.codecraft.webmagic.pipeline.Pipeline;

/**
 * Created with IntelliJ IDEA.
 * User: niu
 * Date: 2014/6/11
 * Time: 12:03
 * Project: dhlz-spider
 */
public class MyPipeLine implements Pipeline {

    private Consumer consumer;

    public MyPipeLine(Consumer consumer) {
        this.consumer = consumer;
    }

    @Override
    public void process(ResultItems resultItems, Task task) {

        if (resultItems.getAll().size() > 0) {

            Node node = resultItems.get(MyPageProcessor.CONTEXT_NODE);
            MySpider mySpider = (MySpider) task;

            // 处理图片
            if (mySpider.getHtmlImageProcess() != null) {
                for (PageResult pageResult : node.getPages()) {
                    for (FieldResult fieldResult : pageResult.getFieldResults()) {
                        if (fieldResult.getFieldHtmlType() == FieldType.HTML_TEXT.getId()) {
                            String result = mySpider.getHtmlImageProcess().process(node.getUrl(),fieldResult.getResult(), mySpider.getSite());
                            fieldResult.setResult(result);
                        }
                    }
                }
            }

            // out 输出
            consumer.transfer(node);
        }
    }


}

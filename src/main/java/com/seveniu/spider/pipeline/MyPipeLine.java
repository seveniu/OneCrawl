package com.seveniu.spider.pipeline;

import com.seveniu.common.json.Json;
import com.seveniu.node.Node;
import com.seveniu.spider.pageProcessor.MyPageProcessor;
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


    @Override
    public void process(ResultItems resultItems, Task task) {

        if (resultItems.getAll().size() > 0) {

            Node node = resultItems.get(MyPageProcessor.CONTEXT_NODE);
            com.seveniu.task.Task task1 = resultItems.get(MyPageProcessor.TASK);
            task1.getConsumer().out(Json.toJson(node));
        }
    }


}

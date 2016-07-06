package com.seveniu.task;

import com.seveniu.AppCrawl;
import com.seveniu.consumer.ConsumerManager;
import com.seveniu.consumer.DefaultConsumer;
import com.seveniu.consumer.TaskInfo;
import com.seveniu.template.PagesTemplate;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

/**
 * Created by seveniu on 5/14/16.
 * TaskManagerTest
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = AppCrawl.class)
public class TaskInfoManagerTest {
    @Autowired
    SpiderRegulate spiderRegulate;
    @Autowired
    ConsumerManager consumerManager;

    @Before
    public void setUp() throws Exception {

    }

    @Test
    public void addTask() throws Exception {
//        TestConsumer testConsumer = new TestConsumer();
//        consumerManager.regConsumer(testConsumer);
        String templateId = "1234";
        int templateType = 1;
        String template =
//                "{\"templates\":[{\"fields\":[{\"type\":2,\"contentType\":0,\"defaultValue\":\"\",\"xpath\":\"//*[@class=\\\"link_title\\\"]/a\",\"regex\":null,\"trim\":false}]},{\"fields\":[{\"type\":1,\"contentType\":0,\"defaultValue\":\"\",\"xpath\":\"//*[@id=\\\"article_content\\\"]\",\"regex\":null,\"trim\":false},{\"type\":1,\"contentType\":0,\"defaultValue\":\"\",\"xpath\":\"//*[@id=\\\"article_details\\\"]/div[1]/h1/span/a\",\"regex\":null,\"trim\":false}]}]}";
//                "{\"templates\":[{\"fields\":[{\"type\":4,\"contentType\":0,\"defaultValue\":\"\",\"xpath\":\"//*[@class=\\\"link_title\\\"]/a\",\"regex\":null,\"trim\":false}]}]}";
//                "{\"templates\":[{\"fields\":[{\"type\":2,\"contentType\":0,\"defaultValue\":\"\",\"xpath\":\"//*[@class=\\\"link_title\\\"]/a\",\"regex\":null,\"trim\":false}]},{\"fields\":[{\"type\":1,\"contentType\":0,\"defaultValue\":\"\",\"xpath\":\"//*[@id=\\\"blog_rank\\\"]/li[1]/span\",\"regex\":null,\"trim\":false}]}]}]}]}";
                "{\"templates\":[{\"name\":\"lb\",\"url\":\"http://www.xyxdq.cn/l1/\",\"fields\":[{\"type\":2,\"contentType\":5,\"name\":\"链接\",\"defaultValue\":null,\"xpath\":\"//html/body/table/tbody/tr/td/table/tbody/tr/td/table/tbody/tr/td[@class='f2']/span[@class='f2']/a[@class='t13']\",\"regex\":null,\"must\":false}]},{\"name\":\"nr\",\"url\":\"http://www.xyxdq.cn/l1/201211/t20121106_394565.htm\",\"fields\":[{\"type\":5,\"contentType\":2,\"name\":\"标题\",\"defaultValue\":null,\"xpath\":\"//html/body/table/tbody/tr/td/table/tbody/tr/td[@class='f1']/h3\",\"regex\":null,\"must\":false},{\"type\":1,\"contentType\":7,\"name\":\"内容\",\"defaultValue\":null,\"xpath\":\"//html/body/table[3]/tbody/tr/td/table[5]/tbody\",\"regex\":null,\"must\":false}]}]}";
        PagesTemplate pagesTemplate = PagesTemplate.fromJson("1234", template);
        ArrayList<String> urls = new ArrayList<String>() {{
            add("http://www.xyxdq.cn/l1/");
        }};
        TaskInfo taskInfo = new TaskInfo("taskId", "taskName", urls,
                templateId, templateType, template, 2, 0, 0);
//        spiderRegulate.addTask("xfasdfasdfen", TemplateType.MULTI_LAYER_CONTENT, pagesTemplate, new TestConsumer(), taskInfo);

        TimeUnit.SECONDS.sleep(50);
    }


    public class TestConsumer extends DefaultConsumer {
        public TestConsumer() {
            super("test");
        }

        @Override
        public boolean has(String url) {
            if (url.equals("http://blog.csdn.net/qq_34640060/article/details/51254690")) {
                return true;
            }
            return false;
        }

    }
}
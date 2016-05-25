package com.seveniu.task;

import com.seveniu.Application;
import com.seveniu.customer.ConsumerManager;
import com.seveniu.customer.DefaultConsumer;
import com.seveniu.spider.SpiderType;
import com.seveniu.spider.imgParse.ImageProcess;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.concurrent.TimeUnit;

/**
 * Created by seveniu on 5/14/16.
 * TaskManagerTest
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
public class TaskManagerTest {
    @Autowired
    TaskManager taskManager;
    @Autowired
    ConsumerManager consumerManager;

    @Before
    public void setUp() throws Exception {

    }

    @Test
    public void addTask() throws Exception {
        TestConsumer testConsumer = new TestConsumer();
        consumerManager.regCustomer(testConsumer);
        taskManager.addTask(SpiderType.ONE_LAYER, "test", "1234",
//                "{\"templates\":[{\"fields\":[{\"htmlType\":2,\"contentType\":0,\"defaultValue\":\"\",\"xpath\":\"//*[@class=\\\"link_title\\\"]/a\",\"regex\":null,\"trim\":false}]},{\"fields\":[{\"htmlType\":1,\"contentType\":0,\"defaultValue\":\"\",\"xpath\":\"//*[@id=\\\"article_content\\\"]\",\"regex\":null,\"trim\":false},{\"htmlType\":1,\"contentType\":0,\"defaultValue\":\"\",\"xpath\":\"//*[@id=\\\"article_details\\\"]/div[1]/h1/span/a\",\"regex\":null,\"trim\":false}]}]}",
//                "{\"templates\":[{\"fields\":[{\"htmlType\":4,\"contentType\":0,\"defaultValue\":\"\",\"xpath\":\"//*[@class=\\\"link_title\\\"]/a\",\"regex\":null,\"trim\":false}]}]}",
//                "{\"templates\":[{\"fields\":[{\"htmlType\":2,\"contentType\":0,\"defaultValue\":\"\",\"xpath\":\"//*[@class=\\\"link_title\\\"]/a\",\"regex\":null,\"trim\":false}]},{\"fields\":[{\"htmlType\":1,\"contentType\":0,\"defaultValue\":\"\",\"xpath\":\"//*[@id=\\\"blog_rank\\\"]/li[1]/span\",\"regex\":null,\"trim\":false}]}]}]}]}",
                "{\"templates\":[{\"name\":\"lb\",\"url\":\"http://www.xyxdq.cn/l1/\",\"fields\":[{\"htmlType\":2,\"contentType\":5,\"name\":\"链接\",\"defaultValue\":null,\"xpath\":\"//html/body/table/tbody/tr/td/table/tbody/tr/td/table/tbody/tr/td[@class='f2']/span[@class='f2']/a[@class='t13']\",\"regex\":null,\"must\":false}]},{\"name\":\"nr\",\"url\":\"http://www.xyxdq.cn/l1/201211/t20121106_394565.htm\",\"fields\":[{\"htmlType\":5,\"contentType\":2,\"name\":\"标题\",\"defaultValue\":null,\"xpath\":\"//html/body/table/tbody/tr/td/table/tbody/tr/td[@class='f1']/h3\",\"regex\":null,\"must\":false},{\"htmlType\":1,\"contentType\":7,\"name\":\"内容\",\"defaultValue\":null,\"xpath\":\"//html/body/table[3]/tbody/tr/td/table[5]/tbody\",\"regex\":null,\"must\":false}]}]}",
                new SpiderConfig()
                        .setJS(false)
                        .setThreadNum(2)
                        .setUrls(new String[]{"http://www.xyxdq.cn/l1/"}),new ImageProcess());

        TimeUnit.SECONDS.sleep(50);
    }


    public class TestConsumer extends DefaultConsumer{
        @Override
        public String getName() {
            return "test";
        }

        @Override
        public boolean has(String url) {
            if(url.equals("http://blog.csdn.net/qq_34640060/article/details/51254690")) {
                return true;
            }
            return false;
        }
    }
}
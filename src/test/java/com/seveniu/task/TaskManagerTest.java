package com.seveniu.task;

import com.seveniu.Application;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.concurrent.TimeUnit;

import static org.junit.Assert.*;

/**
 * Created by seveniu on 5/14/16.
 * TaskManagerTest
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
public class TaskManagerTest {
    @Autowired
    TaskManager taskManager;
    @Before
    public void setUp() throws Exception {

    }

    @Test
    public void addTask() throws Exception {
        taskManager.addTask("default","ahah",
                "{\"templates\":[{\"fields\":[{\"htmlType\":4,\"contentType\":0,\"defaultValue\":\"\",\"xpath\":\"//*[@class=\\\"link_title\\\"]/a\",\"regex\":null,\"trim\":false}]}]}",
//                "{\"templates\":[{\"fields\":[{\"htmlType\":2,\"contentType\":0,\"defaultValue\":\"\",\"xpath\":\"//*[@class=\\\"link_title\\\"]/a\",\"regex\":null,\"trim\":false}]},{\"fields\":[{\"htmlType\":1,\"contentType\":0,\"defaultValue\":\"\",\"xpath\":\"//*[@id=\\\"blog_rank\\\"]/li[1]/span\",\"regex\":null,\"trim\":false}]}]}]}]}",
                1,false,"http://blog.csdn.net/sunnydogzhou?viewmode=contents");

        TimeUnit.SECONDS.sleep(50);
    }

}
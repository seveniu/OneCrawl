package com.seveniu.dataProcess;

import com.seveniu.AppCrawl;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.Assert.*;

/**
 * Created by seveniu on 10/24/16.
 * *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = AppCrawl.class)
@ActiveProfiles("home")
public class MysqlDataQueueTest {
    @Autowired
    MysqlDataQueue mysqlDataQueue;
    @Test
    public void addData() throws Exception {
//        mysqlDataQueue.addData("asdf","kkkkkkk");
    }

}
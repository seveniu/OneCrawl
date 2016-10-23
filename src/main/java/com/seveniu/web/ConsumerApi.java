package com.seveniu.web;

import com.alibaba.fastjson.JSON;
import com.seveniu.TaskQueue;
import com.seveniu.consumer.Consumer;
import com.seveniu.consumer.ConsumerManager;
import com.seveniu.consumer.ConsumerTaskManager;
import com.seveniu.task.SpiderRegulate;
import com.seveniu.task.TaskStatistic;
import com.seveniu.thriftServer.ConsumerConfig;
import com.seveniu.thriftServer.ResourceInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

/**
 * Created by seveniu on 6/6/16.
 * ConsumerApi
 */
@Controller
@RequestMapping("/api/consumer")
public class ConsumerApi {
    private Logger logger = LoggerFactory.getLogger(ConsumerApi.class);

    private final ConsumerManager consumerManager;
    @Autowired
    TaskQueue taskQueue;

    @Autowired
    public ConsumerApi(ConsumerManager consumerManager) {
        this.consumerManager = consumerManager;
    }

    @RequestMapping(value = "/reg", method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
    @ResponseBody
    public ResponseEntity<String> reg(@RequestBody ConsumerConfig remoteConsumerConfig) {
        try {
            String result = consumerManager.regRemoteConsumer(remoteConsumerConfig);
            return ResponseEntity.ok().body(result);
        } catch (Exception e) {
            logger.warn("reg consumer failed : error : {}", e.getMessage());
            return ResponseEntity.badRequest().body(e.getMessage());
        }

    }

    @RequestMapping(value = "/running-task", method = RequestMethod.GET, produces = "text/html;charset=UTF-8")
    @ResponseBody
    public ResponseEntity<String> addTask(String uuid) {
        try {
            List<TaskStatistic> result = consumerManager.getConsumerByUUID(uuid).getTaskManager().getRunningTaskInfo();
            return ResponseEntity.ok(JSON.toJSONString(result));
        } catch (Exception e) {
            logger.warn("reg consumer failed : error : {}", e.getMessage());
            return ResponseEntity.badRequest().body(e.getMessage());
        }

    }

    @RequestMapping(value = "/resource-info", method = RequestMethod.GET, produces = "text/html;charset=UTF-8")
    @ResponseBody
    public ResponseEntity<String> getResourceInfo(String uuid) {
        try {
            Consumer consumer = consumerManager.getConsumerByUUID(uuid);
            ConsumerTaskManager taskManager = consumer.getTaskManager();
            ResourceInfo resourceInfo = new ResourceInfo(
                    taskManager.getMaxRunning(),
                    taskManager.getMaxWait(),
                    taskManager.getCurRunningSize(),
                    taskQueue.getWaitSize(consumer.getName())
            );
            return ResponseEntity.ok(JSON.toJSONString(resourceInfo));
        } catch (Exception e) {
            logger.warn("reg consumer failed : error : {}", e.getMessage());
            return ResponseEntity.badRequest().body(e.getMessage());
        }

    }

    @RequestMapping(value = "/task-summary", method = RequestMethod.GET, produces = "text/html;charset=UTF-8")
    @ResponseBody
    public ResponseEntity<String> getTaskSummary(String uuid) {
        try {
            SpiderRegulate.SpiderInfo taskManager = consumerManager.getConsumerByUUID(uuid).getTaskManager().getSpiderInfo();
            return ResponseEntity.ok(JSON.toJSONString(taskManager));
        } catch (Exception e) {
            logger.warn("get consumer task summary failed : error : {}", e.getMessage());
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    String getBody(HttpServletRequest request) throws IOException {
        StringBuilder stringBuilder = new StringBuilder();
        BufferedReader bufferedReader = null;
        try {
            InputStream inputStream = request.getInputStream();
            if (inputStream != null) {
                bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                char[] charBuffer = new char[128];
                int bytesRead = -1;
                while ((bytesRead = bufferedReader.read(charBuffer)) > 0) {
                    stringBuilder.append(charBuffer, 0, bytesRead);
                }
            } else {
                stringBuilder.append("");
            }
        } finally {
            if (bufferedReader != null) {
                bufferedReader.close();
            }
        }
        return stringBuilder.toString();
    }
}

package com.seveniu.web;

import com.seveniu.consumer.ConsumerManager;
import com.seveniu.consumer.remote.RemoteConsumerConfig;
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

/**
 * Created by seveniu on 6/6/16.
 * ConsumerApi
 */
@Controller
@RequestMapping("/consumer")
public class ConsumerApi {
    private Logger logger = LoggerFactory.getLogger(ConsumerApi.class);

    @Autowired
    ConsumerManager consumerManager;

    @RequestMapping(value = "/reg", method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
    @ResponseBody
    public ResponseEntity<String> reg(@RequestBody RemoteConsumerConfig remoteConsumerConfig) {
        try {
            boolean result = consumerManager.regRemoteConsumer(remoteConsumerConfig);
            if(result) {
                return ResponseEntity.ok().body("true");
            } else {
                return ResponseEntity.ok().body("exist");
            }
        } catch (Exception e) {
            logger.warn("reg consumer failed : error : {}",e.getMessage());
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

package com.seveniu.customer;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;

/**
 * Created by seveniu on 5/24/16.
 * RemoteConsumer
 */
public class RemoteConsumer extends Consumer {
    private Logger logger = LoggerFactory.getLogger(RemoteConsumer.class);

    private RemoteConsumerConfig remoteConsumerConfig;

    private RemoteRequest remoteRequest;

    public RemoteConsumer(RemoteConsumerConfig remoteConsumerConfig) {
        this.remoteConsumerConfig = remoteConsumerConfig;
        this.remoteRequest = new RemoteRequest();
    }

    @Override
    public String getName() {
        return remoteConsumerConfig.getName();
    }

    @Override
    public boolean has(String url) {
        StringBuilder sb = new StringBuilder(remoteConsumerConfig.getDuplicateUrl());
        sb.append("?url=").append(url);
        try {
            return Boolean.valueOf(remoteRequest.get(sb.toString()));
        } catch (URISyntaxException e) {
            e.printStackTrace();
            logger.error("url error : {}", e.getMessage());
            return false;
        }
    }

    @Override
    public void out(String result) {
        remoteRequest.post(remoteConsumerConfig.getReceiveUrl(), result);
    }


    private class RemoteRequest {
        CloseableHttpClient httpClient;

        public RemoteRequest() {
            this.httpClient = HttpClients.createDefault();
        }

        public String get(String url) throws URISyntaxException {
            return get(new URI(url));
        }

        public String get(URI url) {
            InputStream inputStream = null;
            CloseableHttpResponse response = null;
            try {
                response = httpClient.execute(
                        new HttpGet(url));
                HttpEntity entity = response.getEntity();
                inputStream = entity.getContent();
                return IOUtils.toString(inputStream, "UTF-8");
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    if (inputStream != null) {
                        inputStream.close();
                    }
                    if (response != null) {
                        response.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }

        public String post(String url, String body) {
            InputStream inputStream = null;
            CloseableHttpResponse response = null;
            try {
                HttpPost post = new HttpPost(url);
                post.setEntity(new StringEntity(body));
                response = httpClient.execute(post);
                HttpEntity entity = response.getEntity();
                inputStream = entity.getContent();
                return IOUtils.toString(inputStream, "UTF-8");
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    if (inputStream != null) {
                        inputStream.close();
                    }
                    if (response != null) {
                        response.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }
    }

}

package com.seveniu.spider.imgParse;

import com.seveniu.util.AppContext;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import us.codecraft.webmagic.Site;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by seveniu on 5/25/16.
 * ImageDownloader
 */
public class ImageDownloader {
    private Logger logger = LoggerFactory.getLogger(ImageDownloader.class);
    private ImageDownloadProcess imageDownloadProcess;

    private static final int THREAD_NUM = 20;

    private CloseableHttpClient httpClient;

    public ImageDownloader(ImageDownloadProcess imageDownloadProcess) {
        this.imageDownloadProcess = imageDownloadProcess;
        this.httpClient = getHttpClient();
//        this.httpClient = HttpClientGenerator.
    }

    private ExecutorService fix = Executors.newFixedThreadPool(THREAD_NUM, new ThreadFactory() {
        AtomicInteger count = new AtomicInteger(0);

        @Override
        public Thread newThread(Runnable r) {
            return new Thread(r, "image-download-thread-" + count.getAndIncrement());
        }
    });

    HashMap<String, String> downloadAll(List<ImageProcess.Img> imgs, Site site) {
        CountDownLatch countDownLatch = new CountDownLatch(imgs.size());
        HashMap<String, String> map = new HashMap<>(imgs.size());
        for (ImageProcess.Img img : imgs) {

            fix.execute(new Runnable() {
                @Override
                public void run() {
                    String name = download(httpClient, img, site);
                    map.put(img.url, name);
                    countDownLatch.countDown();
                }
            });
        }
        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return map;
    }

    private String download(CloseableHttpClient httpClient, ImageProcess.Img img, Site site) {
        CloseableHttpResponse responseGet;
        HttpGet httpGet = null;

        InputStream inputStream = null;
        try {
            // 以get方法执行请求
            httpGet = new HttpGet(img.url);
            httpGet.addHeader("User-Agent", site.getUserAgent());


            // 获得服务器响应的所有信息
            responseGet = httpClient.execute(httpGet);

            if (responseGet.getStatusLine().getStatusCode() != 200) {
                return null;
            }
            HttpEntity entity = responseGet.getEntity();
            inputStream = entity.getContent();
            byte[] bytes = IOUtils.toByteArray(inputStream);
            return imageDownloadProcess.process(img, entity.getContentType().getValue(), bytes);
        } catch (Exception e) {
            logger.debug("file download error url: {} , error : {}", img.url, e.getMessage());
            return null;
        } finally {
            try {
                if (inputStream != null) {
                    inputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (httpGet != null) {
                httpGet.releaseConnection();
            }
        }
    }


    private CloseableHttpClient getHttpClient() {

        PoolingHttpClientConnectionManager cm = new PoolingHttpClientConnectionManager(5, TimeUnit.SECONDS);
        cm.setMaxTotal(THREAD_NUM);
        return HttpClients.custom().setConnectionManager(cm).build();
    }

    interface ImageDownloadProcess {
        String process(ImageProcess.Img img, String contentType, byte[] bytes);
    }

    public static ImageDownloader get() {
        return AppContext.getBean(ImageDownloader.class);
    }
}

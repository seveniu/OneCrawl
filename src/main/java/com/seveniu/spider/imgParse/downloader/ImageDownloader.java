package com.seveniu.spider.imgParse.downloader;

import com.seveniu.spider.imgParse.HtmlImageProcess;
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
import java.util.concurrent.TimeUnit;

/**
 * Created by seveniu on 5/25/16.
 * ImageDownloader
 */
public class ImageDownloader {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    private int threadNum;

    public ImageDownloader(int threadNum) {
        this.threadNum = threadNum;
    }

    public String download(HtmlImageProcess.Img img, Site site, ImageDownloadProcess imageDownloadProcess) {
        CloseableHttpResponse responseGet;
        HttpGet httpGet = null;

        InputStream inputStream = null;
        try {
            // 以get方法执行请求
            httpGet = new HttpGet(img.getUrl());
            httpGet.addHeader("User-Agent", site.getUserAgent());


            // 获得服务器响应的所有信息
            responseGet = getHttpClient().execute(httpGet);

            if (responseGet.getStatusLine().getStatusCode() != 200) {
                return null;
            }
            HttpEntity entity = responseGet.getEntity();
            inputStream = entity.getContent();
            byte[] bytes = IOUtils.toByteArray(inputStream);
            logger.debug("download img url bytes : " + bytes.length);
            return imageDownloadProcess.process(img, entity.getContentType().getValue(), bytes);
        } catch (Exception e) {
            logger.debug("file download error url: {} , error : {}", img.getUrl(), e.getMessage());
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
        cm.setMaxTotal(threadNum);
        return HttpClients.custom().setConnectionManager(cm).build();
    }

    public interface ImageDownloadProcess {
        String process(HtmlImageProcess.Img img, String contentType, byte[] bytes);
    }

    public static ImageDownloader get() {
        return AppContext.getBean(ImageDownloader.class);
    }
}

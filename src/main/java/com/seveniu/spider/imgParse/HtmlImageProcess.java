package com.seveniu.spider.imgParse;

import com.seveniu.common.thread.CountDownExecutor;
import com.seveniu.spider.imgParse.downloader.ImageDownloader;
import com.seveniu.spider.imgParse.filter.ImageRepeatCheck;
import com.seveniu.spider.imgParse.recorder.ImageRecorder;
import com.seveniu.spider.imgParse.storage.ImageStorage;
import com.seveniu.util.AppContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import us.codecraft.webmagic.Site;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 处理 html 文档中 的 图片资源
 * 下载所有的图片资源, 计算 md5 , 然后用 md5 作为 图片存储的 key值
 * md5 用于排重, 和查找图片
 * 最后将 html 文档中的 图片 标签 替换为 [[md5.extension], eg: [[8e1db979fd62daa448b2a2901c2bd66d.jpg]]
 * <p>
 * <p>
 * Created by seveniu on 5/24/16.
 * ImageProcess
 */
@Component
public class HtmlImageProcess {
    private Logger logger = LoggerFactory.getLogger(this.getClass());
    private static final int THREAD_NUM = 20;
    private ImageDownloader imageDownloader;
    @Autowired
    private ImageRepeatCheck imageRepeatCheck;
    @Autowired
    private ImageStorage imageStorage;
    @Autowired
    private ImageRecorder imageRecorder;

    private ThreadPoolExecutor fix = (ThreadPoolExecutor) Executors.newFixedThreadPool(THREAD_NUM, new ThreadFactory() {
        AtomicInteger count = new AtomicInteger(0);

        @Override
        public Thread newThread(Runnable r) {
            return new Thread(r, "image-download-thread-" + count.getAndIncrement());
        }
    });

    public HtmlImageProcess() {
        this.imageDownloader = new ImageDownloader(THREAD_NUM);
    }

//    public HtmlImageProcess(ImageRepeatCheck imageRepeatCheck, ImageStorage imageStorage) {
//        this(imageRepeatCheck, imageStorage, null);
//    }


    private static final Pattern IMG_TAG = Pattern.compile("(<img[\\s\\S]*?>)");

    public String process(String url, String html, Site site) {
        List<Img> imgs = new ArrayList<>();
        // 1. 解析出所有的图片 地址 ,
        html = parseAllImageUrls(imgs, html);
        if (imgs.size() == 0) {
            return html;
        }
        ImgDownloadResult result = new ImgDownloadResult(imgs.size());
        ImageProcess[] imageProcesses = new ImageProcess[imgs.size()];
        for (int i = 0; i < imageProcesses.length; i++) {
            imageProcesses[i] = new ImageProcess(imgs.get(i), site, imageDownloader, imageRepeatCheck,
                    imageStorage, imageRecorder, result);

        }
        try {
            CountDownExecutor countDownExecutor = new CountDownExecutor(fix, imageProcesses);
            countDownExecutor.await();
            // 3. 替换 img tag 为 [[md5.extension]]
            if (result.size() == 0) {
                return html;
            }
            html = parseAllIndex(result, html);
        } catch (InterruptedException e) {
            e.printStackTrace();
            return html;
        }
        return html;
    }

    private String parseAllImageUrls(List<Img> imageUrls, String html) {

        Matcher m = IMG_TAG.matcher(html);
        StringBuffer sb = new StringBuffer();
        while (m.find()) {
            String imageTag = m.group(1);
            String imageUrl = getUrl(imageTag);
            if (imageUrl == null) {
                continue;
            }
            if (imageRepeatCheck.contain(imageUrl)) {
                logger.debug("img : {} repeat", imageUrl);
                continue;
            }
            String imageAlt = getAlt(imageTag);
            if (imageAlt == null) {
                imageAlt = "";
            }

            Img img = new Img(imageUrl, imageAlt);
            String placeHolder = "[[" + imageUrls.size() + "]]";
            imageUrls.add(img);
            m.appendReplacement(sb, placeHolder);
        }
        m.appendTail(sb);
        return sb.toString();

    }

    private String parseAllIndex(ImgDownloadResult result, String html) {
        Matcher m = IMG_TAG.matcher(html);
        StringBuffer sb = new StringBuffer();
        while (m.find()) {
            String imageTag = m.group(1);
            String imageUrl = getUrl(imageTag);
            if (imageUrl == null) {
                continue;
            }
            String placeholder = result.getName(imageUrl);
            if (placeholder == null || placeholder.isEmpty()) {
                continue;
            }
            try {
                m.appendReplacement(sb, placeholder);
            } catch (Exception e) {
                logger.debug("image url : " + imageUrl);
                e.printStackTrace();
            }
        }
        m.appendTail(sb);
        return sb.toString();
    }


    private static Pattern patternForHrefWithoutQuoteImg = Pattern.compile("(<[ ]*?img[\\s\\S]*?[ ]src=)([^\"'<>\\s]+)", Pattern.CASE_INSENSITIVE);
    private static Pattern patternForHrefWithQuoteImg = Pattern.compile("(<[ ]*?img[^<>]*?[ ]src=)[\"']([^\"'<>]*)[\"']", Pattern.CASE_INSENSITIVE);

    private static Pattern patternForAltWithoutQuoteImg = Pattern.compile("(<[ ]*?img[\\s\\S]*?[ ]alt=)([^\"'<>\\s]+)", Pattern.CASE_INSENSITIVE);
    private static Pattern patternForAltWithQuoteImg = Pattern.compile("(<[ ]*?img[^<>]*?[ ]alt=)[\"']([^\"'<>]*)[\"']", Pattern.CASE_INSENSITIVE);

    private static String getAlt(String imgTag) {
        Matcher m = patternForAltWithoutQuoteImg.matcher(imgTag);
        if (m.find()) {
            return m.group(2).trim();
        } else {
            m = patternForAltWithQuoteImg.matcher(imgTag);
            if (m.find()) {
                return m.group(2).trim();
            }
        }
        return null;
    }

    private static String getUrl(String imgTag) {
        Matcher m = patternForHrefWithoutQuoteImg.matcher(imgTag);
        if (m.find()) {
            return m.group(2).trim();
        } else {
            m = patternForHrefWithQuoteImg.matcher(imgTag);
            if (m.find()) {
                return m.group(2).trim();
            }
        }
        return null;
    }

    public String getName(String url) {
        String temp = url.substring(url.lastIndexOf("/") + 1).trim();
        return temp.substring(0, temp.lastIndexOf("."));
    }

    public class Img {
        private String url;
        private String alt;

        Img(String url, String alt) {
            this.url = url;
            this.alt = alt;
        }

        public String getUrl() {
            return url;
        }

        public String getAlt() {
            return alt;
        }
    }

    public class ImgDownloadResult {
        ConcurrentHashMap<String, String> resultMap;

        public ImgDownloadResult(int size) {
            resultMap = new ConcurrentHashMap<>(size);
        }

        public void add(String imageUrl, String name) {
            resultMap.put(imageUrl, name);
        }

        public String getName(String imageUrl) {
            return resultMap.get(imageUrl);
        }

        public int size() {
            return resultMap.size();
        }
    }

    public static HtmlImageProcess get() {
        return AppContext.getBean(HtmlImageProcess.class);
    }
}

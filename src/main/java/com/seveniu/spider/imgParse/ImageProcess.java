package com.seveniu.spider.imgParse;

import com.seveniu.common.encrypt.MD5Util;
import com.seveniu.spider.imgParse.filter.BloomFilterImpl;
import com.seveniu.spider.imgParse.filter.ImageRepeatCheck;
import com.seveniu.spider.imgParse.recorder.ImageLoggerRecorder;
import com.seveniu.spider.imgParse.recorder.ImageRecorder;
import com.seveniu.spider.imgParse.storage.ImageFileStorage;
import com.seveniu.spider.imgParse.storage.ImageStorage;
import com.seveniu.util.AppContext;
import org.springframework.stereotype.Component;
import us.codecraft.webmagic.Site;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
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
public class ImageProcess {
    private ImageDownloader imageDownloader;
    private ImageRepeatCheck imageRepeatCheck;
    private ImageStorage imageStorage;
    private ImageRecorder imageRecorder;

    public ImageProcess() {
        this(null, null, null);
    }

    public ImageProcess(ImageRepeatCheck imageRepeatCheck, ImageStorage imageStorage, ImageRecorder imageRecorder) {
        if (imageRepeatCheck == null) {
            imageRepeatCheck = new BloomFilterImpl("img_bloom_file");
        }
        if (imageStorage == null) {
            imageStorage = new ImageFileStorage();
        }
        if (imageRecorder == null) {
            imageRecorder = new ImageLoggerRecorder();
        }
        this.imageRepeatCheck = imageRepeatCheck;
        this.imageStorage = imageStorage;
        this.imageRecorder = imageRecorder;
        this.imageDownloader = new ImageDownloader(downloadProcess);
    }

    public ImageProcess(ImageRepeatCheck imageRepeatCheck, ImageStorage imageStorage) {
        this(imageRepeatCheck, imageStorage, null);
    }


    private static final Pattern IMG_TAG = Pattern.compile("(<img[\\s\\S]*?>)");

    public String process(String html, Site site) {
        List<Img> imgs = new ArrayList<>();
        // 1. 解析出所有的图片 地址 ,
        html = parseAllImageUrls(imgs, html);
        if (imgs.size() == 0) {
            return html;
        }
        // 2. 下载所有的图片, 存入 map, key:url value:md5
        Map<String, String> map = this.imageDownloader.downloadAll(imgs,site);
        // 3. 替换 img tag 为 [[md5.extension]]
        html = parseAllIndex(map, html);
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

    private String parseAllIndex(Map<String, String> result, String html) {
        Matcher m = IMG_TAG.matcher(html);
        StringBuffer sb = new StringBuffer();
        while (m.find()) {
            String imageTag = m.group(1);
            String imageUrl = getUrl(imageTag);
            if (imageUrl == null) {
                continue;
            }
            String placeholder = result.get(imageUrl);
            m.appendReplacement(sb,placeholder);
        }
        m.appendTail(sb);
        return sb.toString();
    }


    private ImageDownloader.ImageDownloadProcess downloadProcess = new ImageDownloader.ImageDownloadProcess() {
        @Override
        public String process(Img img, String contentType, byte[] bytes) {
            /**
             * 返回 md5.extension
             */
            // 获取扩展名
            String extension;

            extension = getExtension(img.url).toLowerCase();
            if (extension.matches(".*(asp|jsp|htm|html|null).*")) {
                if (contentType.matches(".*(image|jepg|jpg).*")) {
                    extension = "jpg";
                }
            }
            String originName;
            if (img.alt != null && img.alt.length() > 0) {
                originName = img.alt;
            } else {
                originName = getName(img.url);
            }

            String md5 = MD5Util.md5(bytes);
            if (imageRepeatCheck.contain(md5)) {
                imageRecorder.record(img.url, originName, extension, md5);
            } else {
                try {
                    imageStorage.save(bytes, originName, extension, md5);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                imageRepeatCheck.put(md5);
                imageRepeatCheck.put(img.url);
                imageRecorder.record(img.url, originName, extension, md5);
            }
            return md5 + "." + extension;
        }
    };

    private String getExtension(String url) {
        String temp = url.substring(url.lastIndexOf(".") + 1).trim();
        Pattern pattern = Pattern.compile("(\\w+)");
        Matcher m = pattern.matcher(temp);
        if (m.find()) {
            return m.group(1);
        }
        return "";
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

    protected class Img {
        String url;
        private String alt;

        Img(String url, String alt) {
            this.url = url;
            this.alt = alt;
        }
    }

    public static ImageProcess get() {
        return AppContext.getBean(ImageProcess.class);
    }
}

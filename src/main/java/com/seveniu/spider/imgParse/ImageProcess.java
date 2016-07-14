package com.seveniu.spider.imgParse;

import com.seveniu.spider.imgParse.downloader.ImageDownloader;
import com.seveniu.spider.imgParse.filter.ImageRepeatCheck;
import com.seveniu.spider.imgParse.recorder.ImageRecorder;
import com.seveniu.spider.imgParse.storage.ImageStorage;
import org.apache.commons.codec.binary.Hex;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import us.codecraft.webmagic.Site;

import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by seveniu on 7/13/16.
 * ImageProcess
 */
public class ImageProcess implements Runnable {
    private Logger logger = LoggerFactory.getLogger(this.getClass());
    private HtmlImageProcess.Img img;
    private Site site;
    private ImageDownloader imageDownloader;
    private ImageRepeatCheck imageRepeatCheck;
    private ImageStorage imageStorage;
    private ImageRecorder imageRecorder;
    private HtmlImageProcess.ImgDownloadResult result;
    private static ThreadLocal<MessageDigest> threadLocal = new ThreadLocal<MessageDigest>() {
        @Override
        protected MessageDigest initialValue() {
            try {
                return MessageDigest.getInstance("MD5");
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
                return null;
            }
        }
    };

    ImageProcess(HtmlImageProcess.Img img, Site site, ImageDownloader imageDownloader, ImageRepeatCheck imageRepeatCheck, ImageStorage imageStorage, ImageRecorder imageRecorder, HtmlImageProcess.ImgDownloadResult result) {
        this.img = img;
        this.site = site;
        this.imageDownloader = imageDownloader;
        this.imageRepeatCheck = imageRepeatCheck;
        this.imageStorage = imageStorage;
        this.imageRecorder = imageRecorder;
        this.result = result;
    }

    @Override
    public void run() {
        String name = imageDownloader.download(img, site, downloadProcess);
        logger.debug("cur ----  img : " + img.getUrl() + "  name :" + name);
        result.add(img.getUrl(), name);
    }


    private ImageDownloader.ImageDownloadProcess downloadProcess = new ImageDownloader.ImageDownloadProcess() {
        @Override
        public String process(HtmlImageProcess.Img img, String contentType, byte[] bytes) {
            /**
             * 返回 md5.extension
             */
            // 获取扩展名
            String extension;

            extension = getExtension(img.getUrl()).toLowerCase();
            if (extension.matches(".*(asp|jsp|htm|html|null).*")) {
                if (contentType.matches(".*(image|jepg|jpg).*")) {
                    extension = "jpg";
                }
            }
            String originName;
            if (img.getAlt() != null && img.getAlt().length() > 0) {
                originName = img.getAlt();
            } else {
                originName = getName(img.getUrl());
            }

            String md5 = md5(bytes);
            if (imageRepeatCheck.contain(md5)) {
                imageRecorder.record(img.getUrl(), originName, extension, md5);
            } else {
                try {
                    imageStorage.save(bytes, originName, extension, md5);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                imageRepeatCheck.put(md5);
                imageRepeatCheck.put(img.getUrl());
                imageRecorder.record(img.getUrl(), originName, extension, md5);
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

    private String md5(byte[] bytes) {
        MessageDigest messagedigest = threadLocal.get();
        if (messagedigest == null) {
            return null;
        }
        return new String(Hex.encodeHex(messagedigest.digest(bytes)));
    }

    public String getName(String url) {
        String temp = url.substring(url.lastIndexOf("/") + 1).trim();
        return temp.substring(0, temp.lastIndexOf("."));
    }
}

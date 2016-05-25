package com.seveniu.spider.imgParse.recorder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by seveniu on 5/24/16.
 * ImageMysqlRecorder
 */
public class ImageLoggerRecorder implements ImageRecorder {
    private Logger logger = LoggerFactory.getLogger("IMAGE_RECORD");
    @Override
    public void record(String url, String imageOriginName, String extension, String md5) {
        logger.info("url:{},originName:{},extension:{},md5:{}");
    }
}

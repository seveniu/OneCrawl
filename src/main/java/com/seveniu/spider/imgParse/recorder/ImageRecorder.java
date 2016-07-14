package com.seveniu.spider.imgParse.recorder;

import org.springframework.stereotype.Component;

/**
 * Created by seveniu on 5/24/16.
 * ImageRecode
 */
@Component
public interface ImageRecorder {

    void record(String url, String imageOriginName, String extension, String md5);
}

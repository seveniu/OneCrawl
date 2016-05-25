package com.seveniu.spider.imgParse.recorder;

/**
 * Created by seveniu on 5/24/16.
 * ImageRecode
 */
public interface ImageRecorder {

    void record(String url, String imageOriginName, String extension, String md5);
}

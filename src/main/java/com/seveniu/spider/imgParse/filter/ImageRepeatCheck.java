package com.seveniu.spider.imgParse.filter;

/**
 * Created by seveniu on 5/24/16.
 * ImageRepeatCheck
 */
public interface ImageRepeatCheck {

    void put(String md5);

    boolean contain(String md5);
}

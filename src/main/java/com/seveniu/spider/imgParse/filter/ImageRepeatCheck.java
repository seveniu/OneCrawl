package com.seveniu.spider.imgParse.filter;

import org.springframework.stereotype.Component;

/**
 * Created by seveniu on 5/24/16.
 * ImageRepeatCheck
 */
@Component
public interface ImageRepeatCheck {

    void put(String md5);

    boolean contain(String md5);
}

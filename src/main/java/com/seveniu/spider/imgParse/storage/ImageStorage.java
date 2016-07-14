package com.seveniu.spider.imgParse.storage;

import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * Created by seveniu on 5/24/16.
 * ImageSave
 */
@Component
public interface ImageStorage {

    void save(byte[] bytes, String originName, String extension, String md5) throws IOException;


}

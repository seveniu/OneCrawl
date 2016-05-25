package com.seveniu.spider.imgParse.storage;

import java.io.IOException;

/**
 * Created by seveniu on 5/24/16.
 * ImageSave
 */
public interface ImageStorage {

    void save(byte[] bytes, String originName, String extension, String md5) throws IOException;


}

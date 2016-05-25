package com.seveniu.spider.imgParse.storage;

import com.seveniu.common.file.FileUtil;

import java.io.IOException;

/**
 * Created by seveniu on 5/24/16.
 * ImageFileSave
 */
public class ImageFileStorage implements ImageStorage {
    public static final String PATH = "image/";

    @Override
    public void save(byte[] bytes, String originName, String extension, String md5) throws IOException {
        String path = getPath(PATH, md5, extension);
        FileUtil.saveFile(bytes, path);
    }

    private String getPath(String path, String name, String extension) {
//        name = name.trim();
//        extension = extension.trim();
//        return path + extension + "/" + name.substring(0, 2) + "/" + name.substring(2, 4) + "/" + name + "." + extension;
        return path + name + "." + extension;

    }
}

package com.seveniu.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;

public class Json {
    public static String toJson(Object object) {
        return JSON.toJSONString(object);
    }

    public static <T> T toObject(String json, Class<T> clazz) {
        return JSON.parseObject(json, clazz);
    }


    public static <T> T toObject(String json, TypeReference<T> type) {
        return JSON.parseObject(json, type);
    }
}

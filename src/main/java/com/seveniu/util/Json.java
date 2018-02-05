package com.seveniu.util;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

/**
 * Created by dhlz on 1/3/17.
 * *
 */
public class Json {
    private Json() {
    }

    public static ObjectMapper getMapper() {
        return new ObjectMapper();
    }

    public static String toJson(Object object) {
        return toJson(object, false);
    }

    public static String toJson(Object object, boolean nonNull) {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(MapperFeature.PROPAGATE_TRANSIENT_MARKER, true);
        if (nonNull) {
            mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        }
        try {
            return mapper.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public static <T> T toObject(String json, Class<T> tClass) {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        try {
            return mapper.readValue(json, tClass);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static <T> T toObject(String json, TypeReference<T> typeReference) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.readValue(json, typeReference);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}

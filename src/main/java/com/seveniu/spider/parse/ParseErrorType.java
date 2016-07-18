package com.seveniu.spider.parse;

/**
 * Created by seveniu on 5/12/16.
 * ParseError
 */
public enum ParseErrorType {

    NOT_FOUND_XPATH("can't find xpath"),
    NO_FIELD_MATCH("no field match");

    private String describe;

    ParseErrorType(String describe) {
        this.describe = describe;
    }

    public String getDescribe() {
        return describe;
    }
}

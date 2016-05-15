package com.seveniu.parse;

/**
 * Created by seveniu on 5/12/16.
 * ParseError
 */
public enum ParseErrorType {

    NOT_FOUND_XPATH("can't find xpath");

    private String describe;

    ParseErrorType(String describe) {
        this.describe = describe;
    }

    public String getDescribe() {
        return describe;
    }
}

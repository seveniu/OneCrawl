package com.seveniu.spider.parse;

import com.seveniu.template.def.Field;

/**
 * Created by seveniu on 5/12/16.
 * ParseError
 */
public class ParseError {

    private Field field;

    private ParseErrorType errorType;

    public ParseError(Field field, ParseErrorType errorType) {
        this.field = field;
        this.errorType = errorType;
    }

    public Field getField() {
        return field;
    }

    public ParseErrorType getErrorType() {
        return errorType;
    }

    @Override
    public String toString() {
        return "ParseError{" +
                "field=" + field +
                ", errorType=" + errorType +
                '}';
    }
}

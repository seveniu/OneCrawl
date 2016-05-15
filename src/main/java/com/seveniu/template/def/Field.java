package com.seveniu.template.def;

import java.util.List;

/**
 * Created by seveniu on 5/12/16.
 * Field
 */
public class Field {

    private int htmlType;
    private int contentType;
    private String name;

    private String defaultValue = "";
    private String xpath;
    private List<String> regex;
    private boolean trim;
    private boolean must;

    public int getHtmlType() {
        return htmlType;
    }

    public void setHtmlType(int htmlType) {
        this.htmlType = htmlType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getContentType() {
        return contentType;
    }

    public void setContentType(int contentType) {
        this.contentType = contentType;
    }

    public String getXpath() {
        return xpath;
    }

    public void setXpath(String xpath) {
        this.xpath = xpath;
    }

    public List<String> getRegex() {
        return regex;
    }

    public void setRegex(List<String> regex) {
        this.regex = regex;
    }

    public boolean isTrim() {
        return trim;
    }

    public void setTrim(boolean trim) {
        this.trim = trim;
    }

    public String getDefaultValue() {
        return defaultValue;
    }

    public void setDefaultValue(String defaultValue) {
        this.defaultValue = defaultValue;
    }

    public boolean isMust() {
        return must;
    }

    public void setMust(boolean must) {
        this.must = must;
    }

    @Override
    public String toString() {
        return "Field{" +
                "htmlType=" + htmlType +
                ", contentType=" + contentType +
                ", defaultValue='" + defaultValue + '\'' +
                ", xpath='" + xpath + '\'' +
                ", regex=" + regex +
                ", trim=" + trim +
                ", must=" + must +
                '}';
    }
}

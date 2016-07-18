package com.seveniu.spider.parse;

/**
 * Created by seveniu on 5/12/16.
 * Link
 */
public class Link {
    private String text;
    private String url;

    public Link() {
    }

    public Link(String text, String url) {
        this.text = text;
        this.url = url;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public String toString() {
        return "Link{" +
                "text='" + text + '\'' +
                ", url='" + url + '\'' +
                '}';
    }
}

package com.seveniu.task;

/**
 * Created by seveniu on 5/12/16.
 * Task
 */
public class SpiderConfig {
    private int threadNum;
    private boolean isJS;
    private boolean processImg;
    private String[] urls;


    public int getThreadNum() {
        return threadNum;
    }

    public SpiderConfig setThreadNum(int threadNum) {
        this.threadNum = threadNum;
        return this;
    }

    public boolean isJS() {
        return isJS;
    }

    public SpiderConfig setJS(boolean JS) {
        isJS = JS;
        return this;
    }

    public boolean isProcessImg() {
        return processImg;
    }

    public SpiderConfig setProcessImg(boolean processImg) {
        this.processImg = processImg;
        return this;
    }

    public String[] getUrls() {
        return urls;
    }

    public SpiderConfig setUrls(String[] urls) {
        this.urls = urls;
        return this;
    }
}

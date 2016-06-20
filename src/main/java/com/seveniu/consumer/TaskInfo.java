package com.seveniu.consumer;

import java.util.List;

/**
 * Created by seveniu on 6/3/16.
 * Task
 */
public class TaskInfo {

    private String id;
    private String name;
    private List<String> urls;
    private String templateId;
    private int templateType;
    private String template;
    private int threadNum;
    private int proxy;
    private int javascript;


    public TaskInfo(String id, String name, List<String> urls, String templateId, int templateType, String template, int threadNum, int proxy, int javascript) {
        this.id = id;
        this.name = name;
        this.urls = urls;
        this.templateId = templateId;
        this.templateType = templateType;
        this.template = template;
        this.threadNum = threadNum;
        this.proxy = proxy;
        this.javascript = javascript;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTemplateId() {
        return templateId;
    }

    public void setTemplateId(String templateId) {
        this.templateId = templateId;
    }

    public int getTemplateType() {
        return templateType;
    }

    public void setTemplateType(int templateType) {
        this.templateType = templateType;
    }

    public String getTemplate() {
        return template;
    }

    public void setTemplate(String template) {
        this.template = template;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getUrls() {
        return urls;
    }

    public void setUrls(List<String> urls) {
        this.urls = urls;
    }

    public int getThreadNum() {
        return threadNum;
    }

    public void setThreadNum(int threadNum) {
        this.threadNum = threadNum;
    }

    public int getProxy() {
        return proxy;
    }

    public void setProxy(int proxy) {
        this.proxy = proxy;
    }

    public int getJavascript() {
        return javascript;
    }

    public void setJavascript(int javascript) {
        this.javascript = javascript;
    }
}

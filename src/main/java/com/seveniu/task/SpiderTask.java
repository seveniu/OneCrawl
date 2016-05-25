package com.seveniu.task;

import us.codecraft.webmagic.Spider;

/**
 * Created by seveniu on 5/17/16.
 * SpiderTask
 */
public interface SpiderTask {
    void start();

    Spider.Status getStatus();

    void close();

    SpiderConfig spiderConfig();

    String getTemplateId();
}

package com.seveniu.spider;

import org.junit.Test;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.processor.PageProcessor;

/**
 * Created by seveniu on 3/13/17.
 * *
 */
public class MySpiderTest {
    @Test
    public void testSpider() {
        PageProcessor pageProcessor = new PageProcessor() {
            @Override
            public void process(Page page) {
                System.out.println(page);
            }

            @Override
            public Site getSite() {
                return Site.me()
                        .setUserAgent("Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/28.0.1500.71 Safari/537.36")
                        .setRetryTimes(3)
                        .setTimeOut(40 * 1000)
                        .setCharset("UTF-8")
                        ;
            }
        };
        Spider.create(pageProcessor)
                .setDownloader(new MyHttpDownload(null))

                .addUrl("http://www.syxc.gov.cn/syxc/xcdt/20170304/003_360f32c4-5eac-45fa-b017-ecf432c0bdbe.htm").run();

    }

}
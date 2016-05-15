package com.seveniu.util;

/**
 * Created with IntelliJ IDEA.
 * User: niu
 * Date: 2014/6/13
 * Time: 20:36
 * Project: dhlz-spider
 */
public class UserAgent {
    private static final String[] agents = new String[]{
            "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 6.1; SLCC2; .NET CLR 2.0.50727; .NET CLR 3.5.30729; .NET CLR 3.0.30729; Media Center PC 6.0; Tablet PC 2.0)",
            "Mozilla/4.0 (compatible; MSIE 7.0; Windows NT 6.1; SLCC2; .NET CLR 2.0.50727; .NET CLR 3.5.30729; .NET CLR 3.0.30729; Media Center PC 6.0; Tablet PC 2.0)",
            "Mozilla/5.0 (Windows; U; Windows NT 6.1; en-US) AppleWebKit/531.9 (KHTML, like Gecko) Version/4.0.3 Safari/531.9.1",
            "Mozilla/5.0 (Windows NT 6.3; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/35.0.1916.153 Safari/537.36",
            "Mozilla/5.0 (Windows NT 6.3; WOW64; rv:29.0) Gecko/20100101 Firefox/29.0",
            "Mozilla/5.0 (Windows NT 6.3; WOW64; Trident/7.0; rv:11.0) like Gecko",
            "Mozilla/4.0 (compatible; MSIE 8.0; Windows NT 6.1; Trident/4.0)",
            "Mozilla/4.0 (compatible; MSIE 8.0; Windows NT 6.1; Win64; x64; Trident/4.0)",
            "Mozilla/4.0 (compatible; MSIE 8.0; Windows NT 6.1; WOW64; Trident/4.0)",
            "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_10_0) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/38.0.2125.122 Safari/537.36"
    };

    public static String getUserAgent() {
        int length = agents.length;
        int key = (int) (Math.random() * length);
        return agents[key];
    }

    public static void main(String[] args) {
        for (int i = 0; i < 1000; i++) {
            getUserAgent();
        }
    }


}

package com.seveniu.spider;

import us.codecraft.webmagic.Request;

/**
 * Created by seveniu on 5/17/16.
 * DownloaderErrorListener
 */
public interface DownloaderErrorListener {
    //连接超时错误
    void onTimeOutError(Request request);

    // 连接状态码错误
    void onStatusCodeError(Request request, int statusCode);

    // 连接状态码错误
    void onOtherConnectError(Request request);
}

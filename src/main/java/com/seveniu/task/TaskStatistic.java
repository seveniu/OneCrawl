package com.seveniu.task;

import com.seveniu.node.Node;
import com.seveniu.spider.DownloaderErrorListener;
import us.codecraft.webmagic.Request;

import java.util.concurrent.atomic.AtomicInteger;

import static com.seveniu.spider.pageProcessor.OneLayerProcessor.CONTEXT_NODE;

/**
 * Created by seveniu on 5/15/16.
 * TaskStatistic
 */
public class TaskStatistic implements DownloaderErrorListener {
    private int startUrlCount = 0;
    private AtomicInteger createUrlCount = new AtomicInteger(0);
    private AtomicInteger createTargetUrlCount = new AtomicInteger(0);
    private AtomicInteger createNextUrlCount = new AtomicInteger(0);
    private AtomicInteger successUrlCount = new AtomicInteger(0);
    private AtomicInteger repeatUrlCount = new AtomicInteger(0);
    private AtomicInteger netErrorUrlCount = new AtomicInteger(0);
    private AtomicInteger doneUrlCount = new AtomicInteger(0);
    private AtomicInteger parseErrorCount = new AtomicInteger(0);
    private AtomicInteger createNodeCount = new AtomicInteger(0);
    private AtomicInteger doneNodeCount = new AtomicInteger(0);
    private AtomicInteger errorNodeCount = new AtomicInteger(0);

    // startUrlCount + createUrlCount = repeatUrlCount + successUrlCount + netErrorUrlCount
    // startUrlCount + createUrlCount = repeatUrlCount + createTargetUrlCount + createNextUrlCount
    // successUrlCount = doneUrlCount + parseErrorCount
    // createNodeCount = doneNodeCount + errorNodeCount

    public void setStartUrlCount(int count) {
        startUrlCount = count;
    }

    public int addTargetUrlCount(int num) {
        return createTargetUrlCount.addAndGet(num);
    }

    public int addCreateNodeCount(int num) {
        return createNodeCount.addAndGet(num);
    }

    public int addDoneNodeCount(int num) {
        return doneNodeCount.addAndGet(num);
    }

    public int addErrorNodeCount(int num) {
        return errorNodeCount.addAndGet(num);
    }

    public int addCreateUrlCount(int num) {
        return createUrlCount.addAndGet(num);
    }

    public int addSuccessUrlCount(int num) {
        return successUrlCount.addAndGet(num);
    }

    public int addDoneUrlCount(int num) {
        return doneUrlCount.addAndGet(num);
    }

    public int addRepeatUrlCount(int num) {
        return repeatUrlCount.addAndGet(num);
    }

    public int addNetErrorUrlCount(int num) {
        return netErrorUrlCount.addAndGet(num);
    }

    public int addParseErrorCount(int num) {
        return parseErrorCount.addAndGet(num);
    }

    public AtomicInteger getCreateUrlCount() {
        return createUrlCount;
    }

    public AtomicInteger getDoneUrlCount() {
        return doneUrlCount;
    }

    public AtomicInteger getRepeatUrlCount() {
        return repeatUrlCount;
    }

    public AtomicInteger getNetErrorUrlCount() {
        return netErrorUrlCount;
    }

    public AtomicInteger getParseErrorCount() {
        return parseErrorCount;
    }


    @Override
    public void onTimeOutError(Request request) {
        addNetErrorUrlCount(1);
        Node contextNode = (Node) request.getExtra(CONTEXT_NODE);
        if (contextNode != null) {
            addDoneNodeCount(1);
        }
    }

    @Override
    public void onStatusCodeError(Request request, int statusCode) {
        addNetErrorUrlCount(1);
        Node contextNode = (Node) request.getExtra(CONTEXT_NODE);
        if (contextNode != null) {
            addDoneNodeCount(1);
        }
    }

    @Override
    public String toString() {
        return "TaskStatistic{" +
                "startUrlCount=" + startUrlCount +
                ", createUrlCount=" + createUrlCount +
                ", createTargetUrlCount=" + createTargetUrlCount +
                ", createNextUrlCount=" + createNextUrlCount +
                ", successUrlCount=" + successUrlCount +
                ", repeatUrlCount=" + repeatUrlCount +
                ", netErrorUrlCount=" + netErrorUrlCount +
                ", doneUrlCount=" + doneUrlCount +
                ", parseErrorCount=" + parseErrorCount +
                ", createNodeCount=" + createNodeCount +
                ", doneNodeCount=" + doneNodeCount +
                ", errorNodeCount=" + errorNodeCount +
                '}';
    }
}

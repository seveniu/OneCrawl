package com.seveniu.task;

import com.seveniu.node.Node;
import com.seveniu.spider.DownloaderErrorListener;
import us.codecraft.webmagic.Request;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import static com.seveniu.spider.pageProcessor.multipleListContentProcessor.CONTEXT_NODE;

/**
 * Created by seveniu on 5/15/16.
 * TaskStatistic
 */
public class TaskStatistic implements DownloaderErrorListener {
    private static final int NETWORK_ERROR_URL_LIST_SIZE_MAX = 50;
    private static final int PARSE_ERROR_URL_LIST_SIZE_MAX = 50;
    private String taskId;
    private Date startTime;
    private Date endTime;
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
    private List<String> netErrorUrlList = Collections.synchronizedList(new ArrayList<>());
    private List<String> parseErrorUrlList = Collections.synchronizedList(new ArrayList<>());

    // startUrlCount + createUrlCount = repeatUrlCount + successUrlCount + netErrorUrlCount
    // startUrlCount + createUrlCount = repeatUrlCount + createTargetUrlCount + createNextUrlCount
    // successUrlCount = doneUrlCount + parseErrorCount
    // createNodeCount = doneNodeCount + errorNodeCount


    public TaskStatistic() {
    }

    public TaskStatistic(String taskId) {
        this.taskId = taskId;
    }

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

    public int addNetErrorUrlCount(String[] urls) {
        if(netErrorUrlList.size() < NETWORK_ERROR_URL_LIST_SIZE_MAX) {
            Collections.addAll(netErrorUrlList, urls);
        }
        return netErrorUrlCount.addAndGet(urls.length);
    }

    public int addParseErrorCount(String[] urls) {
        if(netErrorUrlList.size() < PARSE_ERROR_URL_LIST_SIZE_MAX) {
            Collections.addAll(parseErrorUrlList, urls);
        }
        return parseErrorCount.addAndGet(urls.length);
    }

    public String getTaskId() {
        return taskId;
    }

    public Date getStartTime() {
        return startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public int getStartUrlCount() {
        return startUrlCount;
    }

    public AtomicInteger getCreateUrlCount() {
        return createUrlCount;
    }

    public AtomicInteger getCreateTargetUrlCount() {
        return createTargetUrlCount;
    }

    public AtomicInteger getCreateNextUrlCount() {
        return createNextUrlCount;
    }

    public AtomicInteger getSuccessUrlCount() {
        return successUrlCount;
    }

    public AtomicInteger getRepeatUrlCount() {
        return repeatUrlCount;
    }

    public AtomicInteger getNetErrorUrlCount() {
        return netErrorUrlCount;
    }

    public AtomicInteger getDoneUrlCount() {
        return doneUrlCount;
    }

    public AtomicInteger getParseErrorCount() {
        return parseErrorCount;
    }

    public AtomicInteger getCreateNodeCount() {
        return createNodeCount;
    }

    public AtomicInteger getDoneNodeCount() {
        return doneNodeCount;
    }

    public AtomicInteger getErrorNodeCount() {
        return errorNodeCount;
    }

    public List<String> getNetErrorUrlList() {
        return netErrorUrlList;
    }

    public List<String> getParseErrorUrlList() {
        return parseErrorUrlList;
    }

    @Override
    public void onTimeOutError(Request request) {
        addNetErrorUrlCount(new String[]{request.getUrl()});
        Node contextNode = (Node) request.getExtra(CONTEXT_NODE);
        if (contextNode != null) {
            addDoneNodeCount(1);
        }
    }

    @Override
    public void onStatusCodeError(Request request, int statusCode) {
        addNetErrorUrlCount(new String[]{request.getUrl()});
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

package com.seveniu.task;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by seveniu on 5/15/16.
 * TaskStatistic
 */
public class TaskStatistic {
    private AtomicInteger createUrlCount = new AtomicInteger(0);
    private AtomicInteger successUrlCount = new AtomicInteger(0);
    private AtomicInteger repeatUrlCount = new AtomicInteger(0);
    private AtomicInteger netErrorUrlCount = new AtomicInteger(0);
    private AtomicInteger doneUrlCount = new AtomicInteger(0);
    private AtomicInteger parseErrorCount = new AtomicInteger(0);
    private AtomicInteger createNodeCount = new AtomicInteger(0);
    private AtomicInteger doneNodeCount = new AtomicInteger(0);

    // createUrlCount = repeatUrlCount + successUrlCount + netErrorUrlCount
    // successUrlCount = doneUrlCount + parseErrorCount
    // createNodeCount > doneNodeCount + parseErrorCount


    public int addCreateNodeCount(int num) {
        return createNodeCount.addAndGet(num);
    }

    public int addDoneNodeCount(int num) {
        return doneNodeCount.addAndGet(num);
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
}

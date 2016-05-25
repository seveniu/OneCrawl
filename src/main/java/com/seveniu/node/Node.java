package com.seveniu.node;

import com.seveniu.parse.PageResult;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by seveniu on 5/12/16.
 * Node
 */
public class Node {
    private String url;
    private List<PageResult> pages = new ArrayList<>();

    public Node(String url) {
        this.url = url;
    }

    public String getUrl() {
        return url;
    }

    public List<PageResult> getPages() {
        return pages;
    }

    public void addPageResult(PageResult pageResult) {
        pages.add(pageResult);
    }
}

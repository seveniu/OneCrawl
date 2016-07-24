package com.seveniu.spider.parse;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by seveniu on 5/17/16.
 * PageResult
 */
public class PageResult {
    private List<FieldResult> fieldResults;
    private String url;


    public List<FieldResult> getFieldResults() {
        return fieldResults;
    }

    public PageResult setFieldResults(List<FieldResult> fieldResults) {
        this.fieldResults = fieldResults;
        return this;
    }

    public void addFieldResult(FieldResult fieldResult) {
        if (this.fieldResults == null) {
            this.fieldResults = new ArrayList<>();
        }
        this.fieldResults.add(fieldResult);
    }

    public String getUrl() {
        return url;
    }

    public PageResult setUrl(String url) {
        this.url = url;
        return this;
    }

}

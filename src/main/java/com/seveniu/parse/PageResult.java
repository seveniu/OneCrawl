package com.seveniu.parse;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by seveniu on 5/17/16.
 * PageResult
 */
public class PageResult {
    private List<FieldResult> fieldResults = new ArrayList<>();
    private String url;

    public PageResult(String url, List<FieldResult> fieldResults) {
        this.fieldResults = fieldResults;
        this.url = url;
    }

    public List<FieldResult> getFieldResults() {
        return fieldResults;
    }

    public void setFieldResults(List<FieldResult> fieldResults) {
        this.fieldResults = fieldResults;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}

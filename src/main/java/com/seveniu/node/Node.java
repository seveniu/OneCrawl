package com.seveniu.node;

import com.seveniu.parse.FieldResult;
import com.seveniu.parse.ParseResult;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by seveniu on 5/12/16.
 * Node
 */
public class Node {
    private String url;
    private List<List<FieldResult>> parseResults = new ArrayList<>();

    public Node(String url) {
        this.url = url;
    }

    public String getUrl() {
        return url;
    }

    public List<List<FieldResult>> getParseResults() {
        return parseResults;
    }

    public void addResult(List<FieldResult> fieldResults) {
        parseResults.add(fieldResults);
    }
}

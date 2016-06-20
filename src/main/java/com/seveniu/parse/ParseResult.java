package com.seveniu.parse;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by seveniu on 5/12/16.
 * ParseResult
 */
public class ParseResult {

    private String url;
    private ParseError parseError;
    private List<FieldResult> fieldResults;
    private List<String> nextPageLinks;
    private List<String> targetLinks;

    public ParseResult(String url) {
        this.url = url;
    }


    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public ParseError getParseError() {
        return parseError;
    }

    public void setParseError(ParseError parseError) {
        this.parseError = parseError;
    }

    public List<FieldResult> getFieldResults() {
        return fieldResults;
    }

    public void setFieldResults(List<FieldResult> fieldResults) {
        this.fieldResults = fieldResults;
    }

    public List<String> getTargetLinks() {
        return targetLinks;
    }

    public void setTargetLinks(List<String> targetLinks) {
        this.targetLinks = targetLinks;
    }

    public List<String> getNextPageLinks() {
        return nextPageLinks;
    }

    public void setNextPageLinks(List<String> nextPageLinks) {
        this.nextPageLinks = nextPageLinks;
    }


    public void addFieldResult(FieldResult fieldResult) {
        if (fieldResults == null) {
            fieldResults = new ArrayList<>();
        }
        fieldResults.add(fieldResult);
    }

    public void addLink(String link) {
        if (targetLinks == null) {
            targetLinks = new ArrayList<>();
        }
        targetLinks.add(link);
    }

    public void addPageLink(String link) {
        if (nextPageLinks == null) {
            nextPageLinks = new ArrayList<>();
        }
        this.nextPageLinks.add(link);
    }

    public boolean hasFieldResults() {
        return fieldResults != null && fieldResults.size() > 0;
    }

    public boolean hasLinks() {
        return targetLinks != null && targetLinks.size() > 0;
    }

    public boolean hasNextPageLinks() {
        return nextPageLinks != null && nextPageLinks.size() > 0;
    }

    public boolean hasError() {
        return parseError != null;
    }
}

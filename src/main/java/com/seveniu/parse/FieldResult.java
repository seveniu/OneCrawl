package com.seveniu.parse;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by seveniu on 5/12/16.
 * FieldResult
 */
public class FieldResult {
    private int fieldId;
    private int fieldHtmlType;
    private String name;
    private String result;
    private List<Link> linkResult;

    public FieldResult() {
    }

    public FieldResult(int fieldId, int fieldHtmlType, String name, Link result) {
        ArrayList<Link> links = new ArrayList<>(1);
        links.add(result);
        this.name = name;
        this.fieldId = fieldId;
        this.fieldHtmlType = fieldHtmlType;
        this.linkResult = links;
    }
    public FieldResult(int fieldId,int fieldHtmlType,String name, List<Link> result) {
        this.name = name;
        this.fieldId = fieldId;
        this.fieldHtmlType = fieldHtmlType;
        this.linkResult = result;
    }
    public FieldResult(int fieldId,int fieldHtmlType,String name, String result) {
        this.name = name;
        this.fieldId = fieldId;
        this.fieldHtmlType = fieldHtmlType;
        this.result = result;
    }

    public int getFieldId() {
        return fieldId;
    }

    public void setFieldId(int fieldId) {
        this.fieldId = fieldId;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public int getFieldHtmlType() {
        return fieldHtmlType;
    }

    public void setFieldHtmlType(int fieldHtmlType) {
        this.fieldHtmlType = fieldHtmlType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Link> getLinkResult() {
        return linkResult;
    }

    public void setLinkResult(List<Link> linkResult) {
        this.linkResult = linkResult;
    }

    @Override
    public String toString() {
        return "FieldResult{" +
                "fieldId=" + fieldId +
                ", result='" + result + '\'' +
                '}';
    }
}

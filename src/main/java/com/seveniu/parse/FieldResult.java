package com.seveniu.parse;

/**
 * Created by seveniu on 5/12/16.
 * FieldResult
 */
public class FieldResult {
    private int fieldId;
    private String name;
    private String result;

    public FieldResult(int fieldId,String name, String result) {
        this.name = name;
        this.fieldId = fieldId;
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

    @Override
    public String toString() {
        return "FieldResult{" +
                "fieldId=" + fieldId +
                ", result='" + result + '\'' +
                '}';
    }
}

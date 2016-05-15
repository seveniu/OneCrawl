package com.seveniu.template.def;

import java.util.List;

/**
 * Created by seveniu on 5/12/16.
 * Template
 */
public class Template {
    private List<Field> fields;

    public List<Field> getFields() {
        return fields;
    }

    public void setFields(List<Field> fields) {
        this.fields = fields;
    }

    @Override
    public String toString() {
        return "Template{" +
                "fields=" + fields +
                '}';
    }
}

package com.seveniu.template.def;

import java.util.List;

/**
 * Created by seveniu on 5/12/16.
 * Template
 */
public class Template {
    private String name;
    private String url;
    private List<Field> fields;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public List<Field> getFields() {
        return fields;
    }

    public void setFields(List<Field> fields) {
        this.fields = fields;
    }

    @Override
    public String toString() {
        return "Template{" +
                "name='" + name + '\'' +
                ", url='" + url + '\'' +
                ", fields=" + fields +
                '}';
    }
}

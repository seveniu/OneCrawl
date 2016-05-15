package com.seveniu.template;

import com.seveniu.common.json.Json;
import com.seveniu.template.def.Field;
import com.seveniu.template.def.Template;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by seveniu on 5/12/16.
 * PagesTemplate
 */
public class PagesTemplate {
    private List<Template> templates;
    private PagesTemplate() {
    }

    public List<Template> getTemplates() {
        return templates;
    }

    public void setTemplates(List<Template> templates) {
        this.templates = templates;
    }

    public Template getTemplate(int index) {
        return templates.get(index);
    }

    public static PagesTemplate fromJson(String json) {
        return Json.toObject(json,PagesTemplate.class);
    }

    @Override
    public String toString() {
        return "PagesTemplate{" +
                "templates=" + templates +
                '}';
    }


}

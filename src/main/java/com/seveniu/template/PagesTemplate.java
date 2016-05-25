package com.seveniu.template;

import com.fasterxml.jackson.core.type.TypeReference;
import com.seveniu.common.json.Json;
import com.seveniu.template.def.Field;
import com.seveniu.template.def.Template;
import com.sun.tools.doclets.formats.html.SourceToHTMLConverter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
        return Json.toObject(json, PagesTemplate.class);
    }

    public static PagesTemplate fromOldJson(String json) {
        List<Map<String, Object>> list = Json.toObject(json, new TypeReference<List<Map<String, Object>>>() {
        });
        List<Template> templates = new ArrayList<>(list.size());
        for (Map<String, Object> map : list) {
            Template template = new Template();
            template.setName((String) map.get("name"));
            template.setUrl((String) map.get("url"));
            List<Map<String, Object>> labels = (List<Map<String, Object>>) map.get("labels");
            List<Field> fields = new ArrayList<>(labels.size());
            for (Map<String, Object> label : labels) {
                Field field = new Field();
                field.setName((String) label.get("text"));
                field.setDefaultValue((String) label.get("value"));
                field.setMust((Boolean) label.get("must"));
                field.setXpath((String) label.get("xpath"));
                field.setHtmlType((Integer) label.get("type"));
                field.setContentType((Integer) label.get("id"));
                field.setRegex((List<String>) label.get("regExp"));
                fields.add(field);
            }
            template.setFields(fields);
            templates.add(template);
        }
        PagesTemplate pagesTemplate = new PagesTemplate();

        pagesTemplate.setTemplates(templates);
        return pagesTemplate;
    }

    public static void main(String[] args) {
        PagesTemplate pagesTemplate = fromOldJson("[{\"name\":\"lb\",\"type\":1,\"url\":\"http://www.xyxdq.cn/l1/\",\"labels\":[{\"id\":5,\"text\":\"链接\",\"type\":2,\"must\":false,\"withFormat\":false,\"xpath\":\"//html/body/table/tbody/tr/td/table/tbody/tr/td/table/tbody/tr/td[@class='f2']/span[@class='f2']/a[@class='t13']\",\"regExp\":null,\"value\":null,\"indexKeyWordId\":-1}]},{\"name\":\"nr\",\"type\":2,\"url\":\"http://www.xyxdq.cn/l1/201211/t20121106_394565.htm\",\"labels\":[{\"id\":2,\"text\":\"标题\",\"type\":1,\"must\":false,\"withFormat\":false,\"xpath\":\"//html/body/table/tbody/tr/td/table/tbody/tr/td[@class='f1']/h3\",\"regExp\":null,\"value\":null,\"indexKeyWordId\":2},{\"id\":7,\"text\":\"内容\",\"type\":1,\"must\":false,\"withFormat\":true,\"xpath\":\"//html/body/table[3]/tbody/tr/td/table[5]/tbody\",\"regExp\":null,\"value\":null,\"indexKeyWordId\":1}]}]");
        System.out.println(Json.toJson(pagesTemplate));
    }

    @Override
    public String toString() {
        return "PagesTemplate{" +
                "templates=" + templates +
                '}';
    }


}

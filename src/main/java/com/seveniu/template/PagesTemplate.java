package com.seveniu.template;

import com.fasterxml.jackson.core.type.TypeReference;
import com.seveniu.common.json.Json;
import com.seveniu.template.def.Field;
import com.seveniu.template.def.Template;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by seveniu on 5/12/16.
 * PagesTemplate
 */
public class PagesTemplate {
    private List<Template> templates;
    private String id;

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

    public String getId() {
        return id;
    }

    public int pagesNum() {
        return templates.size();
    }

    public static PagesTemplate fromJson(String id, String json) {
        PagesTemplate pagesTemplate = new PagesTemplate();
        List<Template> templates = Json.toObject(json, new TypeReference<List<Template>>() { });
        pagesTemplate.id = id;
        pagesTemplate.templates = templates;
        return pagesTemplate;
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
                field.setType((Integer) label.get("type"));
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
        pagesTemplate = fromJson("1","[{\"name\":\"123\",\"fields\":[{\"xpath\":\"//html/body/table[@class='ju']/tbody/tr/td/table/tbody/tr/td/table/tbody/tr/td/table/tbody/tr/td/table/tbody/tr/td/a[@class='f5']\",\"regex\":null,\"defaultValue\":null,\"name\":\"链接\",\"must\":false,\"type\":2,\"contentType\":5}],\"url\":\"http://sz.dinghai.gov.cn/alljlist.asp?ClassID=44\"},{\"name\":\"321\",\"fields\":[{\"xpath\":\"//html/body/table/tbody/tr/td[@class='f10']\",\"regex\":null,\"defaultValue\":null,\"name\":\"标题\",\"must\":false,\"type\":1,\"contentType\":2},{\"xpath\":\"//html/body/table/tbody/tr/td[@class='vwgao']\",\"regex\":null,\"defaultValue\":null,\"name\":\"内容\",\"must\":false,\"type\":1,\"contentType\":7},{\"xpath\":\"//html/body/table/tbody/tr/td[@class='vwgao']/div\",\"regex\":null,\"defaultValue\":null,\"name\":\"时间\",\"must\":false,\"type\":1,\"contentType\":8}],\"url\":\"http://sz.dinghai.gov.cn/view.asp?NewsID=291\"}]");
        System.out.println(Json.toJson(pagesTemplate));
    }

    @Override
    public String toString() {
        return "PagesTemplate{" +
                "templates=" + templates +
                '}';
    }


}

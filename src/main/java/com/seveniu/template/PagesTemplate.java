package com.seveniu.template;

import com.alibaba.fastjson.TypeReference;
import com.seveniu.util.Json;
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
        if(index < templates.size()) {
            return templates.get(index);
        }
        return null;
    }

    public String getId() {
        return id;
    }

    public int pagesNum() {
        return templates.size();
    }

    public static PagesTemplate fromJson(String id, String json) {
        PagesTemplate pagesTemplate = new PagesTemplate();
        List<Template> templates = Json.toObject(json, new TypeReference<List<Template>>() {
        });
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
                field.setId((Integer) label.get("id"));
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
        pagesTemplate = fromJson("1", "[{\"fields\":[{\"defaultValue\":\"\",\"id\":5,\"must\":false,\"name\":\"链接\",\"type\":2,\"xpath\":\"//div[@class='listTitle']/h4[@class='itemName']/a\"},{\"defaultValue\":\"\",\"id\":6,\"must\":false,\"name\":\"下一页\",\"type\":3,\"xpath\":\"\"}],\"name\":\"首页\",\"url\":\"http://www.smzdm.com/\"},{\"fields\":[{\"defaultValue\":\"\",\"id\":1,\"must\":false,\"name\":\"作者\",\"type\":5,\"xpath\":\"//body/section[@class='wrap']/div[@class='leftWrap']/article[@class='article-details']/div[@class='article-top-box clearfix']/div[@class='article-right']/div[@class='article-meta-box']/div[@class='article_meta']/span[@class='ellipsis author']\"},{\"defaultValue\":\"\",\"id\":2,\"must\":false,\"name\":\"标题\",\"type\":5,\"xpath\":\"//body/section[@class='wrap']/div[@class='leftWrap']/article[@class='article-details']/div[@class='article-top-box clearfix']/div[@class='article-right']/h1[@class='article_title']/em\"},{\"defaultValue\":\"\",\"id\":7,\"must\":false,\"name\":\"内容\",\"type\":1,\"xpath\":\"//body/section[@class='wrap']/div[@class='leftWrap']/article[@class='article-details']/div[@class='article-top-box clearfix']\"},{\"defaultValue\":\"\",\"id\":8,\"must\":false,\"name\":\"时间\",\"type\":5,\"xpath\":\"//body/section[@class='wrap']/div[@class='leftWrap']/article[@class='article-details']/div[@class='article-top-box clearfix']/div[@class='article-right']/div[@class='article-meta-box']/div[@class='article_meta']/span[2]\"},{\"defaultValue\":\"\",\"id\":6,\"must\":false,\"name\":\"下一页\",\"type\":3,\"xpath\":\"\"}],\"name\":\"内容\",\"url\":\"http://www.smzdm.com/p/6237042/\"}]");
        System.out.println(Json.toJson(pagesTemplate));
        System.out.println(Json.toObject("\"71287\"]", Integer.class));
        System.out.println(Json.toObject("[\"71287\",\"95953\"]", new TypeReference<List<Integer>>() {
        }));
    }

    @Override
    public String toString() {
        return "PagesTemplate{" +
                "templates=" + templates +
                '}';
    }


}

package com.seveniu.template.def;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.node.JsonNodeType;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by seveniu on 5/12/16.
 * Field
 */
public class Field {

    private int htmlType;
    private int contentType;
    private String name;

    private String defaultValue = "";
    private String xpath;
    private List<String> regex;
    private boolean must;

    public int getHtmlType() {
        return htmlType;
    }

    public void setHtmlType(int htmlType) {
        this.htmlType = htmlType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getContentType() {
        return contentType;
    }

    public void setContentType(int contentType) {
        this.contentType = contentType;
    }

    public String getXpath() {
        return xpath;
    }

    public void setXpath(String xpath) {
        this.xpath = xpath;
    }

    public List<String> getRegex() {
        return regex;
    }

    public void setRegex(List<String> regex) {
        this.regex = regex;
    }


    public String getDefaultValue() {
        if (defaultValue == null) {
            defaultValue = "";
        }
        return defaultValue;
    }

    public void setDefaultValue(String defaultValue) {
        this.defaultValue = defaultValue;
    }

    public boolean isMust() {
        return must;
    }

    public void setMust(boolean must) {
        this.must = must;
    }

    @Override
    public String toString() {
        return "Field{" +
                "htmlType=" + htmlType +
                ", contentType=" + contentType +
                ", name='" + name + '\'' +
                ", defaultValue='" + defaultValue + '\'' +
                ", xpath='" + xpath + '\'' +
                ", regex=" + regex +
                ", must=" + must +
                '}';
    }

    public static void main(String[] args) throws IOException {
        String json = "{\"id\":8,\"text\":\"时间\",\"type\":1,\"must\":true,\"withFormat\":false,\"xpath\":\"//*[@id=\\\"erji_body_main\\\"]/div[3]/table/tbody/tr[3]/td/div\",\"regExp\":[\"asdf\",\"xxx\"],\"value\":null,\"indexKeyWordId\":-1,\"defaultValue\":\"aaaaaaaaaa\"}";
//        Field field = Json.toObject(json,Field.class);
//        ObjectMapper objectMapper = new ObjectMapper();
//        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
//        Field field1 = objectMapper.readValue(json, Field.class);
//        System.out.println(field1);
        System.out.println(fromOld(json));
    }

    public static Field fromOld(String json) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        SimpleModule module = new SimpleModule();
        module.addDeserializer(Field.class, new OldFieldDeserializer());
        mapper.registerModule(module);
        return mapper.readValue(json,Field.class);
    }

    public static final class OldFieldDeserializer
            extends JsonDeserializer<Field> {
        @Override
        public Field deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException, JsonProcessingException {
            Field field = new Field();
            JsonNode node = jp.getCodec().readTree(jp);

            JsonNode htmlType = node.get("type");
            field.setHtmlType(htmlType.intValue());

            JsonNode contentType = node.get("id");
            field.setContentType(contentType.intValue());

            JsonNode name = node.get("text");
            field.setName(name.asText());

            JsonNode xpath = node.get("xpath");
            field.setXpath(xpath.asText());

            JsonNode regex = node.get("regExp");
            if(regex.isArray()) {
                List<String> temp = new ArrayList<>(regex.size());
                for (JsonNode jsonNode : regex) {
                    temp.add(jsonNode.textValue());
                }
                field.setRegex(temp);
            }


            JsonNode defaultValueNode = node.get("defaultValue");
            if(defaultValueNode.getNodeType() == JsonNodeType.STRING){
                field.setDefaultValue(defaultValueNode.textValue());
            }

            JsonNode must = node.get("must");
            field.setMust(must.asBoolean());
            return field;
        }
    }
}

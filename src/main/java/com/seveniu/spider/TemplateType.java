package com.seveniu.spider;


/**
 * Created by seveniu on 5/17/16.
 * SpiderType
 */
public enum TemplateType {
    MULTI_LAYER_CONTENT(1),
    TEST_SINGLE_PAGE(2);


    private int id;

    TemplateType(int i) {
        this.id = i;
    }


    public static TemplateType get(int id) throws IllegalArgumentException {
        switch (id) {
            case 1:
                return MULTI_LAYER_CONTENT;
            case 2:
                return TEST_SINGLE_PAGE;
            default:
                throw new IllegalArgumentException("id " + id + " is error");
        }
    }

    @Override
    public String toString() {
        return "SpiderType{" +
                "id=" + id +
                '}';
    }
}

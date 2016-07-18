package com.seveniu.template.def;

/**
 * Created by seveniu on 5/12/16.
 * FieldType
 */
public enum FieldType {
    //HTML_TEXT 文本
    // TARGET_LINK 跳转链接
    // NEXT_LINK 下一页链接
    // TEXT_LINK 抓取的链接文本
    HTML_TEXT(1), TARGET_LINK(2), NEXT_LINK(3), TEXT_LINK(4), PURE_TEXT(5), DOWNLOAD_LINK(6);
    private int id;

    FieldType(int i) {
        this.id = i;
    }

    public int getId() {
        return id;
    }

    public static FieldType getType(int value) {
        FieldType[] fieldTypes = FieldType.values();
        for (FieldType fieldType : fieldTypes) {
            if(fieldType.getId() == value) {
                return fieldType;
            }
        }
        return null;
    }
}

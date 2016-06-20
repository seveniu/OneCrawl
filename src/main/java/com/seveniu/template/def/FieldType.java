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
    HTML_TEXT(1), TARGET_LINK(2),NEXT_LINK(3), TEXT_LINK(4), PURE_TEXT(5);
    private int id;

    FieldType(int i) {
        this.id = i;
    }

    public int getId() {
        return id;
    }

    public static FieldType getType(int value) {
        if(value == FieldType.HTML_TEXT.getId()) {
            return HTML_TEXT;
        } else if (value == FieldType.TARGET_LINK.getId()) {
            return TARGET_LINK;
        } else if (value == FieldType.NEXT_LINK.getId()) {
            return NEXT_LINK;
        } else if (value == FieldType.TEXT_LINK.getId()) {
            return TEXT_LINK;
        } else if (value == FieldType.PURE_TEXT.getId()) {
            return PURE_TEXT;
        }

        return null;
    }
}

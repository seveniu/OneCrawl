package com.seveniu.template.def;

/**
 * Created by seveniu on 5/12/16.
 * FieldType
 */
public enum FieldHtmlType {
    //TEXT 文本
    // TARGET_LINK 跳转链接
    // NEXT_LINK 下一页链接
    // TEXT_LINK 抓取的链接文本
    TEXT(1), TARGET_LINK(2),NEXT_LINK(3), TEXT_LINK(4);
    private int id;

    FieldHtmlType(int i) {
        this.id = i;
    }

    public int getId() {
        return id;
    }

    public static FieldHtmlType getType(int value) {
        if(value == FieldHtmlType.TEXT.getId()) {
            return TEXT;
        } else if (value == FieldHtmlType.TARGET_LINK.getId()) {
            return TARGET_LINK;
        } else if (value == FieldHtmlType.NEXT_LINK.getId()) {
            return NEXT_LINK;
        } else if (value == FieldHtmlType.TEXT_LINK.getId()) {
            return TEXT_LINK;
        }

        return null;
    }
}

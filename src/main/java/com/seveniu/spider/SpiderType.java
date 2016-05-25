package com.seveniu.spider;

/**
 * Created by seveniu on 5/17/16.
 * SpiderType
 */
public enum SpiderType {
    ONE_LAYER(1);


    private int id;

    SpiderType(int i) {
        this.id = i;
    }


    @Override
    public String toString() {
        return "SpiderType{" +
                "id=" + id +
                '}';
    }
}

package com.seveniu.customer;

/**
 * Created by seveniu on 5/23/16.
 * ConsumerConfig
 */
public class RemoteConsumerConfig {
    private String name;
    private String duplicateUrl;
    private String receiveUrl;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDuplicateUrl() {
        return duplicateUrl;
    }

    public void setDuplicateUrl(String duplicateUrl) {
        this.duplicateUrl = duplicateUrl;
    }

    public String getReceiveUrl() {
        return receiveUrl;
    }

    public void setReceiveUrl(String receiveUrl) {
        this.receiveUrl = receiveUrl;
    }
}

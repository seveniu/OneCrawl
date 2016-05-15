package com.seveniu.customer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by seveniu on 5/14/16.
 * DefaultConsumer
 */
public class DefaultConsumer extends Consumer{
    private Logger logger = LoggerFactory.getLogger(DefaultConsumer.class);
    public DefaultConsumer() {
        super("default");
    }

    @Override
    public boolean has(String url) {
        return false;
    }

    @Override
    public void out(String result) {
        logger.info(result);
    }
}

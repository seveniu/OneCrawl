package com.seveniu.dataProcess;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

/**
 * Created by seveniu on 10/11/16.
 * *
 */
@Service
public class MysqlDataQueue implements DataQueue {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    private final
    JdbcTemplate jdbcTemplate;

    @Autowired
    public MysqlDataQueue(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void addData(String key, String data) {
        jdbcTemplate.update("INSERT INTO queue (name,data) values (?,?)", key, data);
    }
}

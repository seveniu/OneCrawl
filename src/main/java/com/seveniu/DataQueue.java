package com.seveniu;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

/**
 * Created by seveniu on 10/11/16.
 * *
 */
@Service
public class DataQueue {


    private JedisPool jedisPool;

    @Autowired
    public DataQueue(@Value("${spring.redis.host}") String host,
                     @Value("${spring.redis.port}") int port) {
        this.jedisPool = new JedisPool(host, port);
    }

    public void addData(String key, String data) {
        try (Jedis jedis = this.jedisPool.getResource()) {
            jedis.lpush(key, data);
        }
    }
}

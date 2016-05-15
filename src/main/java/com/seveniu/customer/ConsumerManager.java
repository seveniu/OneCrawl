package com.seveniu.customer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.*;

/**
 * Created by seveniu on 5/13/16.
 * CustomerManager
 */
@Component
public class ConsumerManager {

    private Logger logger = LoggerFactory.getLogger(ConsumerManager.class);
    private static final int WAIT_THRESHOLD = 1000;
    private ConcurrentHashMap<String, Consumer> customers = new ConcurrentHashMap<>();

    public ConsumerManager() {
        monitor();
        regCustomer(new DefaultConsumer());
    }

    public void regCustomer(Consumer consumer) {
        customers.put(consumer.getName(), consumer);
    }

    public void removeCustomer(String customerName) {
        customers.remove(customerName);
    }

    public Consumer getCustomer(String name) {
        return customers.get(name);
    }

    private void monitor() {
        ScheduledExecutorService scheduled = Executors.newSingleThreadScheduledExecutor(new ThreadFactory() {
            @Override
            public Thread newThread(Runnable r) {
                return new Thread(r, "OutManager-monitor-thread");
            }
        });

        scheduled.scheduleWithFixedDelay(new Runnable() {
            @Override
            public void run() {
                for (Map.Entry<String, Consumer> entry : customers.entrySet()) {
                    Consumer consumer = entry.getValue();
                    int waitSize = consumer.waitSize();
                    if (waitSize > WAIT_THRESHOLD) {
                        logger.warn("outer '{}' has more than threshold,cur wait size : {}", entry.getKey(), consumer.waitSize());
                    }
                }
            }
        }, 0, 1, TimeUnit.MINUTES);
    }

}

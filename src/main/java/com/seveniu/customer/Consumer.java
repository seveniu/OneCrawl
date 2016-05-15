package com.seveniu.customer;

import com.seveniu.filter.Filter;
import com.seveniu.out.Out;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.LinkedBlockingQueue;

/**
 * Created by seveniu on 5/13/16.
 * Dominater
 */
public abstract class Consumer implements Filter, Out {
    private Logger logger = LoggerFactory.getLogger(Consumer.class);
    private String name;
    private LinkedBlockingQueue<String> queue = new LinkedBlockingQueue<>();

    public Consumer(String name) {
        this.name = name;
        transfer();
    }


    public String getName() {
        return name;
    }


    private void transfer() {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    try {
                        String data = queue.take();
                        out(data);
                    } catch (InterruptedException e) {
                        logger.error("outer : {} thread error : {}", getName(), e.getMessage());
                        break;
                    }
                }

            }
        }, getName() + "-transfer-thread");
        thread.start();
    }

    int waitSize() {
        return queue.size();
    }
}

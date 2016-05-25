package com.seveniu.spider.imgParse.filter;

import com.google.common.hash.BloomFilter;
import com.google.common.hash.Funnel;
import com.google.common.hash.Funnels;
import com.seveniu.common.tools.ShutdownHook;
import com.seveniu.common.tools.ShutdownHookManager;
import com.seveniu.def.SystemError;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by seveniu on 5/24/16.
 * BloomFilterImpl
 */
public class BloomFilterImpl implements ImageRepeatCheck, ShutdownHook {
    private BloomFilter<CharSequence> images;
    private AtomicInteger count = new AtomicInteger();
    private String serializablePath;

    public BloomFilterImpl(String serializablePath) {
        this.serializablePath = serializablePath;
        ShutdownHookManager.get().register(this);
        deserialization();
    }

    @Override
    public void put(String md5) {
        images.put(md5);
        count.incrementAndGet();
        if (count.get() > 100) {
            count.set(0);
            serializableAsync();
        }
    }

    @Override
    public boolean contain(String md5) {
        return images.mightContain(md5);
    }

    private void deserialization() {

        Funnel<CharSequence> funnel = Funnels.stringFunnel(Charset.forName("UTF-8"));
        try {
            File file = new File(serializablePath);
            if (file.exists()) {
                images = BloomFilter.readFrom(new FileInputStream(serializablePath), funnel);
            } else {
                images = BloomFilter.create(funnel, 1000000, 0.001);
            }
        } catch (IOException e) {
            e.printStackTrace();
            SystemError.shutdown(SystemError.IMAGE_BLOOMFILTER_DESERIALIZATION_ERROR);
        }
    }

    private void serializable() {
        try {
            File file = new File(serializablePath);
            if (!file.exists()) {
                if (file.getParentFile() != null) {
                    file.getParentFile().mkdirs();
                }
            }
            images.writeTo(new FileOutputStream(serializablePath));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void serializableAsync() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                serializable();
            }
        }, "image-serializable").start();
    }

    @Override
    public void shutdown() {
        serializable();
    }

}

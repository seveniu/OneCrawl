package com.seveniu.util;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * User: seveniu
 * Date: 8/17/15
 * Time: 2:20 AM
 * Project: dhlz-search
 */
@Component
public class AppContext implements ApplicationContextAware {


    private static ApplicationContext ctx;
    private static AppContext instance = new AppContext();

    private AppContext() {
    }

    public static AppContext get() {
        return instance;
    }

    public synchronized void setCtx(ApplicationContext ctx) {
        if (ctx == null) {
            throw new NullPointerException();
        }
        AppContext.ctx = ctx;
    }

    public static Object getBean(String beanName) {
        if (AppContext.ctx == null) {
            throw new NullPointerException("ctx not set");
        }
        return AppContext.ctx.getBean(beanName);
    }

    public static <T> T getBean(Class<T> tClass) {
        if (AppContext.ctx == null) {
            throw new NullPointerException("ctx not set");
        }
        return AppContext.ctx.getBean(tClass);
    }

    public ApplicationContext getCtx() {
        return ctx;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        AppContext.ctx = applicationContext;
        System.out.println("get ctx : " + AppContext.ctx);
    }
}

/*
 * Copyright 2012-2013 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.seveniu;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import java.util.Arrays;

@SpringBootApplication
@Configuration
@EnableAutoConfiguration
@ComponentScan("com.seveniu")
public class Application {
    private static final Logger logger = LoggerFactory.getLogger(Application.class);
    private static ApplicationContext ctx;
    @Autowired
    private Environment environment;

    public static ApplicationContext getCtx() {
        return ctx;
    }

    public static void main(String[] args) throws Exception {
        ctx = SpringApplication.run(Application.class, args);
//        System.out.println(Arrays.toString(ctx.getBeanDefinitionNames()));
        Application application = ctx.getBean(Application.class);
        application.init();
    }

    private void init() {
        logger.debug("profile:{}", environment.getProperty("spring.profiles.active"));
        logger.debug("hello:{}", environment.getProperty("hello"));
        logger.debug("env:{}", Arrays.toString(environment.getActiveProfiles()));
    }

}

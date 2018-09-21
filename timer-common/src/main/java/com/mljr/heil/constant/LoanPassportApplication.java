package com.mljr.heil.constant;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

/**
 * @description:
 * @author: Guo Lixiao
 * @date: 2018-1-8 15:58
 * @see:
 * @since:
 **/

@SpringBootApplication
@EnableAutoConfiguration
public class LoanPassportApplication {
    private static Logger log = LoggerFactory.getLogger(LoanPassportApplication.class);

    public static void main(String[] args) {
        System.setProperty("dubbo.application.logger", "slf4j");
        ConfigurableApplicationContext context = SpringApplication.run(LoanPassportApplication.class, args);
        String[] activeProfiles = context.getEnvironment().getActiveProfiles();
        for (String profile : activeProfiles) {
            System.out.println("Spring active profile:" + profile);
        }
        Logger logger = LoggerFactory.getLogger(LoanPassportApplication.class);
        log.info("Server startup");
        logger.info("应用程序启动完毕!");
    }
}

package com.mljr.heil;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.PropertySource;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * @description:
 * @author: Guo Lixiao
 * @date: 2018-1-8 15:58
 * @see:
 * @since:
 **/

@SpringBootApplication
@EnableAutoConfiguration
@ComponentScan(basePackages = {"com.mljr"})
@PropertySource("classpath:config.properties")
@EnableSwagger2
public class MainApplication extends SpringBootServletInitializer {
    private static Logger log = LoggerFactory.getLogger(MainApplication.class);

    public static void main(String[] args) {
        System.setProperty("dubbo.application.logger", "slf4j");
        ConfigurableApplicationContext context = SpringApplication.run(MainApplication.class, args);
        String[] activeProfiles = context.getEnvironment().getActiveProfiles();
        for (String profile : activeProfiles) {
            System.out.println("Spring active profile:" + profile);
        }
        Logger logger = LoggerFactory.getLogger(MainApplication.class);
        log.info("Server startup");
        logger.info("应用程序启动完毕!");
    }
}

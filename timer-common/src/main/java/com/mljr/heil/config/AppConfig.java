package com.mljr.heil.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * Author : LI-JIAN
 * Date   : 2017-03-22
 * 全局配置
 */
@Component
public class AppConfig {
    Logger logger = LoggerFactory.getLogger(this.getClass());

    @Value("${app.env.name:prod}")
    String envName;

    boolean isProduct = false;

    @PostConstruct
    public void init() {
        isProduct = "prod".equalsIgnoreCase(envName);
        logger.info("car-timer 当前启动环境---------->{}", envName);
    }

    public boolean isProduct() {
        return isProduct;
    }

    public String getEnvName() {
        return envName;
    }

}

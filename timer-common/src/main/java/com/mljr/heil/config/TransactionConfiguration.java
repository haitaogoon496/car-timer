package com.mljr.heil.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.jdbc.DataSourceTransactionManagerAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;

/**
 * @description:
 * @Date : 2018/7/12$ 17:56$
 * @Author : liht
 */
@EnableTransactionManagement
@Slf4j
@AutoConfigureAfter({ MybatisConfig.class })
@Configuration
public class TransactionConfiguration extends DataSourceTransactionManagerAutoConfiguration {

    @Bean
    @Autowired
    public DataSourceTransactionManager transactionManager(DynamicDataSource dynamicDataSource) {
        log.info("事物配置--------------------:{}", dynamicDataSource);
        DataSourceTransactionManager transactionManager = new DataSourceTransactionManager(dynamicDataSource);
        transactionManager.setGlobalRollbackOnParticipationFailure(true);
//        log.info("事物配置--------------------:{}", master);
//        DataSourceTransactionManager transactionManager = new DataSourceTransactionManager(master);
//        transactionManager.setGlobalRollbackOnParticipationFailure(true);

        return transactionManager;
    }

}

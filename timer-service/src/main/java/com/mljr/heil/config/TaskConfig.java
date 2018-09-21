package com.mljr.heil.config;

import org.quartz.JobListener;
import org.quartz.SchedulerListener;
import org.quartz.TriggerListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;

/**
 * Author : BlackShadowWalker
 * Date   : 2016-11-04
 */
@Configuration
@EnableScheduling
@Order(Ordered.LOWEST_PRECEDENCE+30)
@PropertySource(value = {"classpath:app.properties"},encoding = "utf-8",ignoreResourceNotFound = true)
public class TaskConfig {

    Logger logger = LoggerFactory.getLogger(this.getClass());

    @Value("${app.env.name}")
    String envName;

    @Bean
    public SchedulerFactoryBean schedulerFactoryBean(
            SchedulerListener[] schedulerListeners,
            JobListener[] jobListeners,
            TriggerListener[] triggerListeners,
            @Qualifier("taskExecutor") TaskExecutor taskExecutor
    ){
        SchedulerFactoryBean item = new SchedulerFactoryBean();
        item.setSchedulerListeners(schedulerListeners);
        item.setGlobalJobListeners(jobListeners);
        item.setGlobalTriggerListeners(triggerListeners);
        item.setAutoStartup(true);
        item.setTaskExecutor(taskExecutor);
        item.setConfigLocation(new ClassPathResource("quartz.properties"));
        item.setSchedulerName("car-timer");
        logger.info("SchedulerFactoryBean实例化完成，当前选择的环境是   ------    >:{}", envName);
        return item;
    }

}

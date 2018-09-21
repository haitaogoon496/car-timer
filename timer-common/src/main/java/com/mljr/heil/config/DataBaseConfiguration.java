package com.mljr.heil.config;

import com.alibaba.druid.pool.DruidDataSource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.bind.RelaxedPropertyResolver;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.*;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.core.env.Environment;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

/**
 * @description:
 * @Date : 2018/7/12$ 17:39$
 * @Author : liht
 */
@Configuration
@PropertySource(value = "classpath:db.properties" ,encoding = "utf-8",ignoreResourceNotFound = true)
@Import(MybatisConfig.class)
@Order(Ordered.LOWEST_PRECEDENCE)
@Slf4j
@EnableAspectJAutoProxy
public class DataBaseConfiguration implements EnvironmentAware {

    private RelaxedPropertyResolver propertyResolver1;
    private RelaxedPropertyResolver propertyResolver2;

    public DataBaseConfiguration(){
        System.out.println("#################### DataBaseConfiguration");
    }
    @Override
    public void setEnvironment(Environment env) {
        this.propertyResolver1 = new RelaxedPropertyResolver(env, "spring.datasource.");
        this.propertyResolver2 = new RelaxedPropertyResolver(env, "database.read.");
    }

    public DataSource master() {
        System.out.println("注入Master druid！！！");
        log.info("{},{}，{}，{}，{}，{}，{}",propertyResolver1.getProperty("url")
        ,propertyResolver1.getProperty("driverClassName"),
                propertyResolver1.getProperty("username"),
                propertyResolver1.getProperty("password"),
                propertyResolver1.getProperty("initial-size"),
                propertyResolver1.getProperty("min-idle"),
                propertyResolver1.getProperty("max-wait"));
        DruidDataSource datasource = new DruidDataSource();
        datasource.setUrl(propertyResolver1.getProperty("url"));
        datasource.setDriverClassName(propertyResolver1.getProperty("driverClassName"));
        datasource.setUsername(propertyResolver1.getProperty("username"));
        datasource.setPassword(propertyResolver1.getProperty("password"));
        datasource.setInitialSize(Integer.valueOf(propertyResolver1.getProperty("initial-size")));
        datasource.setMinIdle(Integer.valueOf(propertyResolver1.getProperty("min-idle")));
        datasource.setMaxWait(Long.valueOf(propertyResolver1.getProperty("max-wait")));
        datasource.setMaxActive(Integer.valueOf(propertyResolver1.getProperty("max-active")));
        //datasource.setMinEvictableIdleTimeMillis(Long.valueOf(propertyResolver1.getProperty("min-evictable-idle-time-millis")));
        try {
            datasource.setFilters("stat,wall");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return datasource;
    }
    public DataSource slave() {
        System.out.println("Slave druid！！！");
        DruidDataSource datasource = new DruidDataSource();
        datasource.setUrl(propertyResolver2.getProperty("url"));
        datasource.setDriverClassName(propertyResolver2.getProperty("driverClassName"));
        datasource.setUsername(propertyResolver2.getProperty("username"));
        datasource.setPassword(propertyResolver2.getProperty("password"));
        datasource.setInitialSize(Integer.valueOf(propertyResolver2.getProperty("initial-size")));
        datasource.setMinIdle(Integer.valueOf(propertyResolver2.getProperty("min-idle")));
        datasource.setMaxWait(Long.valueOf(propertyResolver2.getProperty("max-wait")));
        datasource.setMaxActive(Integer.valueOf(propertyResolver2.getProperty("max-active")));
        //datasource.setMinEvictableIdleTimeMillis(Long.valueOf(propertyResolver2.getProperty("min-evictable-idle-time-millis")));
        try {
            datasource.setFilters("stat,wall");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return datasource;
    }

    @Bean("dynamicDataSource")
    @Order(Ordered.LOWEST_PRECEDENCE+10)
    public DynamicDataSource dynamicDataSource() {
        System.out.println("注入了动态数据源");
        DataSource master = master();
        DataSource slave = slave();
        Map<Object, Object> targetDataSources = new HashMap<Object, Object>();
        targetDataSources.put(DynamicDataSource.DatabaseType.Master, master);
        targetDataSources.put(DynamicDataSource.DatabaseType.Slave, slave);

        DynamicDataSource dataSource = new DynamicDataSource();
        dataSource.setTargetDataSources(targetDataSources);// 该方法是AbstractRoutingDataSource的方法
        dataSource.setDefaultTargetDataSource(master);
        System.out.println("动态数据源注入成功===============");
        return dataSource;
    }
}

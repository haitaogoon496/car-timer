package com.mljr.heil.config;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.mapper.MapperScannerConfigurer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import java.util.Properties;

@Configuration
@Order(Ordered.HIGHEST_PRECEDENCE + 1000)
@EnableTransactionManagement(proxyTargetClass = true)
public class MybatisConfig {
	private Logger log  = LoggerFactory.getLogger(this.getClass());
	
	private final String mapper = "com.mljr.heil.mapper";
	
    @Bean
    @Order(Ordered.HIGHEST_PRECEDENCE + 30)
    public MapperScannerConfigurer mapperScannerConfigurer() {
    	log.info("MybatisConfig-mapperScannerConfigurerz注入");
        MapperScannerConfigurer mapperScannerConfigurer = new MapperScannerConfigurer();
        //获取之前注入的beanName为sqlSessionFactory的对象
        mapperScannerConfigurer.setSqlSessionFactoryBeanName("sqlSessionFactory");
        //指定xml配置文件的路径
        mapperScannerConfigurer.setBasePackage(mapper);
        return mapperScannerConfigurer;
    }


    //=======================================================================================================================
    @Bean(name = "sqlSessionFactory")
    @Autowired
    @Order(Ordered.HIGHEST_PRECEDENCE+5)
    public SqlSessionFactory sqlSessionFactory(@Qualifier("dynamicDataSource") DynamicDataSource dynamicDataSource) {

        SqlSessionFactoryBean bean = new SqlSessionFactoryBean();
        bean.setDataSource(dynamicDataSource);
        try {
            log.info("sqlSessionFactory============:dynamicDataSource:{},ClientInfo:{}",dynamicDataSource, dynamicDataSource.getConnection().getClientInfo());
            SqlSessionFactory session = bean.getObject();
            bean.setTypeAliasesPackage("com.mljr.heil.entity,com.mljr.heil.form");
            Properties properties = new Properties();
            properties.setProperty("dialect", "mysql");
            bean.setConfigurationProperties(properties);
            return session;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Bean(name = "sqlSessionTemplate")
    @Autowired
    @Order(Ordered.HIGHEST_PRECEDENCE+50)
    public SqlSessionTemplate sqlSessionTemplate(SqlSessionFactory sqlSessionFactory) {
        return new SqlSessionTemplate(sqlSessionFactory);
    }
//
//    @Bean
//    @Order(Ordered.HIGHEST_PRECEDENCE+70)
//    public MapperScannerConfigurer scannerConfigurer(){
//        MapperScannerConfigurer configurer = new MapperScannerConfigurer();
//        configurer.setSqlSessionFactoryBeanName("sqlSessionFactory");
//        configurer.setSqlSessionTemplateBeanName("sqlSessionTemplate");
//        configurer.setBasePackage(mapper);
//        configurer.setMarkerInterface(Mapper.class);
//        return configurer;
//    }




}

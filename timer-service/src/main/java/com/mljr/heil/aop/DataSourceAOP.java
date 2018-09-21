package com.mljr.heil.aop;

import com.mljr.heil.config.DynamicDataSource;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.stereotype.Component;

/**
 * @description:
 * @Date : 2018/7/12$ 17:59$
 * @Author : liht
 */
@Aspect
@Slf4j
@Component
public class DataSourceAOP {

//    @Before("execution( * com.mljr.heil.mapper..*.get*(..)) || execution( * com.mljr.heil.mapper..*.select*(..))" +
//            "|| execution( * com.mljr.heil.mapper..*.query*(..))")
    public void setReadDataSourceType() {
        DynamicDataSource.slave();
        log.info("============================================dataSource切换到：slave");
    }

//    @Before("execution( * com.mljr.heil.mapper..*.insert*(..)) || " +
//            "execution( * com.mljr.heil.mapper..*.update*(..)) || " +
//            "execution( * com.mljr.heil.mapper..*.delete*(..)) || " +
//            "execution( * com.mljr.heil.mapper..*.add*(..)) ||" +
//            "execution( * com.mljr.heil.service..*.insert*(..))")
    public void setWriteDataSourceType() {
        DynamicDataSource.master();
        log.info("================================================dataSource切换到：master");
    }
}

package com.mljr.heil.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

/**
 * @description:
 * @Date : 2018/7/12$ 17:42$
 * @Author : liht
 */
@Slf4j
public class DynamicDataSource extends AbstractRoutingDataSource {

    private static final ThreadLocal<DatabaseType> contextHolder = new ThreadLocal<DatabaseType>();

    @Override
    protected Object determineCurrentLookupKey() {
        Object object = contextHolder.get();
        log.info("当前数据源是:{}", object!=null?object.toString():"空数据源");
        return object;
    }

    public static enum DatabaseType {
        Master, Slave
    }

    public synchronized static void master(){
        contextHolder.set(DatabaseType.Master);
    }

    public synchronized static void slave(){
        contextHolder.set(DatabaseType.Slave);
    }

    public static void setDatabaseType(DatabaseType type) {
        contextHolder.set(type);
    }

    public static DatabaseType getType(){
        return contextHolder.get();
    }
}

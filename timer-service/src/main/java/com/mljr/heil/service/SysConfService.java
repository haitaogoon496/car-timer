package com.mljr.heil.service;


import com.mljr.heil.callback.LockExecuteCallback;
import com.mljr.heil.entity.SysConf;

import java.util.Date;
import java.util.Map;

/**
 * Author : BlackShadowWalker
 * Date   : 2016-09-06
 */
public interface SysConfService {

    int insert(SysConf item);

    int insertNewValue(String name, Object value);

    int insertNewValue(String name, Object value, String desp);

    boolean existName(String name);

    SysConf getByName(String name);

    String getValue(String name);

    Long getLongValue(String name);

    //读取值，如果没有则写入默认值，并返回默认值
    Long getOrInsertLongValue(String name, Long defaultValue);

    Boolean getBooleanValue(String name);

    Integer getIntegerValue(String name);

    Date getDateValue(String name);

    <T extends Enum> T getEnumValue(String name, Class<T> cls);

    Map<String, SysConf> getValues(String prefix);

    Map<String, String> getKeyValues(String prefix);

    <T> int updateValue(String name, T value);

    <T> int insertOrUpdateValue(String name, T value, String desp);

    int deleteValue(String name);

    int deleteValues(String prefix);

    /**
     * 加锁处理数据
     * @param lockKey 锁key
     * @param timeout 获取锁超时时间(秒)
     * @param expire  锁的有效期(秒)
     * @param callback 回调
     */
    void lockExecute(String lockKey, long timeout, long expire, boolean autoUnLock, LockExecuteCallback callback);

    //清理过期的 LOCK.key
    int cleanExpiredLockKey(long expireTimestamp);


}

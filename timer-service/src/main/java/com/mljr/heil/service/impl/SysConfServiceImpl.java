package com.mljr.heil.service.impl;

import com.mljr.heil.callback.LockExecuteCallback;
import com.github.blackshadowwalker.spring.distributelock.LockException;
import com.mljr.heil.entity.SysConf;
import com.mljr.heil.mapper.SysConfMapper;
import com.mljr.heil.service.SysConfService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Author : BlackShadowWalker
 * Date   : 2016-09-06
 */
@Service
public class SysConfServiceImpl implements SysConfService {
    private Logger log = LoggerFactory.getLogger(this.getClass());

    @Autowired
    SysConfMapper sysConfMapper;

    @Value("LOCK:")
    String lockKeyPrefix;

    public static final String CAPTCHA_TYPE = "sys.captchaType";

    @PostConstruct
    public void init() {
    }

    @Transactional
    @Override
    public int insert(SysConf item) {
        return sysConfMapper.insert(item);
    }

    @Transactional
    @Override
    public int insertNewValue(String name, Object value) {
        return sysConfMapper.insertNewValue(name, value != null ? value.toString() : null);
    }

    @Override
    public int insertNewValue(String name, Object value, String desp) {
        return sysConfMapper.insertConf(name, value != null ? value.toString() : null, desp);
    }

    @Override
    public boolean existName(String name) {
        return sysConfMapper.existName(name);
    }

    @Override
    public SysConf getByName(String name) {
        return sysConfMapper.getByName(name);
    }

    @Override
    public String getValue(String name) {
        return sysConfMapper.getValue(name);
    }

    @Override
    public Long getLongValue(String name) {
        return sysConfMapper.getLongValue(name);
    }

    //读取值，如果没有则写入默认值，并返回默认值
    @Override
    public Long getOrInsertLongValue(String name, Long defaultValue) {
        Long value = sysConfMapper.getLongValue(name);
        if (value == null && defaultValue != null) {
            sysConfMapper.insertNewValue(name, defaultValue.toString());
            return defaultValue;
        }
        return value;
    }

    @Override
    public Boolean getBooleanValue(String name) {
        return sysConfMapper.getBooleanValue(name);
    }

    @Override
    public Integer getIntegerValue(String name) {
        return sysConfMapper.getIntegerValue(name);
    }

    @Override
    public Date getDateValue(String name) {
        return sysConfMapper.getDateValue(name);
    }

    @Override
    public <T extends Enum> T getEnumValue(String name, Class<T> cls) {
        String value = sysConfMapper.getValue(name);
        return value != null ? (T) Enum.valueOf(cls, value) : null;
    }

    @Override
    public Map<String, SysConf> getValues(String prefix) {
        return sysConfMapper.getValues(prefix);
    }

    @Override
    public Map<String, String> getKeyValues(String prefix) {
        Map<String, SysConf> map = sysConfMapper.getValues(prefix);
        if (map == null || map.isEmpty()) {
            return null;
        }
        Map<String, String> ret = new HashMap<>();
        for (String key : map.keySet()) {
            ret.put(key, map.get(key).getValue());
        }
        return ret;
    }

    @Transactional
    @Override
    public <T> int updateValue(String name, T value) {
        return sysConfMapper.updateValue(name, value != null ? String.valueOf(value) : null);
    }

    @Override
    public <T> int insertOrUpdateValue(String name, T value, String desp) {
        if (sysConfMapper.existName(name)) {
            return sysConfMapper.updateValue(name, value != null ? String.valueOf(value) : null);
        } else {
            return sysConfMapper.insertConf(name, value != null ? String.valueOf(value) : null, "异步通知分发最近eventId");
        }
    }

    @Transactional
    @Override
    public int deleteValue(String name) {
        return sysConfMapper.deleteValue(name);
    }

    @Transactional
    @Override
    public int deleteValues(String prefix) {
        return sysConfMapper.deleteValues(prefix);
    }

    //    private final ReentrantLock takeLock = new ReentrantLock();
//    private final Condition lockCondition = takeLock.newCondition();
    private final Map<String, ReentrantLock> lockMap = new ConcurrentHashMap<>();
    private final Map<ReentrantLock, Condition> lockConditionMap = new ConcurrentHashMap<>();

    //加锁处理数据timeout=3,expire=120
    @Override
    public void lockExecute(final String lockKeyIN, long timeout, long expire, boolean autoUnLock, LockExecuteCallback callback) {
        timeout *= 1000;
        expire *= 1000;
        long sysTime = System.currentTimeMillis();
        long nextExpireTime = sysTime + expire;
        SysConf sysConf = null;
        String lockKey = lockKeyPrefix + lockKeyIN;
        boolean locked = false;

        sysConf = sysConfMapper.getByName(lockKey);//锁
        if (sysConf == null) {
            sysConfMapper.insertNewValue(lockKey, 0);
            sysConf = sysConfMapper.getByName(lockKey);
        }

        int lines = 0;
        Long value = null;
        ReentrantLock lock = null;
        Condition lockCondition = null;
        try {
            synchronized (lockMap) {
                lock = lockMap.get(lockKeyIN);
                if (lock == null || lockConditionMap.get(lock) == null) {
                    lock = new ReentrantLock();
                    lockCondition = lock.newCondition();
                    lockMap.put(lockKeyIN, lock);
                    lockConditionMap.put(lock, lockCondition);
                } else {
                    lockCondition = lockConditionMap.get(lock);
                }
            }
            lock.lock();
//            sysConfMapper.innodbLockWaitTimeout(timeout / 1000);
            while (true) {
                log.info("锁{}进来了-----",Thread.currentThread().getName());
                value = Long.parseLong(sysConf.getValue());
                if (System.currentTimeMillis() > value) {
                    lines = sysConfMapper.updateValueByIdOptLock(sysConf.getId(), nextExpireTime, value);//加锁
                }
                if (lines == 1) {
                    locked = true;
                    break;
                }
                if (System.currentTimeMillis() - sysTime > timeout) {
                    break;
                }
                sysConf = sysConfMapper.getByName(lockKey);

                if (timeout > 10 * 1000) {
                    lockCondition.await(timeout / 10, TimeUnit.MILLISECONDS);
                } else {
                    lockCondition.await(timeout, TimeUnit.MILLISECONDS);
                }
            }

            if (!locked) {
                throw new LockException("锁已经被抢占");
            }
        } catch (InterruptedException e) {
            log.error("", e);
        } catch (Exception e) {
            throw e;
        } finally {
            lockCondition.signalAll();
            lock.unlock();
            if (autoUnLock && locked) {
                sysConfMapper.updateValueByIdOptLock(sysConf.getId(), 0, value);
            }
        }
        if (locked) {
            callback.execute();
        }
    }

    @Override
    public int cleanExpiredLockKey(long expireTimestamp) {
        return sysConfMapper.cleanExpiredLockKey(lockKeyPrefix, expireTimestamp);
    }

}

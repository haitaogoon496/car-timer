package com.mljr.heil.service.impl;

import com.github.blackshadowwalker.spring.distributelock.LockException;
import com.mljr.heil.callback.LockExecuteCallback;
import com.mljr.heil.entity.SysConf;
import com.mljr.heil.entity.User;
import com.mljr.heil.mapper.SysConfMapper;
import com.mljr.heil.mapper.UserMapper;
import com.mljr.heil.service.SysConfService;
import com.mljr.heil.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
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
public class UserServiceImpl implements UserService {
    private Logger log = LoggerFactory.getLogger(this.getClass());

    @Autowired
    UserMapper userMapper;

    @PostConstruct
    public void init() {
    }

    @Transactional
    @Override
    public int insert(User item) {
        return userMapper.insert(item);
    }


    @Override
    public User getByName(String name) {
        return userMapper.getByName(name);
    }

    @Override
    public List<User> getUserListByName(String userName) {
        return userMapper.getUserListByName(userName);
    }
}

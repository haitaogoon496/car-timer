package com.mljr.heil.service;


import com.mljr.heil.callback.LockExecuteCallback;
import com.mljr.heil.entity.SysConf;
import com.mljr.heil.entity.User;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Author : BlackShadowWalker
 * Date   : 2016-09-06
 */
public interface UserService {

    int insert(User item);

    User getByName(String name);

    List<User> getUserListByName(String userName);
}

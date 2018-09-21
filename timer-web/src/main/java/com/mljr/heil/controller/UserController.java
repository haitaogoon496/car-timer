package com.mljr.heil.controller;

import com.mljr.heil.config.DynamicDataSource;
import com.mljr.heil.dto.ResponseDTO;
import com.mljr.heil.entity.SysConf;
import com.mljr.heil.entity.User;
import com.mljr.heil.service.SysConfService;
import com.mljr.heil.service.UserService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * @description:
 * @Date : 2018/7/12$ 21:15$
 * @Author : liht
 */
@RequestMapping("/user")
@RestController
public class UserController {

    @Resource
    private UserService userService;

    @Resource
    private DynamicDataSource dynamicDataSource;

    @RequestMapping(value = "/getUserByName/{name}",method = RequestMethod.GET)
    public User getUser(@PathVariable String name) {
        User user = userService.getByName(name);
        user.setDesp(DynamicDataSource.getType()!=null?DynamicDataSource.getType().toString():"");
        return user;
    }

    @RequestMapping(value = "insertUser",method = RequestMethod.PUT)
    public ResponseDTO<Object> insert(@RequestBody User user) {
        userService.insert(user);
        return new ResponseDTO(DynamicDataSource.getType());
    }

    @RequestMapping(value = "/getUserListByName/{name}",method = RequestMethod.GET)
    public List<User> getUserList(@PathVariable String name) {
        List<User> userList = userService.getUserListByName(name);
        userList.stream().forEach(item->{
            item.setDesp(DynamicDataSource.getType()!=null?DynamicDataSource.getType().toString():"");
        });
        return userList;
    }
}

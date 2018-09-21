package com.mljr.heil.mapper;

import com.mljr.heil.entity.SysConf;
import com.mljr.heil.entity.User;
import org.apache.ibatis.annotations.*;

import java.util.List;

public interface UserMapper {
    String TABLE = "t_user";

    @Insert("insert into " + TABLE + "(user_name, password) " +
            "values(#{userName}, #{password})")
    @Options(useGeneratedKeys = true)
    int insert(User item);

    @Select("select * from " + TABLE + " where user_name = #{userName}")
    @ResultMap("com.mljr.heil.mapper.UserMapper.BaseResultMap")
    User getByName(String userName);

    @Select("select * from " + TABLE + " where user_name = #{userName}")
    @ResultMap("com.mljr.heil.mapper.UserMapper.BaseResultMap")
    List<User> getUserListByName(String userName);
}
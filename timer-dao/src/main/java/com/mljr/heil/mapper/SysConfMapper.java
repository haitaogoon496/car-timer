package com.mljr.heil.mapper;

import com.mljr.heil.entity.SysConf;
import org.apache.ibatis.annotations.*;

import java.util.Date;
import java.util.Map;

public interface SysConfMapper {
    String TABLE = "t_sys_conf";

    @Insert("insert into " + TABLE + "(name, value, createTime) " +
            "values(#{name}, #{value}, now())")
    @Options(useGeneratedKeys = true)
    int insert(SysConf item);

    @Insert("insert into " + TABLE + "(name, value, createTime) " +
            "values(#{name}, #{value}, now())")
    <T> int insertNewValue(@Param("name") String name, @Param("value") T value);

    @Insert("insert into " + TABLE + "(name, value, desp, createTime) " +
            "values(#{name}, #{value}, #{desp}, now())")
    <T> int insertConf(@Param("name") String name, @Param("value") T value, @Param("desp") String desp);

    @Select("select count(1) > 0 from " + TABLE + " where name = #{name}")
    @ResultType(Boolean.class)
    boolean existName(String name);

    @Select("select * from " + TABLE + " where name = #{name}")
    @ResultType(SysConf.class)
    SysConf getByName(String name);

    @Select("select value from " + TABLE + " where name = #{name}")
    @ResultType(String.class)
    String getValue(String name);

    @Select("select value from " + TABLE + " where name = #{name}")
    @ResultType(Long.class)
    Long getLongValue(String name);

    @Select("select value from " + TABLE + " where name = #{name}")
    @ResultType(Integer.class)
    Integer getIntegerValue(String name);

    @Select("select value from " + TABLE + " where name = #{name}")
    @ResultType(Boolean.class)
    Boolean getBooleanValue(String name);

    @Select("select value from " + TABLE + " where name = #{name}")
    @ResultType(Date.class)
    Date getDateValue(String name);

    @Select("select * from " + TABLE + " where name like concat(#{prefix}, '%')")
    @MapKey(value = "name")
    @ResultType(SysConf.class)
    Map<String, SysConf> getValues(String prefix);

    @Update("update " + TABLE + " set value = #{value} where name = #{name}")
    int updateValue(@Param("name") String name, @Param("value") String value);

    @Delete("delete from " + TABLE + " where name = #{name}")
    int deleteValue(String name);

    @Delete("delete from " + TABLE + " where name like concat(#{prefix}, '%')")
    int deleteValues(String prefix);

    @Options
    @Select("select * from " + TABLE + " where name = #{name} for update")
    @ResultType(Long.class)
    SysConf getLongLock(@Param("name") String name);

    @Update("update " + TABLE + " set value = #{value} where id = #{id}")
    int updateValueById(@Param("id") Long id, @Param("value") String value);

    @Update("update " + TABLE + " set value = #{value} where id = #{id} and value = #{preValue}")
    <T> int updateValueByIdOptLock(@Param("id") Long id, @Param("value") T value, @Param("preValue") T preValue);

    //清理过期的 LOCK.key
    @Delete("delete from " + TABLE + " where name like concat(#{keyPrefix}, '%') and value <= #{expireTimestamp} ")
    int cleanExpiredLockKey(@Param("keyPrefix") String keyPrefix, @Param("expireTimestamp") long expireTimestamp);

    @Update("set innodb_lock_wait_timeout = #{timeout}")
    int innodbLockWaitTimeout(Long timeout);
}
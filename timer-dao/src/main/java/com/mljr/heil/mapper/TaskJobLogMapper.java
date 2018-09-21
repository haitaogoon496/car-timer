package com.mljr.heil.mapper;

import com.mljr.heil.entity.TaskJobLog;

public interface TaskJobLogMapper {
    int deleteByPrimaryKey(Long id);

    int insert(TaskJobLog record);

    int insertSelective(TaskJobLog record);

    TaskJobLog selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(TaskJobLog record);

    int updateByPrimaryKey(TaskJobLog record);
}
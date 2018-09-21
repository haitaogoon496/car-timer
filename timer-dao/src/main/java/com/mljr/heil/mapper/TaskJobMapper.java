package com.mljr.heil.mapper;

import com.mljr.heil.entity.TaskJob;
import com.mljr.heil.entity.TaskJobLog;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.session.RowBounds;

import java.util.List;

public interface TaskJobMapper {
    String TABLE = "t_job";

    @Insert("insert into " + TABLE + "(jobGroup, jobName, name, cron, jobClass, tags, desp, jsonData, status, version, createTime) " +
            "value(#{jobGroup}, #{jobName}, #{name}, #{cron}, #{jobClass}, #{tags}, #{desp}, #{jsonData}, #{status}, #{version},#{createTime})")
    @Options(useGeneratedKeys = true)
    int insert(TaskJob job);

    @Select("select count(1) > 0 from " + TABLE + " where jobGroup = #{jobGroup} and jobName = #{jobName}")
    @ResultType(Boolean.class)
    Boolean existJob(@Param("jobGroup") String jobGroup, @Param("jobName") String jobName);

    @Select("select * from " + TABLE + " where jobGroup = #{jobGroup} and jobName = #{jobName}")
    @ResultType(TaskJob.class)
    TaskJob getByJobGroupName(@Param("jobGroup") String jobGroup, @Param("jobName") String jobName);

    @Select("select * from " + TABLE + " where id = #{id} ")
    @ResultType(TaskJob.class)
    TaskJob getById(Long id);

    @Select("select * from " + TABLE + " order by jobGroup, jobName ")
    @ResultType(TaskJob.class)
    List<TaskJob> queryAll();

    int update(TaskJob item);

    @Insert("insert into t_job_log(jobGroup, jobName, jobClass, name, tags, version, startTime, endTime, success, takesInMs, remark) " +
            "values(#{jobGroup}, #{jobName}, #{jobClass}, #{name}, #{tags}, #{version}, #{startTime}, #{endTime}, #{success}, #{takesInMs}, #{remark})")
    @Options(useGeneratedKeys = true)
    int insertLog(TaskJobLog item);

    int updateLog(TaskJobLog item);

    List<TaskJobLog> queryLog(TaskJobLog query, RowBounds rowBounds);
}
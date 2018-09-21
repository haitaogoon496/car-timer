package com.mljr.heil.service;

import com.github.pagehelper.PageInfo;

import com.mljr.heil.entity.TaskJob;
import com.mljr.heil.entity.TaskJobLog;
import com.mljr.heil.entity.TriggerJob;
import org.quartz.SchedulerException;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;

/**
 * Author : BlackShadowWalker
 * Date   : 2016-11-07
 */
public interface JobService {

    DateFormat CRON_FORMAT = new SimpleDateFormat("ss mm HH dd MM ? yyyy");

    Map<String, String> queryAllBizJobClass();

    void rescheduleJob(TaskJob taskJob) throws SchedulerException, ClassNotFoundException;

    void checkCron(String cron) throws ParseException;

    TaskJob getById(Long id);

    List<TaskJob> queryAll();

    //查询所有处于调度状态的job
    List<TriggerJob> queryScheduleJobs() throws SchedulerException;

    TaskJob getByJobGroupName(String jobGroup, String jobName);

    int insert(TaskJob taskJob) throws ParseException ;

    TaskJob update(Long id, TaskJob taskJob) throws ParseException;

    int insertLog(TaskJobLog log);

    int updateLog(TaskJobLog log);

    PageInfo<TaskJobLog> queryLog(TaskJobLog query, Integer pageNum, Integer pageSize);

    TaskJob triggerNow(Long id) throws SchedulerException, ClassNotFoundException;

    TaskJob pauseJob(Long id) throws SchedulerException;

    TaskJob removeJob(Long id) throws SchedulerException;

    TaskJob pauseTrigger(String name, String group) throws SchedulerException;

}

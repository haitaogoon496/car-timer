package com.mljr.heil.facade;

import com.mljr.heil.entity.TaskJob;
import com.mljr.heil.entity.TaskJobLog;
import com.mljr.heil.entity.TriggerJob;

import java.util.List;
import java.util.Map;

/**
 * Author : LI-JIAN
 * Date   : 2017-05-16
 */
public interface JobFacade {

    Map<String, String> queryAllBizJobClass();

    TaskJob rescheduleJob(TaskJob taskJob);

    String checkCron(String cron);

    TaskJob getById(Long id);

    List<TaskJob> queryAll();

    //查询所有处于调度状态的job
    List<TriggerJob> queryScheduleJobs();

    TaskJob getByJobGroupName(String jobGroup, String jobName);

    TaskJob insert(TaskJob taskJob);

    TaskJob update(Long id, TaskJob taskJob);

    List<TaskJobLog> queryLog(TaskJobLog query, Integer pageSize, Integer pageNum);

    TaskJob triggerNow(Long id);

    TaskJob pauseJob(Long id);

    TaskJob removeJob(Long id);

    TaskJob pauseTrigger(String name, String group);

}

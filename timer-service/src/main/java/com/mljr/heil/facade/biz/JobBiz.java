package com.mljr.heil.facade.biz;


import com.github.pagehelper.PageInfo;
import com.mljr.heil.entity.TaskJob;
import com.mljr.heil.entity.TaskJobLog;
import com.mljr.heil.entity.TriggerJob;
import com.mljr.heil.facade.JobFacade;
import org.quartz.SchedulerException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.mljr.heil.service.JobService;

import java.text.ParseException;
import java.util.List;
import java.util.Map;

/**
 * Author : LI-JIAN
 * Date   : 2017-05-16
 */
@Component
public class JobBiz implements JobFacade {
    private Logger log = LoggerFactory.getLogger(this.getClass());

    @Autowired
    JobService jobService;

    @Override
    public Map<String, String> queryAllBizJobClass() {
        return jobService.queryAllBizJobClass();
    }

    @Override
    public TaskJob rescheduleJob(TaskJob taskJob) {
        try {
            jobService.rescheduleJob(taskJob);
        } catch (Exception e) {
            //log.error("{}-{}", taskJob.getJobGroup(), taskJob.getJobName(), e);
        }
        return taskJob;
    }

    @Override
    public String checkCron(String cron) {
        try {
            jobService.checkCron(cron);
            return cron;
        } catch (ParseException e) {
            log.error("cron:{}", cron, e);
            return e.getMessage();
        }
    }

    @Override
    public TaskJob getById(Long id) {
        return jobService.getById(id);
    }

    @Override
    public List<TaskJob> queryAll() {
        return jobService.queryAll();
    }

    @Override
    public List<TriggerJob> queryScheduleJobs() {
        try {
            return jobService.queryScheduleJobs();
        } catch (SchedulerException e) {
            log.error("", e);
            return null;
        }
    }

    @Override
    public TaskJob getByJobGroupName(String jobGroup, String jobName) {
        return jobService.getByJobGroupName(jobGroup, jobName);
    }

    @Override
    public TaskJob insert(TaskJob taskJob) {
        try {
            jobService.insert(taskJob);
            return taskJob;
        } catch (ParseException e) {
            //log.error("{}-{}", taskJob.getJobGroup(), taskJob.getJobName(), e);
            return null;
        }
    }

    @Override
    public TaskJob update(Long id, TaskJob taskJob) {
        try {
            return jobService.update(id, taskJob);
        } catch (ParseException e) {
            //log.error("{}-{}", taskJob.getJobGroup(), taskJob.getJobName(), e);
            return null;
        }
    }

    @Override
    public List<TaskJobLog> queryLog(TaskJobLog query, Integer pageSize, Integer pageNum) {
        PageInfo<TaskJobLog> pageInfo = jobService.queryLog(query, pageNum, pageSize);
        return pageInfo == null ? null : pageInfo.getList();
    }

    @Override
    public TaskJob triggerNow(Long id) {
        try {
            return jobService.triggerNow(id);
        } catch (SchedulerException e) {
            log.error("id:{}", id, e);
            return null;
        } catch (ClassNotFoundException e) {
            log.error("id:{}", id, e);
            return null;
        }
    }

    @Override
    public TaskJob pauseJob(Long id) {
        try {
            return jobService.pauseJob(id);
        } catch (SchedulerException e) {
            log.error("id:{}", id, e);
            return null;
        }
    }

    @Override
    public TaskJob removeJob(Long id) {
        try {
            return jobService.removeJob(id);
        } catch (SchedulerException e) {
            log.error("id:{}", id, e);
            return null;
        }
    }

    @Override
    public TaskJob pauseTrigger(String name, String group) {
        try {
            return jobService.pauseTrigger(name, group);
        } catch (SchedulerException e) {
            //log.error("{}-{}", name, group, e);
            return null;
        }
    }
}

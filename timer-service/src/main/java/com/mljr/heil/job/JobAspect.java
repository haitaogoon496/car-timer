package com.mljr.heil.job;

import com.mljr.heil.entity.TaskJob;
import com.mljr.heil.entity.TaskJobLog;
import com.mljr.heil.service.JobService;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;

/**
 * Author : BlackShadowWalker
 * Date   : 2016-11-08
 */
@Component
@Aspect
public class JobAspect {

    @Autowired
    JobService jobService;

    @Pointcut("this(com.mljr.heil.job.TaskJobBiz)")
    private void job() {
    }

    @Around("job()")
    public Object jobInterceptor(ProceedingJoinPoint pjp) throws Throwable {
        TaskJob taskJob = (TaskJob) pjp.getArgs()[0];
        if (taskJob == null || taskJob.getId() == null) {
            return pjp.proceed();
        }
        TaskJobLog taskJobLog = new TaskJobLog();
        taskJobLog.setJobGroup(taskJob.getJobGroup());
        taskJobLog.setJobName(taskJob.getJobName());
        taskJobLog.setName(taskJob.getName());
        taskJobLog.setJobClass(taskJob.getJobClass());
        taskJobLog.setTags(taskJob.getTags());
        taskJobLog.setVersion(taskJob.getVersion());
        taskJobLog.setStartTime(new Timestamp(System.currentTimeMillis()));
        taskJobLog.setSuccess(false);
        long t1 = System.nanoTime();
        try {
            Object ret = pjp.proceed();
            taskJobLog.setSuccess(true);
            return ret;
        } catch (NullPointerException e) {
            taskJobLog.setSuccess(false);
            taskJobLog.setRemark("空指针");
            throw e;
        } catch (Exception e) {
            taskJobLog.setSuccess(false);
            String s = e.getMessage();//max 1024
            taskJobLog.setRemark(s.length() > 1023 ? s.substring(0, 1023) : s);
            throw e;
        } finally {
            taskJobLog.setTakesInMs((System.nanoTime() - t1) / 1000000);
            taskJobLog.setEndTime(new Timestamp(System.currentTimeMillis()));
            jobService.insertLog(taskJobLog);
        }
    }

}

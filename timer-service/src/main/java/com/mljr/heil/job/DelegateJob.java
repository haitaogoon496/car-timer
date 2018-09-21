package com.mljr.heil.job;

import com.mljr.heil.callback.LockExecuteCallback;
import com.github.blackshadowwalker.spring.distributelock.LockException;

import com.mljr.heil.entity.TaskJob;
import com.mljr.heil.constant.JobConstant;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;
import com.mljr.heil.service.SysConfService;

/**
 * Author : BlackShadowWalker
 * Date   : 2016-11-07
 */
public class DelegateJob implements Job {
    private Logger log = LoggerFactory.getLogger(this.getClass());

    @Transactional
    @Override
    public void execute(final JobExecutionContext context) throws JobExecutionException {
        final String key = context.getTrigger().getKey().toString();
        TaskJobBiz taskJobBiz = (TaskJobBiz) context.getJobDetail().getJobDataMap().get(JobConstant.JOB_BIZ);
        TaskJob taskJob = (TaskJob) context.getJobDetail().getJobDataMap().get(JobConstant.JOB_CONFIG);
        SysConfService sysConfService = (SysConfService) context.getJobDetail().getJobDataMap().get(JobConstant.JOB_SYS_CONF_SERVICE);
        log.info("taskJob->{}执行了,任务名称->{}",taskJob.getName(),Thread.currentThread().getName());
        try {
            long timeout = 3;//获取锁10秒超时
            long expire = 120;//锁有效期120秒
            sysConfService.lockExecute("JOB:" + key, timeout, expire, false, new LockExecuteCallback() {
                @Override
                public void execute() {
                    taskJobBiz.doJob(taskJob);
                    try {
                        Thread.sleep(3);
                    } catch (InterruptedException e) {
                        log.error(key, e);
                    }
                }
            });
        } catch (LockException e) {
            log.info("{} {}", taskJob.getJobKey(), e.getMessage());
        } catch (Exception e) {
            log.error(key, e);
        }

    }

}

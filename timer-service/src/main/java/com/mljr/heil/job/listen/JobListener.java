package com.mljr.heil.job.listen;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.JobKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * Author : BlackShadowWalker
 * Date   : 2016-11-04
 */
@Component
public class JobListener implements org.quartz.JobListener{
    private Logger log = LoggerFactory.getLogger(this.getClass());

    @Override
    public String getName() {
        return "全局监听";
    }

    @Override
    public void jobToBeExecuted(JobExecutionContext context) {
        JobKey jobKey= context.getJobDetail().getKey();
        log.info("将要执行任务: {} With Trigger:{}", jobKey, context.getTrigger().getKey());
    }

    @Override
    public void jobExecutionVetoed(JobExecutionContext context) {
        log.info("Vetoed: {}", context.getJobDetail());
    }

    @Override
    public void jobWasExecuted(JobExecutionContext context, JobExecutionException jobException) {
        if (jobException == null ) {
            log.info("任务执行完成：{} With Trigger:{}", context.getJobDetail().getKey(), context.getTrigger().getKey());
        } else {
            log.info("任务执行异常：{} With Trigger:{} ERROR:{}", context.getJobDetail().getKey(), context.getTrigger().getKey(), jobException.getMessage());
        }
    }
}

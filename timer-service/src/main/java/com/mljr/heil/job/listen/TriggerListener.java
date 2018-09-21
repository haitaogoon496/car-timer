package com.mljr.heil.job.listen;

import org.quartz.JobExecutionContext;
import org.quartz.Trigger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * Author : BlackShadowWalker
 * Date   : 2016-11-04
 */
@Component
public class TriggerListener implements org.quartz.TriggerListener {
    private Logger log = LoggerFactory.getLogger(this.getClass());

    @Override
    public String getName() {
        return "全局监听";
    }

    @Override
    public void triggerFired(Trigger trigger, JobExecutionContext context) {
        log.debug("触发：{} - {}", trigger.getKey(), trigger.getDescription());
    }

    @Override
    public boolean vetoJobExecution(Trigger trigger, JobExecutionContext context) {
        return false;
    }

    @Override
    public void triggerMisfired(Trigger trigger) {
        log.info("{}-{}", trigger.getJobKey(), trigger.getDescription());
    }

    @Override
    public void triggerComplete(Trigger trigger, JobExecutionContext context, Trigger.CompletedExecutionInstruction triggerInstructionCode) {
        log.debug("触发器执行完成： {}", trigger.getKey());
    }
}

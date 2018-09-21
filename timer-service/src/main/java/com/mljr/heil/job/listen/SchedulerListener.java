package com.mljr.heil.job.listen;

import org.quartz.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * Author : BlackShadowWalker
 * Date   : 2016-11-04
 */
@Component
public class SchedulerListener implements org.quartz.SchedulerListener {
    private Logger log = LoggerFactory.getLogger(this.getClass());

    @Override
    public void jobScheduled(Trigger trigger) {
        log.info("{}", trigger.getJobKey());
    }

    @Override
    public void jobUnscheduled(TriggerKey triggerKey) {

    }

    @Override
    public void triggerFinalized(Trigger trigger) {

    }

    @Override
    public void triggerPaused(TriggerKey triggerKey) {

    }

    @Override
    public void triggersPaused(String triggerGroup) {

    }

    @Override
    public void triggerResumed(TriggerKey triggerKey) {

    }

    @Override
    public void triggersResumed(String triggerGroup) {

    }

    @Override
    public void jobAdded(JobDetail jobDetail) {
        log.info("添加JOB: {}", jobDetail.getKey());
    }

    @Override
    public void jobDeleted(JobKey jobKey) {
        log.info("删除JOB: {}", jobKey);
    }

    @Override
    public void jobPaused(JobKey jobKey) {
        log.info("暂停JOB: {}", jobKey);
    }

    @Override
    public void jobsPaused(String jobGroup) {
        log.info("暂停JOB组: {}", jobGroup);
    }

    @Override
    public void jobResumed(JobKey jobKey) {
        log.info("恢复JOB: {}", jobKey);
    }

    @Override
    public void jobsResumed(String jobGroup) {
        log.info("恢复JOB组: {}", jobGroup);
    }

    @Override
    public void schedulerError(String msg, SchedulerException cause) {
        log.error(msg, cause.getCause());
    }

    @Override
    public void schedulerInStandbyMode() {

    }

    @Override
    public void schedulerStarted() {
        log.info("schedulerStarted");
    }

    @Override
    public void schedulerStarting() {
        log.info("schedulerStarting...");
    }

    @Override
    public void schedulerShutdown() {
        log.info("schedulerShutdown");
    }

    @Override
    public void schedulerShuttingdown() {
        log.info("schedulerShuttingdown...");
    }

    @Override
    public void schedulingDataCleared() {
        log.info("schedulingDataCleared");
    }
}

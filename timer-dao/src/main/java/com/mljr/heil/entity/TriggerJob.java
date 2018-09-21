package com.mljr.heil.entity;

import lombok.Data;

import java.util.Date;

/**
 * @description:
 * @Date : 2018/7/5$ 16:55$
 * @Author : liht
 */
@Data
public class TriggerJob {
    Long jobId;
    String jobGroup;
    String jobName;
    String name;
    Date startTime;
    Date endTime;
    Date previousFireTime;
    Date nextFireTime;
    Date finalFireTime;
    String description;
    Boolean mayFireAgain;
    Integer misfireInstruction;
}

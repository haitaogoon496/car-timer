package com.mljr.heil.entity;

import lombok.Data;

import java.util.Date;

@Data
public class TaskJob {
    private Long id;

    private String jobGroup;

    private String jobName;

    private String name;

    private String cron;

    private Integer version;

    private String jobClass;

    private String tags;

    private String desp;

    private String jsonData;

    private Integer status;

    private Date createTime;

    public String getJobKey(){
        return this.jobGroup + "." + this.jobName;
    }
}

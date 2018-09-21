package com.mljr.heil.entity;

import lombok.Data;

import java.util.Date;

@Data
public class TaskJobLog {
    private Long id;

    private String jobGroup;

    private String jobName;

    private String name;

    private Date startTime;

    private Date endTime;

    private Long takesInMs;

    private Boolean success;

    private Integer version;

    private String jobClass;

    private String tags;

    private String remark;


}
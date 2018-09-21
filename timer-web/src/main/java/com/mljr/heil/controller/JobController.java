//package com.mljr.heil.controller;
//
//import com.mljr.heil.entity.TaskJob;
//import com.mljr.heil.entity.TaskJobLog;
//import com.mljr.heil.entity.TriggerJob;
//import com.mljr.heil.dto.ResponseDTO;
//import com.mljr.heil.facade.JobFacade;
//import io.swagger.annotations.Api;
//import io.swagger.annotations.ApiOperation;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Controller;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.List;
//import java.util.Map;
//
///**
// * Author : BlackShadowWalker
// * Date   : 2016-11-08
// */
//@Api(description = "JOB", tags = "MS")
//@Controller
//@RequestMapping("/job")
//public class JobController {
//
//    @Autowired
//    JobFacade jobFacade;
//
//    @ApiOperation("读取所有Job配置")
//    @ResponseBody
//    @RequestMapping(value = "", method = RequestMethod.GET)
//    public ResponseDTO<List<TaskJob>> queryAll() {
//        return new ResponseDTO<>(jobFacade.queryAll());
//    }
//
//    @ApiOperation("读取所有的jobClass")
//    @ResponseBody
//    @RequestMapping(value = "/jobClasses", method = RequestMethod.GET)
//    public ResponseDTO<Map<String, String>> queryAllBizJobClass() {
//        return new ResponseDTO<>(jobFacade.queryAllBizJobClass());
//    }
//
//    @ApiOperation("读取所有当前处于调度态Job")
//    @ResponseBody
//    @RequestMapping(value = "/scheduleJobs", method = RequestMethod.GET)
//    public ResponseDTO<List<TriggerJob>> queryScheduleJobs() throws Exception {
//        return new ResponseDTO<>(jobFacade.queryScheduleJobs());
//    }
//
//    @ApiOperation("新增Job")
//    @ResponseBody
//    @RequestMapping(value = "", method = RequestMethod.POST)
//    public ResponseDTO<TaskJob> newTaskJob(TaskJob taskJob) throws Exception {
//        TaskJob resp = jobFacade.insert(taskJob);
//        jobFacade.rescheduleJob(taskJob);
//        return new ResponseDTO<>(resp);
//    }
//
//    @ApiOperation("修改Job")
//    @ResponseBody
//    @RequestMapping(value = "", method = RequestMethod.PUT)
//    public ResponseDTO<TaskJob> updateTaskJob(Long taskJobId, TaskJob taskJob) throws Exception {
//        TaskJob resp = jobFacade.update(taskJobId, taskJob);
//        jobFacade.rescheduleJob(resp);
//        return new ResponseDTO<>(resp);
//    }
//
//    @ApiOperation("立即执行JOB")
//    @ResponseBody
//    @RequestMapping(value = "/triggerNow", method = RequestMethod.POST)
//    public ResponseDTO<TaskJob> triggerNow(Long taskJobId) throws Exception {
//        return new ResponseDTO<>(jobFacade.triggerNow(taskJobId));
//    }
//
//    @ApiOperation("执行JOB记录")
//    @ResponseBody
//    @RequestMapping(value = "/jobLog", method = RequestMethod.GET)
//    public ResponseDTO<List<TaskJobLog>> jobLog(
//            @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
//            @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize,
//            @ModelAttribute TaskJobLog query) throws Exception {
//        return new ResponseDTO<>(jobFacade.queryLog(query, pageSize, pageNum));
//    }
//
//    @ApiOperation("暂停JOB")
//    @ResponseBody
//    @RequestMapping(value = "/pause", method = RequestMethod.GET)
//    public ResponseDTO<TaskJob> pauseJob(Long taskJobId) throws Exception {
//        return new ResponseDTO<>(jobFacade.pauseJob(taskJobId));
//    }
//
//    @ApiOperation("暂停JOB")
//    @ResponseBody
//    @RequestMapping(value = "/pause/trigger", method = RequestMethod.GET)
//    public ResponseDTO<TaskJob> pauseTrigger(String triggerName, String triggerGroup) throws Exception {
//        return new ResponseDTO<>(jobFacade.pauseTrigger(triggerName, triggerGroup));
//    }
//
//}

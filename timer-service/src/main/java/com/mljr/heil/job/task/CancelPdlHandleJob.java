package com.mljr.heil.job.task;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import com.mljr.heil.entity.TaskJob;
import com.mljr.heil.enums.JobStatus;
import com.mljr.heil.job.TaskJobBiz;
import com.mljr.heil.service.JobService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 *
 * @ClassName CancelPdlHandleJob
 * @Description 进件撤销30天处理
 * @author lihaitao
 * @Date 2017年8月11日 下午3:53:33
 * @version 1.0.0
 */
@Component
public class CancelPdlHandleJob implements TaskJobBiz {
    private Logger log = LoggerFactory.getLogger(this.getClass());

    @Autowired
    JobService jobService;


    @Override
    public void doJob(TaskJob taskJob) {
        log.info("进件撤销30天定时任务开始执行。。。。。。。。。。。。。");
        try {
        	System.out.println("定时任务测试--------------------每1分钟执行一次！！！！！！！！！！！");
		} catch (Exception e) {
			log.error("进件撤销30天定时处理异常！");
			log.error(e.getMessage(), e);
		}
    }

    @PostConstruct
    public void init() throws Exception {
        String jobGroup = "car-heil-test";
        String name = "timer测试";
        String jobName = this.getClass().getSimpleName();
        if (jobService.getByJobGroupName(jobGroup, jobName) == null) {
            TaskJob job = new TaskJob();
            job.setJobGroup(jobGroup);
            job.setJobName(jobName);
            job.setName(name);
            job.setJobClass(this.getClass().getName());
            job.setCron("0/1 * * * * ?");
            job.setStatus(JobStatus.START.CODE);
            jobService.insert(job);
        }
    }

}

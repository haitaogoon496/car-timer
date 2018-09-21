package com.mljr.heil.service.impl;

import com.alibaba.fastjson.JSON;
import com.mljr.heil.bean.Code;
import com.github.pagehelper.PageInfo;

import com.mljr.heil.entity.TaskJob;
import com.mljr.heil.entity.TaskJobLog;
import com.mljr.heil.entity.TriggerJob;
import com.mljr.heil.mapper.TaskJobMapper;
import com.mljr.heil.service.JobService;
import com.mljr.heil.config.AppConfig;
import com.mljr.heil.constant.JobConstant;
import com.mljr.heil.enums.JobStatus;
import com.mljr.heil.exp.AlertException;
import com.mljr.heil.job.DelegateJob;
import com.mljr.heil.job.TaskJobBiz;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.session.RowBounds;
import org.quartz.*;
import org.quartz.impl.matchers.GroupMatcher;
import org.quartz.spi.OperableTrigger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.framework.AopProxyUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Lazy;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.mljr.heil.service.SysConfService;
import com.mljr.heil.util.ValidateUtils;

import javax.annotation.PostConstruct;
import java.net.InetAddress;
import java.text.ParseException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Author : BlackShadowWalker
 * Date   : 2016-11-07
 * 定时任务处理
 * 1. 完成单实例任务通过数据库配置
 * 2. 完成多实例(quartz集群)通过数据库配置分发
 */
@Service
@Lazy
public class JobServiceImpl implements JobService {
    private Logger log = LoggerFactory.getLogger(this.getClass());

    @Autowired
    TaskJobMapper mapper;
    @Autowired
    ApplicationContext applicationContext;
    @Autowired
    Scheduler scheduler;
    @Autowired
    SysConfService sysConfService;

    @Value("${job.default.group:HR}")
    String defaultGroup;

    @Autowired
    AppConfig appConfig;

    boolean isStg;

    @PostConstruct
    public void init() throws Exception {
        log.info("JobServiceImpl---------------------------------->执行init方法");
        isStg = InetAddress.getLocalHost().getHostName().contains("stg");
        this.checkJobConfigInterval();
    }

    public Map<String, String> queryAllBizJobClass() {
        Map<String, TaskJobBiz> map = applicationContext.getBeansOfType(TaskJobBiz.class);
        if (map == null) {
            return null;
        }
        Map<String, String> classes = new HashMap<>();
        for (String beanName : map.keySet()) {
            TaskJobBiz biz = map.get(beanName);
            Class<?> targetClass = AopProxyUtils.ultimateTargetClass(biz);
            if (targetClass == null) {
                targetClass = biz.getClass();
            }
            classes.put(beanName, targetClass.getName());
        }
        return classes;
    }

    /**
     * 要执行的任务 task
     * @param taskJob
     * @return
     * @throws SchedulerException
     * @throws ClassNotFoundException
     */
    private JobDetail buildJobDetail(TaskJob taskJob) throws SchedulerException, ClassNotFoundException {
        String jobGroup = taskJob.getJobGroup();
        String jobName = taskJob.getJobName();
        JobKey jobKey = JobKey.jobKey(jobName, jobGroup);
        if (StringUtils.isEmpty(taskJob.getJobClass())) {
            log.warn("jobClass is Empty of id : {}", taskJob.getId());
            return null;
        }
        Class<TaskJobBiz> jobClass = (Class<TaskJobBiz>) Class.forName(taskJob.getJobClass());

        String[] names = applicationContext.getBeanNamesForType(jobClass);
        if (names == null || names.length != 1) {
            // 1个jobClass对应是一个确切的类型，不能是父类
            throw new SchedulerException("taskJob bean of type " + taskJob.getJobClass() + " has more than one names " + Arrays.toString(names));
        }
        String beanId = names[0];
        TaskJobBiz biz = (TaskJobBiz) applicationContext.getBean(beanId);

        JobDataMap dataMap = new JobDataMap();
        dataMap.put(JobConstant.JOB_SERVICE, this);
        dataMap.put(JobConstant.JOB_BIZ, biz);
        dataMap.put(JobConstant.JOB_CONFIG, taskJob);
        dataMap.put(JobConstant.JOB_SYS_CONF_SERVICE, sysConfService);
        return JobBuilder.newJob().withIdentity(jobKey)
                .ofType(DelegateJob.class)
                .withDescription(taskJob.getDesp())
                .setJobData(dataMap)
                .build();
    }

    /**
     * 绑定任务和trigger的关系，trigger是个定时器 ,负责设置调度策略 例如，每隔多长时间执行你的任务
     * @param taskJob
     * @throws SchedulerException
     * @throws ClassNotFoundException
     */
    public void rescheduleJob(TaskJob taskJob) throws SchedulerException, ClassNotFoundException {
        JobKey jobKey = JobKey.jobKey(taskJob.getJobName(), taskJob.getJobGroup());
        TriggerKey triggerKey = TriggerKey.triggerKey(taskJob.getJobName() + "-" + taskJob.getId(), taskJob.getJobGroup());

        JobDetail jobDetail = scheduler.getJobDetail(jobKey);
        if (jobDetail != null) {
            scheduler.deleteJob(jobKey);
        }
        if (taskJob.getStatus() != JobStatus.START.CODE) {
            return;
        }
        jobDetail = buildJobDetail(taskJob);

        Trigger trigger = TriggerBuilder.newTrigger()
                .withIdentity(triggerKey)
                .forJob(taskJob.getJobName(), taskJob.getJobGroup())
                .withDescription(taskJob.getCron())
                .withSchedule(CronScheduleBuilder.cronSchedule(taskJob.getCron()))
                .build();
        OperableTrigger trig = (OperableTrigger) trigger;
        Date ft = trig.computeFirstFireTime(null);
        if (ft != null) {
            scheduler.scheduleJob(jobDetail, trigger);
        }
    }

    public void checkCron(String cron) throws ParseException {
        new CronExpression(cron);
    }

    public TaskJob getById(Long id) {
        return mapper.getById(id);
    }

    @Override
    public List<TaskJob> queryAll() {
        return mapper.queryAll();
    }

    @Override
    public List<TriggerJob> queryScheduleJobs() throws SchedulerException {
        List<TriggerJob> triggerJobList = new ArrayList<>();
        for (JobKey key : scheduler.getJobKeys(GroupMatcher.anyGroup())) {
            JobDetail jobDetail = scheduler.getJobDetail(key);
            TaskJob taskJob = (TaskJob) jobDetail.getJobDataMap().get(JobConstant.JOB_CONFIG);
            for (Trigger trigger : scheduler.getTriggersOfJob(key)) {
                TriggerJob triggerJob = new TriggerJob();
                triggerJob.setJobId(taskJob.getId());
                triggerJob.setJobGroup(taskJob.getJobGroup());
                triggerJob.setJobName(taskJob.getJobName());
                triggerJob.setName(taskJob.getName());

                triggerJob.setMisfireInstruction(trigger.getMisfireInstruction());
                triggerJob.setDescription(trigger.getDescription());
                triggerJob.setStartTime(trigger.getStartTime());
                triggerJob.setEndTime(trigger.getEndTime());
                triggerJob.setFinalFireTime(trigger.getFinalFireTime());
                triggerJob.setPreviousFireTime(trigger.getPreviousFireTime());
                triggerJob.setNextFireTime(trigger.getNextFireTime());
                triggerJob.setMayFireAgain(trigger.mayFireAgain());
                triggerJobList.add(triggerJob);
            }
        }
        return triggerJobList;
    }

    public TaskJob getByJobGroupName(String jobGroup, String jobName) {
        return mapper.getByJobGroupName(jobGroup, jobName);
    }
    @Transactional
    @Lazy
    public int insert(TaskJob taskJob) throws ParseException {
        if (StringUtils.isEmpty(taskJob.getJobGroup())) {
            taskJob.setJobGroup(defaultGroup);
        }
        ValidateUtils.notTrimEmptyParam(taskJob.getJobName(), "jobName");
        ValidateUtils.notTrimEmptyParam(taskJob.getCron(), "cron");
        if (mapper.existJob(taskJob.getJobGroup(), taskJob.getJobName())) {
            throw new AlertException("JOB已存在");
        }
        if (taskJob.getStatus() == null) {
            taskJob.setStatus(JobStatus.START.CODE);
        }
        taskJob.setVersion(1);
        taskJob.setCreateTime(new Date());
        if (!StringUtils.isEmpty(taskJob.getCron())) {
            this.checkCron(taskJob.getCron());
        }
        return mapper.insert(taskJob);
    }

    @Transactional
    public TaskJob update(Long id, TaskJob taskJob) throws ParseException {
        TaskJob check = mapper.getById(id);
        ValidateUtils.notNull(check, Code.E_400, "未找到任务");
        taskJob.setId(id);
        if (!StringUtils.isEmpty(taskJob.getCron())) {
            this.checkCron(taskJob.getCron());
        }
        taskJob.setVersion(check.getVersion() + 1);
        mapper.update(taskJob);
        return mapper.getById(id);
    }

    @Transactional
    @Override
    public int insertLog(TaskJobLog log) {
        return mapper.insertLog(log);
    }

    @Transactional
    @Override
    public int updateLog(TaskJobLog log) {
        return mapper.updateLog(log);
    }

    @Override
    public PageInfo<TaskJobLog> queryLog(TaskJobLog query, Integer pageNum, Integer pageSize) {
        List<TaskJobLog> list = mapper.queryLog(query, new RowBounds(pageNum, pageSize));
        return new PageInfo<>(list);
    }

    @Override
    public TaskJob triggerNow(Long id) throws SchedulerException, ClassNotFoundException {
        TaskJob taskJob = mapper.getById(id);
        if (taskJob == null) {
            throw new AlertException("JOB不存在");
        }
        JobKey jobKey = JobKey.jobKey(taskJob.getJobName(), taskJob.getJobGroup());
        String triName = taskJob.getJobName() + "-" + UUID.randomUUID().toString().substring(0, 7);
        TriggerKey triggerKey = TriggerKey.triggerKey(triName, taskJob.getJobGroup());
        JobDetail jobDetail = scheduler.getJobDetail(jobKey);

        TriggerBuilder trigger = TriggerBuilder.newTrigger()
                .withIdentity(triggerKey)
                .withDescription("triggerNow");

        if (jobDetail == null) {
            jobDetail = buildJobDetail(taskJob);
            scheduler.scheduleJob(jobDetail, trigger.build());
        } else {
            trigger.forJob(jobDetail);
            scheduler.scheduleJob(trigger.build());
        }
        log.info("trigger[{}] Job:{}", triName, taskJob.getName());
        taskJob.setJobName(triggerKey.getName());
        taskJob.setJobGroup(triggerKey.getGroup());
        return taskJob;
    }

    @Override
    public TaskJob pauseJob(Long id) throws SchedulerException {
        TaskJob taskJob = mapper.getById(id);
        if (taskJob == null) {
            throw new AlertException("JOB不存在");
        }
        JobKey jobKey = JobKey.jobKey(taskJob.getJobName(), taskJob.getJobGroup());
        if (scheduler.checkExists(jobKey)) {
            scheduler.pauseJob(jobKey);
        }
        return taskJob;
    }

    @Override
    public TaskJob removeJob(Long id) throws SchedulerException {
        TaskJob taskJob = mapper.getById(id);
        if (taskJob == null) {
            throw new AlertException("JOB不存在");
        }
        JobKey jobKey = JobKey.jobKey(taskJob.getJobName(), taskJob.getJobGroup());
        if (scheduler.checkExists(jobKey)) {
            scheduler.deleteJob(jobKey);
        }
        return taskJob;
    }

    @Override
    public TaskJob pauseTrigger(String name, String group) throws SchedulerException {
        if (StringUtils.isEmpty(group)) {
            group = this.defaultGroup;
        }
        TriggerKey triggerKey = TriggerKey.triggerKey(name, group);
        Trigger trigger = scheduler.getTrigger(triggerKey);
        if (trigger != null) {
            TaskJob taskJob = (TaskJob) trigger.getJobDataMap().get(JobConstant.JOB_CONFIG);
            scheduler.pauseTrigger(triggerKey);
            return taskJob;
        }
        return null;
    }

    //定期更新数据库cron配置
    @Scheduled(cron = "0 0/2 * * * ?")
    public void checkJobConfigInterval() throws Exception {
        log.info("Scheduled是一个调度器 - 具体信息----------------------{}:", scheduler.getContext().getKeys());
        if (isStg || "testCase".equalsIgnoreCase(appConfig.getEnvName())) {
            return;//stg或单元测试则忽略加载
        }
        List<TaskJob> taskJobList = mapper.queryAll();
        if (taskJobList == null) {
            return;
        }
        for (TaskJob item : taskJobList) {
            try {
                JobDetail jobDetail = scheduler.getJobDetail(JobKey.jobKey(item.getJobName(), item.getJobGroup()));
                if (jobDetail == null) {
                    log.info("jobDetail为空，直接执行该job，jobName:{}", JSON.toJSON(jobDetail));
                    this.rescheduleJob(item);
                    continue;
                }
                TaskJob taskJob = (TaskJob) jobDetail.getJobDataMap().get(JobConstant.JOB_CONFIG);
                if (taskJob.getVersion() < item.getVersion()) {
                    log.info("taskJob版本更新了，要重新执行，taskJob:{}", JSON.toJSON(taskJob));
                    this.rescheduleJob(item);
                }
            } catch (SchedulerException e) {
                log.error("Error: {}",e.getMessage(), e);
            } catch (ClassNotFoundException e) {
                log.error("找不到Job类(非生产环境可忽略), {} Error: {}", item.getJobKey(), e.getMessage());
            }
        }
        Set<JobKey> existJobKey = taskJobList.stream().map(item -> JobKey.jobKey(item.getJobName(), item.getJobGroup())).collect(Collectors.toSet());

        for (JobKey key : scheduler.getJobKeys(GroupMatcher.anyGroup())) {
            try {
                if (existJobKey.contains(key)) {
                    continue;
                }
                scheduler.deleteJob(key);
                log.info("移除任务-------------------------------: {}", key);
            } catch (SchedulerException e) {
                log.error("Scheduler {} Error: {}", key, e.getMessage());
            }
        }
    }
}

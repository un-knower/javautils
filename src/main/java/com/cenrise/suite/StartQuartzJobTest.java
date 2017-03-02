package com.cenrise.suite;

import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;

/**
 * 调度设计
 */
public class StartQuartzJobTest {

    public static void main(String args[]) {
        StartQuartzJobTest quartzTest = new StartQuartzJobTest();
        try {
            StartQuartzJobTest.testQuartz();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void testQuartz() throws Exception {
        SchedulerFactory schedFact = new StdSchedulerFactory();
        Scheduler sched = schedFact.getScheduler();
        sched.start();
        JobDetail job = JobBuilder.newJob(StartQuartzJob.class)
                .withIdentity("myJob", "group1")
                .usingJobData("jobSays", "hello world!")
                .usingJobData("myFloatValue", 3.141f).build();
        Trigger trigger = TriggerBuilder
                .newTrigger()
                .withIdentity("myTrigger", "group1")
                .startNow()
                .withSchedule(CronScheduleBuilder.cronSchedule("0/2 * * * * ?"))
                .build();
        sched.scheduleJob(job, trigger);
    }
}

package com.cenrise.suite;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

/**
 * 调度类
 */
public class StartQuartzJob implements Job {

    /**
     * 调度执行类
     *
     * @param context
     * @throws JobExecutionException
     */
    public void execute(JobExecutionContext context) throws JobExecutionException {
        System.out.println("test quartz");
    }
}

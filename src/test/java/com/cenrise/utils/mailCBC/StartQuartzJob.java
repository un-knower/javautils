package com.cenrise.utils.mailCBC;

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
        //发送邮件
        CBCMailFetch cbcMailFetch = new CBCMailFetch();
        try {
            cbcMailFetch.fetchMail();
        } catch (Exception e) {
            e.printStackTrace();
        }
        for (String str : cbcMailFetch.getSpecifiedDate()) {
            System.out.println(str);
        }
        ;

    }
}

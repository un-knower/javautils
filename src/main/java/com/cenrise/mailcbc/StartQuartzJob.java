package com.cenrise.mailcbc;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import java.util.Date;

/**
 * 调度类
 *
 * @author dongpo.jia
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
//            cbcMailFetch.fetchMail();
            System.out.println("test1.........."+new Date());
        } catch (Exception e) {
            e.printStackTrace();
        }
//        for (String str : cbcMailFetch.getSpecifiedDate()) {
//            System.out.println(str);
//        }
        ;

    }
}

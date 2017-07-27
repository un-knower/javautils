package com.cenrise.utils.mailCBC.mail163;


import javax.mail.internet.AddressException;

/**
 * 测试发送类，作者实体测试时使用的是网易中的个性化域名邮箱@blog-china.cn，并成功的发送邮件：
 */
public class TestSendEmail { // 163邮箱

    public static void main(String[] args) {

        // 这个类主要是设置邮件
        MailSenderInfo mailInfo = new MailSenderInfo();
        mailInfo.setMailServerHost("smtp.ym.163.com");   //邮箱服务器名，不要改变
        mailInfo.setMailServerPort("25");    //端口，不是 ssl发送邮件，就不要该这个 端口
        mailInfo.setValidate(true);
        mailInfo.setUserName("***@blog-china.cn"); // 实际发送者
        mailInfo.setPassword("****");// 您的邮箱密码
        mailInfo.setFromAddress("****@blog-china.cn"); // 设置发送人邮箱地址
        mailInfo.setToAddress("****@qq.com"); // 设置接受者邮箱地址
        mailInfo.setSubject("系统邮件-找回密码");
        mailInfo.setContent("<h4>您的 邮箱密码为:</h4>");
        // 这个类主要来发送邮件
        SimpleMailSender sms = new SimpleMailSender();
        try {
            sms.sendTextMail(mailInfo); // 发送文体格式
        } catch (AddressException e) {
            e.printStackTrace();
        }
        sms.sendHtmlMail(mailInfo); // 发送html格式

    }


}
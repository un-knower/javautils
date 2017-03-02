package com.cenrise.utils.mailCBC;

import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class Sender {
    private String receiver = "hui.zz@163.com";
    private String subject = "Hello! My Friend! Sending best wishes!";
    private String cc = "hui.zz@hytc.edu.cn"; // (Blind) Carbon Copy
    private String mailContent = "Hello! Frodo! peril is approaching! go to Minas Tirith now@!";
    private Session session;// session 没有子类，可以被共享,来自javax.mail包
    private Message msg;// 放内容，实现接口part，有子类MimeMessage，来自javax.mail

    public void sendNow() {
        Properties props = new Properties();// dictionary-hashtable-properties
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.host", "smtp.163.com");// 没有开外网，程序就跑不动了
        // 呵呵运行这程序的时候必须有smtp server
        session = Session.getDefaultInstance(props, new Authenticator() {
            public PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication("courses4public", "hytczzh");
            }
        });
        session.setDebug(true); // 允许调试，因此可以用getDebug 取调试信息,消息多得吓人。哼哼
        try {
            msg = new MimeMessage(session);
            msg.setFrom(new InternetAddress("courses4public@163.com"));
            InternetAddress toAddress = new InternetAddress(receiver);// 收件人
            msg.addRecipient(Message.RecipientType.TO, toAddress);// 加收件人
            InternetAddress ccAddress = new InternetAddress(cc);// 收件人
            msg.addRecipient(Message.RecipientType.CC, ccAddress);// 加收件人
            msg.setSubject(subject);
            msg.setText(mailContent);
            Transport.send(msg);
        } catch (MessagingException ex) {
            while ((ex = (MessagingException) ex.getNextException()) != null)
                ex.printStackTrace();
        }
    } // sendNow end

    public static void main(String[] args) {
        new Sender().sendNow();
    }
}

package com.cenrise.utils.mailCBC;

import javax.mail.*;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Date;
import java.util.Properties;

/**
 * 测试发送163邮箱
 */
public class Sender163Test {
    private String receiver = "jiadp@cenrise.com";
    private String subject = "Hello,Test! Best Wishes!" + new Date();
    private String cc = "295445156@qq.com";
    private String mailContent = "Hello,popo! Coming" + new Date();
    private Session session;//没有子类,可以被共享,来自javax.mail
    private Message msg;//放内容,实现接口part,有子类MimeMessage,来自javax.mail

    public void sendNow() {
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.host", "smtp.suixingpay.com");//没有开外网,程序就跑不动了,163邮箱

        session = Session.getDefaultInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                //TODO 密码需要填写明文 PasswordAuthentication是一个包装类,里面包含了用户名和密码
                return new PasswordAuthentication("dc_mail@suixingpay.com", "datamail123");
            }
        });
        session.setDebug(true);
        try {
            msg = new MimeMessage(session);
            msg.setFrom(new InternetAddress("dc_mail@suixingpay.com"));
            //
            InternetAddress toAddress = new InternetAddress(receiver);//收件人
            msg.addRecipient(Message.RecipientType.TO, toAddress);//加收件人
            InternetAddress ccAddress = new InternetAddress(cc);//收件人
            msg.addRecipient(Message.RecipientType.CC, ccAddress);//加收件人
            msg.setSubject(subject);
            msg.setText(mailContent);
            Transport.send(msg);

        } catch (AddressException e) {
            e.printStackTrace();
        } catch (MessagingException e) {
            while ((e = (MessagingException) e.getNextException()) != null)
                e.printStackTrace();
        }//senNow end

    }

    public static void main(String[] args) {
        new Sender163Test().sendNow();
    }

}

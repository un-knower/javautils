package com.cenrise.utils.mailCBC;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

/**
 * 发送消息步骤
 * （1把应该导入的javax.mail java.util和java.io导入。
 * （2）创建一个Properties对象，存放邮件服务器的信息。
 * （3） 给 Propertie对象赋值。
 * （4） 生成session对象。
 * （5）从这个 session中创建一条Message。
 * （6）给这条Message设定 From TO CC BCC 和Subject。
 * （7）给邮件正文设定内容。
 * （8）用Transport.send()发送邮件。
 */
public class 发送消息 {
    public void test() throws AddressException, MessagingException {
        String host = "smtp.163.com";
        String from = "courses4public@163.com";
        String to = "courses4public@163.com";
        //Get system properties
        Properties props = System.getProperties();
        //Setup mail server
        props.put("mail.smtp.host", host);
        props.put("mail.smtp.auth", "true");
        //Get session
        Session session = Session.getDefaultInstance(props, null);
        session.setDebug(true);
        //Define message
        MimeMessage message = new MimeMessage(session);
        message.setFrom(new InternetAddress(from));
        message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
        message.setSubject("Hello JavaMail");
        message.setText("Welcome to JavaMail");
        message.saveChanges();
        //Sedn message
        Transport trans = session.getTransport("smtp");
        trans.connect(host, "courses4public", "hytczzh");
        trans.sendMessage(message, message.getAllRecipients());
        trans.close();
    }
}

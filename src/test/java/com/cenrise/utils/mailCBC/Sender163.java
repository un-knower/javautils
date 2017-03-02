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
public class Sender163 {
    private String receiver = "jiadp@cenrise.com";
    private String subject = "Hello,Test! Best Wishes!" + new Date();
    private String cc = "295445156@qq.com";
    private String mailContent = "Hello,popo! Coming" + new Date();
    private Session session;//没有子类,可以被共享,来自javax.mail
    private Message msg;//放内容,实现接口part,有子类MimeMessage,来自javax.mail

    public void sendNow() {
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        //163服务器地址: POP3服务器: pop.163.com SMTP服务器: smtp.163.com IMAP服务器: imap.163.com
        props.put("mail.smtp.host", "smtp.163.com");//没有开外网,程序就跑不动了,163邮箱
        //腾讯企业邮 接收服务器： imap.exmail.qq.com(使用SSL，端口号993) 发送服务器：  smtp.exmail.qq.com(使用SSL，端口号465)
        //props.put("mail.smtp.host", "imap.exmail.qq.com");//没有开外网,程序就跑不动了,腾讯企业邮

        //验证信息需要通过Session传给邮件服务器,其中Authenticator负责密码校验,如果不需要验证身份就用null或用单参数的getInstance()。
        session = Session.getDefaultInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                //PasswordAuthentication是一个包装类,里面包含了用户名和密码
                return new PasswordAuthentication("jiadp2012", "2012@beijing");
            }
        });
        session.setDebug(true);//允许调试,因此可以用getDebug()方法取调试信息,消息很多
        try {
            //创建了自己的Session对象后就可以发送消息了,这时需要用到Message消息类型。由于Message是一个抽象类,所以使用时必须使用一个具体的子类型。在大多数情况下,这个子类是javax.mail.internet.MimeMessage。一个MimeMessage是封装了MIME类型数据和报头的消息。消息的报头严格限制为只能使用US-ASCII字符,尽管非ASCII字符可以被编码到某些报头字段中。
            msg = new MimeMessage(session);
            msg.setFrom(new InternetAddress("jiadp2012@163.com"));
            //一旦创建了会话和消息,并为消息填充了内容,就需要用Address类为信件标上地址。同Message类一样,Address类也是抽象类。可以使用javax.mail.internet.InternetAddress类。
            //
            InternetAddress toAddress = new InternetAddress(receiver);//收件人
            msg.addRecipient(Message.RecipientType.TO, toAddress);//加收件人
            InternetAddress ccAddress = new InternetAddress(cc);//收件人
            msg.addRecipient(Message.RecipientType.CC, ccAddress);//加收件人
            msg.setSubject(subject);
            msg.setText(mailContent);
            //发送消息的最后一步操作是使用Transport类。该类使用特定协议(通常是SMTP语言来发送消息。他是一个抽象类,其操作与Session类有些相似。可以通过只调用表态的send()方法来使用该类的默认版本。
            Transport.send(msg);

        } catch (AddressException e) {
            e.printStackTrace();
        } catch (MessagingException e) {
            while ((e = (MessagingException) e.getNextException()) != null)
                e.printStackTrace();
        }//senNow end

    }

    public static void main(String[] args) {
        new Sender163().sendNow();
    }

}

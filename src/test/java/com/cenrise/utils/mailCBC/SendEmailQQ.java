package com.cenrise.utils.mailCBC;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.security.GeneralSecurityException;
import java.util.Date;
import java.util.Properties;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import com.sun.mail.util.MailSSLSocketFactory;

/**
 * 腾讯企业邮箱
 */
public class SendEmailQQ {
    private static String account;//登录用户名
    private static String pass; //登录密码
    private static String from; //发件地址
    private static String host; //服务器地址
    private static String port; //端口
    private static String protocol;//协议

    static {
        Properties prop = new Properties();
        InputStream instream = ClassLoader.getSystemResourceAsStream("emailqq.properties");
        try {
            prop.load(instream);
        } catch (IOException e) {
            System.out.println("加载属性文件失败");
        }
        account = prop.getProperty("e.account");
        pass = prop.getProperty("e.pass");
        from = prop.getProperty("e.from");
        host = prop.getProperty("e.host");
        port = prop.getProperty("e.port");
        protocol = prop.getProperty("e.protocol");
    }

    //用户名密码验证，需要实现抽象类Authenticator的抽象方法PasswordAuthentication
    //身份认证类javax.mail.Authenticator。大多数邮件系统为了防止邮件乱发,设定了smtp身份认证功能,所有在发送邮件时,经常需要提供用户名和密码。在Java程序中专门用下面的类来封装用户认证操作。
    static class MyAuthenricator extends Authenticator {
        String u = null;
        String p = null;

        public MyAuthenricator(String u, String p) {
            this.u = u;
            this.p = p;
        }

        @Override
        protected PasswordAuthentication getPasswordAuthentication() {
            return new PasswordAuthentication(u, p);
        }
    }


    private String to;    //收件人
    private String id;    //重置密码地址标识(这句话是我的业务需要，你们可以不要)

    public SendEmailQQ(String to, String id) {
        this.to = to;
        this.id = id;
    }

    public void send() {
        //javamail需要使用Properties创建一个session对象,它将寻找字符串mail.smtp.host属性的值,这就是发送邮件的主机地址
        Properties prop = new Properties();
        prop.setProperty("mail.transport.protocol", protocol);//协议
        prop.setProperty("mail.smtp.host", host);//服务器
        prop.setProperty("mail.smtp.port", port);//端口
        prop.setProperty("mail.smtp.auth", "true");//使用smtp身份验证
        //使用SSL，企业邮箱必需！开启安全协议
        MailSSLSocketFactory sf = null;
        try {
            sf = new MailSSLSocketFactory();
            sf.setTrustAllHosts(true);
        } catch (GeneralSecurityException e1) {
            e1.printStackTrace();
        }
        prop.put("mail.smtp.ssl.enable", "true");
        prop.put("mail.smtp.ssl.socketFactory", sf);

        //类Session定义了一个基本的邮件会话,同远程邮件系统服务器的交互应答过程。Session利用java.util.Properties对象获取诸如邮件服务器、用户名、密码等信息。
        //getDefaultInstance,getDefaultInstance返回一个共享的Session
        Session session = Session.getDefaultInstance(prop, new MyAuthenricator(account, pass));
        session.setDebug(true);
        MimeMessage mimeMessage = new MimeMessage(session);
        try {
            mimeMessage.setFrom(new InternetAddress(from, "XXX"));
            mimeMessage.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
            mimeMessage.setSubject("XXX账户密码重置");
            mimeMessage.setSentDate(new Date());
            mimeMessage.setText("您在XXX使用了密码重置功能，请点击下面链接重置密码:\n"
                    + "http://localhost:8080/XXX/ResetPassword?id="
                    + id);
            mimeMessage.saveChanges();
            Transport.send(mimeMessage);
        } catch (MessagingException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    public static void main(String args[]) {
        SendEmailQQ s = new SendEmailQQ("test@cenrise.com",
                "ZAQ!2wsx");
        s.send();
    }
}


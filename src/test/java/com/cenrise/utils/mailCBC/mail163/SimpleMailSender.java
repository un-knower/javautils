package com.cenrise.utils.mailCBC.mail163;


import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.internet.AddressException;
import javax.mail.internet.MimeMessage;
import java.util.Date;

/**
 * 邮件（可以携带附件的邮件）发送器
 *
 * @author guopengfei
 *         留在青山的 博客(https://www.blog-china.cn/liuzaiqingshan/home)
 */
public class SimpleMailSender {
    /**
     * 以文本格式发送邮件
     *
     * @param mailInfo 待发送的邮件的信息
     */
    public boolean sendTextMail(MailSenderInfo mailInfo) throws AddressException {
        // 判断是否需要身份认证
        MyAuthenticator authenticator = null;
        java.util.Properties pro = mailInfo.getProperties();
        if (mailInfo.isValidate()) {
            // 如果需要身份认证，则创建一个密码验证器
            authenticator = new MyAuthenticator(mailInfo.getUserName(),
                    mailInfo.getPassword());
        }
        // 根据邮件会话属性和密码验证器构造一个发送邮件的session
        javax.mail.Session sendMailSession = Session.getDefaultInstance(pro, authenticator);
        try {
            // 根据session创建一个邮件消息
            javax.mail.Message mailMessage = new MimeMessage(sendMailSession);
            // 创建邮件发送者地址
            javax.mail.Address from = new javax.mail.internet.InternetAddress(mailInfo.getFromAddress());
            // 设置邮件消息的发送者
            mailMessage.setFrom(from);
            // 创建邮件的接收者地址，并设置到邮件消息中
            javax.mail.Address to = new javax.mail.internet.InternetAddress(mailInfo.getToAddress());
            mailMessage.setRecipient(javax.mail.Message.RecipientType.TO, to);
            // 设置邮件消息的主题
            mailMessage.setSubject(mailInfo.getSubject());
            // 设置邮件消息发送的时间
            mailMessage.setSentDate(new Date());
            // 设置邮件消息的主要内容
            String mailContent = mailInfo.getContent();
            mailMessage.setText(mailContent);
            // 发送邮件
            javax.mail.Transport.send(mailMessage);
            return true;
        } catch (MessagingException ex) {
            ex.printStackTrace();
        }
        return false;
    }

    /**
     * 以HTML格式发送邮件
     *
     * @param mailInfo 待发送的邮件信息
     */
    public boolean sendHtmlMail(MailSenderInfo mailInfo) {
        // 判断是否需要身份认证
        MyAuthenticator authenticator = null;
        java.util.Properties pro = mailInfo.getProperties();
        // 如果需要身份认证，则创建一个密码验证器
        if (mailInfo.isValidate()) {
            authenticator = new MyAuthenticator(mailInfo.getUserName(),
                    mailInfo.getPassword());
        }
        // 根据邮件会话属性和密码验证器构造一个发送邮件的session
        javax.mail.Session sendMailSession = Session
                .getDefaultInstance(pro, authenticator);
        try {
            // 根据session创建一个邮件消息
            javax.mail.Message mailMessage = new MimeMessage(sendMailSession);
            // 创建邮件发送者地址
            javax.mail.Address from = new javax.mail.internet.InternetAddress(mailInfo.getFromAddress());
            // 设置邮件消息的发送者
            mailMessage.setFrom(from);
            // 创建邮件的接收者地址，并设置到邮件消息中
            javax.mail.Address to = new javax.mail.internet.InternetAddress(mailInfo.getToAddress());
            // Message.RecipientType.TO属性表示接收者的类型为TO
            mailMessage.setRecipient(javax.mail.Message.RecipientType.TO, to);
            // 设置邮件消息的主题
            mailMessage.setSubject(mailInfo.getSubject());
            // 设置邮件消息发送的时间
            mailMessage.setSentDate(new Date());

            // MiniMultipart类是一个容器类，包含MimeBodyPart类型的对象
            javax.mail.Multipart mainPart = new javax.mail.internet.MimeMultipart();
            // 创建一个包含HTML内容的MimeBodyPart
            javax.mail.BodyPart html = new javax.mail.internet.MimeBodyPart();
            // 设置HTML内容
            html.setContent(mailInfo.getContent(), "text/html; charset=utf-8");
            mainPart.addBodyPart(html);

            // 设置信件的附件(用本地上的文件作为附件)
            // html = new MimeBodyPart();
            // FileDataSource fds = new FileDataSource("D:\\...javamail.doc");
            // DataHandler dh = new DataHandler(fds);
            // html.setFileName("javamail.doc");
            // html.setDataHandler(dh);
            // mainPart.addBodyPart(html);

            // 将MiniMultipart对象设置为邮件内容
            mailMessage.setContent(mainPart);
            mailMessage.saveChanges();

            // 发送邮件
            javax.mail.Transport.send(mailMessage);
            return true;
        } catch (MessagingException ex) {
            ex.printStackTrace();
        }
        return false;
    }
}
package com.cenrise.utils.mailCBC;


import java.util.Properties;
import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

public class MyMailTest {
    public static void main(String[] args) throws Exception {

        // 会话===========================
        Properties props = System.getProperties();
        props.put("mail.smtp.host", "smtp.163.com");
        props.put("mail.smtp.auth", "true");// 需要验证
        Session session = Session.getDefaultInstance(props, null);

        // msg 设置=======================
        MimeMessage mimeMsg = new MimeMessage(session);

        // 设置标题
        mimeMsg.setSubject("标题test");

        // 设置内容----begin
        Multipart mp = new MimeMultipart();

        // 添加文本
        BodyPart bp1 = new MimeBodyPart();
        bp1.setContent("文本内容", "text/html;charset=GB2312");
        mp.addBodyPart(bp1);

        // 添加附件
        BodyPart bp2 = new MimeBodyPart();
        FileDataSource fileds = new FileDataSource("c:\\boot.ini");
        bp2.setDataHandler(new DataHandler(fileds));
        bp2.setFileName(fileds.getName());
        mp.addBodyPart(bp2);


        mimeMsg.setContent(mp);
        // 设置内容----end

        mimeMsg.setFrom(new InternetAddress("xiangzhengyan@163.com"));
        mimeMsg.setRecipients(Message.RecipientType.TO, InternetAddress
                .parse("xiangyh@asiacom-online.com"));
        mimeMsg.saveChanges();

        // 传输==================================
        Transport transport = session.getTransport("smtp");
        transport.connect((String) props.get("mail.smtp.host"),
                "xiangzhengyan", "pass");
        transport.sendMessage(mimeMsg, mimeMsg
                .getRecipients(Message.RecipientType.TO));
        transport.close();
    }
}

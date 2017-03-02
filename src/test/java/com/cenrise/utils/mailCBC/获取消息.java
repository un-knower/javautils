package com.cenrise.utils.mailCBC;


import javax.mail.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Properties;

/**
 * 获取消息
 * （1  把应该导入的javax.mail java.util和java.io导入。
 * （2）创建一个Properties对象，存放邮件服务器的信息。
 * （3）给 Propertie对象赋值。
 * （4）生成session对象。
 * （5）从这个session中创建Store。
 * (6) 从Store中获取Folder。
 * (7) 从Folder中读取邮件内容。
 * 阅读邮件时，首先要获取一个会话，然后获取并连接到一个相应的收件箱的存储器上，接着打开相应的文件夹，再获取消息。同时，要在操作完成后关闭连接。
 */
public class 获取消息 {
    public void test() throws MessagingException, IOException {
        String host = "pop3.163.com";
        String username = "courses4public";
        String password = "hytczzh";
        //Create empty properties
        Properties props = new Properties();
        props.put("mail.pop3.auth", "true");
        //Get session
        Session sessipn = Session.getDefaultInstance(props, null);
        //Get the store
        Store store = sessipn.getStore("pop3");
        store.connect(host, username, password);
        //Get folder
        Folder folder = store.getFolder("INBOX");
        folder.open(Folder.READ_ONLY);
        //Get directory
        Message message[] = folder.getMessages();
        for (int i = 0, n = message.length; i < n; i++) {
            System.out.println(i + ": " + message[i].getFrom()[0] + "\t");
        }
        //Close connection
        folder.close(false);
        store.close();

    }

    public void test2() throws MessagingException, IOException {

        String host = "pop3.163.com";
        String username = "courses4public";
        String password = "hytczzh";
        //Create empty properties
        Properties props = new Properties();
        props.put("mail.pop3.auth", "true");
        //Get session
        Session sessipn = Session.getDefaultInstance(props, null);
        //Get the store
        Store store = sessipn.getStore("pop3");
        store.connect(host, username, password);
        //Get folder
        Folder folder = store.getFolder("INBOX");
        folder.open(Folder.READ_ONLY);
        //Get directory
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        //Get directory
        Message[] message = folder.getMessages();
        for (int i = 0, n = message.length; i < n; i++) {
            System.out.println(i + ": " + message[i].getFrom()[0] + "\t" + message[i].getSubject());
            System.out.println("Do you want to read message?" + "[Yes to read read/QUIT to end]");
            String line = reader.readLine();
            if ("Yes".equals(line)) {
                message[i].writeTo(System.out);
            } else if ("QUIT".equals(line)) {
                break;
            }
        }
        //Close connection
        folder.close(false);
        store.close();

    }
}

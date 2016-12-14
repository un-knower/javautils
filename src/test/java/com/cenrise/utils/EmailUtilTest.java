package com.cenrise.utils;

import java.io.File;

import com.cenrise.utils.EmailUtil;


public class EmailUtilTest {


    public void testMail() {
        EmailUtil se   = new EmailUtil(false);
        String    path = System.getProperty("user.dir") + "/Junit/Resource/ali.gif";
        se.doSendHtmlEmail("邮件主题", "邮件内容", "438558488@qq.com");
        File affix = new File(path);
        se.doSendHtmlEmail("邮件主题", "邮件内容", "438558488@qq.com", affix);//
    }
}
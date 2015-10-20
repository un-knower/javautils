package com.cenrise.commons.utils;

import java.io.File;

import org.junit.Test;

import com.cenrise.commons.utils.EmailUtil;


public class EmailUtilTest {

	@Test
    public void testMail() {
        EmailUtil se   = new EmailUtil(false);
        String    path = System.getProperty("user.dir") + "/src/test/resources/ali.gif";
        //se.doSendHtmlEmail("邮件主题", "邮件内容", "3083015151@qq.com");
        File affix = new File(path);
        //se.doSendHtmlEmail("邮件主题", "邮件内容", "3083015151@qq.com", affix);//
    }
}
package com.cenrise.utils.mailCBC;

import java.net.Authenticator;
import java.net.PasswordAuthentication;
import java.util.Properties;

/**
 * 如果smtp需要身份验证,则程序需要自定义一个类继承Authenticator,然后在类中实现getPasswordAuthentication()方法
 */
public class Auth extends Authenticator {
    Properties pwd;

    public Auth() {
        try {
            pwd = new Properties();
            pwd.put("Alex", "123");
            pwd.put("Mary", "456");
            pwd.put("Obama", "110");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected PasswordAuthentication getPasswordAuthentication() {
//        String str = getDefaultUserName();//取得当前的用户
//        System.out.print("");
        return super.getPasswordAuthentication();
    }
}

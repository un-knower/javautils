package com.cenrise.suite;

import com.cenrise.utils.Const;
import org.apache.commons.vfs2.FileObject;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.math.BigInteger;
import java.net.InetAddress;

/**
 * 连接ftp,及业务处理
 */
public class SFTPPUT {

    // String substitution..
    String serverName = "10.151.30.10";//服务器
    String serverPort = "22";//端口设计
    String username = "ci";//用户名
    //    String realPassword = decryptPasswordOptionallyEncrypted("Zj4xyBkgjd");
    String password = "Zj4xyBkgjd";
    String realSftpDirString = "/apps/tomcat7-40-tomcat-air-ticket-merchant/logs";
    String realKeyFilename = null;
    String realPassPhrase = null;
    FileObject TargetFolder = null;
    /**
     * The word that is put before a password to indicate an encrypted form.  If this word is not present, the password is considered to be NOT encrypted
     */
    public static final String PASSWORD_ENCRYPTED_PREFIX = "Encrypted ";
    private static final int RADIX = 16;
    private static final String SEED = "0933910847463829827159347601486730416058";
    private static final int DEFAULT_PORT = 21;

    public static void main(String[] args) {
        SFTPPUT sftpput = new SFTPPUT();
        try {
            sftpput.testsftp();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void testsftp() throws Exception {

        SFTPClient sftpclient = null;
        try {
            // Create sftp client to host ...
            sftpclient = new SFTPClient(InetAddress.getByName(serverName), toInt(serverPort, DEFAULT_PORT), username, realKeyFilename, realPassPhrase);
            System.out.println("使用用户名 [" + username + "] 在端口 [" + serverPort + "] 上打开到服务器 [" + serverName + "] 的SFTP的连接");
            // login to ftp host ...
            sftpclient.login(password);

            // Get all the files in the current directory...
            String[] filelist = sftpclient.dir();
            if (filelist == null) {
                throw new Exception("在远程目录上发现0个目录");
            }

            //下载文件
//            sftpclient.get("/Users/yp-tc-m-2684/jiadp","/apps/tomcat7-40-tomcat-air-ticket-merchant/logs/catalina.out");

            ///apps/tomcat7-40-tomcat-air-ticket-merchant/logs/提交成功.jpeg
            FileObject fileObject = SFTPClientVFS.getFileObject("/Users/yp-tc-m-2684/Downloads/json2.txt");
            sftpclient.put(fileObject, "/apps/tomcat7-40-tomcat-air-ticket-merchant/logs/北京天安门.txt");
            ///Users/yp-tc-m-2684/Downloads/json2.txt
            //郑州地铁.gif


        } catch (Exception e) {
            System.out.println(Const.getStackTracker(e));
            throw new Exception("从SFTP获取文件出错 \\: " + e);
        } finally {
            // close connection, if possible
            try {
                if (sftpclient != null) sftpclient.disconnect();
            } catch (Exception e) {
                // just ignore this, makes no big difference
            }

            try {
                if (TargetFolder != null) {
                    TargetFolder.close();
                    TargetFolder = null;
                }
            } catch (Exception e) {
            }

        }
    }

    /**
     * Decrypts a password if it contains the prefix "Encrypted "
     *
     * @param password The encrypted password
     * @return The decrypted password or the original value if the password doesn't start with "Encrypted "
     */
    public static final String decryptPasswordOptionallyEncrypted(String password) {
        if (!isEmpty(password) && password.startsWith(PASSWORD_ENCRYPTED_PREFIX)) {
            return decryptPassword(password.substring(PASSWORD_ENCRYPTED_PREFIX.length()));
        }
        return password;
    }

    /**
     * Convert a String into an integer.  If the conversion fails, assign a default value.
     *
     * @param str The String to convert to an integer
     * @param def The default value
     * @return The converted value or the default.
     */
    public static final int toInt(String str, int def) {
        int retval;
        try {
            retval = Integer.parseInt(str);
        } catch (Exception e) {
            retval = def;
        }
        return retval;
    }

    /**
     * Check if the string supplied is empty.  A String is empty when it is null or when the length is 0
     *
     * @param string The string to check
     * @return true if the string supplied is empty
     */
    public static final boolean isEmpty(String string) {
        return string == null || string.length() == 0;
    }

    public static final String decryptPassword(String encrypted) {
        if (encrypted == null) return "";
        if (encrypted.length() == 0) return "";

        BigInteger bi_confuse = new BigInteger(SEED);

        try {
            BigInteger bi_r1 = new BigInteger(encrypted, RADIX);
            BigInteger bi_r0 = bi_r1.xor(bi_confuse);

            return new String(bi_r0.toByteArray());
        } catch (Exception e) {
            return "";
        }
    }

    /**
     * Returns a string of the stack trace of the specified exception
     */
    public static final String getStackTracker(Throwable e) {
        return getClassicStackTrace(e);
    }

    public static final String getClassicStackTrace(Throwable e) {
        StringWriter stringWriter = new StringWriter();
        PrintWriter printWriter = new PrintWriter(stringWriter);
        e.printStackTrace(printWriter);
        String string = stringWriter.toString();
        try {
            stringWriter.close();
        } catch (IOException ioe) {
        } // is this really required?
        return string;
    }
}

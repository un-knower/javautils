package com.cenrise.suite;

import com.cenrise.utils.Const;
import com.enterprisedt.net.ftp.FTPClient;
import com.enterprisedt.net.ftp.FTPConnectMode;
import com.enterprisedt.net.ftp.FTPException;

import java.io.IOException;
import java.net.InetAddress;

/**
 * 通过edtftpjput访问ftp
 */
public class EDTFTPJPUT extends FTPClient {
    String serverName = "10.151.30.10";//服务器
    String serverPort = "22";//端口设计
    String username = "ci";//用户名
    String password = "Zj4xyBkgjd";
    String realSftpDirString = "/apps/tomcat7-40-tomcat-air-ticket-merchant/logs";
    public static final int FTP_DEFAULT_PORT = 21;//默认端口号

    FTPClient createFtpClient() {
        return new EDTFTPJPUT();
    }

    public void test() {
        FTPClient ftpclient = null;
        try {
            ftpclient = createAndSetUpFtpClient();
            ftpclient.connect();
            ftpclient.login(username, password);
            String[] dirs = ftpclient.dir();
            for (String dir : dirs) {
                System.out.println(dir);
            }
        } catch (Exception e) {
            System.out.println("传输到 FTP \\: " + e.getMessage() + "时发生错误");
            System.out.println(Const.getStackTracker(e));
        } finally {
            if (ftpclient != null && ftpclient.connected()) {
                try {
                    ftpclient.quit();
                } catch (Exception e) {
                    System.out.println("退出 FTP 连接时发生错误: " + e.getMessage());
                }
            }
        }
    }

    FTPClient createAndSetUpFtpClient() throws IOException, FTPException {
        FTPClient ftpClient = createFtpClient();
        ftpClient.setRemoteAddr(InetAddress.getByName(serverName));
        if (!Const.isEmpty(serverPort)) {
            ftpClient.setRemotePort(Const.toInt(serverPort, FTP_DEFAULT_PORT));
        }

        if (true) {
            ftpClient.setConnectMode(FTPConnectMode.ACTIVE);
            System.out.println("set active ftp connection mode");
        } else {
            ftpClient.setConnectMode(FTPConnectMode.PASV);
            System.out.println("set passive ftp connection mode");
        }

        // Set the timeout
        if (timeout > 0) {
            ftpClient.setTimeout(timeout);
            System.out.println("Set timeout to [" + timeout + "]");
        }
        ftpClient.setControlEncoding(controlEncoding);
        System.out.println("Set control encoding to [" + controlEncoding + "]");
        return ftpClient;
    }

    public static void main(String[] args) {
        EDTFTPJPUT edtftpjput = new EDTFTPJPUT();
        edtftpjput.test();
    }
}

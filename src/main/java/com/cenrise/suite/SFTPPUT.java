package com.cenrise.suite;

import com.cenrise.utils.Const;
import com.cenrise.utils.xml.sax.SaxService;
import org.apache.commons.vfs2.FileObject;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.math.BigInteger;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Map;

/**
 * 连接ftp,及业务处理
 */
public class SFTPPUT {

    // String substitution..
    String serverName = "10.1.11.53";//服务器
    String serverPort = "22";//端口设计
    String username = "root";//用户名
    String password = "123456";
    String realKeyFilename = null;
    String realPassPhrase = null;
    FileObject TargetFolder = null;
    public static final String PASSWORD_ENCRYPTED_PREFIX = "Encrypted ";
    private static final int RADIX = 16;
    private static final String SEED = "0933910847463829827159347601486730416058";
    private static final int DEFAULT_PORT = 21;

    public String realSftpDirString = "/home/appgroup/kettle/pdi-ce-5.0.1.A-stable/data-integration/MyKtrs";

    public static void main(String[] args) {
        SFTPPUT sftpput = new SFTPPUT();
        try {
            sftpput.testsftp("/home/appgroup/kettle/pdi-ce-5.0.1.A-stable/data-integration/MyKtrs/", "hive.ktr", "field");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 读取远程服务器上的文件 TODO 优化
     *
     * @param parentPath
     * @param fileName
     * @return
     * @throws Exception
     */
    public File queryElement(String parentPath, String fileName) throws Exception {

        SFTPClient sftpclient = null;
        try {
            sftpclient = new SFTPClient(InetAddress.getByName(serverName), toInt(serverPort, DEFAULT_PORT), username, realKeyFilename, realPassPhrase);
            System.out.println("使用用户名 [" + username + "] 在端口 [" + serverPort + "] 上打开到服务器 [" + serverName + "] 的SFTP的连接");
            sftpclient.login(password);

            //切换目录
            sftpclient.chdir(parentPath);

            File file = sftpclient.get(fileName);
            System.out.println(file.exists());


            return file;
        } catch (Exception e) {
            System.out.println(Const.getStackTracker(e));
            throw new Exception("从SFTP获取文件出错 \\: " + e);
        } finally {
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
     * 只连接，不切换
     *
     * @return
     * @throws Exception
     */
    public SFTPClient queryElementConnServer() throws Exception {

        SFTPClient sftpclient = null;
        try {
            sftpclient = new SFTPClient(InetAddress.getByName(serverName), toInt(serverPort, DEFAULT_PORT), username, realKeyFilename, realPassPhrase);
            System.out.println("使用用户名 [" + username + "] 在端口 [" + serverPort + "] 上打开到服务器 [" + serverName + "] 的SFTP的连接");
            sftpclient.login(password);
            return sftpclient;

        } catch (Exception e) {
            System.out.println(Const.getStackTracker(e));
            throw new Exception("从SFTP获取文件出错 \\: " + e);
        } finally {
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

    public File queryFile(String parentPath, String fileName) throws Exception {

        SFTPClient sftpclient = null;
        try {
            sftpclient = new SFTPClient(InetAddress.getByName(serverName), toInt(serverPort, DEFAULT_PORT), username, realKeyFilename, realPassPhrase);
            System.out.println("使用用户名 [" + username + "] 在端口 [" + serverPort + "] 上打开到服务器 [" + serverName + "] 的SFTP的连接");
            sftpclient.login(password);

            //切换目录
            sftpclient.chdir(parentPath);

            File file = sftpclient.get(fileName);
            System.out.println(file.exists());

            return file;
        } catch (Exception e) {
            System.out.println(Const.getStackTracker(e));
            throw new Exception("从SFTP获取文件出错 \\: " + e);
        } finally {
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
     * 连接到指定目录
     *
     * @param parentPath
     * @return
     * @throws Exception
     */
    public SFTPClient queryElementConnServer(String parentPath) throws Exception {

        SFTPClient sftpclient = null;
        try {
            sftpclient = new SFTPClient(InetAddress.getByName(serverName), toInt(serverPort, DEFAULT_PORT), username, realKeyFilename, realPassPhrase);
            System.out.println("使用用户名 [" + username + "] 在端口 [" + serverPort + "] 上打开到服务器 [" + serverName + "] 的SFTP的连接");
            sftpclient.login(password);

            //切换目录
            sftpclient.chdir(parentPath);
            return sftpclient;

        } catch (Exception e) {
            System.out.println(Const.getStackTracker(e));
            throw new Exception("从SFTP获取文件出错 \\: " + e);
        } finally {
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
     * 读取远程服务器上的文件
     *
     * @param sftpclient
     * @param fileName
     * @return
     * @throws Exception
     */
    public File queryElementContent(SFTPClient sftpclient, String parentPath, String fileName) throws Exception {

        try {
            if (parentPath != null) {
                //切换目录
                sftpclient.chdir(parentPath);
            }
            File file = sftpclient.get(fileName);
            System.out.println(file.exists());
            return file;
        } catch (Exception e) {
            System.out.println(Const.getStackTracker(e));
            throw new Exception("从SFTP获取文件出错 \\: " + e);
        } finally {
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

    public void testsftp(String parentPath, String fileName, String findName) throws Exception {

        SFTPClient sftpclient = null;
        try {
            sftpclient = new SFTPClient(InetAddress.getByName(serverName), toInt(serverPort, DEFAULT_PORT), username, realKeyFilename, realPassPhrase);
            System.out.println("使用用户名 [" + username + "] 在端口 [" + serverPort + "] 上打开到服务器 [" + serverName + "] 的SFTP的连接");
            sftpclient.login(password);

            //切换目录
            sftpclient.chdir(parentPath);

            File file = sftpclient.get(fileName);
            System.out.println(file.exists());

            ArrayList<Map<String, String>> entryList = (ArrayList<Map<String, String>>) SaxService.ReadXML(file.getPath(), findName);
            for (Map<String, String> entryMap : entryList) {

            }
        } catch (Exception e) {
            System.out.println(Const.getStackTracker(e));
            throw new Exception("从SFTP获取文件出错 \\: " + e);
        } finally {
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

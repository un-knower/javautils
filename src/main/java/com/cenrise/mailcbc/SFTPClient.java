package com.cenrise.mailcbc;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.InetAddress;


import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Proxy;
import com.jcraft.jsch.ProxyHTTP;
import com.jcraft.jsch.ProxySOCKS5;

import com.jcraft.jsch.Session;
import com.jcraft.jsch.SftpException;
import com.jcraft.jsch.ChannelSftp.LsEntry;
import com.jcraft.jsch.SftpATTRS;

import org.apache.commons.io.FileUtils;
import org.apache.commons.vfs2.FileContent;
import org.apache.commons.vfs2.FileObject;
import org.apache.commons.vfs2.FileSystemException;
import org.apache.commons.vfs2.FileType;

/**
 * sftp实现的工具类
 *
 * @author dongpo.jia
 */
public class SFTPClient {

    private static final String COMPRESSION_S2C = "compression.s2c";
    private static final String COMPRESSION_C2S = "compression.c2s";

    public static final String PROXY_TYPE_SOCKS5 = "SOCKS5";
    public static final String PROXY_TYPE_HTTP = "HTTP";
    public static final String HTTP_DEFAULT_PORT = "80";
    public static final int SSH_DEFAULT_PORT = 22;

    private InetAddress serverIP;
    private int serverPort;
    private String userName;
    private String password;
    private String prvkey = null; // Private key
    private String passphrase = null; // Empty passphrase for now
    private String compression = null;

    private Session s;
    private ChannelSftp c;

    /**
     * 初始化连接设置客户端
     *
     * @param serverIP   远程服务器ip
     * @param serverPort 远程服务器端口
     * @param userName   远程服务器用户名
     * @throws Exception
     */
    public SFTPClient(InetAddress serverIP, int serverPort, String userName) throws Exception {
        this(serverIP, serverPort, userName, null, null);
    }

    /**
     * 初始化连接设置客户端
     *
     * @param serverIP           远程服务器ip
     * @param serverPort         远程服务器端口
     * @param userName           远程服务器用户名
     * @param privateKeyFilename filename of private key
     * @throws Exception
     */
    public SFTPClient(InetAddress serverIP, int serverPort, String userName, String privateKeyFilename) throws Exception {
        this(serverIP, serverPort, userName, privateKeyFilename, null);
    }

    /**
     * Init Helper Class with connection settings
     *
     * @param serverIP           IP address of remote server
     * @param serverPort         port of remote server
     * @param userName           username of remote server
     * @param privateKeyFilename filename of private key
     * @param passPhrase         passphrase
     * @throws Exception
     */
    public SFTPClient(InetAddress serverIP, int serverPort, String userName,
                      String privateKeyFilename, String passPhrase) throws Exception {

        if (serverIP == null || serverPort < 0 || userName == null || userName.equals("")) {
            throw new Exception("For a SFTP connection server name and username must be set and server port must be greater than zero.");
        }

        this.serverIP = serverIP;
        this.serverPort = serverPort;
        this.userName = userName;

        JSch jsch = new JSch();
        try {
            if (!isEmpty(privateKeyFilename)) {
                // We need to use private key authentication
                this.prvkey = privateKeyFilename;
                byte[] passphrasebytes = new byte[0];
                if (!isEmpty(passPhrase)) {
                    // Set passphrase
                    this.passphrase = passPhrase;
                    passphrasebytes = GetPrivateKeyPassPhrase().getBytes();
                }
                jsch.addIdentity(
                        getUserName(),
                        FileUtils.readFileToByteArray(new File(GetPrivateKeyFileName())),   // byte[] privateKey
                        null,            // byte[] publicKey
                        passphrasebytes  // byte[] passPhrase
                );
            }
            s = jsch.getSession(userName, serverIP.getHostAddress(), serverPort);
        } catch (IOException e) {
            throw new Exception(e);
        } catch (JSchException e) {
            throw new Exception(e);
        }
    }

    public void login(String password) throws Exception {
        this.password = password;

        s.setPassword(this.getPassword());
        try {
            java.util.Properties config = new java.util.Properties();
            config.put("StrictHostKeyChecking", "no");
            //set compression property
            // zlib, none
            String compress = getCompression();
            if (compress != null) {
                config.put(COMPRESSION_S2C, compress);
                config.put(COMPRESSION_C2S, compress);
            }
            s.setConfig(config);
            s.connect();
            Channel channel = s.openChannel("sftp");
            channel.connect();
            c = (ChannelSftp) channel;
        } catch (JSchException e) {
            throw new Exception(e);
        }
    }

    public void chdir(String dirToChangeTo) throws Exception {
        try {
            c.cd(dirToChangeTo);
        } catch (SftpException e) {
            throw new Exception(e);
        }
    }

    public String[] dir() throws Exception {
        String[] fileList = null;

        try {
            java.util.Vector<?> v = c.ls(".");
            java.util.Vector<String> o = new java.util.Vector<String>();
            if (v != null) {
                for (int i = 0; i < v.size(); i++) {
                    Object obj = v.elementAt(i);
                    if (obj != null && obj instanceof com.jcraft.jsch.ChannelSftp.LsEntry) {
                        LsEntry lse = (com.jcraft.jsch.ChannelSftp.LsEntry) obj;
                        if (!lse.getAttrs().isDir()) o.add(lse.getFilename());
                    }
                }
            }
            if (o.size() > 0) {
                fileList = new String[o.size()];
                o.copyInto(fileList);
            }
        } catch (SftpException e) {
            throw new Exception(e);
        }

        return fileList;
    }

    /**
     * 从ftp上下载文件
     *
     * @param localFilePath 下载目的地路径
     * @param remoteFile    远程文件路径
     * @throws Exception
     */
    public void get(String localFilePath, String remoteFile) throws Exception {
        int mode = ChannelSftp.OVERWRITE;
        try {
            c.get(remoteFile, localFilePath, null, mode);
        } catch (SftpException e) {
            throw new Exception(e);
        }
    }

    public String pwd() throws Exception {
        try {
            return c.pwd();
        } catch (SftpException e) {
            throw new Exception(e);
        }
    }

    /**
     * 上传文件到远程服务器
     *
     * @param fileObject 本地文件
     * @param remoteFile 上传到远程目录的文件名
     * @throws Exception
     */
    public void put(FileObject fileObject, String remoteFile) throws Exception {
        int mode = ChannelSftp.OVERWRITE;
        InputStream inputStream = null;
        try {
            inputStream = getInputStream(fileObject);
            c.put(inputStream, remoteFile, null, mode);
        } catch (Exception e) {
            throw new Exception(e);
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    throw new Exception(e);
                }
            }
        }
    }

    public static InputStream getInputStream(FileObject fileObject) throws FileSystemException {
        FileContent content = fileObject.getContent();
        return content.getInputStream();
    }

    /**
     * 删除文件
     *
     * @param file
     * @throws Exception
     */
    public void delete(String file) throws Exception {
        try {
            c.rm(file);
        } catch (SftpException e) {
            throw new Exception(e);
        }
    }

    /**
     * Creates this file as a folder.
     */
    public void createFolder(String foldername) throws Exception {
        try {
            c.mkdir(foldername);
        } catch (SftpException e) {
            throw new Exception(e);
        }
    }

    /**
     * Rename the file.
     */
    public void renameFile(String sourcefilename, String destinationfilename) throws Exception {
        try {
            c.rename(sourcefilename, destinationfilename);
        } catch (SftpException e) {
            throw new Exception(e);
        }
    }

    public FileType getFileType(String filename) throws Exception {
        try {
            SftpATTRS attrs = c.stat(filename);
            if (attrs == null) return FileType.IMAGINARY;

            if ((attrs.getFlags() & SftpATTRS.SSH_FILEXFER_ATTR_PERMISSIONS) == 0)
                throw new Exception("Unknown permissions error");

            if (attrs.isDir())
                return FileType.FOLDER;
            else
                return FileType.FILE;
        } catch (Exception e) {
            throw new Exception(e);
        }
    }

    public boolean folderExists(String foldername) {
        boolean retval = false;
        try {
            SftpATTRS attrs = c.stat(foldername);
            if (attrs == null) return false;

            if ((attrs.getFlags() & SftpATTRS.SSH_FILEXFER_ATTR_PERMISSIONS) == 0)
                throw new Exception("Unknown permissions error");

            retval = attrs.isDir();
        } catch (Exception e) {
            // Folder can not be found!
        }
        return retval;
    }


    public void setProxy(String host, String port, String user, String pass, String proxyType)
            throws Exception {

        if (isEmpty(host) || toInt(port, 0) == 0) {
            throw new Exception("Proxy server name must be set and server port must be greater than zero.");
        }
        Proxy proxy = null;
        String proxyhost = host + ":" + port;

        if (proxyType.equals(PROXY_TYPE_HTTP)) {
            proxy = new ProxyHTTP(proxyhost);
            if (!isEmpty(user)) {
                ((ProxyHTTP) proxy).setUserPasswd(user, pass);
            }
        } else if (proxyType.equals(PROXY_TYPE_SOCKS5)) {
            proxy = new ProxySOCKS5(proxyhost);
            if (!isEmpty(user)) {
                ((ProxySOCKS5) proxy).setUserPasswd(user, pass);
            }
        }
        s.setProxy(proxy);
    }

    public void disconnect() {
        c.disconnect();
        s.disconnect();
    }

    public String GetPrivateKeyFileName() {
        return this.prvkey;
    }

    public String GetPrivateKeyPassPhrase() {
        return this.passphrase;
    }

    public String getPassword() {
        return password;
    }

    public int getServerPort() {
        return serverPort;
    }

    public String getUserName() {
        return userName;
    }

    public InetAddress getServerIP() {
        return serverIP;
    }

    public void setCompression(String compression) {
        this.compression = compression;
    }

    public String getCompression() {
        if (this.compression == null) return null;
        if (this.compression.equals("none")) return null;
        return this.compression;
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
}

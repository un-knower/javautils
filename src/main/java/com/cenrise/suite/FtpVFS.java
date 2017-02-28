package com.cenrise.suite;

import org.apache.commons.vfs2.*;
import org.apache.commons.vfs2.provider.sftp.SftpFileSystemConfigBuilder;

import java.io.File;
import java.io.FileOutputStream;
import java.net.InetAddress;

/**
 * 通过vfs2连接ftp的方法,测试
 * 这几种方式还没有测试通过
 */
public class FtpVFS {
    private String targetResourceURL;

    public static void main(String[] args) {
        FtpVFS ftpVFS = new FtpVFS();
        try {
            ftpVFS.testftp2();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 测试末通过
     * @throws Exception
     */
    public void testsftp() throws Exception {
        FileSystemManager fsManager = VFS.getManager();
        FileSystemOptions opts = new FileSystemOptions();
        SftpFileSystemConfigBuilder.getInstance().setStrictHostKeyChecking(opts, "no");
        //显示目录
        FileObject fo = fsManager.resolveFile("sftp://ci:Zj4xyBkgjd@10.151.30.10:22/apps/tomcat7-40-tomcat-air-ticket-merchant/logs/", opts);
        FileObject[] children = fo.getChildren();//得到远程文件列表

        for (int i = 0; i < children.length; i++) {
            FileObject f = children[i];
            FileContent c = f.getContent();

            File localFile = new File(f.getName().getBaseName());
            FileOutputStream out = new FileOutputStream(localFile);
            org.apache.commons.io.IOUtils.copy(c.getInputStream(), out);//写入本地
            //或使用写入
            FileObject obj = fsManager.resolveFile(this.getTargetResourceURL() + f.getName().getBaseName());
            if (!obj.exists()) {
                obj.createFile();
                obj.copyFrom(f, Selectors.SELECT_SELF);
            }


            final long size = (f.getType() == FileType.FILE) ? c.getSize() : -1;
            final long date = (f.getType() == FileType.FILE) ? c.getLastModifiedTime() : -1;
            System.out.println(f.getName().getPath() + " date:" + date + " Size:" + size);
        }
    }


    public void testftp2() throws Exception {
        FileSystemManager fsManager = VFS.getManager();
        FileObject fo = fsManager.resolveFile("ftp://ci:Zj4xyBkgjd@10.151.30.10:21/apps/tomcat7-40-tomcat-air-ticket-merchant/logs");
        FileObject[] children = fo.getChildren();//得到远程文件列表
        for (int i = 0; i < children.length; i++) {
            FileObject f = children[i];
            FileContent c = f.getContent();

            File localFile = new File(f.getName().getBaseName());
            FileOutputStream out = new FileOutputStream(localFile);
            org.apache.commons.io.IOUtils.copy(c.getInputStream(), out);//写入本地
            //或使用写入
            FileObject obj = fsManager.resolveFile(this.getTargetResourceURL() + f.getName().getBaseName());
            if (!obj.exists()) {
                obj.createFile();
                obj.copyFrom(f, Selectors.SELECT_SELF);
            }


            final long size = (f.getType() == FileType.FILE) ? c.getSize() : -1;
            final long date = (f.getType() == FileType.FILE) ? c.getLastModifiedTime() : -1;
            System.out.println(f.getName().getPath() + " date:" + date + " Size:" + size);
        }
    }


    public void testftp() throws Exception {
        FileSystemManager manager;
        try {
            manager = VFS.getManager();
            FileObject ftpFile = manager.resolveFile("ftp://ci:Zj4xyBkgjd@10.151.30.10:21/apps/tomcat7-40-tomcat-air-ticket-merchant/logs");
            FileObject[] children = ftpFile.getChildren();
            System.out.println("Children of " + ftpFile.getName().getURI());
            for (FileObject child : children) {
                String baseName = child.getName().getBaseName();
                System.out.println("文件名(中文)：" + baseName + "  --  " + new String(baseName.getBytes("iso-8859-1"), "UTF-8"));
            }
        } catch (FileSystemException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setTargetResourceURL(String targetResourceURL) {
        this.targetResourceURL = targetResourceURL;
    }

    public String getTargetResourceURL() {
        return targetResourceURL;
    }
}

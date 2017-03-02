package com.cenrise.worktile.mailCBC;


import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeUtility;
import javax.mail.search.AndTerm;
import javax.mail.search.FromStringTerm;
import javax.mail.search.SearchTerm;
import javax.mail.search.SubjectTerm;
import java.io.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.security.Security;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 接收邮件
 *
 * @author dongpo.jia
 */
public class CBCMailFetch {
    private MimeMessage mimeMessage;

    public CBCMailFetch() {

    }

    public CBCMailFetch(MimeMessage mimeMessage) {
        this.mimeMessage = mimeMessage;
    }

    public static void main(String args[]) throws Exception {
        //发送邮件
        CBCMailFetch cbcMailFetch = new CBCMailFetch();
        cbcMailFetch.fetchMail();
        for (String str : cbcMailFetch.getSpecifiedDate()) {
            System.out.println(str);
        }
    }

    public void fetchMail() throws Exception {
        Security.addProvider(new com.sun.net.ssl.internal.ssl.Provider());
        final String SSL_FACTORY = "javax.net.ssl.SSLSocketFactory";

        Properties props = System.getProperties();
        props.setProperty("mail.pop3.socketFactory.class", SSL_FACTORY);
        props.setProperty("mail.pop3.socketFactory.fallback", "false");
        props.setProperty("mail.pop3.port", "995");
        props.setProperty("mail.pop3.socketFactory.port", "995");

        Session session = Session.getDefaultInstance(props, null);

        URLName urln = new URLName("pop3", "pop.yeepay.com", 995, null,
                "dongpo.jia@yeepay.com", "2012@beijing");
        Store store = session.getStore(urln);
        Folder inbox = null;
        try {
            store.connect();
            inbox = store.getFolder("INBOX");
            inbox.open(Folder.READ_ONLY);
            FetchProfile profile = new FetchProfile();
            profile.add(FetchProfile.Item.ENVELOPE);

            // 搜索发件人为”智联招聘“或主题包含”中国建设银行“的邮件
            //SearchTerm subjectTerm = new SubjectTerm("中国建设银行");//收件箱主题过滤
            //SearchTerm addressTerm = new FromTerm(new InternetAddress("295445156@qq.com"));//发件人地址
            SearchTerm andTerm = new AndTerm(new FromStringTerm("min.hu@yeepay.com"), new SubjectTerm("中国建设银行"));
            Message[] messages = inbox.search(andTerm);

//            Message[] messages = inbox.getMessages();
            inbox.fetch(messages, profile);
            System.out.println("收件箱的邮件数：" + messages.length);
            CBCMailFetch pmm = null;
            for (int i = 0; i < messages.length; i++) {
                //邮件发送者、邮件标题、邮件大小、邮件发送时间
                String from = Const.decodeText(messages[i].getFrom()[0].toString());
                InternetAddress ia = new InternetAddress(from);
                System.out.println("邮件信息,发送者:[" + ia.getPersonal() + "],发件人邮箱地址:[" + (ia.getAddress()) + "],标题:[" + messages[i].getSubject() + "],邮件大小:[" + messages[i].getSize() + "],发送时间:[" + messages[i].getSentDate() + "]");

                pmm = new CBCMailFetch((MimeMessage) messages[i]);

                Message message = messages[i];
                Multipart multipart = (Multipart) message.getContent();
                for (int j = 0, n = multipart.getCount(); j < n; j++) {
                    Part part = multipart.getBodyPart(j);
                    String disposition = part.getDisposition();
                    if (disposition != null && ((disposition.equals(Part.ATTACHMENT) || (disposition.equals(Part.INLINE))))) {
                        String attachmentName = part.getFileName();
                        System.out.println(attachmentName);
                        //SHOP.105584045110069.20161028.zip
                        String[] attachmentNames = attachmentName.split("\\.");
                        System.out.println("第一个元素:" + attachmentNames[0] + "第二个元素:" + attachmentNames[1] + "第三个元素:" + attachmentNames[2] + "第四个元素:" + attachmentNames[3]);

                        if (attachmentNames[1] == null || attachmentNames[2] == null) {
                            throw new Exception("附件结构不符合,记录");
                        }

                        //格式化日期
                        DateFormat format = new SimpleDateFormat("yyyyMMdd");
                        Date date = null;
                        String str = null;
                        try {
                            date = format.parse(attachmentNames[2]);  // Thu Jan 18 00:00:00 CST 2007
                        } catch (ParseException e) {
                            System.out.println("日期格式化错误:" + e);
                            e.printStackTrace();
                        }

                        //昨天到当前(昨天的0点,到当前时间点)的数据或指定的数据
                        boolean configFlag = getSpecifiedDate().contains(attachmentNames[2]);
                        boolean yesterday = date.after(Const.lastDayWholePointDate(new Date())) && date.before(new Date());
                        //TODO 临时去掉,为了方便测试
//                        if (configFlag || yesterday) {
                        //105110054111509为建行豹子8011   //105584045110069建行斑马8010
                        File file = pmm.getOutFile(attachmentName);
                        if (attachmentNames[1].equals("105110054111509") || attachmentNames[1].equals("105584045110069")) {
                            pmm.saveFile(file, part.getInputStream());

                            //解压
                            String fileFullName = file.getAbsolutePath();
                            String fileFullDirTmp = file.getParent() + "/tmp_" + attachmentName;
                            fileFullDirTmp = fileFullDirTmp.substring(0, fileFullDirTmp.length() - 4);
                            unzip(fileFullName, fileFullDirTmp);

                            //获取解压后的文件全路径
                            List<File> files = Const.searchFile(new File(fileFullDirTmp), ".det.");
                            for (File onefile : files) {
                                //发送到指定ftp,后删除生成的目录
                                SFTPPUT sftpput = new SFTPPUT();
                                String ftpRemote = "/apps/tomcat7-40-tomcat-air-ticket-merchant/logs" + "/" + onefile.getName();
                                System.out.println("文件路径[" + onefile.getAbsolutePath() + "],上传至ftp路径[" + ftpRemote + "]");
                                sftpput.put(onefile.getAbsolutePath(), ftpRemote);
                                //删除生成的目录
                                Const.delFolder(onefile.getParent());
                            }
                        }
//                        }
                    }
                }
            }
        } finally {
            try {
                inbox.close(false);
            } catch (Exception e) {
            }
            try {
                store.close();
            } catch (Exception e) {
            }
        }
    }


    /**
     * 解压,通过vfs2的方式
     */
    public void unzip(String packagePath, String outPath) {
        //解压
        //packagePath = "/Users/yp-tc-m-2684/Workspaces/IdeaProjectsTong/JavaMailSample/tmp/SHOP.105110054111509.20170223.zip";
        try {
            URI uri = new URI(packagePath);
            //outPath = "/Users/yp-tc-m-2684/Workspaces/IdeaProjectsTong/JavaMailSample/tmp/tmp_SHOP.105110054111509.20170223";
            Const.createDirFiles(outPath);
            File file = new File(outPath);
            new ZipfileUnpacker(uri).unpack(file);

        } catch (IOException e) {
            e.printStackTrace();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }

    /**
     * 保存文件
     *
     * @param destFile 目标文件完整路径
     * @param in       源文件输入流
     * @throws Exception
     */
    public static void saveFile(File destFile, InputStream in)
            throws IOException {
        OutputStream bos = null;
        try {
            bos = new FileOutputStream(destFile, false);
            int bytesRead = 0;
            byte[] buffer = new byte[8192];
            while ((bytesRead = in.read(buffer, 0, 8192)) != -1) {
                bos.write(buffer, 0, bytesRead);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (bos != null) {
                bos.close();
            }
            if (in != null) {
                in.close();
            }
        }
    }


    /**
     * 输出到项目的tmp目录下
     *
     * @param fileName
     * @return
     */
    public File getOutFile(String fileName) {
        File appDir = new File(System.getProperty("user.dir"), "tmp");
        if (!appDir.exists()) {
            appDir.mkdir();
        }
        //文件夹上创建文件
        File storefile = new File(appDir, fileName);
        System.out.println("storefile's path: " + storefile.toString());
        return storefile;
    }

    /**
     * 读取日期配置,用于读取指定的日期数据
     *
     * @return
     */
    public List<String> getSpecifiedDate() {
        List<String> filterList = new ArrayList<String>();
        try {
            String filename = "/AutoCheckAccount.properties";
//            File configFile = new File(filename);// 只能配置一张表
            InputStream inputStream = this.getClass().getResourceAsStream(filename);
            if (inputStream != null) {
                Properties prop = new Properties();
                prop.load(inputStream);
                // 获取上次的配置信息
                String timestamp = prop.getProperty("timestamp") == null ? null : prop
                        .getProperty("timestamp").trim();
                if (timestamp != null) {
                    Collections.addAll(filterList, timestamp.split(";"));
                }
            } else {
                System.out.println("不存在当前文件[" + filename + "]");
            }
        } catch (Exception e) {
            System.out.println("读取指定的日志配置失败![" + e + "]");
            e.printStackTrace();
        }
        return filterList;
    }


}

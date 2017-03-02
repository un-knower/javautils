package com.cenrise.utils.mailCBC;

import org.apache.commons.vfs2.FileSystemException;
import org.apache.tools.zip.ZipEntry;
import org.apache.tools.zip.ZipFile;

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
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.ZipException;

/**
 * 接收@yeepay.com
 */
public class CBCMailFetch {
    private MimeMessage mimeMessage;
    private String saveAttachPath = "."; //附件下载后的存放目录
    private StringBuffer bodytext = new StringBuffer();//存放邮件内容
    private String dateformat = "yy-MM-dd HH:mm"; //默认的日前显示格式
//    public static final char separator = '\\';

    public CBCMailFetch() {

    }

    public CBCMailFetch(MimeMessage mimeMessage) {
        this.mimeMessage = mimeMessage;
    }

    public void setMimeMessage(MimeMessage mimeMessage) {
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
                String from = decodeText(messages[i].getFrom()[0].toString());
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
                        boolean yesterday = date.after(lastDayWholePointDate(new Date())) && date.before(new Date());
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
                    //pmm.setAttachPath(".");
                    //pmm.saveAttachMent((Part) messages[i]);
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
     * 保存文件输入流到指定目录和文件名的文件实体中
     *
     * @param destFileName 目标文件名
     * @param in           源文件输入流
     * @param destFilePath 目标文件路径
     * @throws Exception
     */
    public static void saveFile(String destFileName, InputStream in,
                                String destFilePath) throws IOException {
        OutputStream bos = null;// 建立一个保存文件的输出流

        try {
            destFilePath = trimFilePath(destFilePath) + "/" + destFileName;
            bos = new FileOutputStream(destFilePath, false);
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

    public static String trimFilePath(String filePath) {
        if (filePath == null || filePath.trim().length() == 0) {
            return "";
        }
        if (filePath.endsWith(String.valueOf('\\'))) {
            filePath = filePath.substring(0, filePath.length() - 1);
        }
        return filePath;
    }


    /**
     * 真正的保存附件到指定目录里
     *
     * @param fileName
     * @param in
     * @throws Exception
     */
    private void saveFileOld(String fileName, InputStream in) throws Exception {
        String osName = System.getProperty("os.name");
        String storedir = getAttachPath();
        String separator = "";
        if (osName == null)
            osName = "";

        String dir = System.getProperty("user.dir");
        if (osName.toLowerCase().indexOf("win") != -1) {
            separator = "\\";
            if (storedir == null || storedir.equals(""))
                storedir = "c:\\tmp";
        } else {
            separator = "/";
            storedir = dir + separator + "/tmp";
        }

        File appDir = new File(System.getProperty("user.dir"), "tmp");
        if (!appDir.exists()) {
            appDir.mkdir();
        }
        //文件夹上创建文件
        File storefile = new File(appDir, fileName);

        System.out.println("storefile's path: " + storefile.toString());
        BufferedOutputStream bos = null;
        BufferedInputStream bis = null;
        try {
            bos = new BufferedOutputStream(new FileOutputStream(storefile));
            bis = new BufferedInputStream(in);
            int c;
            while ((c = bis.read()) != -1) {
                bos.write(c);
                bos.flush();
            }
        } catch (Exception exception) {
            exception.printStackTrace();
            throw new Exception("文件保存失败!");
        } finally {
            bos.close();
            bis.close();
        }
    }

    /**
     * 创建文件支持多级目录 如果目录不存在先创建目录再创建文件，如果目录存在直接创建目录
     *
     * @param filePath 需要创建的文件
     * @return 是否成功
     */
    public static boolean createDirFiles(String filePath) {
        File file = new File(filePath);
        File dir = file.getParentFile();
        if (!dir.exists()) {
            if (dir.mkdirs()) {
                try {
                    return file.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } else {
            try {
                return file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return false;
    }


    /**
     * 判断此邮件是否包含附件
     */
    public boolean isContainAttach(Part part) throws Exception {
        boolean attachflag = false;
        String contentType = part.getContentType();
        if (part.isMimeType("multipart/*")) {
            Multipart mp = (Multipart) part.getContent();
            for (int i = 0; i < mp.getCount(); i++) {
                BodyPart mpart = mp.getBodyPart(i);
                String disposition = mpart.getDisposition();
                if ((disposition != null)
                        && ((disposition.equals(Part.ATTACHMENT)) || (disposition
                        .equals(Part.INLINE))))
                    attachflag = true;
                else if (mpart.isMimeType("multipart/*")) {
                    attachflag = isContainAttach((Part) mpart);
                } else {
                    String contype = mpart.getContentType();
                    if (contype.toLowerCase().indexOf("application") != -1)
                        attachflag = true;
                    if (contype.toLowerCase().indexOf("name") != -1)
                        attachflag = true;
                }
            }
        } else if (part.isMimeType("message/rfc822")) {
            attachflag = isContainAttach((Part) part.getContent());
        }
        return attachflag;
    }

    /**
     * 保存附件
     *
     * @param part
     * @throws Exception
     */
    public void saveAttachMent(Part part) throws Exception {
        String fileName = "";
        if (part.isMimeType("multipart")) {
            Multipart mp = (Multipart) part.getContent();
            for (int i = 0; i < mp.getCount(); i++) {
                BodyPart mpart = mp.getBodyPart(i);
                String disposition = mpart.getDisposition();
                if ((disposition != null)
                        && ((disposition.equals(Part.ATTACHMENT)) || (disposition
                        .equals(Part.INLINE)))) {
                    fileName = mpart.getFileName();
                    if (fileName.toLowerCase().indexOf("gb2312") != -1) {
                        fileName = MimeUtility.decodeText(fileName);
                    }
                    saveFileOld(fileName, mpart.getInputStream());
                } else if (mpart.isMimeType("multipart")) {
                    saveAttachMent(mpart);
                } else {
                    fileName = mpart.getFileName();
                    if ((fileName != null)
                            && (fileName.toLowerCase().indexOf("GB2312") != -1)) {
                        fileName = MimeUtility.decodeText(fileName);
                        saveFileOld(fileName, mpart.getInputStream());
                    }
                }
            }
        } else if (part.isMimeType("message/rfc822")) {
            saveAttachMent((Part) part.getContent());
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

    public static boolean createDir(String dir) {
        boolean result = false;
        File f = new File(toLocalDir(dir));
        if (f.exists()) {
            return true;
        }
        result = f.mkdir();
        if (!result) {
            result = f.mkdirs();
        }
        return result;
    }

    public static String toLocalDir(String dir) {
        String osName = System.getProperty("os.name").toLowerCase();
        return toLocalDir(osName, dir);
    }

    public static String toLocalDir(String osName, String dir) {
        osName = osName.toLowerCase();
        if (osName.indexOf("windows") != -1) {
            return dir.replace('/', '\\');
        }

        return dir.replace('\\', '/');
    }

    protected static String decodeText(String text)
            throws UnsupportedEncodingException {
        if (text == null)
            return null;
        if (text.startsWith("=?GB") || text.startsWith("=?gb"))
            text = MimeUtility.decodeText(text);
        else
            text = new String(text.getBytes("ISO8859_1"));
        return text;
    }


    /**
     * 设置附件存放路径
     *
     * @param attachpath
     */
    public void setAttachPath(String attachpath) {
        this.saveAttachPath = attachpath;
    }

    /**
     * 设置日期显示格式
     *
     * @param format
     * @throws Exception
     */
    public void setDateFormat(String format) throws Exception {
        this.dateformat = format;
    }

    /**
     * 获得附件存放路径
     *
     * @return
     */
    public String getAttachPath() {
        return saveAttachPath;
    }

    /**
     * 获得此邮件的Message-ID
     */
    public String getMessageId() throws MessagingException {
        return mimeMessage.getMessageID();
    }

    /**
     * 获得邮件主题
     */
    public String getSubject() throws MessagingException {
        String subject = "";
        try {
            subject = MimeUtility.decodeText(mimeMessage.getSubject());
            if (subject == null)
                subject = "";
        } catch (Exception exce) {
        }
        return subject;
    }

    /**
     * 获得邮件发送日期
     */
    public String getSentDate() throws Exception {
        Date sentdate = mimeMessage.getSentDate();
        SimpleDateFormat format = new SimpleDateFormat(dateformat);
        return format.format(sentdate);
    }

    /**
     * 获得邮件正文内容
     */
    public String getBodyText() {
        return bodytext.toString();
    }


    /**
     * 删除一个目录
     *
     * @param file 需要处理的文件
     * @return 是否成功
     */
    public static boolean deleteDir(File file) {
        List<File> files = listFileAll(file);
        if (isValid(files)) {
            for (File f : files) {
                if (f.isDirectory()) {
                    deleteDir(f);
                } else {
                    delFile(f);
                }
            }
        }
        return file.delete();
    }

    /**
     * 罗列指定路径下的全部文件包括文件夹
     *
     * @param path 需要处理的文件
     * @return 返回文件列表
     */
    public static List<File> listFileAll(File path) {
        List<File> list = new ArrayList<File>();
        File[] files = path.listFiles();
        if (isValid(files)) {
            for (File file : files) {
                list.add(file);
                if (file.isDirectory()) {
                    list.addAll(listFileAll(file));
                }
            }
        }
        return list;
    }

    public static boolean isValid(Object[] objs) {
        if (objs != null && objs.length != 0) {
            return true;
        }
        return false;
    }

    /**
     * 删除文件夹
     *
     * @param file
     * @return
     * @throws Exception
     */
    public static void deleteFile(File file) {
        if (file.exists()) { // 判断文件是否存在
            if (file.isFile()) { // 判断是否是文件
                boolean b = file.delete(); // delete()方法 你应该知道 是删除的意思;
                if (b) {
                    System.out.println("delete success...");
                }
            } else if (file.isDirectory()) { // 否则如果它是一个目录
                File files[] = file.listFiles(); // 声明目录下所有的文件 files[];
                for (int i = 0; i < files.length; i++) { // 遍历目录下所有的文件
                    deleteFile(files[i]); // 把每个文件 用这个方法进行迭代
                }
            }
            boolean b = file.delete();
            if (b) {
                System.out.println("delete success...");
            }
        } else {
            System.out.println("file not exist！");
        }
    }

    /**
     * 返回前一天的整点信息
     *
     * @param date
     * @return 2014-3-3 00:00:00
     */
    public static Date lastDayWholePointDate(Date date) {

        GregorianCalendar gc = new GregorianCalendar();
        gc.setTime(date);
        if ((gc.get(gc.HOUR_OF_DAY) == 0) && (gc.get(gc.MINUTE) == 0)
                && (gc.get(gc.SECOND) == 0)) {
            return new Date(date.getTime() - (24 * 60 * 60 * 1000));
        } else {
            Date date2 = new Date(date.getTime() - gc.get(gc.HOUR_OF_DAY) * 60 * 60
                    * 1000 - gc.get(gc.MINUTE) * 60 * 1000 - gc.get(gc.SECOND)
                    * 1000 - 24 * 60 * 60 * 1000);
            return date2;
        }
    }

    /**
     * 删除文件
     *
     * @param file 需要处理的文件
     * @return 是否成功
     */
    public static boolean delFile(File file) {
        return file.delete();
    }

    /**
     * 判断集合的有效性
     */
    @SuppressWarnings("rawtypes")
    public static boolean isValid(Collection col) {
        return !(col == null || col.isEmpty());
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

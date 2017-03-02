package com.cenrise.utils.mailCBC;

import org.apache.tools.zip.ZipEntry;
import org.apache.tools.zip.ZipFile;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeUtility;
import javax.mail.search.*;
import java.io.*;
import java.security.Security;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.zip.ZipException;

/**
 * 接收@yeepay.com
 */
public class YeePayFetch {
    private MimeMessage mimeMessage = null;
    private String saveAttachPath = "."; //附件下载后的存放目录
    private StringBuffer bodytext = new StringBuffer();//存放邮件内容
    private String dateformat = "yy-MM-dd HH:mm"; //默认的日前显示格式

//    public static final char separator = '\\';

    public YeePayFetch(MimeMessage mimeMessage) {
        this.mimeMessage = mimeMessage;
    }

    public void setMimeMessage(MimeMessage mimeMessage) {
        this.mimeMessage = mimeMessage;
    }


    public static void main(String args[]) throws Exception {
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
            SearchTerm andTerm = new AndTerm(new FromStringTerm("295445156@qq.com"), new SubjectTerm("中国建设银行"));
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
                        pmm.saveFile(pmm.getOutFile(part.getFileName()), part.getInputStream());
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
     * 传入压缩包路径，已传入字符集解压文件到相应文件目录(zip)
     *
     * @param packagePath
     * @param outPath
     * @param characterSet
     * @return
     */
    public static boolean unzip(String packagePath, String outPath, String characterSet)
            throws IOException, FileNotFoundException, ZipException {
        try {
            BufferedInputStream bi;

            if (characterSet == null) {
                characterSet = "GBK"; // 默认GBK
            }

            ZipFile zf = new ZipFile(packagePath, characterSet); // 支持中文

            Enumeration e = zf.getEntries();
            while (e.hasMoreElements()) {
                ZipEntry ze2 = (ZipEntry) e.nextElement();
                String entryName = ze2.getName();
                String path = outPath + "/" + entryName;
                if (ze2.isDirectory()) {
                    System.out.println("正在创建解压目录 - " + entryName);
                    File decompressDirFile = new File(path);
                    if (!decompressDirFile.exists()) {
                        decompressDirFile.mkdirs();
                    }
                } else {
                    System.out.println("正在创建解压文件 - " + entryName);
                    String fileDir = path.substring(0, path.lastIndexOf("/"));
                    File fileDirFile = new File(fileDir);
                    if (!fileDirFile.exists()) {
                        fileDirFile.mkdirs();
                    }
                    BufferedOutputStream bos = new BufferedOutputStream(
                            new FileOutputStream(outPath + "/" + entryName));

                    bi = new BufferedInputStream(zf.getInputStream(ze2));
                    byte[] readContent = new byte[1024];
                    int readCount = bi.read(readContent);
                    while (readCount != -1) {
                        bos.write(readContent, 0, readCount);
                        readCount = bi.read(readContent);
                    }
                    bos.close();
                }
            }
            zf.close();

            // 删除过滤所有 以__MACOSX,.DS_Store文件目录
            File outFile = new File(outPath);
            File[] inner1Files = outFile.listFiles();
            if (inner1Files.length > 0) {
                for (File inner1File : inner1Files) {
                    if (inner1File.getName().startsWith("__MACOSX") || inner1File.getName().startsWith(".DS_Store")) {
                        deleteDir(inner1File);
                    }

                    if (inner1File.isDirectory()) {
                        File[] inner2Files = inner1File.listFiles();
                        if (inner2Files.length > 0) {
                            for (File inner2File : inner2Files) {
                                if (inner2File.getName().startsWith("__MACOSX")
                                        || inner2File.getName().startsWith(".DS_Store")) {
                                    deleteDir(inner1File);
                                }
                            }
                        }
                    }
                }
            }

            return true;
        } catch (Exception e) {
            System.out.println("zip解压失败！characterSet[" + characterSet + "]");
            e.printStackTrace();
            return false;
        } finally {
            // 东航压缩包内直接是各地区压缩包，解决方法，如果解压后0.7都为压缩包则将压缩包放入新建的文件夹内，模拟国航的目录结构
            File[] afterUpZipFile = new File(outPath).listFiles(); // 最外层压缩包内文件，国航为一个文件夹，通用为csv文件，东航为各地区压缩包
            int total = afterUpZipFile.length;
            int zipNum = 0;
            for (File file : afterUpZipFile) {
                if (file.getName().endsWith(".zip")) {
                    zipNum++;
                }
            }
            if ((zipNum / total) > 0.7) {
                String newFilePath = outPath + File.separator + new File(packagePath).getName();
                File newFile = new File(newFilePath);
                newFile.mkdir();

                for (File file : afterUpZipFile) {
                    file.renameTo(new File(newFile.getPath() + File.separator + file.getName()));
                }
            }

            deleteDir(new File(packagePath)); // 删除压缩包
        }
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

}

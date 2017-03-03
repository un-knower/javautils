package com.cenrise.utils;

import java.awt.image.BufferedImage;
import java.io.*;
import java.net.FileNameMap;
import java.net.MalformedURLException;
import java.net.URLConnection;
import java.nio.ByteBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.util.*;
import java.util.zip.ZipException;
//import java.util.zip.ZipEntry;
//import java.util.zip.ZipException;
//import java.util.zip.ZipFile;
//import java.util.zip.ZipEntry;
//import java.util.zip.ZipException;
//import java.util.zip.ZipFile;

import com.cenrise.worktile.mailCBC.*;
import com.cenrise.worktile.mailCBC.Const;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.tools.zip.ZipEntry;
import org.apache.tools.zip.ZipFile;

import com.cenrise.utils.algorithm.FileImpl;
import com.cenrise.utils.algorithm.FileTypeImpl;

/**
 * 封装了些文件相关的操作
 */
public class FileUtil {
    /**
     * Buffer的大小
     */
    private static Integer BUFFER_SIZE = 1024 * 1024 * 10;
    private static final Logger log = Logger.getLogger(FileUtil.class);

    /**
     * 获取文件的行数
     *
     * @param file 统计的文件
     * @return 文件行数
     */
    public static int countLines(File file) {
        int count = 0;
        try (InputStream is = new BufferedInputStream(new FileInputStream(file))) {
            byte[] c = new byte[BUFFER_SIZE];
            int readChars;
            while ((readChars = is.read(c)) != -1) {
                for (int i = 0; i < readChars; ++i) {
                    if (c[i] == '\n')
                        ++count;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return count;
    }

    /**
     * 以列表的方式获取文件的所有行
     *
     * @param file 需要出来的文件
     * @return 包含所有行的list
     */
    public static List<String> lines(File file) {
        List<String> list = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                list.add(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return list;
    }

    /**
     * 以列表的方式获取文件的所有行
     *
     * @param file     需要处理的文件
     * @param encoding 指定读取文件的编码
     * @return 包含所有行的list
     */
    public static List<String> lines(File file, String encoding) {
        List<String> list = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(file), encoding))) {
            String line;
            while ((line = reader.readLine()) != null) {
                list.add(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return list;
    }

    /**
     * 以列表的方式获取文件的指定的行数数据
     *
     * @param file  处理的文件
     * @param lines 需要读取的行数
     * @return 包含制定行的list
     */
    public static List<String> lines(File file, int lines) {
        List<String> list = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                list.add(line);
                if (list.size() == lines) {
                    break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return list;
    }

    /**
     * 以列表的方式获取文件的指定的行数数据
     *
     * @param file     需要处理的函数
     * @param lines    需要处理的行还俗
     * @param encoding 指定读取文件的编码
     * @return 包含制定行的list
     */
    public static List<String> lines(File file, int lines, String encoding) {
        List<String> list = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(file), encoding))) {
            String line;
            while ((line = reader.readLine()) != null) {
                list.add(line);
                if (list.size() == lines) {
                    break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return list;
    }

    /**
     * 以字节的方式读取文件
     *
     * @param file
     * @return
     */
    public static byte[] readAsByte(File file) {
        byte[] res = new byte[0];
        try (FileInputStream fs = new FileInputStream(file); FileChannel channel = fs.getChannel()) {
            ByteBuffer byteBuffer = ByteBuffer.allocate((int) channel.size());
            while ((channel.read(byteBuffer)) > 0) {
                // do nothing
            }
            res = byteBuffer.array();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return res;
    }

    /**
     * 以字节的方式读取较大的文件
     *
     * @param file
     * @return
     */
    public static byte[] readAsByteWithBigFile(File file) {
        byte[] res = new byte[0];
        try (FileChannel fc = new RandomAccessFile(file, "r").getChannel();) {
            MappedByteBuffer byteBuffer = fc.map(FileChannel.MapMode.READ_ONLY, 0, fc.size()).load();
            res = new byte[(int) fc.size()];
            if (byteBuffer.remaining() > 0) {
                // System.out.println("remain");
                byteBuffer.get(res, 0, byteBuffer.remaining());
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return res;
    }

    /**
     * 以字符串的方式读取文件
     *
     * @param file
     * @param encoding
     * @return
     */
    public static String readAsString(File file, String encoding) {
        String res = "";
        try {
            res = new String(readAsByte(file), encoding);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return res;
    }

    /**
     * 以字符串的方式读取大文件
     */
    public static String readAsStringWithBigFile(File file, String encoding) {
        String res = "";
        try {
            res = new String(readAsByteWithBigFile(file), encoding);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return res;
    }

    /**
     * 在文件末尾追加一行
     *
     * @param file 需要处理的函数
     * @param str  添加的子字符串
     * @return 是否成功
     */
    public static boolean appendLine(File file, String str) {
        String lineSeparator = System.getProperty("line.separator", "\n");
        try (RandomAccessFile randomFile = new RandomAccessFile(file, "rw")) {
            long fileLength = randomFile.length();
            randomFile.seek(fileLength);
            randomFile.writeBytes(lineSeparator + str);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 在文件末尾追加一行
     *
     * @param file     需要处理的文件
     * @param str      添加的字符串
     * @param encoding 指定写入的编码
     * @return 是否成功
     */
    public static boolean appendLine(File file, String str, String encoding) {
        String lineSeparator = System.getProperty("line.separator", "\n");
        try (RandomAccessFile randomFile = new RandomAccessFile(file, "rw")) {
            long fileLength = randomFile.length();
            randomFile.seek(fileLength);
            randomFile.write((lineSeparator + str).getBytes(encoding));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 在行尾添加一行,如果是空文件从第一行开始添加
     *
     * @param file
     * @param str
     * @param encoding
     * @return
     * @throws IOException
     */
    public static boolean appendLineFromToOneLine(File file, String str, String encoding) throws IOException {
        List<String> oneLineDatas = lines(file, "GBK");
        String lineSeparator = System.getProperty("line.separator", "\n");
        RandomAccessFile randomFile = new RandomAccessFile(file, "rw");
        try {
            long fileLength = randomFile.length();
            randomFile.seek(fileLength);
            if (oneLineDatas.size() == 0) {
                randomFile.write((str).getBytes(encoding));
            } else {
                randomFile.write((lineSeparator + str).getBytes(encoding));
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            randomFile.close();
        }
        return false;
    }

    /**
     * 在未尾追加的几种实现方式举例参考
     *
     * @param file
     * @param str
     * @param encoding
     */
    private void StudyForappendLine(File file, String str, String encoding) {
        try {
            FileOutputStream fos = new FileOutputStream(new File("d:\\abc.txt"), true);
            String str2 = "ABC \n"; // 字符串末尾需要换行符
            fos.write(str2.getBytes());
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            FileWriter fw = new FileWriter("d:\\abc.txt", true);
            PrintWriter pw = new PrintWriter(fw);
            pw.println("append content"); // 字符串末尾不需要换行符
            pw.close();
            fw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            RandomAccessFile rf = new RandomAccessFile("d:\\abc.txt", "rw");
            rf.seek(rf.length()); // 将指针移动到文件末尾
            rf.writeBytes("Append a line again!\n"); // 字符串末尾需要换行符
            rf.close();// 关闭文件流
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 将字符串写入到文件中
     */
    public static boolean write(File file, String str) {
        try (RandomAccessFile randomFile = new RandomAccessFile(file, "rw")) {
            randomFile.writeBytes(str);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 将字符串以追加的方式写入到文件中
     */
    public static boolean writeAppend(File file, String str) {
        try (RandomAccessFile randomFile = new RandomAccessFile(file, "rw")) {
            long fileLength = randomFile.length();
            randomFile.seek(fileLength);
            randomFile.writeBytes(str);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 将字符串以制定的编码写入到文件中
     */
    public static boolean write(File file, String str, String encoding) {
        try (RandomAccessFile randomFile = new RandomAccessFile(file, "rw")) {
            randomFile.write(str.getBytes(encoding));
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 将字符串以追加的方式以制定的编码写入到文件中
     */
    public static boolean writeAppend(File file, String str, String encoding) {
        try (RandomAccessFile randomFile = new RandomAccessFile(file, "rw")) {
            long fileLength = randomFile.length();
            randomFile.seek(fileLength);
            randomFile.write(str.getBytes(encoding));
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 快速清空一个超大的文件
     *
     * @param file 需要处理的文件
     * @return 是否成功
     */
    public static boolean cleanFile(File file) {
        try (FileWriter fw = new FileWriter(file)) {
            fw.write("");
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 获取文件的Mime类型
     *
     * @param file 需要处理的文件
     * @return 返回文件的mime类型
     * @throws java.io.IOException
     */
    public static String mimeType(String file) throws java.io.IOException {
        FileNameMap fileNameMap = URLConnection.getFileNameMap();
        return fileNameMap.getContentTypeFor(file);
    }

    /**
     * 获取文件的类型
     * <p/>
     * Summary:只利用文件头做判断故不全
     *
     * @param file 需要处理的文件
     * @return 文件类型
     */
    public static String fileType(File file) {
        return FileTypeImpl.getFileType(file);
    }

    /**
     * 获取文件最后的修改时间
     *
     * @param file 需要处理的文件
     * @return 返回文件的修改时间
     */
    public static Date modifyTime(File file) {
        return new Date(file.lastModified());
    }

    /**
     * 获取文件的Hash
     *
     * @param file 需要处理的文件
     * @return 返回文件的hash值
     */
    public static String hash(File file) {
        return SecUtil.FileMD5(file);
    }

    /**
     * 复制文件
     *
     * @param resourcePath 源文件
     * @param targetPath   目标文件
     * @return 是否成功
     */
    public static boolean copy(String resourcePath, String targetPath) {
        File file = new File(resourcePath);
        return copy(file, targetPath);
    }

    /**
     * 复制文件 通过该方式复制文件文件越大速度越是明显
     *
     * @param file       需要处理的文件
     * @param targetFile 目标文件
     * @return 是否成功
     */
    public static boolean copy(File file, String targetFile) {
        try (FileInputStream fin = new FileInputStream(file);
             FileOutputStream fout = new FileOutputStream(new File(targetFile))) {
            FileChannel in = fin.getChannel();
            FileChannel out = fout.getChannel();
            // 设定缓冲区
            ByteBuffer buffer = ByteBuffer.allocate(BUFFER_SIZE);
            while (in.read(buffer) != -1) {
                // 准备写入，防止其他读取，锁住文件
                buffer.flip();
                out.write(buffer);
                // 准备读取。将缓冲区清理完毕，移动文件内部指针
                buffer.clear();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 移动文件到指定目录
     *
     * @param oldPath 包含路径的文件名 如：E:/phsftp/src/ljq.txt
     * @param newPath 目标文件目录 如：E:/phsftp/dest
     */
    public static void moveFile(String oldPath, String newPath) {
        copy(oldPath, newPath);
        delFile(oldPath);
    }

    /**
     * 移动文件到指定目录，不会删除文件夹
     *
     * @param oldPath 源文件目录 如：E:/phsftp/src
     * @param newPath 目标文件目录 如：E:/phsftp/dest
     */
    public static void moveFiles(String oldPath, String newPath) {
        copyDir(oldPath, newPath);
        delAllFile(oldPath);
    }

    /**
     * 移动文件到指定目录，会删除文件夹
     *
     * @param oldPath 源文件目录 如：E:/phsftp/src
     * @param newPath 目标文件目录 如：E:/phsftp/dest
     */
    public static void moveFolder(String oldPath, String newPath) {
        copyDir(oldPath, newPath);
        delFolder(oldPath);
    }

    /**
     * 获取文件的编码(cpDetector)探测
     *
     * @param file 需要处理的文件
     * @return 文件的编码
     */
    public static String cpdetector(File file) {
        try {
            return FileImpl.cpdetector(file.toURL());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 利用简单的文件头字节特征探测文件编码
     *
     * @param file 需要处理的文件
     * @return UTF-8 Unicode UTF-16BE GBK
     */
    public static String simpleEncoding(String file) {
        try {
            return FileImpl.simpleEncoding(file);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 创建多级目录
     *
     * @param paths 需要创建的目录
     * @return 是否成功
     */
    public static boolean createPaths(String paths) {
        File dir = new File(paths);
        return !dir.exists() && dir.mkdir();
    }

    /**
     * 创建文件支持多级目录
     *
     * @param filePath 需要创建的文件
     * @return 是否成功
     */
    public static boolean createFiles(String filePath) {
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
        }
        return false;
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
     * 删除文件
     *
     * @param file 需要处理的文件
     * @return 是否成功
     */
    public static boolean delFile(File file) {
        return file.delete();
    }

    /**
     * 删除文件
     *
     * @param fileName 包含路径的文件名
     */
    public static void delFile(String fileName) {
        try {
            String filePath = fileName;
            java.io.File delFile = new java.io.File(filePath);
            delFile.delete();
        } catch (Exception e) {
            // log.error("删除文件操作出错: " + e.getMessage());
            e.printStackTrace();
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
        if (ValidUtil.isValid(files)) {
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
     * 快速的删除超大的文件
     *
     * @param file 需要处理的文件
     * @return 是否成功
     */
    public static boolean deleteBigFile(File file) {
        return cleanFile(file) && file.delete();
    }

    /**
     * 罗列指定路径下的全部文件
     *
     * @param path 需要处理的文件
     * @return 包含所有文件的的list
     */
    public static List<File> listFile(String path) {
        File file = new File(path);
        return listFile(file);
    }

    /**
     * 删除文件夹
     *
     * @param folderPath 文件夹路径
     */
    public static void delFolder(String folderPath) {
        try {
            // 删除文件夹里面所有内容
            delAllFile(folderPath);
            String filePath = folderPath;
            java.io.File myFilePath = new java.io.File(filePath);
            // 删除空文件夹
            myFilePath.delete();
        } catch (Exception e) {
            // log.error("删除文件夹操作出错" + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * 删除文件夹里面的所有文件
     *
     * @param path 文件夹路径
     */
    public static void delAllFile(String path) {
        File file = new File(path);
        if (!file.exists()) {
            return;
        }
        if (!file.isDirectory()) {
            return;
        }
        String[] childFiles = file.list();
        File temp = null;
        for (int i = 0; i < childFiles.length; i++) {
            // File.separator与系统有关的默认名称分隔符
            // 在UNIX系统上，此字段的值为'/'；在Microsoft Windows系统上，它为 '\'。
            if (path.endsWith(File.separator)) {
                temp = new File(path + childFiles[i]);
            } else {
                temp = new File(path + File.separator + childFiles[i]);
            }
            if (temp.isFile()) {
                temp.delete();
            }
            if (temp.isDirectory()) {
                delAllFile(path + "/" + childFiles[i]);// 先删除文件夹里面的文件
                delFolder(path + "/" + childFiles[i]);// 再删除空文件夹
            }
        }
    }

    /**
     * 复制目录
     *
     * @param filePath   需要处理的文件
     * @param targetPath 目标文件
     */
    public static void copyDir(String filePath, String targetPath) {
        File file = new File(filePath);
        copyDir(file, targetPath);
    }

    /**
     * 复制目录
     *
     * @param filePath   需要处理的文件
     * @param targetPath 目标文件
     */
    public static void copyDir(File filePath, String targetPath) {
        File targetFile = new File(targetPath);
        if (!targetFile.exists()) {
            createPaths(targetPath);
        }
        File[] files = filePath.listFiles();
        if (ValidUtil.isValid(files)) {
            for (File file : files) {
                String path = file.getName();
                if (file.isDirectory()) {
                    copyDir(file, targetPath + "/" + path);
                } else {
                    copy(file, targetPath + "/" + path);
                }
            }
        }
    }

    /**
     * 罗列指定路径下的全部文件
     *
     * @param path 需要处理的文件
     * @return 返回文件列表
     */
    public static List<File> listFile(File path) {
        List<File> list = new ArrayList<>();
        File[] files = path.listFiles();
        if (ValidUtil.isValid(files)) {
            for (File file : files) {
                if (file.isDirectory()) {
                    list.addAll(listFile(file));
                } else {
                    list.add(file);
                }
            }
        }
        return list;
    }

    /**
     * 罗列指定路径下的全部文件包括文件夹
     *
     * @param path 需要处理的文件
     * @return 返回文件列表
     */
    public static List<File> listFileAll(File path) {
        List<File> list = new ArrayList<>();
        File[] files = path.listFiles();
        if (ValidUtil.isValid(files)) {
            for (File file : files) {
                list.add(file);
                if (file.isDirectory()) {
                    list.addAll(listFileAll(file));
                }
            }
        }
        return list;
    }

    /**
     * 罗列指定路径下的全部文件包括文件夹
     *
     * @param path   需要处理的文件
     * @param filter 处理文件的filter
     * @return 返回文件列表
     */
    public static List<File> listFileFilter(File path, FilenameFilter filter) {
        List<File> list = new ArrayList<>();
        File[] files = path.listFiles();
        if (ValidUtil.isValid(files)) {
            for (File file : files) {
                if (file.isDirectory()) {
                    list.addAll(listFileFilter(file, filter));
                } else {
                    if (filter.accept(file.getParentFile(), file.getName())) {
                        list.add(file);
                    }
                }
            }
        }
        return list;
    }

    /**
     * 获取指定目录下的特点文件,通过后缀名过滤
     *
     * @param dirPath  需要处理的文件
     * @param postfixs 文件后缀
     * @return 返回文件列表
     */
    public static List<File> listFileFilter(File dirPath, final String postfixs) {
        /*
         * 如果在当前目录中使用Filter讲只罗列当前目录下的文件不会罗列孙子目录下的文件 FilenameFilter filefilter =
		 * new FilenameFilter() { public boolean accept(File dir, String name) {
		 * return name.endsWith(postfixs); } };
		 */
        List<File> list = new ArrayList<File>();
        File[] files = dirPath.listFiles();
        if (ValidUtil.isValid(files)) {
            for (File file : files) {
                if (file.isDirectory()) {
                    list.addAll(listFileFilter(file, postfixs));
                } else {
                    String fileName = file.getName().toLowerCase();
                    if (fileName.endsWith(postfixs.toLowerCase())) {
                        list.add(file);
                    }
                }
            }
        }
        return list;
    }

    /**
     * 在指定的目录下搜寻文个文件
     *
     * @param dirPath  搜索的目录
     * @param fileName 搜索的文件名
     * @return 返回文件列表
     */
    public static List<File> searchFile(File dirPath, String fileName) {
        List<File> list = new ArrayList<>();
        File[] files = dirPath.listFiles();
        if (ValidUtil.isValid(files)) {
            for (File file : files) {
                if (file.isDirectory()) {
                    list.addAll(searchFile(file, fileName));
                } else {
                    String Name = file.getName();
                    if (Name.equals(fileName)) {
                        list.add(file);
                    }
                }
            }
        }
        return list;
    }

    /**
     * 查找符合正则表达式reg的的文件
     *
     * @param dirPath 搜索的目录
     * @param reg     正则表达式
     * @return 返回文件列表
     */
    public static List<File> searchFileReg(File dirPath, String reg) {
        List<File> list = new ArrayList<>();
        File[] files = dirPath.listFiles();
        if (ValidUtil.isValid(files)) {
            for (File file : files) {
                if (file.isDirectory()) {
                    list.addAll(searchFile(file, reg));
                } else {
                    String Name = file.getName();
                    if (RegUtil.isMatche(Name, reg)) {
                        list.add(file);
                    }
                }
            }
        }
        return list;
    }

    /**
     * 获取文件内容的总行数
     *
     * @param file
     * @return
     * @throws IOException
     */
    static int getTotalLines(File file) throws IOException {
        FileReader in = new FileReader(file);
        LineNumberReader reader = new LineNumberReader(in);
        String s = reader.readLine();
        int lines = 0;
        while (s != null) {
            lines++;
            s = reader.readLine();
            if (lines >= 2) {
                if (s != null) {
                    System.out.println(s + "$");
                }
            }
        }
        reader.close();
        in.close();
        return lines;
    }

    /**
     * 读取数据
     *
     * @param inSream
     * @param charsetName
     * @return
     * @throws Exception
     */
    public static String readData(InputStream inSream, String charsetName) throws Exception {
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int len = -1;
        while ((len = inSream.read(buffer)) != -1) {
            outStream.write(buffer, 0, len);
        }
        byte[] data = outStream.toByteArray();
        outStream.close();
        inSream.close();
        return new String(data, charsetName);
    }

    /**
     * 一行一行读取文件，适合字符读取，若读取中文字符时会出现乱码
     *
     * @param path
     * @return
     * @throws Exception
     */
    public static Set<String> readFile(String path) throws Exception {
        Set<String> datas = new HashSet<String>();
        FileReader fr = new FileReader(path);
        BufferedReader br = new BufferedReader(fr);
        String line = null;
        while ((line = br.readLine()) != null) {
            datas.add(line);
        }
        br.close();
        fr.close();
        return datas;
    }

    /**
     * 判断是否符是合法的文件路径
     *
     * @param path 需要处理的文件路径
     */
    public static boolean legalFile(String path) {
        // 下面的正则表达式有问题
        String regex = "[a-zA-Z]:(?:[/][^/:*?\"<>|.][^/:*?\"<>|]{0,254})+";
        // String regex
        // ="^([a-zA-z]:)|(^\\.{0,2}/)|(^\\w*)\\w([^:?*\"><|]){0,250}";
        return RegUtil.isMatche(commandPath(path), regex);
    }

    /**
     * 传入压缩包路径，已传入字符集解压文件到相应文件目录(zip)
     * 删了压缩文件
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
     * 传入压缩包路径,解压,取里边名称包含det的文件,放在zip同目录,然后把解压后的文件夹删了。
     * 这个不是工具方法。
     *
     * @param files
     * @return
     * @throws IOException
     * @throws FileNotFoundException
     * @throws ZipException
     */
    public static void unZipRedirect(List<File> files) throws IOException, FileNotFoundException, ZipException {
        for (File file : files) {
            String packagePath = file.getAbsolutePath();//压缩文件路径
            String outPath = packagePath.substring(0, packagePath.length() - 4);
            String characterSet = "GBK";

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
            } catch (Exception e) {
                if (e.getMessage().equals("archive is not a ZIP archive")) {
                    System.out.println("zip压缩文件[" + file.getAbsolutePath() + "]异常,文件不能解压!" + SysUtil.lineSeparator() + Const.getStackTracker(e));
                    continue;
                }
                System.out.println("zip压缩文件[" + file.getAbsolutePath() + "]解压过程中发生异常!" + SysUtil.lineSeparator() + Const.getStackTracker(e));
                continue;
            } finally {
                //deleteDir(new File(packagePath)); // 删除压缩包

                //获取解压后的文件全路径
                List<File> filedirs = com.cenrise.worktile.mailCBC.Const.searchFile(new File(outPath), ".det.");
                for (File onefile : filedirs) {
                    //移动文件到zip同级目录
                    copy(onefile, file.getParent() + java.io.File.separator + onefile.getName());
                }

                //删除生成的目录
                com.cenrise.worktile.mailCBC.Const.delFolder(outPath);
            }
        }
    }

    /**
     * 传入压缩包路径，已传入字符集解压文件到相应文件目录(rar)
     *
     * @param packagePath
     * @param outPath
     * @param characterSet
     * @return
     */
    public boolean unrar(String packagePath, String outPath, String characterSet) {

        return false;
    }

    /**
     * 判断压缩包文件名字符集
     *
     * @param str
     * @return
     */
    public static String getEncoding(String str) {
        String encode = "GB2312";
        try {
            if (str.equals(new String(str.getBytes(encode), encode))) {
                String s = encode;
                return s;
            }
        } catch (Exception exception) {
        }
        encode = "ISO-8859-1";
        try {
            if (str.equals(new String(str.getBytes(encode), encode))) {
                String s1 = encode;
                return s1;
            }
        } catch (Exception exception1) {
        }
        encode = "UTF-8";
        try {
            if (str.equals(new String(str.getBytes(encode), encode))) {
                String s2 = encode;
                return s2;
            }
        } catch (Exception exception2) {
        }
        encode = "GBK";
        try {
            if (str.equals(new String(str.getBytes(encode), encode))) {
                String s3 = encode;
                return s3;
            }
        } catch (Exception exception3) {
        }
        return "";
    }

    /**
     * 返回一个通用的文件路径
     *
     * @param file 需要处理的文件路径
     * @return Summary windows中路径分隔符是\在linux中是/但windows也支持/方式 故全部使用/
     */
    public static String commandPath(String file) {
        return file.replaceAll("\\\\", "/").replaceAll("//", "/");
    }

    private static String MESSAGE = "";

    public static final char separator = '\\';

    public static final char linux_separator = '/';

//    private static final int BUFFER_SIZE = 16 * 1024;

    private static final long VIRTUAL_MEMORY_SIZE = 512 * 1024 * 1024;

    /**
     * 读取文件的内容(文件的格式仅限.html、.css、.js)
     * @param filePath
     * @param encode
     * @return
     * @throws Exception
     */
    public static String readFileContent(String filePath, String encode)
            throws Exception {
        BufferedInputStream bis = null;
        StringBuffer sb = new StringBuffer();

        try {
            bis = new BufferedInputStream(new FileInputStream(
                    new File(filePath)));
            byte[] buf = new byte[1024 * 10];
            int len = -1;
            while ((len = bis.read(buf)) != -1) {
                sb.append(new String(buf, 0, len, encode));
            }
        } catch (Exception e) {
            System.out.println("read file content error. reason:" + e);
            throw new IOException(e);
        } finally {
            try {
                bis.close();
            } catch (Exception e) {
                System.out.println("read file content error. reason:" + e);
                throw new Exception(e);
            }
        }
        return sb.toString();
    }

    /**
     * 内容输出到文件
     *
     * @param content  内容
     * @param filePath 文件路径
     * @param encode   编码
     * @return void
     * @throws Exception
     * @throws throws
     */
    public static void stringWriteToFile(String content, String filePath,
                                         String encode) throws Exception {
        File indexTemplateFile = new File(filePath);
        if (indexTemplateFile.exists()) {
            boolean flag = indexTemplateFile.delete();
            if (!flag) {
                System.out.println("delete file is failed");
            }
        }
        try {
            indexTemplateFile.createNewFile();
        } catch (IOException e1) {
            System.out.println("creat file is failed. filePath = " + filePath);
            throw e1;
        }
        BufferedOutputStream out = null;
        try {
            out = new BufferedOutputStream(new FileOutputStream(
                    indexTemplateFile));
            out.write(content.getBytes(encode));
        } catch (Exception e) {
            System.out.println("The content is written to the file failed. filePath = "
                    + filePath);
            throw e;
        } finally {
            try {
                out.close();
            } catch (Exception e) {
                System.out.println("Closes the output stream failure . ");
                throw (e);
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

   /* *//**
     * zip文件解压缩
     *
     * 、、@param zipFileName     文件完整路径
     * 、、@param outputDirectory 解压保存路径
     * 、、@param isCover         是否重名覆盖
     * @throws IOException
     */
  /*  @SuppressWarnings("unchecked")
   public static void unzip(String zipFileName, String outputDirectory,
                             boolean isCover) throws IOException {
        System.out.println("To extract the files ：" + zipFileName
                + SerConstants.COLON + outputDirectory);
        ZipFile zipFile = new ZipFile(zipFileName);
        try {
            Enumeration<ZipEntry> e = zipFile.getEntries();
            org.apache.tools.zip.ZipEntry zipEntry = null;
            createDirectory(outputDirectory, "");
            InputStream in = null;
            FileOutputStream out = null;
            while (e.hasMoreElements()) {
                zipEntry = e.nextElement();
                if (zipEntry.isDirectory()) {
                    String name = zipEntry.getName();
                    name = name.substring(0, name.length() - 1);
                    File f = new File(outputDirectory + File.separator + name);
                    if (!f.exists()) {
                        if (!f.mkdirs()) {
                            zipFile.close();
                            throw new IOException(
                                    "Failed to create the directory！");
                        }
                    }

                } else {
                    String fileName = zipEntry.getName();
                    fileName = fileName.replace(separator, File.separatorChar);
                    fileName = fileName.replace(linux_separator,
                            File.separatorChar);
                    if (fileName.indexOf(File.separatorChar) != -1) {
                        createDirectory(outputDirectory, getPath(fileName));
                        fileName = fileName.substring(fileName.lastIndexOf(separator) + 1,
                                fileName.length());
                    }

                    File f = new File(outputDirectory + File.separator
                            + zipEntry.getName());
                    if (f.exists()) {
                        if (isCover) {
                            // 如果要覆盖，先删除原有文件
                            FileUtil.delete(f);
                        } else {
                            // 如果不覆盖，不做处理
                            continue;
                        }

                        // continue;
                    }

                    if (!f.createNewFile()) {
                        zipFile.close();
                        throw new IOException("Creating file failed ！");
                    }
                    try {
                        in = zipFile.getInputStream(zipEntry);
                        out = new FileOutputStream(f);

                        byte[] by = new byte[1024];
                        int c;
                        while ((c = in.read(by)) != -1) {
                            out.write(by, 0, c);
                        }
                    } catch (IOException ex) {
                        throw ex;
                    } finally {
                        if (out != null) {
                            out.close();
                        }
                        if (in != null) {
                            in.close();
                        }
                    }
                }
            }
        } catch (IOException e) {
            throw e;
        } finally {
            zipFile.close();
        }

    }*/

    /*@SuppressWarnings("unchecked")
    public static void unzipNoDir(String zipFileName, String outputDirectory,
                                  boolean isCover) throws IOException {
        System.out.println("To extract the files ：" + zipFileName
                + SerConstants.COLON + outputDirectory);
        ZipFile zipFile = new ZipFile(zipFileName);
        try {
            Enumeration<ZipEntry> e = zipFile.getEntries();
            org.apache.tools.zip.ZipEntry zipEntry = null;
            createDirectory(outputDirectory, "");
            InputStream in = null;
            FileOutputStream out = null;
            while (e.hasMoreElements()) {
                zipEntry = e.nextElement();
                if (zipEntry.isDirectory()) {

                } else {
                    // String fileName = zipEntry.getName();
                    String newName = zipEntry.getName()
                            .substring(zipEntry.getName().lastIndexOf('/') + 1,
                                    zipEntry.getName().length());
                    File f = new File(outputDirectory + File.separator
                            + newName);
                    if (f.exists()) {
                        if (isCover) {
                            // 如果要覆盖，先删除原有文件
                            FileUtil.delete(f);
                        } else {
                            // 如果不覆盖，不做处理
                            continue;
                        }
                    }

                    if (!f.createNewFile()) {
                        zipFile.close();
                        throw new IOException("Creating file failed ！");
                    }
                    try {
                        in = zipFile.getInputStream(zipEntry);
                        out = new FileOutputStream(f);
                        byte[] by = new byte[1024];
                        int c;
                        while ((c = in.read(by)) != -1) {
                            out.write(by, 0, c);
                        }
                    } catch (IOException ex) {
                        throw ex;
                    } finally {
                        if (out != null) {
                            out.close();
                        }
                        if (in != null) {
                            in.close();
                        }
                    }
                }
            }
        } catch (IOException e) {
            throw e;
        } finally {
            zipFile.close();
        }

    }*/

    public static String trimFilePath(String filePath) {
        if (filePath == null || filePath.trim().length() == 0) {
            return "";
        }
        if (filePath.endsWith(String.valueOf(separator))) {
            filePath = filePath.substring(0, filePath.length() - 1);
        }
        return filePath;
    }

    private static void createDirectory(String directory, String subDirectory)
            throws IOException {
        String dir[];
        File fl = new File(directory);

        if (subDirectory.equals("") && fl.exists() != true) {
            if (fl.mkdirs()) {
                throw new IOException("Creating file failed ！" + directory);
            }
        } else if (!subDirectory.equals("")) {
            dir = subDirectory.replace(separator, File.separatorChar)
                    .split("\\\\");
            StringBuffer sBuffer = new StringBuffer(directory);
            for (int i = 0; i < dir.length; i++) {
                File subFile = new File(sBuffer.toString() + File.separator
                        + dir[i]);
                if (!subFile.exists()) {
                    if (!subFile.mkdirs()) {
                        throw new IOException("Creating file failed ！"
                                + subDirectory);
                    }
                }
                sBuffer.append(separator + dir[i]);
            }

        }

    }

    /**
     * 当文件路径包含文件名时，截取该文件所在文件夹路径
     *
     * @param fileName
     * @return
     */
    public static String getPath(String fileName) {
        if (fileName.lastIndexOf(File.separator) >= 0) {
            return fileName.substring(0, fileName.lastIndexOf(File.separator));
        }
        return fileName;

    }

    /**
     * 把字符串写入指定文件
     *
     * @param fileName
     * @param content
     */
    public static void writeStringToFile(String fileName, String content)
            throws IOException {
        File file = new File(fileName);

        if (file.exists()) {
            FileUtil.delete(file);
        }
        // if (file.createNewFile()) {
        // throw new IOException("创建文件：" + fileName + "失败！");
        // }
        BufferedWriter writer = new BufferedWriter(new FileWriter(file));
        writer.write(content);
        writer.close();
    }

    /**
     * 获取某文件路径的跟路径
     *
     * @param fileName
     * @return
     */
    public static String getRootPath(String fileName) {
        if (fileName.lastIndexOf("/") >= 0) {
            return "/" + fileName.split("/")[1];
        }

        return fileName;

    }

    /**
     * 删除指定的文件
     *
     * @param file
     */
    /*public static void deleteFile(File file) throws IOException {
        if (file == null || !file.exists()) {
            return;
        }

        if (file.isDirectory()) {
            deleteDirectory(file);
        } else {
            if (!file.delete()) {
                System.out.println("deleteFile Delete file failed ："
                        + file.getAbsolutePath());
            }
        }

    }*/

    /**
     * 删除指定的目录
     *
     * @param directory
     * @throws IOException
     */
    public static void deleteDirectory(File directory) throws IOException {
        String[] subFiles = directory.list();
        for (int i = 0; i < subFiles.length; i++) {
            File subFile = new File(directory.getAbsolutePath()
                    + File.separator + subFiles[i]);
            deleteFile(subFile);
        }
        if (!directory.delete()) {
            System.out.println("deleteDirectory Delete the specified directory failed ："
                    + directory.getAbsolutePath());
        }
    }

    /**
     * 获取旧图片全路径
     *
     * @param filePath 图片的路径
     * @param imageUrl 数据库中保存的图片路径
     * @return
     */
    public static String getOldResourcePath(String filePath, String imageUrl) {
        int imageIndex = 0;
        int imageIndex2 = 0;
        int pathIndex = 0;
        int pathIndex2 = 0;
        if (imageUrl != null && !imageUrl.equals("")) {
            imageIndex = imageUrl.lastIndexOf("\\");
            imageIndex2 = imageUrl.lastIndexOf("/");
            imageIndex = (imageIndex > imageIndex2 ? imageIndex : imageIndex2);
        }
        if (filePath != null && !filePath.equals("")) {
            pathIndex = filePath.lastIndexOf("\\");
            pathIndex2 = filePath.lastIndexOf("/");
            pathIndex = (pathIndex > pathIndex2 ? pathIndex : pathIndex2);
        }
        StringBuffer oldImagePath = new StringBuffer();
        if (pathIndex > 0) {
            oldImagePath.append(filePath.substring(0, pathIndex));
        } else {
            oldImagePath.append(filePath);
        }
        oldImagePath.append(File.separator);
        if (imageIndex > 0) {
            oldImagePath.append(imageUrl.substring(imageIndex + 1));
        } else {
            oldImagePath.append(imageUrl);
        }

        return oldImagePath.toString();
    }

    /**
     * 删除旧图片
     *
     * @param fullFilePath 删除图片的全路径
     */
    public static void delOldResource(String fullFilePath) {
        if (fullFilePath != null && !fullFilePath.equals("")) {
            File outFile = new File(fullFilePath);
            if (outFile.exists()) {
                if (!outFile.delete()) {
                    System.out.println("delOldResource Remove a resource file failed ："
                            + fullFilePath);
                }
            }
        }
    }

    /**
     * 资源上传的处理方法
     *
     * @param file  上传的文件实体
     * @param cover 是否重名覆盖 true -- 是，false -- 否
     * @throws PortalMSException
     *//*
    public static void uploadResource(File file, String path, Boolean cover)
            throws PortalMSException {
        if (file == null || path == null || path.equals("")) {
            return;
        }
        File outFile = null;
        try {
            outFile = new File(path);
            String pathName = FileUtil.getPath(outFile.getPath());
            FileMgrTools.createFolder(pathName);
            // 如果有同名文件
            if (outFile.exists()) {
                // 如果选择同名覆盖
                if (cover) {
                    org.apache.commons.io.FileUtil.forceDelete(outFile);
                } else {// 否则退出操作
                    return;
                }
            } else {
                if (!outFile.createNewFile()) {
                    throw new PortalMSException(
                            Constants.ERROR_CODE_NODE_RESOURCE_FIND.getLongValue(),
                            new IOException("Creating file failed ！"));
                }
            }
            // 创建源文件输入流
            FileInputStream in = new FileInputStream(file);
            // 保存输入流到指定文件路径
            FileUtil.saveFile(outFile, in);
        } catch (Exception e) {
            e.printStackTrace();
            throw new PortalMSException(
                    Constants.ERROR_CODE_NODE_RESOURCE_UPLOAD.getLongValue(), e);
        }
    }*/

    /**
     * 复制单个文件
     *
     * @param srcFileName  待复制的文件名
     * @param destFileName 目标文件名
     * @param overlay      如果目标文件存在，是否覆盖
     * @return 如果复制成功返回true，否则返回false
     */
    public static boolean copyFile(String srcFileName, String destFileName,
                                   boolean overlay) {
        File srcFile = new File(srcFileName);

        // 判断源文件是否存在
        if (!srcFile.exists()) {
            MESSAGE = "source file：" + srcFileName + "not exist！";
            System.out.println(MESSAGE);
            return false;
        } else if (!srcFile.isFile()) {
            MESSAGE = "Copy file failed ，source file：" + srcFileName
                    + "Is not a file ！";
            System.out.println(MESSAGE);
            return false;
        }

        // 判断目标文件是否存在
        File destFile = new File(destFileName);
        if (destFile.exists()) {
            // 如果目标文件存在并允许覆盖
            if (overlay) {
                // 删除已经存在的目标文件，无论目标文件是目录还是单个文件
                boolean b = new File(destFileName).delete();
                if (b) {
                    System.out.println("Successfully deleted ...");
                }
            }
        } else {
            // 如果目标文件所在目录不存在，则创建目录
            if (!destFile.getParentFile().exists()) {
                // 目标文件所在目录不存在
                if (!destFile.getParentFile().mkdirs()) {
                    // 复制文件失败：创建目标文件所在目录失败
                    return false;
                }
            }
        }

        // 复制文件
        int byteread = 0; // 读取的字节数
        InputStream in = null;
        OutputStream out = null;

        try {
            in = new FileInputStream(srcFile);
            out = new FileOutputStream(destFile);
            byte[] buffer = new byte[1024];

            while ((byteread = in.read(buffer)) != -1) {
                out.write(buffer, 0, byteread);
            }
            return true;
        } catch (FileNotFoundException e) {
            return false;
        } catch (IOException e) {
            return false;
        } finally {
            try {
                if (out != null)
                    out.close();

            } catch (IOException e) {
                e.printStackTrace();
            }
            if (in != null)
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
        }
    }

    /**
     * 复制整个目录的内容
     *
     * @param srcDirName  待复制目录的目录名
     * @param destDirName 目标目录名
     * @param overlay     如果目标目录存在，是否覆盖
     * @return 如果复制成功返回true，否则返回false
     */
    public static boolean copyDirectory(String srcDirName, String destDirName,
                                        boolean overlay) {
        // 判断源目录是否存在
        File srcDir = new File(srcDirName);
        if (!srcDir.exists()) {
            MESSAGE = "Copy directory failed ：source directory" + srcDirName
                    + "no exist！";
            System.out.println(MESSAGE);
            return false;
        } else if (!srcDir.isDirectory()) {
            MESSAGE = "Copy directory failed ：" + srcDirName
                    + "Is not a directory ！";
            System.out.println(MESSAGE);
            return false;
        }

        // 如果目标目录名不是以文件分隔符结尾，则加上文件分隔符
        if (!destDirName.endsWith(File.separator)) {
            destDirName = destDirName + File.separator;
        }
        File destDir = new File(destDirName);
        // 如果目标文件夹存在
        if (destDir.exists()) {
            // 如果允许覆盖则删除已存在的目标目录
            if (overlay) {
                try {
                    deleteDirectory(new File(destDirName));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                MESSAGE = "Copy directory failed：destination directory "
                        + destDirName + "Already exists ！";
                System.out.println(MESSAGE);
                return false;
            }
        } else {
            if (!destDir.mkdirs()) {
                System.out.println("Copy directory failed：Create destination directory failed ！");
                return false;
            }
        }

        boolean flag = true;
        File[] files = srcDir.listFiles();
        for (int i = 0; i < files.length; i++) {
            // 复制文件
            if (files[i].isFile()) {
                flag = copyFile(files[i].getAbsolutePath(), destDirName
                        + files[i].getName(), overlay);
                if (!flag)
                    break;
            } else if (files[i].isDirectory()) {
                flag = copyDirectory(files[i].getAbsolutePath(), destDirName
                        + files[i].getName(), overlay);
                if (!flag)
                    break;
            }
        }
        if (!flag) {
            MESSAGE = "copy directory" + srcDirName + "to" + destDirName
                    + " fail ！";
            System.out.println(MESSAGE);
            return false;
        } else {
            return true;
        }
    }

    /**
     * 物理删除悬浮菜单图片
     *
     * @param url
     */
   /* public static void deleteImageFile(String url) {
        String rootUrl = XMLFactory.getValueString("publish.resourceHardiskPath");
        StringBuffer generalFilePathSB = new StringBuffer();
        generalFilePathSB.append(rootUrl);
        generalFilePathSB.append(url);
        String oldImagePath = generalFilePathSB.toString();
        delOldResource(oldImagePath);
    }*/

    /**
     * 检查海报真实尺寸 1.如果尺寸为空 或不存在，则默认取其真实尺寸； 2.有尺寸则校验尺寸大小,不匹配抛出错误
     *
     * @param file
     * @param posterType
     * @return
     */
   /* public static boolean checkImageFileSize(File file, String posterType) {
        if (StringUtils.isEmpty(posterType)) {
            return Boolean.TRUE;
        }
        BufferedImage image = null;
        try {
            FileInputStream fis = new FileInputStream(file);
            image = javax.imageio.ImageIO.read(fis);
        } catch (Exception e) {
            return Boolean.FALSE;
        }
        int height = image.getHeight();
        int width = image.getWidth();
        String realSpel = width + "*" + height;
        if (posterType.indexOf(realSpel) != -1) {
            return Boolean.TRUE;
        } else {
            return Boolean.FALSE;
        }

    }*/

    /**
     * 取图片真实尺寸
     *
     * @param file
     * @return
     */
   /* public static String getImageFileSize(File file) {

        BufferedImage image = null;
        try {
            FileInputStream fis = new FileInputStream(file);
            image = javax.imageio.ImageIO.read(fis);
        } catch (Exception e) {
            return null;
        }
        int height = image.getHeight();
        int width = image.getWidth();
        return width + "*" + height;

    }*/

    /**
     * @param dir
     */
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

    /**
     * Description : 镜像
     *
     * @param srcPath 来源文件
     * @param dstPath 目标文件
     * @throws Exception
     */
    public static void mirror(File srcPath, File dstPath) throws Exception {
        mirrordelFile(srcPath, dstPath);
        copyDirectory(srcPath, dstPath);
    }

    // 镜像 根据目标 删除 来源的文件
    private static void mirrordelFile(File src, File dest) {
        if (!dest.exists()) {
            return;
        }
        if (!src.exists()) {
            if (dest.isDirectory()) {
                delete(dest);
            } else {
                boolean flag = dest.delete();
                if (!flag) {
                    System.out.println("this file delete is failed");
                }
            }
            return;
        }

        if (dest.isDirectory()) {
            if (src.isFile()) {
                boolean flag = dest.delete();
                if (!flag) {
                    System.out.println("this file delete is failed");
                }
            } else {
                File[] chilefiles = dest.listFiles();
                if (null != chilefiles && chilefiles.length > 0) {
                    for (int i = 0; i < chilefiles.length; i++) {
                        File dest2 = chilefiles[i];
                        String filename = dest2.getName();
                        String srcPath = src.getPath();
                        File src2 = new File(srcPath + File.separator
                                + filename);
                        mirrordelFile(src2, dest2);
                    }
                }
            }
        } else {
            if (src.isDirectory()) {
                boolean flag = dest.delete();
                if (!flag) {
                    System.out.println("this file delete is failed");
                }
            }
        }
    }

    /**
     * 目录复制
     *
     * @param srcPath 源目录
     * @param dstPath 目标目录
     * @throws IOException
     */
    public static void copyDirectory(File srcPath, File dstPath)
            throws Exception {

        if (srcPath.isDirectory()) {

            if (!dstPath.exists()) {
                boolean ismkSus = dstPath.mkdirs();
                if (!ismkSus) {
                    System.out.println("this directory is exists:" + dstPath);
                }
            }

            String files[] = srcPath.list();

            for (int i = 0; i < files.length; i++) {
                File ff = new File(dstPath, files[i]);
                File srcff = new File(srcPath, files[i]);
                copyDirectory(srcff, ff);
            }
        } else {
            if (!srcPath.exists()) {
                System.out.println("File or directory " + srcPath
                        + "does not exist.");
            } else {
                copy(srcPath, dstPath);
            }
        }
    }

    private static void delete(File file) {
        if (file.isDirectory()) {
            File[] files = file.listFiles();
            for (int i = 0; i < files.length; i++) {
                if (files[i].isDirectory()) {
                    delete(files[i]);
                } else {
                    boolean flag = files[i].delete();
                    if (!flag) {
                        System.out.println("this file delete is failed");
                    }
                }
            }
            boolean flag = file.delete();
            if (!flag) {
                System.out.println("this file delete is failed");
            }
        } else {
            boolean flag = file.delete();
            if (!flag) {
                System.out.println("this file delete is failed");
            }
        }
    }

    /**
     * Description : 保存图片到物理路径下
     *
     * @param src
     * @param dst
     * @throws Exception
     */
    public static void copy(File src, File dst) throws Exception {
        InputStream srcIo = null;
        try {
            long srctime = src.lastModified();
            if (dst.exists()) {
                long srcfilesize = src.length();
                long dstfilesize = dst.length();
                long dsttime = dst.lastModified();
                if (srcfilesize == dstfilesize && srctime == dsttime) {
                    return;
                }
            }
            srcIo = new FileInputStream(src);
            copyStream(srcIo, dst, src.length());
            boolean flag = dst.setLastModified(srctime);
            if (!flag) {
                System.out.println("copy file is failed");
            }
        } catch (FileNotFoundException e) {
            StringBuilder msg = new StringBuilder(src.getAbsolutePath());
            msg.append("The file does not exist !");
            System.out.println(msg.toString());
            throw e;
        } catch (IOException e) {
            StringBuilder msg = new StringBuilder(src.getAbsolutePath());
            msg.append("copy to");
            msg.append(dst.getAbsolutePath());
            msg.append("File failed !");
            System.out.println(msg.toString());
            throw e;
        } finally {
            if (null != srcIo) {
                srcIo.close();
            }
        }
    }

    private static void copyStream(InputStream src, File dst,
                                   final long byteCount) throws Exception {
        FileChannel srcChannel = null;
        FileChannel dstChannel = null;
        BufferedInputStream in = null;
        OutputStream out = null;
        FileOutputStream fOut = null;
        try {
            if (!dst.exists()) {
                boolean flag = dst.createNewFile();
                if (!flag) {
                    System.out.println("create file is failed");
                }
            }
            if (src instanceof FileInputStream && byteCount > -1) {
                srcChannel = ((FileInputStream) src).getChannel();
                fOut = new FileOutputStream(dst);
                dstChannel = fOut.getChannel();
                if (VIRTUAL_MEMORY_SIZE > byteCount) {
                    srcChannel.transferTo(0, srcChannel.size(), dstChannel);
                } else {
                    long postion = 0;
                    while (byteCount > postion) {
                        long needCopyByte = byteCount - postion;
                        if (needCopyByte > VIRTUAL_MEMORY_SIZE) {
                            needCopyByte = VIRTUAL_MEMORY_SIZE;
                        }
                        postion += srcChannel.transferTo(postion,
                                needCopyByte,
                                dstChannel);
                    }
                }
            } else {
                in = new BufferedInputStream(src, BUFFER_SIZE);
                out = new BufferedOutputStream(new FileOutputStream(dst),
                        BUFFER_SIZE);
                byte[] buffer = new byte[BUFFER_SIZE];
                int len = 0;
                while ((len = in.read(buffer)) > 0) {
                    out.write(buffer, 0, len);
                }
            }
        } catch (Exception e) {
            System.out.println("file copy failure");
            throw e;
        } finally {
            try {
                if (null != dstChannel) {
                    dstChannel.close();
                }
            } catch (Exception e) {
                System.out.println("dstChannel close failure");
            }
            try {
                if (null != fOut) {
                    fOut.close();
                }
            } catch (Exception e) {
                System.out.println("fOut close failure");
            }
            try {
                if (null != srcChannel) {
                    srcChannel.close();
                }
            } catch (Exception e) {
                System.out.println("srcChannel close failure");
            }
            try {
                if (null != in) {
                    in.close();
                }
            } catch (Exception e) {
                System.out.println("in close failure");
            }
            try {
                if (null != out) {
                    out.close();
                }
            } catch (Exception e) {
                System.out.println("out close failure");
            }
        }
    }

    /**
     * 得到图片文件的BufferedImage对象(用于获取图片的相关属性: 尺寸等)
     *
     * @param url
     * @return BufferedImage
     * @throws IOException
     * @throws throws
     */
    public static BufferedImage fileToImage(String url) throws IOException {
        File file = new File(url);
        FileInputStream is = new FileInputStream(file);
        BufferedImage sourceImg = javax.imageio.ImageIO.read(is);
        return sourceImg;
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
     * 创建一个文件夹
     *
     * @param path
     * @return
     * @throws Exception
     */
    public static boolean createFolder(String path) throws Exception {
        File dirFile = null;
        boolean isCreate = true;
        try {
            System.out.println("create file folder...");
            dirFile = new File(path);
            // 判断文件夹是否已存在
            boolean isExist = dirFile.exists();
            if (!isExist) {
                // 文件夹不存在，则创建一个
                isCreate = dirFile.mkdirs();
            }
        } catch (Exception e) {
            throw e;
        }
        return isCreate;
    }

    /*
     * 删除一个文件夹
     * @param path
     * @return
     * @throws Exception
     *

    public static boolean deleteFolder(String path) throws Exception{
        try{
            System.out.println("删除文件夹...");
        }catch(Exception e){
            throw e;
        }
        return true;
    }
    */

    /**
     * 从磁盘查找文件是否存在
     *
     * @param path
     * @param fileName
     * @return true:存在       false:不存在
     * @throws Exception
     */
    public static boolean isFileExists(String path, String fileName) throws Exception {
        boolean isExists = false;
        File file = null;
        try {
            System.out.println("Query file exists ...");
            if (fileName == null || "".equalsIgnoreCase(fileName)) {
                file = new File(path);
            } else {
                file = new File(path + "/" + fileName);
            }
            if (file.exists()) {
                // 存在返回true
                isExists = true;
            }
        } catch (Exception e) {
            throw e;
        }
        return isExists;
    }

    /**
     * 从磁盘读取文件内容
     *
     * @param path
     * @param fileName
     * @return 文件内容(String)
     * @throws Exception
     */
    /*public static String getFileContent(String path, String fileName) throws Exception {
        String content = "";
        FileInputStream inFile = null;
        List<byte[]> bytesList = null;
        int byteSize = 0;
        try {
            System.out.println("Read the file contents from the disk ...");
            // 判断文件是否存在
            if (isFileExists(path, fileName)) {
                bytesList = new ArrayList<byte[]>();
                // 新建文件读取流对象
                File in = new File(path + "/" + fileName);
                inFile = new FileInputStream(in);
                byte[] buffer = new byte[1024];
                int i = 0;
                System.out.println("Loop to read 1024 bytes ...");
                // 循环读取1024字节到bytesList中
                while ((i = inFile.read(buffer)) != -1) {
                    // byteSize用于保存总字节大小
                    byteSize += i;
                    byte[] temp = new byte[i];
                    // 将读取到的实际大小存入temp中
                    System.arraycopy(buffer, 0, temp, 0, i);
                    bytesList.add(temp);
                }
                // 申请一个总大小的字节空间
                byte[] total = new byte[byteSize];
                int pos = 0;
                System.out.println("Will read the content is stored in a variable ...");
                // 将bytesList中内容取出入到总的字节空间中
                for (int j = 0; j < bytesList.size(); j++) {
                    byte[] temp = bytesList.get(j);
                    // 将每个临时字节数据存入一个总的字节变量中
                    System.arraycopy(temp, 0, total, pos, temp.length);
                    pos += temp.length;
                }
                String fileEncoding = XMLFactory.getValueString("plugin.staticPage.charSet");
                // 按UTF-8编码方式将字节数据转换成字符数据
                content = new String(total, fileEncoding);
                // content = new
                // String(content.getBytes(fileEncoding),Constants.PAGE_ENCODING.getStringValue());
                // File f = new File(path + "/" + fileName);
                // read = new InputStreamReader(new FileInputStream(f),"UTF-8");
                // BufferedReader reader=new BufferedReader(read);
                // String line;
                // while ((line = reader.readLine()) != null) {
                // content += line;
                // }
            }
        } catch (Exception e) {
            throw e;
        } finally {
            // 关闭打开的文件流
            if (inFile != null) {
                inFile.close();
                inFile = null;
            }
        }
        return content;
    }*/

    /**
     * 根据文件内容创建一个磁盘文件
     *
     * @param path
     * @param fileName
     * @param content
     * @return
     * @throws Exception
     */
    public static boolean createFileByContent(String path, String fileName, byte[] content) throws Exception {
        createFolder(path);
        DataOutputStream out = null;
        File file = null;
        boolean isCreate = true;
        try {
            System.out.println("Create a disk file according to the content ...");
            // 新建一个文件句柄
            file = new File(path + "/" + fileName);
            // 如果文件已存在则先删除
            if (file.exists()) {
                // 如果删除失败，控制下面不再写文件
                isCreate = file.delete();
            } else {
                // 文件不存在则创建一个空文件,如果创建失败，控制下面不再写文件
                isCreate = file.createNewFile();
            }
            // 如果删除或创建文件成功，则写入文件内容
            if (isCreate) {
                // 新建一个文件输入流对象
                out = new DataOutputStream(new FileOutputStream(file));
                // 将内容写入创建的文件中
                out.write(content);
            }
        } catch (Exception e) {
            throw e;
        } finally {
            // 关闭创建的文件流
            if (out != null) {
                out.close();
                out = null;
            }
        }
        return isCreate;
    }

    /**
     * 根据源和目的复制一个文件
     *
     * @param orgPath
     * @param orgFileName
     * @param destPath
     * @param destFileName
     * @return
     * @throws Exception
     */
    public static boolean copyFile(String orgPath, String orgFileName, String destPath, String destFileName)
            throws Exception {
        FileInputStream inFile = null;
        FileOutputStream outFile = null;
        boolean isCopy = true;
        String org = null;
        String dest = null;
        try {
            // 判断源文件是否存在
            if (isFileExists(orgPath, orgFileName)) {
                // 新建源和目标文件句柄
                if (orgFileName == null || orgFileName.equalsIgnoreCase("")) {
                    org = orgPath;
                } else {
                    org = orgPath + "/" + orgFileName;
                }
                if (destFileName == null || destFileName.equalsIgnoreCase("")) {
                    dest = destPath;
                } else {
                    dest = destPath + "/" + destFileName;
                }
                File orgFile = new File(org);
                File destFile = new File(dest);

                //创建目录
                File destDir = new File(destPath);
                if (!destDir.exists()) {
                    boolean flag = destDir.mkdirs();
                    if (flag) {
                        System.out.println("this directory is created successful...");
                    }
                }
                if (destFile.exists()) {
                    // 如果删除失败，控制下面不再复制文件
                    isCopy = destFile.delete();
                }
                if (isCopy) {
                    // 创建源和目标文件的流对象
                    inFile = new FileInputStream(orgFile);
                    outFile = new FileOutputStream(destFile);
                    byte[] buffer = new byte[1024];
                    int i = 0;

                    // 从源文件中每次读取1024字节存入目标文件
                    while ((i = inFile.read(buffer)) != -1) {
                        outFile.write(buffer, 0, i);
                    }
                }
            }
        } catch (Exception e) {
            throw e;
        } finally {
            closeStream(inFile, outFile);
        }
        return isCopy;
    }

    private static void closeStream(FileInputStream inFile, FileOutputStream outFile) {
        // 关闭目标文件流对象
        if (outFile != null) {
            try {
                outFile.close();
            } catch (Exception e) {
                outFile = null;
            }
        }
        // 关闭源文件流对象
        if (inFile != null) {
            try {
                inFile.close();
            } catch (Exception e) {
                inFile = null;
            }
        }
    }

    /*
     * 删除一个文件
     * @param path
     * @param fileName
     * @return
     * @throws Exception
     *
    public static boolean deleteFile(String path, String fileName) throws Exception{
        try{
            System.out.println("删除一个文件...");
        }catch(Exception e){
            throw e;
        }
        return true;
    }
    */

    /**
     * 文件夹镜象对拷
     *
     * @param orgPath       源文件夹路径
     * @param destPath      目标文件夹路径
     * @param copySubFolder 是否拷贝子文件件
     * @return
     * @throws Exception
     */
    public static boolean folderCopy(String orgPath, String destPath, boolean copySubFolder) throws Exception {
        boolean isCopy = true;
        try {
            if (isFileExists(orgPath, null)) {
                System.out.println("The folder image copy ...");
                // 创建目标文件夹
                createFolder(destPath);
                // 获取源文件夹的文件列表
                File[] fileList = getFolderFilesList(orgPath);
                if (fileList != null && fileList.length > 0) {
                    System.out.println("Copy the file to the target folder ...");
                    // 一个一个复制文件到目标文件夹
                    for (int i = 0; i < fileList.length; i++) {
                        // 调用复制方法完成文件的复制功能
                        isCopy = copyFile(orgPath, fileList[i].getName(), destPath, fileList[i].getName());
                        // 如果有一个文件复制失败，则整个复制失败
                        if (!isCopy) {
                            break;
                        }
                    }
                }
            }
        } catch (Exception e) {
            throw e;
        }
        return isCopy;
    }

    /**
     * 文件夹镜象对拷-根据过滤条件
     *
     * @param orgPath       源文件夹路径
     * @param destPath      目标文件夹路径
     * @param fileFilter 是否拷贝子文件件
     * @return
     * @throws Exception
     */
    public static boolean folderCopyByFileFilter(String orgPath, String destPath, FileFilter fileFilter)
            throws Exception {
        boolean isCopy = true;
        try {
            if (isFileExists(orgPath, null)) {
                // 拷贝当前目录下的文件
                folderCopy(orgPath, destPath, false);

                // 拷贝当前目录下未被fileFilter过滤的子文件夹文件
                File[] subFolderList = getSubFolderList(orgPath, fileFilter);
                if (subFolderList != null && subFolderList.length > 0) {

                    String folderName = null;
                    String subOrgPath = null;
                    String subDestPath = null;
                    for (int j = 0; j < subFolderList.length; j++) {
                        if (subFolderList[j].isDirectory()) {
                            folderName = subFolderList[j].getName();
                            subOrgPath = orgPath + "/" + folderName;
                            subDestPath = destPath + "/" + folderName;
                            // 子文件件拷贝(嵌套)
                            isCopy = folderCopyByFileFilter(subOrgPath, subDestPath, fileFilter);
                            if (!isCopy) {
                                break;
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            throw e;
        }
        return isCopy;
    }

    /**
     * 获取文件夹下子文件夹列表-根据过滤条件
     *
     * @param path
     * @return
     * @throws Exception
     */
    private static File[] getSubFolderList(final String path, FileFilter fileFilter) throws Exception {
        File[] fileList = null;
        try {
            System.out.println("Suddenly get folder folder list -According to the filtering conditions ...");

            if (path != null && !"".equalsIgnoreCase(path)) {
                // 创建一个文件句柄
                File dir = new File(path);
                // 读取文件列表
                fileList = dir.listFiles(fileFilter);
                // 文件列表排序
                if (fileList != null && fileList.length > 0) {
                    Arrays.sort(fileList);
                }
            }
        } catch (Exception e) {
            throw e;
        }
        return fileList;
    }

    /**
     * 获取文件夹下文件列表
     *
     * @param path
     * @return
     * @throws Exception
     */
    private static File[] getFolderFilesList(final String path) throws Exception {
        File[] fileList = null;
        try {
            System.out.println("Gets the file folder list ...");
            // 创建一个内部类实现文件夹过滤，只罗列文件，不罗列文件夹
            FileFilter filter = new FileFilter() {
                public boolean accept(File pathname) {
                    boolean isAccept = false;
                    // 过滤文件夹和以__.为名的临时生成的文件
                    if (pathname.isFile()) {
                        String fileName = pathname.getName();
                        if (fileName != null && fileName.indexOf("__.") < 0) {
                            isAccept = true;
                        }
                    }
                    return isAccept;
                }
            };
            if (path != null && !"".equalsIgnoreCase(path)) {
                // 创建一个文件句柄
                File dir = new File(path);
                // 读取文件列表
                fileList = dir.listFiles(filter);
                // 文件列表排序
                if (fileList != null && fileList.length > 0) {
                    Arrays.sort(fileList);
                }
            }
        } catch (Exception e) {
            throw e;
        }
        return fileList;
    }

    public static String getRootPath(String curretpath, boolean isFile) {
        if (null == curretpath || "".equals(curretpath)) {
            return curretpath;
        }
        File f = new File(curretpath);
        curretpath = f.getPath();
        int tmp = 0;
        StringBuffer buf = new StringBuffer();
        while ((tmp = curretpath.indexOf("/", tmp)) != -1) {
            tmp++;
            if (isFile) {
                isFile = false;
                continue;
            }
            buf.append("..").append("/");
        }
        return buf.toString().equals("") ? "" : buf.toString().substring(0, buf.length() - 1);
    }
}
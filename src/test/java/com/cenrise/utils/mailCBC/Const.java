package com.cenrise.utils.mailCBC;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by dongpo.jia on 2017/3/1.
 */
public class Const {
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
     * 在指定的目录下搜寻文件名包含件
     *
     * @param dirPath  搜索的目录
     * @param str 字段串
     * @return 返回文件列表
     */
    public static List<File> searchFile(File dirPath, String str) {
        List<File> list = new ArrayList<File>();
        File[] files = dirPath.listFiles();
        if (isValid(files)) {
            for (File file : files) {
                if (file.isDirectory()) {
                    list.addAll(searchFile(file, str));
                } else {
                    String Name = file.getName();
                    if (Name.contains(str)) {
                        list.add(file);
                    }
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
     * 返回指定的堆栈异常信息
     *
     * @param e
     * @return
     */
    public static String getStackTracker(Throwable e) {
        return getClassicStackTrace(e);
    }

    public static String getClassicStackTrace(Throwable e) {
        StringWriter stringWriter = new StringWriter();
        PrintWriter printWriter = new PrintWriter(stringWriter);
        e.printStackTrace(printWriter);
        String string = stringWriter.toString();
        try {
            stringWriter.close();
        } catch (IOException ioe) {
            // is this really required?
        }
        return string;
    }

    /**
     * java中判断字符串是否为数字的方法的几种方法
     * @param str
     * @return
     */
    public static boolean isNumeric(String str){
        //匹配所有数字，包括负数
        Pattern pattern = Pattern.compile("-?[0-9]+.?[0-9]*");
        Matcher isNum = pattern.matcher(str);
        if( !isNum.matches() ){
            return false;
        }
        return true;
    }

    public static void main(String[] args) {
        System.out.println(isNumeric("0."));
    }

}

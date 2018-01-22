package com.cenrise.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

/**
 * 压缩文档相关的工具类
 */
public class ZIPUtil {
    /**
     * 文档压缩
     *
     * @param file 需要压缩的文件或目录
     * @param dest 压缩后的文件名称
     * @throws Exception
     */
    public static void deCompress(File file, String dest) throws Exception {
        try (ZipOutputStream zos = new ZipOutputStream(new FileOutputStream(dest))) {
            zipFile(file, zos, "");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void zipFile(File inFile, ZipOutputStream zos, String dir) throws IOException {
        if (inFile.isDirectory()) {
            File[] files = inFile.listFiles();
            if (ValidUtil.isValid(files)) {
                for (File file : files) {
                    String name = inFile.getName();
                    if (!"".equals(dir)) {
                        name = dir + "/" + name;
                    }
                    zipFile(file, zos, name);
                }
            }
        } else {
            String entryName = null;
            if (!"".equals(dir)) {
                entryName = dir + "/" + inFile.getName();
            } else {
                entryName = inFile.getName();
            }
            ZipEntry entry = new ZipEntry(entryName);
            zos.putNextEntry(entry);
            try (InputStream is = new FileInputStream(inFile)) {
                int len = 0;
                while ((len = is.read()) != -1) {
                    zos.write(len);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 文档解压
     *
     * @param source 需要解压缩的文档名称
     * @param path   需要解压缩的路径
     */
    public static void unCompress(File source, String path) throws IOException {
        ZipEntry zipEntry = null;
        FileUtil.createPaths(path);
        //实例化ZipFile，每一个zip压缩文件都可以表示为一个ZipFile
        //实例化一个Zip压缩文件的ZipInputStream对象，可以利用该类的getNextEntry()方法依次拿到每一个ZipEntry对象
        try (
                ZipFile zipFile = new ZipFile(source);
                ZipInputStream zipInputStream = new ZipInputStream(new FileInputStream(source))
        ) {
            while ((zipEntry = zipInputStream.getNextEntry()) != null) {
                String fileName = zipEntry.getName();
                File temp = new File(path + "/" + fileName);
                if (!temp.getParentFile().exists()) {
                    temp.getParentFile().mkdirs();
                }
                try (OutputStream os = new FileOutputStream(temp);
                     //通过ZipFile的getInputStream方法拿到具体的ZipEntry的输入流
                     InputStream is = zipFile.getInputStream(zipEntry)) {
                    int len = 0;
                    while ((len = is.read()) != -1) {
                        os.write(len);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 功能:压缩多个文件成一个zip文件
     *
     * @param srcfile：源文件列表
     * @param zipfile：压缩后的文件
     */
    public static void zipFiles(File[] srcfile, File zipfile) {
        byte[] buf = new byte[1024];
        try {
            //ZipOutputStream类：完成文件或文件夹的压缩
            ZipOutputStream out = new ZipOutputStream(new FileOutputStream(zipfile));
            for (int i = 0; i < srcfile.length; i++) {
                FileInputStream in = new FileInputStream(srcfile[i]);
                out.putNextEntry(new ZipEntry(srcfile[i].getName()));
                int len;
                while ((len = in.read(buf)) > 0) {
                    out.write(buf, 0, len);
                }
                out.closeEntry();
                in.close();
            }
            out.close();
            System.out.println("压缩完成.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 功能:解压缩
     *
     * @param zipfile：需要解压缩的文件
     * @param descDir：解压后的目标目录
     */
    public static void unZipFiles(File zipfile, String descDir) {
        try {
            ZipFile zf = new ZipFile(zipfile);
            for (Enumeration entries = zf.entries(); entries.hasMoreElements(); ) {
                ZipEntry entry = (ZipEntry) entries.nextElement();
                String zipEntryName = entry.getName();
                InputStream in = zf.getInputStream(entry);
                OutputStream out = new FileOutputStream(descDir + zipEntryName);
                byte[] buf1 = new byte[1024];
                int len;
                while ((len = in.read(buf1)) > 0) {
                    out.write(buf1, 0, len);
                }
                in.close();
                out.close();
                System.out.println("解压缩完成.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 功能:
     *
     * @param args
     */
    public static void main(String[] args) {
        //2个源文件
        File f1 = new File("/Users/jiadongpo/Workspaces/GitLabVbill/dp-pro/dp-base/src/main/resources/diagrams/DataExtractProcess.bpmn");
        File f2 = new File("/Users/jiadongpo/Workspaces/GitLabVbill/dp-pro/dp-base/src/main/resources/diagrams/DataExtractProcess.png");
        File[] srcfile = {f1, f2};

        //压缩后的文件
        File zipfile = new File("/Users/jiadongpo/Workspaces/GitLabVbill/dp-pro/dp-base/src/main/resources/diagrams/DataExtractProcess.zip");
        ZIPUtil.zipFiles(srcfile, zipfile);

        //需要解压缩的文件
        File file = new File("/Users/jiadongpo/Documents/未命名文件夹/DataExtractProcess.zip");
        //解压后的目标目录
        String dir = "/Users/jiadongpo/Documents/未命名文件夹 2/";
        ZIPUtil.unZipFiles(file, dir);
    }
}

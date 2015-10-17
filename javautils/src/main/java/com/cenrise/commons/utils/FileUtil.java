package com.cenrise.commons.utils;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.io.PrintWriter;
import java.io.RandomAccessFile;
import java.io.UnsupportedEncodingException;
import java.net.FileNameMap;
import java.net.MalformedURLException;
import java.net.URLConnection;
import java.nio.ByteBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;

import com.cenrise.commons.utils.algorithm.FileImpl;
import com.cenrise.commons.utils.algorithm.FileTypeImpl;

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
	 * @param file
	 *            统计的文件
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
	 * @param file
	 *            需要出来的文件
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
	 * @param file
	 *            需要处理的文件
	 * @param encoding
	 *            指定读取文件的编码
	 * @return 包含所有行的list
	 */
	public static List<String> lines(File file, String encoding) {
		List<String> list = new ArrayList<>();
		try (BufferedReader reader = new BufferedReader(new InputStreamReader(
				new FileInputStream(file), encoding))) {
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
	 * @param file
	 *            处理的文件
	 * @param lines
	 *            需要读取的行数
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
	 * @param file
	 *            需要处理的函数
	 * @param lines
	 *            需要处理的行还俗
	 * @param encoding
	 *            指定读取文件的编码
	 * @return 包含制定行的list
	 */
	public static List<String> lines(File file, int lines, String encoding) {
		List<String> list = new ArrayList<>();
		try (BufferedReader reader = new BufferedReader(new InputStreamReader(
				new FileInputStream(file), encoding))) {
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
		try (FileInputStream fs = new FileInputStream(file);
				FileChannel channel = fs.getChannel()) {
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
			MappedByteBuffer byteBuffer = fc.map(FileChannel.MapMode.READ_ONLY,
					0, fc.size()).load();
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
	 * @param file
	 *            需要处理的函数
	 * @param str
	 *            添加的子字符串
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
	 * @param file
	 *            需要处理的文件
	 * @param str
	 *            添加的字符串
	 * @param encoding
	 *            指定写入的编码
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
	public static boolean appendLineFromToOneLine(File file, String str,
			String encoding) throws IOException {
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
			FileOutputStream fos = new FileOutputStream(
					new File("d:\\abc.txt"), true);
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
	 * @param file
	 *            需要处理的文件
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
	 * @param file
	 *            需要处理的文件
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
	 * @param file
	 *            需要处理的文件
	 * @return 文件类型
	 */
	public static String fileType(File file) {
		return FileTypeImpl.getFileType(file);
	}

	/**
	 * 获取文件最后的修改时间
	 *
	 * @param file
	 *            需要处理的文件
	 * @return 返回文件的修改时间
	 */
	public static Date modifyTime(File file) {
		return new Date(file.lastModified());
	}

	/**
	 * 获取文件的Hash
	 *
	 * @param file
	 *            需要处理的文件
	 * @return 返回文件的hash值
	 */
	public static String hash(File file) {
		return SecUtil.FileMD5(file);
	}

	/**
	 * 复制文件
	 *
	 * @param resourcePath
	 *            源文件
	 * @param targetPath
	 *            目标文件
	 * @return 是否成功
	 */
	public static boolean copy(String resourcePath, String targetPath) {
		File file = new File(resourcePath);
		return copy(file, targetPath);
	}

	/**
	 * 复制文件 通过该方式复制文件文件越大速度越是明显
	 *
	 * @param file
	 *            需要处理的文件
	 * @param targetFile
	 *            目标文件
	 * @return 是否成功
	 */
	public static boolean copy(File file, String targetFile) {
		try (FileInputStream fin = new FileInputStream(file);
				FileOutputStream fout = new FileOutputStream(new File(
						targetFile))) {
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
	 * @param oldPath
	 *            包含路径的文件名 如：E:/phsftp/src/ljq.txt
	 * @param newPath
	 *            目标文件目录 如：E:/phsftp/dest
	 */
	public static void moveFile(String oldPath, String newPath) {
		copy(oldPath, newPath);
		delFile(oldPath);
	}

	/**
	 * 移动文件到指定目录，不会删除文件夹
	 * 
	 * @param oldPath
	 *            源文件目录 如：E:/phsftp/src
	 * @param newPath
	 *            目标文件目录 如：E:/phsftp/dest
	 */
	public static void moveFiles(String oldPath, String newPath) {
		copyDir(oldPath, newPath);
		delAllFile(oldPath);
	}

	/**
	 * 移动文件到指定目录，会删除文件夹
	 * 
	 * @param oldPath
	 *            源文件目录 如：E:/phsftp/src
	 * @param newPath
	 *            目标文件目录 如：E:/phsftp/dest
	 */
	public static void moveFolder(String oldPath, String newPath) {
		copyDir(oldPath, newPath);
		delFolder(oldPath);
	}

	/**
	 * 获取文件的编码(cpDetector)探测
	 *
	 * @param file
	 *            需要处理的文件
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
	 * @param file
	 *            需要处理的文件
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
	 * @param paths
	 *            需要创建的目录
	 * @return 是否成功
	 */
	public static boolean createPaths(String paths) {
		File dir = new File(paths);
		return !dir.exists() && dir.mkdir();
	}

	/**
	 * 创建文件支持多级目录
	 *
	 * @param filePath
	 *            需要创建的文件
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
	 * @param filePath
	 *            需要创建的文件
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
	 * @param file
	 *            需要处理的文件
	 * @return 是否成功
	 */
	public static boolean delFile(File file) {
		return file.delete();
	}

	/**
	 * 删除文件
	 * 
	 * @param fileName
	 *            包含路径的文件名
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
	 * @param file
	 *            需要处理的文件
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
	 * @param file
	 *            需要处理的文件
	 * @return 是否成功
	 */
	public static boolean deleteBigFile(File file) {
		return cleanFile(file) && file.delete();
	}

	/**
	 * 罗列指定路径下的全部文件
	 *
	 * @param path
	 *            需要处理的文件
	 * @return 包含所有文件的的list
	 */
	public static List<File> listFile(String path) {
		File file = new File(path);
		return listFile(file);
	}

	/**
	 * 删除文件夹
	 * 
	 * @param folderPath
	 *            文件夹路径
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
	 * @param path
	 *            文件夹路径
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
	 * @param filePath
	 *            需要处理的文件
	 * @param targetPath
	 *            目标文件
	 */
	public static void copyDir(String filePath, String targetPath) {
		File file = new File(filePath);
		copyDir(file, targetPath);
	}

	/**
	 * 复制目录
	 *
	 * @param filePath
	 *            需要处理的文件
	 * @param targetPath
	 *            目标文件
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
	 * @param path
	 *            需要处理的文件
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
	 * @param path
	 *            需要处理的文件
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
	 * @param path
	 *            需要处理的文件
	 * @param filter
	 *            处理文件的filter
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
	 * @param dirPath
	 *            需要处理的文件
	 * @param postfixs
	 *            文件后缀
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
	 * @param dirPath
	 *            搜索的目录
	 * @param fileName
	 *            搜索的文件名
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
	 * @param dirPath
	 *            搜索的目录
	 * @param reg
	 *            正则表达式
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
	public static String readData(InputStream inSream, String charsetName)
			throws Exception {
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
	 * @param path
	 *            需要处理的文件路径
	 */
	public static boolean legalFile(String path) {
		// 下面的正则表达式有问题
		String regex = "[a-zA-Z]:(?:[/][^/:*?\"<>|.][^/:*?\"<>|]{0,254})+";
		// String regex
		// ="^([a-zA-z]:)|(^\\.{0,2}/)|(^\\w*)\\w([^:?*\"><|]){0,250}";
		return RegUtil.isMatche(commandPath(path), regex);
	}

	/**
	 * 返回一个通用的文件路径
	 *
	 * @param file
	 *            需要处理的文件路径
	 * @return Summary windows中路径分隔符是\在linux中是/但windows也支持/方式 故全部使用/
	 */
	public static String commandPath(String file) {
		return file.replaceAll("\\\\", "/").replaceAll("//", "/");
	}
}
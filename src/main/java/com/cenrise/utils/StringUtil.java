package com.cenrise.utils;

import java.io.ByteArrayInputStream;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.PropertyResourceBundle;
import java.util.Random;
import java.util.StringTokenizer;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

import org.apache.oro.text.perl.Perl5Util;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.io.SAXReader;

import com.cenrise.commons.utils.algorithm.BCConvert;
import com.cenrise.commons.utils.algorithm.StringImpl;

/**
 * 提供些常用的字符串相关的工具方法 按长度分割、字符转码、防敏感字符、防SQL注入、html标签转义、Email 数字
 * 汉字验证、编码转换、字符替换、日期字符转换等等
 */
public class StringUtil {

	private static Random randGen = null;
	private static Object initLock = new Object();
	private static char[] numbersAndLetters = null;

	public static final StringFormatConstants LOWER_CASE = StringFormatConstants.LOWER_CASE;// 小写常量

	public static final StringFormatConstants UPPER_CASE = StringFormatConstants.UPPER_CASE;// 大写常量
	/** 加权因子 */
	@SuppressWarnings("unused")
	private static final int[] weight = new int[] { 7, 9, 10, 5, 8, 4, 2, 1, 6,
			3, 7, 9, 10, 5, 8, 4, 2, 1 };
	// 过滤通过页面表单提交的字符
	private static String[][] FilterChars = { { "<", "&lt;" }, { ">", "&gt;" },
			{ " ", "&nbsp;" }, { "\"", "&quot;" }, { "&", "&amp;" },
			{ "/", "&#47;" }, { "\\", "&#92;" }, { "\n", "<br>" } };
	// 过滤通过javascript脚本处理并提交的字符
	private static String[][] FilterScriptChars = { { "\n", "\'+\'\\n\'+\'" },
			{ "\r", " " }, { "\\", "\'+\'\\\\\'+\'" },
			{ "\'", "\'+\'\\\'\'+\'" } };

	// 字符串处理常量枚举
	private enum StringFormatConstants {
		// 小写
		LOWER_CASE,
		// 大写
		UPPER_CASE;
	}

	/**
	 * 判断是否是空字符串 null和"" 都返回 true
	 *
	 * @param str
	 *            判断的字符串
	 * @return 是否有效
	 */
	public static boolean isEmpty(String str) {
		return str == null || str.equals("");
	}

	/**
	 * 把string array or list用给定的符号symbol连接成一个字符串
	 *
	 * @param list
	 *            需要处理的列表
	 * @param symbol
	 *            链接的符号
	 * @return 处理后的字符串
	 */
	public static String joinString(List list, String symbol) {
		String result = "";
		if (list != null) {
			for (Object o : list) {
				String temp = o.toString();
				if (temp.trim().length() > 0)
					result += (temp + symbol);
			}
			if (result.length() > 1) {
				result = result.substring(0, result.length() - 1);
			}
		}
		return result;
	}

	/**
	 * 判定第一个字符串是否等于的第二个字符串中的某一个值
	 *
	 * @param str1
	 *            测试的字符串
	 * @param str2
	 *            字符串数组(用,分割)
	 * @return 是否包含
	 */
	public static boolean requals(String str1, String str2) {
		if (str1 != null && str2 != null) {
			str2 = str2.replaceAll("\\s*", "");
			String[] arr = str2.split(",");
			for (String t : arr) {
				if (t.equals(str1.trim())) {
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * 判定第一个字符串是否等于的第二个字符串中的某一个值
	 *
	 * @param str1
	 *            测试的字符串
	 * @param str2
	 *            字符串数组
	 * @param split
	 *            str2字符串的分隔符
	 * @return 是否包含
	 */
	public static boolean requals(String str1, String str2, String split) {
		if (str1 != null && str2 != null) {
			str2 = str2.replaceAll("\\s*", "");
			String[] arr = str2.split(split);
			for (String t : arr) {
				if (t.equals(str1.trim())) {
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * 字符串省略截取 字符串截取到指定长度size+...的形式
	 *
	 * @param subject
	 *            需要处理的字符串
	 * @param size
	 *            截取的长度
	 * @return 处理后的字符串
	 */
	public static String subStringOmit(String subject, int size) {
		if (subject != null && subject.length() > size) {
			subject = subject.substring(0, size) + "...";
		}
		return subject;
	}

	/**
	 * 截取字符串　超出的字符用symbol代替
	 *
	 * @param str
	 *            需要处理的字符串
	 * @param len
	 *            字符串长度
	 * @param symbol
	 *            最后拼接的字符串
	 * @return 测试后的字符串
	 */
	public static String subStringSymbol(String str, int len, String symbol) {
		String temp = "";
		if (str != null && str.length() > len) {
			temp = str.substring(0, len) + symbol;
		}
		return temp;
	}

	/**
	 * 把string array or list用给定的符号symbol连接成一个字符串
	 *
	 * @param array
	 *            需要处理的字符串数组
	 * @param symbol
	 *            链接的符号
	 * @return 处理后的字符串
	 */
	public static String joinString(String[] array, String symbol) {
		String result = "";
		if (array != null) {
			for (String temp : array) {
				if (temp != null && temp.trim().length() > 0)
					result += (temp + symbol);
			}
			if (result.length() > 1 && ValidUtil.isValid(symbol)) {
				result = result.substring(0, result.length() - symbol.length());
			}
		}
		return result;
	}

	/**
	 * 把array or list用给定的符号symbol连接成一个字符串
	 *
	 * @param array
	 *            需要处理的字符串数组
	 * @param symbol
	 *            链接的符号
	 * @return 处理后的字符串
	 */
	public static String joinString(Object[] array, String symbol) {
		String result = "";
		if (array != null) {
			for (Object temp : array) {
				result += (temp.toString() + symbol);
			}
			if (result.length() > 1 && ValidUtil.isValid(symbol)) {
				result = result.substring(0, result.length() - symbol.length());
			}
		}
		return result;
	}

	/**
	 * 隐藏邮件地址前缀。
	 *
	 * @param email
	 *            - EMail邮箱地址 例如: ssss@koubei.com 等等...
	 * @return 返回已隐藏前缀邮件地址, 如 *********@koubei.com.
	 */
	public static String getHideEmailPrefix(String email) {
		if (null != email) {
			int index = email.lastIndexOf('@');
			if (index > 0) {
				email = repeat("*", index).concat(email.substring(index));
			}
		}
		return email;
	}

	/**
	 * repeat - 通过源字符串重复生成N次组成新的字符串。
	 *
	 * @param src
	 *            - 源字符串 例如: 空格(" "), 星号("*"), "浙江" 等等...
	 * @param num
	 *            - 重复生成次数
	 * @return 返回已生成的重复字符串
	 */
	public static String repeat(String src, int num) {
		StringBuilder s = new StringBuilder();
		for (int i = 0; i < num; i++)
			s.append(src);
		return s.toString();
	}

	/**
	 * 截取字符串左侧的Num位截取到末尾
	 *
	 * @param str1
	 *            处理的字符串
	 * @param num
	 *            开始位置
	 * @return 截取后的字符串
	 */
	public static String ltrim(String str1, int num) {
		String tt = "";
		if (!isEmpty(str1) && str1.length() >= num) {
			tt = str1.substring(num, str1.length());
		}
		return tt;

	}

	/**
	 * 截取字符串右侧第0位到第Num位
	 *
	 * @param str
	 *            处理的字符串
	 * @param num
	 *            截取的位置
	 * @return 截取后的字符串
	 */
	public static String rtrim(String str, int num) {
		if (!isEmpty(str) && str.length() > num) {
			str = str.substring(0, str.length() - num);
		}
		return str;
	}

	/**
	 * 根据指定的字符把源字符串分割成一个list
	 *
	 * @param src
	 *            处理的字符串
	 * @param pattern
	 *            分割字符串
	 * @return 处理后的list
	 */
	public static List<String> parseString2List(String src, String pattern) {
		List<String> list = new ArrayList<>();
		if (src != null) {
			String[] tt = src.split(pattern);
			list.addAll(Arrays.asList(tt));
		}
		return list;
	}

	/**
	 * 格式化一个float
	 *
	 * @param format
	 *            要格式化成的格式 such as #.00, #.#
	 * @return 格式化后的字符串
	 */
	public static String formatDouble(double f, String format) {
		DecimalFormat df = new DecimalFormat(format);
		return df.format(f);
	}

	/**
	 * 截取字符串左侧指定长度的字符串
	 *
	 * @param input
	 *            输入字符串
	 * @param count
	 *            截取长度
	 * @return 截取字符串
	 */
	public static String left(String input, int count) {
		if (isEmpty(input)) {
			return "";
		}
		count = (count > input.length()) ? input.length() : count;
		return input.substring(0, count);
	}

	/**
	 * 截取字符串右侧指定长度的字符串
	 *
	 * @param input
	 *            输入字符串
	 * @param count
	 *            截取长度
	 * @return 截取字符串 Summary 其他编码的有待测试
	 */
	public static String right(String input, int count) {
		if (isEmpty(input)) {
			return "";
		}
		count = (count > input.length()) ? input.length() : count;
		return input.substring(input.length() - count, input.length());
	}

	/**
	 * 全角字符变半角字符
	 *
	 * @param str
	 *            需要处理的字符串
	 * @return 处理后的字符串
	 */
	public static String full2Half(String str) {
		if (isEmpty(str)) {
			return "";
		}
		return BCConvert.qj2bj(str);
	}

	/**
	 * 半角字符变全角字符
	 * 
	 * @param str
	 *            需要处理的字符串
	 * @return 处理后的字符串
	 */
	public static String Half2Full(String str) {
		if (isEmpty(str)) {
			return "";
		}
		return BCConvert.bj2qj(str);
	}

	/**
	 * 页面中去除字符串中的空格、回车、换行符、制表符
	 *
	 * @param str
	 *            需要处理的字符串
	 */
	public static String replaceBlank(String str) {
		if (str != null) {
			Pattern p = Pattern.compile("\\s*|\t|\r|\n");
			Matcher m = p.matcher(str);
			str = m.replaceAll("");
		}
		return str;
	}

	/**
	 * 判断字符串数组中是否包含某字符串元素
	 *
	 * @param substring
	 *            某字符串
	 * @param source
	 *            源字符串数组
	 * @return 包含则返回true，否则返回false
	 */
	public static boolean isIn(String substring, String[] source) {
		if (isEmpty(substring) || !ValidUtil.isValid(source)) {
			return false;
		}
		for (String t : source) {
			if (substring.equals(t)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 字符串转换unicode.实现native2ascii.exe类似的功能
	 *
	 * @param string
	 *            需要处理的字符串
	 */
	public static String string2Unicode(String string) {
		StringBuilder uni = new StringBuilder();
		for (int i = 0; i < string.length(); i++) {
			String temp = "\\u"
					+ String.valueOf(Integer.toHexString(string.charAt(i)));
			uni.append(temp);
		}
		return uni.toString();
	}

	/**
	 * 转字符串 实现native2ascii.exe类似的功能
	 *
	 * @param unicode
	 *            需要处理的字符串
	 */
	public static String unicode2String(String unicode) {
		StringBuilder str = new StringBuilder();
		String[] hex = unicode.split("\\\\u");
		for (int i = 1; i < hex.length; i++) {
			int data = Integer.parseInt(hex[i], 16);
			str.append((char) data);
		}
		return str.toString();
	}

	/**
	 * 删除所有的标点符号
	 *
	 * @param str
	 *            处理的字符串
	 */
	public static String trimPunct(String str) {
		if (isEmpty(str)) {
			return "";
		}
		return str.replaceAll("[\\pP\\p{Punct}]", "");
	}

	/**
	 * 字符串相似度比较(速度较快)
	 */
	public static double SimilarityRatio(String str1, String str2) {
		str1 = StringUtil.trimPunct(str1);
		str2 = StringUtil.trimPunct(str2);
		if (str1.length() > str2.length()) {
			return StringImpl.SimilarityRatio(str1, str2);
		} else {
			return StringImpl.SimilarityRatio(str2, str1);
		}

	}

	/**
	 * 字符串相似度比较(速度较快)
	 */
	public static double SimilarDegree(String str1, String str2) {
		str1 = StringUtil.trimPunct(str1);
		str2 = StringUtil.trimPunct(str2);
		if (str1.length() > str2.length()) {
			return StringImpl.SimilarDegree(str1, str2);
		} else {
			return StringImpl.SimilarDegree(str2, str1);
		}
	}

	/**
	 * 获取字符串的编码
	 *
	 * @param str
	 *            处理的字符串
	 */
	public static String SimpleEncoding(String str) {
		if (isEmpty(str)) {
			return "";
		}
		return StringImpl.simpleEncoding(str);
	}

	/**
	 * 获取字符串的编码
	 */
	public static String cpDetector(String str) {
		if (isEmpty(str)) {
			return "";
		}
		return StringImpl.encoding(str);
	}

	/**
	 * 获取字符串str在String中出现的次数
	 *
	 * @param string
	 *            处理的字符串
	 * @param str
	 *            子字符串
	 */
	public static int countSubStr(String string, String str) {
		if ((str == null) || (str.length() == 0) || (string == null)
				|| (string.length() == 0)) {
			return 0;
		}
		int count = 0;
		int index = 0;
		while ((index = string.indexOf(str, index)) != -1) {
			count++;
			index += str.length();
		}
		return count;
	}

	/**
	 * 替换一个出现的子串
	 *
	 * @param s
	 *            source string
	 * @param sub
	 *            substring to replace
	 * @param with
	 *            substring to replace with
	 */
	public static String replaceFirst(String s, String sub, String with) {
		int i = s.indexOf(sub);
		if (i == -1) {
			return s;
		}
		return s.substring(0, i) + with + s.substring(i + sub.length());
	}

	/**
	 * 替换最后一次出现的字串 Replaces the very last occurrence of a substring with
	 * supplied string.
	 *
	 * @param s
	 *            source string
	 * @param sub
	 *            substring to replace
	 * @param with
	 *            substring to replace with
	 */
	public static String replaceLast(String s, String sub, String with) {
		int i = s.lastIndexOf(sub);
		if (i == -1) {
			return s;
		}
		return s.substring(0, i) + with + s.substring(i + sub.length());
	}

	/**
	 * 删除所有的子串 Removes all substring occurrences from the string.
	 *
	 * @param s
	 *            source string
	 * @param sub
	 *            substring to remove
	 */
	public static String remove(String s, String sub) {
		int c = 0;
		int sublen = sub.length();
		if (sublen == 0) {
			return s;
		}
		int i = s.indexOf(sub, c);
		if (i == -1) {
			return s;
		}
		StringBuilder sb = new StringBuilder(s.length());
		do {
			sb.append(s.substring(c, i));
			c = i + sublen;
		} while ((i = s.indexOf(sub, c)) != -1);
		if (c < s.length()) {
			sb.append(s.substring(c, s.length()));
		}
		return sb.toString();
	}

	/**
	 * 将字符串首字母转大写
	 * 
	 * @param str
	 *            需要处理的字符串
	 */
	public static String upperFirstChar(String str) {
		if (isEmpty(str)) {
			return "";
		}
		char[] cs = str.toCharArray();
		if ((cs[0] >= 'a') && (cs[0] <= 'z')) {
			cs[0] -= (char) 0x20;
		}
		return String.valueOf(cs);
	}

	/**
	 * 将字符串首字母转小写
	 * 
	 * @param str
	 * @return
	 */
	public static String lowerFirstChar(String str) {
		if (isEmpty(str)) {
			return "";
		}
		char[] cs = str.toCharArray();
		if ((cs[0] >= 'A') && (cs[0] <= 'Z')) {
			cs[0] += (char) 0x20;
		}
		return String.valueOf(cs);
	}

	/**
	 * 去除字符串首尾出现的某个字符.
	 * 
	 * @param source
	 *            源字符串.
	 * @param element
	 *            需要去除的字符.
	 * @return String.
	 */
	public static String trimFirstAndLastChar(String source, char element) {
		boolean beginIndexFlag = true;
		boolean endIndexFlag = true;
		do {
			int beginIndex = source.indexOf(element) == 0 ? 1 : 0;
			int endIndex = source.lastIndexOf(element) + 1 == source.length() ? source
					.lastIndexOf(element) : source.length();
			source = source.substring(beginIndex, endIndex);
			beginIndexFlag = (source.indexOf(element) == 0);
			endIndexFlag = (source.lastIndexOf(element) + 1 == source.length());
		} while (beginIndexFlag || endIndexFlag);
		return source;
	}

	/**
	 * 去掉左右空格后字符串是否为空.
	 * 
	 * @param astr
	 *            String
	 * @return boolean
	 */
	public static boolean isTrimEmpty(String astr) {
		if ((null == astr) || (astr.length() == 0)) {
			return true;
		}
		if (isBlank(trim(astr))) {
			return true;
		}
		return false;
	}

	/**
	 * 字符串是否为空:null或者长度为0.
	 * 
	 * @param astr
	 *            源字符串.
	 * @return boolean
	 */
	public static boolean isBlank(String astr) {
		return ((null == astr) || (astr.length() == 0));
	}

	/**
	 * 得到字符串中某个字符的个数
	 * 
	 * @param str
	 *            字符串
	 * @param c
	 *            字符
	 * @return
	 */
	public static final int getCharnum(String str, char c) {
		int num = 0;
		char[] chars = str.toCharArray();
		for (int i = 0; i < chars.length; i++) {
			if (c == chars[i]) {
				num++;
			}
		}

		return num;
	}

	/**
	 * 返回yyyymm
	 * 
	 * @param aDate
	 * @return
	 */
	public static final String getYear_Month(Date aDate) {
		SimpleDateFormat df = null;
		String returnValue = "";

		if (aDate != null) {
			df = new SimpleDateFormat("yyyyMM");
			returnValue = df.format(aDate);
		}

		return (returnValue);
	}

	/**
	 * hxw 返回当前年
	 * 
	 * @return
	 */
	public static String getYear() {
		Calendar calendar = Calendar.getInstance();
		return String.valueOf(calendar.get(1));
	}

	/**
	 * hxw 返回当前月
	 * 
	 * @return
	 */
	public static String getMonth() {
		Calendar calendar = Calendar.getInstance();
		String temp = String.valueOf(calendar.get(2) + 1);
		if (temp.length() < 2)
			temp = "0" + temp;
		return temp;
	}

	/**
	 * 按长度分割字符串
	 * 
	 * @param content
	 * @param len
	 * @return
	 */
	public static String[] split(String content, int len) {
		if (content == null || content.equals("")) {
			return null;
		}
		int len2 = content.length();
		if (len2 <= len) {
			return new String[] { content };
		} else {
			int i = len2 / len + 1;
			System.out.println("i:" + i);
			String[] strA = new String[i];
			int j = 0;
			int begin = 0;
			int end = 0;
			while (j < i) {
				begin = j * len;
				end = (j + 1) * len;
				if (end > len2)
					end = len2;
				strA[j] = content.substring(begin, end);
				// System.out.println(strA[j]+"<br/>");
				j = j + 1;
			}
			return strA;
		}
	}

	/**
	 * Email格式化
	 * 
	 * @param email
	 * @return
	 */
	public static boolean emailFormat(String email) {
		boolean tag = true;
		final String pattern1 = "^([a-z0-9A-Z]+[-|_|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";
		final Pattern pattern = Pattern.compile(pattern1);
		final Matcher mat = pattern.matcher(email);
		if (!mat.find()) {
			tag = false;
		}
		return tag;
	}

	/**
	 * 验证是不是EMAIL
	 * 
	 * @param email
	 * @return
	 */
	public static boolean isEmail(String email) {
		boolean retval = false;
		String check = "^([a-z0-9A-Z]+[-|_|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";
		Pattern regex = Pattern.compile(check);
		Matcher matcher = regex.matcher(email);
		retval = matcher.matches();
		return retval;
	}

	/**
	 * 验证是不是Email
	 * 
	 * @param str
	 * @return
	 */
	public static boolean isEmail2(String str) {
		Pattern pattern = Pattern
				.compile("\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*");
		Matcher isNum = pattern.matcher(str);
		if (!isNum.matches()) {
			return false;
		}
		return true;
	}

	/**
	 * 验证汉字为true
	 * 
	 * @param s
	 * @return
	 */
	public static boolean isLetterorDigit(String s) {
		if (s.equals("") || s == null) {
			return false;
		}
		for (int i = 0; i < s.length(); i++) {
			if (!Character.isLetterOrDigit(s.charAt(i))) {
				// if (!Character.isLetter(s.charAt(i))){
				return false;
			}
		}
		// Character.isJavaLetter()
		return true;
	}

	/**
	 * 判断某字符串是否为null，如果长度大于256，则返回256长度的子字符串，反之返回原串
	 * 
	 * @param str
	 * @return
	 */
	public static String checkStr(String str) {
		if (str == null) {
			return "";
		} else if (str.length() > 256) {
			return str.substring(256);
		} else {
			return str;
		}
	}

	/**
	 * 验证是不是Int validate a int string
	 * 
	 * @param str
	 *            the Integer string.
	 * @return true if the str is invalid otherwise false.
	 */
	public static boolean validateInt(String str) {
		if (str == null || str.trim().equals(""))
			return false;

		char c;
		for (int i = 0, l = str.length(); i < l; i++) {
			c = str.charAt(i);
			if (!((c >= '0') && (c <= '9')))
				return false;
		}

		return true;
	}

	/**
	 * 字节码转换成16进制字符串 内部调试使用
	 * 
	 * @param b
	 * @return
	 */
	public static String byte2hex(byte[] b) {
		String hs = "";
		String stmp = "";
		for (int n = 0; n < b.length; n++) {
			stmp = (java.lang.Integer.toHexString(b[n] & 0XFF));
			if (stmp.length() == 1)
				hs = hs + "0" + stmp;
			else
				hs = hs + stmp;
			if (n < b.length - 1)
				hs = hs + ":";
		}
		return hs.toUpperCase();
	}

	/**
	 * 字节码转换成自定义字符串 内部调试使用
	 * 
	 * @param b
	 * @return
	 */
	public static String byte2string(byte[] b) {
		String hs = "";
		String stmp = "";
		for (int n = 0; n < b.length; n++) {
			stmp = (java.lang.Integer.toHexString(b[n] & 0XFF));
			if (stmp.length() == 1)
				hs = hs + "0" + stmp;
			else
				hs = hs + stmp;
			// if (n<b.length-1) hs=hs+":";
		}
		return hs.toUpperCase();
	}

	/**
	 * 字符串转换成字节码 内部调试使用
	 * 
	 * @param b
	 * @return
	 */
	public static byte[] string2byte(String hs) {
		byte[] b = new byte[hs.length() / 2];
		for (int i = 0; i < hs.length(); i = i + 2) {
			String sub = hs.substring(i, i + 2);
			byte bb = new Integer(Integer.parseInt(sub, 16)).byteValue();
			b[i / 2] = bb;
		}
		return b;
	}

	/**
	 * 验证字符串是否为空
	 * 
	 * @param param
	 * @return
	 */
	public static boolean empty(String param) {
		return param == null || param.trim().length() < 1;
	}

	/**
	 * 验证英文字母或数据
	 * 
	 * @param str
	 * @return
	 */
	public static boolean isLetterOrDigit(String str) {
		java.util.regex.Pattern p = null; // 正则表达式
		java.util.regex.Matcher m = null; // 操作的字符串
		boolean value = true;
		try {
			p = java.util.regex.Pattern.compile("[^0-9A-Za-z]");
			m = p.matcher(str);
			if (m.find()) {
				value = false;
			}
		} catch (Exception e) {
		}
		return value;
	}

	/**
	 * 验证是否是小写字符串
	 * 
	 * @param str
	 * @return
	 */
	@SuppressWarnings("unused")
	private static boolean isLowerLetter(String str) {
		java.util.regex.Pattern p = null; // 正则表达式
		java.util.regex.Matcher m = null; // 操作的字符串
		boolean value = true;
		try {
			p = java.util.regex.Pattern.compile("[^a-z]");
			m = p.matcher(str);
			if (m.find()) {
				value = false;
			}
		} catch (Exception e) {
		}
		return value;
	}

	/**
	 * 判断一个字符串是否都为数字
	 * 
	 * @param strNum
	 * @return
	 */
	public static boolean isDigit(String strNum) {
		return strNum.matches("[0-9]{1,}");
	}

	/**
	 * 判断一个字符串是否都为数字
	 * 
	 * @param strNum
	 * @return
	 */
	public static boolean isDigit2(String strNum) {
		Pattern pattern = Pattern.compile("[0-9]{1,}");
		Matcher matcher = pattern.matcher((CharSequence) strNum);
		return matcher.matches();
	}

	/**
	 * 截取数字
	 * 
	 * @param content
	 * @return
	 */
	public static String getNumbers(String content) {
		Pattern pattern = Pattern.compile("\\d+");
		Matcher matcher = pattern.matcher(content);
		while (matcher.find()) {
			return matcher.group(0);
		}
		return "";
	}

	/**
	 * 截取非数字
	 * 
	 * @param content
	 * @return
	 */
	public static String splitNotNumber(String content) {
		Pattern pattern = Pattern.compile("\\D+");
		Matcher matcher = pattern.matcher(content);
		while (matcher.find()) {
			return matcher.group(0);
		}
		return "";
	}

	/**
	 * 给字符编码
	 * 
	 * @param str
	 * @param code
	 * @return
	 */
	public static String encode(String str, String code) {
		try {
			return URLEncoder.encode(str, code);
		} catch (Exception ex) {
			ex.fillInStackTrace();
			return "";
		}
	}

	/**
	 * 给字符串解码
	 * 
	 * @param str
	 * @param code
	 * @return
	 */
	public static String decode(String str, String code) {
		try {
			return URLDecoder.decode(str, code);
		} catch (Exception ex) {
			ex.fillInStackTrace();
			return "";
		}
	}

	/**
	 * 字符串去除两边空格
	 * 
	 * @param param
	 * @return
	 */
	public static String nvl(String param) {
		return param != null ? param.trim() : "";
	}

	/**
	 * Integer.parseInt(param)
	 * 
	 * @param param
	 * @param d
	 * @return
	 */
	public static int parseInt(String param, int d) {
		int i = d;
		try {
			i = Integer.parseInt(param);
		} catch (Exception e) {
		}
		return i;
	}

	public static int parseInt(String param) {
		return parseInt(param, 0);
	}

	public static long parseLong(String param) {
		long l = 0L;
		try {
			l = Long.parseLong(param);
		} catch (Exception e) {
		}
		return l;
	}

	public static double parseDouble(String param) {
		return parseDouble(param, 1.0);
	}

	public static double parseDouble(String param, double t) {
		double tmp = 0.0;
		try {
			tmp = Double.parseDouble(param.trim());
		} catch (Exception e) {
			tmp = t;
		}
		return tmp;
	}

	public static boolean parseBoolean(String param) {
		if (empty(param))
			return false;
		switch (param.charAt(0)) {
		case 49: // '1'
		case 84: // 'T'
		case 89: // 'Y'
		case 116: // 't'
		case 121: // 'y'
			return true;

		}
		return false;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static final String[] split(String str, String delims) {
		StringTokenizer st = new StringTokenizer(str, delims);
		ArrayList list = new ArrayList();
		for (; st.hasMoreTokens(); list.add(st.nextToken()))
			;
		return (String[]) list.toArray(new String[list.size()]);
	}

	/**
	 * 获取指定长度的字符
	 * 
	 * @param str
	 * @param length
	 * @return
	 */
	public static String substring(String str, int length) {
		if (str == null)
			return null;

		if (str.length() > length)
			return (str.substring(0, length - 2) + "...");
		else
			return str;
	}

	/**
	 * 验证Double
	 * 
	 * @param str
	 * @return
	 * @throws RuntimeException
	 */
	public static boolean validateDouble(String str) throws RuntimeException {
		if (str == null)
			// throw new RuntimeException("null input");
			return false;
		char c;
		int k = 0;
		for (int i = 0, l = str.length(); i < l; i++) {
			c = str.charAt(i);
			if (!((c >= '0') && (c <= '9')))
				if (!(i == 0 && (c == '-' || c == '+')))
					if (!(c == '.' && i < l - 1 && k < 1))
						// throw new RuntimeException("invalid number");
						return false;
					else
						k++;
		}
		return true;
	}

	/**
	 * 验证手机号
	 * 
	 * @param str
	 * @param includeUnicom
	 * @return
	 */
	public static boolean validateMobile(String str, boolean includeUnicom) {
		if (str == null || str.trim().equals(""))
			return false;
		str = str.trim();

		if (str.length() != 11 || !validateInt(str))
			return false;

		if (includeUnicom
				&& (str.startsWith("130") || str.startsWith("133") || str
						.startsWith("131")))
			return true;

		if (!(str.startsWith("139") || str.startsWith("138")
				|| str.startsWith("137") || str.startsWith("136") || str
					.startsWith("135")))
			return false;
		return true;
	}

	/**
	 * 验证手机
	 * 
	 * @param str
	 * @return
	 */
	public static boolean validateMobile(String str) {
		return validateMobile(str, false);
	}

	/**
	 * 把字符串从gb2312转成ISO8859-1
	 * 
	 * @param s
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	public static String gbToIso(String s) throws UnsupportedEncodingException {
		return covertCode(s, "GB2312", "ISO8859-1");
	}

	/**
	 * 如果s不为空时返回new String(s.getBytes(code1), code2)
	 * 
	 * @param s
	 * @param code1
	 * @param code2
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	public static String covertCode(String s, String code1, String code2)
			throws UnsupportedEncodingException {
		if (s == null)
			return null;
		else if (s.trim().equals(""))
			return "";
		else
			return new String(s.getBytes(code1), code2);
	}

	/**
	 * 转换编码
	 * 
	 * @param s0
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	public static String transCode(String s0)
			throws UnsupportedEncodingException {
		if (s0 == null || s0.trim().equals(""))
			return null;
		else {
			s0 = s0.trim();
			return new String(s0.getBytes("GBK"), "ISO8859-1");
		}
	}

	/**
	 * 编码转换
	 * 
	 * @param s
	 * @return
	 */
	public static String GBToUTF8(String s) {
		try {
			StringBuffer out = new StringBuffer("");
			byte[] bytes = s.getBytes("unicode");
			for (int i = 2; i < bytes.length - 1; i += 2) {
				out.append("\\u");
				String str = Integer.toHexString(bytes[i + 1] & 0xff);
				for (int j = str.length(); j < 2; j++) {
					out.append("0");
				}
				out.append(str);
				String str1 = Integer.toHexString(bytes[i] & 0xff);
				for (int j = str1.length(); j < 2; j++) {
					out.append("0");
				}

				out.append(str1);
			}
			return out.toString();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 替换所有
	 * 
	 * @param obj
	 * @param oldString
	 * @param newString
	 * @return
	 */
	@SuppressWarnings("unused")
	public static final String[] replaceAll(String[] obj, String oldString,
			String newString) {
		if (obj == null) {
			return null;
		}
		int length = obj.length;
		String[] returnStr = new String[length];
		String str = null;
		for (int i = 0; i < length; i++) {
			returnStr[i] = replaceAll(obj[i], oldString, newString);
		}
		return returnStr;
	}

	/**
	 * 字符串全文替换
	 * 
	 * @param s0
	 * @param oldstr
	 * @param newstr
	 * @return
	 */
	public static String replaceAll(String s0, String oldstr, String newstr) {
		if (s0 == null || s0.trim().equals(""))
			return null;
		StringBuffer sb = new StringBuffer(s0);
		int begin = 0;
		// int from = 0;
		begin = s0.indexOf(oldstr);
		while (begin > -1) {
			sb = sb.replace(begin, begin + oldstr.length(), newstr);
			s0 = sb.toString();
			begin = s0.indexOf(oldstr, begin + newstr.length());
		}
		return s0;
	}

	/**
	 * 把字符串转成html
	 * 
	 * @param str
	 * @return
	 */
	public static String toHtml(String str) {
		String html = str;
		if (str == null || str.length() == 0) {
			return str;
		}
		html = replaceAll(html, "&", "&amp;");
		html = replaceAll(html, "<", "&lt;");
		html = replaceAll(html, ">", "&gt;");
		html = replaceAll(html, "\r\n", "\n");
		html = replaceAll(html, "\n", "<br>\n");
		html = replaceAll(html, "\t", "         ");
		html = replaceAll(html, " ", "&nbsp;");
		return html;
	}

	/**
	 * 字符串替换,用dest替换astr里面的Sign.
	 * 
	 * @param str1
	 *            源字符串
	 * @param sign
	 *            要被替换的标记
	 * @param dest
	 *            替换后的标记
	 * @return String
	 */
	public static String replace(String str1, String sign, String dest) {
		String astr = str1;
		if (isBlank(astr)) {
			return "";
		}

		Perl5Util util = new Perl5Util();
		String lastSign = sign.replace('`', ' ');
		// astr = util.substitute("s",astr)
		String lastDest = dest.replace('`', ' ');
		astr = util.substitute("s`" + lastSign + "`" + lastDest + "`g", astr);

		return astr;
	}

	/**
	 * 替换
	 * 
	 * @param line
	 * @param oldString
	 * @param newString
	 * @return
	 */
	public static final String replaceIgnoreCase(String line, String oldString,
			String newString) {
		if (line == null) {
			return null;
		}
		String lcLine = line.toLowerCase();
		String lcOldString = oldString.toLowerCase();
		int i = 0;
		if ((i = lcLine.indexOf(lcOldString, i)) >= 0) {
			char[] line2 = line.toCharArray();
			char[] newString2 = newString.toCharArray();
			int oLength = oldString.length();
			StringBuffer buf = new StringBuffer(line2.length);
			buf.append(line2, 0, i).append(newString2);
			i += oLength;
			int j = i;
			while ((i = lcLine.indexOf(lcOldString, i)) > 0) {
				buf.append(line2, j, i - j).append(newString2);
				i += oLength;
				j = i;
			}
			buf.append(line2, j, line2.length - j);
			return buf.toString();
		}
		return line;
	}

	/**
	 * 标签转义
	 * 
	 * @param input
	 * @return
	 */
	public static final String escapeHTMLTags(String input) {
		// Check if the string is null or zero length -- if so, return
		// what was sent in.
		if (input == null || input.length() == 0) {
			return input;
		}
		// Use a StringBuffer in lieu of String concatenation -- it is
		// much more efficient this way.
		StringBuffer buf = new StringBuffer(input.length());
		char ch = ' ';
		for (int i = 0; i < input.length(); i++) {
			ch = input.charAt(i);
			if (ch == '<') {
				buf.append("&lt;");
			} else if (ch == '>') {
				buf.append("&gt;");
			} else {
				buf.append(ch);
			}
		}
		return buf.toString();
	}

	/**
	 * 随机字符串
	 * 
	 * @param length
	 * @return
	 */
	public static final String randomString(int length) {
		if (length < 1) {
			return null;
		}
		// Init of pseudo random number generator.
		if (randGen == null) {
			synchronized (initLock) {
				if (randGen == null) {
					randGen = new Random();
					// Also initialize the numbersAndLetters array
					numbersAndLetters = ("0123456789abcdefghijklmnopqrstuvwxyz"
							+ "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ")
							.toCharArray();
				}
			}
		}
		// Create a char buffer to put random letters and numbers in.
		char[] randBuffer = new char[length];
		for (int i = 0; i < randBuffer.length; i++) {
			randBuffer[i] = numbersAndLetters[randGen.nextInt(71)];
		}
		return new String(randBuffer);
	}

	/**
	 * 固定长度字符串截取
	 * 
	 * @param content
	 * @param i
	 * @return
	 */
	public static String getSubstring(String content, int i) {
		int varsize = 10;
		String strContent = content;
		if (strContent.length() < varsize + 1) {
			return strContent;
		} else {
			int max = (int) Math.ceil((double) strContent.length() / varsize);
			if (i < max - 1) {
				return strContent.substring(i * varsize, (i + 1) * varsize);
			} else {
				return strContent.substring(i * varsize);
			}
		}
	}

	/**
	 * 日期转String
	 * 
	 * @param pattern
	 * @return
	 */
	public static String now(String pattern) {
		return dateToString(new Date(), pattern);
	}

	public static String dateToString(Date date, String pattern) {
		if (date == null) {
			return "";
		} else {
			SimpleDateFormat sdf = new SimpleDateFormat(pattern);
			return sdf.format(date);
		}
	}

	/**
	 * 当前时间，格式“yyyyMMddHHmmssSSS”
	 * 
	 * @return
	 */
	public static synchronized String getNow() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmssSSS");
		return sdf.format(new Date());
	}

	/**
	 * String转Date
	 * 
	 * @param strDate
	 * @param pattern
	 * @return
	 * @throws ParseException
	 */
	public static java.sql.Date stringToDate(String strDate, String pattern)
			throws ParseException {
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
		Date date = simpleDateFormat.parse(strDate);
		long lngTime = date.getTime();
		return new java.sql.Date(lngTime);
	}

	/**
	 * String转化成日期
	 * 
	 * @param strDate
	 * @param pattern
	 * @return
	 * @throws ParseException
	 */
	public static java.util.Date stringToUtilDate(String strDate, String pattern)
			throws ParseException {
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
		return simpleDateFormat.parse(strDate);
	}

	/**
	 * s输出html
	 * 
	 * @param s
	 * @return
	 */
	public static String formatHTMLOutput(String s) {
		if (s == null)
			return null;

		if (s.trim().equals(""))
			return "";

		String formatStr;
		formatStr = replaceAll(s, " ", "&nbsp;");
		formatStr = replaceAll(formatStr, "\n", "<br />");

		return formatStr;
	}

	/*
	 * 4舍5入 @param num 要调整的数 @param x 要保留的小数位
	 */
	public static double myround(double num, int x) {
		BigDecimal b = new BigDecimal(num);
		return b.setScale(x, BigDecimal.ROUND_HALF_UP).doubleValue();
	}

	/**
	 * 解析
	 * 
	 * @param param
	 * @param d
	 * @return
	 */
	public static int parseLongInt(Long param, int d) {
		int i = d;
		try {
			i = Integer.parseInt(String.valueOf(param));
		} catch (Exception e) {
		}
		return i;
	}

	/**
	 * 解析
	 * 
	 * @param param
	 * @return
	 */
	public static int parseLongInt(Long param) {
		return parseLongInt(param, 0);
	}

	/**
	 * 返回字符串
	 * 
	 * @param obj
	 * @return
	 */
	public static String returnString(Object obj) {
		if (obj == null) {
			return "";
		} else {
			return obj.toString();
		}
	}

	/**
	 * 修改敏感字符编码
	 * 
	 * @param value
	 * @return
	 */
	public static String htmlEncode(String value) {
		String re[][] = { { "<", "&lt;" }, { ">", "&gt;" }, { "\"", "&quot;" },
				{ "\\′", "&acute;" }, { "&", "&amp;" } };

		for (int i = 0; i < 4; i++) {
			value = value.replaceAll(re[i][0], re[i][1]);
		}
		return value;
	}

	/**
	 * 防SQL注入
	 * 
	 * @param str
	 * @return
	 */
	public static boolean sql_inj(String str) {
		String inj_str = "'|and|exec|insert|select|delete|update|count|*|%|chr|mid|master|truncate|char|declare|;|or|-|+|,";
		String inj_stra[] = inj_str.split("|");
		for (int i = 0; i < inj_stra.length; i++) {
			if (str.indexOf(inj_stra[i]) >= 0) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 用指定的字符集编码URL
	 * 
	 * @param url
	 *            要编码的URL
	 * @param charset
	 *            字符集
	 * @return 编码后的URL
	 */
	public static String encodeURL(String url, String charset) {
		if (url != null && url.length() > 0) {
			try {
				return URLEncoder.encode(url, charset);
			} catch (UnsupportedEncodingException ex) {
				return url;
			}
		}
		return url;
	}

	/**
	 * 以指定的字符编码解析字符串的长度
	 * 
	 * @param txt
	 *            要解析的字符串
	 * @param charset
	 *            编码
	 * @return 字符串的长度
	 */
	public static int getStrLength(String txt, String charset) {
		try {
			return txt.getBytes(charset).length;
		} catch (UnsupportedEncodingException ex) {
			return txt.length();
		}
	}

	/**
	 * 去掉指定字符串的首尾特殊字符
	 * 
	 * @param source
	 *            指定字符串
	 * @param beTrim
	 *            要去除的特殊字符
	 * @return 去掉特殊字符后的字符串
	 */
	public static String trimStringWithAppointedChar(String source,
			String beTrim) {
		if (!source.equalsIgnoreCase("")) {
			// 循环去掉字符串首的beTrim字符
			String beginChar = source.substring(0, 1);
			while (beginChar.equalsIgnoreCase(beTrim)) {
				source = source.substring(1, source.length());
				beginChar = source.substring(0, 1);
			}

			// 循环去掉字符串尾的beTrim字符
			String endChar = source.substring(source.length() - 1,
					source.length());
			while (endChar.equalsIgnoreCase(beTrim)) {
				source = source.substring(0, source.length() - 1);
				endChar = source
						.substring(source.length() - 1, source.length());
			}
		}
		return source;
	}

	/**
	 * 去掉指定字符串的首尾特殊字符
	 * 
	 * @param source
	 *            指定字符串
	 * @param beTrim
	 *            要去除的特殊字符
	 * @param endTrim
	 *            要去的特殊字符
	 * @return 去掉特殊字符后的字符串
	 */
	public static String trimStringWithAppointedChar(String source,
			String beTrim, String endTrim) {
		if (!source.equalsIgnoreCase("")) {
			// 循环去掉字符串首的beTrim字符
			String beginChar = source.substring(0, 2);
			while (beginChar.equalsIgnoreCase(beTrim)) {
				source = source.substring(2, source.length());
				beginChar = source.substring(0, 2);
			}

			// 循环去掉字符串尾的beTrim字符
			String endChar = source.substring(source.length() - 1,
					source.length());
			while (endChar.equalsIgnoreCase(endTrim)) {
				source = source.substring(0, source.length() - 1);
				endChar = source
						.substring(source.length() - 1, source.length());
			}
		}
		return source;
	}

	/**
	 * 用sign分隔字符串data(不判断data是否带有双引号的)
	 * 
	 * @param data
	 *            要拆分的字符串
	 * @param sign
	 *            分隔符
	 * @return list 分隔后的List
	 */
	public static List<String> spit(String data, String sign) {
		StringTokenizer stkzer = new StringTokenizer(data, sign);
		String temp = null;
		List<String> list = new ArrayList<String>();
		while (stkzer.hasMoreTokens()) {
			temp = stkzer.nextToken();
			list.add(temp);
		}
		return list;
	}

	/**
	 * 用sign分隔字符串data(data是带有双引号的) 如:"system_id","type","command_line",
	 * 
	 * @param data
	 *            要拆分的字符串
	 * @param sign
	 *            分隔符
	 * @return list 分隔后的List
	 */
	public static List<String> spitWithQuotationMark(String data, String sign) {

		List<String> keysWithQuotationMark = new ArrayList<String>();

		String[] tempData = data.split(sign);
		for (int i = 0; i < tempData.length; i++) {
			keysWithQuotationMark.add(tempData[i]);
		}

		List<String> keys = new ArrayList<String>();
		// 此时得到的key值列表还是带有双引号的，下边的循环把双引号去掉
		for (int i = 0; i < keysWithQuotationMark.size(); i++) {
			String eachKey = (String) keysWithQuotationMark.get(i);
			String key = null;
			if (eachKey.length() != 0
					&& eachKey.substring(0, 1).equalsIgnoreCase("\"")) {
				eachKey = eachKey.substring(1, eachKey.length());
			}
			if (eachKey.length() != 0
					&& eachKey.substring(eachKey.length() - 1)
							.equalsIgnoreCase("\"")) {
				eachKey = eachKey.substring(0, eachKey.length() - 1);
			}
			key = eachKey;

			keys.add(key);
		}
		return keys;
	}

	/**
	 * 判断字符串为null或者为""
	 * 
	 * @param value
	 *            要判断的字符串
	 * @return 是否为null或者为""
	 */
	public static boolean isNullorBlank(String value) {
		return null == value || "".equals(value);
	}

	/**
	 * 去掉指定字符串两端的空格
	 * 
	 * @param value
	 *            指定的字符串
	 * @return 去掉两端空格后的字符串。如果传入的指定字符串是null，返回""。
	 */
	public static String trim(String value) {
		if (value == null) {
			return "";
		} else {
			return value.trim();
		}
	}

	/**
	 * 将指定字符串的两端加上单引号"'"
	 * 
	 * @param value
	 *            指定的字符串
	 * 
	 * @return 加过单引号的字符串，如果传入的字符串是null，返回null。
	 */
	public static String sem(String value) {
		if (value == null) {
			return null;
		} else {
			return "'" + value + "'";

		}
	}

	/**
	 * 将指定的数字转化为指定长度的字符串，多余部分用"#"填充。例如：intToStrWithSharp(1000, 6)->"##1000"
	 * 
	 * @param value
	 *            要转换的整数
	 * @param length
	 *            转换后的字符串长度
	 * @return 转换后的字符串，如果指定的长度小于整数的位数，则只返回数字。例如：intToStrWithSharp(1000,
	 *         2)->"1000"
	 */
	public static String intToStrWithSharp(Integer value, int length) {
		int valueLength = value.toString().length();
		int diff = length - valueLength;

		if (value.intValue() < Integer.MAX_VALUE) {
			StringBuffer sb = new StringBuffer();
			for (int i = 0; i < diff; i++) {
				sb.append('#');
			}
			sb.append(value.intValue());
			return sb.toString();
		} else {
			return "-1";
		}
	}

	/**
	 * 判断一个String对象是否为null，为null返回""，否则返回str自身。
	 * 
	 * @param str
	 *            要判断的String对象
	 * @return str自身或""
	 */
	public static String getEmptyStringIfNull(String str) {
		if (str == null)
			return "";
		return str;
	}

	/**
	 * 将一个byte数组转换为字符串
	 * 
	 * @param arr
	 *            要转换的byte数组
	 * @return 转换好的字符串，如果数组的length=0，则返回""。
	 */
	public static String bytetoString(byte[] arr) {
		String str = "";
		String tempStr = "";
		for (int i = 1; i < arr.length; i++) {
			tempStr = (Integer.toHexString(arr[i] & 0xff));
			if (tempStr.length() == 1) {
				str = str + "0" + tempStr;
			} else {
				str = str + tempStr;
			}
		}
		return str;
	}

	/**
	 * 分析字符串得到Integer.
	 * 
	 * @param str1
	 *            String
	 * @return Integer
	 */
	public static Integer myparseIntObj(String str1) {
		try {
			if (isBlank(str1)) {
				return null;
			} else {
				// 16进制
				if (str1.startsWith("0x")) {
					String sLast = str1.substring(2);
					return Integer.valueOf(sLast, 16);
				} else {
					return Integer.valueOf(str1);
				}
			}
		} catch (NumberFormatException e) {
			return null;
		}
	}

	/**
	 * 分析一个字符串,得到一个整数,如果错误,设置为缺省值-1.
	 * 
	 * @param str1
	 *            String
	 * @return int
	 */
	public static int myparseInt(String str1) {
		return myparseInt(str1, -1);
	}

	/**
	 * 分析一个字符串,得到一个整数,如果错误,设置为缺省值. 如果一个字符串以0x开头,则认为是16进制的.
	 * 
	 * @param str1
	 *            字符串
	 * @param nDefault
	 *            缺省值
	 * @return int
	 */
	public static int myparseInt(String str1, int nDefault) {
		int result;
		try {
			if (isBlank(str1)) {
				result = nDefault;
			} else {
				// 16进制
				if (str1.startsWith("0x")) {
					String sLast = str1.substring(2);
					result = Integer.parseInt(sLast, 16);
				} else {
					result = Integer.parseInt(str1);
				}
			}
		} catch (NumberFormatException e) {
			result = nDefault;
		}
		return result;
	}

	/**
	 * 分析一个字符串得到float,如果错误,设置一个缺省值-1.
	 * 
	 * @param str1
	 *            String
	 * @return float
	 */
	public static float myparseFloat(String str1) {
		return myparseFloat(str1, -1);
	}

	/**
	 * 分析一个字符串得到float,如果错误,设置一个缺省值.
	 * 
	 * @param str1
	 *            String
	 * @param nDefault
	 *            缺省值
	 * @return float
	 */
	public static float myparseFloat(String str1, float nDefault) {
		float result;
		try {
			result = isBlank(str1) ? nDefault : Float.parseFloat(str1);
		} catch (NumberFormatException e) {
			result = nDefault;
		}
		return result;
	}

	/**
	 * 分析一个字符串得到Float,如果错误,返回null.
	 * 
	 * @param str1
	 *            String
	 * @return Float(may be null)
	 */
	public static Float myparseFloatObj(String str1) {
		try {
			if (isBlank(str1)) {
				return null;
			} else {
				return Float.valueOf(str1);
			}
		} catch (NumberFormatException e) {
			return null;
		}
	}

	/**
	 * 分析一个字符串得到long,如果错误,设置一个缺省值 -1.
	 * 
	 * @param str1
	 *            String
	 * @return long
	 */
	public static long myparseLong(String str1) {
		return myparseLong(str1, -1);
	}

	/**
	 * 分析一个字符串得到long,如果错误,设置一个缺省值 .
	 * 
	 * @param str1
	 *            字符串
	 * @param nDefault
	 *            缺省值
	 * @return long
	 */
	public static long myparseLong(String str1, long nDefault) {
		long result;
		try {
			result = isBlank(str1) ? nDefault : Long.parseLong(str1);
		} catch (NumberFormatException e) {
			result = nDefault;
		}
		return result;
	}

	/**
	 * 分析一个字符串得到Long,如果错误,返回null .
	 * 
	 * @param str1
	 *            字符串
	 * @return Long
	 */
	public static Long myparseLongObj(String str1) {
		try {
			if (isBlank(str1)) {
				return null;
			} else {
				// 16进制
				if (str1.startsWith("0x")) {
					String sLast = str1.substring(2);
					return Long.valueOf(sLast, 16);
				} else {
					return Long.valueOf(str1);
				}
			}
		} catch (NumberFormatException e) {
			return null;
		}
	}

	/**
	 * 为显示/编辑而转换串值，将空对象转换为空串.
	 * 
	 * @param astr
	 *            字符串的值
	 * @return 如果字符串为空,则返回空串(不是null),如果不空,原样返回
	 */
	public static String getShowStr(String astr) {
		return (null == astr) ? "" : astr;
	}

	/**
	 * 返回一个字符串的n次组合后的字符串.
	 * 
	 * @param sStr
	 *            原字符串
	 * @param nRate
	 *            次数
	 * @return 组合好的字符串
	 */
	public static String getManyStr(String sStr, int nRate) {
		StringBuffer strBF = new StringBuffer();
		for (int i = 0; i < nRate; i++) {
			strBF.append(sStr);
		}
		return strBF.toString();
	}

	/**
	 * 格式化数字:返回定长的字符串.
	 * 
	 * @param aNum
	 *            格式化的数字
	 * @param aLength
	 *            长度
	 * @return 格式化好的字符串.
	 */
	public static String formatNumber(int aNum, int aLength) {
		String sNum = Integer.toString(aNum);

		int nLength = aLength - sNum.length();
		if (nLength < 1) {
			return sNum;
		}

		for (int i = 1; i <= nLength; i++) {
			sNum = "0" + sNum;
		}
		return sNum;
	}

	/**
	 * 根据格式输出浮点数的字符串.
	 * 
	 * @param aFloat
	 *            浮点数
	 * @param nSyo
	 *            字符串格式,参考NumberFormat的说明.
	 * @return String
	 */
	public static String getShowFloat(float aFloat, String nSyo) {
		NumberFormat astr = NumberFormat.getInstance();
		((DecimalFormat) astr).applyPattern(nSyo);

		return astr.format(aFloat);
	}

	/**
	 * 从属性里面读取一个字符串出来,如果空,返回缺省值.
	 * 
	 * @param aPROP
	 *            属性句柄
	 * @param itemName
	 *            属性名称
	 * @param sDefault
	 *            缺省值
	 * @return String
	 */
	public static String getPROPString(PropertyResourceBundle aPROP,
			String itemName, String sDefault) {
		String aValue = "";
		try {
			if (null != aPROP) {
				aValue = aPROP.getString(itemName);
			}
		} catch (MissingResourceException e) {
			// donothing
		} catch (ClassCastException e) {
			// donothing
		}

		if (isTrimEmpty(aValue)) {
			aValue = sDefault;
		}
		return aValue;
	}

	/**
	 * 从属性里面读取一个字符串出来,如果空,返回"".
	 * 
	 * @param aPROP
	 *            属性句柄
	 * @param itemName
	 *            属性名称
	 * @return String
	 */
	public static String getPROPString(PropertyResourceBundle aPROP,
			String itemName) {
		return getPROPString(aPROP, itemName, "");
	}

	/**
	 * 翻译一个字符串到目标编码.
	 * 
	 * 如果缺省编码为空,则设置缺省编码为源编码.
	 * 
	 * @param aStr
	 *            源字符串
	 * @param sDefaultEncode
	 *            缺省编码
	 * @param srcCharSet
	 *            源编码
	 * @param destCharSet
	 *            目标编码
	 * @return 编码后的字符串
	 */
	public static String getEXTCHARSETString(String aStr,
			String sDefaultEncode, String srcCharSet, String destCharSet) {
		String lastDefaultEncode = sDefaultEncode;
		String strTemp = null;

		try {
			strTemp = aStr;

			if (isBlank(lastDefaultEncode)) {
				lastDefaultEncode = srcCharSet;
			}
			// 如果源字符集不等于目标字符集
			if (!(sDefaultEncode.equalsIgnoreCase(destCharSet))) {
				if (strTemp != null) {
					if (isTrimEmpty(lastDefaultEncode)) {
						strTemp = new String(strTemp.getBytes(), destCharSet);
					} else {
						strTemp = new String(
								strTemp.getBytes(lastDefaultEncode),
								destCharSet);
					}
				}
			}
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}

		return (strTemp == null) ? "" : strTemp;
	}

	/**
	 * 对编过码的字符串进行解码.
	 * 
	 * @param astr
	 *            String
	 * @param encoding
	 *            编码
	 * @return String
	 */
	/*
	 * @SuppressWarnings("deprecation") public static String urlDecode(String
	 * astr, String encoding) { if (isBlank(astr)) { return ""; } String aRes =
	 * astr; try { if (SysInfo.isJavaVersion14()) { aRes =
	 * URLDecoder.decode(astr, encoding); } else { aRes =
	 * URLDecoder.decode(astr); } } catch (Exception e) { e.printStackTrace(); }
	 * 
	 * return aRes; }
	 */

	/**
	 * 对字符串进行url编码.
	 * 
	 * 1.3和1.4不同,用于GBK环境. 如果切换到1.4中在考虑增加一个函数.
	 * 
	 * @param astr
	 *            源字符串
	 * @param encoding
	 *            编码
	 * @return String
	 */
	/*
	 * @SuppressWarnings("deprecation") public static String urlEncode(String
	 * astr, String encoding) { if (isBlank(astr)) { return ""; } String aRes =
	 * astr; try { if (SysInfo.isJavaVersion14()) { aRes =
	 * URLEncoder.encode(astr, encoding); } else { aRes =
	 * URLEncoder.encode(astr); } } catch (Exception e) { e.printStackTrace(); }
	 * 
	 * return aRes; }
	 */

	/**
	 * 按照规则拆分字符串到字符串数组中.
	 * 
	 * @param pattern
	 *            拆分规则,格式为: /,/
	 * @param aStr
	 *            要拆分的字符串
	 * @return 拆分后的字符串数组
	 */
	public static String[] splitString(String pattern, String aStr) {
		List<Object> alist = new ArrayList<Object>();
		Perl5Util util = new Perl5Util();
		util.split(alist, pattern, aStr);
		String[] astrs = new String[alist.size()];
		alist.toArray(astrs);
		return astrs;
	}

	/**
	 * 按照规则拆分字符串到字符串数组中.
	 * 
	 * @param pattern
	 *            拆分规则,格式为: /,/
	 * @param aStr
	 *            要拆分的字符串
	 * @param nLimit
	 *            拆分次数
	 * @return 拆分后的字符串数组
	 */
	public static String[] splitString(String pattern, String aStr, int nLimit) {
		List<Object> alist = new ArrayList<Object>();
		Perl5Util util = new Perl5Util();
		util.split(alist, pattern, aStr, nLimit);
		String[] astrs = new String[alist.size()];
		alist.toArray(astrs);
		return astrs;
	}

	/**
	 * 拆分字符串到数组列表中.
	 * 
	 * @param pattern
	 *            拆分规则
	 * @param aStr
	 *            要拆分的字符串
	 * @return 拆分后的数组列表
	 */
	@SuppressWarnings("unchecked")
	public static List splitStrToList(String pattern, String aStr) {
		List alist = new ArrayList();
		Perl5Util util = new Perl5Util();
		util.split(alist, pattern, aStr);
		return alist;
	}

	/**
	 * 拆分字符串为一维字符串数组
	 * 
	 * @param str
	 *            要拆分的字符串
	 * @param sign
	 *            字符串拆分的标识符
	 * @return strData 拆分后的一维字符串数组
	 */
	public static String[] splitStrToArray(String str, String sign) {
		String[] strData = null;
		StringTokenizer st1 = new StringTokenizer(str, sign);
		strData = new String[st1.countTokens()];
		int i = 0;
		while (st1.hasMoreTokens()) {
			strData[i] = st1.nextToken().trim();
			i++;
		}
		return strData;
	}

	/**
	 * 截取一定长度的字符串,根据指定的编码来判断长度. 例如指定编码为GBK,则一个汉字为2个字符长度
	 * 
	 * @param astr
	 *            String
	 * @param nlength
	 *            int
	 * @param destEncode
	 *            String
	 * @return String
	 */
	public static String msubstr(String astr, int nlength, String destEncode) {
		byte[] mybytes;
		try {
			mybytes = astr.getBytes(destEncode);

			if (mybytes.length <= nlength) {
				return astr;
			}
			if (nlength < 1) {
				return "";
			}

			for (int i = 0; i < astr.length(); i++) {
				String aTestStr = astr.substring(0, i + 1);
				int nDestLength = aTestStr.getBytes(destEncode).length;
				if (nDestLength > nlength) {
					if (i == 0) {
						return "";
					} else {
						return astr.substring(0, i);
					}
				}
			}

			return astr;
		} catch (java.io.UnsupportedEncodingException e) {
			return "";
		}
	}

	/**
	 * 返回带省略标记的截断的字符串.
	 * 
	 * @param astr
	 *            源字符串
	 * @param nlength
	 *            截断的长度
	 * @param aDot
	 *            后缀
	 * @param encoding
	 *            编码
	 * @return String
	 */
	public static String getDotMsubstr(String astr, int nlength, String aDot,
			String encoding) {
		byte[] mybytes = astr.getBytes();

		// if not long enough,return old string
		if (mybytes.length <= nlength) {
			return astr;
		}

		int nLastLen = nlength - aDot.length();

		if (nLastLen < 1) {
			nLastLen = 1;

		}
		return msubstr(astr, nLastLen, encoding) + aDot;

	}

	/**
	 * 得到字符串的字符长度 按照指定编码测定.
	 * 
	 * @param astr
	 *            String
	 * 
	 * @param sDestEncode
	 *            String
	 * 
	 * @return int 返回字符长度
	 */
	public static int mlength(String astr, String sDestEncode) {
		try {
			return astr.getBytes(sDestEncode).length;
		} catch (java.io.UnsupportedEncodingException e) {
			return astr.getBytes().length;
		}
	}

	/**
	 * 连接2个字符串.
	 * 
	 * @param aOriStr
	 *            源字符串
	 * @param aLinkSign
	 *            连接标记
	 * @param aLinkStr
	 *            要连接的字符串
	 * @return String
	 */
	public static String link2Str(String aOriStr, String aLinkSign,
			String aLinkStr) {
		if (isBlank(aOriStr)) {
			return aLinkStr;
		} else {
			return aOriStr + aLinkSign + aLinkStr;
		}
	}

	/**
	 * 连接字符串数组.
	 * 
	 * @param astrBf
	 *            StringBuffer
	 * @param aryStr
	 *            String[]
	 * @return StringBuffer
	 */
	public static StringBuffer linkAryStr(StringBuffer astrBf, String[] aryStr) {
		for (int i = 0; i < aryStr.length; i++) {
			astrBf.append(aryStr[i]);
		}
		return astrBf;
	}

	/**
	 * 连接字符串数组.
	 * 
	 * @param aryStr
	 *            String[]
	 * @param sSign
	 *            String
	 * @return String
	 */
	public static String linkAryStr(String[] aryStr, String sSign) {
		StringBuffer asbf = new StringBuffer();
		if (null == aryStr) {
			return asbf.toString();
		}

		for (int i = 0; i < aryStr.length; i++) {
			if (i > 0) {
				asbf.append(sSign);
			}
			asbf.append(aryStr[i]);
		}
		return asbf.toString();
	}

	/**
	 * 把用户输入的内容原样显示,但是html按照html格式显示. 对应原版的toTextSee. 其中包含处理回车,空格,Tab等,不翻译
	 * <为&lt;
	 * 
	 * @param str1
	 *            String
	 * @return String
	 */
	public static String str2TextShow(String str1) {
		String astr = str1;
		if (isBlank(astr)) {
			return "";
		}

		Perl5Util util = new Perl5Util();
		astr = util.substitute("s#\t#&nbsp;&nbsp;#g", astr);
		astr = util.substitute("s#\r##g", astr);

		astr = util.substitute("s#\n{3,}#\n\n#g", astr);
		astr = util.substitute("s#^\n+#\n#g", astr);
		// astr = util.substitute("s/\\\n/\\n/g", astr);

		astr = util.substitute("s#\n#<br>#g", astr);

		// astr = util.substitute("s#\\s\\s\\s#&nbsp;&nbsp;&nbsp;#g", astr);
		astr = util.substitute("s#\\s\\s\\s#&nbsp; &nbsp;#g", astr);
		astr = util.substitute("s#\\s\\s#&nbsp;&nbsp;#g", astr);

		// $str=nl2br($str);
		return (astr);

	}

	/**
	 * 把字符串处理,完全按照输入时的原样显示. html格式的部分也处理为文本.
	 * 
	 * @param aStr
	 *            String
	 * @return String
	 */
	public static String str2PureTextShow(String aStr) {
		return str2TextShow(str2TextHtml(aStr));
	}

	/**
	 * 把字符串处理,方便编辑.
	 * 
	 * @param aStr
	 *            String
	 * @return String
	 */
	public static String str2InputTextEdit(String aStr) {
		return getShowStr(str2InputText(aStr));
	}

	/**
	 * 返回字符串,编码处理,主要用于html链接的值.
	 * 
	 * @param str1
	 *            String
	 * @return String
	 */
	public static String str2URLValue(String str1) {
		String astr = str1;
		if (isBlank(astr)) {
			return "";
		}
		// 上面注释掉的是原有的程序
		// Perl5Util util = new Perl5Util();
		// astr = util.substitute("s#\"#\\%22#g", astr);
		// 直接编码
		// astr = urlEncode(astr);

		astr = str2TextHtmlRidSpace(astr);

		return astr;
	}

	/**
	 * 返回字符串,编码处理,主要用于html链接内部函数的值. 例如javascript的参数
	 * 
	 * %22 => " %3c => < %3e => > &amp; =>& %20 =>空格
	 * 
	 * @param str1
	 *            String
	 * @return String
	 */
	public static String str2JSUrlFuncValue(String str1) {
		String astr = str1;
		if (isBlank(astr)) {
			return "";
		}
		Perl5Util util = new Perl5Util();
		astr = util.substitute("s#\"#%22#g", astr);
		astr = util.substitute("s/</%3c/g", astr);
		astr = util.substitute("s/>/%3e/g", astr);
		astr = util.substitute("s/&/&amp;/g", astr);
		astr = util.substitute("s/ /%20/g", astr);
		return astr;
	}

	/**
	 * 返回字符串,编码处理,主要用于onclick,onchange等javascript的值.
	 * 
	 * 引号被替换为 \&quot;
	 * 
	 * @param str1
	 *            String
	 * @return String
	 */
	public static String str2JSCommValue(String str1) {
		String astr = str1;
		if (isBlank(astr)) {
			return "";
		}

		Perl5Util util = new Perl5Util();
		astr = util.substitute("s/&/&amp;/g", astr);
		astr = util.substitute("s#<#&lt;#g", astr);
		astr = util.substitute("s/>/&gt;/g", astr);
		// astr = util.substitute("s/ /&nbsp;/g", astr);
		// 引号被替换为 \&quot;
		astr = util.substitute("s/\"/\\\\&quot;/g", astr);
		return astr;
	}

	/**
	 * 转换字符串, 用于普通xml赋值. < replaced with &lt; > replaced with &gt; & replaced
	 * with &amp; " replaced with &quot; ' replaced with &apos;
	 * 
	 * @param str1
	 *            String
	 * @return String
	 */
	public static String str2TextXML(String str1) {
		String astr = str1;
		if (isBlank(astr)) {
			return "";
		}

		Perl5Util util = new Perl5Util();
		astr = util.substitute("s/&/&amp;/", astr);
		astr = util.substitute("s#<#&lt;#g", astr);
		astr = util.substitute("s/>/&gt;/g", astr);
		astr = util.substitute("s/\"/&quot;/", astr);
		astr = util.substitute("s/'/&apos;/", astr);
		return astr;
	}

	/**
	 * 返回字符串:用于input表单元素的value:对应toHtml_in.
	 * 
	 * @param str1
	 *            String
	 * @return String
	 */
	public static String str2InputText(String str1) {
		String astr = str1;
		if (isBlank(astr)) {
			return "";
		}
		Perl5Util util = new Perl5Util();
		astr = util.substitute("s/&/&amp;/", astr);
		astr = util.substitute("s#<#&lt;#g", astr);
		astr = util.substitute("s/>/&gt;/g", astr);
		// astr = util.substitute("s/ /&nbsp;/", astr);
		astr = util.substitute("s/\"/&quot;/g", astr);
		return astr;
	}

	/**
	 * 把html中的特殊字符翻译为和显示和输入的一样:原版对应toHtml_all.
	 * 
	 * @param astr
	 *            String
	 * @return String
	 */
	public static String str2TextHtml(String astr) {
		if (isBlank(astr)) {
			return "";
		}
		String result = astr;
		Perl5Util util = new Perl5Util();
		result = util.substitute("s/&/&amp;/g", result);
		result = util.substitute("s#<#&lt;#g", result);
		result = util.substitute("s/>/&gt;/g", result);
		result = util.substitute("s/ /&nbsp;/g", result);
		result = util.substitute("s/\"/&quot;/g", result);
		return result;
	}

	/**
	 * 把html中的特殊字符翻译为和显示和输入的一样:但不翻译空格 .
	 * 
	 * @param astr
	 *            String
	 * @return String
	 */
	public static String str2TextHtmlRidSpace(String astr) {
		if (isBlank(astr)) {
			return "";
		}

		String result = astr;

		Perl5Util util = new Perl5Util();
		result = util.substitute("s/&/&amp;/g", result);
		result = util.substitute("s#<#&lt;#g", result);
		result = util.substitute("s/>/&gt;/g", result);
		// result = util.substitute("s/ /&nbsp;/g", result);
		result = util.substitute("s/\"/&quot;/g", result);
		return result;
	}

	/**
	 * 把字符串中的双引号替换为\",用作html元素的赋值,例如把这个值放在""中间时.
	 * 
	 * @param str1
	 *            String
	 * @return String
	 */
	public static String str2HtmlValue(String str1) {
		String sStr = str1;
		if (isBlank(sStr)) {
			return "";
		}

		Perl5Util util = new Perl5Util();
		sStr = util.substitute("s/\"/\\\\\"/g", sStr);
		return sStr;
	}

	/**
	 * 在用作sql的字符串替换字符串中的单引号为两个单引号.
	 * 
	 * 如果是查询组合Sql时需要,请参考CommSQLFunc类
	 * 
	 * @param str1
	 *            String
	 * @return String
	 */
	public static String str2TextSql(String str1) {
		String astr = str1;
		if (isBlank(astr)) {
			return "";
		}

		Perl5Util util = new Perl5Util();
		astr = util.substitute("s/\'/\'\'/g", astr);
		return astr;
	}

	/**
	 * if a String identify "true".
	 * 
	 * @param aPropString
	 *            字符串
	 * 
	 * @return true if y,yes,true,1
	 */
	public static boolean isTrueString(String aPropString) {
		String strTemp = aPropString.toLowerCase(Locale.US);
		return (strTemp.startsWith("true") || strTemp.startsWith("yes")
				|| strTemp.startsWith("1") || strTemp.startsWith("y"));
	}

	/**
	 * 将字符串里指定的字符串被代替字符串所替换 例如 指定的字符串 "sdupipo" 将里面出现p的字符穿都变成g
	 * 
	 * @param source
	 *            指定字符串
	 * 
	 * @param target
	 *            要被代替的字符串
	 * 
	 * @param replace
	 *            代替字符串
	 * 
	 * @return 替换后的字符串
	 */
	public static String stringReplace(String source, String target,
			String replace) {
		if (source != null && target != null && replace != null) {
			StringBuffer stringbuffer = new StringBuffer(source.length() + 256);

			int i = -1;
			do {
				i++;

				i = source.indexOf(target);
				if (i > -1) {
					stringbuffer.append(source.substring(0, i));

					stringbuffer.append(replace);

					source = source.substring(i + target.length());
				}
			} while (i != -1);

			stringbuffer.append(source);

			return stringbuffer.toString();
		} else {
			return source;
		}
	}

	/**
	 * 根据键值获取资料里国际化对应的值
	 * 
	 * @param key
	 *            国际化键值
	 * 
	 * @return 对应的国际化值
	 */
	/*
	 * public static String getResourceValueByKey(String key) { ResourceBundle
	 * RESOURCE_BUNDLE = ResourceBundle .getBundle(Global.I18N_MESSAGE_FILE);
	 * 
	 * return RESOURCE_BUNDLE.getString(key); }
	 */

	/**
	 * 判断字符串是否为空
	 * 
	 */
	public static boolean isNullString(String str) {
		if (str == null || str.trim().length() < 1)
			return true;
		else
			return false;
	}

	/**
	 * 把源字符串的第一字符串去掉然后添加要替换的字符串
	 * 
	 * 例如："a,b,c"要把a去掉然后往后面添加d变成"b,c,d" source的大小必须和count相等
	 * 
	 * @param source
	 *            源字符串
	 * 
	 * @param replace
	 *            要替换的字符串
	 * 
	 * @param count
	 *            要替换的大小
	 * 
	 * @return 返回替换后的字符串
	 */
	public static String convertString(String source, String replace, int count) {
		String[] listSource = source.split(",");

		String resultStr = "";

		if (null != source && source.equals("")) {
			resultStr = replace;

			return resultStr;
		}

		if (listSource.length == count) {
			for (int i = 0; i < listSource.length; i++) {
				if (i == count - 1) {
					listSource[i] = replace;

					resultStr += listSource[i];
				} else {
					listSource[i] = listSource[i + 1];

					resultStr += listSource[i] + ",";
				}
			}
		} else if (listSource.length < count) {
			resultStr = source + "," + replace;
		}

		return resultStr;
	}

	/**
	 * 把html中的特殊字符翻译为和显示和输入的一样:原版对应toHtml_all.
	 * 
	 * @param astr
	 *            String
	 * @return String
	 */
	/*
	 * public static String str2DefaultTextHtml(String astr) { if
	 * (isBlank(astr)) { return ""; } // String result = astr; // Perl5Util util
	 * = new Perl5Util(); // result = util.substitute("s/&/&amp;/g", result); //
	 * result = util.substitute("s/</&lt;#g", result); // result =
	 * util.substitute("s/>/&gt;/g", result); // result =
	 * util.substitute("s/ /&nbsp;/g", result); // result =
	 * util.substitute("s/\"/&quot;/g", result); // astr = str2TextHtml(astr);
	 * String r = HtmlUtils.htmlEscape(astr); Perl5Util util = new Perl5Util();
	 * 
	 * r = util.substitute("s/ /&nbsp;/g", r); util = null; return r; }
	 */

	/**
	 * 将字节进行 KB MB GB换算
	 * 
	 * @param size
	 *            字节数
	 * @return 换算后的字符串
	 */
	public static String changeCapacities(long size) {
		long returns = 0;
		String den = "B";
		if (size < 1024) {
			returns = size;
		} else if (size >= 1024 && size < 1048576) {
			returns = size / 1024;
			den = "KB";
		} else if (size >= 1048576 && size < 1073741824) {
			returns = size / 1024 / 1024;
			den = "MB";
		} else {
			returns = size / 1024 / 1024 / 1024;
			den = "GB";
		}
		return new String(returns + den);
	}

	public static Document getDocument(String message) {
		Document doc = null;
		SAXReader reader = new SAXReader();
		try {
			doc = reader.read(new ByteArrayInputStream(message
					.getBytes("utf-8")));
		} catch (DocumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return doc;
	}

	/**
	 * 根据键值获取资料里国际化对应的值
	 * 
	 * @param key
	 *            国际化键值
	 * 
	 * @param language
	 *            要国际化的语言（肯据客户端浏览器的语言国际化）
	 * 
	 * @return 对应的国际化值
	 */
	/*
	 * public static String getResourceValueByKey(String key, String language) {
	 * 
	 * Locale local = new Locale(language);
	 * 
	 * ResourceBundle RESOURCE_BUNDLE = ResourceBundle.getBundle(
	 * Global.I18N_MESSAGE_FILE, local); return RESOURCE_BUNDLE.getString(key);
	 * }
	 */

	public static boolean isInteger(String str) {
		Pattern pattern = Pattern.compile("[0-9]*");
		Matcher isNum = pattern.matcher(str);
		if (!isNum.matches()) {
			return false;
		}
		return true;
	}

	public static boolean isLetterOrInteger(String str) {
		Pattern pattern = Pattern.compile("[0-9A-Za-z]*");
		Matcher isNum = pattern.matcher(str);
		if (!isNum.matches()) {
			return false;
		}
		return true;
	}

	/**
	 * 此方法已经不建议使用，请使用checkMobileNumber(String mobile)<br>
	 * 判断是否是移动手机号码 如果输入的是11位号码 正则验证 验证通过 return "" 验证未通过 继续判断是14701开头 如果是
	 * 继续判断14701之后的号码是否是数字 如是 返回""; 否 返回1 表示 移动号码开头 2表示非移动号码 3 错误
	 * 
	 * @param str
	 * @return ""是移动号码
	 */
	@Deprecated
	public static String checkMobile(String str) {
		if (str.length() == 11) {
			Pattern pattern = Pattern
					.compile("^(((134[0-9]{1})|(135[0-9]{1})|(136[0-9]{1})|(137[0-9]{1})|(138[0-9]{1})|(139[0-9]{1})|(150[0-9]{1})|(151[0-9]{1})|(152[0-9]{1})|(157[0-9]{1})|(158[0-9]{1})|(159[0-9]{1})|(188[0-9]{1}))+\\d{7})$");
			Matcher isNum = pattern.matcher(str);
			if (isNum.matches()) {
				return "";
			}
			if (str.substring(0, 5).equals("14701")) {
				if (isInteger(str.substring(5, str.length()))) {
					return "";
				} else {
					return "1";
				}
			}
		} else {
			if (str.length() >= 3) {
				String tmps = str.substring(0, 3);
				if (tmps.equals("134") || tmps.equals("135")
						|| tmps.equals("136") || tmps.equals("137")
						|| tmps.equals("138") || tmps.equals("139")
						|| tmps.equals("150") || tmps.equals("151")
						|| tmps.equals("152") || tmps.equals("157")
						|| tmps.equals("158") || tmps.equals("159")) {
					return "1";
				} else {
					if (str.length() >= 5) {
						if (str.substring(0, 5).equals("14701")) {
							return "1";
						}
					}
					return "2";
				}
			} else {
				return "2";
			}
		}
		return "3";
	}

	/**
	 * 验证移动手机号
	 * 
	 * @param mobile
	 * @return 0：非移动手机号；1:是移动号码；2：手机号不完整；
	 * @author zhoushijun
	 * @since 2009-12-2
	 */
	public static int checkMobileNumber(String mobile) {
		if (mobile.length() == 11) {
			Pattern pattern = Pattern
					.compile("^((134|135|136|137|138|139|150|151|152|157|158|159|188|147|187)+\\d{8})$");
			Matcher isNum = pattern.matcher(mobile);
			if (isNum.matches()) {
				return 1;
			} else {
				return 0;
			}
		} else {
			return 2;
		}
	}

	/**
	 * @description: 补充字符的方法,1、direction的取值为r(在原字符串右边补充)，l(在原字符串左边补充)
	 * @param oldStr
	 *            ：原字符串
	 * @param strLen
	 *            ：返回字符串长度
	 * @param padChar
	 *            ：插入字符串
	 * @param direction
	 *            ：插入方向
	 * @return
	 */
	public static String padString(String oldStr, int strLen, char padChar,
			char direction) {
		String newStr = oldStr;
		try {
			if (oldStr.length() >= strLen) {
				newStr = oldStr;
			} else {
				if (direction == 'r') {
					while (newStr.length() < strLen) {
						newStr = newStr + padChar;
					}
				} else {
					while (newStr.length() < strLen) {
						newStr = padChar + newStr;
					}
				}
			}
			return newStr;
		} catch (Exception e) {
			return oldStr;
		}
	}

	/** 提供字符串到Vector的转变 * */
	public static Vector Str2Vect(String tStr, String sStr) {
		Vector vector = new Vector();
		StringTokenizer st = new StringTokenizer(tStr, sStr);
		while (st.hasMoreTokens()) {
			vector.add(st.nextToken());
		}
		return vector;
	}

	/** 提供Vector到字符串的转变，转变后的字符串以sStr作为分割符 * */
	public static String Vect2Str(Vector tVect, String sStr) {
		String reStr = "";
		if (tVect.size() > 0)
			reStr = (String) tVect.get(0);
		for (int i = 1; i < tVect.size(); i++) {
			reStr += sStr + (String) tVect.get(i);
		}
		return reStr;
	}

	/** 提供Vector到字符串的转变，转变后的字符串没有分割符 * */
	public static String Vect2Str(Vector tVect) {
		String reStr = "";
		for (int i = 0; i < tVect.size(); i++) {
			reStr += (String) tVect.get(i);
		}
		return reStr;
	}

	/** 提供字符串到字符串数组的转变,转变后的字符串以sStr作为分割符 * */
	public static String[] Str2Strs(String tStr, String sStr) {
		StringTokenizer st = new StringTokenizer(tStr, sStr);
		String[] reStrs = new String[st.countTokens()];
		int n = 0;
		while (st.hasMoreTokens()) {
			reStrs[n] = st.nextToken();
			n++;
		}
		return reStrs;
	}

	/**
	 * 将以separator分割的字符串str按cnt个拆分开
	 * 
	 * @return String[]
	 * @author Administrator 2009-12-3
	 */
	public static String[] subStrToArray(String str, String separator, int cnt) {
		String[] arr = StringUtil.Str2Strs(str, separator);
		String[] ar = null;
		if (arr.length > cnt) {
			int num = (arr.length % cnt) > 0 ? 1 : 0;
			ar = new String[arr.length / cnt + num];
			int sta = 0;
			int end = (arr[0].length() + 1) * cnt - 1;
			for (int i = 0; i < ar.length; i++) {
				ar[i] = str.substring(sta, end);
				sta = end + 1;
				end = sta + (arr[0].length() + 1) * cnt - 1;
				if (sta > str.length()) {
					break;
				}
				if (end > str.length()) {
					end = str.length();
					ar[i + 1] = str.substring(sta, end);
					break;
				}
			}
		} else {
			ar = new String[1];
			ar[0] = str;
		}
		return ar;
	}

	/** 提供字符串数组到字符串的转变，转变后的字符串以sStr作为分割符 * */
	public static String Strs2Str(String[] tStrs, String sStr) {
		String reStr = "";
		int len = tStrs.length;
		if (len > 0) {
			if (tStrs[0] != null)
				reStr = tStrs[0];
		}
		for (int i = 1; i < len; i++) {
			if (tStrs[i] != null) {
				if (tStrs[i].length() > 0)
					reStr += sStr + tStrs[i];
			}
		}
		return reStr;
	}

	/** 提供字符串数组到字符串的转变，转变后的字符串以sStr作为分割符,每个元素用''包含 * */
	public static String Strs2Str(String[] tStrs, String sStr, String tostr) {
		String reStr = "";
		int len = tStrs.length;
		if (len > 0) {
			if (tStrs[0] != null)
				reStr = "'" + tStrs[0] + "'";
		}
		for (int i = 1; i < len; i++) {
			if (tStrs[i] != null) {
				if (tStrs[i].length() > 0)
					reStr += sStr + "'" + tStrs[i] + "'";
			}
		}
		return reStr;
	}

	public static double numberDecimal(double d, int i) {
		BigDecimal b = new BigDecimal(d);
		BigDecimal bd1 = b.setScale(i, b.ROUND_HALF_UP);
		d = bd1.doubleValue();
		return d;
	}

	/** 字符串数组到字符串的转变，转变后的字符串没有分割符 * */
	public static String Strs2Str(String[] tStrs) {
		String reStr = "";
		int len = tStrs.length;
		for (int i = 0; i < len; i++) {
			if (tStrs[i] != null) {
				if (tStrs[i].length() > 0)
					reStr += tStrs[i];
			}
		}
		return reStr;
	}

	/** 字符串以指定长度进行切割，结果放入Vector对象中 * */
	public Vector Str2Vect(String tStr, int nleng) {
		int strLength = tStr.length();
		int ndiv = strLength / nleng;
		Vector reVect = new Vector();
		if (strLength % nleng == 0)
			ndiv--;
		for (int i = 0; i < (ndiv); i++) {
			reVect.add(tStr.substring(i * nleng, (i + 1) * nleng));
		}
		reVect.add(tStr.substring(ndiv * nleng, strLength));
		return reVect;
	}

	/** 字符串相除，如果产生异常，返回"-" * */
	public static String Divide(String a, String b) {
		try {
			return String.valueOf(Double.valueOf(a).doubleValue()
					/ Double.valueOf(b).doubleValue());
		} catch (Exception e) {
			return "-";
		}
	}

	/** 字符串相除 如果产生异常，返回re * */
	public static String Divide(String a, String b, String re) {
		try {
			return String.valueOf(Double.valueOf(a).doubleValue()
					/ Double.valueOf(b).doubleValue());
		} catch (Exception e) {
			return re;
		}
	}

	/** 字符串相减，如果产生异常，返回re * */
	public static String decrease(String a, String b, String re) {
		try {
			return String.valueOf(Double.valueOf(a).doubleValue()
					- Double.valueOf(b).doubleValue());
		} catch (Exception e) {
			return re;
		}
	}

	/** 字符串相减，如果产生异常，返回a * */
	public static String decrease(String a, int b) {
		try {
			return String.valueOf(Integer.valueOf(a).intValue()
					- Integer.valueOf(b).intValue());
		} catch (Exception e) {
			return a;
		}
	}

	/** 字符串相减，如果产生异常，返回a * */
	public static String decrease(String a, String b) {
		try {
			return String.valueOf(Double.valueOf(a).doubleValue()
					- Double.valueOf(b).doubleValue());
		} catch (Exception e) {
			return a;
		}
	}

	/** 字符串减一 如果产生异常，返回a * */
	public static String decrease(String a) {
		try {
			return String.valueOf(Double.valueOf(a).doubleValue() - 1);
		} catch (Exception e) {
			return a;
		}
	}

	/** 字符串相加 如果产生异常，返回re * */
	public static String adding(String a, String b, String re) {
		try {
			return String.valueOf(Double.valueOf(a).doubleValue()
					+ Double.valueOf(b).doubleValue());
		} catch (Exception e) {
			return re;
		}
	}

	/** 字符串相加 如果产生异常，返回a * */
	public static String adding(String a, String b) {
		try {
			return String.valueOf(Double.valueOf(a).doubleValue()
					+ Double.valueOf(b).doubleValue());
		} catch (Exception e) {
			return a;
		}
	}

	/** 字符串加一 如果产生异常，返回a * */
	public static String adding(String a) {
		try {
			return String.valueOf(Double.valueOf(a).doubleValue() + 1);
		} catch (Exception e) {
			return a;
		}
	}

	/** 字符串相乘 如果产生异常，返回re * */
	public static String multiply(String a, String b, String re) {
		try {
			return String.valueOf(Double.valueOf(a).doubleValue()
					* Double.valueOf(b).doubleValue());
		} catch (Exception e) {
			return re;
		}
	}

	/** 字符串相乘 如果产生异常，返回a * */
	public static String multiply(String a, String b) {
		try {
			return String.valueOf(Double.valueOf(a).doubleValue()
					* Double.valueOf(b).doubleValue());
		} catch (Exception e) {
			return a;
		}
	}

	/** 字符串(a-b)/b 如果产生异常，返回re * */
	public static String Tqb(String a, String b, String re) {
		try {
			return String.valueOf((Double.valueOf(a).doubleValue() - Double
					.valueOf(b).doubleValue())
					/ (Double.valueOf(b).doubleValue()));
		} catch (Exception e) {
			return re;
		}
	}

	/** 字符串(a-b)/b 如果产生异常，返回"-" * */
	public static String Tqb(String a, String b) {
		try {
			return String.valueOf((Double.valueOf(a).doubleValue() - Double
					.valueOf(b).doubleValue())
					/ (Double.valueOf(b).doubleValue()));
		} catch (Exception e) {
			return "-";
		}
	}

	/** 将字符串转换成Utf-8编码格式 * */
	public static String toUtf8String(String s) {
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < s.length(); i++) {
			char c = s.charAt(i);
			if (c >= 0 && c <= 255) {
				sb.append(c);
			} else {
				byte[] b;
				try {
					b = Character.toString(c).getBytes("utf-8");
				} catch (Exception ex) {
					System.out.println(ex);
					b = new byte[0];
				}
				for (int j = 0; j < b.length; j++) {
					int k = b[j];
					if (k < 0)
						k += 256;
					sb.append("%" + Integer.toHexString(k).toUpperCase());
				}
			}
		}
		return sb.toString();
	}

	/** 将字符串转换成GBK编码格式 * */
	public static String toGbkString(String s) {
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < s.length(); i++) {
			char c = s.charAt(i);
			if (c >= 0 && c <= 255) {
				sb.append(c);
			} else {
				byte[] b;
				try {
					b = Character.toString(c).getBytes("GBK");
				} catch (Exception ex) {
					System.out.println(ex);
					b = new byte[0];
				}
				for (int j = 0; j < b.length; j++) {
					int k = b[j];
					if (k < 0)
						k += 256;
					sb.append("%" + Integer.toHexString(k).toUpperCase());
				}
			}
		}
		return sb.toString();
	}

	/**
	 * 用给定的分隔符对字符串进行拆分，并生成数组
	 * 
	 * @param message
	 *            需要拆分的字符串
	 * @param separator
	 *            分隔符
	 * @return 生成的数组
	 */
	public static String[] splitToArray(String message, String separator) {
		List list = new ArrayList();
		int start = 0;
		int index = 0;
		while ((index = message.indexOf(separator, start)) != -1) {
			list.add(message.substring(start, index));
			start = index + separator.length();
		}

		if (start < message.length()) {
			list.add(message.substring(start, message.length()));
		}

		return (String[]) list.toArray(new String[list.size()]);
	}

	/** 将字符串转换为java.sql.date类型,str的格式必须匹配给定的格式formatStr * */
	public static java.sql.Date str2SqlDate(String str, String formatStr) {
		java.sql.Date sqlDate = new java.sql.Date(0);// 默认获得当前时间
		try {
			sqlDate = new java.sql.Date(new java.text.SimpleDateFormat(
					formatStr).parse(str).getTime());
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return sqlDate;
	}

	/** 将对象的返回的值不能为空 */
	public static String clear(Object obj) {
		if (null == obj || "null".equals(obj))
			return "";
		return obj.toString();
	}

	/** 将对象的返回的值不能为空，否则返回0 */
	public static String clearToZero(Object obj) {
		if (null == obj)
			return "0";
		return obj.toString();
	}

	/** 若对象为空返回指定的值 */
	public static String clear(Object obj, String value) {
		if (null == obj)
			return value;
		return obj.toString();
	}

	/** 判断对象的值是否为空 */
	public static boolean isNull(Object obj) {
		boolean flag = false;
		if (null == obj || "".equals(obj))
			flag = true;
		return flag;

	}

	/**
	 * 判断对象的值是否为空
	 * 
	 * @param obj
	 * @return
	 */
	public static boolean isNotNull(Object obj) {
		boolean flag = true;
		if (null == obj || "".equals(obj))
			flag = false;
		return flag;

	}

	/**
	 * 判断List是否为空
	 * 
	 * @param l
	 * @return
	 */
	public static boolean isNotNull(List l) {
		if (null != l && l.size() > 0)
			return true;
		return false;
	}

	/**
	 * 字符是否为空
	 * 
	 * @param str
	 * @return
	 */
	public static boolean isNotNull(String str) {
		if (null != str && str.trim().length() > 0)
			return true;
		return false;
	}

	public static boolean isNotNull(String[] str) {
		if (null != str && str.length > 0)
			return true;
		return false;
	}

	/**
	 * 不为空
	 * 
	 * @param strObject
	 * @return
	 */
	public static String notNull(Object strObject) {
		return notNull(strObject, "");
	}

	/**
	 * strObject为空返回defaultValue，不为空返回strObject
	 * 
	 * @param str
	 * @return
	 */
	public static String notNull(Object strObject, String defaultValue) {
		if ((strObject == null) || ("".equals(strObject.toString().trim()))) {
			return defaultValue;
		}
		return strObject.toString();
	}

	/**
	 * 字符不为空
	 * 
	 * @param str
	 * @return
	 */
	public static String notNull(String str) {
		/* 56 */return notNull(str, "");
	}

	/**
	 * 不为空
	 * 
	 * @param str
	 * @param defaultValue
	 * @return
	 */
	public static String notNull(String str, String defaultValue) {
		/* 66 */if ((str == null) || ("".equals(str.trim()))) {
			/* 67 */return defaultValue;
		}
		/* 69 */return str;
	}

	/**
	 * 如果参数为null返回“”
	 * 
	 * @param s
	 * @return
	 */
	public static String ifNull(Object s) {
		return ((s == null) ? "" : s.toString());
	}

	/**
	 * 如果s参数为空返回desc，不为空返回s
	 * 
	 * @param s
	 * @param desc
	 * @return
	 */
	public static String ifNull(Object s, String desc) {
		return ((s == null) ? desc : s.toString());
	}

	public static String replaceStrNullOrNot(String str) {
		if (null != str && str.length() > 0) {
			return str;
		} else {
			return " ";
		}
	}

	public static String replaceStrNullOrNot(Object obj) {
		if (null != obj && !"".equals(obj)) {
			return obj.toString();
		} else {
			return " ";
		}
	}

	/**
	 * 
	 * 方法名称:isNull
	 * <p>
	 * 方法描述:判断是字符串是否为空
	 * <p>
	 * 参数:
	 * 
	 * @param str
	 *            字符串 参数:
	 * @return boolean
	 *         <p>
	 *         <p>
	 * @author HJun
	 *         <p>
	 * @date Sep 2, 2009
	 *       <p>
	 */
	public static boolean isNull(String str) {
		if (null == str || "".equals(str.trim()))
			return true;
		return false;
	}

	/**
	 * 
	 * 方法名称:isNull
	 * <p>
	 * 方法描述: 判断结果集是否为空
	 * <p>
	 * 参数:
	 * 
	 * @param list
	 *            结果集对象 参数:
	 * @return boolean
	 *         <p>
	 *         <p>
	 * @author HJun
	 *         <p>
	 * @date Sep 2, 2009
	 *       <p>
	 */
	public static boolean isNull(List list) {
		if (null == list || list.size() == 0)
			return true;
		return false;
	}

	/**
	 * 
	 * 方法名称:isNull
	 * <p>
	 * 方法描述: 判断字符串数组是否为空
	 * <p>
	 * 参数:
	 * 
	 * @param str
	 *            String[] 字符串数组 参数:
	 * @return boolean
	 *         <p>
	 *         <p>
	 * @author HJun
	 *         <p>
	 * @date Sep 2, 2009
	 *       <p>
	 */
	public static boolean isNull(String[] str) {
		if (null == str || str.length == 0)
			return true;
		return false;
	}

	public static String translateToChinese(String str) {
		if (str != null && !"".equals(str) && IsNumber(str)) {
			return translateToChinese(Integer.parseInt(str));
		} else {
			return "0";
		}

	}

	/**
	 * @方法名 translate
	 * @功能 简单的数字转中文
	 * @param a
	 *            原始数字
	 * @return 中文字符串
	 */
	public static String translateToChinese(int a) {

		String[] units = { "", "十", "百", "千", "万", "十", "百", "千", "亿" };
		String[] nums = { "一", "二", "三", "四", "五", "六", "七", "八", "九", "十" };

		String result = "";
		if (a < 0) {
			result = "负";
			a = Math.abs(a);
		}
		String t = String.valueOf(a);
		for (int i = t.length() - 1; i >= 0; i--) {
			int r = (int) (a / Math.pow(10, i));
			if (r % 10 != 0) {
				String s = String.valueOf(r);
				String l = s.substring(s.length() - 1, s.length());
				result += nums[Integer.parseInt(l) - 1];
				result += (units[i]);
			} else {
				if (!result.endsWith("零")) {
					result += "零";
				}
			}
		}
		String num = a + "";
		/*
		 * 因为方法对10-20之间的数字支持不好，比如11返回一十一，不能满足需求 所以这里单独判断
		 */
		if (a == 10) {
			return "十";
		} else if (a > 10 && a < 20) {
			return result.substring(1);
		} else if (num.endsWith("0")) {
			result = result.substring(0, result.length() - 1);
		}
		return result;
	}

	public static boolean eq(String str, Object o) {
		if (null != o && isNotNull(o)) {
			return o.equals(str);
		}
		return false;
	}

	public static boolean eq(Object o1, Object o2) {
		if (null == o1 || isNull(o1)) {
			if (o2 == null || isNull(o2)) {
				return true;
			} else {
				return false;
			}
		} else {
			return o1.equals(o2);
		}
	}

	/**
	 * @return String
	 * @author Administrator 2009-9-20
	 */
	public static String toString(List list) {
		StringBuffer reStr = new StringBuffer("");
		for (Object o : list) {
			reStr.append(o.toString());
			reStr.append(",");
		}
		return reStr.toString();
	}

	/**
	 * 截取字符串
	 * 
	 * @param src
	 *            待截取字符串
	 * @param num
	 *            截取长度
	 * @return 截取后的字符串
	 */
	public static String cutString(String src, int num) {
		if (isNull(src))
			return src;
		return src.substring(0, src.length() > num ? num : src.length());
	}

	public static double mulite(String str, String str2) {
		double d1 = Double.parseDouble(str);
		double d2 = Double.parseDouble(str2);
		return d1 * d2;
	}

	/**
	 * @函数名称：IsNumber
	 * @功能描述：是否数字
	 * @param str
	 *            ：true表示是，false表示否
	 * @return：是或否
	 * @exception: null 空指针异常
	 */
	public static boolean IsNumber(String str) {
		Pattern pattern = Pattern.compile("[0-9]*");
		return pattern.matcher(str).matches();
	}

	/**
	 * @函数名称：IsNumber
	 * @功能描述：是数值
	 * @param str
	 *            ：true表示是，false表示否
	 * @return：是或否
	 * @exception: null 空指针异常
	 */
	public static boolean IsFloat(String str) {
		Pattern pattern = Pattern.compile("\\d+[.]?\\d*");
		return pattern.matcher(str).matches();
	}

	public static String getRequestStr(HttpServletRequest request, String param) {
		String result = request.getParameter(param);
		if (StringUtil.isNull(result)) {
			result = (String) request.getAttribute(param);
		}
		if (StringUtil.isNull(result)) {
			result = "";
		}
		result = result.trim();
		return result;
	}

	/**
	 * XML字符串中不能用这些字符 < > ' " &
	 * 
	 * @param str
	 *            待转换的字符串
	 * @return String 转换后的字符串
	 * @author zhangg
	 */
	public static String replace4XML(String str) {
		if (str == null)
			return "";
		return str.replace("<", "&lt;").replace(">", "&gt;")
				.replace("'", "&apos;").replace("\"", "&quot;")
				.replace("&", "&amp;");
	}

	/**
	 * 和“WebContent/skins/default/js/sotowerfunction.js”里的方法“tag2code()”是一对，
	 * 需要一起修改
	 * 
	 * @函数名称：HtmlTag2String
	 * @功能描述：转换html符号为字符
	 * @param：String
	 * @return：String
	 */
	public static String HtmlTag2String(String temp) {
		temp = temp == null ? "" : temp;
		return temp.replace("-lt;", "<").replace("-gt;", ">")
				.replace("-amp;", "&");
	}

	/**
	 * 
	 * 方法名称：zero 方法描述：设置小数，前面没有0增加0
	 * 
	 * @param str
	 * @return
	 * @return String
	 * @author HJun
	 * @version 1.0 Dec 17, 2009
	 */
	public static String zero(String str) {
		if (str.startsWith(".")) {
			str = "0".concat(str);
		}
		return str;
	}

	/**
	 * @函数名称：String2HtmlTag
	 * @功能描述：转换“<”、“>”为html符号
	 * @param：String
	 * @return：String
	 */
	public static String String2HtmlTag(String temp) {
		temp = temp == null ? "" : temp;
		return temp.replace("&", "&amp;").replace("<", "&lt;")
				.replace(">", "&gt;").replace("\"", "&quot;")
				.replace("'", "&acute;");
	}

	/**
	 * split("separator",'a') returns {"sep","r","tor"}
	 * 
	 * @函数名称：convertToUTF8
	 * @功能描述：方法描述拆分字符串
	 * @param src
	 * @param char
	 * @return <br>
	 */
	public static String[] split(String src, char separator) {

		if (src == null)
			return null;
		else
			src = src.trim();
		int sprtCount = count(src, separator);
		if (sprtCount == 0) {
			String[] det = new String[1];
			det[0] = src;
			return det;
		}
		String[] det = new String[sprtCount + 1];
		int indexs = 0, indexe = 0;
		for (int i = 0; i <= sprtCount; i++) {
			indexe = src.indexOf(separator, indexs);
			if (indexe == -1)
				det[i] = src.substring(indexs);
			else {
				det[i] = src.substring(indexs, indexe);
				indexs = indexe + 1;
			}
		}
		return det;
	}

	/**
	 * @函数名称：count
	 * @功能描述：方法描述个数
	 * @param pStr
	 *            String
	 * @return String
	 */
	public static int count(String ptr, char c) {
		int coun = 0, pos = 0;
		while ((pos = ptr.indexOf(c, pos)) != -1) {
			coun++;
			pos++;
		}
		return coun;
	}

	/**
	 * @函数名称：count
	 * @功能描述：方法描述个数
	 * @param pStr
	 *            String
	 * @param c
	 *            String
	 * @return String
	 */
	public static int count(String ptr, String c) {
		int coun = 0, pos = 0;
		while ((pos = ptr.indexOf(c, pos)) != -1) {
			coun++;
			pos += c.length();
		}
		return coun;
	}

	/**
	 * @函数名称：getDoubleXDigit
	 * @功能描述：保留X位小数
	 * @param obj
	 *            String
	 * @param x
	 *            double
	 * @return double
	 */
	public static double getDoubleXDigit(String obj, double x) {
		double tmpD = Double.parseDouble(obj);
		double y = 10;
		y = Math.pow(y, x);
		tmpD = tmpD * y;
		tmpD = Math.round(tmpD);
		tmpD = tmpD / y;
		return tmpD;
	}

	public static String getBackName(String fileName) {
		if (isNull(fileName))
			return "";
		if (fileName.lastIndexOf(".") < 0)
			return "";
		return fileName.substring(fileName.lastIndexOf("."));
	}

	public static String getGBK(String str, String code) {
		try {
			return new String(str.getBytes(), code);
		} catch (UnsupportedEncodingException e) {
			return null;
		}
	}

	/** 提供字符串到ArrayList的转变 * */
	public static List<String> str2List(String tStr, String sStr) {
		if (isNull(tStr)) {
			return null;
		}
		List<String> list = new ArrayList<String>();
		StringTokenizer st = new StringTokenizer(tStr, sStr);
		while (st.hasMoreTokens()) {
			list.add(st.nextToken());
		}
		return list;
	}

	/**
	 * 方法名称： 方法描述：
	 * 
	 * @param context
	 * @version 1.0
	 */
	public static Object invokeMethod(Object owner, String methodName,
			Object[] args) throws Exception {
		Class ownerClass = owner.getClass();
		System.out.println("Method=" + methodName);
		methodName = methodName.substring(0, 1).toUpperCase()
				+ methodName.substring(1);
		Method method = null;
		try {
			method = ownerClass.getMethod("get" + methodName);

		} catch (SecurityException e) {
		} catch (NoSuchMethodException e) {
			return " can't find 'get" + methodName + "' method";
		}

		return method.invoke(owner);
	}

	/**
	 * 方法名称： 方法描述：
	 * 
	 * @param context
	 * @version 1.0
	 */
	public static String convertString(String s) {
		if (isNotNull(s)) {
			char[] c = s.toCharArray();
			String str = "";
			int i = 0;
			for (char ch : c) {
				if (Character.isUpperCase(ch) && i > 0) {
					str += "_";
				}
				str += ch;
				i++;
			}
			return str;
		} else {
			return "";
		}
	}

	public static String convertString1(String s) {
		if (isNotNull(s)) {
			String ss = s.toLowerCase();
			char[] carr = ss.toCharArray();
			String str = "";
			char temp = 0;
			for (int i = 0; i < carr.length; i++) {
				if (i > 0)
					temp = carr[i - 1];
				if (Character.isLetter(carr[i]) && temp == '_') {
					str += (carr[i] + "").toUpperCase();
				} else {
					str += carr[i];
				}
			}
			str = str.replace("_", "");
			return str;
		} else {
			return "";
		}
	}

	/**
	 * 
	 * @author zhangling 在模糊查询中过滤容易引发SQL语句执行异常的符号
	 */
	public static String ReplaceSqlLike(String strQuery) {
		String strRet = strQuery;
		strRet = strRet.replace("/", "//");
		strRet = strRet.replace("'", "''");
		strRet = strRet.replace("%", "/%");
		strRet = strRet.replace("[", "/[");
		strRet = "'%" + strRet + "%' escape '/'";
		return strRet;
	}

	/**
	 * 返回年月，格式如yyyyMM
	 * 
	 * @return返回时间格式字符串。按年，月
	 */
	public static String getYearAndMouth() {
		SimpleDateFormat df = new SimpleDateFormat("yyyyMM");
		Date date = new Date();
		return df.format(date);
	}

	/**
	 * 用特殊的字符连接字符串
	 * 
	 * @param strings
	 *            要连接的字符串数组
	 * @param spilit_sign
	 *            连接字符
	 * @return 连接字符串
	 */
	public static String stringConnect(String[] strings, String spilit_sign) {
		StringBuffer str = new StringBuffer();
		for (int i = 0; i < strings.length; i++) {
			str.append(strings[i]);
			str.append(spilit_sign);
		}
		return str.toString();
	}

	/**
	 * 过滤字符串里的的特殊字符
	 * 
	 * @param str
	 *            要过滤的字符串
	 * @return 过滤后的字符串
	 */
	public static String stringFilter(String str) {
		String[] str_arr = stringSpilit(str, "");
		for (int i = 0; i < str_arr.length; i++) {
			for (int j = 0; j < FilterChars.length; j++) {
				if (FilterChars[j][0].equals(str_arr[i]))
					str_arr[i] = FilterChars[j][1];
			}
		}
		return (stringConnect(str_arr, "")).trim();
	}

	/**
	 * 过滤脚本中的特殊字符（包括回车符(\n)和换行符(\r)）
	 * 
	 * @param str
	 *            要进行过滤的字符串
	 * @return 过滤后的字符串
	 */
	public static String stringFilterScriptChar(String str) {
		String[] str_arr = stringSpilit(str, "");
		for (int i = 0; i < str_arr.length; i++) {
			for (int j = 0; j < FilterScriptChars.length; j++) {
				if (FilterScriptChars[j][0].equals(str_arr[i]))
					str_arr[i] = FilterScriptChars[j][1];
			}
		}
		return (stringConnect(str_arr, "")).trim();
	}

	/**
	 * 分割字符串
	 * 
	 * @param str
	 *            要分割的字符串
	 * @param spilit_sign
	 *            字符串的分割标志
	 * @return 分割后得到的字符串数组
	 */
	public static String[] stringSpilit(String str, String spilit_sign) {
		String[] spilit_string = str.split(spilit_sign);
		if (spilit_string[0].equals("")) {
			String[] new_string = new String[spilit_string.length - 1];
			for (int i = 1; i < spilit_string.length; i++)
				new_string[i - 1] = spilit_string[i];
			return new_string;
		} else
			return spilit_string;
	}

	/**
	 * 字符串字符集转换
	 * 
	 * @param str
	 *            要转换的字符串
	 * @return 转换过的字符串
	 */
	public static String stringTransCharset(String str) {
		String new_str = null;
		try {
			new_str = new String(str.getBytes("iso-8859-1"), "GBK");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new_str;
	}

	public static boolean isChineaseLetter(String name) {
		Pattern pattern = Pattern.compile("^[\u4e00-\u9fa5]*$");
		Matcher isNum = pattern.matcher(name);
		if (!isNum.matches()) {
			return false;
		}
		return true;
	}

	/**
	 * 
	 * @param source
	 * @param fillSeperator
	 * @param length
	 * @return
	 */
	public static String fillStringBefore(String source, String fillSeperator,
			int length) {
		String dest = "";
		if (source == null) {
			source = "";
		}
		for (int i = 0; i < length - source.length(); ++i)
			dest = dest + fillSeperator;
		dest = dest + source;
		return dest;
	}

	/**
	 * 如果source为null返回“0“
	 * 
	 * @param source
	 * @return
	 */
	public static String inc(String source) {
		if (source == null) {
			return "0";
		}
		int length = source.length();
		long temp = Long.parseLong(source) + 1L;
		return fillStringBefore(String.valueOf(temp), "0", length);
	}

	/**
	 * 
	 * @param content
	 * @return
	 */
	public static String sqlFilter(String content) {
		content = (content == null) ? "" : content;
		content = replace(content, "'", "''");
		return content;
	}

	public static String filter(String content) {
		if (content == null) {
			/* 153 */return "";
		}
		/* 155 */content = (content == null) ? " " : content;
		/* 156 */content = replace(content, "<", "<");
		/* 157 */content = replace(content, ">", ">");
		/* 158 */content = replace(content, " ", " ");
		/* 159 */content = replace(content, "\n", "<BR>");
		/* 160 */return content;
	}

	public static String filter(Object content) {
		return filter((String) content);
	}

	/**
	 * 字符双数组解析
	 * 
	 * @param data
	 * @return
	 */
	public static String parseInsertStr(String[][] data) {
		/* 214 */StringBuffer sb1 = new StringBuffer();
		/* 215 */sb1.append(" (");
		/* 216 */StringBuffer sb2 = new StringBuffer();
		/* 217 */sb2.append(" values(");
		/* 218 */for (int i = 0; i < data.length; ++i) {
			/* 219 */sb1.append(data[i][0]);
			/* 220 */switch (data[i][2].charAt(0)) {
			case '1':
				/* 222 */sb2.append("'" + data[i][1] + "'");
				/* 223 */break;
			case '2':
				/* 225 */sb2.append(data[i][1]);
			}

			/* 228 */if (i < data.length - 1) {
				/* 229 */sb1.append(",");
				/* 230 */sb2.append(",");
			} else {
				/* 233 */sb1.append(")");
				/* 234 */sb2.append(")");
			}
		}
		/* 237 */sb1.append(sb2.toString());
		/* 238 */return sb1.toString();
	}

	public static String parseUpdateStr(String[][] data) {
		/* 247 */StringBuffer sb1 = new StringBuffer();
		/* 248 */sb1.append(" ");
		/* 249 */for (int i = 0; i < data.length; ++i) {
			/* 250 */sb1.append(data[i][0]);
			/* 251 */sb1.append("=");
			/* 252 */switch (data[i][2].charAt(0)) {
			case '1':
				/* 254 */sb1.append("'" + data[i][1] + "'");
				/* 255 */break;
			case '2':
				/* 257 */sb1.append(data[i][1]);
			}

			/* 260 */if (i < data.length - 1) {
				/* 261 */sb1.append(",");
			}
		}
		/* 264 */return sb1.toString();
	}

	public static String encodeBase64(byte[] abyte0) {
		/* 383 */int l = abyte0.length;
		/* 384 */StringBuffer stringbuffer = new StringBuffer((l / 3 + 1) * 4);
		/* 385 */for (int i1 = 0; i1 < l; ++i1) {
			/* 386 */int i = abyte0[i1] >> 2 & 0x3F;
			/* 387 */stringbuffer
					.append(
					/* 388 */"ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/"
					/* 389 */.charAt(i));
			/* 390 */i = abyte0[i1] << 4 & 0x3F;
			/* 391 */if (++i1 < l) {
				/* 392 */i |= abyte0[i1] >> 4 & 0xF;
			}
			/* 394 */stringbuffer
					.append(
					/* 395 */"ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/"
					/* 396 */.charAt(i));
			/* 397 */if (i1 < l) {
				/* 398 */int j = abyte0[i1] << 2 & 0x3F;
				/* 399 */if (++i1 < l) {
					/* 400 */j |= abyte0[i1] >> 6 & 0x3;
				}
				/* 402 */stringbuffer
						.append(
						/* 403 */"ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/"
						/* 404 */.charAt(j));
			} else {
				/* 407 */++i1;
				/* 408 */stringbuffer.append('=');
			}
			/* 410 */if (i1 < l) {
				/* 411 */int k = abyte0[i1] & 0x3F;
				/* 412 */stringbuffer
						.append(
						/* 413 */"ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/"
						/* 414 */.charAt(k));
			} else {
				/* 417 */stringbuffer.append('=');
			}
		}
		/* 420 */return stringbuffer.toString();
	}

	public static String encodeBase64(String s) {
		/* 429 */return encodeBase64(s.getBytes());
	}

	public static String decodeBase64(byte[] abyte0) {
		/* 438 */int k = abyte0.length;
		/* 439 */StringBuffer stringbuffer = new StringBuffer(k * 3 / 4);
		/* 440 */for (int l = 0; l < k; ++l) {
			/* 441 */int i =
			/* 442 */"ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/"
			/* 443 */.indexOf(abyte0[l]);
			/* 444 */++l;
			/* 445 */int j =
			/* 446 */"ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/"
			/* 447 */.indexOf(abyte0[l]);
			/* 448 */i = i << 2 | j >> 4 & 0x3;
			/* 449 */stringbuffer.append((char) i);
			/* 450 */if (++l < k) {
				/* 451 */i = abyte0[l];
				/* 452 */if (i == 61) {
					break;
				}
				/* 455 */i =
				/* 456 */"ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/"
				/* 457 */.indexOf((char) i);
				/* 458 */j = j << 4 & 0xF0 | i >> 2 & 0xF;
				/* 459 */stringbuffer.append((char) j);
			}
			/* 461 */if (++l >= k) {
				continue;
			}
			/* 464 */j = abyte0[l];
			/* 465 */if (j == 61) {
				break;
			}
			/* 468 */j =
			/* 469 */"ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/"
			/* 470 */.indexOf((char) j);
			/* 471 */i = i << 6 & 0xC0 | j;
			/* 472 */stringbuffer.append((char) i);
		}
		/* 474 */return stringbuffer.toString();
	}

	public static String decodeBase64(String s) {
		/* 483 */return decodeBase64(s.getBytes());
	}

	public static String doubleToCurrency(double f, String moneyUnit,
			String digit) {
		/* 556 */NumberFormat nf = NumberFormat.getIntegerInstance();
		/* 557 */if (moneyUnit == null) {
			/* 558 */return "空";
		}
		/* 560 */if ((moneyUnit.equals("万元")) || (moneyUnit.equals("2"))) {
			/* 561 */f /= 10000.0D;
		}
		/* 563 */if ((moneyUnit.equals("亿元")) || (moneyUnit.equals("3"))) {
			/* 564 */f /= 100000000.0D;
		}
		/* 566 */if (digit == null) {
			/* 567 */nf.setMinimumFractionDigits(2);
			/* 568 */nf.setMaximumFractionDigits(2);
		} else {
			/* 571 */nf.setMinimumFractionDigits(Integer.parseInt(digit));
			/* 572 */nf.setMaximumFractionDigits(Integer.parseInt(digit));
		}
		/* 574 */return nf.format(f);
	}

	public static String doubleToCurrency1(double f, Locale l) {
		/* 628 */NumberFormat nf = (l == null) ? NumberFormat
				.getCurrencyInstance() :
		/* 629 */NumberFormat.getCurrencyInstance(l);
		/* 630 */return nf.format(f);
	}

	public static String doubleToCurrency2(double f, Locale l) {
		/* 642 */NumberFormat nf = (l == null) ? NumberFormat
				.getCurrencyInstance() :
		/* 643 */NumberFormat.getCurrencyInstance(l);
		/* 644 */String temp = nf.format(f);
		/* 645 */int i = temp.indexOf(nf.getCurrency().getSymbol());
		/* 646 */if (i > 0) {
			/* 647 */return temp.substring(0, i) + temp.substring(i + 1);
		}

		/* 650 */return temp.substring(1);
	}

	public static String doubleToCurrency3(double f, Locale l) {
		/* 661 */String currency = doubleToCurrency2(f, l);
		/* 662 */return replace(currency, ",", "");
	}

	public static String floatToCurrency1(float f, Locale l) {
		/* 673 */NumberFormat nf = (l == null) ? NumberFormat
				.getCurrencyInstance() :
		/* 674 */NumberFormat.getCurrencyInstance(l);
		/* 675 */return nf.format(f);
	}

	public static String floatToCurrency3(float f, Locale l) {
		/* 686 */NumberFormat nf = (l == null) ? NumberFormat
				.getCurrencyInstance() :
		/* 687 */NumberFormat.getCurrencyInstance(l);
		/* 688 */String currency = nf.format(f).substring(1);
		/* 689 */return replace(currency, ",", "");
	}

	public static String floatToCurrency2(float f, Locale l) {
		/* 702 */NumberFormat nf = (l == null) ? NumberFormat
				.getCurrencyInstance() :
		/* 703 */NumberFormat.getCurrencyInstance(l);
		/* 704 */return nf.format(f).substring(1);
	}

	public static String stringToCurrency1(String str, Locale l) {
		/* 715 */if ((str == null) || (str.equals(""))) {
			/* 716 */return "";
		}

		/* 719 */double f = 0.0D;
		try {
			/* 721 */f = Double.parseDouble(str);
		} catch (Exception ex) {
			/* 724 */return "";
		}

		/* 727 */return doubleToCurrency1(f, l);
	}

	public static String makeRightConditionStr(HashSet set, String column) {
		/* 1005 */if (set.contains("*")) {
			/* 1006 */return "";
		}
		/* 1008 */if ((set == null) || (set.size() == 0)) {
			/* 1009 */return column + " in ('')";
		}
		/* 1011 */if ((set != null) && (set.size() > 0)) {
			/* 1012 */Iterator it = set.iterator();
			/* 1013 */String temp = "";
			/* 1014 */while (it.hasNext()) {
				/* 1015 */String temp1 = (String) it.next();
				/* 1016 */temp = temp + "'" + temp1 + "',";
			}
			/* 1018 */temp = temp.substring(0, temp.length() - 1);
			/* 1019 */return column + " in (" + temp + ")";
		}

		/* 1022 */return "";
	}

	public static String makeRightConditionS(HashSet set, String column) {
		/* 1035 */if (set.contains("*")) {
			/* 1036 */return "";
		}
		/* 1038 */if ((set == null) || (set.size() == 0)) {
			/* 1039 */return " AND " + column + " IN ('')";
		}
		/* 1041 */if ((set != null) && (set.size() > 0)) {
			/* 1042 */Iterator it = set.iterator();
			/* 1043 */String temp = "";
			/* 1044 */while (it.hasNext()) {
				/* 1045 */String temp1 = (String) it.next();
				/* 1046 */temp = temp + "'" + temp1 + "',";
			}
			/* 1048 */temp = temp.substring(0, temp.length() - 1);
			/* 1049 */return " AND " + column + " IN (" + temp + ")";
		}

		/* 1052 */return "";
	}

	private static String getPass(String psPass) throws Exception {
		/* 1185 */psPass = psPass.trim();
		/* 1186 */if (psPass.length() < 3) {
			/* 1187 */throw new Exception("密码长度必须大于等于3");
		}
		/* 1189 */if (psPass.length() > 30) {
			/* 1190 */throw new Exception("密码长度必须小于等于30");
		}
		/* 1192 */int viTemp = psPass.charAt(0);
		/* 1193 */int viAscSum = 0;
		/* 1194 */for (int j = 0; j < psPass.length(); ++j) {
			/* 1195 */char vcChar = psPass.substring(j, j + 1).charAt(0);
			/* 1196 */viTemp = vcChar;
			/* 1197 */viAscSum += viTemp;
		}
		/* 1199 */int viSumMod = viAscSum % 26;
		/* 1200 */int viMaxMod = viSumMod;
		/* 1201 */String vsModStr = "";
		/* 1202 */String vsDevStr = "";
		/* 1203 */for (int k = 0; k < psPass.length(); ++k) {
			/* 1204 */char vcChar = psPass.substring(k, k + 1).charAt(0);
			/* 1205 */viTemp = vcChar + viSumMod;
			/* 1206 */int viDev = viTemp / 26;
			/* 1207 */int viMod = viTemp - (viDev * 26);
			/* 1208 */if (viDev > viMaxMod) {
				/* 1209 */viMaxMod = viDev;
			}
			/* 1211 */vsModStr = vsModStr + "," + (char) (viMod + 97);
			/* 1212 */vsDevStr = vsDevStr + "," + String.valueOf(viDev);
		}
		/* 1214 */vsModStr = vsModStr.substring(1);
		/* 1215 */vsDevStr = vsDevStr.substring(1);
		/* 1216 */String vsTemp = "";
		/* 1217 */StringTokenizer st = new StringTokenizer(vsDevStr,
		/* 1218 */",");
		do {
			/* 1220 */char vcChar = st.nextToken().charAt(0);
			/* 1221 */viTemp = Integer.parseInt(String.valueOf(vcChar));
			/* 1222 */vsTemp = vsTemp + "," + (char) viTemp;
		}
		/* 1219 */while (
		/* 1224 */st.hasMoreTokens());
		/* 1225 */vsDevStr = vsTemp.substring(1);

		/* 1227 */psPass = "";
		/* 1228 */int i = 0;
		/* 1229 */StringTokenizer st2 = new StringTokenizer(vsDevStr,
		/* 1230 */",");
		/* 1231 */StringTokenizer st3 = new StringTokenizer(vsModStr,
		/* 1232 */",");
		do {
			/* 1234 */++i;
			/* 1235 */if (i % 2 == 1) {
				/* 1236 */psPass = st2.nextToken() + st3.nextToken() + psPass;
			} else {
				/* 1239 */psPass = st3.nextToken() + st2.nextToken() + psPass;
			}
		}
		/* 1242 */while ((st2.hasMoreTokens()) && (
		/* 1242 */st3.hasMoreTokens()));
		/* 1243 */return psPass;
	}

	public static void main(String[] args) {
		String ss = "&amp;";
		System.out.println(str2TextXML(ss));
	}
}

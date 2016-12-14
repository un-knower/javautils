package com.cenrise.utils;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Random;

import org.apache.commons.lang.RandomStringUtils;

/**
 * 数学计算工具类
 * 
 * @author <a href="mailto:zhsj0110@163.com">zhoushijun</a>
 * 
 */
public class MathUtil {

	/**
	 * a除以b的整数,向上取整.
	 * 
	 * @param divHeader
	 *            int
	 * @param divUnder
	 *            int
	 * @return int
	 */
	public static int ceildiv(int divHeader, int divUnder) {
		return (int) Math.ceil((double) divHeader / (double) divUnder);

	}

	/**
	 * 返回两个数相除的结果,按照nSyo的格式输出. 格式说明参考NumberFormat的帮助.
	 * 
	 * @param divHeader
	 *            被除数
	 * @param divUnder
	 *            除数
	 * @param nSyo
	 *            格式
	 * @return String
	 */
	public static String mydiv(int divHeader, int divUnder, String nSyo) {
		if (divHeader == 0 || divUnder == 0) {
			return "0.0";
		}

		NumberFormat astr = NumberFormat.getInstance();
		((DecimalFormat) astr).applyPattern(nSyo);

		return astr.format((double) divHeader / (double) divUnder);
	}

	/**
	 * 得到一个随机整数,最大是n.
	 * 
	 * @param nMax
	 *            最大值
	 * @return 输出:随机整数
	 */
	public static int getRandom(int nMax) {
		Random hello;
		hello = new Random();
		return hello.nextInt(nMax);
	}

	/**
	 * 得到随机数,加上字符串前缀.
	 * 
	 * @param nMax
	 *            随机数的最大值
	 * @param strPre
	 *            字符串前缀
	 * @return 字符串前缀加上随机数
	 */
	public static String getRandom(int nMax, String strPre) {
		Random hello = new Random();

		String result = strPre + hello.nextInt(nMax);
		return result;
	}

	private static final int DEF_DIV_SCALE = 2;

	/**
	 * 判断字符串是否为数字
	 * 
	 * @param num
	 * @return
	 */
	public static boolean isNumberic(String num) {
		return (null == num || num.length() <= 0 || num.matches("\\d{1,}")) ? true
				: false;
	}

	/**
	 * @return 返回12位随机数
	 */
	public static String randomNumber() {
		return RandomStringUtils.randomNumeric(12);
	}

	/**
	 * @param parm
	 * @return 返回指定位数随机数
	 */
	public static String randomNumber(int parm) {
		return RandomStringUtils.randomNumeric(parm);
	}

	/**
	 * * 两个Double数相加 *
	 * 
	 * @param v1
	 *            *
	 * @param v2
	 *            *
	 * @return Double
	 */
	public static Double add(Double v1, Double v2) {
		BigDecimal b1 = new BigDecimal(v1.toString());
		BigDecimal b2 = new BigDecimal(v2.toString());
		return new Double(b1.add(b2).doubleValue());
	}

	/**
	 * * 两个Double数相减 *
	 * 
	 * @param v1
	 *            *
	 * @param v2
	 *            *
	 * @return Double
	 */
	public static Double sub(Double v1, Double v2) {
		BigDecimal b1 = new BigDecimal(v1.toString());
		BigDecimal b2 = new BigDecimal(v2.toString());
		return new Double(b1.subtract(b2).doubleValue());
	}

	/**
	 * * 两个Double数相乘 *
	 * 
	 * @param v1
	 *            *
	 * @param v2
	 *            *
	 * @return Double
	 */
	public static Double mul(Double v1, Double v2) {
		BigDecimal b1 = new BigDecimal(v1.toString());
		BigDecimal b2 = new BigDecimal(v2.toString());
		return new Double(b1.multiply(b2).doubleValue());
	}

	/**
	 * * 两个Double数相除 *
	 * 
	 * @param v1
	 *            *
	 * @param v2
	 *            *
	 * @return Double
	 */
	public static Double div(Double v1, Double v2) {
		BigDecimal b1 = new BigDecimal(v1.toString());
		BigDecimal b2 = new BigDecimal(v2.toString());
		return new Double(b1
				.divide(b2, DEF_DIV_SCALE, BigDecimal.ROUND_HALF_UP)
				.doubleValue());
	}

	/**
	 * * 两个Double数相除，并保留scale位小数 *
	 * 
	 * @param v1
	 *            *
	 * @param v2
	 *            *
	 * @param scale
	 *            *
	 * @return Double
	 */
	public static Double div(Double v1, Double v2, int scale) {
		if (scale < 0) {
			throw new IllegalArgumentException(
					"The scale must be a positive integer or zero");
		}
		BigDecimal b1 = new BigDecimal(v1.toString());
		BigDecimal b2 = new BigDecimal(v2.toString());
		return new Double(b1.divide(b2, scale, BigDecimal.ROUND_HALF_UP)
				.doubleValue());
	}

	/**
	 * @param v1
	 * @return 返回指定Double的负数
	 */
	public static Double neg(Double v1) {
		return sub(new Double(0), v1);
	}

	/**
	 * 解析数字，如5-8，返回集合5，6，7，8
	 * 
	 * @param str
	 *            字符
	 * @return 分解后的集合，当str为null时返回null，不包含“-”时返回空集合
	 */
	public static String[] parseString(String str) {
		if (str == null) {
			return null;
		}
		String[] strNums = str.split("-");
		Integer intStart = Integer.valueOf(strNums[0]);
		Integer intStop = Integer.valueOf(strNums[1]);
		String[] strs = new String[intStop - intStart + 1];
		int j = 0;
		for (int i = intStart; i <= intStop; i++) {
			strs[j++] = String.valueOf(i);
		}
		return strs;
	}

	/**
	 * 如果是连续的数据转换成*-*的形势，如4，5，6转换为4-6
	 * 
	 * @param str
	 * @return
	 */
	public static String groupContinueNumber(String str) {
		if (str == null) {
			return null;
		}
		String[] strs = str.split(",");

		if (!str.contains(",") || strs.length < 2) {
			return str;
		}
		for (int i = 1; i < strs.length; i++) {
			Integer inteValue = Integer.valueOf(strs[i])
					- Integer.valueOf(strs[i - 1]);
			if (inteValue != 1) {
				return str;
			}
		}
		return strs[0] + "-" + strs[strs.length - 1];
	}

	public static void main(String args) {
		String s = groupContinueNumber("1,2,3,4");
		System.out.println(s);
	}

}
package com.cenrise.commons.utils.example;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

/**
 * personinfo表数据生成
 * 
 * @author Admin
 *
 */
/**
 DROP TABLE IF EXISTS `personinfo`;
CREATE TABLE `personinfo` (
  `id` int(100) NOT NULL,
  `name` varchar(100) DEFAULT NULL,
  `birthday` datetime DEFAULT NULL COMMENT '信息表测试用',
  `phone` varchar(20) DEFAULT NULL,
  UNIQUE KEY `id` (`id`) USING BTREE,
  KEY `birthday` (`birthday`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=gb2312;
 */

public class CreateTableTest1 {
	/**
	 * 返回手机号码
	 */
	private static String[] telFirst = "134,135,136,137,138,139,150,151,152,157,158,159,130,131,132,155,156,133,153"
			.split(",");

	public static void main(String[] args) {

		// 格式化一下日期
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");// 设置日期格式
		// System.out.println(df.format(RandomValue.randomDate("1000-01-01",
		// "2015-10-01")));

		for (int i = 201; i <401 ; i++) {// 10000000
			String birthday = df.format(randomDate("2010-10-01", "2020-10-01"));
			String dataSql = "insert into personinfo2(id,name,birthday,phone) values ("
					+ i
					+ ",'"
					+ getCharAndNumr(6)
					+ "','"
					+ birthday
					+ "','"
					+ getTel() + "');";
			System.out.println(dataSql);
		}
	}

	public static Date randomDate(String beginDate, String endDate) {
		try {
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
			Date start = format.parse(beginDate);// 构造开始日期
			Date end = format.parse(endDate);// 构造结束日期
			// getTime()表示返回自 1970 年 1 月 1 日 00:00:00 GMT 以来此 Date 对象表示的毫秒数。
			if (start.getTime() >= end.getTime()) {
				return null;
			}
			long date = random(start.getTime(), end.getTime());

			return new Date(date);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	private static long random(long begin, long end) {
		long rtn = begin + (long) (Math.random() * (end - begin));
		// 如果返回的是开始时间和结束时间，则递归调用本函数查找随机值
		if (rtn == begin || rtn == end) {
			return random(begin, end);
		}
		return rtn;
	}

	/**
	 * java生成随机数字和字母组合
	 * 
	 * @param length
	 *            [生成随机数的长度]
	 * @return
	 */
	public static String getCharAndNumr(int length) {
		String val = "";
		Random random = new Random();
		for (int i = 0; i < length; i++) {
			// 输出字母还是数字
			String charOrNum = random.nextInt(2) % 2 == 0 ? "char" : "num";
			// 字符串
			if ("char".equalsIgnoreCase(charOrNum)) {
				// 取得大写字母还是小写字母
				int choice = random.nextInt(2) % 2 == 0 ? 65 : 97;
				val += (char) (choice + random.nextInt(26));
			} else if ("num".equalsIgnoreCase(charOrNum)) { // 数字
				val += String.valueOf(random.nextInt(10));
			}
		}
		return val;
	}

	public static String getTel() {
		int index = getNum(0, telFirst.length - 1);
		String first = telFirst[index];
		String second = String.valueOf(getNum(1, 888) + 10000).substring(1);
		String thrid = String.valueOf(getNum(1, 9100) + 10000).substring(1);
		return first + second + thrid;
	}

	public static int getNum(int start, int end) {
		return (int) (Math.random() * (end - start + 1) + start);
	}

}

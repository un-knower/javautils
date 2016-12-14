package com.cenrise.utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author:Neptune Description:DateUtil 提供一些常用的时间想法的方法
 */
public class DateUtil {

	/**
	 * ORA标准时间格式
	 */
	private static final SimpleDateFormat ORA_DATE_TIME_FORMAT = new SimpleDateFormat(
			"yyyyMMdd");
	/**
	 * 带时分秒的ORA标准时间格式
	 */
	private static final SimpleDateFormat ORA_DATE_TIME_EXTENDED_FORMAT = new SimpleDateFormat(
			"yyyyMMddHHmmss");
	private static final SimpleDateFormat ORA_DATE_TIME_FORMAT_CHINEASE = new SimpleDateFormat(
			"yyyy年MM月dd日");
	private static final long ONE_DAY = 24 * 60 * 60 * 1000;
	private static final long ONE_HOUR = 60 * 60 * 1000;
	private static final long ONE_MIN = 60 * 1000;

	// 日期时间类型格式
	private static String DATETIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
	// 日期类型格式
	private static String DATE_FORMAT = "yyyy-MM-dd";
	// 时间类型的格式
	private static String TIME_FORMAT = "HH:mm:ss";
	// 注意SimpleDateFormat不是线程安全的
	private static ThreadLocal<SimpleDateFormat> ThreadDateTime = new ThreadLocal<SimpleDateFormat>();
	private static ThreadLocal<SimpleDateFormat> ThreadDate = new ThreadLocal<SimpleDateFormat>();
	private static ThreadLocal<SimpleDateFormat> ThreadTime = new ThreadLocal<SimpleDateFormat>();

	private static SimpleDateFormat DateTimeInstance() {
		SimpleDateFormat df = ThreadDateTime.get();
		if (df == null) {
			df = new SimpleDateFormat(DATETIME_FORMAT);
			ThreadDateTime.set(df);
		}
		return df;
	}

	private static SimpleDateFormat DateInstance() {
		SimpleDateFormat df = ThreadDate.get();
		if (df == null) {
			df = new SimpleDateFormat(DATE_FORMAT);
			ThreadDate.set(df);
		}
		return df;
	}

	private static SimpleDateFormat TimeInstance() {
		SimpleDateFormat df = ThreadTime.get();
		if (df == null) {
			df = new SimpleDateFormat(TIME_FORMAT);
			ThreadTime.set(df);
		}
		return df;
	}

	/**
	 * 获取当前日期时间
	 *
	 * @return 返回当前时间的字符串值
	 */
	public static String getcurrentDateTime() {
		return DateTimeInstance().format(new Date());
	}

	/**
	 * 将指定的时间格式化成出返回
	 *
	 * @param date
	 * @return
	 */
	public static String DateTime(Date date) {
		return DateTimeInstance().format(date);
	}

	/**
	 * 将指定的字符串解析为时间类型
	 *
	 * @param datestr
	 * @return
	 * @throws ParseException
	 */
	public static Date DateTime(String datestr) throws ParseException {
		return DateTimeInstance().parse(datestr);
	}

	/**
	 * 获取当前的日期
	 *
	 * @return
	 */
	public static String getcurrentDate() {
		return DateInstance().format(new Date());
	}

	/**
	 * 将指定的时间格式化成出返回
	 *
	 * @param date
	 * @return
	 */
	public static String Date(Date date) {
		return DateInstance().format(date);
	}

	/**
	 * 将指定的字符串解析为时间类型
	 *
	 * @param dateStr
	 * @return
	 * @throws ParseException
	 */
	public static Date Date(String dateStr) throws ParseException {
		return DateInstance().parse(dateStr);
	}

	/**
	 * 获取当前的时间
	 *
	 * @return
	 */
	public static String Time() {
		return TimeInstance().format(new Date());
	}

	/**
	 * 讲指定的时间格式化成出返回
	 *
	 * @param date
	 * @return
	 */
	public static String Time(Date date) {
		return TimeInstance().format(date);
	}

	/**
	 * 将指定的字符串解析为时间类型
	 *
	 * @param dateStr
	 * @return
	 * @throws ParseException
	 */
	public static Date Time(String dateStr) throws ParseException {
		return TimeInstance().parse(dateStr);
	}

	/**
	 * 在当前时间的基础上加或减去year年
	 *
	 * @param year
	 * @return
	 */
	public static Date year(int year) {
		java.util.Calendar Cal = java.util.Calendar.getInstance();
		Cal.setTime(new Date());
		Cal.add(Calendar.YEAR, year);
		return Cal.getTime();
	}

	/**
	 * 在指定的时间上加或减去几年
	 *
	 * @param date
	 * @param year
	 * @return
	 */
	public static Date year(Date date, int year) {
		java.util.Calendar Cal = java.util.Calendar.getInstance();
		Cal.setTime(date);
		Cal.add(java.util.Calendar.YEAR, year);
		return Cal.getTime();
	}

	/**
	 * 在当前时间的基础上加或减去几月
	 *
	 * @param month
	 * @return
	 */
	public static Date month(int month) {
		java.util.Calendar Cal = java.util.Calendar.getInstance();
		Cal.setTime(new Date());
		Cal.add(Calendar.MONTH, month);
		return Cal.getTime();
	}

	/**
	 * 在指定的时间上加或减去几月
	 *
	 * @param date
	 * @param month
	 * @return
	 */
	public static Date month(Date date, int month) {
		java.util.Calendar Cal = java.util.Calendar.getInstance();
		Cal.setTime(date);
		Cal.add(java.util.Calendar.MONTH, month);
		return Cal.getTime();
	}

	/**
	 * 在当前时间的基础上加或减去几天
	 *
	 * @param day
	 * @return
	 */
	public static Date day(int day) {
		java.util.Calendar Cal = java.util.Calendar.getInstance();
		Cal.setTime(new Date());
		Cal.add(Calendar.DAY_OF_YEAR, day);
		return Cal.getTime();
	}

	/**
	 * 在指定的时间上加或减去几天
	 *
	 * @param date
	 * @param day
	 * @return
	 */
	public static Date day(Date date, int day) {
		java.util.Calendar Cal = java.util.Calendar.getInstance();
		Cal.setTime(date);
		Cal.add(java.util.Calendar.DAY_OF_YEAR, day);
		return Cal.getTime();
	}

	/**
	 * 在当前时间的基础上加或减去几小时-支持浮点数
	 *
	 * @param hour
	 * @return
	 */
	public static Date hour(float hour) {
		java.util.Calendar Cal = java.util.Calendar.getInstance();
		Cal.setTime(new Date());
		Cal.add(java.util.Calendar.MINUTE, (int) (hour * 60));
		return Cal.getTime();
	}

	/**
	 * 在制定的时间上加或减去几小时-支持浮点数
	 *
	 * @param date
	 * @param hour
	 * @return
	 */
	public static Date hour(Date date, float hour) {
		java.util.Calendar Cal = java.util.Calendar.getInstance();
		Cal.setTime(date);
		Cal.add(java.util.Calendar.MINUTE, (int) (hour * 60));
		return Cal.getTime();
	}

	/**
	 * 在当前时间的基础上加或减去几分钟
	 *
	 * @param minute
	 * @return
	 */
	public static Date Minute(int minute) {
		java.util.Calendar Cal = java.util.Calendar.getInstance();
		Cal.setTime(new Date());
		Cal.add(java.util.Calendar.MINUTE, minute);
		return Cal.getTime();
	}

	/**
	 * 在制定的时间上加或减去几分钟
	 *
	 * @param date
	 * @param minute
	 * @return
	 */
	public static Date Minute(Date date, int minute) {
		java.util.Calendar Cal = java.util.Calendar.getInstance();
		Cal.setTime(date);
		Cal.add(java.util.Calendar.MINUTE, minute);
		return Cal.getTime();
	}

	/**
	 * 判断字符串是否为日期字符串
	 *
	 * @param date
	 *            日期字符串
	 * @return true or false
	 */
	public static boolean isDate(String date) {
		try {
			DateTimeInstance().parse(date);
			return true;
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * 时间date1和date2的时间差-单位秒
	 *
	 * @param date1
	 * @param date2
	 * @return 秒
	 */
	public static long Subtract(Date date1, Date date2) {
		long cha = (date2.getTime() - date1.getTime()) / 1000;
		return cha;
	}

	/**
	 * 时间date1和date2的时间差-单位秒
	 *
	 * @param date1
	 * @param date2
	 * @return 秒
	 */
	public static long Subtract(String date1, String date2) {
		long rs = 0;
		try {
			Date start = DateTimeInstance().parse(date1);
			Date end = DateTimeInstance().parse(date2);
			long cha = (end.getTime() - start.getTime()) / 1000;
			rs = cha;
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return rs;
	}

	/**
	 * 时间date1和date2的时间差 -单位分钟
	 *
	 * @param date1
	 * @param date2
	 * @return 分钟
	 */
	public static int SubtractMinute(String date1, String date2) {
		int rs = 0;
		try {
			Date start = DateTimeInstance().parse(date1);
			Date end = DateTimeInstance().parse(date2);
			long cha = (end.getTime() - start.getTime()) / 1000;
			rs = (int) cha / (60);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return rs;
	}

	/**
	 * 时间date1和date2的时间差-单位分钟
	 *
	 * @param date1
	 * @param date2
	 * @return 分钟
	 */
	public static int SubtractMinute(Date date1, Date date2) {
		long cha = date2.getTime() - date1.getTime();
		return (int) cha / (1000 * 60);
	}

	/**
	 * 时间date1和date2的时间差-单位小时
	 *
	 * @param date1
	 * @param date2
	 * @return 小时
	 */
	public static int SubtractHour(Date date1, Date date2) {
		long cha = (date2.getTime() - date1.getTime()) / 1000;
		return (int) cha / (60 * 60);
	}

	/**
	 * 时间date1和date2的时间差-单位小时
	 *
	 * @param date1
	 * @param date2
	 * @return 小时
	 */
	public static int SubtractHour(String date1, String date2) {
		int rs = 0;
		try {
			Date start = DateTimeInstance().parse(date1);
			Date end = DateTimeInstance().parse(date2);
			long cha = (end.getTime() - start.getTime()) / 1000;
			rs = (int) cha / (60 * 60);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return rs;
	}

	/**
	 * 时间date1和date2的时间差-单位天
	 *
	 * @param date1
	 * @param date2
	 * @return 天
	 */
	public static int SubtractDay(String date1, String date2) {
		int rs = 0;
		try {
			Date start = DateTimeInstance().parse(date1);
			Date end = DateTimeInstance().parse(date2);
			long sss = (end.getTime() - start.getTime()) / 1000;
			rs = (int) sss / (60 * 60 * 24);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return rs;
	}

	/**
	 * 时间date1和date2的时间差-单位天
	 *
	 * @param date1
	 * @param date2
	 * @return 天
	 */
	public static int SubtractDay(Date date1, Date date2) {
		long cha = date2.getTime() - date1.getTime();
		return (int) cha / (1000 * 60 * 60 * 24);
	}

	/**
	 * 时间date1和date2的时间差-单位月
	 *
	 * @param date1
	 * @param date2
	 * @return 月
	 */
	public static int SubtractMonth(String date1, String date2) {
		int result;
		Calendar c1 = Calendar.getInstance();
		Calendar c2 = Calendar.getInstance();
		try {
			c1.setTime(DateInstance().parse(date1));
			c2.setTime(DateInstance().parse(date2));
			int year1 = c1.get(Calendar.YEAR);
			int month1 = c1.get(Calendar.MONTH);
			int year2 = c2.get(Calendar.YEAR);
			int month2 = c2.get(Calendar.MONTH);
			if (year1 == year2) {
				result = month2 - month1;
			} else {
				result = 12 * (year2 - year1) + month2 - month1;
			}
		} catch (ParseException e) {
			e.printStackTrace();
			result = -1;
		}
		return result;
	}

	/**
	 * 时间date1和date2的时间差-单位月
	 *
	 * @param date1
	 * @param date2
	 * @return 月
	 */
	public static int SubtractMonth(Date date1, Date date2) {
		int result;
		Calendar c1 = Calendar.getInstance();
		Calendar c2 = Calendar.getInstance();
		c1.setTime(date1);
		c2.setTime(date2);
		int year1 = c1.get(Calendar.YEAR);
		int month1 = c1.get(Calendar.MONTH);
		int year2 = c2.get(Calendar.YEAR);
		int month2 = c2.get(Calendar.MONTH);
		if (year1 == year2) {
			result = month2 - month1;
		} else {
			result = 12 * (year2 - year1) + month2 - month1;
		}
		return result;
	}

	/**
	 * 时间date1和date2的时间差-单位年
	 *
	 * @param date1
	 * @param date2
	 * @return 年
	 */
	public static int SubtractYear(String date1, String date2) {
		int result;
		Calendar c1 = Calendar.getInstance();
		Calendar c2 = Calendar.getInstance();
		try {
			c1.setTime(DateInstance().parse(date1));
			c2.setTime(DateInstance().parse(date2));
			int year1 = c1.get(Calendar.YEAR);
			int year2 = c2.get(Calendar.YEAR);
			result = year2 - year1;
		} catch (ParseException e) {
			e.printStackTrace();
			result = -1;
		}
		return result;
	}

	/**
	 * 时间date1和date2的时间差-单位年
	 *
	 * @param date1
	 * @param date2
	 * @return 年
	 */
	public static int SubtractYear(Date date1, Date date2) {
		int result;
		Calendar c1 = Calendar.getInstance();
		Calendar c2 = Calendar.getInstance();
		c1.setTime(date1);
		c2.setTime(date2);
		int year1 = c1.get(Calendar.YEAR);
		int year2 = c2.get(Calendar.YEAR);
		result = year2 - year1;
		return result;
	}

	/**
	 * 获取俩个时间的查结果用时秒表示
	 *
	 * @param date1
	 * @param date2
	 * @return 几小时:几分钟:几秒钟
	 * @Summary:此处可以讲计算结果包装成一个结构体返回便于格式化
	 */
	public static String SubtractTime(String date1, String date2) {
		String result = "";
		try {
			Date start = DateTimeInstance().parse(date1);
			Date end = DateTimeInstance().parse(date2);
			long sss = (end.getTime() - start.getTime()) / 1000;
			int hh = (int) sss / (60 * 60);
			int mm = (int) (sss - hh * 60 * 60) / (60);
			int ss = (int) (sss - hh * 60 * 60 - mm * 60);
			result = hh + ":" + mm + ":" + ss;
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return result;
	}

	/**
	 * 获取俩个时间的查结果用时秒表示
	 *
	 * @param date1
	 * @param date2
	 * @return 几天-几小时:几分钟:几秒钟
	 * @Summary:此处可以讲计算结果包装成一个结构体返回便于格式化
	 */
	public static String SubtractDate(String date1, String date2) {
		String result = "";
		try {
			Date start = DateTimeInstance().parse(date1);
			Date end = DateTimeInstance().parse(date2);
			long sss = (end.getTime() - start.getTime()) / 1000;
			int dd = (int) sss / (60 * 60 * 24);
			int hh = (int) (sss - dd * 60 * 60 * 24) / (60 * 60);
			int mm = (int) (sss - dd * 60 * 60 * 24 - hh * 60 * 60) / (60);
			int ss = (int) (sss - dd * 60 * 60 * 24 - hh * 60 * 60 - mm * 60);
			result = dd + "-" + hh + ":" + mm + ":" + ss;
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return result;
	}

	/**
	 * 获取俩个时间之前的相隔的天数
	 *
	 * @param startTime
	 * @param endTime
	 * @return
	 * @throws ParseException
	 */
	public static int subDay(Date startTime, Date endTime) {
		int days = 0;
		Calendar can1 = Calendar.getInstance();
		can1.setTime(startTime);
		Calendar can2 = Calendar.getInstance();
		can2.setTime(endTime);
		int year1 = can1.get(Calendar.YEAR);
		int year2 = can2.get(Calendar.YEAR);

		Calendar can = null;
		if (can1.before(can2)) {
			days -= can1.get(Calendar.DAY_OF_YEAR);
			days += can2.get(Calendar.DAY_OF_YEAR);
			can = can1;
		} else {
			days -= can2.get(Calendar.DAY_OF_YEAR);
			days += can1.get(Calendar.DAY_OF_YEAR);
			can = can2;
		}
		for (int i = 0; i < Math.abs(year2 - year1); i++) {
			days += can.getActualMaximum(Calendar.DAY_OF_YEAR);
			can.add(Calendar.YEAR, 1);
		}

		return days;
	}

	/**
	 * 获取俩个时间之前的相隔的天数
	 *
	 * @param startTime
	 * @param endTime
	 * @return
	 * @throws ParseException
	 */
	public static int subDay(String startTime, String endTime) {
		int days = 0;
		try {
			Date date1 = DateInstance().parse(
					DateInstance().format(DateTimeInstance().parse(startTime)));
			Date date2 = DateInstance().parse(
					DateInstance().format(DateTimeInstance().parse(endTime)));
			Calendar can1 = Calendar.getInstance();
			can1.setTime(date1);
			Calendar can2 = Calendar.getInstance();
			can2.setTime(date2);
			int year1 = can1.get(Calendar.YEAR);
			int year2 = can2.get(Calendar.YEAR);

			Calendar can = null;
			if (can1.before(can2)) {
				days -= can1.get(Calendar.DAY_OF_YEAR);
				days += can2.get(Calendar.DAY_OF_YEAR);
				can = can1;
			} else {
				days -= can2.get(Calendar.DAY_OF_YEAR);
				days += can1.get(Calendar.DAY_OF_YEAR);
				can = can2;
			}
			for (int i = 0; i < Math.abs(year2 - year1); i++) {
				days += can.getActualMaximum(Calendar.DAY_OF_YEAR);
				can.add(Calendar.YEAR, 1);
			}

		} catch (ParseException e) {
			e.printStackTrace();
		}
		return days;
	}

	/**
	 * 返回俩个时间在时间段(例如每天的08:00-18:00)的时长-单位秒
	 *
	 * @param startDate
	 * @param endDate
	 * @param timeBurst
	 *            只就按该时间段内的08:00-18:00时长
	 * @return 计算后的秒数
	 * @throws ParseException
	 * @summary 格式错误返回0
	 */
	public static long subtimeBurst(String startDate, String endDate,
			String timeBurst) throws ParseException {
		Date start = DateTimeInstance().parse(startDate);
		Date end = DateTimeInstance().parse(endDate);
		return subtimeBurst(start, end, timeBurst);
	}

	/**
	 * 返回俩个时间在时间段(例如每天的08:00-18:00)的时长-单位秒
	 *
	 * @param startDate
	 * @param endDate
	 * @param timeBurst
	 *            只就按该时间段内的08:00-18:00时长
	 * @return 计算后的秒数
	 * @throws ParseException
	 */
	public static long subtimeBurst(Date startDate, Date endDate,
			String timeBurst) throws ParseException {
		long second = 0;
		Pattern p = Pattern.compile("^\\d{2}:\\d{2}-\\d{2}:\\d{2}");
		Matcher m = p.matcher(timeBurst);
		boolean falg = false;
		if (startDate.after(endDate)) {
			Date temp = startDate;
			startDate = endDate;
			endDate = temp;
			falg = true;
		}
		if (m.matches()) {
			String[] a = timeBurst.split("-");
			int day = subDay(startDate, endDate);
			if (day > 0) {
				long firstMintues = 0;
				long lastMintues = 0;
				long daySecond = 0;
				String strDayStart = DateInstance().format(startDate) + " "
						+ a[0] + ":00";
				String strDayEnd = DateInstance().format(startDate) + " "
						+ a[1] + ":00";
				Date dayStart = DateTimeInstance().parse(strDayStart);
				Date dayEnd = DateTimeInstance().parse(strDayEnd);
				daySecond = Subtract(dayStart, dayEnd);
				if ((startDate.after(dayStart) || startDate.equals(dayStart))
						&& startDate.before(dayEnd)) {
					firstMintues = (dayEnd.getTime() - startDate.getTime()) / 1000;
				} else if (startDate.before(dayStart)) {
					firstMintues = (dayEnd.getTime() - dayStart.getTime()) / 1000;
				}
				dayStart = DateTimeInstance().parse(
						DateInstance().format(endDate) + " " + a[0] + ":00");
				dayEnd = DateTimeInstance().parse(
						DateInstance().format(endDate) + " " + a[1] + ":00");
				if (endDate.after(dayStart)
						&& (endDate.before(dayEnd) || endDate.equals(dayEnd))) {
					lastMintues = (endDate.getTime() - dayStart.getTime()) / 1000;
				} else if (endDate.after(dayEnd)) {
					lastMintues = (dayEnd.getTime() - dayStart.getTime()) / 1000;
				}
				// 第一天的秒数 + 最好一天的秒数 + 天数*全天的秒数
				second = firstMintues + lastMintues;
				second += (day - 1) * daySecond;
			} else {
				String strDayStart = DateInstance().format(startDate) + " "
						+ a[0] + ":00";
				String strDayEnd = DateInstance().format(startDate) + " "
						+ a[1] + ":00";
				Date dayStart = DateTimeInstance().parse(strDayStart);
				Date dayEnd = DateTimeInstance().parse(strDayEnd);
				if ((startDate.after(dayStart) || startDate.equals(dayStart))
						&& startDate.before(dayEnd) && endDate.after(dayStart)
						&& (endDate.before(dayEnd) || endDate.equals(dayEnd))) {
					second = (endDate.getTime() - startDate.getTime()) / 1000;
				} else {
					if (startDate.before(dayStart)) {
						if (endDate.before(dayEnd)) {
							second = (endDate.getTime() - dayStart.getTime()) / 1000;
						} else {
							second = (dayEnd.getTime() - dayStart.getTime()) / 1000;
						}
					}
					if (startDate.after(dayStart)) {
						if (endDate.before(dayEnd)) {
							second = (endDate.getTime() - startDate.getTime()) / 1000;
						} else {
							second = (dayEnd.getTime() - startDate.getTime()) / 1000;
						}
					}
				}
				if ((startDate.before(dayStart) && endDate.before(dayStart))
						|| startDate.after(dayEnd) && endDate.after(dayEnd)) {
					second = 0;
				}
			}
		} else {
			second = (endDate.getTime() - startDate.getTime()) / 1000;
		}
		if (falg) {
			second = Long.parseLong("-" + second);
		}
		return second;
	}

	/**
	 * 时间Date在时间段(例如每天的08:00-18:00)上增加或减去second秒
	 *
	 * @param date
	 * @param second
	 * @param timeBurst
	 * @return 计算后的时间
	 * @Suumary 指定的格式错误后返回原数据
	 */
	public static Date calculate(String date, int second, String timeBurst) {
		Date start = null;
		try {
			start = DateTimeInstance().parse(date);
			return calculate(start, second, timeBurst);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return new Date();
	}

	/**
	 * 时间Date在时间段(例如每天的08:00-18:00)上增加或减去second秒
	 *
	 * @param date
	 * @param second
	 * @param timeBurst
	 * @return 计算后的时间
	 * @Suumary 指定的格式错误后返回原数据
	 */
	public static Date calculate(Date date, int second, String timeBurst) {
		Pattern p = Pattern.compile("^\\d{2}:\\d{2}-\\d{2}:\\d{2}");
		Matcher m = p.matcher(timeBurst);
		java.util.Calendar cal = java.util.Calendar.getInstance();
		if (m.matches()) {
			String[] a = timeBurst.split("-");
			try {
				Date dayStart = DateTimeInstance().parse(
						DateInstance().format(date) + " " + a[0] + ":00");
				Date dayEnd = DateTimeInstance().parse(
						DateInstance().format(date) + " " + a[1] + ":00");
				int DaySecond = (int) Subtract(dayStart, dayEnd);
				int toDaySecond = (int) Subtract(dayStart, dayEnd);
				if (second >= 0) {
					if ((date.after(dayStart) || date.equals(dayStart))
							&& (date.before(dayEnd) || date.equals(dayEnd))) {
						cal.setTime(date);
						toDaySecond = (int) Subtract(date, dayEnd);
					}
					if (date.before(dayStart)) {
						cal.setTime(dayStart);
						toDaySecond = (int) Subtract(dayStart, dayEnd);
					}
					if (date.after(dayEnd)) {
						cal.setTime(day(dayStart, 1));
						toDaySecond = 0;
					}

					if (second > toDaySecond) {
						int day = (second - toDaySecond) / DaySecond;
						int remainder = (second - toDaySecond) % DaySecond;
						cal.setTime(day(dayStart, 1));
						cal.add(Calendar.DAY_OF_YEAR, day);
						cal.add(Calendar.SECOND, remainder);
					} else {
						cal.add(Calendar.SECOND, second);
					}

				} else {
					if ((date.after(dayStart) || date.equals(dayStart))
							&& (date.before(dayEnd) || date.equals(dayEnd))) {
						cal.setTime(date);
						toDaySecond = (int) Subtract(date, dayStart);
					}
					if (date.before(dayStart)) {
						cal.setTime(day(dayEnd, -1));
						toDaySecond = 0;
					}
					if (date.after(dayEnd)) {
						cal.setTime(dayEnd);
						toDaySecond = (int) Subtract(dayStart, dayEnd);
					}
					if (Math.abs(second) > Math.abs(toDaySecond)) {
						int day = (Math.abs(second) - Math.abs(toDaySecond))
								/ DaySecond;
						int remainder = (Math.abs(second) - Math
								.abs(toDaySecond)) % DaySecond;
						cal.setTime(day(dayEnd, -1));
						cal.add(Calendar.DAY_OF_YEAR,
								Integer.valueOf("-" + day));
						cal.add(Calendar.SECOND,
								Integer.valueOf("-" + remainder));
					} else {
						cal.add(Calendar.SECOND, second);
					}
				}
			} catch (ParseException e) {
				e.printStackTrace();
			}
		} else {
			cal.setTime(date);
		}
		return cal.getTime();
	}

	/**
	 * 将两个格式为HH:MM:SS的时间字符串相加，例如：00:59:06 + 01:00:59 返回 02:00:05。
	 * 
	 * @param time1
	 *            要累计的时间字符串
	 * @param time2
	 *            要累计的时间字符串
	 * 
	 * @return 累计后的时间字符串
	 */
	public static String addTwoTimeStr(String time1, String time2) {

		String returnStr = "00:00:00";
		if (time1 != null && !time1.equalsIgnoreCase("") && time2 != null
				&& !time2.equalsIgnoreCase("")) {
			String[] time1Array = time1.split(":");
			String[] time2Array = time2.split(":");
			int hour1 = (new Integer(time1Array[0])).intValue();
			int hour2 = (new Integer(time2Array[0])).intValue();
			int min1 = (new Integer(time1Array[1])).intValue();
			int min2 = (new Integer(time2Array[1])).intValue();
			int sec1 = (new Integer(time1Array[2])).intValue();
			int sec2 = (new Integer(time2Array[2])).intValue();

			String lastSec, lastMin, lastHour;

			int totalSec = sec1 + sec2;
			if (totalSec / 60 > 0) {
				// 超过1分钟的时间累计到min1中
				min1 = min1 + totalSec / 60;
			}
			if (totalSec % 60 > 9) {
				lastSec = new Integer(totalSec % 60).toString();
			} else {
				lastSec = new String("0"
						+ new Integer(totalSec % 60).toString());
			}

			int totalMin = min1 + min2;
			if (totalMin / 60 > 0) {
				// 超过1分钟的时间累计到hour1中
				hour1 = hour1 + totalMin / 60;
			}
			if (totalMin % 60 > 9) {
				lastMin = new Integer(totalMin % 60).toString();
			} else {
				lastMin = new String("0"
						+ new Integer(totalMin % 60).toString());
			}

			int totalHour = hour1 + hour2;
			if (totalHour % 24 > 9) {
				lastHour = new Integer(totalHour % 24).toString();
			} else {
				lastHour = new String("0"
						+ new Integer(totalHour % 24).toString());
			}

			returnStr = lastHour + ":" + lastMin + ":" + lastSec;
		} else if (time1 != null && !time1.equalsIgnoreCase("")) {
			returnStr = time1.substring(0, 8);
		} else if (time2 != null && !time2.equalsIgnoreCase("")) {
			returnStr = time2.substring(0, 8);
		} else {
			returnStr = "00:00:00";
		}

		return returnStr;
	}

	/**
	 * 创建一个标准ORA时间格式的克隆
	 * 
	 * @return 标准ORA时间格式的克隆
	 */
	@SuppressWarnings("unused")
	private static synchronized DateFormat getOraDateTimeFormat() {
		SimpleDateFormat theDateTimeFormat = (SimpleDateFormat) ORA_DATE_TIME_FORMAT
				.clone();
		theDateTimeFormat.setLenient(false);
		return theDateTimeFormat;
	}

	/**
	 * 创建一个带分秒的ORA时间格式的克隆
	 * 
	 * @return 标准ORA时间格式的克隆
	 */
	private static synchronized DateFormat getOraExtendDateTimeFormat() {
		SimpleDateFormat theDateTimeFormat = (SimpleDateFormat) ORA_DATE_TIME_EXTENDED_FORMAT
				.clone();
		theDateTimeFormat.setLenient(false);
		return theDateTimeFormat;
	}

	/**
	 * @return 返回 oRA_DATE_TIME_FORMAT_CHINEASE。
	 */
	private static synchronized SimpleDateFormat getOraChineaseDateTimeFormat() {
		SimpleDateFormat theDateTimeFormat = (SimpleDateFormat) ORA_DATE_TIME_FORMAT_CHINEASE
				.clone();
		theDateTimeFormat.setLenient(false);
		return theDateTimeFormat;
	}

	/**
	 * 得到系统当前的日期 格式为YYYY-MM-DD
	 * 
	 * @return 系统当前的日期 格式为YYYY-MM-DD
	 */
	public static String getSystemCurrentDate() {
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(System.currentTimeMillis());
		return doTransform(calendar.get(Calendar.YEAR),
				calendar.get(Calendar.MONTH) + 1,
				calendar.get(Calendar.DAY_OF_MONTH));
	}

	/**
	 * 返回格式为YYYY-MM-DD
	 * 
	 * @param year
	 *            年
	 * @param month
	 *            月
	 * @param day
	 *            日
	 * @return
	 */
	private static String doTransform(int year, int month, int day) {
		StringBuffer result = new StringBuffer();
		result.append(String.valueOf(year))
				.append("-")
				.append(month < 10 ? "0" + String.valueOf(month) : String
						.valueOf(month))
				.append("-")
				.append(day < 10 ? "0" + String.valueOf(day) : String
						.valueOf(day));
		return result.toString();
	}

	/**
	 * 得到系统当前的日期和时间 格式为YYYY-MM-DD hh:mm:ss
	 * 
	 * @return 格式为YYYY-MM-DD hh:mm:ss的系统当前的日期和时间
	 */
	public static final String getSystemCurrentDateTime() {
		Calendar newCalendar = Calendar.getInstance();
		newCalendar.setTimeInMillis(System.currentTimeMillis());
		int year = newCalendar.get(Calendar.YEAR);
		int month = newCalendar.get(Calendar.MONTH) + 1;
		int day = newCalendar.get(Calendar.DAY_OF_MONTH);
		int hour = newCalendar.get(Calendar.HOUR_OF_DAY);
		int minute = newCalendar.get(Calendar.MINUTE);
		int second = newCalendar.get(Calendar.SECOND);
		return doTransform(year, month, day, hour, minute, second);
	}

	public static final String getSimpleSystemCurrentDateTime() {
		Calendar newCalendar = Calendar.getInstance();
		newCalendar.setTimeInMillis(System.currentTimeMillis());
		return toString(newCalendar.getTime(), getOraDateTimeFormat());

	}

	/**
	 * 得到当前系统日期 格式为yyyy年mm月dd日
	 * 
	 * @return
	 */
	public static final String getChineaseDate() {
		Calendar newCalendar = Calendar.getInstance();
		newCalendar.setTimeInMillis(System.currentTimeMillis());
		return toString(newCalendar.getTime(), getOraChineaseDateTimeFormat());
	}

	/**
	 * 得到系统当前的时间 格式为hh:mm:ss
	 * 
	 * @return 格式为hh:mm:ss的系统当前时间
	 */
	public static final String getSystemCurrentTime() {
		return getSystemCurrentDateTime().substring(11, 19);
	}

	/**
	 * 返回格式为YYYY-MM-DD hh:mm:ss
	 * 
	 * @param year
	 *            年
	 * @param month
	 *            月
	 * @param day
	 *            日
	 * @param hour
	 *            小时
	 * @param minute
	 *            分钟
	 * @param second
	 *            秒
	 * @return
	 */
	private static final String doTransform(int year, int month, int day,
			int hour, int minute, int second) {
		StringBuffer result = new StringBuffer();
		result.append(String.valueOf(year))
				.append("-")
				.append(month < 10 ? "0" + String.valueOf(month) : String
						.valueOf(month))
				.append("-")
				.append(day < 10 ? "0" + String.valueOf(day) : String
						.valueOf(day))
				.append(" ")
				.append(hour < 10 ? "0" + String.valueOf(hour) : String
						.valueOf(hour))
				.append(":")
				.append(minute < 10 ? "0" + String.valueOf(minute) : String
						.valueOf(minute))
				.append(":")
				.append(second < 10 ? "0" + String.valueOf(second) : String
						.valueOf(second));
		return result.toString();
	}

	/**
	 * 获得昨天的日期
	 * 
	 * @return 指定日期的上一天 格式:YYYY-MM-DD
	 */
	public static synchronized String getDayBeforeToday() {
		Date date = new Date(System.currentTimeMillis());
		GregorianCalendar gc = (GregorianCalendar) Calendar.getInstance();
		gc.setTime(date);
		gc.add(Calendar.DATE, -1);
		return doTransform(toString(gc.getTime(), getOraExtendDateTimeFormat()))
				.substring(0, 10);
	}

	/**
	 * 获得指定日期的上一天的日期
	 * 
	 * @param dateStr
	 *            指定的日期 格式:YYYY-MM-DD
	 * 
	 * @return 指定日期的上一天 格式:YYYY-MM-DD
	 */
	public static synchronized String getDayBeforeToday(String dateStr) {
		Date date = toDayStartDate(dateStr);
		GregorianCalendar gc = (GregorianCalendar) Calendar.getInstance();
		gc.setTime(date);
		gc.add(Calendar.DATE, -1);
		return doTransform(toString(gc.getTime(), getOraExtendDateTimeFormat()))
				.substring(0, 10);
	}

	/**
	 * 获得明天的日期
	 * 
	 * @return 指定日期的下一天 格式:YYYY-MM-DD
	 */
	public static synchronized String getDayAfterToday() {
		Date date = new Date(System.currentTimeMillis());
		GregorianCalendar gc = (GregorianCalendar) Calendar.getInstance();
		gc.setTime(date);
		gc.add(Calendar.DATE, 1);
		return doTransform(toString(gc.getTime(), getOraExtendDateTimeFormat()))
				.substring(0, 10);
	}

	/**
	 * 获得指定日期的下一天的日期
	 * 
	 * @param dateStr
	 *            指定的日期 格式:YYYY-MM-DD
	 * 
	 * @return 指定日期的下一天 格式:YYYY-MM-DD
	 */
	public static synchronized String getDayAfterToday(String dateStr) {
		Date date = toDayStartDate(dateStr);
		GregorianCalendar gc = (GregorianCalendar) Calendar.getInstance();
		gc.setTime(date);
		gc.add(Calendar.DATE, 1);
		return doTransform(toString(gc.getTime(), getOraExtendDateTimeFormat()))
				.substring(0, 10);
	}

	/**
	 * 获得以后几个月的日期
	 * 
	 * @return 指定日期的后面几个月 格式:YYYY-MM-DD
	 */
	public static synchronized Date getDayAfterMonth(int months) {
		Date date = new Date(System.currentTimeMillis());
		GregorianCalendar gc = (GregorianCalendar) Calendar.getInstance();
		gc.setTime(date);
		gc.add(Calendar.MONTH, months);
		return gc.getTime();
	}

	/**
	 * 返回格式为YYYY-MM-DD hh:mm:ss
	 * 
	 * @param date
	 *            输入格式为ORA标准时间格式
	 * @return
	 */
	private static String doTransform(String date) {
		StringBuffer buffer = new StringBuffer();
		buffer.append(date.substring(0, 4));
		buffer.append("-");
		buffer.append(date.substring(4, 6));
		buffer.append("-");
		buffer.append(date.substring(6, 8));
		buffer.append(" ");
		buffer.append(date.substring(8, 10));
		buffer.append(":");
		buffer.append(date.substring(10, 12));
		buffer.append(":");
		buffer.append(date.substring(12, 14));

		return buffer.toString();
	}

	/**
	 * 将一个日期对象转换成为指定日期、时间格式的字符串。 如果日期对象为空，返回一个空字符串对象.
	 * 
	 * @param theDate
	 *            要转换的日期对象
	 * @param theDateFormat
	 *            返回的日期字符串的格式
	 * @return 转换结果
	 */
	public static synchronized String toString(Date theDate,
			DateFormat theDateFormat) {
		if (theDate == null) {
			return "";
		} else {
			return theDateFormat.format(theDate);
		}
	}

	/**
	 * 将Date类型转换后返回本系统默认的日期格式 YYYY-MM-DD hh:mm:ss
	 * 
	 * @param theDate
	 *            要转换的Date对象
	 * @return 转换后的字符串
	 */
	public static synchronized String toDefaultString(Date theDate) {
		if (theDate == null) {
			return "";
		}
		return doTransform(toString(theDate, getOraExtendDateTimeFormat()));
	}

	/**
	 * 将输入格式为2004-8-13 12:31:22类型的字符串转换为标准的Date类型
	 * 
	 * @param dateStr
	 *            要转换的字符串
	 * 
	 * @return 转化后的标准的Date类型
	 */
	public static synchronized Date toDate(String dateStr) {
		String[] list0 = dateStr.split(" ");
		String date = list0[0];
		String time = list0[1];
		String[] list1 = date.split("-");
		int year = new Integer(list1[0]).intValue();
		int month = new Integer(list1[1]).intValue();
		int day = new Integer(list1[2]).intValue();
		String[] list2 = time.split(":");
		int hour = new Integer(list2[0]).intValue();
		int min = new Integer(list2[1]).intValue();
		int second = new Integer(list2[2]).intValue();
		Calendar cale = Calendar.getInstance();
		cale.set(year, month - 1, day, hour, min, second);
		return cale.getTime();
	}

	/**
	 * 将输入格式为2004-8-13,2004-10-8类型的字符串转换为标准的Date类型,这种Date类型 对应的日期格式为YYYY-MM-DD
	 * 00:00:00,代表一天的开始时刻
	 * 
	 * @param dateStr
	 *            要转换的字符串
	 * @return 转换后的Date对象
	 */
	public static synchronized Date toDayStartDate(String dateStr) {
		String[] list = dateStr.split("-");
		int year = Integer.parseInt(list[0]);
		int month = Integer.parseInt(list[1]);
		int day = Integer.parseInt(list[2]);
		Calendar cale = Calendar.getInstance();
		cale.set(year, month - 1, day, 0, 0, 0);
		return cale.getTime();

	}

	/**
	 * 将输入格式为2004-8-13,2004-10-8类型的字符串转换为标准的Date类型,这种Date类型 对应的日期格式为YYYY-MM-DD
	 * 23:59:59,代表一天的结束时刻
	 * 
	 * @param dateStr
	 *            输入格式:2004-8-13,2004-10-8
	 * @return 转换后的Date对象
	 */
	public static synchronized Date toDayEndDate(String dateStr) {
		String[] list = dateStr.split("-");
		int year = new Integer(list[0]).intValue();
		int month = new Integer(list[1]).intValue();
		int day = new Integer(list[2]).intValue();
		Calendar cale = Calendar.getInstance();
		cale.set(year, month - 1, day, 23, 59, 59);
		return cale.getTime();

	}

	/**
	 * 将两个scorm时间相加
	 * 
	 * @param scormTime1
	 *            scorm时间,格式为00:00:00(1..2).0(1..3)
	 * @param scormTime2
	 *            scorm时间,格式为00:00:00(1..2).0(1..3)
	 * @return 两个scorm时间相加的结果
	 */
	public static synchronized String addTwoScormTime(String scormTime1,
			String scormTime2) {
		int dotIndex1 = scormTime1.indexOf(".");
		int hh1 = Integer.parseInt(scormTime1.substring(0, 2));
		int mm1 = Integer.parseInt(scormTime1.substring(3, 5));
		int ss1 = 0;
		if (dotIndex1 != -1) {
			ss1 = Integer.parseInt(scormTime1.substring(6, dotIndex1));
		} else {
			ss1 = Integer
					.parseInt(scormTime1.substring(6, scormTime1.length()));
		}
		int ms1 = 0;
		if (dotIndex1 != -1) {
			ms1 = Integer.parseInt(scormTime1.substring(dotIndex1 + 1,
					scormTime1.length()));
		}

		int dotIndex2 = scormTime2.indexOf(".");
		int hh2 = Integer.parseInt(scormTime2.substring(0, 2));
		int mm2 = Integer.parseInt(scormTime2.substring(3, 5));
		int ss2 = 0;
		if (dotIndex2 != -1) {
			ss2 = Integer.parseInt(scormTime2.substring(6, dotIndex2));
		} else {
			ss2 = Integer
					.parseInt(scormTime2.substring(6, scormTime2.length()));
		}
		int ms2 = 0;
		if (dotIndex2 != -1) {
			ms2 = Integer.parseInt(scormTime2.substring(dotIndex2 + 1,
					scormTime2.length()));
		}

		int hh = 0;
		int mm = 0;
		int ss = 0;
		int ms = 0;

		if (ms1 + ms2 >= 1000) {
			ss = 1;
			ms = ms1 + ms2 - 1000;
		} else {
			ms = ms1 + ms2;
		}
		if (ss1 + ss2 + ss >= 60) {
			mm = 1;
			ss = ss1 + ss2 + ss - 60;
		} else {
			ss = ss1 + ss2 + ss;
		}
		if (mm1 + mm2 + mm >= 60) {
			hh = 1;
			mm = mm1 + mm2 + mm - 60;
		} else {
			mm = mm1 + mm2 + mm;
		}
		hh = hh + hh1 + hh2;

		StringBuffer sb = new StringBuffer();
		if (hh < 10) {
			sb.append("0").append(hh);
		} else {
			sb.append(hh);
		}
		sb.append(":");
		if (mm < 10) {
			sb.append("0").append(mm);
		} else {
			sb.append(mm);
		}
		sb.append(":");
		if (ss < 10) {
			sb.append("0").append(ss);
		} else {
			sb.append(ss);
		}
		sb.append(".");
		if (ms < 10) {
			sb.append(ms).append("00");
		} else if (ms < 100) {
			sb.append(ms).append("0");
		} else {
			sb.append(ms);
		}
		return sb.toString();
	}

	/**
	 * 根据timeType返回当前日期与传入日期的差值（当前日期减传入日期） 当要求返回月份的时候，date的日期必须和当前的日期相等，
	 * 否则返回0（例如：2003-2-23 和 2004-6-12由于23号和12号不是同一天，固返回0， 2003-2-23 和 2005-6-23
	 * 则需计算相差的月份，包括年，此例应返回28（个月）。 2003-2-23 和 2001-6-23
	 * 也需计算相差的月份，包括年，此例应返回-20（个月））
	 * 
	 * @param date
	 *            要与当前日期比较的日期
	 * @param timeType
	 *            0代表返回两个日期相差月数，1代表返回两个日期相差天数
	 * 
	 * @return 根据timeType返回当前日期与传入日期的差值
	 */
	public static int CompareDateWithNow(Date date, int timeType) {
		Date now = Calendar.getInstance().getTime();

		Calendar calendarNow = Calendar.getInstance();
		calendarNow.setTime(now);
		calendarNow.set(Calendar.HOUR, 0);
		calendarNow.set(Calendar.MINUTE, 0);
		calendarNow.set(Calendar.SECOND, 0);

		Calendar calendarPara = Calendar.getInstance();
		calendarPara.setTime(date);
		calendarPara.set(Calendar.HOUR, 0);
		calendarPara.set(Calendar.MINUTE, 0);
		calendarPara.set(Calendar.SECOND, 0);

		float nowTime = now.getTime();
		float dateTime = date.getTime();

		if (timeType == 0) {
			if (calendarNow.get(Calendar.DAY_OF_YEAR) == calendarPara
					.get(Calendar.DAY_OF_YEAR))
				return 0;
			return (calendarNow.get(Calendar.YEAR) - calendarPara
					.get(Calendar.YEAR))
					* 12
					+ calendarNow.get(Calendar.MONTH)
					- calendarPara.get(Calendar.MONTH);
		} else {
			float result = nowTime - dateTime;
			float day = 24 * 60 * 60 * 1000;
			// System.out.println("day "+day);
			// result = (result > 0) ? result : -result;
			// System.out.println(result);
			result = result / day;
			Float resultFloat = new Float(result);
			float fraction = result - resultFloat.intValue();
			if (fraction > 0.5) {
				return resultFloat.intValue() + 1;
			} else if (fraction < -0.5) {
				return resultFloat.intValue() - 1;
			} else {
				return resultFloat.intValue();
			}
		}
	}

	/**
	 * 判断当前月份是否是最后12月
	 * 
	 * @return 否是最后12月
	 */
	public static boolean isLastMonth() {
		if (month().equals("12"))
			return true;
		return false;
	}

	/**
	 * 获得当前日期的年份
	 * 
	 * @return 当前日期的年份
	 */
	public static Integer year() {
		Calendar calendar = Calendar.getInstance();
		return new Integer(calendar.get(Calendar.YEAR));
	}

	/**
	 * 获得当前日期的月份
	 * 
	 * @return 当前日期的月份
	 */
	public static Integer month() {
		Calendar calendar = Calendar.getInstance();
		return new Integer(calendar.get(Calendar.MONTH) + 1);
	}

	/**
	 * 获得当前日期的前一个月份,如果当前是1月，返回12
	 * 
	 * @return 当前日期的前一个月份
	 */
	public static Integer beforeMonth() {
		Calendar calendar = Calendar.getInstance();
		int month = calendar.get(Calendar.MONTH);
		if (month == 0) {
			return new Integer(12);
		} else {
			return new Integer(month);
		}
	}

	/**
	 * 获得当前日期的后一个月份,如果当前是12月，返回1
	 * 
	 * @return 当前日期的后一个月份
	 */
	public static Integer nextMonth() {
		Calendar calendar = Calendar.getInstance();
		int month = calendar.get(Calendar.MONTH);
		if (month == 11) {
			return new Integer(1);
		} else {
			return new Integer(month + 2);
		}
	}

	/**
	 * 获得当前日期的日
	 * 
	 * @return 当前日期的日
	 */
	public static Integer day() {
		Calendar calendar = Calendar.getInstance();
		return new Integer(calendar.get(Calendar.DAY_OF_MONTH));
	}

	/**
	 * 获得当前时间的小时
	 * 
	 * @return 当前时间的小时
	 */
	public static Integer hour() {
		Calendar calendar = Calendar.getInstance();
		return new Integer(calendar.get(Calendar.HOUR_OF_DAY));
	}

	/**
	 * 获得当前时间的分钟
	 * 
	 * @return 当前时间的分钟
	 */
	public static Integer minute() {
		Calendar calendar = Calendar.getInstance();
		return new Integer(calendar.get(Calendar.MINUTE));
	}

	/**
	 * 获得当前时间的秒
	 * 
	 * @return 当前时间的秒
	 */
	public static Integer second() {
		Calendar calendar = Calendar.getInstance();
		return new Integer(calendar.get(Calendar.SECOND));
	}

	/**
	 * 获得当前的星期是本月的第几周
	 * 
	 * @return 当前的星期是本月的第几周
	 */
	public static int WeekofMonth() {
		Calendar calendar = Calendar.getInstance();
		return calendar.get(Calendar.WEEK_OF_MONTH);
	}

	/**
	 * 获得当前的日期是本星期的第几天
	 * 
	 * @return 当前的日期是本星期的第几天
	 */
	public static int DayofWeek() {
		Calendar calendar = Calendar.getInstance();
		return calendar.get(Calendar.DAY_OF_WEEK) - 1;
	}

	/**
	 * 获得这个月有几周
	 * 
	 * @return 这个月有几周
	 */
	public static int getMaxWeekofMonth() {
		Calendar calendar = Calendar.getInstance();
		return calendar.getActualMaximum(Calendar.WEEK_OF_MONTH);
	}

	/**
	 * 获得当前的星期是本月的第几周
	 * 
	 * @return 当前的星期是本月的第几周
	 */
	public static int WeekofMonth(int year, int month, int day) {
		Calendar calendar = Calendar.getInstance();
		calendar.set(year, month - 1, day, 0, 0, 0);
		return calendar.get(Calendar.WEEK_OF_MONTH);
	}

	/**
	 * 获得当前的日期是本星期的第几天
	 * 
	 * @return 当前的日期是本星期的第几天
	 */
	public static int DayofWeek(int year, int month, int day) {
		Calendar calendar = Calendar.getInstance();
		calendar.set(year, month - 1, day, 0, 0, 0);
		return calendar.get(Calendar.DAY_OF_WEEK) - 1;
	}

	/**
	 * 获得当前的日期是本星期的第几天
	 * 
	 * @return 当前的日期是本星期的第几天
	 */
	public static int getTodayofWeek() {
		Calendar calendar = Calendar.getInstance();
		return calendar.get(Calendar.DAY_OF_WEEK) - 1;
	}

	/**
	 * 获得这个月有几周
	 * 
	 * @return 这个月有几周
	 */
	public static int getMaxWeekofMonth(int year, int month) {
		Calendar calendar = Calendar.getInstance();
		calendar.set(year, month - 1, 1, 0, 0, 0);
		return calendar.getActualMaximum(Calendar.WEEK_OF_MONTH);
	}

	/**
	 * 得到当前日期的字符串，格式是YYYY-MM-DD
	 * 
	 * @return 当前日期的字符串，格式是YYYY-MM-DD
	 */
	public static String Date() {
		Calendar calendar = Calendar.getInstance();
		StringBuffer date = new StringBuffer();
		date.append(calendar.get(Calendar.YEAR));
		date.append('-');
		date.append(calendar.get(Calendar.MONTH) + 1);
		date.append('-');
		date.append(calendar.get(Calendar.DAY_OF_MONTH));
		return date.toString();
	}

	/**
	 * 得到当前日期时间的字符串，格式是YYYY-MM-DD HH:MM:SS
	 * 
	 * @return 当前日期时间的字符串，格式是YYYY-MM-DD HH:MM:SS
	 */
	public static String DateTime() {
		Calendar calendar = Calendar.getInstance();
		StringBuffer date = new StringBuffer();
		date.append(calendar.get(Calendar.YEAR));
		date.append('-');
		date.append(calendar.get(Calendar.MONTH) + 1);
		date.append('-');
		date.append(calendar.get(Calendar.DAY_OF_MONTH));
		date.append(' ');
		date.append(calendar.get(Calendar.HOUR_OF_DAY));
		date.append(':');
		date.append(calendar.get(Calendar.MINUTE));
		date.append(':');
		date.append(calendar.get(Calendar.SECOND));
		return date.toString();
	}

	/**
	 * 比较2个日期大小
	 * 
	 * @param startTime
	 *            起始日期
	 * @param endTime
	 *            结束日期
	 * @return 比较2个日期大小。>0：startTime>endTime 0:startTime=endTime
	 *         <0:startTime<endTime
	 * 
	 * @throws ParseException
	 */
	public static int compareTwoDate(String startTime, String endTime)
			throws ParseException {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

		Date b = formatter.parse(startTime);
		Date c = formatter.parse(endTime);

		return b.compareTo(c);
	}

	/**
	 * 比较一个日期是否在指定的日期段中
	 * 
	 * @param nowSysDateTime
	 *            要判断的日期
	 * @param startTime
	 *            起始日期
	 * @param endTime
	 *            结束日期
	 * @return nowSysDateTime在startTime和endTime中返回true
	 * @throws ParseException
	 */
	public static boolean compareThreeDate(String nowSysDateTime,
			String startTime, String endTime) throws ParseException {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

		Date b = formatter.parse(startTime);
		Date a = formatter.parse(nowSysDateTime);
		Date c = formatter.parse(endTime);
		if (a.compareTo(b) >= 0 && a.compareTo(c) <= 0) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 得到某一年的某月的前/后一个月是那一年 例如得到2002年1月的前一个月是哪年 (2002,1,-1) =2001.
	 * 
	 * @param year
	 *            时间点的年份
	 * @param month
	 *            时间点的月份
	 * @param pastMonth
	 *            于时间点的月份距离,负数表示以前的时间,正数表示以后的时间
	 * @return 返回年
	 */
	public static int getYearPastMonth(int year, int month, int pastMonth) {
		return year
				+ (int) Math.floor((double) (month - 1 + pastMonth)
						/ (double) 12);
	}

	/**
	 * 得到某个月的下几个个月是那个月.
	 * 
	 * @param month
	 *            当前月
	 * @param pastMonth
	 *            和当前月的月数差距
	 * @return 目标月数
	 */
	public static int getMonthPastMonth(int month, int pastMonth) {
		return ((12 + month - 1 + pastMonth) % 12) + 1;
	}

	/**
	 * 返回月份的季度数.
	 * 
	 * 0表示非法月份.正常返回1,2,3,4.
	 * 
	 * @param nMonth
	 *            int
	 * @return int
	 */
	public static int getQuarterbyMonth(int nMonth) {
		final int[] monthQuarters = { 0, 1, 1, 1, 2, 2, 2, 3, 3, 3, 4, 4, 4 };

		if (nMonth >= 0 && nMonth <= 12) {
			return monthQuarters[nMonth];
		} else {
			return 0;
		}
	}

	/**
	 * 得到当前日期.
	 * 
	 * @return 当前日期的java.sql.Date格式
	 */
	public static java.sql.Date getNowSqlDate() {
		java.util.Date aDate = new java.util.Date();
		return new java.sql.Date(aDate.getTime());
	}

	/**
	 * 得到当前日期.
	 * 
	 * @return 当前日期的java.util.Date格式
	 */
	public static java.util.Date getNowDate() {
		return Calendar.getInstance().getTime();
	}

	/**
	 * 得到当前时间.
	 * 
	 * @return java.sql.Time
	 */
	public static java.sql.Timestamp getNowTimestamp() {
		return new java.sql.Timestamp(new java.util.Date().getTime());
	}

	/**
	 * 得到某个时间的字符串显示,格式为yyyy-MM-dd HH:mm.
	 * 
	 * @param aTime
	 *            要分析的时间
	 * @return String
	 */
	public static String getTimeShow(java.sql.Timestamp aTime) {
		if (null == aTime) {
			return "";
		}
		SimpleDateFormat sdfTemp = new SimpleDateFormat("yyyy-MM-dd HH:mm",
				Locale.US);
		return sdfTemp.format(aTime);
	}

	/**
	 * 按照自己的格式显示时间.
	 * 
	 * @param aTime
	 *            要分析的时间
	 * @param aFormat
	 *            按照SimpleDateFormat的规则的格式
	 * @return 字符串
	 */
	public static String getSelfTimeShow(java.sql.Timestamp aTime,
			String aFormat) {
		if (null == aTime) {
			return "";
		}
		SimpleDateFormat sdfTemp = new SimpleDateFormat(aFormat, Locale.US);
		return sdfTemp.format(aTime);
	}

	/**
	 * 按照自己的格式显示时间.
	 * 
	 * @param aTime
	 *            要分析的时间
	 * @param aFormat
	 *            按照SimpleDateFormat的规则的格式
	 * @return 字符串
	 */
	public static String getSelfTimeShow(java.sql.Date aTime, String aFormat) {
		if (null == aTime) {
			return "";
		}
		SimpleDateFormat sdfTemp = new SimpleDateFormat(aFormat, Locale.US);
		return sdfTemp.format(aTime);
	}

	/**
	 * 查询某个月的天数.
	 * 
	 * @param year
	 *            年
	 * @param month
	 *            月
	 * @return 月的天数
	 */
	public static int getDayinMonth(int year, int month) {
		final int[] dayNumbers = { 31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30,
				31 };
		int result;
		if ((month == 2) && ((year % 4) == 0) && ((year % 100) != 0)
				|| ((year % 400) == 0)) {
			result = 29;
		} else {
			result = dayNumbers[month - 1];
		}

		return result;
	}

	/**
	 * 检查日期是否合法.
	 * 
	 * @param ayear
	 *            年
	 * @param amonth
	 *            月
	 * @param aday
	 *            日
	 * @return 合法返回0,非法返回-1,空返回1
	 */
	public static int validDate(int ayear, int amonth, int aday) {
		int isGood = 0;
		if ((ayear == 0) || (amonth == 0) || (aday == 0)) {
			isGood = 1;
		} else {
			int monthDays = getDayinMonth(ayear, amonth);
			if ((aday < 1) || (aday > monthDays)) {
				isGood = -1;
			}
		}
		return isGood;
	}

	/**
	 * 检查日期是否合法.
	 * 
	 * @param syear
	 *            年
	 * @param smonth
	 *            月
	 * @param sday
	 *            日
	 * @return 合法返回0,非法返回-1,空返回1
	 */
	public static int validDate(String syear, String smonth, String sday) {
		int ayear, amonth, aday;
		ayear = StringUtil.myparseInt(syear, 0);
		amonth = StringUtil.myparseInt(smonth, 0);
		aday = StringUtil.myparseInt(sday, 0);
		return validDate(ayear, amonth, aday);
	}

	/**
	 * 检查一个日期字符串是否合法: 2003-9-5.
	 * 
	 * @param aDateStr
	 *            日期字符串
	 * @return 合法返回0,非法返回-1,空返回1
	 */
	public static int validDate(String aDateStr) {
		if (StringUtil.isTrimEmpty(aDateStr)) {
			return 1;
		}
		String[] aObj = StringUtil.splitString("/-/", aDateStr);
		if (null == aObj) {
			return 1;
		}
		return validDate(aObj[0], aObj[1], aObj[2]);
	}

	/**
	 * 检测时间的合法性.
	 * 
	 * @param nHour
	 *            int
	 * @param nMin
	 *            int
	 * @param nSec
	 *            int
	 * @return int 合法返回0,非法返回-1,空返回1
	 */
	public static int validTime(int nHour, int nMin, int nSec) {
		int isGood = 0; // normal
		if ((nHour == 0) || (nMin == 0) || (nSec == 0)) {
			isGood = 1; // empty
		} else {
			if ((nHour > 23 || nHour < 0 || nMin > 59 || nMin < 0 || nSec > 59 || nSec < 0)) {
				isGood = -1; // invalid
			}
		}
		return isGood;
	}

	/**
	 * 检查一个日期字符串是否合法: 2003-9-5 13:52:5.
	 * 
	 * @param aDateTimeStr
	 *            日期字符串
	 * @return 合法返回0,非法返回-1,空返回1
	 */
	public static int validDateTime(String aDateTimeStr) {
		if (StringUtil.isTrimEmpty(aDateTimeStr)) {
			return 1;
		}
		String[] aObj = StringUtil.splitString("/ /", aDateTimeStr);
		if (null == aObj) {
			return 1;
		}
		if (aObj.length != 2) {
			return 1;
		}

		if (validDate(aObj[0]) == 0) {
			String[] aTimeObj = StringUtil.splitString("/:/", aObj[1]);
			if (aTimeObj.length == 3) {
				int nHour = StringUtil.myparseInt(aTimeObj[0], 0);
				int nMin = StringUtil.myparseInt(aTimeObj[0], 0);
				int nSec = StringUtil.myparseInt(aTimeObj[0], 0);
				return validTime(nHour, nMin, nSec);
			}
		}
		return -1;
	}

	/**
	 * 检查日期是否为空.
	 * 
	 * @param syear
	 *            年
	 * @param smonth
	 *            月
	 * @param sday
	 *            日
	 * @return 为空返回true
	 */
	public static boolean isEmptyDate(String syear, String smonth, String sday) {
		boolean isEmpty = false;

		int ayear, amonth, aday;
		ayear = StringUtil.myparseInt(syear, 0);
		amonth = StringUtil.myparseInt(smonth, 0);
		aday = StringUtil.myparseInt(sday, 0);

		if ((ayear == 0) || (amonth == 0) || (aday == 0)) {
			isEmpty = true;
		}
		return isEmpty;
	}

	/**
	 * 检查日期是否为空.
	 * 
	 * @param ayear
	 *            年
	 * @param amonth
	 *            月
	 * @param aday
	 *            日
	 * @return 为空返回true
	 */
	public static boolean isEmptyDate(int ayear, int amonth, int aday) {
		boolean isEmpty = false;

		if ((ayear == 0) || (amonth == 0) || (aday == 0)) {
			isEmpty = true;
		}
		return isEmpty;
	}

	/**
	 * 得到现在时间的前/后一段时间.
	 * 
	 * @param nSecs
	 *            距离现在时间的秒数
	 * @return Timestamp
	 */
	public static java.sql.Timestamp getPastTime(int nSecs) {
		java.sql.Timestamp ts1 = getNowTimestamp();
		java.sql.Timestamp ts2;
		ts2 = new java.sql.Timestamp(ts1.getTime() - nSecs * 1000);
		return ts2;
	}

	/**
	 * 得到距离某个时间一段时间的一个时间.
	 * 
	 * @param aTime
	 *            相对的时间
	 * @param nSecs
	 *            时间距离:秒
	 * @return Timestamp
	 */
	public static java.sql.Timestamp getPastTime(java.sql.Timestamp aTime,
			int nSecs) {
		java.sql.Timestamp ts2;
		ts2 = new java.sql.Timestamp(aTime.getTime() - nSecs * 1000);
		return ts2;
	}

	/**
	 * 检查aDate是不是今天.
	 * 
	 * @param aDate
	 *            分析的日期
	 * @return 是今天返回true
	 */
	public static boolean isToday(java.sql.Date aDate) {
		Calendar aCal1 = Calendar.getInstance();
		aCal1.setTime(aDate);

		java.sql.Date date1 = getNowSqlDate();

		Calendar aCal2 = Calendar.getInstance();
		aCal2.setTime(date1);

		return ((aCal1.get(Calendar.DATE) == aCal2.get(Calendar.DATE))
				&& (aCal1.get(Calendar.YEAR) == aCal2.get(Calendar.YEAR)) && (aCal1
					.get(Calendar.MONTH) == aCal2.get(Calendar.MONTH)));
	}

	/**
	 * 把字符串按照规则转换为日期时间的long值.
	 * 
	 * 如果不合法,则返回今天.
	 * 
	 * @param str
	 *            要分析的字符串
	 * @param pattern
	 *            规则
	 * @throws NullPointerException
	 * @return long
	 */
	public static Long str2DateTime(String str, String pattern) {
		DateFormat dateFmt = new SimpleDateFormat(pattern, Locale.US);
		Calendar calendar = Calendar.getInstance();
		try {
			calendar.setTime(dateFmt.parse(str));
			// return c.getTimeInMillis();
			return new Long(calendar.getTime().getTime());
		} catch (Exception e) {
			// SystemUtil.printLine(e.toString());
			// return null
			return null;
		}
	}

	/**
	 * 从一个日期字符串得到时间的long值.
	 * 
	 * @param strTemp
	 *            String
	 * @return long
	 */
	public static Long dateStr2Time(String strTemp) {
		if (null == strTemp || strTemp.length() < 8) {
			return null;
		}
		String sSign = strTemp.substring(4, 5);
		String sPattern = new StringBuffer("yyyy").append(sSign).append("MM")
				.append(sSign).append("dd").toString();
		return str2DateTime(strTemp, sPattern);
	}

	/**
	 * 从一个日期时间字符串得到时间的long值. 例如 2004-5-6 23:52:22 ,包含秒
	 * 
	 * @param strTemp
	 *            String
	 * @return long
	 */
	public static Long dateTimeStr2Time(String strTemp) {
		if (null == strTemp || strTemp.length() < 8) {
			return null;
		}
		String sSign = strTemp.substring(4, 5);
		String sPattern = new StringBuffer("yyyy").append(sSign).append("MM")
				.append(sSign).append("dd").append(" hh:mm:ss").toString();

		Long aLong = str2DateTime(strTemp, sPattern);
		if (null == aLong) {
			String sShortPattern = new StringBuffer("yyyy").append(sSign)
					.append("MM").append(sSign).append("dd").append(" hh:mm")
					.toString();
			aLong = str2DateTime(strTemp, sShortPattern);
		}
		return aLong;
	}

	/**
	 * 把2003-10-11这种字符串转换为时间格式,如果不合法,返回的是当前时间.
	 * 
	 * @param aStr
	 *            要分析的字符串,格式为2003-11-11
	 * @return Timestamp
	 */
	public static java.sql.Timestamp dateStr2Timestamp(String aStr) {
		Long temp = dateStr2Time(aStr);
		if (null == temp) {
			return null;
		}
		return new java.sql.Timestamp(temp.longValue());
	}

	/**
	 * 把2003-10-11这种字符串转换为时间格式,如果不合法,返回的是当前时间.
	 * 
	 * @param aStr
	 *            要分析的字符串,格式为2003-11-11
	 * @return Date
	 */
	public static java.util.Date dateStr2Date(String aStr) {
		Long result = dateStr2Time(aStr);
		if (null == result) {
			return null;
		}
		return new java.sql.Date(result.longValue());

		/*
		 * //version 1 for check 2004.6.26 by scud
		 * 
		 * Calendar aSJ = Calendar.getInstance(); aSJ.setTime(new
		 * java.util.Date()); if (StrFunc.calcCharCount('-', aStr) >= 2) { int
		 * aYear, aMonth, aDay; String[] aObj = StrFunc.splitString("/-/",
		 * aStr); aYear = StrFunc.myparseInt(aObj[0]); aMonth =
		 * StrFunc.myparseInt(aObj[1]) - 1; aDay = StrFunc.myparseInt(aObj[2]);
		 * if (aYear > 0 && aMonth > -1 && aDay > -1) { aSJ.set(aYear, aMonth,
		 * aDay); } } return new java.sql.Date(aSJ.getTime().getTime());
		 */
	}

	/**
	 * 把2003-10-11 23:43:55这种字符串转换为时间格式,如果不合法,返回的是当前时间.
	 * 
	 * @param aStr
	 *            要分析的字符串
	 * @return Timestamp
	 */
	public static java.sql.Timestamp dateTimeStr2Timestamp(String aStr) {
		Long tempVar = dateTimeStr2Time(aStr);
		if (null == tempVar) {
			return null;
		}
		return new java.sql.Timestamp(tempVar.longValue());
	}

	/**
	 * 日期字符串转换为日期范围的开始时间,即从该天的0:0:0开始. 如果该数据库类型就是日期类型,则直接getFormDate即可.
	 * 
	 * @param aStr
	 *            String
	 * @return Timestamp
	 */
	public static java.sql.Timestamp dateStr2BeginDateTime(String aStr) {
		if (StringUtil.isTrimEmpty(aStr)) {
			return null;
		} else {
			return dateTimeStr2Timestamp(aStr + " 0:0:0");
		}
	}

	/**
	 * 日期字符串转换为日期范围的结束时间,即到该天的23:59:59. 如果该数据库类型就是日期类型,则直接getFormDate即可.
	 * 
	 * @param aStr
	 *            String
	 * @return Timestamp
	 */
	public static java.sql.Timestamp dateStr2EndDateTime(String aStr) {
		if (StringUtil.isTrimEmpty(aStr)) {
			return null;
		} else {
			return dateTimeStr2Timestamp(aStr + " 23:59:59");
		}
	}

	/**
	 * 得到日期中的年.
	 * 
	 * @param aDate
	 *            要分析的日期
	 * @return 年
	 */
	public static int getYearFromDate(java.util.Date aDate) {
		Calendar cFF = Calendar.getInstance();
		cFF.setTime(aDate);
		return cFF.get(Calendar.YEAR);
	}

	/**
	 * 得到日期中的月.
	 * 
	 * @param aDate
	 *            要分析的日期
	 * @return 月
	 */
	public static int getMonthFromDate(java.util.Date aDate) {
		Calendar cFF = Calendar.getInstance();
		cFF.setTime(aDate);
		return cFF.get(Calendar.MONTH) + 1;
	}

	/**
	 * 得到日期中的天.
	 * 
	 * @param aDate
	 *            要分析的日期
	 * @return 天
	 */
	public static int getDAYFromDate(java.util.Date aDate) {
		Calendar cFF = Calendar.getInstance();
		cFF.setTime(aDate);
		return cFF.get(Calendar.DATE);
	}

	/**
	 * 得到日期中的小时.
	 * 
	 * @param aDate
	 *            要分析的日期
	 * @return 小时
	 */
	public static int getHourFromDate(java.util.Date aDate) {
		Calendar cFF = Calendar.getInstance();
		cFF.setTime(aDate);
		return cFF.get(Calendar.HOUR_OF_DAY);
	}

	/**
	 * 得到日期中的分钟.
	 * 
	 * @param aDate
	 *            要分析的日期
	 * @return 分钟
	 */
	public static int getMinuteFromDate(java.util.Date aDate) {
		Calendar cFF = Calendar.getInstance();
		cFF.setTime(aDate);
		return cFF.get(Calendar.MINUTE);
	}

	/**
	 * 得到日期的星期.
	 * 
	 * @param aDate
	 *            要分析的日期
	 * @return 星期
	 */
	public static int getWeekFromDate(java.util.Date aDate) {
		Calendar cFF = Calendar.getInstance();
		cFF.setTime(aDate);
		return cFF.get(Calendar.DAY_OF_WEEK);
	}

	/**
	 * 得到当前月的第一天是日期几.
	 * 
	 * @param year
	 *            要分析的年份
	 * @param month
	 *            要分析的月份
	 * @return 星期
	 */
	public static int getFirstDayOfWeek(Integer year, Integer month) {
		Calendar cFF = Calendar.getInstance();
		cFF.set(Calendar.YEAR, year);
		cFF.set(Calendar.MONTH, month - 1);
		cFF.set(Calendar.DATE, 1);
		return cFF.get(Calendar.DAY_OF_WEEK) - 1;
	}

	/**
	 * 获得当前时间与给定时间的距离
	 * 
	 * @param compMillSecond
	 *            给定时间的与协调世界时 1970 年 1 月 1 日午夜之间的时间差
	 * @return 例如:367Day 59H 56Min
	 */
	public static String diff(long compMillSecond) {
		long diff = System.currentTimeMillis() - compMillSecond;
		long day = diff / ONE_DAY;
		long hour = (diff % ONE_DAY) / ONE_HOUR;
		long min = ((diff % ONE_DAY) % ONE_HOUR) / ONE_MIN;
		return String.valueOf(day) + " Days " + String.valueOf(hour)
				+ " Hours " + String.valueOf(min) + " Mins ";
	}

	/**
	 * 得到间隔时间的天数
	 * 
	 * @param overdate
	 * @return
	 */
	public static String getIntevalDays(Date overdate) {
		String temp = null;
		@SuppressWarnings("unused")
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		Date nowdate = new Date();
		long nowlong = nowdate.getTime();
		long overlong = overdate.getTime();
		long templong = overlong - nowlong;
		long day = templong / 1000 / 60 / 60 / 24;
		temp = String.valueOf(day);
		if (overdate.getTime() < nowdate.getTime()) {
			return "-1";
		}
		return temp;
	}

	/**
	 * 计算2个日期相隔多少天
	 * 
	 * @param beforeDate
	 *            起始日期
	 * 
	 * @param afterDate
	 *            结束日期
	 * 
	 * @return 相差多少天
	 * @throws ParseException
	 */
	public static Integer getIntevalDays(Date beforeDate, Date afterDate) {

		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

		String beforeString = dateFormat.format(beforeDate);
		String afterString = dateFormat.format(afterDate);
		beforeString = beforeString + " 00:00:00";
		afterString = afterString + " 00:00:00";

		Integer temp = -1;

		long beforeLong = 0;
		long afterLong = 0;
		try {
			beforeLong = dateFormat.parse(beforeString).getTime();
			afterLong = dateFormat.parse(afterString).getTime();
		} catch (ParseException e) {
			e.printStackTrace();
		}

		if (beforeLong < afterLong) {

			long templong = afterLong / 1000 / 60 / 60 / 24 - beforeLong / 1000
					/ 60 / 60 / 24;

			temp = Integer.valueOf(String.valueOf(templong));
		}

		return temp;
	}

	/**
	 * 得到间隔时间的分钟数
	 * 
	 * @param overdate
	 * @return
	 */
	public static String getIntevalMinutes(Date overdate) {
		String temp = null;
		@SuppressWarnings("unused")
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		Date nowdate = new Date();
		long nowlong = nowdate.getTime();
		long overlong = overdate.getTime();
		long templong = overlong - nowlong;
		long hour = templong / 1000 / 60 / 60;
		templong = templong - hour * 60 * 60 * 1000;
		long minute = templong / 1000 / 60;
		temp = String.valueOf(minute);
		if (overdate.getTime() < nowdate.getTime()) {
			return "-1";
		}
		return temp;
	}

	/**
	 * 获得给定日期的后几天的日期
	 * 
	 * @param date
	 *            给定的日期
	 * @param days
	 *            过几天的天数
	 * @return 返回给定日期后几天的日期
	 */
	public static synchronized Date getDayAfterDate(Date date, int days) {

		GregorianCalendar gc = (GregorianCalendar) Calendar.getInstance();

		gc.setTime(date);

		gc.add(Calendar.DAY_OF_YEAR, days);

		return gc.getTime();
	}

	/**
	 * 得到二个日期间的间隔小时
	 */
	public static Long getTwoDay(String sj1, String sj2) {
		SimpleDateFormat myFormatter = new SimpleDateFormat(
				"yyyy-MM-dd HH:mm:ss");
		long day = 0;
		try {
			java.util.Date date = myFormatter.parse(sj1);
			java.util.Date mydate = myFormatter.parse(sj2);
			day = (date.getTime() - mydate.getTime()) / (60 * 60 * 1000);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return day;
	}

	/**
	 * 得到二个日期间的间隔分钟数
	 */
	public static Long getTwoMinute(String sj1, String sj2) {
		SimpleDateFormat myFormatter = new SimpleDateFormat(
				"yyyy-MM-dd HH:mm:ss");
		long day = 0;
		try {
			java.util.Date date = myFormatter.parse(sj1);
			java.util.Date mydate = myFormatter.parse(sj2);
			day = (date.getTime() - mydate.getTime()) / (60 * 1000);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return day;
	}

	public static Date strToDate(String date, String formatStr) {
		SimpleDateFormat dateformat = new SimpleDateFormat(formatStr);
		try {
			return dateformat.parse(date);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 与当前时间比较，是否大于当前时间 myDAte 传过来的时间
	 */
	public boolean compareWithToday(Date myDate) {
		String getDate = myDate.toString();
		Date nowdate = new Date(); // 当前日期
		// String myString = "2008-09-08";
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd", Locale.CHINA);
		Date d = null;
		try {
			d = sdf.parse(getDate);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		boolean flag = d.before(nowdate);// 如果在当前时间之前返回true,之后false
		return flag;
	}

	/**
	 * 得到给定日期N天后的日期,
	 * 
	 * @param datestr
	 *            要是yyyy-MM-dd格式
	 * @param num
	 * @return
	 * @throws ParseException
	 */
	public static String getDayAfterNum(String datestr, int num)
			throws ParseException {
		String pattern = "yyyy-MM-dd";
		SimpleDateFormat format = new SimpleDateFormat(pattern);
		Date date1 = format.parse(datestr);
		long time = date1.getTime() + (1000L * 60 * 60 * 24 * num);
		Date date = new Date();
		date.setTime(time);
		String s = format.format(date);
		return s;
	}

	/**
	 * 消息时间问题：要先设置时间再对Calendar格式化，不然当系统时间上下午不一致时，做出现 10点，22点的可能
	 * 把Calendar.HOUR改成HOUR_OF_DAY
	 * 
	 * @author jdp
	 * 
	 *         文档已经解释的很清楚了
	 * 
	 *         Calendar.HOUR_OF_DAY是： Field number for get and set indicating
	 *         the hour of the day. HOUR_OF_DAY is used for the 24-hour clock.
	 *         E.g., at 10:04:15.250 PM the HOUR_OF_DAY is 22.
	 * 
	 *         Calendar.HOUR是： Field number for get and set indicating the hour
	 *         of the morning or afternoon. HOUR is used for the 12-hour clock
	 *         (0 - 11). Noon and midnight are represented by 0, not by 12.
	 *         E.g., at 10:04:15.250 PM the HOUR is 10.
	 * 
	 */
	public static Date getRemindTime(Date date, int hour, int minute) {
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		c.set(Calendar.AM_PM, Calendar.AM);
		c.set(Calendar.HOUR_OF_DAY, hour);
		c.set(Calendar.MINUTE, minute);
		Date d = c.getTime();
		return d;
	}

	public static void main(String[] args) throws Exception {

		// Calendar calendar = Calendar.getInstance();
		// calendar.

		// GregorianCalendar gc = (GregorianCalendar) Calendar.getInstance();
		//
		// gc.set(2008, 04, 20);

		// @SuppressWarnings("unused")
		System.out.println(getIntevalDays(toDate("2010-01-13 23:59:59"),
				toDate("2010-01-14 00:00:00")));

		String datestr = "2014-12-2";
		String getDateValue = getDayAfterNum(datestr, 60);
		System.out.println(getDateValue);

		System.out.println(getRemindTime(new Date(), 10, 0));
	}

	/**
	 * 返回指定日期的Calendar实例
	 * 
	 * @param date
	 *            传入日期
	 * @return
	 */
	public static Calendar getCalender(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		return calendar;
	}

	/**
	 * 返回当天的Calendar实例
	 * 
	 * @return calendar Calendar实例
	 */
	public static Calendar getCurrentCalendar() {
		Date date = new Date();
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		return calendar;
	}

	/**
	 * 返回自定义格式的当前日期
	 * 
	 * @param pattern
	 *            指定的格式
	 * @return 指定格式的日期
	 */
	public static String getCurrentTimeByCustomPattern(String pattern) {
		return new SimpleDateFormat(pattern).format(new Date());
	}

	/**
	 * 返回当前日期
	 * 
	 * @return
	 */
	public static Date getCurrentTime() {
		Calendar calendar = Calendar.getInstance();
		return calendar.getTime();
	}

	/**
	 * 返回当前年份
	 * 
	 * @return
	 */
	public static int getCurrentYear() {
		Calendar calendar = Calendar.getInstance();
		return calendar.get(Calendar.YEAR);
	}

	/**
	 * 返回当前月份
	 * 
	 * @return
	 */
	public static int getCurrentMonth() {
		Calendar calendar = Calendar.getInstance();
		return (calendar.get(Calendar.MONTH) + 1);
	}

	/**
	 * 返回当前日
	 * 
	 * @return
	 */
	public static int getCurrentDay() {
		Calendar calendar = Calendar.getInstance();
		return calendar.get(Calendar.DATE);
	}

	/**
	 * 将指定日期转换为自定义格式
	 * 
	 * @param date
	 * @param pattern
	 * @return
	 */
	public static String getTimeByCustomPattern(Date date, String pattern) {
		return new SimpleDateFormat(pattern).format(date);
	}

	/**
	 * 判断日期是否属于自然季度的末尾月,3-5月春，6-8月夏，9-11月秋，12-2月冬
	 * 
	 * @param date
	 * @return
	 */
	public static boolean isEndOfSeason(Date date) {
		Calendar calendar = getCalender(date);
		int month = calendar.get(Calendar.MONTH) + 1;
		if (month % 3 == 2) {
			return true;
		}
		return false;
	}

	/**
	 * 返回指定日期的年份
	 * 
	 * @param date
	 * @return
	 */
	public static int getYear(Date date) {
		Calendar calendar = getCalender(date);
		return calendar.get(Calendar.YEAR);
	}

	/**
	 * 返回指定日期的月份
	 * 
	 * @param date
	 * @return
	 */
	public static int getMonth(Date date) {
		Calendar calendar = getCalender(date);
		return (calendar.get(Calendar.MONTH) + 1);
	}

	/**
	 * 返回指定日期的天数
	 * 
	 * @param date
	 * @return
	 */
	public static int getDay(Date date) {
		Calendar calendar = getCalender(date);
		return calendar.get(Calendar.DATE);
	}

	/**
	 * 日期型转换为自定义格式的字符串
	 * 
	 * @param pattern
	 *            格式
	 * @param date
	 *            日期
	 * @return
	 */
	public static String dateToString(Date date, String pattern) {
		return new SimpleDateFormat(pattern).format(date);
	}

	/**
	 * 默认以 yy-MM-dd HH:mm:ss 格式时间
	 * 
	 * @param date
	 * @return
	 */
	public static String dateToString(Date date) {
		return dateToString(date, "yy-MM-dd HH:mm:ss");
	}

	/**
	 * 根据 format 将dateStr转换成 date
	 * 
	 * @param dateStr
	 * @param format
	 * @return
	 */
	public static Date stringToDate(String dateStr, String format) {
		SimpleDateFormat dateFormat = new SimpleDateFormat(format);
		Date date = null;
		try {
			date = dateFormat.parse(dateStr);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			System.out.println("date to string failure");
			e.printStackTrace();
		}
		return date;
	}

	/**
	 * 默认以 yy-MM-dd HH:mm:ss 格式时间
	 * 
	 * @param dateStr
	 * @return
	 */
	public static Date stringToDate(String dateStr) {
		return stringToDate(dateStr, "yy-MM-dd HH:mm:ss");
	}
}

package com.cenrise.test;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cenrise.commons.utils.RandomUtil;

public class InsertOracleSourceDestination {
	private long startTime; // 获取开始时间
	private long endTime; // 获取结束时间
	private static final Logger logger = LoggerFactory
			.getLogger(InsertOracleSourceDestination.class);

	/**
	 * 插入5个字段的数据
	 */
	@Test
	public void insertSource5() {
		String sql = "INSERT INTO SOURCE5_T(col1,col2,col3,col4,col5) values(?,?,?,?,?)";
		Connection connection = null;
		PreparedStatement ps = null;
		int batchNumber = 0;// 批次处理数
		try {
			connection = DBUtil.openConnection();
			ps = connection.prepareStatement(sql);
			final int batchSize = 1000;// 批处理数
			int count = 0;// 记数器
			startTime = System.currentTimeMillis();
			logger.info("数据库查询组件开始！");
			for (int i = 200000; i < 200001; i++) {
				ps.setInt(1, i);// INTEGER
				ps.setString(2, RandomUtil.getRandomChar(20));// CHAR(20)
				ps.setFloat(3, RandomUtil.getFloat());// FLOAT
				java.util.Date dateutil1 = RandomUtil.randomDate("1901-1-1",
						"2078-1-1");
				java.sql.Date date1 = new java.sql.Date(dateutil1.getTime());
				ps.setDate(4, date1);// DATE
				ps.setString(5, RandomUtil.getCharAndNumr(4000));// VARCHAR2(4000)
				ps.addBatch();// 添加批处理
				if (++count % batchSize == 0) {// 添加批提交数
					ps.executeBatch();
					endTime = System.currentTimeMillis();
					logger.info("第" + ++batchNumber + "批数据处理：" + count
							/ batchSize + "条数据，运行时间： " + (endTime - startTime)
							+ "ms");
				}
			}
			ps.executeBatch(); // 插入不够一批的数据
			endTime = System.currentTimeMillis();
			logger.info("所有数据处理完成：" + ++batchNumber + "批次，共" + count
					+ "条数据，运行时间： " + (endTime - startTime) + "ms");
		} catch (SQLException e) {
			System.out.println("SQLException: - " + e);
		} finally {
			try {
				connection.close();
				ps.close();
			} catch (SQLException e) {
				System.out.println("SQLException Finally: - " + e);
			}
		}
	}

	/**
	 * 插入10个字段的数据
	 */
	@Test
	public void insertSource10() {
		String sql = "INSERT INTO SOURCE10_T(col1, col2, col3, col4, col5, col6, col7, col8, col9, col10) values(?,?,?,?,?,?,?,?,?,?)";
		Connection connection = null;
		PreparedStatement ps = null;
		int batchNumber = 0;// 批次处理数
		try {
			connection = DBUtil.openConnection();
			ps = connection.prepareStatement(sql);
			final int batchSize = 1000;// 批处理数
			int count = 0;// 记数器
			startTime = System.currentTimeMillis();
			logger.info("数据库查询组件开始！");
			for (int i = 1; i <200000 ; i++) {
				ps.setInt(1, i);// INTEGER
				ps.setString(2, RandomUtil.getRandomChar(20));// CHAR(20)
				ps.setFloat(3, RandomUtil.getFloat());// FLOAT
				java.util.Date dateutil1 = RandomUtil.randomDate("1901-1-1",
						"2078-1-1");
				java.sql.Date date1 = new java.sql.Date(dateutil1.getTime());
				ps.setDate(4, date1);// DATE
				ps.setString(5, RandomUtil.getCharAndNumr(4000));// VARCHAR2(4000)
				ps.setString(6, RandomUtil.getCharAndNumr(4000));// VARCHAR2(4000)
				ps.setString(7, RandomUtil.getCharAndNumr(4000));// VARCHAR2(4000)
				ps.setString(8, RandomUtil.getCharAndNumr(4000));// VARCHAR2(4000)
				ps.setString(9, RandomUtil.getCharAndNumr(4000));// VARCHAR2(4000)
				ps.setString(10, RandomUtil.getCharAndNumr(4000));// VARCHAR2(4000)
				ps.addBatch();// 添加批处理
				if (++count % batchSize == 0) {// 添加批提交数
					ps.executeBatch();
					endTime = System.currentTimeMillis();
					logger.info("第" + ++batchNumber + "批数据处理：" + count
							/ batchSize + "条数据，运行时间： " + (endTime - startTime)
							+ "ms");
				}
			}
			ps.executeBatch(); // 插入不够一批的数据
			endTime = System.currentTimeMillis();
			logger.info("所有数据处理完成：" + ++batchNumber + "批次，共" + count
					+ "条数据，运行时间： " + (endTime - startTime) + "ms");

		} catch (SQLException e) {
			System.out.println("SQLException: - " + e);
		} finally {
			try {
				connection.close();
				ps.close();
			} catch (SQLException e) {
				System.out.println("SQLException Finally: - " + e);
			}
		}
	}

	/**
	 * 插入5个字段的数据
	 */
	@Test
	public void insertSource50() {
		String sql = "INSERT INTO SOURCE50_T(col1, col2, col3, col4, col5, col6, col7, col8, col9, col10, col11, col12, col13, col14, col15, col16, col17, col18, col19, col20, col21, col22, col23, col24, col25, col26, col27, col28, col29, col30, col31, col32, col33, col34, col35, col36, col37, col38, col39, col40, col41, col42, col43, col44, col45, col46, col47, col48, col49, col50) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
		Connection connection = null;
		PreparedStatement ps = null;
		int batchNumber = 0;// 批次处理数
		try {
			connection = DBUtil.openConnection();
			ps = connection.prepareStatement(sql);
			final int batchSize = 1000;// 批处理数
			int count = 0;// 记数器
			startTime = System.currentTimeMillis();
			logger.info("数据库查询组件开始！");
			for (int i = 2; i < 5000; i++) {
				ps.setInt(1, i);// INTEGER
				ps.setString(2, RandomUtil.getRandomChar(20));// CHAR(20)
				ps.setFloat(3, RandomUtil.getFloat());// FLOAT
				java.util.Date dateutil1 = RandomUtil.randomDate("1901-1-1",
						"2078-1-1");
				java.sql.Date date1 = new java.sql.Date(dateutil1.getTime());
				ps.setDate(4, date1);// DATE
				ps.setString(5, RandomUtil.getCharAndNumr(4000));// VARCHAR2(4000)
				ps.setString(6, RandomUtil.getCharAndNumr(4000));// VARCHAR2(4000)
				ps.setString(7, RandomUtil.getCharAndNumr(4000));// VARCHAR2(4000)
				ps.setString(8, RandomUtil.getCharAndNumr(4000));// VARCHAR2(4000)
				ps.setString(9, RandomUtil.getCharAndNumr(4000));// VARCHAR2(4000)
				ps.setString(10, RandomUtil.getCharAndNumr(4000));// VARCHAR2(4000)
				ps.setString(11, RandomUtil.getCharAndNumr(4000));// VARCHAR2(4000)
				ps.setString(12, RandomUtil.getCharAndNumr(4000));// VARCHAR2(4000)
				ps.setString(13, RandomUtil.getCharAndNumr(4000));// VARCHAR2(4000)
				ps.setString(14, RandomUtil.getCharAndNumr(4000));// VARCHAR2(4000)
				ps.setString(15, RandomUtil.getCharAndNumr(4000));// VARCHAR2(4000)
				ps.setString(16, RandomUtil.getCharAndNumr(4000));// VARCHAR2(4000)
				ps.setString(17, RandomUtil.getCharAndNumr(4000));// VARCHAR2(4000)
				ps.setString(18, RandomUtil.getCharAndNumr(4000));// VARCHAR2(4000)
				ps.setString(19, RandomUtil.getCharAndNumr(4000));// VARCHAR2(4000)
				ps.setString(20, RandomUtil.getCharAndNumr(4000));// VARCHAR2(4000)
				ps.setString(21, RandomUtil.getCharAndNumr(4000));// VARCHAR2(4000)
				ps.setString(22, RandomUtil.getCharAndNumr(4000));// VARCHAR2(4000)
				ps.setString(23, RandomUtil.getCharAndNumr(4000));// VARCHAR2(4000)
				ps.setString(24, RandomUtil.getCharAndNumr(4000));// VARCHAR2(4000)
				ps.setString(25, RandomUtil.getCharAndNumr(4000));// VARCHAR2(4000)
				ps.setString(26, RandomUtil.getCharAndNumr(4000));// VARCHAR2(4000)
				ps.setString(27, RandomUtil.getCharAndNumr(4000));// VARCHAR2(4000)
				ps.setString(28, RandomUtil.getCharAndNumr(4000));// VARCHAR2(4000)
				ps.setString(29, RandomUtil.getCharAndNumr(4000));// VARCHAR2(4000)
				ps.setString(30, RandomUtil.getCharAndNumr(4000));// VARCHAR2(4000)
				ps.setString(31, RandomUtil.getCharAndNumr(4000));// VARCHAR2(4000)
				ps.setString(32, RandomUtil.getCharAndNumr(4000));// VARCHAR2(4000)
				ps.setString(33, RandomUtil.getCharAndNumr(4000));// VARCHAR2(4000)
				ps.setString(34, RandomUtil.getCharAndNumr(4000));// VARCHAR2(4000)
				ps.setString(35, RandomUtil.getCharAndNumr(4000));// VARCHAR2(4000)
				ps.setString(36, RandomUtil.getCharAndNumr(4000));// VARCHAR2(4000)
				ps.setString(37, RandomUtil.getCharAndNumr(4000));// VARCHAR2(4000)
				ps.setString(38, RandomUtil.getCharAndNumr(4000));// VARCHAR2(4000)
				ps.setString(39, RandomUtil.getCharAndNumr(4000));// VARCHAR2(4000)
				ps.setString(40, RandomUtil.getCharAndNumr(4000));// VARCHAR2(4000)
				ps.setString(41, RandomUtil.getCharAndNumr(4000));// VARCHAR2(4000)
				ps.setString(42, RandomUtil.getCharAndNumr(4000));// VARCHAR2(4000)
				ps.setString(43, RandomUtil.getCharAndNumr(4000));// VARCHAR2(4000)
				ps.setString(44, RandomUtil.getCharAndNumr(4000));// VARCHAR2(4000)
				ps.setString(45, RandomUtil.getCharAndNumr(4000));// VARCHAR2(4000)
				ps.setString(46, RandomUtil.getCharAndNumr(4000));// VARCHAR2(4000)
				ps.setString(47, RandomUtil.getCharAndNumr(4000));// VARCHAR2(4000)
				ps.setString(48, RandomUtil.getCharAndNumr(4000));// VARCHAR2(4000)
				ps.setString(49, RandomUtil.getCharAndNumr(4000));// VARCHAR2(4000)
				ps.setString(50, RandomUtil.getCharAndNumr(4000));// VARCHAR2(4000)
				ps.addBatch();// 添加批处理
				if (++count % batchSize == 0) {// 添加批提交数
					ps.executeBatch();
					endTime = System.currentTimeMillis();
					logger.info("第" + ++batchNumber + "批数据处理：" + count
							/ batchSize + "条数据，运行时间： " + (endTime - startTime)
							+ "ms");
				}
			}
			ps.executeBatch(); // 插入不够一批的数据
			endTime = System.currentTimeMillis();
			logger.info("所有数据处理完成：" + ++batchNumber + "批次，共" + count
					+ "条数据，运行时间： " + (endTime - startTime) + "ms");
		} catch (SQLException e) {
			System.out.println("SQLException: - " + e);
		} finally {
			try {
				connection.close();
				ps.close();
			} catch (SQLException e) {
				System.out.println("SQLException Finally: - " + e);
			}
		}
	}

	/**
	 * 插入100个字段的数据
	 */
	@Test
	public void insertSource100() {
		String sql = "INSERT INTO SOURCE100_T(col1, col2, col3, col4, col5, col6, col7, col8, col9, col10, col11, col12, col13, col14, col15, col16, col17, col18, col19, col20, col21, col22, col23, col24, col25, col26, col27, col28, col29, col30, col31, col32, col33, col34, col35, col36, col37, col38, col39, col40, col41, col42, col43, col44, col45, col46, col47, col48, col49, col50, col51, col52, col53, col54, col55, col56, col57, col58, col59, col60, col61, col62, col63, col64, col65, col66, col67, col68, col69, col70, col71, col72, col73, col74, col75, col76, col77, col78, col79, col80, col81, col82, col83, col84, col85, col86, col87, col88, col89, col90, col91, col92, col93, col94, col95, col96, col97, col98, col99, col100) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
		Connection connection = null;
		PreparedStatement ps = null;
		int batchNumber = 0;// 批次处理数
		try {
			connection = DBUtil.openConnection();
			ps = connection.prepareStatement(sql);
			final int batchSize = 1000;// 批处理数
			int count = 0;// 记数器
			startTime = System.currentTimeMillis();
			logger.info("数据库查询组件开始！");
			for (int i = 165001; i < 200000; i++) {
				ps.setInt(1, i);// INTEGER
				ps.setString(2, RandomUtil.getRandomChar(20));// CHAR(20)
				ps.setFloat(3, RandomUtil.getFloat());// FLOAT
				java.util.Date dateutil1 = RandomUtil.randomDate("1901-1-1",
						"2078-1-1");
				java.sql.Date date1 = new java.sql.Date(dateutil1.getTime());
				ps.setDate(4, date1);// DATE
				ps.setString(5, RandomUtil.getCharAndNumr(4000));// VARCHAR2(4000)
				ps.setString(6, RandomUtil.getCharAndNumr(4000));// VARCHAR2(4000)
				ps.setString(7, RandomUtil.getCharAndNumr(4000));// VARCHAR2(4000)
				ps.setString(8, RandomUtil.getCharAndNumr(4000));// VARCHAR2(4000)
				ps.setString(9, RandomUtil.getCharAndNumr(4000));// VARCHAR2(4000)
				ps.setString(10, RandomUtil.getCharAndNumr(4000));// VARCHAR2(4000)
				ps.setString(11, RandomUtil.getCharAndNumr(4000));// VARCHAR2(4000)
				ps.setString(12, RandomUtil.getCharAndNumr(4000));// VARCHAR2(4000)
				ps.setString(13, RandomUtil.getCharAndNumr(4000));// VARCHAR2(4000)
				ps.setString(14, RandomUtil.getCharAndNumr(4000));// VARCHAR2(4000)
				ps.setString(15, RandomUtil.getCharAndNumr(4000));// VARCHAR2(4000)
				ps.setString(16, RandomUtil.getCharAndNumr(4000));// VARCHAR2(4000)
				ps.setString(17, RandomUtil.getCharAndNumr(4000));// VARCHAR2(4000)
				ps.setString(18, RandomUtil.getCharAndNumr(4000));// VARCHAR2(4000)
				ps.setString(19, RandomUtil.getCharAndNumr(4000));// VARCHAR2(4000)
				ps.setString(20, RandomUtil.getCharAndNumr(4000));// VARCHAR2(4000)
				ps.setString(21, RandomUtil.getCharAndNumr(4000));// VARCHAR2(4000)
				ps.setString(22, RandomUtil.getCharAndNumr(4000));// VARCHAR2(4000)
				ps.setString(23, RandomUtil.getCharAndNumr(4000));// VARCHAR2(4000)
				ps.setString(24, RandomUtil.getCharAndNumr(4000));// VARCHAR2(4000)
				ps.setString(25, RandomUtil.getCharAndNumr(4000));// VARCHAR2(4000)
				ps.setString(26, RandomUtil.getCharAndNumr(4000));// VARCHAR2(4000)
				ps.setString(27, RandomUtil.getCharAndNumr(4000));// VARCHAR2(4000)
				ps.setString(28, RandomUtil.getCharAndNumr(4000));// VARCHAR2(4000)
				ps.setString(29, RandomUtil.getCharAndNumr(4000));// VARCHAR2(4000)
				ps.setString(30, RandomUtil.getCharAndNumr(4000));// VARCHAR2(4000)
				ps.setString(31, RandomUtil.getCharAndNumr(4000));// VARCHAR2(4000)
				ps.setString(32, RandomUtil.getCharAndNumr(4000));// VARCHAR2(4000)
				ps.setString(33, RandomUtil.getCharAndNumr(4000));// VARCHAR2(4000)
				ps.setString(34, RandomUtil.getCharAndNumr(4000));// VARCHAR2(4000)
				ps.setString(35, RandomUtil.getCharAndNumr(4000));// VARCHAR2(4000)
				ps.setString(36, RandomUtil.getCharAndNumr(4000));// VARCHAR2(4000)
				ps.setString(37, RandomUtil.getCharAndNumr(4000));// VARCHAR2(4000)
				ps.setString(38, RandomUtil.getCharAndNumr(4000));// VARCHAR2(4000)
				ps.setString(39, RandomUtil.getCharAndNumr(4000));// VARCHAR2(4000)
				ps.setString(40, RandomUtil.getCharAndNumr(4000));// VARCHAR2(4000)
				ps.setString(41, RandomUtil.getCharAndNumr(4000));// VARCHAR2(4000)
				ps.setString(42, RandomUtil.getCharAndNumr(4000));// VARCHAR2(4000)
				ps.setString(43, RandomUtil.getCharAndNumr(4000));// VARCHAR2(4000)
				ps.setString(44, RandomUtil.getCharAndNumr(4000));// VARCHAR2(4000)
				ps.setString(45, RandomUtil.getCharAndNumr(4000));// VARCHAR2(4000)
				ps.setString(46, RandomUtil.getCharAndNumr(4000));// VARCHAR2(4000)
				ps.setString(47, RandomUtil.getCharAndNumr(4000));// VARCHAR2(4000)
				ps.setString(48, RandomUtil.getCharAndNumr(4000));// VARCHAR2(4000)
				ps.setString(49, RandomUtil.getCharAndNumr(4000));// VARCHAR2(4000)
				ps.setString(50, RandomUtil.getCharAndNumr(4000));// VARCHAR2(4000)
				ps.setString(51, RandomUtil.getCharAndNumr(4000));// VARCHAR2(4000)
				ps.setString(52, RandomUtil.getCharAndNumr(4000));// VARCHAR2(4000)
				ps.setString(53, RandomUtil.getCharAndNumr(4000));// VARCHAR2(4000)
				ps.setString(54, RandomUtil.getCharAndNumr(4000));// VARCHAR2(4000)
				ps.setString(55, RandomUtil.getCharAndNumr(4000));// VARCHAR2(4000)
				ps.setString(56, RandomUtil.getCharAndNumr(4000));// VARCHAR2(4000)
				ps.setString(57, RandomUtil.getCharAndNumr(4000));// VARCHAR2(4000)
				ps.setString(58, RandomUtil.getCharAndNumr(4000));// VARCHAR2(4000)
				ps.setString(59, RandomUtil.getCharAndNumr(4000));// VARCHAR2(4000)
				ps.setString(60, RandomUtil.getCharAndNumr(4000));// VARCHAR2(4000)
				ps.setString(61, RandomUtil.getCharAndNumr(4000));// VARCHAR2(4000)
				ps.setString(62, RandomUtil.getCharAndNumr(4000));// VARCHAR2(4000)
				ps.setString(63, RandomUtil.getCharAndNumr(4000));// VARCHAR2(4000)
				ps.setString(64, RandomUtil.getCharAndNumr(4000));// VARCHAR2(4000)
				ps.setString(65, RandomUtil.getCharAndNumr(4000));// VARCHAR2(4000)
				ps.setString(66, RandomUtil.getCharAndNumr(4000));// VARCHAR2(4000)
				ps.setString(67, RandomUtil.getCharAndNumr(4000));// VARCHAR2(4000)
				ps.setString(68, RandomUtil.getCharAndNumr(4000));// VARCHAR2(4000)
				ps.setString(69, RandomUtil.getCharAndNumr(4000));// VARCHAR2(4000)
				ps.setString(70, RandomUtil.getCharAndNumr(4000));// VARCHAR2(4000)
				ps.setString(71, RandomUtil.getCharAndNumr(4000));// VARCHAR2(4000)
				ps.setString(72, RandomUtil.getCharAndNumr(4000));// VARCHAR2(4000)
				ps.setString(73, RandomUtil.getCharAndNumr(4000));// VARCHAR2(4000)
				ps.setString(74, RandomUtil.getCharAndNumr(4000));// VARCHAR2(4000)
				ps.setString(75, RandomUtil.getCharAndNumr(4000));// VARCHAR2(4000)
				ps.setString(76, RandomUtil.getCharAndNumr(4000));// VARCHAR2(4000)
				ps.setString(77, RandomUtil.getCharAndNumr(4000));// VARCHAR2(4000)
				ps.setString(78, RandomUtil.getCharAndNumr(4000));// VARCHAR2(4000)
				ps.setString(79, RandomUtil.getCharAndNumr(4000));// VARCHAR2(4000)
				ps.setString(80, RandomUtil.getCharAndNumr(4000));// VARCHAR2(4000)
				ps.setString(81, RandomUtil.getCharAndNumr(4000));// VARCHAR2(4000)
				ps.setString(82, RandomUtil.getCharAndNumr(4000));// VARCHAR2(4000)
				ps.setString(83, RandomUtil.getCharAndNumr(4000));// VARCHAR2(4000)
				ps.setString(84, RandomUtil.getCharAndNumr(4000));// VARCHAR2(4000)
				ps.setString(85, RandomUtil.getCharAndNumr(4000));// VARCHAR2(4000)
				ps.setString(86, RandomUtil.getCharAndNumr(4000));// VARCHAR2(4000)
				ps.setString(87, RandomUtil.getCharAndNumr(4000));// VARCHAR2(4000)
				ps.setString(88, RandomUtil.getCharAndNumr(4000));// VARCHAR2(4000)
				ps.setString(89, RandomUtil.getCharAndNumr(4000));// VARCHAR2(4000)
				ps.setString(90, RandomUtil.getCharAndNumr(4000));// VARCHAR2(4000)
				ps.setString(91, RandomUtil.getCharAndNumr(4000));// VARCHAR2(4000)
				ps.setString(92, RandomUtil.getCharAndNumr(4000));// VARCHAR2(4000)
				ps.setString(93, RandomUtil.getCharAndNumr(4000));// VARCHAR2(4000)
				ps.setString(94, RandomUtil.getCharAndNumr(4000));// VARCHAR2(4000)
				ps.setString(95, RandomUtil.getCharAndNumr(4000));// VARCHAR2(4000)
				ps.setString(96, RandomUtil.getCharAndNumr(4000));// VARCHAR2(4000)
				ps.setString(97, RandomUtil.getCharAndNumr(4000));// VARCHAR2(4000)
				ps.setString(98, RandomUtil.getCharAndNumr(4000));// VARCHAR2(4000)
				ps.setString(99, RandomUtil.getCharAndNumr(4000));// VARCHAR2(4000)
				ps.setString(100, RandomUtil.getCharAndNumr(4000));// VARCHAR2(4000)
				ps.addBatch();// 添加批处理
				if (++count % batchSize == 0) {// 添加批提交数
					ps.executeBatch();
					endTime = System.currentTimeMillis();
					logger.info("第" + ++batchNumber + "批数据处理：" + count
							/ batchSize + "条数据，运行时间： " + (endTime - startTime)
							+ "ms");
				}
			}
			ps.executeBatch(); // 插入不够一批的数据
			endTime = System.currentTimeMillis();
			logger.info("所有数据处理完成：" + ++batchNumber + "批次，共" + count
					+ "条数据，运行时间： " + (endTime - startTime) + "ms");
		} catch (SQLException e) {
			System.out.println("SQLException: - " + e);
		} finally {
			try {
				connection.close();
				ps.close();
			} catch (SQLException e) {
				System.out.println("SQLException Finally: - " + e);
			}
		}
	}

	public static void main(String[] args) throws Exception {
		String sql = "SELECT * FROM SOURCE5_T";
		Connection connection = DBUtil.openConnection();
		PreparedStatement prepareStatement = connection.prepareStatement(sql);
		ResultSet rs = prepareStatement.executeQuery();
		System.out.println(rs);
	}
}

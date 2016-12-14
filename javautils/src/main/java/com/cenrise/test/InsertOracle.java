package com.cenrise.test;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

import org.junit.Test;

import com.cenrise.commons.utils.RandomUtil;

/**
 * 保存数据到Oracle
 * 
 * @author jiadp
 *
 */
public class InsertOracle {

	/**
	 * 所有的Oracle类型
	 * 
	 * @throws FileNotFoundException
	 *             目前的数据为单条记录524K
	 */
	@Test
	public void getInsertOracleDB() {
		// intervaldaytosecondtype,intervalyeartomonthtype,
		String sql = "INSERT INTO ORACLEDB(id,numbertype,longtype,varchar2type,nvarchar2type,binary_floattype,binary_doubletype,"
				+ "chartype,datetype,rawtype,timestamptype,timestampwithlocaltimezonetype,timestampwithtimezonetype,clobtype,nclobtype,blobtype) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

		Connection connection = null;
		PreparedStatement ps = null;
		FileInputStream inputStream = null;
		try {
			// File image = new File("D:/111.bmp");
			// inputStream = new FileInputStream(image);
			connection = DBUtil.openConnection();
			ps = connection.prepareStatement(sql);
			final int batchSize = 2000;// 批处理数
			int count = 0;// 记数器
			for (int i = 108001; i < 200001; i++) {
				ps.setInt(1, i/* RandomUtil.getNum(1, 10000) */);// id
				ps.setInt(2, RandomUtil.getNum(1, 10000));// numbertype
				ps.setLong(3, RandomUtil.getNum(1, 10000));// varchar
				ps.setString(4, RandomUtil.getCharAndNumr(5));// varchar2
				ps.setString(5, RandomUtil.getCharAndNumr(7));// nvarchar2
				ps.setFloat(6, 12.7f);
				ps.setDouble(7, 21.1d);
				ps.setString(8, "8");// char(50)
				/*
				 * java.sql.Date date = new java.sql.Date(
				 * System.currentTimeMillis());
				 */
				java.util.Date dateutil1 = RandomUtil.randomDate("1901-1-1",
						"2078-1-1");
				java.sql.Date date1 = new java.sql.Date(dateutil1.getTime());
				// SimpleDateFormat f=new
				// SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
				// String dateStr = f.format(date1);
				ps.setDate(9, date1);// 改成string
				// ps.setString(10, null);// INTERVAL DAY(2) TO SECOND(6)
				// ps.setString(11, null);// INTERVAL YEAR(2) TO MONTH
				ps.setBytes(10, "北京欢迎您 For binary！".getBytes());// RAW(200),用于存储二进制
				Timestamp timestamp = new Timestamp(System.currentTimeMillis());
				ps.setTimestamp(11, timestamp);// timestamp
				ps.setTimestamp(12, timestamp);// TIMESTAMP(6) WITH LOCAL
												// TIME ZONE
				ps.setTimestamp(13, timestamp);// TIMESTAMP(6) WITH TIME
												// ZONE
				ps.setString(14, "CLOB");// CLOB
				ps.setString(15, "NCLOB");// NCLOB
				// Mysql可以用SerialBlob，Oracle不可以，错误如：java.lang.ClassCastException:
				// javax.sql.rowset.serial.SerialBlob cannot be cast to
				// oracle.sql.BLOB
				// Blob blob = new SerialBlob("北京欢迎您！".getBytes());
				// ps.setBlob(18, blob);// BLOB

				// 设置二进制BLOB参数
				File file_blob = new File("D:\\222.jpg");
				try {// 操作Oracle的Blob类型
					InputStream in = new BufferedInputStream(
							new FileInputStream(file_blob));
					ps.setBinaryStream(16, in, (int) file_blob.length());
				} catch (Exception e) {
					System.out.println("操作OracleBlob错误" + e);
				}
				ps.addBatch();// 添加批处理
				if (++count % batchSize == 0) {//添加批提交数
					ps.executeBatch();
				}
			}
			ps.executeBatch(); // 插入不够一批的数据

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
	 * Date类型有问题，跳过DI-158
	 */
	@Test
	public void getInsertOracleDBRS() {
		// String sqlSelectFrom = "select * from ORACLEDB";
		String sqlSelectFrom = "select id , numbertype , longtype , varchar2type , nvarchar2type , charType , dateType , rawtype , timestampType , clobtype , nclobtype from tong.oracledb where datetype >=to_date('2000-01-01 13:23:44','yyyy-mm-dd hh24:mi:ss')";
		Connection connection = null;
		PreparedStatement ps = null;

		try {
			connection = DBUtil.openConnection();
			ps = connection.prepareStatement(sqlSelectFrom,
			/* ResultSet.TYPE_FORWARD_ONLY */ResultSet.TYPE_SCROLL_SENSITIVE,
					ResultSet.CONCUR_READ_ONLY);
			// ps = connection.prepareStatement(sqlSelectFrom);
			ResultSet rs = ps.executeQuery();

			int rowcount = 0;
			if (rs.last()) {
				rowcount = rs.getRow();
				rs.beforeFirst(); // 把指针再移到初始化的位置
			}

			while (rs.next()) {
				String dateValue = rs.getString(7);// date
				System.out.println(dateValue);
			}

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
		String sql = "SELECT * FROM oracledb";
		Connection connection = DBUtil.openConnection();
		PreparedStatement prepareStatement = connection.prepareStatement(sql);
		ResultSet rs = prepareStatement.executeQuery();
		System.out.println(rs);
	}
}

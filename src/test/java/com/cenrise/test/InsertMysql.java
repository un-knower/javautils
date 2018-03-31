package com.cenrise.test;

import java.io.FileInputStream;
import java.math.BigDecimal;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.Date;

import javax.sql.rowset.serial.SerialBlob;

import org.junit.Test;

import com.cenrise.utils.RandomUtil;

/**
 * 保存数据到Mysql
 * 
 * @author Admin
 *
 */
public class InsertMysql {

	/**
	 * 所有的Mysql类型
	 */
	@Test
	public void getInsertMysqlDB() {
		String sql = "INSERT INTO mysqldb(id,varcharType,timeType,timestampType,doubleType,decimalType,textType,"
				+ "blobType,yearType,enumType,setType,bigintType,bitType,floatType,charType,binaryType,varbinaryType) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

		Connection connection = null;
		PreparedStatement ps = null;
		FileInputStream inputStream = null;

		try {
			// File image = new File("D:/111.bmp");
			// inputStream = new FileInputStream(image);
			connection = DBUtil.openConnection();
			ps = connection.prepareStatement(sql);

			for (int i = 1; i < 20; i++) {
				ps.setInt(1, RandomUtil.getNum(1, 10000));// id
				ps.setString(2, RandomUtil.getCharAndNumr(10));// varchar
				Time time = new Time(System.currentTimeMillis());
				ps.setTime(3, time);// time
				Timestamp timestamp = new Timestamp(System.currentTimeMillis());
				ps.setTimestamp(4, timestamp);// timestamp
				ps.setDouble(5, 12.23);// double
				BigDecimal bigDecimal = new BigDecimal(21.21);
				ps.setBigDecimal(6, bigDecimal);// decimal
				ps.setString(7, RandomUtil.getCharAndNumr(4));// text
				Blob blob = new SerialBlob("北京欢迎您！".getBytes());
				ps.setBlob(8, blob);// blob
				ps.setString(9, "2016");// year
				ps.setString(10, "1");// emnu
				ps.setString(11, "西藏");// set
				ps.setInt(12, 13);// bigint
				ps.setBoolean(13, true);// bit
				ps.setFloat(14, 12);// float
				ps.setString(15, "char value");// char
				ps.setBytes(16, "北京欢迎您 For binary！".getBytes());// binary
				ps.setBytes(17, "北京欢迎您 For varbinary！".getBytes());// varbinary
				ps.executeUpdate();
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
		String sql = "SELECT * FROM mysqldb";
		Connection connection = DBUtil.openConnection();
		PreparedStatement prepareStatement = connection.prepareStatement(sql);
		ResultSet rs = prepareStatement.executeQuery();
		System.out.println(rs);
	}

	@Test
	public void writeBlobForByte() throws Exception {
		String updateSQL = "UPDATE test101 " + "SET timestampC = ? "
				+ "WHERE id=?";//
		Connection conn = DBUtil.openConnection();
		PreparedStatement pstmt = conn.prepareStatement(updateSQL);
		// read the file
		Date date = new Date();

		Timestamp time = new Timestamp(date.getTime());

		byte[] b = time.toString().getBytes();
		// set parameters
		pstmt.setBytes(1, b);
		pstmt.setInt(2, 1);
		pstmt.executeUpdate();

	}

	@Test
	public void getSQLFordd() {
		for (int i = 100; i < 1000000; i++) {
			String valueVar = ",'前门大街','2017-02-22','11:22:22','2015-11-21 11:21:00','0','23.99',0xB1B1BEA9CCECB0B2C3C5,0x32333233322E323332,'2012','1','1'";
			System.out.println(i + valueVar);
		}
	}

}

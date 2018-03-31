package com.cenrise.test;

import java.io.FileInputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.sql.Timestamp;

import org.junit.Test;

import com.cenrise.utils.RandomUtil;

/**
 * 保存数据到sqlserver
 * 
 * @author jiadp
 *
 */
public class InsertSqlServer {

	/**
	 * 所有的SqlServer类型
	 */
	@Test
	public void getInsertSqlServerDB() {// 34个类型
		String sql = "INSERT INTO sqlserverdb(id,bigintType,binaryType,bitType,charType,dateType,datetimeType,datetime2Type,datetimeoffsetType,decimalType,"
				+ "floatType,imageType,ncharType,ntextType,numericType,nvarcharType,realType,smalldatetimeType,smallintType,textType,"
				+ "timeType,tinyintType,uniqueidentifierType,varbinaryType,varcharType,xmlType) values("
				+ "?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

		Connection connection = null;
		PreparedStatement ps = null;
		FileInputStream inputStream = null;

		try {
			connection = DBUtil.openConnection();
			ps = connection.prepareStatement(sql);

			for (int i = 3; i < 10; i++) {
				ps.setInt(1, RandomUtil.getNum(1, 10000));// id
				ps.setInt(2, RandomUtil.getNum(1, 10000));// bigint
				ps.setBytes(3, "北京欢迎您！".getBytes());// binary
				ps.setBoolean(4, true);// bit
				ps.setString(5, "charabc");// char
				java.util.Date dateutil1 = RandomUtil.randomDate("1901-1-1",
						"2078-1-1");
				java.sql.Date date1 = new java.sql.Date(dateutil1.getTime());
				// new java.sql.Date(System.currentTimeMillis());
				ps.setDate(6, date1);
				java.util.Date dateutil2 = RandomUtil.randomDate("1901-1-1",
						"2078-1-1");
				java.sql.Date date2 = new java.sql.Date(dateutil2.getTime());
				ps.setDate(7, date2);// datetime
				java.util.Date dateutil3 = RandomUtil.randomDate("1901-1-1",
						"2078-1-1");
				java.sql.Date date3 = new java.sql.Date(dateutil3.getTime());
				ps.setDate(8, date3);// datetime2
				java.util.Date dateutil4 = RandomUtil.randomDate("1901-1-1",
						"2078-1-1");
				java.sql.Date date4 = new java.sql.Date(dateutil4.getTime());
				ps.setDate(9, date4);// date time off set
				ps.setDouble(10, 1.1);// decimal
				ps.setDouble(11, 1.11);// float
				ps.setBytes(12, "北京欢迎您！".getBytes());// image
				ps.setString(13, "a2a1");// nchar
				ps.setString(14, "ntext值");// ntext
				ps.setDouble(15, 1.19);// numeric
				ps.setString(16, "113bbbb");// nvarchar
				ps.setDouble(17, 1.1);// real
				java.util.Date dateutil5 = RandomUtil.randomDate("1901-1-1",
						"2078-1-1");
				java.sql.Date date5 = new java.sql.Date(dateutil5.getTime());
				ps.setDate(18, date5);// smalldatetime
				ps.setInt(19, RandomUtil.getNum(1, 10000));// smallint
				ps.setString(20, "23fsa");// text
				Time time = new Time(System.currentTimeMillis());
				ps.setTime(21, time);// time
				ps.setInt(22, 12);// tinyint
				ps.setString(23, "6F9619FF-8B86-D011-B42D-00C04FC964FF");// uniqueidentifier
				ps.setBytes(24, "北京欢迎您 For binary！".getBytes());// varbinary
				ps.setString(25, "北京欢迎您 For binary！");// varchar
				ps.setString(26, "<Person><ID>1</ID><Name>刘备</Name></Person>");// xml
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
		String sql = "SELECT * FROM sqlserverdb";
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
		java.util.Date date = new java.util.Date();
		Timestamp time = new Timestamp(date.getTime());

		byte[] b = time.toString().getBytes();
		// set parameters
		pstmt.setBytes(1, b);
		pstmt.setInt(2, 1);
		pstmt.executeUpdate();

	}

}

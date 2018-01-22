package com.cenrise.test;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Properties;

import javax.sql.DataSource;

import org.apache.commons.dbcp.BasicDataSourceFactory;

/**
 * 
 * 连接数据库 用于获得数据库连接的工具类
 * 
 * @author jiadp
 */
public class DBUtil {
    // 连接池对象
    private static DataSource dataSource;
    
    // 负责将connection与当前执行线程绑定
    private static ThreadLocal<Connection> connLocal = new ThreadLocal<Connection>();
    
    static {
        try {
            Properties props = new Properties();
            props.load(DBUtil.class.getClassLoader().getResourceAsStream("dbcp.properties"));
            dataSource = BasicDataSourceFactory.createDataSource(props);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public static Connection openConnection()
        throws SQLException {
        Connection conn = connLocal.get();
        if (conn == null) {
            conn = dataSource.getConnection();
            connLocal.set(conn);
        }
        return conn;
    }
    
    public static void closeConnection()
        throws SQLException {
        Connection conn = connLocal.get();
        connLocal.set(null);
        if (conn != null && !conn.isClosed()) {
            conn.close();
        }
    }

    public static void main(String[] args) {
        try {
            Connection conncection= DBUtil.openConnection();
            Connection conn = null;
            PreparedStatement pre = null;
            ResultSet rs = null;

//            pre = conncection.prepareStatement("SELECT * FROM T_TIF_IF");
            pre = conncection.prepareStatement("SELECT * FROM azkaban.active_sla");
            rs = pre.executeQuery();
            while (rs.next()) {
                rs.getString(1);
            }
            System.out.println(conncection.getCatalog());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

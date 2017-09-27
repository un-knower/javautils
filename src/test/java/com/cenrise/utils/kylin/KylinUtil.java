package com.cenrise.utils.kylin;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.Connection;
import java.sql.Driver;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Calendar;
import java.util.Date;
import java.util.Properties;

/**
 * 工具方法
 */
public class KylinUtil {
    public static final String CR = System.getProperty("line.separator");

    /**
     * 打印当前堆栈信息
     *
     * @param e
     * @return
     */
    public static String getStackTracker(Throwable e) {
        return getClassicStackTrace(e);
    }

    public static String getClassicStackTrace(Throwable e) {
        StringWriter stringWriter = new StringWriter();
        PrintWriter printWriter = new PrintWriter(stringWriter);
        e.printStackTrace(printWriter);
        String string = stringWriter.toString();
        try {
            stringWriter.close();
        } catch (IOException ioe) {
            // is this really required?
        }
        return string;
    }

    /**
     * 返回前几天的，某个时间的时分秒，如2017-06-16 8:27:59秒
     *
     * @param beforeday 前几天
     * @param hour
     * @param minus
     * @param second
     * @return
     */
    public static Date beforeDateTime(int beforeday, int hour, int minus, int second) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DATE, calendar.get(Calendar.DATE) - beforeday);
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, minus);
        calendar.set(Calendar.SECOND, second);
        return calendar.getTime();
    }

    /**
     * kylin jdbc 例子
     *
     * @throws Exception
     */
    public void queryByJDBC() throws Exception {
        // 加载Kylin的JDBC驱动程序
        Driver driver = (Driver) Class.forName("org.apache.kylin.jdbc.Driver").newInstance();

        // 配置登录Kylin的用户名和密码
        Properties info = new Properties();
        info.put("user", "ADMIN");
        info.put("password", "KYLIN");

        // 连接Kylin服务
        Connection conn = driver.connect("jdbc:kylin://10.1.30.101:7070/sxf_dc", info);
        Statement state = conn.createStatement();
        ResultSet resultSet = state.executeQuery("select count(DT_UTE) from BI_MPOS_FLOW  group by DT_UTE limit 5");

        while (resultSet.next()) {
            String col1 = resultSet.getString(1);
//            String col2 = resultSet.getString(2);
//            String col3 = resultSet.getString(3);
            System.out.println(col1);
        }

        //获取元数据
        ResultSet tables = conn.getMetaData().getTables(null, null, "BI_MPOS_FLOW", null);
        while (tables.next()) {
            System.out.println("=========获取元数据===========");
            for (int i = 0; i < 10; i++) {
                System.out.println(tables.getString(i + 1));
            }
        }
    }

}

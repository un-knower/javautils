package com.cenrise.test.azkaban;

import org.apache.commons.dbcp.BasicDataSourceFactory;

import javax.sql.DataSource;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Properties;

/**
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
            Connection conncection = DBUtil.openConnection();
            Connection conn = null;
            PreparedStatement pre = null;
            ResultSet rs = null;

            pre = conncection.prepareStatement("SELECT  id, name, active, modified_time, create_time, version, last_modified_by, description, enc_type, settings_blob FROM projects WHERE id=6");
            rs = pre.executeQuery();
            List<Object> objectsProject = DBUtil.handleProjects(rs);
//            flowFromObject(objectsProject);
            System.out.println(objectsProject.size());

            pre = conncection.prepareStatement("SELECT project_id, version, flow_id, modified_time, encoding_type, json FROM project_flows WHERE project_id=6 AND version=3 AND flow_id='end'");
            rs = pre.executeQuery();
            List<Object> objectsFlows = DBUtil.handleFlows(rs);
//            flowFromObject(objectsFlows);
            System.out.println(objectsFlows.size());


        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static List<Object> handleProjects(final ResultSet rs) throws SQLException {
        if (!rs.next()) {
            return Collections.emptyList();
        }

        final ArrayList<Object> projects = new ArrayList<>();
        do {
            final int id = rs.getInt(1);
            final String name = rs.getString(2);
            final boolean active = rs.getBoolean(3);
            final long modifiedTime = rs.getLong(4);
            final long createTime = rs.getLong(5);
            final int version = rs.getInt(6);
            final String lastModifiedBy = rs.getString(7);
            final String description = rs.getString(8);
            final int encodingType = rs.getInt(9);
            final byte[] data = rs.getBytes(10);

            if (data != null) {
                final EncodingType encType = EncodingType.fromInteger(encodingType);
                final Object blobObj;
                try {
                    // Convoluted way to inflate strings. Should find common package or
                    // helper function.
                    if (encType == EncodingType.GZIP) {
                        // Decompress the sucker.
                        final String jsonString = GZIPUtils.unGzipString(data, "UTF-8");
                        blobObj = JSONUtils.parseJSONFromString(jsonString);
                    } else {
                        final String jsonString = new String(data, "UTF-8");
                        blobObj = JSONUtils.parseJSONFromString(jsonString);
                    }
                    projects.add(blobObj);
//                    project = Project.projectFromObject(blobObj);
                } catch (final IOException e) {
                    throw new SQLException("Failed to get project.", e);
                }
            } else {
            }
        } while (rs.next());

        return projects;
    }

    /* public static String SELECT_ALL_PROJECT_FLOWS =
             "SELECT project_id, version, flow_id, modified_time, encoding_type, json FROM project_flows WHERE project_id=? AND version=?";
 */
    public static List<Object> handleFlows(final ResultSet rs) throws SQLException {
        if (!rs.next()) {
            return Collections.emptyList();
        }

        final ArrayList<Object> flows = new ArrayList<>();
        do {
            final String flowId = rs.getString(3);
            final int encodingType = rs.getInt(5);
            final byte[] dataBytes = rs.getBytes(6);

            if (dataBytes == null) {
                continue;
            }

            final EncodingType encType = EncodingType.fromInteger(encodingType);

            Object flowObj = null;
            try {
                // Convoluted way to inflate strings. Should find common package or
                // helper function.
                if (encType == EncodingType.GZIP) {
                    // Decompress the sucker.
                    final String jsonString = GZIPUtils.unGzipString(dataBytes, "UTF-8");
                    flowObj = JSONUtils.parseJSONFromString(jsonString);
                } else {
                    final String jsonString = new String(dataBytes, "UTF-8");
                    flowObj = JSONUtils.parseJSONFromString(jsonString);
                }
                flows.add(flowObj);

            } catch (final IOException e) {
                throw new SQLException("Error retrieving flow data " + flowId, e);
            }
        } while (rs.next());

        return flows;
    }


    public static void projectFromObject(final Object object) {
        final Map<String, Object> projectObject = (Map<String, Object>) object;
        final int id = (Integer) projectObject.get("id");
        final String name = (String) projectObject.get("name");
        final String description = (String) projectObject.get("description");
        final String lastModifiedUser = (String) projectObject.get("lastModifiedUser");
        final long createTimestamp = coerceToLong(projectObject.get("createTimestamp"));
        final long lastModifiedTimestamp =
                coerceToLong(projectObject.get("lastModifiedTimestamp"));
        final String source = (String) projectObject.get("source");
        Boolean active = (Boolean) projectObject.get("active");
        active = active == null ? true : active;
        final int version = (Integer) projectObject.get("version");
        final Map<String, Object> metadata =
                (Map<String, Object>) projectObject.get("metadata");
    }

    public static void flowFromObject(final Object object) {
        final Map<String, Object> flowObject = (Map<String, Object>) object;

        final String id = (String) flowObject.get("id");
        final Boolean layedout = (Boolean) flowObject.get("layedout");
        final Boolean isEmbeddedFlow = (Boolean) flowObject.get("embeddedFlow");

        final int projId = (Integer) flowObject.get("project.id");

        final int version = (Integer) flowObject.get("version");

        // Loading projects
        final List<Object> propertiesList = (List<Object>) flowObject.get("props");

        // Loading nodes
        final List<Object> nodeList = (List<Object>) flowObject.get("nodes");

        // Loading edges
        final List<Object> edgeList = (List<Object>) flowObject.get("edges");

        final Map<String, Object> metadata =
                (Map<String, Object>) flowObject.get("metadata");


        List<String> sds = (List<String>) flowObject.get("failure.email");
        if (flowObject.containsKey("mailCreator")) {
            flowObject.get("mailCreator").toString();
        }
    }

    private static long coerceToLong(final Object obj) {
        if (obj == null) {
            return 0;
        } else if (obj instanceof Integer) {
            return (Integer) obj;
        }

        return (Long) obj;
    }
}
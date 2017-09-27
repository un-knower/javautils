package com.cenrise.utils.kylin;

import org.apache.commons.codec.binary.Base64;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

/**
 * Kylin Rest API对应接口
 * 基于配置的
 * cubes?cubeName
 */
public class KylinRESTfulMethod {

    private static String encoding;

    /**
     * 授权登陆
     *
     * @param user
     * @param passwd
     * @return
     */
    public static String login(String baseURL, String user, String passwd) {
        String method = "POST";
        String para = baseURL + "/user/authentication";
        byte[] key = (user + ":" + passwd).getBytes();
        encoding = Base64.encodeBase64String(key);
        return excute(para, method, null);
    }


    public static String listQueryableTables(String baseURL, String projectName) {
        String method = "GET";
        String para = baseURL + "/tables_and_columns?project=" + projectName;
        return excute(para, method, null);
    }


    /**
     * 显示所有Cube信息
     *
     * @param offset      required int Offset used by pagination
     * @param limit       required int Cubes per page.
     * @param cubeName    optional string Keyword for cube names. To find cubes whose name contains this keyword.
     * @param projectName optional string Project name.
     * @return
     */
    public static String listCubes(String baseURL, int offset,
                                   int limit,
                                   String cubeName,
                                   String projectName) {
        String method = "GET";
        String para = baseURL + "/cubes?offset=" + offset
                + "&limit=" + limit
                + "&cubeName=" + cubeName
                + "&projectName=" + projectName;
        return excute(para, method, null);
    }

    /**
     * 获取指定Cube描述
     *
     * @param cubeName Cube name.
     * @return
     */
    public static String getCubeDes(String baseURL, String cubeName) {
        String method = "GET";
        String para = baseURL + "/cube_desc/" + cubeName;
        return excute(para, method, null);
    }


    /**
     * 获取指定Cube
     *
     * @param cubeName
     * @return
     */
    public static String getCube(String baseURL, String cubeName) {
        String method = "GET";
        String para = baseURL + "/cubes/" + cubeName;
        return excute(para, method, null);
    }
 public static String getCubesByName(String baseURL,String body) {
        String method = "GET";
        //http://localhost:7070/kylin/api/cubes?cubeName=test_kylin_cube_with_slr&limit=15&offset=0
        String para = baseURL + "/cubes";
//        String para = baseURL + "/cubes?cubeName=170823_MPOS_OPERATION&limit=15&offset=0";
        return excute(para, method, body);
    }


    /**
     * 获取数据模型
     *
     * @param modelName Data model name, by default it should be the same with cube name.
     * @return
     */
    public static String getDataModel(String baseURL, String modelName) {
        String method = "GET";
        String para = baseURL + "/model/" + modelName;
        return excute(para, method, null);
    }

    /**
     * 把Cube设置为可用状态
     *
     * @param cubeName cubeName Cube name.
     * @return
     */
    public static String enableCube(String baseURL, String cubeName) {
        String method = "PUT";
        String para = baseURL + "/cubes/" + cubeName + "/enable";
        return excute(para, method, null);
    }

    /**
     * 把Cube设置为不可用状态
     *
     * @param cubeName Cube name.
     * @return
     */
    public static String disableCube(String baseURL, String cubeName) {
        String method = "PUT";
        String para = baseURL + "/cubes/" + cubeName + "/disable";
        return excute(para, method, null);
    }

    /**
     * 清除cube
     *
     * @param cubeName Cube name.
     * @return
     */
    public static String purgeCube(String baseURL, String cubeName) {
        String method = "PUT";
        String para = baseURL + "/cubes/" + cubeName + "/purge";
        return excute(para, method, null);
    }


    /**
     * 恢复任务
     *
     * @param jobId Job id
     * @return
     */
    public static String resumeJob(String baseURL, String jobId) {
        String method = "PUT";
        String para = baseURL + "/jobs/" + jobId + "/resume";
        return excute(para, method, null);
    }


    /**
     * 构建cube
     * startTime - required long Start timestamp of data to build, e.g. 1388563200000 for 2014-1-1
     * endTime - required long End timestamp of data to build
     * buildType - required string Supported build type: ‘BUILD’, ‘MERGE’, ‘REFRESH’
     *
     * @param cubeName Cube name.
     * @return
     */
    public static String buildCube(String baseURL, String cubeName, String body) {
        String method = "PUT";
        String para = baseURL + "/cubes/" + cubeName + "/rebuild";
        return excute(para, method, body);
    }


    /**
     * 取消任务
     *
     * @param jobId Job id.
     * @return
     */
    public static String discardJob(String baseURL, String jobId) {
        String method = "PUT";
        String para = baseURL + "/jobs/" + jobId + "/cancel";
        return excute(para, method, null);
    }

    /**
     * 获取任务状态
     *
     * @param jobId Job id.
     * @return
     */
    public static String getJobStatus(String baseURL, String jobId) {
        String method = "GET";
        String para = baseURL + "/jobs/" + jobId;
        return excute(para, method, null);
    }

    /**
     * 获取任务某一步的输出
     *
     * @param jobId  Job id.
     * @param stepId Step id; the step id is composed by jobId with step sequence id;
     *               for example, the jobId is “fb479e54-837f-49a2-b457-651fc50be110”, its 3rd step id
     *               is “fb479e54-837f-49a2-b457-651fc50be110-3”,
     * @return
     */
    public static String getJobStepOutput(String baseURL, String jobId, String stepId) {
        String method = "GET";
        String para = baseURL + "/" + jobId + "/steps/" + stepId + "/output";
        return excute(para, method, null);
    }

    /**
     * 获取Hive表
     *
     * @param tableName table name to find.
     * @return
     */
    public static String getHiveTable(String baseURL, String tableName) {
        String method = "GET";
        String para = baseURL + "/tables/" + tableName;
        return excute(para, method, null);
    }

    /**
     * 获取Hive表信息
     *
     * @param tableName table name to find.
     * @return
     */
    public static String getHiveTableInfo(String baseURL, String tableName) {
        String method = "GET";
        String para = baseURL + "/tables/" + tableName + "/exd-map";
        return excute(para, method, null);
    }


    /**
     * 获取Hive表列表
     *
     * @param projectName will list all tables in the project.
     * @param extOptional boolean set true to get extend info of table.
     * @return
     */
    public static String getHiveTables(String baseURL, String projectName, boolean extOptional) {
        String method = "GET";
        String para = baseURL + "/tables?project=" + projectName + "&ext=" + extOptional;
        return excute(para, method, null);
    }


    /**
     * 加载Hive表
     *
     * @param tables  table names you want to load from hive, separated with comma.
     * @param project the project which the tables will be loaded into.
     * @return
     */
    public static String loadHiveTables(String baseURL, String tables, String project) {
        String method = "POST";
        String para = baseURL + "/tables/" + tables + "/" + project;
        return excute(para, method, null);
    }

    /**
     * 清理缓存
     *
     * @param type   ‘METADATA’ or ‘CUBE’
     * @param name   Cache key, e.g the cube name.
     * @param action ‘create’, ‘update’ or ‘drop’
     * @return
     */
    public static String wipeCache(String baseURL, String type, String name, String action) {
        String method = "POST";
        String para = baseURL + "/cache/" + type + "/" + name + "/" + action;
        return excute(para, method, null);
    }

    /**
     * 查询
     *
     * @param body
     * @return
     */
    public static String query(String baseURL, String body) {
        String method = "POST";
        String para = baseURL + "/query";
        return excute(para, method, body);
    }

    /**
     * 执行cube
     *
     * @param para
     * @param method
     * @param body
     * @return
     */
    private static String excute(String para, String method, String body) {
        StringBuilder out = new StringBuilder();
        try {
            URL url = new URL(para);
            System.out.println("url:"+url+",body:"+body);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod(method);
            connection.setDoOutput(true);
            connection.setRequestProperty("Authorization", "Basic " + encoding);
            connection.setRequestProperty("Content-Type", "application/json");
            if (body != null) {
                byte[] outputInBytes = body.getBytes("UTF-8");
                OutputStream os = connection.getOutputStream();
                os.write(outputInBytes);
                os.close();
            }
            InputStream content = (InputStream) connection.getInputStream();
            BufferedReader in = new BufferedReader(new InputStreamReader(content));
            String line;
            while ((line = in.readLine()) != null) {
                out.append(line);
            }
            in.close();
            connection.disconnect();

        } catch (Exception e) {
//            XxlJobLogger.log("执行Cube异常：" + KylinUtil.CR + KylinUtil.getStackTracker(e));
            e.printStackTrace();
        }
        return out.toString();
    }

    public static String listProjects(String baseURL) {
        String method = "GET";
        String para = baseURL + "/projects";
        return excute(para, method, null);
    }

    public static String getModelByCubeName(String baseURL, String cubeName) {
        String method = "GET";
        String para = baseURL + "/cube_desc/" + cubeName;
        return excute(para, method, null);
    }


    /**
     * 时间戳转字符串
     *
     * @param timeMillis
     * @return yyyy-MM-dd HH:mm:ss
     */
    public static String timestampToString(long timeMillis) {
        Timestamp ts = new Timestamp(timeMillis);
        String tsStr = "";
        DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            tsStr = sdf.format(ts);
        } catch (Exception e) {
//            XxlJobLogger.log("timestamp转成String异常：" + KylinUtil.CR + KylinUtil.getStackTracker(e));
            e.printStackTrace();
        }
        return tsStr;
    }
}

package com.cenrise.test.azkaban;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.cenrise.utils.xml.sax.SaxService;
import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class AzkabanUtilLocal {
    private Logger logger = Logger.getLogger(AzkabanUtilLocal.class);
    Connection conn = null;
    PreparedStatement pre = null;
    ResultSet rs = null;
    //上下节点，临时数据
    JSONObject jsonbjectCurrentData;
    JSONObject jsonbjectNextData;

    public static void main(String[] args) throws Exception {
        AzkabanUtilLocal azkabanUtil = new AzkabanUtilLocal();
        try {
            azkabanUtil.conn = DBUtil.openConnection();

            List<Project> projectsList = azkabanUtil.fetchAllActiveProjects();
            for (Project project : projectsList) {
                int projectId = project.getId();
                int version = project.getVersion();
                //依赖关系
                List<Flow> flowList = azkabanUtil.queryProjectFlows(projectId, version);
                for (Flow flow : flowList) {
                    int projectId1 = flow.getProjectId();//6
                    //String flowId = flow.getId();//"end"
                    int version1 = flow.getVersion();//e

                    //获取到点到点的边信息queryProjectProperties
                    Collection<Edge> edges = flow.getEdges();
                    //查某个点的信息，如kettle文件路径
                    Collection<Node> nodes = flow.getNodes();
                    for (Edge edge : edges) {
                        String sourceId = edge.getSourceId();
                        String targetId = edge.getTargetId();

                        Connection connectionDPPro = DBUtilDPPro.openConnection();
                        String sql = "INSERT INTO debug_azkaban_edge(id,source_name,target_name) VALUES (UUID(),'" + sourceId + "','" + targetId + "')";
                        PreparedStatement preparedStatement = connectionDPPro.prepareStatement(sql);
                        preparedStatement.execute();
                        DBUtilDPPro.closeConnection();

                        Node nodeSource = queryNode(nodes, sourceId);
                        Node nodeTarget = queryNode(nodes, targetId);

                        //处理源和目的节点
                        azkabanUtil.process(projectId1, version1, nodeSource, azkabanUtil);
                        azkabanUtil.process(projectId1, version1, nodeTarget, azkabanUtil);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * 处理azkaban的一个节点
     *
     * @param projectId1
     * @param version1
     * @param node
     * @param azkabanUtil
     * @throws Exception
     */
    private void process(int projectId1, int version1, Node node, AzkabanUtilLocal azkabanUtil) throws Exception {

        //String id = node.getId();//SYCH_RPT_TABLES
        String jobSource = node.getJobSource();//SYCH_RPT_TABLES.job
        // String type = node.getType();//command
        // int level = node.getLevel();//第几级 3

        //可读取到上传的转换信息，及依赖
        Map<String, Props> propsStrMap = azkabanUtil.queryProjectProperties(projectId1, version1, jobSource);
        for (Map.Entry<String, Props> propsMap : propsStrMap.entrySet()) {
            // String propsMapKey = propsMap.getKey();//SYCH_RPT_TABLES.job
            Props propsMapValue = propsMap.getValue();
            // String type1 = propsMapValue.get("type");//command
            String command = propsMapValue.get("command");//ss /home/appgroup/kettle/pdi-ce-5.0.1........kjb
            // String dependencies = propsMapValue.get("dependencies");//sychend

            String[] splitArray = command.split(" ");
            String filePath = null;
            if (splitArray.length >= 3) {
                filePath = command.split(" ")[3];
            } else {
                //直接记录这个azkaban节点，不用继续
                continue;
            }

            //路径转换为本地
            filePath = transPath(filePath);

            File file = new File(filePath);
            processOneFile(file, azkabanUtil);

        }

    }

    /**
     * 把远程目录转变为本地目录
     *
     * @param filePath
     * @return
     */
    private String transPath(String filePath) {
        String realSftpDirString = "/home/appgroup/kettle/pdi-ce-5.0.1.A-stable/data-integration/MyKtrs";
        String realSftpDirStringLocal = "/Users/jiadongpo/Documents/VbillRepo_20171225";
        return filePath.replace(realSftpDirString, realSftpDirStringLocal);
    }

    /**
     * 解析一个文件，ktr或kjb文件，抽象用于递归处理
     *
     * @param file
     * @param azkabanUtil
     * @throws Exception
     */
    private void processOneFile(File file, AzkabanUtilLocal azkabanUtil) throws Exception {
        String parentPath = file.getParent();
        String fileName = file.getName();
        System.out.println("处理一个文件processOneFile[" + file.getAbsolutePath() + "]");
        //登陆ftp，查找文件
        File fileEle = new File(parentPath + "/" + fileName);

        if (fileEle == null || !fileEle.exists()) {
            return;
        }

        System.out.println("当前正在处理的文件为：" + fileEle.getAbsolutePath());

        //策略是只读sql执行器组件和存储过程，其它的只记录转换名

        //读取指定文件中的指定节点
        ArrayList<Map<String, String>> entryList = (ArrayList<Map<String, String>>) SaxService.ReadXML(fileEle.getAbsolutePath(), "entry");
        ArrayList<Map<String, String>> hopList = (ArrayList<Map<String, String>>) SaxService.ReadXML(fileEle.getAbsolutePath(), "hop");
        ArrayList<Map<String, String>> stepList = (ArrayList<Map<String, String>>) SaxService.ReadXML(fileEle.getAbsolutePath(), "step");

        //kettle文件内部的顺序关系
        for (Map<String, String> hopMap : hopList) {
            String from = hopMap.get("from");
            String to = hopMap.get("to");
            Map<String, String> entryMapFrom = azkabanUtil.queryEntry(entryList, from);
            Map<String, String> entryMapTo = azkabanUtil.queryEntry(entryList, to);
            //这里考虑到需要记录上下节点，且必须为转换时记录，所以采用记录上下节点的方式，如果上节点不为空，添加到下节点，然后插入数据后，清空节点。
            azkabanUtil.exeKettle(entryMapFrom, stepList, fileEle, azkabanUtil);
            azkabanUtil.exeKettle(entryMapTo, stepList, fileEle, azkabanUtil);
        }

        if (fileEle != null && fileEle.exists()) {
            fileEle.delete();
        }

    }


    /**
     * 通过边查到源点和目标点的节点
     *
     * @param nodes
     * @param targetId
     * @return
     */
    private static Node queryNode(Collection<Node> nodes, String targetId) {
        if (targetId == null) {
            //TODO 导常
            return null;
        }
        for (Node node : nodes) {
            String nodeName = node.getId();//SYCH_RPT_TABLES
            if (targetId.equals(nodeName)) {
                return node;
            }
        }
        return null;
    }

    /**
     * 处理kettle文件的上下节点
     *
     * @param entryMap
     * @param stepList
     */
    public void exeKettle(Map<String, String> entryMap, ArrayList<Map<String, String>> stepList, File fileEle, AzkabanUtilLocal azkabanUtil) throws Exception {

        //开始，转换类型
        String fileType = entryMap.get("type");
        if (fileType.equals("SPECIAL")) {
            //特殊类型 TODO 直接记录点
            JSONObject jsonObject = kettleNode("SPECIAL", null, null, null, null);
            placeNode(jsonObject);
        } else if (fileType.equals("DBProc")) {
            //执行存储过程组件没有transname和directory
            String name = entryMap.get("name");//用DB存储过程
            String connection = entryMap.get("connection");//163生产查询
            String procedure = entryMap.get("procedure");//proc_miantableName_PrevNext
            JSONObject jsonObject = kettleNode(name, null, null, procedure, connection);
            placeNode(jsonObject);

        } else if (fileType.equals("ExecSQL")) {
            //sql执行器
            String name = entryMap.get("name");//执行SQL脚本
            //String type = entryMap.get("type");//ExecSQL
            String connection = entryMap.get("connection");//生产123
            String sql = entryMap.get("sql");//select * from dual
            Set<String> setStr = new HashSet<String>();
            setStr.add(sql);
            JSONObject jsonObject = kettleNode(name, connection, setStr, null, null);
            placeNode(jsonObject);
        } else if (fileType.equals("TRANS")) {
            //TODO 如果是转换，读取转换文件
            //<transname>存储过程执行</transname>
            //<directory>/执行顺序</directory>
            String transname = entryMap.get("transname");
            String directory = entryMap.get("directory");
            if (transname == null || directory == null) {
                return;
            }
            String parent = fileEle.getParent();
            //转换一下
            directory.replace("${Internal.Job.Filename.Directory}","");
            String dir = parent + "/" + directory;

            //根据文件名和路径获取文件
            File fileKtr = new File(dir + "/" + transname + ".ktr");
            processOneFile(fileKtr, azkabanUtil);
        } else if (fileType.equals("JOB")) {
            //TODO 如果是任务，读取job文件
            //<jobname>job_sub_file_1</jobname>
            //<directory>/执行顺序</directory>
            String jobname = entryMap.get("jobname");
            String directory = entryMap.get("directory");
            if (jobname == null || directory == null) {
                return;
            }
            //${Internal.Job.Filename.Directory}/Totalamount.kjb
            String parent = fileEle.getParent();
            String dir = parent + "/" + directory;

            //根据文件名和路径获取文件
            File fileKtr = new File(dir + "/" + jobname + ".kjb");
            processOneFile(fileKtr, azkabanUtil);
        } else {
            //应该是各类转换
            String name = entryMap.get("name");
            JSONObject jsonObject = kettleNode(name, null, null, null, null);
            placeNode(jsonObject);
        }
    }

    /**
     * 处理节点逻辑，上下节点，先放前边的节点，再放后面的，放后面的节点的同时即表示两点一线，保存清空
     *
     * @param jsonObject
     * @throws SQLException
     */
    private void placeNode(JSONObject jsonObject) throws SQLException {
        if (jsonbjectCurrentData == null) {
            jsonbjectCurrentData = jsonObject;
        } else if (jsonbjectNextData == null) {
            jsonbjectNextData = jsonObject;
            //持久化到数据库
            saveToDB(jsonbjectCurrentData, jsonbjectNextData);
            //清空
            jsonbjectCurrentData = null;
            jsonbjectNextData = null;
        }
    }

    /**
     * 持久化kettle的文件信息
     *
     * @param jsonbjectCurrentData
     * @param jsonbjectNextData
     */
    private void saveToDB(JSONObject jsonbjectCurrentData, JSONObject jsonbjectNextData) throws SQLException {
        StringBuilder sqlKey = new StringBuilder();
        StringBuilder sqlValue = new StringBuilder();

        Object nameCurrent = jsonbjectCurrentData.get("name");
        Object tableConnectCurrent = jsonbjectCurrentData.get("table_connect");
        Object tableNamesCurrent = jsonbjectCurrentData.get("table_names");
        Object procConnCurrent = jsonbjectCurrentData.get("proc_conn");
        Object procNameCurrent = jsonbjectCurrentData.get("proc_name");

        if (nameCurrent != null) {
            sqlKey.append("source_name");
            sqlValue.append("'" + nameCurrent + "'");
        }

        if (tableConnectCurrent != null) {
            if (sqlKey != null && sqlValue != null) {
                sqlKey.append(",");
                sqlValue.append(",");
            }
            sqlKey.append("source_table_conn");
            sqlValue.append("'" + tableConnectCurrent + "'");
        }

        if (tableNamesCurrent != null) {
            if (sqlKey != null && sqlValue != null) {
                sqlKey.append(",");
                sqlValue.append(",");
            }

            JSONArray jsonArray = JSONArray.parseArray(String.valueOf(tableNamesCurrent));
            sqlKey.append("source_table_tables");
            sqlValue.append("'" + jsonArray.toJSONString() + "'");
        }

        if (procConnCurrent != null) {
            if (sqlKey != null && sqlValue != null) {
                sqlKey.append(",");
                sqlValue.append(",");
            }
            sqlKey.append("source_proc_conn");
            sqlValue.append("'" + procConnCurrent + "'");
        }

        if (procNameCurrent != null) {
            if (sqlKey != null && sqlValue != null) {
                sqlKey.append(",");
                sqlValue.append(",");
            }

            sqlKey.append("source_proc_name");
            sqlValue.append("'" + procNameCurrent + "'");
        }

        Object nameNext = jsonbjectNextData.get("name");
        Object tableConnectNext = jsonbjectNextData.get("table_connect");
        Object tableNamesNext = jsonbjectNextData.get("table_names");
        Object procConnNext = jsonbjectNextData.get("proc_conn");
        Object procNameNext = jsonbjectNextData.get("proc_name");

        if (nameNext != null) {
            if (sqlKey != null && sqlValue != null) {
                sqlKey.append(",");
                sqlValue.append(",");
            }
            sqlKey.append("target_name");
            sqlValue.append("'" + nameNext + "'");
        }

        if (tableConnectNext != null) {
            if (sqlKey != null && sqlValue != null) {
                sqlKey.append(",");
                sqlValue.append(",");
            }
            sqlKey.append("target_table_conn");
            sqlValue.append("'" + tableConnectNext + "'");
        }

        if (tableNamesNext != null) {
            if (sqlKey != null && sqlValue != null) {
                sqlKey.append(",");
                sqlValue.append(",");
            }

            JSONArray jsonArray = JSONArray.parseArray(String.valueOf(tableNamesNext));
            sqlKey.append("target_table_tables");
            sqlValue.append("'" + jsonArray.toJSONString() + "'");
        }

        if (procConnNext != null) {
            if (sqlKey != null && sqlValue != null) {
                sqlKey.append(",");
                sqlValue.append(",");
            }

            sqlKey.append("target_proc_conn");
            sqlValue.append("'" + procConnNext + "'");
        }

        if (procNameNext != null) {
            if (sqlKey != null && sqlValue != null) {
                sqlKey.append(",");
                sqlValue.append(",");
            }

            sqlKey.append("target_proc_name");
            sqlValue.append("'" + procNameNext + "'");
        }

        if (sqlKey != null && sqlValue != null) {
            StringBuilder sql = new StringBuilder();
            sql.append("INSERT INTO debug_kettle (");
            sql.append("id,");
            sql.append(sqlKey);
            sql.append(") VALUES (");
            sql.append("UUID(),");
            sql.append(sqlValue);
            sql.append(")");

            Connection connectionDPPro = DBUtilDPPro.openConnection();
            PreparedStatement preparedStatement = connectionDPPro.prepareStatement(sql.toString());
            preparedStatement.executeUpdate();
            DBUtilDPPro.closeConnection();

        }


    }

    public static String SELECT_ALL_ACTIVE_PROJECTS =
            "SELECT id, name, active, modified_time, create_time, version, last_modified_by, description, enc_type, settings_blob FROM projects WHERE active=true";
    public static String SELECT_ACTIVE_PROJECT_BY_NAME =
            "SELECT id, name, active, modified_time, create_time, version, last_modified_by, description, enc_type, settings_blob FROM projects WHERE name=? AND active=true";

    /**
     * 获取所有活跃的项目
     *
     * @return
     * @throws Exception
     */
    public List<Project> fetchAllActiveProjects() throws Exception {
        List<Project> objectsProject = null;
        //取项目
        try {
            pre = conn.prepareStatement(SELECT_ALL_ACTIVE_PROJECTS);
            rs = pre.executeQuery();
            //项目级别
            JdbcProjectHandlerSet.ProjectResultHandler projectResultHandler = new JdbcProjectHandlerSet.ProjectResultHandler();
            objectsProject = projectResultHandler.handle(rs);

//            System.out.println(objectsProject.size());
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return objectsProject;
    }

    /**
     * 查询项目信息
     *
     * @param project_id
     * @return
     */
    public List<Project> queryProjects(Integer project_id) {
        List<Project> objectsProject = null;
        //取项目
        try {
            pre = conn.prepareStatement("SELECT  id, name, active, modified_time, create_time, version, last_modified_by, description, enc_type, settings_blob FROM projects WHERE id=" + project_id);
            rs = pre.executeQuery();
            //项目级别
            JdbcProjectHandlerSet.ProjectResultHandler projectResultHandler = new JdbcProjectHandlerSet.ProjectResultHandler();
            objectsProject = projectResultHandler.handle(rs);

//            System.out.println(objectsProject.size());
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return objectsProject;

    }

    /**
     * 获取project_properties信息
     *
     * @return
     */
    public Map<String, Props> queryProjectProperties(Integer projectId, Integer version) throws Exception {
        try {
            JdbcProjectHandlerSet.ProjectPropertiesResultsHandler projectPropertiesResultsHandler = new JdbcProjectHandlerSet.ProjectPropertiesResultsHandler();

            pre = conn.prepareStatement("SELECT project_id, version, name, modified_time, encoding_type, property FROM project_properties WHERE project_id=" + projectId + " AND version=" + version);
            rs = pre.executeQuery();
            List<Pair<String, Props>> properties = projectPropertiesResultsHandler.handle(rs);


            if (properties == null || properties.isEmpty()) {
                return null;
            }
            final HashMap<String, Props> props = new HashMap<>();
            for (final Pair<String, Props> pair : properties) {
                props.put(pair.getFirst(), pair.getSecond());
            }
            return props;
        } catch (final SQLException e) {
            logger.error("Error fetching properties, project id" + projectId + " version " + version, e);
            throw new Exception("Error fetching properties", e);
        }

    }

    /**
     * 获取project_properties信息
     *
     * @return
     */
    public Map<String, Props> queryProjectProperties(Integer projectId, Integer version, String name) throws Exception {
        try {
            JdbcProjectHandlerSet.ProjectPropertiesResultsHandler projectPropertiesResultsHandler = new JdbcProjectHandlerSet.ProjectPropertiesResultsHandler();

            pre = conn.prepareStatement("SELECT project_id, version, name, modified_time, encoding_type, property FROM project_properties WHERE project_id=" + projectId + " AND version=" + version + " AND name='" + name + "'");
            rs = pre.executeQuery();
            List<Pair<String, Props>> properties = projectPropertiesResultsHandler.handle(rs);


            if (properties == null || properties.isEmpty()) {
                return null;
            }
            final HashMap<String, Props> props = new HashMap<>();
            for (final Pair<String, Props> pair : properties) {
                props.put(pair.getFirst(), pair.getSecond());
            }
            return props;
        } catch (final SQLException e) {
            logger.error("Error fetching properties, project id" + projectId + " version " + version, e);
            throw new Exception("Error fetching properties", e);
        }

    }

    public List<Flow> queryProjectFlows(Integer project_id, Integer version) {
        List<Flow> objectsFlows = null;
        try {
            pre = conn.prepareStatement("SELECT project_id, version, flow_id, modified_time, encoding_type, json FROM project_flows WHERE project_id=" + project_id + " AND version=" + version);
            rs = pre.executeQuery();
            JdbcProjectHandlerSet.ProjectFlowsResultHandler projectFlowsResultHandler = new JdbcProjectHandlerSet.ProjectFlowsResultHandler();

            objectsFlows = projectFlowsResultHandler.handle(rs);
//            System.out.println(objectsFlows.size());
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return objectsFlows;
    }

    public List<Flow> queryProjectFlows(Integer project_id, Integer version, String flow_id) {
        List<Flow> objectsFlows = null;
        try {
            pre = conn.prepareStatement("SELECT project_id, version, flow_id, modified_time, encoding_type, json FROM project_flows WHERE project_id=" + project_id + " AND version=" + version + " AND flow_id=" + "'" + flow_id + "'");
            rs = pre.executeQuery();
            JdbcProjectHandlerSet.ProjectFlowsResultHandler projectFlowsResultHandler = new JdbcProjectHandlerSet.ProjectFlowsResultHandler();

            objectsFlows = projectFlowsResultHandler.handle(rs);
//            System.out.println(objectsFlows.size());
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return objectsFlows;
    }

    File tempDir;

    /**
     * 查询project_files
     *
     * @param project_id
     * @param version
     * @return
     * @throws Exception
     */
    public File queryProjectFiles(Integer project_id, Integer version) throws Exception {

        this.tempDir = new File(this.getClass().getResource("/").getPath());

        final int collect = 5;
        int fromChunk = 0;
        int toChunk = collect;


        BufferedOutputStream bStream = null;

        final ProjectFileHandler projHandler = fetchProjectMetaData(project_id, version);
        if (projHandler == null) {
            return null;
        }
        final int numChunks = projHandler.getNumChunks();
        File file = null;
        try {

            try {
                file = File
                        .createTempFile(projHandler.getFileName(), String.valueOf(version), this.tempDir);
                bStream = new BufferedOutputStream(new FileOutputStream(file));
            } catch (final IOException e) {
                throw new Exception("Error creating temp file for stream.");
            }

            try {
                do {

                    pre = conn.prepareStatement("SELECT project_id, version, chunk, size, file FROM project_files WHERE project_id=" + project_id + " AND version=" + version + " AND chunk >= " + fromChunk + " AND chunk < " + toChunk + " ORDER BY chunk ASC");
                    rs = pre.executeQuery();
                    JdbcProjectHandlerSet.ProjectFileChunkResultHandler projectFileChunkResultHandler = new JdbcProjectHandlerSet.ProjectFileChunkResultHandler();
                    List<byte[]> data = projectFileChunkResultHandler.handle(rs);

                    try {
                        for (final byte[] d : data) {
                            bStream.write(d);
                        }
                    } catch (final IOException e) {
                        throw new Exception("Error writing file", e);
                    }

                    // Add all the bytes to the stream.
                    fromChunk += collect;
                    toChunk += collect;

                } while (fromChunk <= numChunks);

            } finally {
                IOUtils.closeQuietly(bStream);
            }

            // Check md5.
            byte[] md5 = null;
            try {
                md5 = Md5Hasher.md5Hash(file);
            } catch (final IOException e) {
                throw new Exception("Error getting md5 hash.", e);
            }

            if (Arrays.equals(projHandler.getMd5Hash(), md5)) {
                logger.info("Md5 Hash is valid");
            } else {
                throw new Exception("Md5 Hash failed on retrieval of file");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return file;
    }


    public ProjectFileHandler fetchProjectMetaData(final int projectId, final int version) throws Exception {

        try {
            String SELECT_PROJECT_VERSION =
                    "SELECT project_id, version, upload_time, uploader, file_type, file_name, md5, num_chunks "
                            + "FROM project_versions WHERE project_id=" + projectId + " AND version=" + version;

            pre = conn.prepareStatement(SELECT_PROJECT_VERSION);
            rs = pre.executeQuery();
            JdbcProjectHandlerSet.ProjectVersionResultHandler projectVersionResultHandler = new JdbcProjectHandlerSet.ProjectVersionResultHandler();
            List<ProjectFileHandler> projectFiles = projectVersionResultHandler.handle(rs);
            if (projectFiles == null || projectFiles.isEmpty()) {
                return null;
            }
            return projectFiles.get(0);
        } catch (final SQLException ex) {
            logger.error("Query for uploaded file for project id " + projectId + " failed.", ex);
            throw new Exception(
                    "Query for uploaded file for project id " + projectId + " failed.", ex);
        }


    }


    /**
     * 读取kettle时，上下节点的记录
     *
     * @param name         转换的名字
     * @param tableNameSet 如果是sql执行器，里边涉及的表集体
     * @param procName     如果是存储过程，记录存储过程的名字
     * @param connectName  数据库连接名称
     * @return
     */
    public static JSONObject kettleNode(String name, String tableConnect, Set<String> tableNameSet, String procName, String connectName) {
        if (name == null) {
            //TODO 错误
        }
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("name", name);

        if (procName != null) {
            jsonObject.put("proc_name", procName);
        }

        if (connectName != null) {
            jsonObject.put("proc_conn", connectName);
        }

        if (tableConnect != null) {
            jsonObject.put("table_connect", tableConnect);
        }

        if (tableNameSet != null) {
            JSONArray jsonArray = new JSONArray();
            for (String str : tableNameSet) {
                jsonArray.add(str);
            }
            if (jsonArray != null && jsonArray.size() != 0) {
                jsonObject.put("table_names", jsonArray);
            }
        }

        return jsonObject;
    }


    public Map<String, String> queryEntry(ArrayList<Map<String, String>> entryList, String entryName) {
        for (Map<String, String> entryMap : entryList) {
            String name = entryMap.get("name");
            if (name.equals(entryName)) {
                return entryMap;
            }
        }
        return null;
    }

}

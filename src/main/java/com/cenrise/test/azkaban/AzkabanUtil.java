package com.cenrise.test.azkaban;

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
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AzkabanUtil {
    private Logger logger = Logger.getLogger(AzkabanUtil.class);
    Connection conn = null;
    PreparedStatement pre = null;
    ResultSet rs = null;

    public static void main(String[] args) {
        AzkabanUtil azkabanUtil = new AzkabanUtil();
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
                    String flowId = flow.getId();//"end"
                    int version1 = flow.getVersion();//e
                    //获取到点到点的边信息queryProjectProperties
                    Collection<Edge> edges = flow.getEdges();
                    for (Edge edge : edges) {
                        String sourceId = edge.getSourceId();
                        String targetId = edge.getTargetId();
                    }
                    //查某个点的信息，如kettle文件路径
                    Collection<Node> nodes = flow.getNodes();
                    for (Node node : nodes) {
                        String id = node.getId();
                        String jobSource = node.getJobSource();//SYCH_RPT_TABLES.job
                        String type = node.getType();//command
                        int level = node.getLevel();//第几级
                        //可读取到上传的转换信息，及依赖
                        Map<String, Props> propsStrMap = azkabanUtil.queryProjectProperties(projectId1, version1, jobSource);
                        for (Map.Entry<String, Props> propsMap : propsStrMap.entrySet()) {
                            String propsMapKey = propsMap.getKey();
                            Props propsMapValue = propsMap.getValue();
                            String type1 = propsMapValue.get("type");//command
                            String command = propsMapValue.get("command");//ss /home/appgroup/kettle/pdi-ce-5.0.1........kjb
                            String dependencies = propsMapValue.get("dependencies");//sychend

                            //TODO 通过linux命令读取kettle文件，command

                        }
                        System.out.println(propsStrMap.size());
                    }
                    //可拿到上传的文件
                    File file = azkabanUtil.queryProjectFiles(projectId1, version1);
                    System.out.println(file.getAbsolutePath());


                }
            }


        } catch (SQLException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
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

            System.out.println(objectsProject.size());
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

            System.out.println(objectsProject.size());
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            /*try {
                DBUtil.closeConnection();
            } catch (SQLException e) {
                e.printStackTrace();
            }*/
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
            System.out.println(objectsFlows.size());
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            /*try {
                DBUtil.closeConnection();
            } catch (SQLException e) {
                e.printStackTrace();
            }*/
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
            System.out.println(objectsFlows.size());
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            /*try {
                DBUtil.closeConnection();
            } catch (SQLException e) {
                e.printStackTrace();
            }*/
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

}

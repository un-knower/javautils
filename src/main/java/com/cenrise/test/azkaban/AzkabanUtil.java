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

            List<Project> projectsList = azkabanUtil.queryProjects(6);

            //依赖关系
            List<Flow> flowList = azkabanUtil.queryProjectFlows(6, 3, "end");

            //可拿到上传的文件
            File file = azkabanUtil.queryProjectFiles(6, 3);

            //可读取到上传的转换信息，及依赖
            Map<String, Props> propsStrMap = azkabanUtil.queryProjectProperties(6, 3);
            System.out.println(propsStrMap.size());

        } catch (SQLException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
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

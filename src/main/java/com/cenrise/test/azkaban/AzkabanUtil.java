package com.cenrise.test.azkaban;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class AzkabanUtil {

    public static void main(String[] args) {
        try {
            Connection conncection = DBUtil.openConnection();
            Connection conn = null;
            PreparedStatement pre = null;
            ResultSet rs = null;

            pre = conncection.prepareStatement("SELECT  id, name, active, modified_time, create_time, version, last_modified_by, description, enc_type, settings_blob FROM projects WHERE id=6");
            rs = pre.executeQuery();
            //项目级别
            JdbcProjectHandlerSet.ProjectResultHandler projectResultHandler = new JdbcProjectHandlerSet.ProjectResultHandler();
            List<Project> objectsProject = projectResultHandler.handle(rs);

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
}

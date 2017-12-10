package com.cenrise.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by ThinkPad on 2017/6/28.
 */
public class FastJsonUtil {

    public static void main(String[] args) {
       /* String json = "{\"task_name\":\"完成数据分发\",\"datasource_name\":\"mysql_101\",\"recipient_lists \":[\"jiadp@cenrise.com\",\"zahngwe@qq.com\",\"qiyi@qq.com\"],\"cc_lists\":[\"jiadp@cenrise.com\",\"zahngwe@qq.com\",\"qiyi@qq.com\"],\"subtasks\":[{\"subtask_name\":\"查询信息表\",\"sql\":\"select * from dual;\"},{\"subtask_name\":\"查询信息表\",\"sql\":\"select * from dual;\"}]}";

        JSONObject jso = JSON.parseObject(json);
        System.out.println("初始jsonObject:\n" + jso + "\n");

        JSONArray jsonArray = jso.getJSONArray("subtasks");
        System.out.println("取数组:\n" + jsonArray + "\n");

        String jsonStr = jsonArray.toJSONString();//JSONArray转化json字符串
        System.out.println("字符串：" + jsonStr);

        JSONObject ao = jsonArray.getJSONObject(0);
        System.out.println("取数组中第一个元素：\n" + ao + "\n");

        String vString = ao.getString("subtask_name");
        System.out.println("取数组中第一个元素属性的值：\n" + vString + "\n");

        System.out.println("测试开始！");*/
//        String kettleStr = "[{nr=0, minutes=0, DayOfMonth=1, start=Y, intervalMinutes=60, schedulerType=0, draw=Y, type=SPECIAL, dummy=N, xloc=192, hour=12, parallel=N, repeat=N, weekDay=1, name=START, intervalSeconds=0, yloc=192}, {cluster=N, add_date=N, nr=0, logging_remote_work=N, type=TRANS, directory=${Internal.Job.Filename.Directory}, set_logfile=N, specification_method=rep_name, params_from_previous=N, parallel=N, clear_rows=N, run_configuration=Pentaho local, exec_per_row=N, arg_from_previous=N, yloc=192, follow_abort_remote=N, wait_until_finished=Y, create_parent_folder=N, draw=Y, xloc=384, transname=STARTDATE.ktr, clear_files=N, loglevel=Basic, name=转换, set_append_logfile=N, add_time=N, pass_all_parameters=Y}, {follow_abort_remote=N, pass_export=N, add_date=N, nr=0, wait_until_finished=Y, create_parent_folder=N, draw=Y, type=JOB, set_logfile=N, specification_method=filename, xloc=384, params_from_previous=N, filename=${Internal.Job.Filename.Directory}/Totalamount.kjb, parallel=N, loglevel=Nothing, name=ssp-7, set_append_logfile=N, exec_per_row=N, expand_remote_job=N, arg_from_previous=N, add_time=N, pass_all_parameters=Y, yloc=336}]";
        String kettleStr = "{nr=0, minutes=0, DayOfMonth=1, start=Y, intervalMinutes=60, schedulerType=0, draw=Y, type=SPECIAL, dummy=N, xloc=192, hour=12, parallel=N, repeat=N, weekDay=1, name=START, intervalSeconds=0, yloc=192}, {cluster=N, add_date=N, nr=0, logging_remote_work=N, type=TRANS, directory=${Internal.Job.Filename.Directory}, set_logfile=N, specification_method=rep_name, params_from_previous=N, parallel=N, clear_rows=N, run_configuration=Pentaho local, exec_per_row=N, arg_from_previous=N, yloc=192, follow_abort_remote=N, wait_until_finished=Y, create_parent_folder=N, draw=Y, xloc=384, transname=STARTDATE.ktr, clear_files=N, loglevel=Basic, name=转换, set_append_logfile=N, add_time=N, pass_all_parameters=Y}, {follow_abort_remote=N, pass_export=N, add_date=N, nr=0, wait_until_finished=Y, create_parent_folder=N, draw=Y, type=JOB, set_logfile=N, specification_method=filename, xloc=384, params_from_previous=N, filename=${Internal.Job.Filename.Directory}/Totalamount.kjb, parallel=N, loglevel=Nothing, name=ssp-7, set_append_logfile=N, exec_per_row=N, expand_remote_job=N, arg_from_previous=N, add_time=N, pass_all_parameters=Y, yloc=336}";
//        String kettleStr = "[{cluster=N, pass_export=N, add_date=N, nr=0, logging_remote_work=N, intervalMinutes=60, type=JOB, directory=${Internal.Job.Filename.Directory}, set_logfile=N, specification_method=filename, dummy=N, params_from_previous=N, hour=12, parallel=N, repeat=N, weekDay=1, clear_rows=N, run_configuration=Pentaho local, exec_per_row=N, expand_remote_job=N, arg_from_previous=N, yloc=336, follow_abort_remote=N, minutes=0, wait_until_finished=Y, DayOfMonth=1, start=Y, create_parent_folder=N, schedulerType=0, draw=Y, xloc=384, transname=STARTDATE.ktr, filename=${Internal.Job.Filename.Directory}/Totalamount.kjb, clear_files=N, loglevel=Nothing, name=ssp-7, set_append_logfile=N, add_time=N, pass_all_parameters=Y, intervalSeconds=0}]";
//        String kettleStr = "{cluster=N, pass_export=N, add_date=N, nr=0, logging_remote_work=N, intervalMinutes=60, type=JOB, directory=${Internal.Job.Filename.Directory}, set_logfile=N, specification_method=filename, dummy=N, params_from_previous=N, hour=12, parallel=N, repeat=N, weekDay=1, clear_rows=N, run_configuration=Pentaho local, exec_per_row=N, expand_remote_job=N, arg_from_previous=N, yloc=336, follow_abort_remote=N, minutes=0, wait_until_finished=Y, DayOfMonth=1, start=Y, create_parent_folder=N, schedulerType=0, draw=Y, xloc=384, transname=STARTDATE.ktr, filename=${Internal.Job.Filename.Directory}/Totalamount.kjb, clear_files=N, loglevel=Nothing, name=ssp-7, set_append_logfile=N, add_time=N, pass_all_parameters=Y, intervalSeconds=0}";
        JSONArray kettleStrJsonArray = JSONArray.parseArray(kettleStr);
//        JSONObject kettleStrJsonObject = JSON.parseObject(kettleStr);
        System.out.println( kettleStrJsonArray.size() );

    }

    /**
     * JSONObject转为map
     *
     * @param object json对象
     * @return 转化后的Map
     */
    public static Map<String, Object> toMap(JSONObject object) {
        Map<String, Object> map = new HashMap<String, Object>();

        for (String key : object.keySet()) {
            Object value = object.get(key);
            if (value instanceof JSONArray) {
                value = toList((JSONArray) value);
            } else if (value instanceof JSONObject) {
                value = toMap((JSONObject) value);
            }
            map.put(key, value);
        }

        return map;
    }

    /**
     * JSONArray转为List
     *
     * @param array json数组
     * @return 转化后的List
     */
    public static List<Object> toList(JSONArray array) {
        List<Object> list = new ArrayList<Object>();
        for (int i = 0; i < array.size(); i++) {
            Object value = array.get(i);
            if (value instanceof JSONArray) {
                value = toList((JSONArray) value);
            } else if (value instanceof JSONObject) {
                value = toMap((JSONObject) value);
            }
            list.add(value);
        }
        return list;
    }
}

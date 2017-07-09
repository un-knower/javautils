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
        String json = "{\"task_name\":\"完成数据分发\",\"datasource_name\":\"mysql_101\",\"recipient_lists \":[\"jiadp@cenrise.com\",\"zahngwe@qq.com\",\"qiyi@qq.com\"],\"cc_lists\":[\"jiadp@cenrise.com\",\"zahngwe@qq.com\",\"qiyi@qq.com\"],\"subtasks\":[{\"subtask_name\":\"查询信息表\",\"sql\":\"select * from dual;\"},{\"subtask_name\":\"查询信息表\",\"sql\":\"select * from dual;\"}]}";

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

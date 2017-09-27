package com.cenrise.utils.Collection;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by ThinkPad on 2017/7/7.
 */
public class MapUtilTest {
    public static void main(String[] args) {
        Map<Integer, String> map = new HashMap<Integer, String>();
        map.put(1, "a");
        map.put(2, "b");
        map.put(3, "ab");
        map.put(4, "ab");
        map.put(4, "ab");// 和上面相同 ， 会自己筛选
        System.out.println(map.size());
        // 第一种：
        System.out.println("第一种：通过Map.keySet遍历key和value：");
        for (Integer in : map.keySet()) {
            //map.keySet()返回的是所有key的值
            String str = map.get(in);//得到每个key多对用value的值
            System.out.println(in + "     " + str);
        }
        // 第二种：
        System.out.println("第二种：通过Map.entrySet使用iterator遍历key和value：");
        Iterator<Map.Entry<Integer, String>> it = map.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<Integer, String> entry = it.next();
            System.out.println("key= " + entry.getKey() + " and value= " + entry.getValue());
        }
        // 第三种：推荐，尤其是容量大时
        System.out.println("第三种：通过Map.entrySet遍历key和value");
        for (Map.Entry<Integer, String> entry : map.entrySet()) {
            //Map.entry<Integer,String> 映射项（键-值对）  有几个方法：用上面的名字entry
            //entry.getKey() ;entry.getValue(); entry.setValue();
            //map.entrySet()  返回此映射中包含的映射关系的 Set视图。
            System.out.println("key= " + entry.getKey() + " and value= "
                    + entry.getValue());
        }
        // 第四种：
        System.out.println("第四种：通过Map.values()遍历所有的value，但不能遍历key");
        for (String v : map.values()) {
            System.out.println("value= " + v);
        }
        Map<Integer, Integer> maps = new HashMap<Integer, Integer>();
        maps.put(1, 8);
        maps.put(3, 12);
        maps.put(5, 53);
        maps.put(123, 33);
        maps.put(42, 11);
        maps.put(44, 42);
        maps.put(15, 3);
        System.out.println(MapUtilTest.getMaxKey(maps));
        System.out.println(MapUtilTest.getMaxValue(maps));


        Map<String, Integer> maps2 = new HashMap<String, Integer>();
        maps2.put("a", 8);
        maps2.put("b", 12);
        maps2.put("c", 53);
        maps2.put("d", 33);
        System.out.println(MapUtilTest.getKeys2(53, maps2));


    }


    /**
     * 求Map<K,V>中Key(键)的最大值
     *
     * @param map
     * @return
     */
    public static Object getMaxKey(Map<Integer, Integer> map) {
        if (map == null) return null;
        Set<Integer> set = map.keySet();
        Object[] obj = set.toArray();
        Arrays.sort(obj);
        return obj[obj.length - 1];
    }

    /**
     * 求Map<K,V>中Value(值)的最大值
     *
     * @param map
     * @return
     */
    public static Object getMaxValue(Map<Integer, Integer> map) {
        if (map == null) return null;
        Collection<Integer> c = map.values();
        Object[] obj = c.toArray();
        Arrays.sort(obj);
        return obj[obj.length - 1];
    }

    /**
     * 通过value获取key
     *
     * @param value
     * @param map
     * @return
     */
    public static List<Object> getKeys(Object value, Map<Object, Object> map) {
        ArrayList<Object> keys = new ArrayList<Object>();
        for (Map.Entry<Object, Object> entry : map.entrySet()) {
            if (value.equals(entry.getValue())) {
                keys.add(entry.getKey());
            } else {
                continue;
            }
        }
        return keys;
    }

    /**
     * 通过value获取key
     *
     * @param value
     * @param map
     * @return
     */
    public static String getKeys2(Object value, Map<String, Integer> map) {
        for (Map.Entry<String, Integer> entry : map.entrySet()) {
            if (value.equals(entry.getValue())) {
                return entry.getKey();
            } else {
                continue;
            }
        }
        return null;
    }

    /**
     * 将rs转为list
     *
     * @param rs
     * @return
     * @throws SQLException
     */
    private static List convertList(ResultSet rs) throws SQLException {
        List list = new ArrayList();
        ResultSetMetaData md = rs.getMetaData();//获取键名
        int columnCount = md.getColumnCount();//获取行的数量
        while (rs.next()) {
            Map rowData = new HashMap();//声明Map
            for (int i = 1; i <= columnCount; i++) {
                rowData.put(md.getColumnName(i), rs.getObject(i));//获取键名及值
            }
            list.add(rowData);
        }
        return list;
    }
}

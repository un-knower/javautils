package com.cenrise.utils.xml.sax;

import java.util.ArrayList;
import java.util.Map;

/**
 * 程序入口
 */

public class XmlSaxTest {
    /**
     * @param args
     */
    public static void main(String[] args) {
        XmlSaxTest xmlSaxTest = new XmlSaxTest();
        // TODO Auto-generated method stub
//        ArrayList<Map<String, String>> list = (ArrayList<Map<String, String>>) SaxService.ReadXML("myClass.xml", "class");
        /*for(int i=0;i<list.size();i++){
        HashMap<String, String> temp=(HashMap<String, String>) list.get(i);
        Iterator<String> iterator=temp.keySet().iterator();
        while(iterator.hasNext()){
        String key=iterator.next().toString();
        String value=temp.get(key);
        System.out.print(key+" "+value+"--");
        }
        }*/

        //策略是只读sql执行器组件和存储过程，其它的只记录转换名
        ArrayList<Map<String, String>> entryList = (ArrayList<Map<String, String>>) SaxService.ReadXML("STARTDATE.xml", "entry");
        ArrayList<Map<String, String>> hopList = (ArrayList<Map<String, String>>) SaxService.ReadXML("STARTDATE.xml", "hop");

        ArrayList<Map<String, String>> stepList = (ArrayList<Map<String, String>>) SaxService.ReadXML("STARTDATE.xml", "step");

        for (Map<String, String> hopMap : hopList) {
            String from = hopMap.get("from");
            String to = hopMap.get("to");
            Map<String, String> entryMapFrom = xmlSaxTest.queryEntry(entryList, from);
            Map<String, String> entryMapTo = xmlSaxTest.queryEntry(entryList, to);
            //开始，转换类型
            String fileType = entryMapFrom.get("type");
            if (fileType.equals("SPECIAL")) {

            } else if (fileType.equals("TRANS")) {
                //<transname>STARTDATE.ktr</transname>
                // <directory>${Internal.Job.Filename.Directory}</directory>
                String transname = entryMapFrom.get("transname");
                String directory = entryMapFrom.get("directory");


                for (Map<String, String> stepMap : stepList) {
                    String stepType = stepMap.get("type");
                    if (stepType.equals("DBProc")) {
                        //执行存储过程组件没有transname和directory
                        /** <step>
                         <name>调用DB存储过程</name>
                         <type>DBProc</type>
                         <connection>163生产查询</connection>
                         <procedure>proc_miantableName_PrevNext</procedure>
                         </step>*/


                    } else if (stepType.equals("ExecSQL")) {
                        //sql执行器
                        /**<step>
                         <name>执行SQL脚本</name>
                         <type>ExecSQL</type>
                         <connection>生产123</connection>
                         <sql>select * from dual</sql>
                         </step>*/
                    }
                }


            } else if (fileType.equals("JOB")) {
                //${Internal.Job.Filename.Directory}/Totalamount.kjb
                String filename = entryMapFrom.get("filename");
                //TODO 需要处理一些变量的转换的情况，当前路径都传过来


            }
            System.out.println(entryMapFrom.get("name") + "=========" + entryMapTo.get("name"));
        }


//        ArrayList<Map<String, String>> entryList = (ArrayList<Map<String, String>>) SaxService.ReadXML("STARTDATE.xml", "entry");
//        ArrayList<Map<String, String>> list2 = (ArrayList<Map<String, String>>) SaxService.ReadXML("STARTDATE.xml", "entries");
//        System.out.println(entryList.toString());
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

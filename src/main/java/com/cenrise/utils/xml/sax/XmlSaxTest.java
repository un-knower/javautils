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

        ArrayList<Map<String, String>> list2 = (ArrayList<Map<String, String>>) SaxService.ReadXML("STARTDATE.xml", "entry");
        System.out.println(list2.toString());
    }
}

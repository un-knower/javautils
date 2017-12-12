package com.cenrise.utils.xml.sax;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.util.List;
import java.util.Map;

/**
 * 封装解析业务类
 */
public class SaxService {
    /**
     * 获取某个节点的信息
     *
     * @param uri
     * @param NodeName
     * @return
     */
    public static List<Map<String, String>> ReadXML(String uri, String NodeName) {
        try {
            //创建一个解析XML的工厂对象
            SAXParserFactory parserFactory = SAXParserFactory.newInstance();
            //创建一个解析XML的对象
            SAXParser parser = parserFactory.newSAXParser();
            //创建一个解析助手类
            Saxhandler myhandler = new Saxhandler(NodeName);
            parser.parse(uri, myhandler);
            return myhandler.getList();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
        }
        return null;
    }
}

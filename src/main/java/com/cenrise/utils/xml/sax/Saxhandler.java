package com.cenrise.utils.xml.sax;


import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 用SAX解析XML的Handler
 */
public class Saxhandler extends DefaultHandler {
    //存储正在解析的元素的数据
    private Map<String, String> map = null;
    //存储所有解析的元素的数据
    private List<Map<String, String>> list = null;
    //正在解析的元素的名字
    String currentTag = null;
    //正在解析的元素的元素值
    String currentValue = null;
    //开始解析的元素
    String nodeName = null;

    public Saxhandler(String nodeName) {
        this.nodeName = nodeName;
    }

    public List<Map<String, String>> getList() {
        return list;
    }

    /**
     * 开始解析文档，即开始解析XML根元素时调用该方法
     *
     * @throws SAXException
     */
    @Override
    public void startDocument() throws SAXException {
        // TODO Auto-generated method stub
        //初始化Map
        list = new ArrayList<Map<String, String>>();
    }

    /**
     * 开始解析每个元素时都会调用该方法
     *
     * @param uri
     * @param localName
     * @param qName
     * @param attributes
     * @throws SAXException
     */
    @Override
    public void startElement(String uri, String localName, String qName,
                             Attributes attributes) throws SAXException {
        // TODO Auto-generated method stub
        //判断正在解析的元素是不是开始解析的元素
        if (qName.equals(nodeName)) {
            map = new HashMap<String, String>();
        }
        //判断正在解析的元素是否有属性值,如果有则将其全部取出并保存到map对象中，如:<person id="00001"></person>
        if (attributes != null && map != null) {
            for (int i = 0; i < attributes.getLength(); i++) {
                map.put(attributes.getQName(i), attributes.getValue(i));
            }
        }
        //正在解析的元素
        currentTag = qName;
    }

    /**
     * 解析到每个元素的内容时会调用此方法
     *
     * @param ch
     * @param start
     * @param length
     * @throws SAXException
     */
    @Override
    public void characters(char[] ch, int start, int length)
            throws SAXException {
        if (currentTag != null && map != null) {
            currentValue = new String(ch, start, length);
            //如果内容不为空和空格，也不是换行符则将该元素名和值和存入map中
            if (currentValue != null && !currentValue.trim().equals("") && !currentValue.trim().equals("\n")) {
                map.put(currentTag, currentValue);
            }
            //当前的元素已解析过，将其置空用于下一个元素的解析
            currentTag = null;
            currentValue = null;
        }
    }

    /**
     * 每个元素结束的时候都会调用该方法
     *
     * @param uri
     * @param localName
     * @param qName
     * @throws SAXException
     */
    @Override
    public void endElement(String uri, String localName, String qName)
            throws SAXException {
        //判断是否为一个节点结束的元素标签
        if (qName.equals(nodeName)) {
            list.add(map);
            map = null;
        }
    }

    /**
     * 结束解析文档，即解析根元素结束标签时调用该方法
     *
     * @throws SAXException
     */
    @Override
    public void endDocument() throws SAXException {
        // TODO Auto-generated method stub
        super.endDocument();
    }
}

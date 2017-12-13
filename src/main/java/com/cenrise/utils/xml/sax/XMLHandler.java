package com.cenrise.utils.xml.sax;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class XMLHandler extends DefaultHandler {
	private List<String> tagName;

	private List<String> attNames;

	// 存储正在解析的元素的数据
	private Map<String, String> map = null;
	// 存储所有解析的元素的数据
	private List<Map<String, String>> list = null;
	// 正在解析的元素的名字
	String currentTag = null;
	// 正在解析的元素的元素值
	String currentValue = null;
	// 开始解析的元素
	String nodeName = null;

	// 存储正在解析的元素的数据
	private HashMap<String, String> elementMap = null;

	public XMLHandler(String nodeName, List<String> tagNames,
                      List<String> attNames) {
		this.nodeName = nodeName;
		this.tagName = tagNames;
		this.attNames = attNames;
	}

	public XMLHandler(String nodeName) {
		this.nodeName = nodeName;
	}

	public List<Map<String, String>> getList() {
		return list;
	}

	public HashMap<String, String> getElementMap() {
		return elementMap;
	}

	// 开始解析文档，即开始解析XML根元素时调用该方法
	@Override
	public void startDocument() throws SAXException {
		// 初始化Map
		list = new ArrayList<Map<String, String>>();
		elementMap = new HashMap<String, String>();
	}

	// 开始解析每个元素时都会调用该方法
	@Override
	public void startElement(String uri, String localName, String qName,
			Attributes attributes) throws SAXException {
		// 判断正在解析的元素是不是开始解析的元素
		if (qName.equals(nodeName)) {
			map = new HashMap<String, String>();
		}
		if (map != null && !qName.equals(nodeName)) {
			elementMap.put(qName, "node");
		}
		// 判断正在解析的元素是否有属性值,如果有则将其全部取出并保存到map对象中，如:<person id="00001"></person>
		if (attributes != null && map != null) {
			for (int i = 0; i < attributes.getLength(); i++) {
				if (attNames != null
						&& attNames.contains(attributes.getQName(i))) {
					map.put(attributes.getQName(i), attributes.getValue(i));
				}
				elementMap.put(attributes.getQName(i), "attribut");
			}
		}
		currentTag = qName; // 正在解析的元素
	}

	// 解析到每个元素的内容时会调用此方法
	@Override
	public void characters(char[] ch, int start, int length)
			throws SAXException {
		if (currentTag != null && map != null) {
			currentValue = new String(ch, start, length);
			// 如果内容不为空和空格，也不是换行符则将该元素名和值和存入map中
			if (currentValue != null && !currentValue.trim().equals("")
					&& !currentValue.trim().equals("\n")) {
				if (currentValue != null && !currentValue.trim().equals("") && !currentValue.trim().equals("\n")) {
					map.put(currentTag, currentValue);
				}
			}
			// 当前的元素已解析过，将其置空用于下一个元素的解析
			currentTag = null;
			currentValue = null;
		}
	}

	// 每个元素结束的时候都会调用该方法
	@Override
	public void endElement(String uri, String localName, String qName)
			throws SAXException {
		// 判断是否为一个节点结束的元素标签
		if (qName.equals(nodeName)) {
			list.add(map);
			map = null;
		}
	}

	// 结束解析文档，即解析根元素结束标签时调用该方法
	@Override
	public void endDocument() throws SAXException {
		super.endDocument();
	}
}

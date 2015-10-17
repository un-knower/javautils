package com.cenrise.commons.utils;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;

/**
 * XML相关操作
 * @author Admin
 *
 */
public class XMLUtil {
	/**字符串转为Document对象
	 * 通过dom4j把xml对象转换成string 导入dom4j包，可以到官网上去下载后导入
	 * @param xmlStr
	 * @return
	 */
	public org.dom4j.Document StrToXMLDocment(String xmlStr) {// Str是传入的一段XML内容的字符串
		org.dom4j.Document document = null;
		try {
			document = DocumentHelper.parseText(xmlStr);// DocumentHelper.parseText(str)这个方法将传入的XML字符串转换处理后返回一个Document对象
		} catch (DocumentException e) {
			e.printStackTrace();
		}
		return document;
	}

	// Document对象转为字符串
	public String XMLDocumentToStr(Document document) {
		String XMLStr = document.asXML();// obj.asXML()则为Document对象转换为字符串方法
		return XMLStr;
	}
}

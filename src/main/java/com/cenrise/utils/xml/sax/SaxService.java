package com.cenrise.utils.xml.sax;

import org.xml.sax.InputSource;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.PushbackReader;
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
//            XMLHandler myhandler = new XMLHandler(NodeName);


            FileInputStream is = new FileInputStream("/Users/jiadongpo/Documents/VbillRepo/increment/T_SES_PAY_ORD_OPT_HIS/SychAddData.kjb");

            PushbackReader r = new PushbackReader(new InputStreamReader(is,
                    "UTF-8"));
            int ch = -1;
            // scan for the < character
            while ((ch = r.read()) != '<')
                ;
            if (ch == '<')
                r.unread('<');
            InputSource source = new InputSource(r);
            parser.parse(source, myhandler);

//            File file = new File("/Users/jiadongpo/Documents/VbillRepo/increment/T_SES_PAY_ORD_OPT_HIS/SychAddData.kjb");
//            parser.parse(uri, myhandler);
            return myhandler.getList();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
        }
        return null;
    }
}

package com.cenrise.commons.utils.algorithm;

import java.io.File;
import java.io.FilenameFilter;

import junit.framework.TestCase;

import com.cenrise.commons.utils.SysUtil;

public class FileEncodingUtilTest extends TestCase {

    public void testConvert()   {
        String file = SysUtil.CURRENT_USER_DIR + "/src/test/resources/GBKTOUTF8.txt";
        FileEncodingUtil.convert(file, "GBK", "UTF-8");
    }

    public void testConvert1() {
        String file = SysUtil.CURRENT_USER_DIR + "/src/test/resources/GBKTOUTF8.txt";
        FileEncodingUtil.convert(file, "UTF-8", "GBK", new FilenameFilter() {
                    @Override
                    public boolean accept(File dir, String name) {
                        return name.endsWith("txt");
                    }
                });
    }

    public void testConvert2(){
        String file = SysUtil.CURRENT_USER_DIR + "/src/test/resources/GBKTOUTF8.txt";
        FileEncodingUtil.convert(new File(file),"GBK", "UTF-8");
    }
}
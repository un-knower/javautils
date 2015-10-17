package com.cenrise.commons.utils;


import static org.junit.Assert.assertEquals;

import org.junit.Assert;

/**
 * <h6>Description:<h6>
 * <p></p>
 *
 * @date 2015-05-29.
 */
public class FilePathUtilTest {


    @org.junit.Test
    public void testCommandPath() throws Exception {
        Assert.assertEquals("路径计算错误", FileUtil.commandPath("//home/scott"), "/home/scott");
        assertEquals("路径计算错误", FileUtil.commandPath("c:\\home\\scott"), "c:/home/scott");
    }


    @org.junit.Test
    public void testGetParentPath() throws Exception {

    }

    @org.junit.Test
    public void testLegalFile() {
        assertEquals("判断错误", true, FileUtil.legalFile("c:\\1.txt"));
        assertEquals("判断错误", true, FileUtil.legalFile("c:/1.txt"));
        assertEquals("判断错误", true, FileUtil.legalFile("C:\\Program Files (x86)\\Tencent"));
        assertEquals("判断错误", false, FileUtil.legalFile("C:\\Program Files\" (x86)\\Tencent"));
//        assertEquals("判断错误",true,FileUtil.legalFile("/root/Tencent"));
//        assertEquals("判断错误",true,FileUtil.legalFile("../Tencent"));
    }
}
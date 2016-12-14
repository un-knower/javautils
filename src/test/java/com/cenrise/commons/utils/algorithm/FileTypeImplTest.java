package com.cenrise.commons.utils.algorithm;

import java.io.File;

import junit.framework.TestCase;

import org.junit.Test;

public class FileTypeImplTest extends TestCase {

    @Test
    public void testFileType() {

        String path = System.getProperty("user.dir") + "/Junit/Resource";
        assertEquals("gif", FileTypeImpl.getFileType(new File(path + "/ali.gif")));
        assertEquals("png", FileTypeImpl.getFileType(new File(path + "/tgepng")));
    }

}
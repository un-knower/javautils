package com.cenrise.commons.utils;


import java.io.File;
import java.text.ParseException;
import java.util.List;

import org.junit.Test;

import com.cenrise.utils.DateUtil;
import com.cenrise.utils.FileUtil;

public class FileTest {
    @Test
    public void testListLine() throws ParseException {
        File file = new File(System.getProperty("user.dir") + "/Junit/Resource/time.txt");
        List<String> lines = FileUtil.lines(file);
        int count=0;

        for(String line :lines){
            String[] tt = line.split("/");
            count += DateUtil.subtimeBurst(tt[0], tt[1], "08:00-19:30");
        }
        System.out.println("sum > "+count/60/60);
        System.out.println("count > "+lines.size());

    }

}

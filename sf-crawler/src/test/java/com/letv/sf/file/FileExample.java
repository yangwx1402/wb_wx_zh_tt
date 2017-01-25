package com.letv.sf.file;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.List;

/**
 * Created by yangyong3 on 2016/12/8.
 */
public class FileExample {
    public static void main(String[] args) throws IOException {
//     List<String> data = FileUtils.readLines(new File("E:\\weibo.txt"),"utf-8");
//        System.out.println(data);
        System.out.println(new Date(1484640361997l));
    }
}

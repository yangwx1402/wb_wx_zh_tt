package com.letv.spider.utils;

import org.apache.commons.io.FileUtils;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * Created by yangyong3 on 2017/1/18.
 */
public class CookieReader {


    private static DateFormat dateFormat = new SimpleDateFormat("EEE dd MMM yyyy hh:mm:ss z", Locale.ENGLISH);

    public static String readCookie(String jsonFile) throws IOException, ParseException {
        String json = FileUtils.readFileToString(new File(jsonFile),"utf-8");
        ObjectMapper mapper = new ObjectMapper();
        List<Map<String, Object>> beanList = mapper.readValue(json, new TypeReference<List<Map<String, Object>>>() {
        });
        System.out.println(beanList.size());
        StringBuilder sb = new StringBuilder();
        for (Map<String, Object> map : beanList) {
            String domain = map.get("domain") == null ? null : map.get("domain").toString();
            String name = map.get("name") == null ? null : map.get("name").toString();
            String value = map.get("value") == null ? null : map.get("value").toString();
            String path = map.get("path") == null ? null : map.get("path").toString();
            Date date = map.get("expires") == null ? null : dateFormat.parse(map.get("expires").toString().replaceAll(",",""));
            sb.append(name+"="+value+";");
        }
        return sb.toString();
    }

    public static void main(String[] args) throws IOException, ParseException {
        // System.out.println(CookieReader.readCookie("D:\\le_eco\\sf-spider\\sf-crawler\\src\\main\\resources\\cookie.txt"));
        System.out.println(dateFormat.parse("Sat 13 Jan 2018 10:20:51 GMT"));
    }
}

package com.letv.sf.cookie;

import com.gargoylesoftware.htmlunit.util.Cookie;
import com.letv.sf.utils.JsonUtils;
import org.apache.commons.io.FileUtils;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by yangyong3 on 2017/1/18.
 */
public class CookieReader {


    private static DateFormat dateFormat = new SimpleDateFormat("EEE dd MMM yyyy hh:mm:ss z", Locale.CHINA);

    public static Set<Cookie> readCookie(String json) throws IOException, ParseException {
        ObjectMapper mapper = new ObjectMapper();
        List<Map<String, Object>> beanList = mapper.readValue(json, new TypeReference<List<Map<String, Object>>>() {
        });
        System.out.println(beanList.size());
        Set<Cookie> cookies = new HashSet<Cookie>();
        Cookie cookie = null;
        for (Map<String, Object> map : beanList) {
            String domain = map.get("domain") == null ? null : map.get("domain").toString();
            String name = map.get("name") == null ? null : map.get("name").toString();
            String value = map.get("value") == null ? null : map.get("value").toString();
            String path = map.get("path") == null ? null : map.get("path").toString();
            Date date = map.get("expires") == null ? null : dateFormat.parse(map.get("expires").toString().replaceAll(",","").replaceAll("周","星期"));
            boolean secure = map.get("secure") == null ? false : (boolean) map.get("secure");
            boolean http = map.get("httponly") == null ? false : (boolean) map.get("httponly");
            cookie = new Cookie(domain,name,value,path,date,secure,http);
            cookies.add(cookie);
        }
        return cookies;
    }

    public static Set<Cookie> getCookie() throws IOException, ParseException {
        File file = new File("D:\\le_eco\\sf-spider\\sf-crawler\\src\\main\\resources\\cookie.txt");
        String json = FileUtils.readFileToString(file, "utf-8");
        return readCookie(json);
    }

    public static void main(String[] args) throws IOException, ParseException {

        System.out.println(CookieReader.getCookie());

        System.out.println(dateFormat.format(new Date()));
    }
}

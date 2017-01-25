package com.letv.sf.http.cookie;

import java.io.IOException;
import java.util.Map;

/**
 * Created by yangyong3 on 2016/12/5.
 */

/**
 * cookie爬取接口
 */
public interface CookieCrawler {
    /**
     * 登录网站并获取cookie
     * @return
     * @throws IOException
     */
     Map<String, String> login() throws IOException;
    /**
     * 通过get方法获取网页信息
     * @param url
     * @return
     * @throws Exception
     */
     String fetchUrl(String url) throws Exception;

     void close();
}

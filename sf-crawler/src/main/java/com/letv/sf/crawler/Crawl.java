package com.letv.sf.crawler;

import com.letv.sf.http.HttpResult;

import java.util.Map;

/**
 * Created by yangyong3 on 2016/11/29.
 */
public interface Crawl {
    /**
     * 爬取抽象
     * @param url
     * @param params
     * @return
     * @throws Exception
     */
    public HttpResult crawl(String url,Map<String,String> params) throws Exception;
}

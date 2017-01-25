package com.letv.sf.zhihu.crawler;

import com.letv.sf.crawler.Crawl;
import com.letv.sf.crawler.zhihu.ZhihuTopicCrawler;
import com.letv.sf.http.HttpResult;

/**
 * Created by yangyong3 on 2016/12/9.
 */
public class ZhihuTopicExample {
    public static void main(String[] args) throws Exception {
        String url = "https://www.zhihu.com/topic/20038208/hot";
        Crawl crawl = new ZhihuTopicCrawler();
        HttpResult httpResult = crawl.crawl(url, null);
        System.out.println(httpResult.getContent());
    }
}

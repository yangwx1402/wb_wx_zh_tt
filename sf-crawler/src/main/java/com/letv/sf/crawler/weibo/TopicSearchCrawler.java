package com.letv.sf.crawler.weibo;

import com.letv.sf.crawler.AbstractCrawler;
import com.letv.sf.http.HttpResult;

import java.io.IOException;
import java.util.Map;

/**
 * Created by yangyong3 on 2016/11/30.
 * 微博话题搜索爬虫
 */
public class TopicSearchCrawler extends AbstractCrawler {

    /**
     * 微博话题爬取
     * @param url
     * @param params
     * @return
     * @throws IOException
     */
    public HttpResult crawl(String url, Map<String, String> params) throws IOException {
        return super.weiboApiCrawler(url,params);
    }
}

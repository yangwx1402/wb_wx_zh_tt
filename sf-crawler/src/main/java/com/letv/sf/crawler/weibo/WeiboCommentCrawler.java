package com.letv.sf.crawler.weibo;

import com.letv.sf.crawler.AbstractCrawler;
import com.letv.sf.http.HttpResult;

import java.util.Map;

/**
 * Created by yangyong3 on 2016/12/1.
 * 微博评论爬虫
 */
public class WeiboCommentCrawler extends AbstractCrawler {
    /**
     * 微博评论爬取
     * @param url
     * @param params
     * @return
     * @throws Exception
     */
    public HttpResult crawl(String url, Map<String, String> params) throws Exception {
        return super.weiboApiCrawler(url,params);
    }
}

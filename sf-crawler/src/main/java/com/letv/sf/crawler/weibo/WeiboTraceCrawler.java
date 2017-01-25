package com.letv.sf.crawler.weibo;

import com.letv.sf.crawler.AbstractCrawler;
import com.letv.sf.crawler.Crawl;
import com.letv.sf.http.HttpResult;

import java.util.Map;

/**
 * Created by yangyong3 on 2016/12/2.
 * 微博趋势跟踪爬虫
 */
public class WeiboTraceCrawler extends AbstractCrawler implements Crawl {
    /**
     * 微博评论转发点赞数跟踪爬取
     * @param url
     * @param params
     * @return
     * @throws Exception
     */
    public HttpResult crawl(String url, Map<String, String> params) throws Exception {
        return super.weiboApiCrawler(url,params);
    }
}

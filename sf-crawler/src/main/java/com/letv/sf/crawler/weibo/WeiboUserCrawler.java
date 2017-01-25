package com.letv.sf.crawler.weibo;

import com.letv.sf.crawler.AbstractCrawler;
import com.letv.sf.http.HttpResult;

import java.io.IOException;
import java.util.Map;

/**
 * Created by yangyong3 on 2016/11/29.
 * 微博用户信息爬虫
 */
public class WeiboUserCrawler extends AbstractCrawler {

    public HttpResult crawl(String url,Map<String,String> params) throws IOException {
      return super.weiboApiCrawler(url,params);
    }
}

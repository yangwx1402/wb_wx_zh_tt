package com.letv.sf.crawler.weixin;

import com.letv.sf.config.CrawlerSlotType;
import com.letv.sf.crawler.AbstractCrawler;
import com.letv.sf.crawler.Crawl;
import com.letv.sf.entity.common.HttpToolGroup;
import com.letv.sf.http.HttpResult;
import com.letv.sf.http.HttpToolFactory;

import java.util.Map;

/**
 * Created by yangyong3 on 2016/12/5.
 * 微信公众号内容搜索爬虫
 */
public class WeixinSearchCrawler extends AbstractCrawler implements Crawl {
    /**
     * 微信搜索爬取
     * @param url
     * @param params
     * @return
     * @throws Exception
     */
    public HttpResult crawl(String url, Map<String, String> params) throws Exception {
        HttpToolGroup group = HttpToolFactory.getHttpToolGroupByType(CrawlerSlotType.weixin.toString());
        HttpResult httpResult = group.getUrlByJsoup(url,true);
        httpResult.setUrl(url);
        return httpResult;
    }
}

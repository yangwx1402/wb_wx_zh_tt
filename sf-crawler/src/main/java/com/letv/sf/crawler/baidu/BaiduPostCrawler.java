package com.letv.sf.crawler.baidu;

import com.letv.sf.config.CrawlerSlotType;
import com.letv.sf.crawler.AbstractCrawler;
import com.letv.sf.crawler.Crawl;
import com.letv.sf.entity.common.HttpToolGroup;
import com.letv.sf.http.HttpResult;
import com.letv.sf.http.HttpToolFactory;

import java.util.Map;

/**
 * Created by yangyong3 on 2016/12/12.
 */

/**
 * 百度贴吧爬取器
 */
public class BaiduPostCrawler extends AbstractCrawler implements Crawl {
    public HttpResult crawl(String url, Map<String, String> params) throws Exception {
        HttpToolGroup group = HttpToolFactory.getHttpToolGroupByType(CrawlerSlotType.baidu.toString());
        HttpResult httpResult = group.getUrlByJsoup(url,true);
        httpResult.setUrl(url);
        return httpResult;
    }
}

package com.letv.sf.crawler.toutiao;

import com.letv.sf.config.CrawlerSlotType;
import com.letv.sf.crawler.AbstractCrawler;
import com.letv.sf.crawler.Crawl;
import com.letv.sf.entity.common.HttpToolGroup;
import com.letv.sf.http.HttpResult;
import com.letv.sf.http.HttpToolFactory;

import java.util.Map;

/**
 * Created by yangyong3 on 2017/1/5.
 */
public class ToutiaoKeywordSearchCrawler extends AbstractCrawler implements Crawl {
    public HttpResult crawl(String url, Map<String, String> params) throws Exception {
        HttpToolGroup group = HttpToolFactory.getHttpToolGroupByType(CrawlerSlotType.common.toString());
        HttpResult httpResult = group.getUrlByJsoup(url, true);
        if (httpResult != null)
            httpResult.setUrl(url);
        return httpResult;
    }
}

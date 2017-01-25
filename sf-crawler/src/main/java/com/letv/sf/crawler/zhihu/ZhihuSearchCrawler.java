package com.letv.sf.crawler.zhihu;

import com.letv.sf.config.CrawlerSlotType;
import com.letv.sf.crawler.AbstractCrawler;
import com.letv.sf.crawler.Crawl;
import com.letv.sf.entity.common.HttpToolGroup;
import com.letv.sf.http.HttpResult;
import com.letv.sf.http.HttpToolFactory;

import java.util.Map;

/**
 * Created by yangyong3 on 2016/12/7.
 * 知乎问题搜索爬虫
 */
public class ZhihuSearchCrawler extends AbstractCrawler implements Crawl {
    /**
     * 知乎搜索爬取,通过搜索sogou知乎搜索
     * @param url
     * @param params
     * @return
     * @throws Exception
     */
    public HttpResult crawl(String url, Map<String, String> params) throws Exception {
        HttpToolGroup group = HttpToolFactory.getHttpToolGroupByType(CrawlerSlotType.zhihu.toString());
        HttpResult httpResult = group.getUrlByJsoup(url,true);
        httpResult.setUrl(url);
        return httpResult;
    }
}

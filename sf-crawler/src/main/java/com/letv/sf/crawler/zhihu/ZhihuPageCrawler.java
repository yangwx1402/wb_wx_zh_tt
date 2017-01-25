package com.letv.sf.crawler.zhihu;

import com.letv.sf.config.CrawlerSlotType;
import com.letv.sf.crawler.AbstractCrawler;
import com.letv.sf.crawler.Crawl;
import com.letv.sf.entity.common.HttpToolGroup;
import com.letv.sf.http.HttpResult;
import com.letv.sf.http.HttpToolFactory;

import java.util.Map;

/**
 * Created by yangyong3 on 2016/12/9.
 * 知乎问题页爬虫
 */
public class ZhihuPageCrawler extends AbstractCrawler implements Crawl {
    /**
     * 知乎问题页爬取
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

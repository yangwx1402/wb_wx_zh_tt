package com.letv.sf.crawler;

import com.letv.sf.config.CrawlerSlotType;
import com.letv.sf.entity.common.HttpToolGroup;
import com.letv.sf.http.HttpResult;
import com.letv.sf.http.HttpToolFactory;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.util.Map;

/**
 * Created by yangyong3 on 2016/11/30.
 */
public abstract class AbstractCrawler implements Crawl {

    private static final Logger log = Logger.getLogger(AbstractCrawler.class);

    /**
     * 通过微博api爬取信息
     *
     * @param url
     * @param params
     * @return
     * @throws IOException
     */
    protected HttpResult weiboApiCrawler(String url, Map<String, String> params) throws IOException {
        HttpToolGroup group = HttpToolFactory.getHttpToolGroupByType(CrawlerSlotType.weibo.toString());
        if (!url.contains("source"))
            url += "&source=" + group.getApp_key();
        log.info("crawl url by jsoup url -[" + url + "]");
        HttpResult httpResult = group.getUrlByJsoup(url, true);
        if (httpResult != null)
            httpResult.setUrl(url);
        return httpResult;
    }
}

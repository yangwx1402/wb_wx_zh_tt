package com.letv.sf.job.thread.weixin;

import com.letv.sf.config.SpiderConfig;
import com.letv.sf.crawler.CrawlerFactory;
import com.letv.sf.parser.ParseFactory;

/**
 * Created by yangyong3 on 2016/12/7.
 * 微信关键词搜索线程
 */
public class WeixinSearchCrawlerThread extends BaseYuqing implements Runnable {
    public void run() {
        search(SpiderConfig.getWeixinXinKeyWordQueueName(), SpiderConfig.getWeixinSearchUrl(),
                CrawlerFactory.weixinSearchCrawler, ParseFactory.weixinSearchParser, SpiderConfig.getWeixinSearchPageLimit());
    }
}

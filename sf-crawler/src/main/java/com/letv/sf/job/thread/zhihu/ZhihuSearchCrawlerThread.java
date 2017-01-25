package com.letv.sf.job.thread.zhihu;

import com.letv.sf.config.SpiderConfig;
import com.letv.sf.job.thread.weixin.BaseYuqing;
import com.letv.sf.parser.ParseFactory;
import com.letv.sf.crawler.CrawlerFactory;

/**
 * Created by yangyong3 on 2016/12/9.
 * 知乎关键词搜索线程
 */
public class ZhihuSearchCrawlerThread extends BaseYuqing implements Runnable {
    public void run() {
        search(SpiderConfig.getZhihuKeyWordQueueName(), SpiderConfig.getZhihuSearchUrl(),
                CrawlerFactory.zhihuSearchCrawler, ParseFactory.zhihuSearchParser, SpiderConfig.getZhihuSearchPageLimit());
    }
}

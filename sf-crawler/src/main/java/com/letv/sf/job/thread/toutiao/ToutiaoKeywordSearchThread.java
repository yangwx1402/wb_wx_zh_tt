package com.letv.sf.job.thread.toutiao;

import com.letv.sf.config.SpiderConfig;
import com.letv.sf.crawler.CrawlerFactory;
import com.letv.sf.dao.DaoFactory;
import com.letv.sf.entity.beidou.BeidouEntity;
import com.letv.sf.entity.common.CrawlerResultEntity;
import com.letv.sf.http.HttpResult;
import com.letv.sf.mq.MessageFactory;
import com.letv.sf.parser.ParseFactory;
import org.apache.log4j.Logger;

import java.util.Map;

/**
 * Created by yangyong3 on 2017/1/5.
 */
public class ToutiaoKeywordSearchThread implements Runnable {

    private static final Logger log = Logger.getLogger(ToutiaoKeywordSearchThread.class);
    private static final int default_seed = 10;
    public void run() {
        BeidouEntity beidou = null;
        while (true) {
            try {
                beidou = MessageFactory.sub(SpiderConfig.getToutiaoSearchKeywordQueueName(), BeidouEntity.class);
                if (beidou != null)
                    process(beidou);
                Thread.sleep(SpiderConfig.getSleepTimes(default_seed));
            } catch (Exception e) {
                e.printStackTrace();
                try {
                    Thread.sleep(SpiderConfig.getSleepTimes(default_seed));
                } catch (InterruptedException e1) {
                    e1.printStackTrace();
                }
            }
        }
    }

    private void process(BeidouEntity beidou) {
        HttpResult<BeidouEntity> httpResult = null;
        CrawlerResultEntity<Map<String, Object>> data = null;
        StringBuffer url = new StringBuffer(100);
        for (int i = 0; i < SpiderConfig.getToutiaoKeyWordSearchLimitPageCount(); i++) {
            try {
                url.append(SpiderConfig.getToutiaoKeywordSearchUrl() + "&offset=" + (i * SpiderConfig.getToutiaoKeywordSearchPageSize()) + "&count=" + SpiderConfig.getToutiaoKeywordSearchPageSize() + "&keyword=" + beidou.getTag());
                log.info("crawl toutiao url -" + url.toString());
                httpResult = CrawlerFactory.toutiaoSearcher.crawl(url.toString(), null);
                if (httpResult == null) {
                    return;
                }
                httpResult.setMeta(beidou);
                data = ParseFactory.toutiaoSearchParser.parse(httpResult);
                DaoFactory.toutiaoDao.saveToutiaoArticle(data.getItems());
                //看看是否还有内容，有就进行翻页
                if (data.getPage() == null || !data.getPage().isHasMore()) {
                    log.info("crawl toutiao url " + url.toString() + " end  maxPage is -" + i);
                    return;
                }
                Thread.sleep(SpiderConfig.getSleepTimes(default_seed));
                url.setLength(0);
            } catch (Exception e) {
                e.printStackTrace();
                log.error("crawl toutiao error url -" + url.toString() + ",message is ", e);
                url.setLength(0);
                try {
                    Thread.sleep(SpiderConfig.getSleepTimes(default_seed));
                } catch (InterruptedException e1) {
                    e1.printStackTrace();
                }
            }
        }
    }
}

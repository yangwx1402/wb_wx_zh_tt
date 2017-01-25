package com.letv.sf.job.thread.toutiao;

import com.letv.sf.config.SpiderConfig;
import com.letv.sf.crawler.CrawlerFactory;
import com.letv.sf.dao.DaoFactory;
import com.letv.sf.entity.toutiao.ToutiaoArticle;
import com.letv.sf.http.HttpResult;
import com.letv.sf.mq.MessageFactory;
import com.letv.sf.parser.ParseFactory;
import org.apache.commons.collections.CollectionUtils;

/**
 * Created by yangyong3 on 2017/1/6.
 */
public class ToutiaoPageCommentThread implements Runnable {

    private static final int default_seed = 10;
    public void run() {
        ToutiaoArticle article = null;
        while (true) {
            try {
                article = MessageFactory.sub(SpiderConfig.getToutiaoCommentQueueName(), ToutiaoArticle.class);
                if (article != null)
                    process(article);
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

    private void process(ToutiaoArticle article) throws Exception {
        HttpResult<ToutiaoArticle> httpResult = CrawlerFactory.toutiaoPageCrawler.crawl(article.getUrl(), null);
        httpResult.setMeta(article);
        article = ParseFactory.toutiaoPageCommentParser.parse(httpResult);
        if (article != null) {
            DaoFactory.toutiaoDao.updateToutiao(article);
            if (!CollectionUtils.isEmpty(article.getComments())) {
                DaoFactory.toutiaoDao.saveToutiaoComments(article.getComments());
            }
        }
    }
}

package com.letv.sf.job.thread.weixin;

import com.letv.sf.config.SpiderConfig;
import com.letv.sf.config.YuqingState;
import com.letv.sf.crawler.CrawlerFactory;
import com.letv.sf.dao.DaoFactory;
import com.letv.sf.dao.yuqing.mapper.YuqingCommentDocMapper;
import com.letv.sf.dao.yuqing.mapper.YuqingDocMapper;
import com.letv.sf.entity.yuqing.YuqingArticle;
import com.letv.sf.http.HttpResult;
import com.letv.sf.mq.MessageFactory;
import com.letv.sf.parser.ParseFactory;
import org.apache.log4j.Logger;

/**
 * Created by yangyong3 on 2016/12/6.
 * 微信文章抓取线程
 */
public class WeixinPageCrawlerThread implements Runnable {
    private static final int default_seed = 10;
    private static final Logger log = Logger.getLogger(WeixinPageCrawlerThread.class);

    public void run() {
        while (true) {
            try {
                YuqingArticle article = MessageFactory.sub(SpiderConfig.getWeixinCommentSeedQueueName(), YuqingArticle.class);
                if (article != null)
                    processArticle(article);
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

    private void processArticle(YuqingArticle source) {
        try {
            //根据公众号文章的url爬取文章信息
            HttpResult httpResult = CrawlerFactory.weixinPageCrawler.crawl(source.getUrl(), null);
            if (httpResult == null) {
                log.error("爬取网页失败,url=[" + source.getUrl() + "]，statusCode="+httpResult.getCode());
                source.setState(YuqingState.FETCHED_COMMENT.getState());
                DaoFactory.yuqingDao.updateWeixin(source, new YuqingDocMapper());
                return;
            } else {
                httpResult.setUrl(source.getUrl());
            }
            //解析文章信息
            YuqingArticle article = ParseFactory.weixinPageParser.parse(httpResult);
            if (article == null) {
                source.setState(YuqingState.FETCHED_COMMENT.getState());
                DaoFactory.yuqingDao.updateWeixin(source, new YuqingDocMapper());
            } else {
                article.setState(YuqingState.FETCHED_COMMENT.getState());
                DaoFactory.yuqingDao.updateWeixin(article, new YuqingDocMapper());
                DaoFactory.yuqingDao.saveComments(article.getComments(), new YuqingCommentDocMapper(), source.getYuqing_id(),source.getBeidou_id());
            }
        } catch (Exception e) {
            log.error("爬取或者解析失败,url=[" + source.getUrl() + "]");
            source.setState(YuqingState.FETCHED_COMMENT.getState());
            DaoFactory.yuqingDao.updateWeixin(source, new YuqingDocMapper());
        }
    }


}

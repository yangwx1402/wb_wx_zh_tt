package com.letv.sf.job.thread.weixin;

import com.letv.sf.config.SpiderConfig;
import com.letv.sf.dao.DaoFactory;
import com.letv.sf.dao.yuqing.mapper.YuqingDocMapper;
import com.letv.sf.entity.beidou.BeidouEntity;
import com.letv.sf.entity.common.CrawlerResultEntity;
import com.letv.sf.entity.yuqing.YuqingArticle;
import com.letv.sf.http.HttpResult;
import com.letv.sf.mq.MessageFactory;
import com.letv.sf.parser.Parse;
import com.letv.sf.crawler.Crawl;
import org.apache.log4j.Logger;

import java.util.List;

/**
 * Created by yangyong3 on 2016/12/9.
 */
public abstract class BaseYuqing {

    private static final Logger log = Logger.getLogger(BaseYuqing.class);
    private static final int default_seed = 10;
    /**
     * 微信关键词搜索
     *
     * @param queue    关键词所在的队列名称
     * @param base_url 搜索url
     * @param crawl    抓取器对象
     * @param parse    解析器对象
     * @param limit    搜索翻页限制
     */
    protected void search(String queue, String base_url, Crawl crawl, Parse<CrawlerResultEntity<YuqingArticle>, BeidouEntity> parse, int limit) {
        while (true) {
            BeidouEntity entity = null;
            HttpResult<BeidouEntity> httpResult = null;
            CrawlerResultEntity<YuqingArticle> rs = null;
            List<YuqingArticle> articles = null;
            //搜索的时候进行翻页
            for (int i = 1; i <= limit; i++) {
                try {
                    entity = MessageFactory.sub(queue, BeidouEntity.class);
                    if (entity == null)
                        break;
                    String url = base_url + "&page=" + i + "&query=" + entity.getTag();
                    httpResult = crawl.crawl(url, null);
                    if (httpResult == null) {
                        log.error("抓取url出错，url is " + url + ",statusCode=" + httpResult.getCode());
                        return;
                    } else {
                        httpResult.setMeta(entity);
                        httpResult.setUrl(url);
                    }
                    rs = parse.parse(httpResult);
                    articles = rs.getItems();
                    DaoFactory.yuqingDao.saveWeixinList(articles, new YuqingDocMapper());
                    Thread.sleep(SpiderConfig.getSleepTimes(default_seed));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            try {
                Thread.sleep(SpiderConfig.getSleepTimes(default_seed));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            try {
                Thread.sleep(SpiderConfig.getSleepTimes(default_seed));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}

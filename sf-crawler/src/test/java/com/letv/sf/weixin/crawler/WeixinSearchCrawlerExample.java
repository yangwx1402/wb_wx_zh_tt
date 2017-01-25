package com.letv.sf.weixin.crawler;

import com.letv.sf.dao.yuqing.mapper.YuqingDocMapper;
import com.letv.sf.dao.yuqing.YuqingDao;
import com.letv.sf.dao.yuqing.YuqingDaoByMongo;
import com.letv.sf.entity.beidou.BeidouEntity;
import com.letv.sf.entity.common.CrawlerResultEntity;
import com.letv.sf.entity.yuqing.YuqingArticle;
import com.letv.sf.http.HttpResult;
import com.letv.sf.parser.Parse;
import com.letv.sf.parser.weixin.WeixinSearchParser;
import com.letv.sf.crawler.Crawl;
import com.letv.sf.crawler.weixin.WeixinSearchCrawler;

import java.util.List;

/**
 * Created by yangyong3 on 2016/12/5.
 */
public class WeixinSearchCrawlerExample {
    public static void main(String[] args) throws Exception {
        String url = "http://weixin.sogou.com/weixin?type=2&query=%E6%98%A5%E6%99%9A&ie=utf8";
        Crawl crawl = new WeixinSearchCrawler();
        HttpResult<BeidouEntity> result = crawl.crawl(url,null);
        BeidouEntity beidou = new BeidouEntity();
        beidou.setBeidou_id(291701001l);
        beidou.setEvent_name("春晚");
        beidou.setEvent_name("春晚");
        beidou.setTag("春晚");
        result.setMeta(beidou);
        Parse<CrawlerResultEntity<YuqingArticle>,BeidouEntity> parse = new WeixinSearchParser();
        CrawlerResultEntity<YuqingArticle> rs = parse.parse(result);
        List<YuqingArticle> articles = rs.getItems();
        YuqingDao dao = new YuqingDaoByMongo();
        dao.saveWeixinList(articles,new YuqingDocMapper());
    }
}

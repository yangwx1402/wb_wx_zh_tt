package com.letv.sf.toutiao.crawler;

import com.letv.sf.config.SpiderConfig;
import com.letv.sf.crawler.Crawl;
import com.letv.sf.crawler.toutiao.ToutiaoKeywordSearchCrawler;
import com.letv.sf.dao.toutiao.ToutiaoDao;
import com.letv.sf.dao.toutiao.ToutiaoDaoByMongo;
import com.letv.sf.entity.beidou.BeidouEntity;
import com.letv.sf.entity.common.CrawlerResultEntity;
import com.letv.sf.http.HttpResult;
import com.letv.sf.parser.Parse;
import com.letv.sf.parser.toutiao.ToutiaoKeywordSearchParser;

import java.util.Map;

/**
 * Created by yangyong3 on 2017/1/5.
 */
public class ToutiaoTest {
    public static void main(String[] args) throws Exception {
        BeidouEntity beidou = new BeidouEntity();
        beidou.setBeidou_id(291701001l);
        beidou.setEvent_name("2017央视春晚");
        beidou.setTag("春晚");
        String url = SpiderConfig.getToutiaoKeywordSearchUrl() + "&offset=0&count=20&keyword="+beidou.getEvent_name();
        System.out.println(url);
        Crawl crawl = new ToutiaoKeywordSearchCrawler();
        HttpResult<BeidouEntity> httpResult = crawl.crawl(url, null);
        httpResult.setMeta(beidou);
        Parse<CrawlerResultEntity<Map<String, Object>>, BeidouEntity> parse = new ToutiaoKeywordSearchParser();

        CrawlerResultEntity<Map<String, Object>> data = parse.parse(httpResult);
        ToutiaoDao dao = new ToutiaoDaoByMongo();
        dao.saveToutiaoArticle(data.getItems());
    }
}

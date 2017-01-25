package com.letv.sf.crawler;

import com.letv.sf.crawler.weibo.TopicSearchCrawler;
import com.letv.sf.entity.common.CrawlerResultEntity;
import com.letv.sf.http.HttpResult;
import com.letv.sf.parser.weibo.TopicSearchParser;
import weibo4j.model.Status;

import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by yangyong3 on 2016/11/30.
 */
public class TopicSearchCrawlerExample {


    public static void main(String[] args) throws Exception {
        final String TOPIC_SEARCH_URL = "https://api.weibo.com/2/search/topics.json?source=140226478&q="+ URLEncoder.encode("春晚","utf-8");
        TopicSearchCrawler crawler = new TopicSearchCrawler();
        TopicSearchParser parser = new TopicSearchParser();
        Map<String,String> map = new HashMap<String,String>();
        HttpResult result = crawler.crawl(TOPIC_SEARCH_URL,map);
        System.out.println(result.getContent());
        CrawlerResultEntity<Status> rs = parser.parse(result);
        System.out.println(rs.getItems().get(0).getText());
//        WeiboDao dao = new WeiboDaoByMysql();
//        for(Status status:rs.getItems()){
//            status.setType("topic");
//            status.setKeyword("春晚");
//            dao.saveTopic(status);
//        }
    }
}

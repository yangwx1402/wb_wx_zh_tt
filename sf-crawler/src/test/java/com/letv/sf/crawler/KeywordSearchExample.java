package com.letv.sf.crawler;

import com.letv.sf.crawler.weibo.KeywordSearchCrawler;
import com.letv.sf.entity.beidou.BeidouEntity;
import com.letv.sf.entity.common.CrawlerResultEntity;
import com.letv.sf.http.HttpResult;
import com.letv.sf.parser.weibo.KeywordSearchParser;
import com.letv.sf.parser.Parse;
import org.apache.commons.io.FileUtils;
import weibo4j.model.Status;

import java.io.File;

/**
 * Created by yangyong3 on 2016/11/30.
 */
public class KeywordSearchExample {
    public static void main(String[] args) throws Exception {
        Crawl crawl = new KeywordSearchCrawler();
        String url = "http://s.weibo.com/weibo/%25E6%2598%25A5%25E6%2599%259A?topnav=1&wvr=6&b=1";
        HttpResult result = crawl.crawl(url, null);
        FileUtils.writeStringToFile(new File("E:\\test.html"),result.getContent(),"utf-8");
        Parse<CrawlerResultEntity<Status>,BeidouEntity> parse = new KeywordSearchParser();
        CrawlerResultEntity<Status> rs = parse.parse(result);
        System.out.println("---"+rs.getItems().size());
    }
}

package com.letv.sf.crawler;

import com.letv.sf.crawler.weibo.WeiboTraceCrawler;
import com.letv.sf.entity.beidou.BeidouEntity;
import com.letv.sf.entity.common.CrawlerResultEntity;
import com.letv.sf.entity.weibo.BeidouMapping;
import com.letv.sf.entity.weibo.WeiboTrace;
import com.letv.sf.http.HttpResult;
import com.letv.sf.parser.Parse;
import com.letv.sf.parser.weibo.WeiboTraceParser;

/**
 * Created by yangyong3 on 2016/12/2.
 */
public class WeiboTraceExample {
    public static void main(String[] args) throws Exception {
       Crawl crawl = new WeiboTraceCrawler();
        String url = "https://api.weibo.com/2/statuses/count.json?ids=3965590052011839,3971345576309613,3971416297175098&source=140226478";
        HttpResult result = crawl.crawl(url,null);
        Parse<CrawlerResultEntity<WeiboTrace>,BeidouMapping> parse = new WeiboTraceParser();
        CrawlerResultEntity<WeiboTrace> resultEntity = parse.parse(result);
        System.out.println(resultEntity.getItems().size());
    }
}

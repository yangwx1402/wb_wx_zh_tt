package com.letv.sf.baidu.crawler;

import com.letv.sf.crawler.Crawl;
import com.letv.sf.crawler.baidu.BaiduPostCrawler;
import com.letv.sf.entity.baidu.BaiduPostEntity;
import com.letv.sf.entity.beidou.BeidouEntity;
import com.letv.sf.http.HttpResult;
import com.letv.sf.parser.Parse;
import com.letv.sf.parser.baidu.BaiduPostParser;

/**
 * Created by yangyong3 on 2016/12/12.
 */
public class BaiduPostCrawlerExample {
    public static void main(String[] args) throws Exception {
        String url = "http://tieba.baidu.com/f?kw=%E7%9B%B4%E9%80%9A%E6%98%A5%E6%99%9A&ie=utf-8";
        BeidouEntity beidou = new BeidouEntity();
        beidou.setBeidou_id(291701001l);
        beidou.setEvent_name("春晚");
        beidou.setEvent_name("春晚");
        beidou.setTag("春晚");
        Crawl crawl = new BaiduPostCrawler();
        HttpResult<BeidouEntity> httpResult = crawl.crawl(url,null);
        httpResult.setMeta(beidou);
        Parse<BaiduPostEntity,BeidouEntity> parser = new BaiduPostParser();
        BaiduPostEntity entity = parser.parse(httpResult);
        System.out.println(entity.getMdata().getName());
    }
}

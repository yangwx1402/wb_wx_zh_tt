package com.letv.sf.zhihu.crawler;

import com.letv.sf.crawler.Crawl;
import com.letv.sf.crawler.zhihu.ZhihuPageCrawler;
import com.letv.sf.entity.beidou.BeidouEntity;
import com.letv.sf.entity.common.CrawlerResultEntity;
import com.letv.sf.entity.yuqing.YuqingArticle;
import com.letv.sf.entity.zhihu.ZhihuQuestionEntity;
import com.letv.sf.http.HttpResult;
import com.letv.sf.parser.Parse;
import com.letv.sf.parser.zhihu.ZhihuPageParser;

/**
 * Created by yangyong3 on 2016/12/9.
 */
public class ZhihuPageExample {
    public static void main(String[] args) throws Exception {
        String url = "https://www.zhihu.com/question/40208287";
        BeidouEntity beidou = new BeidouEntity();
        beidou.setBeidou_id(291701001l);
        beidou.setEvent_name("春晚");
        beidou.setEvent_name("春晚");
        beidou.setTag("春晚");
        Crawl crawl = new ZhihuPageCrawler();
        HttpResult httpResult = crawl.crawl(url, null);
        httpResult.setUrl(url);
        httpResult.setMeta(beidou);
        Parse<ZhihuQuestionEntity, BeidouEntity> parse = new ZhihuPageParser();
        parse.parse(httpResult);

    }
}

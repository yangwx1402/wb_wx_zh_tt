package com.letv.sf.crawler;

import com.letv.sf.crawler.baidu.BaiduPostCrawler;
import com.letv.sf.crawler.toutiao.ToutiaoKeywordSearchCrawler;
import com.letv.sf.crawler.toutiao.ToutiaoPageCommentCrawler;
import com.letv.sf.crawler.weibo.*;
import com.letv.sf.crawler.weixin.WeixinPageCrawler;
import com.letv.sf.crawler.weixin.WeixinSearchCrawler;
import com.letv.sf.crawler.zhihu.ZhihuPageCrawler;
import com.letv.sf.crawler.zhihu.ZhihuSearchCrawler;
import com.letv.sf.crawler.zhihu.ZhihuTopicCrawler;

/**
 * Created by yangyong3 on 2016/12/1.
 */
public class CrawlerFactory {

    public static final Crawl keyWordCrawler = new KeywordSearchCrawler();

    public static final Crawl topicCrawler = new TopicSearchCrawler();

    public static final Crawl weiboUserCrawler = new WeiboUserCrawler();

    public static final Crawl weiboCommentCrawler = new WeiboCommentCrawler();

    public static final Crawl weiboTraceCrawler = new WeiboTraceCrawler();

    public static final Crawl weixinSearchCrawler = new WeixinSearchCrawler();

    public static final Crawl weixinPageCrawler = new WeixinPageCrawler();

    public static final Crawl zhihuSearchCrawler = new ZhihuSearchCrawler();

    public static final Crawl zhihuTopicCrawler = new ZhihuTopicCrawler();

    public static final Crawl zhihuPageCrawler = new ZhihuPageCrawler();

    public static final Crawl baiduPostCrawler = new BaiduPostCrawler();

    public static final Crawl toutiaoSearcher = new ToutiaoKeywordSearchCrawler();

    public static final Crawl toutiaoPageCrawler = new ToutiaoPageCommentCrawler();
}

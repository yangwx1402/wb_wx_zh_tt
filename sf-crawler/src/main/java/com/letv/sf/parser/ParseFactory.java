package com.letv.sf.parser;

import com.letv.sf.entity.baidu.BaiduPostEntity;
import com.letv.sf.entity.beidou.BeidouEntity;
import com.letv.sf.entity.common.CrawlerResultEntity;
import com.letv.sf.entity.toutiao.ToutiaoArticle;
import com.letv.sf.entity.weibo.BeidouMapping;
import com.letv.sf.entity.weibo.WeiboTrace;
import com.letv.sf.entity.yuqing.YuqingArticle;
import com.letv.sf.entity.yuqing.ZhihuPageEntity;
import com.letv.sf.entity.zhihu.ZhihuQuestionEntity;
import com.letv.sf.parser.baidu.BaiduPostParser;
import com.letv.sf.parser.toutiao.ToutiaoKeywordSearchParser;
import com.letv.sf.parser.toutiao.ToutiaoPageCommentParser;
import com.letv.sf.parser.weibo.*;
import com.letv.sf.parser.weixin.WeixinPageParser;
import com.letv.sf.parser.weixin.WeixinSearchParser;
import com.letv.sf.parser.zhihu.ZhihuPageParser;
import com.letv.sf.parser.zhihu.ZhihuSearchParser;
import com.letv.sf.parser.zhihu.ZhihuTopicParser;
import weibo4j.model.Comment;
import weibo4j.model.Status;
import weibo4j.model.User;

import java.util.Map;

/**
 * Created by yangyong3 on 2016/12/1.
 */
public class ParseFactory {
    public static final Parse<CrawlerResultEntity<Status>, BeidouEntity> keywordParser = new KeywordSearchParser();

    public static final Parse<CrawlerResultEntity<Status>, BeidouEntity> topicParser = new TopicSearchParser();

    public static final Parse<User, BeidouEntity> weiboUserParser = new WeiboUserParser();

    public static final Parse<CrawlerResultEntity<WeiboTrace>, BeidouMapping> weiboTraceParser = new WeiboTraceParser();

    public static final Parse<CrawlerResultEntity<Comment>, BeidouEntity> weiboCommentParser = new WeiboCommentParser();

    public static final Parse<CrawlerResultEntity<YuqingArticle>, BeidouEntity> weixinSearchParser = new WeixinSearchParser();

    public static final Parse<YuqingArticle, BeidouEntity> weixinPageParser = new WeixinPageParser();

    public static final Parse<CrawlerResultEntity<YuqingArticle>, BeidouEntity> zhihuSearchParser = new ZhihuSearchParser();

    public static final Parse<ZhihuPageEntity, BeidouEntity> zhihuTopicParser = new ZhihuTopicParser();

    public static final Parse<ZhihuQuestionEntity, BeidouEntity> zhihuPageParser = new ZhihuPageParser();

    public static final Parse<BaiduPostEntity,BeidouEntity> baiduPostParser = new BaiduPostParser();

    public static final Parse<CrawlerResultEntity<Map<String, Object>>, BeidouEntity> toutiaoSearchParser = new ToutiaoKeywordSearchParser();

    public static final Parse<ToutiaoArticle, ToutiaoArticle> toutiaoPageCommentParser = new ToutiaoPageCommentParser();
}

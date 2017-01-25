package com.letv.sf.zhihu.crawler;

import com.letv.sf.crawler.zhihu.ZhihuTopicCrawler;
import com.letv.sf.dao.DaoFactory;
import com.letv.sf.dao.yuqing.mapper.YuqingDocMapper;
import com.letv.sf.dao.yuqing.mapper.YuqingMdataDocMapper;
import com.letv.sf.entity.beidou.BeidouEntity;
import com.letv.sf.entity.yuqing.ZhihuPageEntity;
import com.letv.sf.http.HttpResult;
import com.letv.sf.parser.Parse;
import com.letv.sf.crawler.Crawl;
import com.letv.sf.parser.zhihu.ZhihuTopicParser;

/**
 * Created by yangyong3 on 2016/12/8.
 */
public class ZhihuSearchExample {
    public static void main(String[] args) throws Exception {
        String url = "https://www.zhihu.com/topic/20038208/top-answers";
        BeidouEntity beidou = new BeidouEntity();
        beidou.setBeidou_id(291701001l);
        beidou.setEvent_name("春晚");
        beidou.setEvent_name("春晚");
        beidou.setTag("春晚");
        Crawl crawl = new ZhihuTopicCrawler();
        HttpResult httpResult = crawl.crawl(url,null);
        httpResult.setMeta(beidou);
        Parse parse = new ZhihuTopicParser();
        ZhihuPageEntity entity = (ZhihuPageEntity) parse.parse(httpResult);
        System.out.println(entity.getQuestions().getItems().size());
        DaoFactory.yuqingDao.saveOrUpdate(entity.getYuqingMdata(),new YuqingMdataDocMapper());
        DaoFactory.yuqingDao.saveWeixinList(entity.getQuestions().getItems(),new YuqingDocMapper());
    }
}

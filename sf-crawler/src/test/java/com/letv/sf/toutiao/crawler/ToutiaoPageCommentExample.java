package com.letv.sf.toutiao.crawler;

import com.letv.sf.crawler.CrawlerFactory;
import com.letv.sf.entity.toutiao.ToutiaoArticle;
import com.letv.sf.http.HttpResult;
import com.letv.sf.parser.ParseFactory;

/**
 * Created by yangyong3 on 2017/1/5.
 */
public class ToutiaoPageCommentExample {
    public static void main(String[] args) throws Exception {
        ToutiaoArticle article = new ToutiaoArticle();
        article.setBeidou_id(291701001l);
        article.setGroup(6361564855732994305l);
        article.setId(6361564855732994305l);
        article.setState(0);
        article.setUrl("http://toutiao.com/group/6361564855732994305/");

        HttpResult<ToutiaoArticle> httpResult = CrawlerFactory.toutiaoPageCrawler.crawl(article.getUrl(),null);
        httpResult.setMeta(article);
        article = ParseFactory.toutiaoPageCommentParser.parse(httpResult);
        System.out.println(article.getText());
        System.out.println(article.getComments().size());
    }
}

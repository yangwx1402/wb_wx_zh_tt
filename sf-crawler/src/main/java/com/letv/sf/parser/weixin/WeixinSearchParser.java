package com.letv.sf.parser.weixin;

import com.letv.sf.config.SpiderConfig;
import com.letv.sf.config.YuqingState;
import com.letv.sf.config.YuqingType;
import com.letv.sf.entity.beidou.BeidouEntity;
import com.letv.sf.entity.common.CrawlerResultEntity;
import com.letv.sf.entity.common.SpiderStatus;
import com.letv.sf.entity.yuqing.YuqingArticle;
import com.letv.sf.http.HttpResult;
import com.letv.sf.parser.Parse;
import com.letv.sf.utils.*;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by yangyong3 on 2016/12/5.
 * 微信搜索也解析
 */
public class WeixinSearchParser implements Parse<CrawlerResultEntity<YuqingArticle>,BeidouEntity> {

    public CrawlerResultEntity<YuqingArticle> parse(HttpResult<BeidouEntity> httpPage) throws Exception {
        Document document = JsoupUtils.getDocument(httpPage.getContent());
        CrawlerResultEntity<YuqingArticle> result = new CrawlerResultEntity<YuqingArticle>();
        Elements articlesElement = document.select("div.txt-box");
        List<YuqingArticle> articles = new ArrayList<YuqingArticle>();
        YuqingArticle article = null;
        if (articlesElement != null && articlesElement.size() > 0) {
            for (int i = 0; i < articlesElement.size(); i++) {
                article = mapperWeixinArticle(articlesElement.get(i));
                article.setBeidou_id(httpPage.getMeta().getBeidou_id());
                article.setSearchTag(httpPage.getMeta().getTag());
                article.setBeidou_name(httpPage.getMeta().getEvent_name());
                articles.add(article);
            }
        }
        result.setItems(articles);
        result.setStatus(SpiderStatus.SUCCESS);
        return result;
    }

    private YuqingArticle mapperWeixinArticle(Element element) {
        YuqingArticle article = new YuqingArticle();
        String title = element.select("h3").text();
        article.setTitle(RegUtils.wordNumChar(title).trim());
        Element contentElement = JsoupUtils.getFirstElementByAttribute(element, "class", "txt-info");
        if (contentElement != null) {
            article.setDescription(contentElement.text());
        }
        Element readCountElement = JsoupUtils.selectFirstElement(element, "span.s1");
        if (readCountElement != null) {
            article.setReadCount(Integer.parseInt(readCountElement.text().replaceAll("\\+","")));
        }
        Element accountElement = JsoupUtils.getFirstElementByAttribute(element, "class", "account");
        if (accountElement != null) {
            article.setAuthor(accountElement.text().trim());
        }
        Element timeElement = JsoupUtils.selectFirstElement(element, "div.s-p");
        if (timeElement != null) {
            long time = System.currentTimeMillis() - Long.parseLong(timeElement.attr("t"));
            article.setCreated_at(DateUtils.dateString(new Date(time), SpiderConfig.created_time_format));
            article.setCreated_long(time);
        }
        Element urlElement = JsoupUtils.selectFirstElement(element,"a");
        if(urlElement!=null){
            //这个是临时链接
            article.setUrl(urlElement.attr("href"));
            article.setMd5(MD5.md5(article.getAuthor()+"_"+article.getTitle()));
        }
        article.setSource(YuqingType.WEIXIN.getName());
        article.setYuqing_id(article.getSource()+"_"+article.getMd5());
        article.setState(YuqingState.NO_FETCH_COMMENT.getState());
        return article;
    }
}

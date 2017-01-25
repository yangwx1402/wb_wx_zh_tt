package com.letv.sf.parser.zhihu;

import com.letv.sf.config.SpiderConfig;
import com.letv.sf.config.YuqingType;
import com.letv.sf.entity.beidou.BeidouEntity;
import com.letv.sf.entity.common.CrawlerResultEntity;
import com.letv.sf.entity.yuqing.YuqingArticle;
import com.letv.sf.entity.yuqing.YuqingMdata;
import com.letv.sf.entity.yuqing.ZhihuPageEntity;
import com.letv.sf.http.HttpResult;
import com.letv.sf.parser.Parse;
import com.letv.sf.utils.DateUtils;
import com.letv.sf.utils.JsoupUtils;
import com.letv.sf.utils.MD5;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by yangyong3 on 2016/12/9.
 * 知乎话题页解析
 */
public class ZhihuTopicParser implements Parse<ZhihuPageEntity, BeidouEntity> {
    public ZhihuPageEntity parse(HttpResult<BeidouEntity> httpPage) throws Exception {
        ZhihuPageEntity zhihuPageEntity = new ZhihuPageEntity();
        CrawlerResultEntity<YuqingArticle> result = new CrawlerResultEntity<YuqingArticle>();
        Document document = Jsoup.parse(httpPage.getContent());
        YuqingMdata mdata = parseMdata(document, httpPage.getMeta());
        List<YuqingArticle> questions = parseQuestions(document, httpPage.getMeta());
        result.setItems(questions);
        zhihuPageEntity.setQuestions(result);
        zhihuPageEntity.setYuqingMdata(mdata);
        return zhihuPageEntity;
    }

    private List<YuqingArticle> parseQuestions(Document document, BeidouEntity meta) {
        List<YuqingArticle> articles = new ArrayList<YuqingArticle>();
        Elements contentElements = document.select("div.content");
        if (contentElements == null || contentElements.size() == 0)
            return articles;
        Element contentElement = null;
        YuqingArticle article = null;
        for (int i = 0; i < contentElements.size(); i++) {
            contentElement = contentElements.get(i);
            article = parseArticle(contentElement, meta);
            if (article != null)
                articles.add(article);
        }
        return articles;
    }

    private YuqingArticle parseArticle(Element contentElement, BeidouEntity meta) {
        YuqingArticle article = new YuqingArticle();
        Element timeElement = JsoupUtils.selectFirstElement(contentElement, "span.time");
        Element questionLinkElement = JsoupUtils.selectFirstElement(contentElement, "a.question_link");
        if (questionLinkElement != null) {
            String url = "https://www.zhihu.com" + questionLinkElement.attr("href");
            String title = questionLinkElement.text();
            article.setUrl(url);
            article.setTitle(title);
        } else
            return null;
        if (timeElement != null) {
            String timestamp = timeElement.attr("data-timestamp");
            long time = Long.parseLong(timestamp);
            article.setCreated_at(DateUtils.dateString(new Date(time), SpiderConfig.created_time_format));
            article.setCreated_long(time);
        }

        article.setMd5(MD5.md5(article.getUrl()));
        article.setSource(YuqingType.ZHIHU.getName());
        article.setYuqing_id(article.getSource() + "_" + article.getMd5());
        article.setSearchTag(meta.getTag());
        article.setBeidou_id(meta.getBeidou_id());
        article.setBeidou_name(meta.getEvent_name());
        article.setYqid_answer(article.getYuqing_id());
        article.setInsert_time(DateUtils.dateString(new Date(),SpiderConfig.insert_update_time_format));
        article.setUpdate_time(DateUtils.dateString(new Date(),SpiderConfig.insert_update_time_format));
        return article;
    }

    private YuqingMdata parseMdata(Document document, BeidouEntity beidouEntity) {
        YuqingMdata mdata = new YuqingMdata();
        Element guanzhu = JsoupUtils.selectFirstElement(document, "div#zh-topic-side-head");
        Element guanzhuCount = JsoupUtils.selectFirstElement(guanzhu, "strong");
        mdata.setBeidou_id(beidouEntity.getBeidou_id());
        mdata.setBeidou_name(beidouEntity.getEvent_name());
        mdata.setSource(YuqingType.ZHIHU.getName());
        Element nameElement = JsoupUtils.selectFirstElement(document, "img.zm-avatar-editor-preview");
        if (nameElement != null) {
            mdata.setName(nameElement.attr("alt"));
        }
        Element urlElement = JsoupUtils.selectFirstElement(document, "a#zh-avartar-edit-form.zm-entry-head-avatar-link");
        if (urlElement != null) {
            String url = urlElement.attr("href");
            mdata.setId(url.replaceAll("/", "").replaceAll("topic", ""));
        }
        if (guanzhuCount != null) {
            mdata.setAttention_user(Integer.parseInt(guanzhuCount.text()));
        }
        mdata.setYqpid(mdata.getSource() + "_" + mdata.getId());
        mdata.setInsert_time(DateUtils.dateString(new Date(),SpiderConfig.insert_update_time_format));
        mdata.setInsert_time_long(System.currentTimeMillis());
        mdata.setUpdate_time(DateUtils.dateString(new Date(),SpiderConfig.insert_update_time_format));
        return mdata;
    }
}

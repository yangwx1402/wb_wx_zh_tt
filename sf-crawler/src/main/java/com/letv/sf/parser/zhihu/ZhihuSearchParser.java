package com.letv.sf.parser.zhihu;

import com.letv.sf.config.YuqingType;
import com.letv.sf.entity.beidou.BeidouEntity;
import com.letv.sf.entity.common.CrawlerResultEntity;
import com.letv.sf.entity.yuqing.YuqingArticle;
import com.letv.sf.http.HttpResult;
import com.letv.sf.parser.Parse;
import com.letv.sf.utils.JsoupUtils;
import com.letv.sf.utils.MD5;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yangyong3 on 2016/12/8.
 * 知乎搜索也解析,通过sogou搜索
 */
public class ZhihuSearchParser extends ZhihuBaseParse implements Parse<CrawlerResultEntity<YuqingArticle>, BeidouEntity> {
    public CrawlerResultEntity<YuqingArticle> parse(HttpResult<BeidouEntity> httpPage) throws Exception {
        Document document = Jsoup.parse(httpPage.getContent());
        CrawlerResultEntity<YuqingArticle> result = new CrawlerResultEntity<YuqingArticle>();
        Elements articleElements = document.select("div.result-about-list");
        Element articleElement = null;
        List<YuqingArticle> articles = new ArrayList<YuqingArticle>();
        YuqingArticle article = null;
        if (articleElements == null || articleElements.size() == 0) return null;
        for (int i = 0; i < articleElements.size(); i++) {
            articleElement = articleElements.get(i);
            article = parse(articleElement);
            article.setBeidou_id(httpPage.getMeta().getBeidou_id());
            article.setSearchTag(httpPage.getMeta().getTag());
            articles.add(article);
        }
        result.setItems(articles);
        return result;
    }

    private YuqingArticle parse(Element articleElement) {
        YuqingArticle article = new YuqingArticle();
        Element titleElement = JsoupUtils.selectFirstElement(articleElement, "h4.about-list-title");
        String title = titleElement.text();
        article.setTitle(title);
        Element authorElement = JsoupUtils.selectFirstElement(articleElement, "span.count");
        int attitudes_count = findAttitudeCount(authorElement.text());
        article.setAttitudes_count(attitudes_count);

        Element authorInfoElement = JsoupUtils.selectFirstElement(articleElement, "p.about-answer");
        String authorInfo = authorInfoElement.html();
        article.setAuthorInfo(authorInfo);
        Element authorNameElement = JsoupUtils.selectFirstElement(authorInfoElement, "a");
        if(authorNameElement!=null) {
            String author = authorNameElement.text();
            article.setAuthor(author);
        }
        Element descElement = JsoupUtils.selectFirstElement(articleElement, "p.about-text");
        String desc = descElement.text();
        article.setDescription(desc);
        Element footDiv = JsoupUtils.selectFirstElement(articleElement, "div.about-lable");
        Element answerElement = JsoupUtils.selectFirstElement(footDiv, "a");
        String url = answerElement.attr("href");
        article.setUrl(url);
        int answer_count = findAnswerCount(answerElement.text());
        article.setReplyCount(answer_count);
        Element tagElement = JsoupUtils.selectFirstElement(footDiv, "cite");
        if (tagElement != null) {
            Elements tagsElements = tagElement.select("a");
            for (int i = 0; i < tagsElements.size(); i++) {
                if (article.getTag() == null)
                    article.setTag(tagsElements.get(i).text());
                else
                    article.setTag(article.getTag()+","+tagsElements.get(i).text());
            }
        }
        article.setMd5(MD5.md5(article.getUrl()));
        article.setSource(YuqingType.ZHIHU.getName());
        article.setYuqing_id(article.getSource()+"_"+article.getMd5());
        if(article.getAuthor()==null)
            article.setAuthor("知乎用户");
        return article;
    }


}

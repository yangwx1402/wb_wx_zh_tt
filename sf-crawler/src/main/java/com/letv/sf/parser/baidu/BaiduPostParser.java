package com.letv.sf.parser.baidu;

import com.letv.sf.config.SpiderConfig;
import com.letv.sf.config.YuqingState;
import com.letv.sf.config.YuqingType;
import com.letv.sf.entity.baidu.BaiduPostEntity;
import com.letv.sf.entity.beidou.BeidouEntity;
import com.letv.sf.entity.yuqing.YuqingArticle;
import com.letv.sf.entity.yuqing.YuqingMdata;
import com.letv.sf.http.HttpResult;
import com.letv.sf.parser.Parse;
import com.letv.sf.utils.DateUtils;
import com.letv.sf.utils.JsoupUtils;
import com.letv.sf.utils.MD5;
import org.apache.commons.collections.CollectionUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by yangyong3 on 2016/12/12.
 * 百度贴吧解析
 */
public class BaiduPostParser implements Parse<BaiduPostEntity, BeidouEntity> {
    public BaiduPostEntity parse(HttpResult<BeidouEntity> httpPage) throws Exception {
        Document document = Jsoup.parse(httpPage.getContent());
        BaiduPostEntity entity = new BaiduPostEntity();
        YuqingMdata mdata = new YuqingMdata();
        //解析title
        Elements titleElement = JsoupUtils.selectElements(document, "a.card_title_fname");
        if (titleElement != null) {
            mdata.setName(titleElement.text());
        }
        //解析关注
        Element guanzhuElement = JsoupUtils.selectFirstElement(document, "span.card_menNum");
        if (guanzhuElement != null) {
            mdata.setAttention_user(trasfer(guanzhuElement.text()));
        }
        //解析帖子数
        Element postCountElement = JsoupUtils.selectFirstElement(document, "span.card_infoNum");
        if (postCountElement != null) {
            mdata.setPosts(trasfer(postCountElement.text()));
        }
        mdata.setBeidou_id(httpPage.getMeta().getBeidou_id());
        mdata.setBeidou_name(httpPage.getMeta().getEvent_name());
        mdata.setId(MD5.md5(mdata.getName()));
        mdata.setInsert_time(DateUtils.dateString(new Date(), SpiderConfig.insert_update_time_format));
        mdata.setInsert_time_long(System.currentTimeMillis());
        mdata.setSource(YuqingType.BAIDU.getName());
        mdata.setUpdate_time(DateUtils.dateString(new Date(), SpiderConfig.insert_update_time_format));
        mdata.setYqpid(mdata.getSource() + "_" + mdata.getId());
        entity.setMdata(mdata);
        //解析帖子信息
        Elements postElements = document.select("div.t_con.cleafix");
        List<YuqingArticle> articles = parsePosts(postElements,httpPage.getMeta());
        if (!CollectionUtils.isEmpty(articles)) {
            entity.setPosts(articles);
        }
        return entity;
    }

    private List<YuqingArticle> parsePosts(Elements postElements,BeidouEntity beidou) {
        if (postElements == null || postElements.size() == 0)
            return null;
        List<YuqingArticle> articles = new ArrayList<YuqingArticle>();
        Element postElement = null;
        YuqingArticle article = null;
        for (int i = 0; i < postElements.size(); i++) {
            postElement = postElements.get(i);
            article = parseArticle(postElement,beidou);
            if (article != null)
                articles.add(article);
        }
        return articles;

    }

    private YuqingArticle parseArticle(Element postElement,BeidouEntity beidou) {
        YuqingArticle article = new YuqingArticle();
        Element replyElement = JsoupUtils.selectFirstElement(postElement, "span.threadlist_rep_num.center_text");
        if (replyElement != null) {
            article.setReplyCount(Integer.parseInt(replyElement.text()));
        }

        Element descElement = JsoupUtils.selectFirstElement(postElement, "div.threadlist_abs.threadlist_abs_onlyline");
        if (descElement != null) {
            article.setDescription(descElement.text());
        }

        Element titleElement = JsoupUtils.selectFirstElement(postElement, "div.threadlist_title.pull_left.j_th_tit");
        if (titleElement != null) {
            article.setTitle(titleElement.text());
            Element urlElement = JsoupUtils.selectFirstElement(titleElement,"a");
            article.setUrl("http://tieba.baidu.com"+urlElement.attr("href"));
            article.setMd5(MD5.md5(article.getUrl()));
        }
        Element authorElement = JsoupUtils.selectFirstElement(postElement, "span.frs-author-name-wrap");
        if (authorElement != null) {
            article.setAuthor(authorElement.text());
        }
        Element timeElement = JsoupUtils.selectFirstElement(postElement, "span.pull-right.is_show_create_time");
        if (timeElement != null) {
            Date date = trasferDate(timeElement.text());
            article.setCreated_at(DateUtils.dateString(date,SpiderConfig.created_time_format));
            article.setCreated_long(date.getTime());
        }

        article.setBeidou_id(beidou.getBeidou_id());
        article.setInsert_time(DateUtils.dateString(new Date(),SpiderConfig.insert_update_time_format));
        article.setUpdate_time(DateUtils.dateString(new Date(),SpiderConfig.insert_update_time_format));
        article.setSource(YuqingType.BAIDU.getName());
        article.setYuqing_id(article.getSource()+"_"+article.getMd5());
        article.setState(YuqingState.FETCHED_COMMENT.getState());
        return article;
    }

    private Integer trasfer(String text) {
        return Integer.parseInt(text.replaceAll(",", ""));
    }

    private Date trasferDate(String text) {
        int length = text.length();
        try {
            //2026-11
            if (length == 7) {
                return DateUtils.parseDate(text, "yyyy-MM");
            } else if (text.contains("-")) {
                return DateUtils.parseDate(DateUtils.dateString(new Date(), "yyyy") + "-" + text, "yyyy-MM-dd");
            } else if (text.contains(":")) {
                return DateUtils.parseDate(DateUtils.dateString(new Date(), "yyyy-MM-dd") + " " + text, "yyyy-MM-dd HH:mm");
            } else {
                return new Date();
            }
        } catch (ParseException e) {
            e.printStackTrace();
            return new Date();
        }
    }
}

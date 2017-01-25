package com.letv.sf.dao.yuqing.mapper;

import com.letv.sf.config.SpiderConfig;
import com.letv.sf.dao.mapper.MongoDocMapper;
import com.letv.sf.entity.yuqing.YuqingArticle;
import com.letv.sf.utils.DateUtils;
import org.apache.wml.dom.WMLAElementImpl;
import org.bson.Document;

import java.sql.Timestamp;
import java.util.Date;

/**
 * Created by yangyong3 on 2016/12/6.
 */
public class YuqingDocMapper implements MongoDocMapper<YuqingArticle> {
    public Document mapper(YuqingArticle weixinArticle) {
        Document document = new Document();
        document.append("yqpid", weixinArticle.getYuqing_id());
        document.append("beidou_id", weixinArticle.getBeidou_id());
        document.append("beidou_name",weixinArticle.getBeidou_name());
        document.append("yqid", weixinArticle.getMd5());
        document.append("url", weixinArticle.getUrl());
        document.append("description", weixinArticle.getDescription());
        document.append("search_tag", weixinArticle.getSearchTag());
        document.append("title", weixinArticle.getTitle());
        document.append("attitudes_count", weixinArticle.getAttitudes_count());
        document.append("body", weixinArticle.getBody());
        document.append("author", weixinArticle.getAuthor());
        document.append("publish_time", weixinArticle.getCreated_at());
        document.append("publish_time_long", weixinArticle.getCreated_long());
        document.append("read_number", weixinArticle.getReadCount());
        document.append("likes_number", weixinArticle.getAttitudes_count());
        document.append("source", weixinArticle.getSource());
        document.append("insert_time", DateUtils.dateString(new Date(), SpiderConfig.insert_update_time_format));
        document.append("state", weixinArticle.getState());
        document.append("author_info", weixinArticle.getAuthorInfo());
        document.append("reply_count", weixinArticle.getReplyCount());
        document.append("tag", weixinArticle.getTag());
        document.append("update_time", DateUtils.dateString(new Date(), SpiderConfig.insert_update_time_format));
        document.append("yqid_answer", weixinArticle.getYqid_answer());
        return document;
    }

    public YuqingArticle deMapper(Document document) {
        YuqingArticle article = new YuqingArticle();
        article.setYuqing_id(document.getString("yqpid"));
        article.setBeidou_id(document.getLong("beidou_id"));
        article.setBeidou_name(document.getString("beidou_name"));
        article.setMd5(document.getString("yqid"));
        article.setUrl(document.getString("url"));
        article.setDescription(document.getString("description"));
        article.setSearchTag(document.getString("search_tag"));
        article.setTitle(document.getString("title"));
        article.setBody(document.getString("body"));
        if (document.containsKey("attitudes_count"))
            article.setAttitudes_count(document.getInteger("attitudes_count"));
        article.setAuthor(document.getString("author"));
        if (document.getString("publish_time") != null)
            article.setCreated_at(document.getString("publish_time"));
        article.setReadCount(document.getInteger("read_number"));
        article.setAttitudes_count(document.getInteger("likes_number"));
        article.setSource(document.getString("source"));
        article.setInsert_time(document.getString("insert_time"));
        article.setCreated_long(document.getLong("publish_time_long"));
        article.setReplyCount(document.getInteger("reply_count"));
        article.setTag(document.getString("tag"));
        article.setYqid_answer(document.getString("yqid_answer"));
        return article;
    }
}

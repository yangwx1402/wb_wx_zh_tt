package com.letv.sf.dao.toutiao.mapper;

import com.letv.sf.dao.mapper.MongoDocMapper;
import com.letv.sf.entity.toutiao.ToutiaoArticle;
import org.bson.Document;

/**
 * Created by yangyong3 on 2017/1/5.
 */
public class ToutiaoArticleMapper implements MongoDocMapper<ToutiaoArticle> {
    public Document mapper(ToutiaoArticle toutiaoArticle) {
        return null;
    }

    public ToutiaoArticle deMapper(Document document) {
        ToutiaoArticle article = new ToutiaoArticle();
        article.setId(document.getLong("id"));
        article.setState(document.getInteger("state"));
        article.setBeidou_id(document.getLong("beidou_id"));
        article.setUrl(document.getString("url"));
        article.setGroup(document.getLong("group_id"));
        return article;
    }
}

package com.letv.sf.dao.yuqing;

import com.letv.sf.config.SpiderConfig;
import com.letv.sf.config.YuqingState;
import com.letv.sf.dao.AbstractDao;
import com.letv.sf.dao.MongoDataSourceFactory;
import com.letv.sf.dao.mapper.MongoDocMapper;
import com.letv.sf.entity.yuqing.YuqingArticle;
import com.letv.sf.entity.yuqing.YuqingComment;
import com.letv.sf.entity.yuqing.YuqingMdata;
import com.letv.sf.utils.DateUtils;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;
import org.bson.Document;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

/**
 * Created by yangyong3 on 2016/12/6.
 */
public class YuqingDaoByMongo extends AbstractDao implements YuqingDao {

    private static final Logger log = Logger.getLogger(YuqingDaoByMongo.class);

    public void saveWeixinList(List<YuqingArticle> articles, MongoDocMapper<YuqingArticle> mapper) {
        if (CollectionUtils.isEmpty(articles))
            return;
        MongoCollection<Document> collection = MongoDataSourceFactory.getMongoCollection(SpiderConfig.getWeixinZhihuBaiduMongoArticleCollection());
        Document find = null;
        Document update = null;
        Document temp = null;
        for (YuqingArticle article : articles) {
            update = mapper.mapper(article);
            find = new Document("yqpid", article.getYuqing_id());
            temp = collection.find(find).first();
            if (temp == null) {
                log.info("insert mongo -" + update.getString("yqpid"));
                collection.insertOne(update);
            }
        }
    }

    public void updateWeixin(YuqingArticle article, MongoDocMapper<YuqingArticle> mapper) {
        MongoCollection<Document> collection = MongoDataSourceFactory.getMongoCollection(SpiderConfig.getWeixinZhihuBaiduMongoArticleCollection());
        Document find = new Document("yqpid", article.getYuqing_id());
        Document queryResult = collection.find(find).first();
        if (queryResult == null)
            return;
        YuqingArticle temp = mapper.deMapper(queryResult);
        article.setBeidou_id(temp.getBeidou_id());
        article.setDescription(temp.getDescription());
        article.setCreated_at(temp.getCreated_at());
        article.setCreated_long(temp.getCreated_long());
        article.setSearchTag(temp.getSearchTag());
        article.setMd5(temp.getMd5());
        article.setState(YuqingState.FETCHED_COMMENT.getState());
        article.setUpdate_time(DateUtils.dateString(new Date(), SpiderConfig.insert_update_time_format));
        article.setUrl(temp.getUrl());
        article.setReplyCount(temp.getReplyCount());
        article.setTag(temp.getTag());
        Document document = mapper.mapper(article);
        log.info("update mongo -" + document.getString("yqpid"));
        collection.findOneAndReplace(find, document);
    }

    public List<YuqingArticle> getYuqingArticleByState(int state, String source, int limit, MongoDocMapper<YuqingArticle> mapper) {
        List<YuqingArticle> articles = new ArrayList<YuqingArticle>();
        MongoCollection<Document> collection = MongoDataSourceFactory.getMongoCollection(SpiderConfig.getWeixinZhihuBaiduMongoArticleCollection());
        Document find = new Document("state", state);
        find.append("source", source);
        find.append("insert_time",DateUtils.dateString(new Date(),SpiderConfig.insert_update_time_format));
        Document sort = new Document("insert_time",-1);
        FindIterable<Document> documents = collection.find(find).sort(sort).limit(limit);
        Iterator<Document> it = documents.iterator();
        while (it.hasNext()) {
            articles.add(mapper.deMapper(it.next()));
        }
        return articles;
    }

    public void saveComments(List<YuqingComment> comments, MongoDocMapper<YuqingComment> mapper, String yqpid,Long beidou_id) {
        if (CollectionUtils.isEmpty(comments))
            return;
        MongoCollection<Document> collection = MongoDataSourceFactory.getMongoCollection(SpiderConfig.getWeixinZhihuBaiduMongoCommentCollection());
        List<Document> documents = new ArrayList<Document>();
        Document find = null;
        Document temp = null;
        for (YuqingComment comment : comments) {
            if (yqpid != null)
                comment.setYqpid(yqpid);
            if(beidou_id!=null&&beidou_id>0)
                comment.setBeidou_id(beidou_id);
            find = new Document("yqrpid", comment.getYqrpid());
            temp = collection.find(find).first();
            if (temp == null)
                documents.add(mapper.mapper(comment));
        }
        collection.insertMany(documents);
    }

    public void updateWeixinState(String yqpid, int state) {
        MongoCollection<Document> collection = MongoDataSourceFactory.getMongoCollection(SpiderConfig.getWeixinZhihuBaiduMongoArticleCollection());
        Document find = new Document("yqpid", yqpid);
        Document update = collection.find(find).first();
        if (update != null) {
            update.put("state", state);
            collection.updateOne(find, update);
        }

    }

    public void saveOrUpdate(YuqingMdata mdata, MongoDocMapper<YuqingMdata> mapper) {
        MongoCollection<Document> collection = MongoDataSourceFactory.getMongoCollection(SpiderConfig.getWeixinZhihuBaiduMongoMdataCollection());
        Document find = new Document();
        find.append("yqpid", mdata.getYqpid());
        Document temp = null;
        Document update = mapper.mapper(mdata);
        temp = collection.find(find).first();
        if (temp == null)
            collection.insertOne(update);
        else
            collection.findOneAndReplace(find, update);
    }

    public void saveYuqing(YuqingArticle article, MongoDocMapper<YuqingArticle> mapper) {
        MongoCollection<Document> collection = MongoDataSourceFactory.getMongoCollection(SpiderConfig.getWeixinZhihuBaiduMongoArticleCollection());
        Document find = new Document("yqpid", article.getYuqing_id());
        Document temp = collection.find(find).first();
        if (temp == null) {
            Document doc = mapper.mapper(article);
            collection.insertOne(doc);
        }
    }
}

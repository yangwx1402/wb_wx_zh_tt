package com.letv.sf.dao.toutiao;

import com.letv.sf.config.SpiderConfig;
import com.letv.sf.dao.MongoDataSourceFactory;
import com.letv.sf.dao.mapper.MongoDocMapper;
import com.letv.sf.entity.toutiao.ToutiaoArticle;
import com.letv.sf.utils.DateUtils;
import com.mongodb.client.MongoCollection;
import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;
import org.bson.BSONObject;
import org.bson.Document;

import java.util.*;

/**
 * Created by yangyong3 on 2017/1/5.
 */
public class ToutiaoDaoByMongo implements ToutiaoDao {

    private static final Logger log = Logger.getLogger(ToutiaoDaoByMongo.class);

    private void saveToutiao(List<Map<String, Object>> data, String collectionName, String keyName) {
        if (CollectionUtils.isEmpty(data))
            return;
        MongoCollection<Document> collection = MongoDataSourceFactory.getMongoCollection(collectionName);
        Document find = new Document();
        Document exist = null;
        for (Map<String, Object> temp : data) {
            find.append(keyName, temp.get(keyName));
            exist = collection.find(find).first();
            if (exist == null) {
                collection.insertOne(new Document(temp));
                log.info("insert mongo " + collectionName + "  " + keyName + "=" + temp.get(keyName));
            } else
                log.info("mongo exist " + collectionName + "  " + keyName + "=" + temp.get(keyName));
        }
    }

    public void saveToutiaoArticle(List<Map<String, Object>> articles) {
        saveToutiao(articles, SpiderConfig.getToutiaoArticleMongoCollectionName(), "id");
    }

    public void saveToutiaoComments(List<Map<String, Object>> comments) {
        saveToutiao(comments, SpiderConfig.getToutiaoArticleCommentMongoCollectionName(), "id");
    }

    public List<ToutiaoArticle> fetchArticleByState(int state, MongoDocMapper<ToutiaoArticle> mapper) {
        List<ToutiaoArticle> articles = new ArrayList<ToutiaoArticle>();
        MongoCollection<Document> collection = MongoDataSourceFactory.getMongoCollection(SpiderConfig.getToutiaoArticleMongoCollectionName());
        Document find = new Document("state", state);
        find.put("insert_time", DateUtils.dateString(new Date(), SpiderConfig.insert_update_time_format));
        Iterator<Document> it = collection.find(find).iterator();
        while (it.hasNext()) {
            articles.add(mapper.deMapper(it.next()));
        }
        return articles;
    }

    public void updateToutiao(ToutiaoArticle article) {
        MongoCollection<Document> collection = MongoDataSourceFactory.getMongoCollection(SpiderConfig.getToutiaoArticleMongoCollectionName());
        Document find = new Document("id",article.getId());

        Document updates = new Document();
        updates.append("text",article.getText());
        updates.append("state",article.getState());
        Document update = new Document("$set",updates);
        collection.updateOne(find,update);
        log.info("update toutiao article id -["+article.getId()+"] state is -["+article.getState()+"]");
    }
}

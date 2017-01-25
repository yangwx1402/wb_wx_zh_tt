package com.letv.sf.dao.beidou;

import com.letv.sf.config.ContentType;
import com.letv.sf.config.SpiderConfig;
import com.letv.sf.dao.MongoDataSourceFactory;
import com.letv.sf.entity.beidou.BeidouEntity;
import com.letv.sf.entity.weibo.BeidouMapping;
import com.letv.sf.utils.DateUtils;
import com.mongodb.BasicDBObject;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.bson.Document;
import weibo4j.model.User;

import java.util.*;

/**
 * Created by yangyong3 on 2016/12/20.
 */
public class BeidouDaoByMongo implements BeidouDao {
    public List<BeidouEntity> getKeywords(ContentType type, String date) {
        List<BeidouEntity> beidouEntities = new ArrayList<BeidouEntity>();
        MongoCollection<Document> collection = MongoDataSourceFactory.getMongoCollection(SpiderConfig.getWeiboMoiveCatalogMongoCollection());
        BasicDBObject find = new BasicDBObject();
        List<BasicDBObject> condList = new ArrayList<BasicDBObject>();
        BasicDBObject release_date = new BasicDBObject("crawl_flag.is_news", "Y");
        BasicDBObject crawl_date = new BasicDBObject("crawl_date", date);
        condList.add(release_date);
        condList.add(crawl_date);
        find.append("$and", condList);
        FindIterable<Document> documents = collection.find(find).limit(1000);
        Iterator<Document> it = documents.iterator();
        Document document = null;
        long beidou_id = 0l;
        List<String> search_tags = null;
        String event_name = null;
        BeidouEntity beidou = null;
        while (it.hasNext()) {
            document = it.next();
            beidou_id = document.getLong("id");
            event_name = document.getString("name");
            if (ContentType.movie == type || ContentType.weixin == type) {
                search_tags = (List<String>) document.get("search_tags");
                if (!CollectionUtils.isEmpty(search_tags)) {
                    for (String line : search_tags) {
                        beidou = new BeidouEntity();
                        beidou.setBeidou_id(beidou_id);
                        beidou.setEvent_name(event_name);
                        beidou.setTag(line);
                        beidouEntities.add(beidou);
                    }
                }
            } else if (ContentType.zhihu == type) {
                beidou = new BeidouEntity();
                beidou.setBeidou_id(beidou_id);
                beidou.setEvent_name(event_name);
                String zhihu_topic = document.getString("zhihu");
                if (!StringUtils.isBlank(zhihu_topic)) {
                    beidou.setTag(zhihu_topic);
                    beidouEntities.add(beidou);
                }
            } else if (ContentType.baidu == type) {
                beidou = new BeidouEntity();
                beidou.setBeidou_id(beidou_id);
                beidou.setEvent_name(event_name);
                String tieba = document.getString("tieba");
                if (!StringUtils.isBlank(tieba)) {
                    beidou.setTag("http://tieba.baidu.com/f?kw=" + tieba + "&ie=utf-8");
                    beidouEntities.add(beidou);
                }
            }

        }
        return beidouEntities;
    }

    public void saveBeidouMapping(BeidouMapping beidou, User user) {
        return;
    }

    public void saveBeidouMappings(BeidouMapping beidou, Collection<User> users) {

    }

    public List<BeidouEntity> listAllBeidou() {
        return null;
    }

    public List<BeidouEntity> getMoiveOfficial(String date) {
        List<BeidouEntity> beidouEntities = new ArrayList<BeidouEntity>();
        MongoCollection<Document> collection = MongoDataSourceFactory.getMongoCollection(SpiderConfig.getWeiboMoiveCatalogMongoCollection());
        BasicDBObject find = new BasicDBObject();
        List<BasicDBObject> condList = new ArrayList<BasicDBObject>();
        BasicDBObject release_date = new BasicDBObject("crawl_flag.is_news", "Y");
        BasicDBObject crawl_date = new BasicDBObject("crawl_date", date);
        condList.add(release_date);
        condList.add(crawl_date);
        find.append("$and", condList);
        FindIterable<Document> documents = collection.find(find).limit(1000);
        Iterator<Document> it = documents.iterator();
        Document document = null;
        long beidou_id = 0l;
        String event_name = null;
        String weibo_official = null;
        BeidouEntity beidou = null;
        while (it.hasNext()) {
            document = it.next();
            beidou = new BeidouEntity();
            beidou_id = document.getLong("id");
            event_name = document.getString("name");
            weibo_official = document.getString("weibo");
            if (!StringUtils.isBlank(weibo_official)) {
                beidou.setBeidou_id(beidou_id);
                beidou.setEvent_name(event_name);
                beidou.setTag(weibo_official);
                beidouEntities.add(beidou);
            }

        }
        return beidouEntities;
    }
}

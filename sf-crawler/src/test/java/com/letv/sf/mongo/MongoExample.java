package com.letv.sf.mongo;

import com.letv.sf.config.SpiderConfig;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

/**
 * Created by yangyong3 on 2016/12/6.
 */
public class MongoExample {
    public static void main(String[] args){
        String collectionName = "test_weixin_crawler";
        String mongo_url = "mongodb://bigdata:NDZmMTNhZGMzYjg@10.183.222.168:27017,10.183.222.177:27017,10.183.222.178:27017/?replicaSet=test_noauth";
        MongoClientURI uri  = new MongoClientURI(mongo_url);
        MongoClient client = new MongoClient(uri);
        MongoDatabase database = client.getDatabase("letv_beidou_spider");
        System.out.println(database.getName());
        //database.createCollection(SpiderConfig.getWeixinZhihuBaiduMongoArticleCollection());

    }
}

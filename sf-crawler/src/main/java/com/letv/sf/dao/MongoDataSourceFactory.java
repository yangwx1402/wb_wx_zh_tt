package com.letv.sf.dao;

import com.letv.sf.utils.PropertiesUtils;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

import java.io.File;
import java.util.Hashtable;
import java.util.Map;
import java.util.Properties;

/**
 * Created by yangyong3 on 2016/12/6.
 */
public class MongoDataSourceFactory {

    private static MongoDatabase database;

    private static final String default_mongo_url = "mongodb://bigdata:NDZmMTNhZGMzYjg@10.183.222.168:27017,10.183.222.177:27017,10.183.222.178:27017/?replicaSet=test_noauth";

    private static final String default_mongo_db = "letv_beidou_spider";

    private static final Map<String, MongoCollection<Document>> collectionPool = new Hashtable<String, MongoCollection<Document>>();

    private static MongoClient client;

    private synchronized static void init() {
        if (database == null) {
            File configFile = new File(MongoDataSourceFactory.class.getResource("/").getPath() + File.separator + "mongo.properties");
            Properties properties = PropertiesUtils.getProperties(configFile);
            MongoClientURI uri = new MongoClientURI(properties.getProperty("mongo.url", default_mongo_url));
            client = new MongoClient(uri);
            database = client.getDatabase(properties.getProperty("mongo.db", default_mongo_db));
        }
    }

    public static MongoCollection<Document> getMongoCollection(String collectionName) {
        init();
        if (collectionPool.containsKey(collectionName)) {
            return collectionPool.get(collectionName);
        }
        MongoCollection<Document> collection = database.getCollection(collectionName);
        collectionPool.put(collectionName, collection);
        return collection;
    }

    public static void destory() {
        if (database != null && client != null)
            client.close();
    }


}

package com.letv.sf.dao.beidou;

import com.letv.sf.config.SpiderConfig;
import com.letv.sf.dao.MongoDataSourceFactory;
import com.letv.sf.dao.beidou.mapper.BeidouReportMapper;
import com.letv.sf.dao.mapper.MongoDocMapper;
import com.letv.sf.entity.beidou.BeidouEntity;
import com.letv.sf.entity.beidou.BeidouReport;
import com.letv.sf.entity.beidou.BeidouReportTable;
import com.letv.sf.utils.DateUtils;
import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.client.AggregateIterable;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.util.JSON;
import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.bson.BSON;
import org.bson.BsonArray;
import org.bson.BsonUndefined;
import org.bson.Document;
import org.bson.codecs.BsonUndefinedCodec;
import org.bson.conversions.Bson;

import java.util.*;

/**
 * Created by yangyong3 on 2016/12/14.
 */
public class BeidouReportDaoByMongo implements BeidouReportDao {

    private static final Logger log = Logger.getLogger(BeidouReportDaoByMongo.class);

    public BeidouReport countReport(String sql) {
        return null;
    }

    public List<BeidouReport> countReports(String sql) {
        return null;
    }

    public BeidouReport computeReport(String tableName, Map<String, Object> params, String desc) {
        MongoCollection<Document> collection = MongoDataSourceFactory.getMongoCollection(tableName);
        Document document = new Document();
        if (params != null && params.size() > 0) {
            for (Map.Entry<String, Object> entry : params.entrySet())
                document.append(entry.getKey(), entry.getValue());
        }
        BeidouReport beidouReport = new BeidouReport();
        log.info("mongo document -[" + document + "]");
        long count = collection.count(document);
        beidouReport.setName(desc);
        beidouReport.setNum(count);
        return beidouReport;
    }

    public List<BeidouReport> computeReports(String tableName, Map<String, Object> params, String desc) {
        return null;
    }

    public Long count(String tableName, Bson sql) {
        MongoCollection<Document> collection = MongoDataSourceFactory.getMongoCollection(tableName);
        return collection.count(sql);
    }

    public BeidouReportTable computeReportTable(String sql, Map<Long, String> beidou_mapping, String crawl_date) {
        BeidouReportTable table = new BeidouReportTable();
        List<Object[]> list = new ArrayList<Object[]>();
        Object[] temp = null;
        BasicDBList array = (BasicDBList) JSON.parse(sql);
        MongoCollection<Document> collection = MongoDataSourceFactory.getMongoCollection(SpiderConfig.getWeixinZhihuBaiduMongoArticleCollection());
        List<Bson> bsons = new ArrayList<Bson>();
        for (int i = 0; i < array.size(); i++) {
            bsons.add((Bson) array.get(i));
        }
        AggregateIterable<Document> documents = collection.aggregate(bsons);
        Iterator<Document> it = documents.iterator();
        Document document = null;
        BeidouReportMapper mapper = new BeidouReportMapper(beidou_mapping);
        if (!StringUtils.isBlank(crawl_date)) {
            mapper.setCrawl_date(crawl_date);
        }
        while (it.hasNext()) {
            document = it.next();
            temp = mapper.deMapper(document);
            if (temp != null)
                list.add(temp);
        }
        table.setData(list);
        return table;
    }

    public Map<Long, String> getCatalogBeidouMapping(String crawler_date) {
        Map<Long, String> result = new HashMap<Long, String>();
        MongoCollection<Document> collection = MongoDataSourceFactory.getMongoCollection(SpiderConfig.getWeiboMoiveCatalogMongoCollection());
        Document document = null;
        BasicDBObject find = new BasicDBObject();
        List<BasicDBObject> condList = new ArrayList<BasicDBObject>();
        BasicDBObject release_date = new BasicDBObject("crawl_flag.is_news", "Y");
        BasicDBObject crawl_date = new BasicDBObject("crawl_date", crawler_date);
        condList.add(release_date);
        condList.add(crawl_date);
        find.append("$and", condList);
        FindIterable<Document> documents = collection.find(find).limit(10000);
        Iterator<Document> it = documents.iterator();
        while (it.hasNext()) {
            document = it.next();
            result.put(document.getLong("id"), document.getString("name"));
        }
        return result;
    }
}

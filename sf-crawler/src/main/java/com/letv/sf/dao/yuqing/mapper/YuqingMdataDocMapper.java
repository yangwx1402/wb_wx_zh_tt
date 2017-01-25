package com.letv.sf.dao.yuqing.mapper;

import com.letv.sf.config.SpiderConfig;
import com.letv.sf.dao.mapper.MongoDocMapper;
import com.letv.sf.entity.yuqing.YuqingMdata;
import com.letv.sf.utils.DateUtils;
import org.bson.Document;

import java.util.Date;

/**
 * Created by yangyong3 on 2016/12/9.
 */
public class YuqingMdataDocMapper implements MongoDocMapper<YuqingMdata> {
    public Document mapper(YuqingMdata yuqingMdata) {
        Document document = new Document();
        document.append("attention_user", yuqingMdata.getAttention_user());
        document.append("beidou_id", yuqingMdata.getBeidou_id());
        document.append("beidou_name", yuqingMdata.getBeidou_name());
        document.append("id", yuqingMdata.getId());
        document.append("insert_time", yuqingMdata.getInsert_time());
        document.append("name", yuqingMdata.getName());
        document.append("posts", yuqingMdata.getPosts());
        document.append("source", yuqingMdata.getSource());
        document.append("update_time", yuqingMdata.getUpdate_time());
        document.append("yqpid", yuqingMdata.getYqpid());
        document.append("insert_time_long", yuqingMdata.getInsert_time_long());
        return document;
    }

    public YuqingMdata deMapper(Document document) {
        YuqingMdata mdata = new YuqingMdata();
        mdata.setAttention_user(document.getInteger("attention_user"));
        mdata.setBeidou_id(document.getLong("beidou_id"));
        mdata.setBeidou_name(document.getString("beidou_name"));
        mdata.setId(document.getString("id"));
        mdata.setInsert_time(DateUtils.dateString(new Date(), SpiderConfig.insert_update_time_format));
        mdata.setName(document.getString("name"));
        mdata.setPosts(document.getInteger("posts"));
        mdata.setSource(document.getString("source"));
        mdata.setYqpid(document.getString("yqpid"));
        mdata.setInsert_time_long(document.getLong("insert_time_long"));
        return mdata;
    }
}

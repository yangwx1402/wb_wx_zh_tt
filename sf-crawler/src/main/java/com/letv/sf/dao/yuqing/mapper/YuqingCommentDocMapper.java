package com.letv.sf.dao.yuqing.mapper;

import com.letv.sf.dao.mapper.MongoDocMapper;
import com.letv.sf.entity.yuqing.YuqingComment;
import org.bson.Document;

/**
 * Created by yangyong3 on 2016/12/7.
 */
public class YuqingCommentDocMapper implements MongoDocMapper<YuqingComment>{
    public Document mapper(YuqingComment yuqingComment) {
        Document document = new Document();
        document.append("likes_number",yuqingComment.getAttitudes_count());
        document.append("author",yuqingComment.getAuthor());
        document.append("body",yuqingComment.getBody());
        document.append("publish_time",yuqingComment.getCreated_at());
        document.append("insert_time",yuqingComment.getInsert_time());
        document.append("publish_time_long",yuqingComment.getCreated_long());
        document.append("source",yuqingComment.getSource());
        document.append("yqpid",yuqingComment.getYqpid());
        document.append("yqrpid",yuqingComment.getYqrpid());
        document.append("beidou_id",yuqingComment.getBeidou_id());
        return document;
    }

    public YuqingComment deMapper(Document document) {
        return null;
    }
}

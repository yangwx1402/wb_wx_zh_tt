package com.letv.sf.entity.yuqing;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * Created by yangyong3 on 2016/12/5.
 */
public class YuqingComment implements Serializable{

    private String yqpid;

    private String yqrpid;

    private Long beidou_id;

    private String author;

    private String body;

    private String source;

    private String insert_time;

    private String created_at;

    private Long created_long;

    private Integer attitudes_count;

    public String getYqpid() {
        return yqpid;
    }

    public void setYqpid(String yqpid) {
        this.yqpid = yqpid;
    }

    public String getYqrpid() {
        return yqrpid;
    }

    public void setYqrpid(String yqrpid) {
        this.yqrpid = yqrpid;
    }

    public Long getBeidou_id() {
        return beidou_id;
    }

    public void setBeidou_id(Long beidou_id) {
        this.beidou_id = beidou_id;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getInsert_time() {
        return insert_time;
    }

    public void setInsert_time(String insert_time) {
        this.insert_time = insert_time;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public Long getCreated_long() {
        return created_long;
    }

    public void setCreated_long(Long created_long) {
        this.created_long = created_long;
    }

    public Integer getAttitudes_count() {
        return attitudes_count;
    }

    public void setAttitudes_count(Integer attitudes_count) {
        this.attitudes_count = attitudes_count;
    }
}

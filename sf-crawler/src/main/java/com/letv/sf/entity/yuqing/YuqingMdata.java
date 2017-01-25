package com.letv.sf.entity.yuqing;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * Created by yangyong3 on 2016/12/9.
 */
public class YuqingMdata implements Serializable{

    private String yqpid;

    private String id;
    private String name;
    private Long beidou_id;
    private String beidou_name;
    private Integer  attention_user;
    private Integer posts;
    private String source;
    private String insert_time;
    private Long insert_time_long;
    private String update_time;

    public String getYqpid() {
        return yqpid;
    }

    public void setYqpid(String yqpid) {
        this.yqpid = yqpid;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getBeidou_id() {
        return beidou_id;
    }

    public void setBeidou_id(Long beidou_id) {
        this.beidou_id = beidou_id;
    }

    public String getBeidou_name() {
        return beidou_name;
    }

    public void setBeidou_name(String beidou_name) {
        this.beidou_name = beidou_name;
    }

    public Integer getAttention_user() {
        return attention_user;
    }

    public void setAttention_user(Integer attention_user) {
        this.attention_user = attention_user;
    }

    public Integer getPosts() {
        return posts;
    }

    public void setPosts(Integer posts) {
        this.posts = posts;
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

    public Long getInsert_time_long() {
        return insert_time_long;
    }

    public void setInsert_time_long(Long insert_time_long) {
        this.insert_time_long = insert_time_long;
    }

    public String getUpdate_time() {
        return update_time;
    }

    public void setUpdate_time(String update_time) {
        this.update_time = update_time;
    }
}

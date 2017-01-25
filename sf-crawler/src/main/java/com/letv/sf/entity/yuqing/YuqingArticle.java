package com.letv.sf.entity.yuqing;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yangyong3 on 2016/12/5.
 */
public class YuqingArticle {

    private Long beidou_id;

    private String beidou_name;

    private String yuqing_id;

    private String searchTag;

    private String body;

    private String url;

    private String md5;

    private String title;

    private String description;

    private Integer readCount;

    private Integer attitudes_count;

    private String source;

    private Integer replyCount;

    private Integer commentCount;

    public String getBeidou_name() {
        return beidou_name;
    }

    public void setBeidou_name(String beidou_name) {
        this.beidou_name = beidou_name;
    }

    public Integer getCommentCount() {
        return commentCount;
    }

    public void setCommentCount(Integer commentCount) {
        this.commentCount = commentCount;
    }

    private String tag;

    private String yqid_answer;

    public String getYqid_answer() {
        return yqid_answer;
    }

    public void setYqid_answer(String yqid_answer) {
        this.yqid_answer = yqid_answer;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public Integer getReplyCount() {
        return replyCount;
    }

    public void setReplyCount(Integer replyCount) {
        this.replyCount = replyCount;
    }

    private int state;

    public List<YuqingComment> getComments() {
        return comments;
    }

    public void setComments(List<YuqingComment> comments) {
        this.comments = comments;
    }

    private List<YuqingComment> comments = new ArrayList<YuqingComment>();

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public Integer getAttitudes_count() {
        return attitudes_count;
    }

    public void setAttitudes_count(Integer attitudes_count) {
        this.attitudes_count = attitudes_count;
    }

    private String created_at;

    private Long created_long;

    private String insert_time;

    private String update_time;

    private String author;

    private String authorInfo;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getMd5() {
        return md5;
    }

    public void setMd5(String md5) {
        this.md5 = md5;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getReadCount() {
        return readCount;
    }

    public void setReadCount(Integer readCount) {
        this.readCount = readCount;
    }

    public Long getBeidou_id() {
        return beidou_id;
    }

    public void setBeidou_id(Long beidou_id) {
        this.beidou_id = beidou_id;
    }

    public String getYuqing_id() {
        return yuqing_id;
    }

    public String getAuthorInfo() {
        return authorInfo;
    }

    public void setAuthorInfo(String authorInfo) {
        this.authorInfo = authorInfo;
    }

    public void setYuqing_id(String yuqing_id) {
        this.yuqing_id = yuqing_id;
    }

    public String getSearchTag() {
        return searchTag;
    }

    public void setSearchTag(String searchTag) {
        this.searchTag = searchTag;
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

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
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

    public String getInsert_time() {
        return insert_time;
    }

    public void setInsert_time(String insert_time) {
        this.insert_time = insert_time;
    }

    public String getUpdate_time() {
        return update_time;
    }

    public void setUpdate_time(String update_time) {
        this.update_time = update_time;
    }
}

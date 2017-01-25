package com.letv.sf.entity.toutiao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by yangyong3 on 2017/1/5.
 */
public class ToutiaoArticle {

    private Long id;

    private Integer state;

    private String text;

    private Long beidou_id;

    private String url;

    private Long group;

    public Long getGroup() {
        return group;
    }

    public void setGroup(Long group) {
        this.group = group;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    private List<Map<String, Object>> comments = new ArrayList<Map<String, Object>>();

    public Long getId() {
        return id;
    }

    public Long getBeidou_id() {
        return beidou_id;
    }

    public void setBeidou_id(Long beidou_id) {
        this.beidou_id = beidou_id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public List<Map<String, Object>> getComments() {
        return comments;
    }

    public void setComments(List<Map<String, Object>> comments) {
        this.comments = comments;
    }
}

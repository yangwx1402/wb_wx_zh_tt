package com.letv.sf.entity.weibo;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * Created by yangyong3 on 2016/12/2.
 */
public class WeiboTrace implements Serializable {

    private Long mid;

    private String beidou_id;

    public String getBeidou_id() {
        return beidou_id;
    }

    public void setBeidou_id(String beidou_id) {
        this.beidou_id = beidou_id;
    }

    private Integer reposts_count;

    private Integer comment_count;

    private Integer attitudes_count;

    private Timestamp time = new Timestamp(System.currentTimeMillis());

    public Long getMid() {
        return mid;
    }

    public void setMid(Long mid) {
        this.mid = mid;
    }

    public Integer getReposts_count() {
        return reposts_count;
    }

    public void setReposts_count(Integer reposts_count) {
        this.reposts_count = reposts_count;
    }

    public Integer getComment_count() {
        return comment_count;
    }

    public void setComment_count(Integer comment_count) {
        this.comment_count = comment_count;
    }

    public Integer getAttitudes_count() {
        return attitudes_count;
    }

    public void setAttitudes_count(Integer attitudes_count) {
        this.attitudes_count = attitudes_count;
    }

    public Timestamp getTime() {
        return time;
    }

    public void setTime(Timestamp time) {
        this.time = time;
    }
}

package com.letv.sf.entity.weibo;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * Created by yangyong3 on 2016/12/7.
 */
public class WeiboTopicEventInfo implements Serializable{
    private Long beidou_id;

    private String type;

    private String status;

    private Integer zip_id;

    private String zip_value;

    private Timestamp insert_time = new Timestamp(System.currentTimeMillis());

    public Long getBeidou_id() {
        return beidou_id;
    }

    public void setBeidou_id(Long beidou_id) {
        this.beidou_id = beidou_id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Integer getZip_id() {
        return zip_id;
    }

    public void setZip_id(Integer zip_id) {
        this.zip_id = zip_id;
    }

    public String getZip_value() {
        return zip_value;
    }

    public void setZip_value(String zip_value) {
        this.zip_value = zip_value;
    }

    public Timestamp getInsert_time() {
        return insert_time;
    }

    public void setInsert_time(Timestamp insert_time) {
        this.insert_time = insert_time;
    }
}

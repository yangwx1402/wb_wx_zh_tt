package com.letv.sf.entity.beidou;

/**
 * Created by yangyong3 on 2016/12/1.
 */
public class BeidouEntity {
    private Long beidou_id;

    private String event_name;

    private String tag;

    private String region;

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    private Long uid;


    public Long getUid() {
        return uid;
    }

    public void setUid(Long uid) {
        this.uid = uid;
    }

    public Long getBeidou_id() {
        return beidou_id;
    }

    public void setBeidou_id(Long beidou_id) {
        this.beidou_id = beidou_id;
    }

    public String getEvent_name() {
        return event_name;
    }

    public void setEvent_name(String event_name) {
        this.event_name = event_name;
    }

}

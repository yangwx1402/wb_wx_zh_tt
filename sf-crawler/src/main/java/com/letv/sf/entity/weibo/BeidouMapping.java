package com.letv.sf.entity.weibo;

/**
 * Created by yangyong3 on 2016/12/5.
 */
public class BeidouMapping {

    private Long mid;

    private String beidou_id;

    private String crawl_source;

    private String crawl_type;

    public String getCrawl_source() {
        return crawl_source;
    }

    public void setCrawl_source(String crawl_source) {
        this.crawl_source = crawl_source;
    }

    public String getCrawl_type() {
        return crawl_type;
    }

    public void setCrawl_type(String crawl_type) {
        this.crawl_type = crawl_type;
    }

    public Long getMid() {
        return mid;
    }

    public void setMid(Long mid) {
        this.mid = mid;
    }

    public String getBeidou_id() {
        return beidou_id;
    }

    public void setBeidou_id(String beidou_id) {
        this.beidou_id = beidou_id;
    }
}

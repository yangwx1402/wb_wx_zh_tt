package com.letv.sf.entity.common;

import java.util.List;

/**
 * Created by yangyong3 on 2016/11/30.
 */
public class CrawlerResultEntity<T> {
    public SpiderStatus getStatus() {
        return status;
    }

    public void setStatus(SpiderStatus status) {
        this.status = status;
    }

    private List<T> items;

    private CrawlerPageInfo page;

    private SpiderStatus status;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    private String message;

    public CrawlerPageInfo getPage() {
        return page;
    }

    public void setPage(CrawlerPageInfo page) {
        this.page = page;
    }

    public List<T> getItems() {
        return items;
    }

    public void setItems(List<T> items) {
        this.items = items;
    }
}

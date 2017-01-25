package com.letv.sf.entity.common;

/**
 * Created by yangyong3 on 2016/11/30.
 */
public class CrawlerPageInfo {
    private long pageTotal;

    private long totlaNum;

    private boolean hasMore;

    public boolean isHasMore() {
        return hasMore;
    }

    public void setHasMore(boolean hasMore) {
        this.hasMore = hasMore;
    }

    public long getPageTotal() {
        return pageTotal;
    }

    public void setPageTotal(long pageTotal) {
        this.pageTotal = pageTotal;
    }

    public long getTotlaNum() {
        return totlaNum;
    }

    public void setTotlaNum(long totlaNum) {
        this.totlaNum = totlaNum;
    }

    public CrawlerPageInfo(long pageTotal,long totlaNum,boolean hasMore){
        this.pageTotal = pageTotal;
        this.totlaNum = totlaNum;
        this.hasMore = hasMore;
    }

    public CrawlerPageInfo(){}

    public CrawlerPageInfo(long pageTotal,long totlaNum){
        this.pageTotal = pageTotal;
        this.totlaNum = totlaNum;
    }

    public CrawlerPageInfo(boolean hasMore){
        this.hasMore = hasMore;
    }

}

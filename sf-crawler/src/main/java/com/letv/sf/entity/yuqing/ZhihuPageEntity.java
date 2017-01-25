package com.letv.sf.entity.yuqing;

import com.letv.sf.entity.common.CrawlerResultEntity;

/**
 * Created by yangyong3 on 2016/12/9.
 */
public class ZhihuPageEntity {

    private CrawlerResultEntity<YuqingArticle> questions;

    private YuqingMdata yuqingMdata;

    public CrawlerResultEntity<YuqingArticle> getQuestions() {
        return questions;
    }

    public void setQuestions(CrawlerResultEntity<YuqingArticle> questions) {
        this.questions = questions;
    }

    public YuqingMdata getYuqingMdata() {
        return yuqingMdata;
    }

    public void setYuqingMdata(YuqingMdata yuqingMdata) {
        this.yuqingMdata = yuqingMdata;
    }
}

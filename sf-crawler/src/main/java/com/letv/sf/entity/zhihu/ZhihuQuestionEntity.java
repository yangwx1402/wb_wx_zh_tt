package com.letv.sf.entity.zhihu;

import com.letv.sf.entity.common.CrawlerResultEntity;
import com.letv.sf.entity.yuqing.YuqingArticle;

/**
 * Created by yangyong3 on 2016/12/16.
 */
public class ZhihuQuestionEntity implements java.io.Serializable {

    private CrawlerResultEntity<YuqingArticle> answers;

    private YuqingArticle question;

    public CrawlerResultEntity<YuqingArticle> getAnswers() {
        return answers;
    }

    public void setAnswers(CrawlerResultEntity<YuqingArticle> answers) {
        this.answers = answers;
    }

    public YuqingArticle getQuestion() {
        return question;
    }

    public void setQuestion(YuqingArticle question) {
        this.question = question;
    }
}

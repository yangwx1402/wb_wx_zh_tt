package com.letv.sf.job.zhihu;

import com.letv.sf.config.YuqingState;
import com.letv.sf.config.YuqingType;
import com.letv.sf.crawler.CrawlerFactory;
import com.letv.sf.dao.DaoFactory;
import com.letv.sf.dao.yuqing.mapper.YuqingCommentDocMapper;
import com.letv.sf.dao.yuqing.mapper.YuqingDocMapper;
import com.letv.sf.entity.beidou.BeidouEntity;
import com.letv.sf.entity.yuqing.YuqingArticle;
import com.letv.sf.entity.zhihu.ZhihuQuestionEntity;
import com.letv.sf.http.HttpResult;
import com.letv.sf.parser.ParseFactory;
import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;

import java.util.List;

/**
 * Created by yangyong3 on 2016/12/27.
 */
public class ZhihuProcess {


    private static final Logger log = Logger.getLogger(ZhihuProcess.class);

    protected void process(YuqingArticle article) throws Exception {
        BeidouEntity beidou = new BeidouEntity();
        beidou.setBeidou_id(article.getBeidou_id());
        beidou.setEvent_name(article.getBeidou_name());
        //抓取问题页面
        HttpResult<BeidouEntity> httpResult = CrawlerFactory.zhihuPageCrawler.crawl(article.getUrl(), null);
        if(httpResult == null){
            log.error("爬取url出错,url is -"+article.getUrl()+",statusCode = "+httpResult.getCode());
            return;
        }
        httpResult.setUrl(article.getUrl());
        httpResult.setMeta(beidou);
        //解析出问题及问题评论，答案和答案评论
        ZhihuQuestionEntity entity = ParseFactory.zhihuPageParser.parse(httpResult);
        if (entity == null || CollectionUtils.isEmpty(entity.getAnswers().getItems())) {
            //修改问题状态为已经抓取过
            article.setState(YuqingState.FETCHED_COMMENT.getState());
            DaoFactory.yuqingDao.updateWeixin(article, new YuqingDocMapper());
            return;
        }
        //处理回答和回答的评论
        for (YuqingArticle answer : entity.getAnswers().getItems()) {
            //保存答案
            DaoFactory.yuqingDao.saveYuqing(answer, new YuqingDocMapper());
            //保存答案的评论
            if (!CollectionUtils.isEmpty(answer.getComments())) {
                DaoFactory.yuqingDao.saveComments(answer.getComments(), new YuqingCommentDocMapper(), answer.getYuqing_id(),beidou.getBeidou_id());
            }
        }
        //修改问题状态为已经抓取过
        if (entity.getQuestion() != null) {

            article.setReplyCount(entity.getQuestion().getReplyCount());
            article.setAttitudes_count(entity.getQuestion().getAttitudes_count());
            //保存评论
            if (!CollectionUtils.isEmpty(entity.getQuestion().getComments())) {
                DaoFactory.yuqingDao.saveComments(entity.getQuestion().getComments(), new YuqingCommentDocMapper(), article.getYuqing_id(),beidou.getBeidou_id());
            }
        }
        //更新问题的状态为已抓取
        article.setState(YuqingState.FETCHED_COMMENT.getState());
        DaoFactory.yuqingDao.updateWeixin(article, new YuqingDocMapper());

    }

    protected List<YuqingArticle> findQestionNotCommented() {
        return DaoFactory.yuqingDao.getYuqingArticleByState(YuqingState.NO_FETCH_COMMENT.getState(), YuqingType.ZHIHU.getName(), 100, new YuqingDocMapper());
    }
}

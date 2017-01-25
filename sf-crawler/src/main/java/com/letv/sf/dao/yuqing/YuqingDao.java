package com.letv.sf.dao.yuqing;

import com.letv.sf.dao.mapper.MongoDocMapper;
import com.letv.sf.entity.yuqing.YuqingArticle;
import com.letv.sf.entity.yuqing.YuqingComment;
import com.letv.sf.entity.yuqing.YuqingMdata;

import java.util.List;

/**
 * Created by young.yang on 2016/11/29.
 */
public interface YuqingDao {
    public void saveWeixinList(List<YuqingArticle> articles, MongoDocMapper<YuqingArticle> mapper);

    public void updateWeixin(YuqingArticle article,MongoDocMapper<YuqingArticle> mapper);

    public List<YuqingArticle> getYuqingArticleByState(int state,String source,int limit,MongoDocMapper<YuqingArticle> mapper);

    public void saveComments(List<YuqingComment> comments,MongoDocMapper<YuqingComment> mapper,String yqpid,Long beidou_id);

    public void updateWeixinState(String yqpid,int state);

    public void saveOrUpdate(YuqingMdata mdata,MongoDocMapper<YuqingMdata> mapper);

    public void saveYuqing(YuqingArticle article,MongoDocMapper<YuqingArticle> mapper);
}

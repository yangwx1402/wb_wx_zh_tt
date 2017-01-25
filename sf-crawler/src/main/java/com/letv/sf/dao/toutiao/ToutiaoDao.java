package com.letv.sf.dao.toutiao;

import com.letv.sf.dao.mapper.MongoDocMapper;
import com.letv.sf.entity.toutiao.ToutiaoArticle;

import java.util.List;
import java.util.Map;

/**
 * Created by yangyong3 on 2017/1/5.
 */
public interface ToutiaoDao {

    public void saveToutiaoArticle(List<Map<String,Object>> articles);

    public void saveToutiaoComments(List<Map<String,Object>> comments);

    public List<ToutiaoArticle> fetchArticleByState(int state, MongoDocMapper<ToutiaoArticle> mapper);

    public void updateToutiao(ToutiaoArticle article);
}

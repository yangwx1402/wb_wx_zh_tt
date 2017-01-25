package com.letv.sf.toutiao.crawler;

import com.letv.sf.dao.toutiao.ToutiaoDaoByMongo;
import com.letv.sf.entity.toutiao.ToutiaoArticle;

/**
 * Created by yangyong3 on 2017/1/6.
 */
public class UpdateToutiaoText {
    public static void main(String[] args){
        ToutiaoDaoByMongo mongo = new ToutiaoDaoByMongo();
                ToutiaoArticle article = new ToutiaoArticle();
        article.setBeidou_id(291701001l);
        article.setGroup(6371736393367109890l);
        article.setId(6371736393367109890l);
        article.setState(1);
        article.setText("1111");
        article.setUrl("http://toutiao.com/group/6361564855732994305/");
        mongo.updateToutiao(article);
    }
}

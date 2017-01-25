package com.letv.sf.mongo;

import com.letv.sf.config.ContentType;
import com.letv.sf.config.SpiderConfig;
import com.letv.sf.dao.beidou.BeidouDao;
import com.letv.sf.dao.beidou.BeidouDaoByMongo;
import com.letv.sf.entity.beidou.BeidouEntity;
import com.letv.sf.utils.DateUtils;

import java.util.Date;
import java.util.List;

/**
 * Created by yangyong3 on 2016/12/21.
 */
public class BeidouMongoExample {
    public static void main(String[] args){
        BeidouDao dao = new BeidouDaoByMongo();
        List<BeidouEntity> entitys = dao.getKeywords(ContentType.movie, DateUtils.dateString(new Date(), SpiderConfig.insert_update_time_format));
        for(BeidouEntity beidou:entitys){
            System.out.println(beidou.getBeidou_id()+","+beidou.getEvent_name()+","+beidou.getTag());
        }
        System.out.println(entitys.size());
    }
}

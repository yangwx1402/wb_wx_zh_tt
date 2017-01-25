package com.letv.sf.dao;

import com.letv.sf.dao.beidou.*;
import com.letv.sf.dao.toutiao.ToutiaoDao;
import com.letv.sf.dao.toutiao.ToutiaoDaoByMongo;
import com.letv.sf.dao.weibo.WeiboDao;
import com.letv.sf.dao.weibo.WeiboDaoByMysql;
import com.letv.sf.dao.slot.CrawlerSlotDao;
import com.letv.sf.dao.slot.CrawlerSlotDaoByMysql;
import com.letv.sf.dao.yuqing.YuqingDao;
import com.letv.sf.dao.yuqing.YuqingDaoByMongo;
import com.letv.sf.entity.beidou.BeidouReport;

/**
 * Created by yangyong3 on 2016/12/1.
 */
public class DaoFactory {

    public static final BeidouDao beidouDao = new BeidouDaoByMysql();

    public static final BeidouDao beidouMongoDao = new BeidouDaoByMongo();

    public static final CrawlerSlotDao weiboSlotDao = new CrawlerSlotDaoByMysql();

    public static final WeiboDao weiboDao = new WeiboDaoByMysql();

    public static final YuqingDao yuqingDao = new YuqingDaoByMongo();

    public static final BeidouReportDao mysqlReport = new BeidouReportDaoByMysql();

    public static final BeidouReportDao mongoReport = new BeidouReportDaoByMongo();

    public static final ToutiaoDao toutiaoDao = new ToutiaoDaoByMongo();
}

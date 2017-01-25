package com.letv.sf.job.baidu;

import com.letv.sf.config.ContentType;
import com.letv.sf.config.SpiderConfig;
import com.letv.sf.crawler.CrawlerFactory;
import com.letv.sf.dao.DaoFactory;
import com.letv.sf.dao.yuqing.mapper.YuqingDocMapper;
import com.letv.sf.dao.yuqing.mapper.YuqingMdataDocMapper;
import com.letv.sf.entity.baidu.BaiduPostEntity;
import com.letv.sf.entity.beidou.BeidouEntity;
import com.letv.sf.http.HttpResult;
import com.letv.sf.parser.ParseFactory;
import com.letv.sf.utils.DateUtils;
import org.apache.commons.collections.CollectionUtils;
import org.quartz.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by yangyong3 on 2016/12/14.
 */

/**
 * 百度贴吧爬取任务
 */
@DisallowConcurrentExecution
public class BaiduPostCrawlerJob implements Job {
    private static final int default_seed = 10;
    private static final int pageSize = 50;

    public void execute(JobExecutionContext context) throws JobExecutionException {
        JobDataMap map = null;
        if (context != null)
            map = context.getJobDetail().getJobDataMap();
        //获取需要抓取的url信息
        List<BeidouEntity> urls = getTopicUrls(map);
        if (CollectionUtils.isEmpty(urls))
            return;

        for (BeidouEntity beidou : urls) {
            process(beidou);
        }
    }

    private void process(BeidouEntity beidou) {
        HttpResult<BeidouEntity> httpResult = null;
        BaiduPostEntity baiduPostEntity = null;
        //翻页爬取贴吧信息
        for (int i = 0; i < SpiderConfig.getBaiduPostPageLimit(); i++) {
            try {
                httpResult = CrawlerFactory.baiduPostCrawler.crawl(beidou.getTag() + "&pn=" + pageSize * i, null);
                Thread.sleep(SpiderConfig.getSleepTimes(default_seed));
                if (httpResult == null)
                    continue;
                httpResult.setMeta(beidou);
                baiduPostEntity = ParseFactory.baiduPostParser.parse(httpResult);
                //存储贴吧基本信息
                if (baiduPostEntity.getMdata() != null) {
                    DaoFactory.yuqingDao.saveOrUpdate(baiduPostEntity.getMdata(), new YuqingMdataDocMapper());
                }
                //存储爬取到的帖子信息
                if (!CollectionUtils.isEmpty(baiduPostEntity.getPosts())) {
                    DaoFactory.yuqingDao.saveWeixinList(baiduPostEntity.getPosts(), new YuqingDocMapper());
                }

            } catch (Exception e) {
                e.printStackTrace();
                try {
                    Thread.sleep(SpiderConfig.getSleepTimes(default_seed));
                } catch (InterruptedException e1) {
                    e1.printStackTrace();
                }
            }
        }
    }

    private List<BeidouEntity> getTopicUrls(JobDataMap map) {
        List<BeidouEntity> urls = new ArrayList<BeidouEntity>();
        if (map == null || map.size() == 0) {
            BeidouEntity entity1 = new BeidouEntity();
            entity1.setBeidou_id(291701001l);
            entity1.setEvent_name("春晚");
            entity1.setTag("http://tieba.baidu.com/f?kw=%E7%9B%B4%E9%80%9A%E6%98%A5%E6%99%9A&ie=utf-8");
            urls.add(entity1);
        } else {
            String pages = map.get("pages").toString();
            String[] temps = pages.split(";");
            for (String url : temps) {
                BeidouEntity entity1 = new BeidouEntity();
                entity1.setBeidou_id(291701001l);
                entity1.setEvent_name("春晚");
                entity1.setTag(url);
                urls.add(entity1);
            }
        }
        List<BeidouEntity> beidouEntities = DaoFactory.beidouMongoDao.getKeywords(ContentType.baidu, DateUtils.dateString(new Date(),SpiderConfig.insert_update_time_format));
        if(!CollectionUtils.isEmpty(beidouEntities))
            urls.addAll(beidouEntities);
        return urls;
    }

    public static void main(String[] args) throws JobExecutionException {
        BaiduPostCrawlerJob job = new BaiduPostCrawlerJob();
        job.execute(null);
    }
}

package com.letv.sf.job.zhihu;

import com.letv.sf.config.ContentType;
import com.letv.sf.config.SpiderConfig;
import com.letv.sf.crawler.CrawlerFactory;
import com.letv.sf.dao.DaoFactory;
import com.letv.sf.dao.yuqing.mapper.YuqingDocMapper;
import com.letv.sf.dao.yuqing.mapper.YuqingMdataDocMapper;
import com.letv.sf.entity.beidou.BeidouEntity;
import com.letv.sf.entity.common.CrawlerResultEntity;
import com.letv.sf.entity.yuqing.YuqingArticle;
import com.letv.sf.entity.yuqing.YuqingMdata;
import com.letv.sf.entity.yuqing.ZhihuPageEntity;
import com.letv.sf.http.HttpResult;
import com.letv.sf.parser.ParseFactory;
import com.letv.sf.utils.DateUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;
import org.quartz.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by yangyong3 on 2016/12/12.
 * 知乎话题抓取任务
 */
@DisallowConcurrentExecution
public class ZhihuTopicCrawlerJob implements Job {
    private static final int default_seed = 10;
    private static final Logger log = Logger.getLogger(ZhihuTopicCrawlerJob.class);

    public void execute(JobExecutionContext context) throws JobExecutionException {
        JobDataMap map = null;
        if (context != null)
            map = context.getJobDetail().getJobDataMap();
        List<BeidouEntity> urls = getTopicUrls(map);
        if (CollectionUtils.isEmpty(urls))
            return;
        for (BeidouEntity beidou : urls) {
            try {
                process(beidou);
                Thread.sleep(SpiderConfig.getSleepTimes(default_seed));
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

    private void process(BeidouEntity beidou) throws Exception {
        HttpResult<BeidouEntity> httpResult = null;
        httpResult = CrawlerFactory.zhihuTopicCrawler.crawl(beidou.getTag(), null);
        if(httpResult == null){
            log.error("爬取url出错,url is -"+beidou.getTag()+",statusCode = "+httpResult.getCode());
            return;
        }
        httpResult.setMeta(beidou);
        ZhihuPageEntity pageEntity = ParseFactory.zhihuTopicParser.parse(httpResult);
        YuqingMdata mdata = pageEntity.getYuqingMdata();
        //解析所有的话题基本信息和问题并保存
        CrawlerResultEntity<YuqingArticle> questions = pageEntity.getQuestions();
        if (mdata != null)
            DaoFactory.yuqingDao.saveOrUpdate(mdata, new YuqingMdataDocMapper());
        if (questions != null) {
            DaoFactory.yuqingDao.saveWeixinList(questions.getItems(), new YuqingDocMapper());
        }
    }

    private List<BeidouEntity> getTopicUrls(JobDataMap map) {
        List<BeidouEntity> urls = new ArrayList<BeidouEntity>();
        if (map == null || map.size() == 0) {
            BeidouEntity entity1 = new BeidouEntity();
            entity1.setBeidou_id(291701001l);
            entity1.setEvent_name("春晚");
            entity1.setTag("https://www.zhihu.com/topic/20038208/newest");
            urls.add(entity1);
            BeidouEntity entity2 = new BeidouEntity();
            entity2.setBeidou_id(291701001l);
            entity2.setEvent_name("春晚");
            entity2.setTag("https://www.zhihu.com/topic/20038208/top-answers");
            urls.add(entity2);
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
        List<BeidouEntity> zhihu_topics = null;
        try{
            zhihu_topics = DaoFactory.beidouMongoDao.getKeywords(ContentType.zhihu, DateUtils.dateString(new Date(),SpiderConfig.insert_update_time_format));
        }catch (Exception e){
            e.printStackTrace();
        }
        if(!CollectionUtils.isEmpty(zhihu_topics))
            urls.addAll(zhihu_topics);
        return urls;
    }

    public static void main(String[] args) throws JobExecutionException {
        ZhihuTopicCrawlerJob job = new ZhihuTopicCrawlerJob();
        job.execute(null);
    }
}

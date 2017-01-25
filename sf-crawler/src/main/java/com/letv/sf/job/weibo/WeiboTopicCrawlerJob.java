package com.letv.sf.job.weibo;

import com.letv.sf.config.ContentType;
import com.letv.sf.config.SpiderConfig;
import com.letv.sf.counter.CounterTool;
import com.letv.sf.counter.CounterType;
import com.letv.sf.crawler.CrawlerFactory;
import com.letv.sf.dao.DaoFactory;
import com.letv.sf.entity.beidou.BeidouEntity;
import com.letv.sf.entity.common.CrawlerResultEntity;
import com.letv.sf.entity.weibo.BeidouMapping;
import com.letv.sf.entity.weibo.WeiboTopicEventInfo;
import com.letv.sf.http.HttpResult;
import com.letv.sf.mq.MessageFactory;
import com.letv.sf.parser.ParseFactory;
import com.letv.sf.utils.RegUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import weibo4j.model.Status;
import weibo4j.model.User;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by yangyong3 on 2016/12/2.
 * 微博topic抓取任务
 */
@DisallowConcurrentExecution
public class WeiboTopicCrawlerJob implements Job {

    private static final Logger log = Logger.getLogger(WeiboTopicCrawlerJob.class);

    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        try {
            BeidouEntity keyword = MessageFactory.sub(SpiderConfig.getTopicKeyWordQueueName(), BeidouEntity.class);
            if (keyword != null) {
                crawlTopic(keyword);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void crawlTopic(BeidouEntity keyword) throws Exception {
        String url = SpiderConfig.getSinaWeiboTopicApiUrl() + "?&q=" + URLEncoder.encode(keyword.getTag(), "utf-8");
        CounterTool.count(CounterType.weibo_topic_request_counter,1);
        HttpResult result = CrawlerFactory.topicCrawler.crawl(url, null);
        if (result == null) {
            log.error("爬取url出错,url is -" + url);
            return;
        }
        CrawlerResultEntity<Status> rs = ParseFactory.topicParser.parse(result);
        List<Status> list = rs.getItems();
        BeidouMapping mapping = new BeidouMapping();
        WeiboTopicEventInfo event = null;
        String topicName = null;
        int event_id = 0;
        List<Status> saveStatuses = new ArrayList<Status>();
        List<User> saveUsers = new ArrayList<User>();
        if (!CollectionUtils.isEmpty(list)) {
            CounterTool.count(CounterType.weibo_topic_counter,list.size());
            boolean exist = false;
            for (Status status : list) {
                exist = DaoFactory.weiboDao.existWeibo(Long.parseLong(status.getMid()));
                if (!exist) {
                    //解析出topic
                    topicName = com.letv.sf.utils.StringUtils.findTwoCharContent(RegUtils.wordNumChar(status.getText()), "#");
                    //保存topic
                    event = DaoFactory.weiboDao.exsitTopicEventInfo(topicName);
                    if (event == null) {
                        event = new WeiboTopicEventInfo();
                        event.setBeidou_id(keyword.getBeidou_id());
                        event.setStatus("A");
                        event.setType("wbtopic");
                        event.setZip_value(topicName);
                        event_id = DaoFactory.weiboDao.insertTopicEventInfo(event);
                    }
                    status.setTopic_id(event_id);
                    status.setBeidou_id(keyword.getBeidou_id());
                    status.setBeidou_name(keyword.getEvent_name());
                    status.setType(ContentType.topic.toString());
                    status.setUser_id(Long.parseLong(status.getId()));
                    if (status.getUser() != null && status.getAvatar() == null) {
                        status.setAvatar(status.getUser().getAvatarLarge());
                    }
                    //保存话题微博
                    //DaoFactory.weiboDao.saveWeibo(status);
                    saveStatuses.add(status);
                    if (status.getUser() != null && !StringUtils.isEmpty(status.getUser().getId())) {
                        exist = DaoFactory.weiboDao.existUser(Long.parseLong(status.getUser().getId()));
                        if (!exist) {
                            mapping.setBeidou_id(keyword.getBeidou_id() + "");
                            if (mapping.getBeidou_id().equals("291701001"))
                                mapping.setCrawl_source(ContentType.chunwan.toString());
                            else
                                mapping.setCrawl_source(ContentType.movie.toString());
                            mapping.setCrawl_type(ContentType.topic.toString());
                            saveUsers.add(status.getUser());
//                            DaoFactory.weiboDao.saveWeiboUser(status.getUser());
//                            DaoFactory.beidouDao.saveBeidouMapping(mapping, status.getUser());
                        }
                    }
                }
            }
            DaoFactory.weiboDao.saveWeibos(saveStatuses);
            DaoFactory.weiboDao.saveWeiboUers(saveUsers);
            DaoFactory.beidouDao.saveBeidouMappings(mapping, saveUsers);
        }
    }
    public static void main(String[] args) throws JobExecutionException {
        WeiboTopicCrawlerJob job = new WeiboTopicCrawlerJob();
        job.execute(null);
    }
}

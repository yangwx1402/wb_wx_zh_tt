package com.letv.sf.job.thread.weibo;

import com.letv.sf.config.ContentType;
import com.letv.sf.config.SpiderConfig;
import com.letv.sf.crawler.CrawlerFactory;
import com.letv.sf.dao.DaoFactory;
import com.letv.sf.entity.beidou.BeidouEntity;
import com.letv.sf.http.HttpResult;
import com.letv.sf.mq.MessageFactory;
import com.letv.sf.parser.ParseFactory;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import weibo4j.model.Status;
import weibo4j.model.User;

/**
 * Created by yangyong3 on 2016/12/22.
 */
public class OfficialWeiboCrawlerThread implements Runnable {

    private static final Logger log = Logger.getLogger(OfficialWeiboCrawlerThread.class);

    private static final int default_seed = 10;

    public void run() {
        BeidouEntity uid = null;
        while (true) {
            try {
                crawlOfficialUser();
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

    private void crawlOfficialUser() throws Exception {
        BeidouEntity beidou = MessageFactory.sub(SpiderConfig.getWeiboOfficialQueueName(), BeidouEntity.class);
        if (beidou == null)
            return;
        String url = SpiderConfig.getSinaWeiboUserApiUrl() + "?screen_name=" + beidou.getTag();
        HttpResult httpResult = CrawlerFactory.weiboUserCrawler.crawl(url, null);
        if (httpResult == null) {
            log.error("爬取url出错,url is -" + url);
            return;
        }
        User user = ParseFactory.weiboUserParser.parse(httpResult);
        boolean flag = false;
        Status status = null;
        if (user != null) {
            flag = DaoFactory.weiboDao.existUser(Long.parseLong(user.getId()));
            if (!flag) {
                DaoFactory.weiboDao.saveWeiboUser(user);
            }
            status = user.getStatus();
            log.info("crawl official user weibo is -" + status);
            if (status != null && !StringUtils.isBlank(status.getId())) {
                status.setUser_id(Long.parseLong(user.getId()));
                status.setScreen_name(user.getScreenName());
                status.setBeidou_id(beidou.getBeidou_id());
                status.setBeidou_name(beidou.getEvent_name());
                status.setType(ContentType.user.toString());
                if (status.getAvatar() == null)
                    status.setAvatar(user.getAvatarLarge());
                flag = DaoFactory.weiboDao.existWeibo(Long.parseLong(status.getId()));
                if (!flag) {
                    DaoFactory.weiboDao.saveWeibo(status);
                }
            }
        }
    }

}

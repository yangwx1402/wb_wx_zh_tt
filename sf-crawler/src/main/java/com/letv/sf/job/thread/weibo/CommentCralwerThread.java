package com.letv.sf.job.thread.weibo;

import com.letv.sf.config.ContentType;
import com.letv.sf.config.SpiderConfig;
import com.letv.sf.crawler.CrawlerFactory;
import com.letv.sf.dao.DaoFactory;
import com.letv.sf.entity.common.CrawlerResultEntity;
import com.letv.sf.entity.weibo.BeidouMapping;
import com.letv.sf.http.HttpResult;
import com.letv.sf.mq.MessageFactory;
import com.letv.sf.parser.ParseFactory;
import com.letv.sf.utils.JsonUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.jsoup.Jsoup;
import weibo4j.model.Comment;
import weibo4j.model.User;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by young.yang on 2016/11/29.
 */

/**
 * 微博评论爬取线程
 */
public class CommentCralwerThread implements Runnable {
    private static final int default_seed = 10;
    private static final Logger log = Logger.getLogger(CommentCralwerThread.class);

    public void run() {
        BeidouMapping mid = null;
        while (true) {
            try {
                mid = MessageFactory.sub(SpiderConfig.getWeiboCommentQueueName(), BeidouMapping.class);
                if (mid != null)
                    crawlComment(mid);
                Thread.sleep(SpiderConfig.getSleepTimes(default_seed));
            } catch (Exception e) {
                e.printStackTrace();
                log.error("crawl weibo comment error ", e);
                try {
                    Thread.sleep(SpiderConfig.getSleepTimes(default_seed));
                } catch (InterruptedException e1) {
                    e1.printStackTrace();
                }
            }
        }
    }

    private void crawlComment(BeidouMapping mid) throws Exception {
        int pageNum = 1;
        String url = null;
        HttpResult result = null;
        CrawlerResultEntity<Comment> entity = null;
        List<Comment> comments = null;
        while (true) {
            url = SpiderConfig.getSinaWeiboCommetApiUrl() + "?id=" + mid.getMid() + "&count=" + SpiderConfig.getSinaWeiboCommentLimitNum() + "&page=" + pageNum;
            result = CrawlerFactory.weiboCommentCrawler.crawl(url, null);
            if (result == null)
                break;
            entity = ParseFactory.weiboCommentParser.parse(result);
            comments = entity.getItems();
            List<Comment> saveComments = new ArrayList<Comment>();
            Set<User> saveUsers = new HashSet<User>();
            if (!CollectionUtils.isEmpty(comments)) {
                boolean flag = false;
                for (Comment comment : comments) {
                    flag = DaoFactory.weiboDao.existComment(comment.getId());
                    //存储微博评论
                    if (!flag) {
                        comment.setBeidou_id(Long.parseLong(mid.getBeidou_id()));
                        comment.setSource_mid(mid.getMid());
                        if (!StringUtils.isBlank(comment.getSource()))
                            comment.setSource(Jsoup.parse(comment.getSource()).text());
                        saveComments.add(comment);
                        //DaoFactory.weiboDao.saveComment(comment);
                    }
                    //随便存储评论用户
                    flag = DaoFactory.weiboDao.existUser(Long.parseLong(comment.getUser().getId()));
                    if (!flag) {
                        saveUsers.add(comment.getUser());
                        //DaoFactory.weiboDao.saveWeiboUser(comment.getUser());
                        if (mid.getBeidou_id().equals("291701001"))
                            mid.setCrawl_source(ContentType.chunwan.toString());
                        else
                            mid.setCrawl_source(ContentType.movie.toString());
                        mid.setCrawl_type(ContentType.comment.toString());

                    }
                }
                DaoFactory.weiboDao.saveComments(saveComments);
                DaoFactory.weiboDao.saveWeiboUers(saveUsers);
                DaoFactory.beidouDao.saveBeidouMappings(mid, saveUsers);
                pageNum++;
            } else {
                //这里说明页码分完了
                log.info("crawl comment url  = " + url + ", page end size is -" + pageNum);
                break;
            }
            Thread.sleep(SpiderConfig.getSleepTimes(default_seed));
        }
        //修改微博状态，说明已经抓取过评论
        DaoFactory.weiboDao.updateState(mid.getMid(), SpiderConfig.getSinaWeiboSearchFetchedComment());
    }


}

package com.letv.sf.crawler;

import com.letv.sf.crawler.weibo.WeiboCommentCrawler;
import com.letv.sf.entity.beidou.BeidouEntity;
import com.letv.sf.entity.common.CrawlerResultEntity;
import com.letv.sf.http.HttpResult;
import com.letv.sf.parser.Parse;
import com.letv.sf.parser.weibo.WeiboCommentParser;
import org.apache.commons.io.FileUtils;
import weibo4j.model.Comment;

import java.io.File;

/**
 * Created by yangyong3 on 2016/12/1.
 */
public class WeiboCommentCrawlerExample {
    public static void main(String[] args) throws Exception {
        Crawl crawl = new WeiboCommentCrawler();
        String url = "https://api.weibo.com/2/comments/show.json?id=4054056459060803&count=150&source=140226478";
        HttpResult result = crawl.crawl(url,null);
        FileUtils.writeStringToFile(new File("E:\\json1.txt"),result.getContent(),"utf-8");
        Parse<CrawlerResultEntity<Comment>,BeidouEntity> parse = new WeiboCommentParser();
        CrawlerResultEntity<Comment> entity = parse.parse(result);
        for(Comment comment:entity.getItems()){
            System.out.println(comment.getId());
        }
    }
}

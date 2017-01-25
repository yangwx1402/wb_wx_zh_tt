package com.letv.sf.crawler;

import com.letv.sf.entity.common.CrawlerResultEntity;
import com.letv.sf.http.HttpResult;
import com.letv.sf.parser.weibo.WeiboCommentParser;
import org.apache.commons.io.FileUtils;
import weibo4j.model.Comment;

import java.io.File;

/**
 * Created by yangyong3 on 2016/12/19.
 */
public class WeiboCommentParserExample {
    public static void main(String[] args) throws Exception {
        String json = FileUtils.readFileToString(new File("E:\\json.txt"), "utf-8");
        HttpResult httpResult = new HttpResult();
        httpResult.setContent(json);
        WeiboCommentParser parser = new WeiboCommentParser();
        System.out.println(httpResult.getContent());
        CrawlerResultEntity<Comment> comments = parser.parse(httpResult);
        System.out.println(comments.getItems().size());
    }
}

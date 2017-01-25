package com.letv.sf.crawler;

import com.letv.sf.entity.common.CrawlerResultEntity;
import com.letv.sf.http.HttpResult;
import com.letv.sf.parser.weibo.KeywordSearchParser;
import org.apache.commons.io.FileUtils;
import weibo4j.model.Status;

import java.io.File;

/**
 * Created by yangyong3 on 2016/11/30.
 */
public class KeywordSearchParserExample {
    public static void main(String[] args) throws Exception {
        KeywordSearchParser parser = new KeywordSearchParser();
        HttpResult result = new HttpResult();
        result.setContent(FileUtils.readFileToString(new File("E:\\test.html"),"utf-8"));
        CrawlerResultEntity<Status> entityt = parser.parse(result);
        System.out.println(entityt.getItems().get(0).getText());
    }
}

package com.letv.sf.parser.toutiao;

import com.letv.sf.config.CrawlerSlotType;
import com.letv.sf.config.SpiderConfig;
import com.letv.sf.entity.common.HttpToolGroup;
import com.letv.sf.entity.toutiao.ToutiaoArticle;
import com.letv.sf.http.HttpResult;
import com.letv.sf.http.HttpToolFactory;
import com.letv.sf.parser.Parse;
import com.letv.sf.utils.DateUtils;
import com.letv.sf.utils.JsonUtils;
import com.letv.sf.utils.TextExtract;
import org.apache.log4j.Logger;


import java.util.*;

/**
 * Created by yangyong3 on 2017/1/5.
 */
public class ToutiaoPageCommentParser implements Parse<ToutiaoArticle, ToutiaoArticle> {

    private static final Logger log = Logger.getLogger(ToutiaoPageCommentParser.class);

    private static final int default_seed = 10;

    public ToutiaoArticle parse(HttpResult<ToutiaoArticle> httpPage) throws Exception {
        if (httpPage == null)
            return null;
        List<Map<String, Object>> comments = new ArrayList<Map<String, Object>>();
        ToutiaoArticle article = httpPage.getMeta();
        article.setText(getPageText(httpPage.getContent(), article.getUrl()));
        article.setState(SpiderConfig.getSinaWeiboSearchFetchedComment());
        parseComments(article, 0, comments);
        article.setComments(comments);
        return article;
    }

    private void parseComments(ToutiaoArticle article, int offset, List<Map<String, Object>> list) {
        StringBuffer url = new StringBuffer(100);
        url.append(SpiderConfig.getToutiaoPageCommentUrl() + "?group_id=" + article.getGroup() + "&item_id=" + article.getId() + "&offset=" + offset + "&count=" + SpiderConfig.getToutiaoKeywordSearchPageSize());
        log.info("crawl toutiao comment id = " + article.getId() + ", url is -[" + url.toString() + "]");
        HttpResult httpRsult = null;
        HttpToolGroup group = HttpToolFactory.getHttpToolGroupByType(CrawlerSlotType.common.toString());
        Map<String, Object> resultMap = null;
        try {
            httpRsult = group.getUrlByJsoup(url.toString(), true);
            if (httpRsult == null)
                return;
            resultMap = JsonUtils.fromJson(httpRsult.getContent(), Map.class);
            if (resultMap.containsKey("message")) {
                //请求结果错误，就返回
                if (!resultMap.get("message").toString().equals("success")) {
                    return;
                }
                //没有data就返回
                if (!resultMap.containsKey("data")) {
                    return;
                }
                Map<String, Object> data = (Map<String, Object>) resultMap.get("data");
                //没有更多评论了 也返回
                if (!data.containsKey("has_more"))
                    return;
                Boolean has_more = Boolean.parseBoolean(data.get("has_more").toString());
                List<Map<String, Object>> comments = (List<Map<String, Object>>) data.get("comments");
                Map<String, Object> copy = null;
                if (comments != null && comments.size() > 0) {
                    for (Map<String, Object> temp : comments) {
                        copy = new HashMap<String, Object>();
                        for (Map.Entry<String, Object> entry : temp.entrySet()) {
                            if (!SpiderConfig.getToutiaoPageCommentRemoveJsonFields().contains(entry.getKey())) {
                                copy.put(entry.getKey(), entry.getValue());
                            }
                        }
                        copy.put("beidou_id",article.getBeidou_id());
                        copy.put("toutiao_aid",article.getId());
                        copy.put("insert_time", DateUtils.dateString(new Date(),SpiderConfig.insert_update_time_format));
                        copy.put("created_at",DateUtils.dateString(new Date(System.currentTimeMillis()-Long.parseLong(copy.get("create_time").toString())),SpiderConfig.created_time_format));
                        copy.put("created_at_long",System.currentTimeMillis()-Long.parseLong(copy.get("create_time").toString()));
                        list.add(copy);
                    }
                }

                //超过了系统配置的最大评论采集数目后也返回
                if (list.size() > SpiderConfig.getToutiaoPageCommentSizeLimit())
                    return;
                if (has_more) {
                    //步子不要太大,容易扯着蛋
                    Thread.sleep(SpiderConfig.getSleepTimes(default_seed));
                    parseComments(article, offset + SpiderConfig.getToutiaoKeywordSearchPageSize(), list);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.error("parser comment error ,url is -[" + url + "] error message is ", e);
            return;
        }
    }

    private String getPageText(String content, String url) {
        TextExtract textExtract = new TextExtract();
        String text = null;
        try {
            text = textExtract.parse(content).replaceAll("<[^>]*>", "");
        } catch (Exception e) {
            e.printStackTrace();
            log.error("TextExtract content ,url is -[" + url + "] error message is ", e);
        }
        return text;
    }
}

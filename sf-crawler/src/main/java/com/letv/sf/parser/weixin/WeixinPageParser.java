package com.letv.sf.parser.weixin;

import com.letv.sf.config.CrawlerSlotType;
import com.letv.sf.config.SpiderConfig;
import com.letv.sf.config.YuqingType;
import com.letv.sf.entity.beidou.BeidouEntity;
import com.letv.sf.entity.common.HttpToolGroup;
import com.letv.sf.entity.yuqing.YuqingArticle;
import com.letv.sf.entity.yuqing.YuqingComment;
import com.letv.sf.http.HttpResult;
import com.letv.sf.http.HttpToolFactory;
import com.letv.sf.parser.Parse;
import com.letv.sf.utils.*;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import weibo4j.org.json.JSONArray;
import weibo4j.org.json.JSONObject;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by yangyong3 on 2016/12/6.
 * 微信文章页解析
 */
public class WeixinPageParser implements Parse<YuqingArticle, BeidouEntity> {

    public YuqingArticle parse(HttpResult<BeidouEntity> httpPage) throws Exception {
        Document document = JsoupUtils.getDocument(httpPage.getContent());
        YuqingArticle article = new YuqingArticle();
        Element authorElement = JsoupUtils.selectFirstElement(document, "span.rich_media_meta.rich_media_meta_text.rich_media_meta_nickname");
        if(authorElement==null)
            throw new Exception("链接已过期或者被封");
        String author = authorElement.text();
        Element titleElement = JsoupUtils.selectFirstElement(document, "h2#activity-name.rich_media_title");
        String title = titleElement.text();
        Element authorInfoElement = JsoupUtils.selectFirstElement(document,"div.rich_media_meta_list");
        if(authorInfoElement!=null){
            article.setAuthorInfo(authorInfoElement.html());
        }
        article.setSource(YuqingType.WEIXIN.getName());
        article.setAuthor(author.trim());
        article.setTitle(RegUtils.wordNumChar(title).trim());
        article.setMd5(MD5.md5(article.getAuthor()+"_"+article.getTitle()));
        article.setYuqing_id(article.getSource()+"_"+article.getMd5());

        article.setBody(document.body().text());
        String url = httpPage.getUrl();
        String comment_get_url = "";
        if (url.contains("?")) {
            comment_get_url += SpiderConfig.getWeixinCommentUrl() + url.split("\\?")[1];
        }
        HttpToolGroup group = HttpToolFactory.getHttpToolGroupByType(CrawlerSlotType.weixin.toString());
        HttpResult commentResult = group.getUrlByJsoup(comment_get_url,true);
        JSONObject jsonObject = new JSONObject(commentResult.getContent());
        JSONArray array = null;
        List<YuqingComment> comments = null;
        YuqingComment comment = null;
        JSONObject temp = null;
        if (jsonObject.has("comment")) {
            array = (JSONArray) jsonObject.get("comment");
            comments = new ArrayList<YuqingComment>();
            for (int i = 0; i < array.length(); i++) {
                temp = (JSONObject) array.get(i);
                comment = new YuqingComment();
                comment.setAttitudes_count(temp.getInt("like_num"));
                comment.setAuthor(temp.getString("nick_name"));
                comment.setBody(temp.getString("content"));
                long time = System.currentTimeMillis()-temp.getLong("create_time");
                comment.setCreated_at(DateUtils.dateString(new Date(time),SpiderConfig.created_time_format));
                comment.setCreated_long(time);
                comment.setInsert_time(DateUtils.dateString(new Date(),SpiderConfig.insert_update_time_format));
                comment.setSource(YuqingType.WEIXIN_COMMENT.getName());
                comment.setYqpid(article.getYuqing_id());
                comment.setYqrpid(comment.getSource()+"_"+temp.getString("content_id"));
                comments.add(comment);
            }
        }
        article.setComments(comments);
        if (jsonObject.has("read_num")) {
            article.setReadCount(jsonObject.getInt("read_num"));
        }
        if (jsonObject.has("like_num")) {
            article.setAttitudes_count(jsonObject.getInt("like_num"));
        }
        return article;
    }
}

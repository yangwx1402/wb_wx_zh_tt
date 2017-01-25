package com.letv.sf.parser.zhihu;

import com.letv.sf.config.CrawlerSlotType;
import com.letv.sf.config.SpiderConfig;
import com.letv.sf.config.YuqingType;
import com.letv.sf.entity.beidou.BeidouEntity;
import com.letv.sf.entity.common.HttpToolGroup;
import com.letv.sf.entity.yuqing.YuqingArticle;
import com.letv.sf.entity.yuqing.YuqingComment;
import com.letv.sf.http.HttpResult;
import com.letv.sf.http.HttpToolFactory;
import com.letv.sf.utils.DateUtils;
import weibo4j.org.json.JSONArray;
import weibo4j.org.json.JSONException;
import weibo4j.org.json.JSONObject;

import java.text.ParseException;
import java.util.Date;
import java.util.List;

/**
 * Created by yangyong3 on 2016/12/12.
 */
public abstract class ZhihuBaseParse {
    private static final int default_seed = 10;
    protected int findAnswerCount(String text) {
        String result = findBySplitString(text, "个",0);
        return Integer.parseInt(result);
    }

    protected int findAttitudeCount(String text) {
        return Integer.parseInt(text.replaceAll("k", "000").replaceAll("K", "000"));
    }

    protected int findCommentCount(String text) {
        String result = findBySplitString(text, "条",0);
        return Integer.parseInt(result);
    }

    protected long findPublishTime(String text) {
        String result = findBySplitString(text, "于",1);
        if(result.length()==5){
            result = DateUtils.dateString(new Date(),"yyyy-MM-dd");
        }
        try {
            Date date = DateUtils.parseDate(result, "yyyy-MM-dd");
            return date.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
            return System.currentTimeMillis();
        }
    }

    private String findBySplitString(String text, String split,int index) {
        String[] temp = null;
        if (text.contains(split)) {
            temp = text.split(split);
            return temp[index].trim();
        }
        return "0";
    }

    protected String findCommentListId(String name) {
        return name.split("-")[0];
    }

    protected List<YuqingComment> getComments(List<YuqingComment> comments,String commentListId, int pageNum, BeidouEntity beidou) {
        HttpToolGroup group = HttpToolFactory.getHttpToolGroupByType(CrawlerSlotType.zhihu.toString());
        String url = SpiderConfig.getZhihuAnswerCommentUrl() + commentListId + "/comments?" + "&page=" + pageNum;
        HttpResult httpResult = null;
        JSONObject jsonObject = null;
        int total = 0;
        int pageSize = 0;
        int nowPage = 0;
        try {
            Thread.sleep(SpiderConfig.getSleepTimes(default_seed));
            httpResult = group.getUrlByJsoup(url,true);
            jsonObject = new JSONObject(httpResult.getContent());
            if (jsonObject.has("paging")) {
                JSONObject page = jsonObject.getJSONObject("paging");
                total = page.getInt("totalCount");
                pageSize = page.getInt("perPage");
                nowPage = page.getInt("currentPage");
            }
            YuqingComment comment = null;
            JSONArray array = null;
            if (jsonObject.has("data")) {
                array = jsonObject.getJSONArray("data");
                for (int i = 0; i < array.length(); i++) {
                    comment = getComment((JSONObject) array.get(i),beidou);
                    comments.add(comment);
                }
            }
            int count = total % pageSize == 0 ? total / pageSize : total / pageSize + 1;
            if (nowPage == count) {
                return comments;
            } else {
                getComments(comments, commentListId, nowPage + 1,beidou);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return comments;
        }
        return comments;
    }

    private YuqingComment getComment(JSONObject jsonObject,BeidouEntity beidou) throws JSONException, ParseException {
        YuqingComment comment = new YuqingComment();
        comment.setAttitudes_count(jsonObject.getInt("likesCount"));
        JSONObject user = jsonObject.getJSONObject("author");
        comment.setAuthor(user.getString("name"));
        comment.setBody(jsonObject.getString("content"));
        comment.setInsert_time(DateUtils.dateString(new Date(),SpiderConfig.insert_update_time_format));
        comment.setSource(YuqingType.ZHIHU_ANSNER_COMMENT.getName());
        long time = DateUtils.parseDate(jsonObject.getString("createdTime").replaceAll("T", " "), "yyyy-MM-dd HH:mm:ss").getTime();
        comment.setCreated_at(DateUtils.dateString(new Date(time),SpiderConfig.created_time_format));
        comment.setCreated_long(time);
        comment.setBeidou_id(beidou.getBeidou_id());
        comment.setYqrpid(comment.getSource()+"_"+jsonObject.getLong("id") + "");
        return comment;
    }

}

package com.letv.sf.parser.weibo;

import com.letv.sf.entity.beidou.BeidouEntity;
import com.letv.sf.entity.common.SpiderStatus;
import com.letv.sf.entity.common.CrawlerResultEntity;
import com.letv.sf.http.HttpResult;
import com.letv.sf.parser.Parse;
import com.letv.sf.utils.JsoupUtils;
import org.apache.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import weibo4j.model.Source;
import weibo4j.model.Status;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by yangyong3 on 2016/11/30.
 * 微博关键词搜索解析
 */
public class KeywordSearchParser implements Parse<CrawlerResultEntity<Status>,BeidouEntity> {

    private static final Logger log = Logger.getLogger(KeywordSearchParser.class);

    public CrawlerResultEntity<Status> parse(HttpResult httpPage) throws Exception {
        CrawlerResultEntity<Status> result = new CrawlerResultEntity<Status>();
        Document document = Jsoup.parse(httpPage.getContent());
        Element yanzhengma = JsoupUtils.selectFirstElement(document,"div.code_ver");
        if(yanzhengma!=null){
            result.setStatus(SpiderStatus.YANGZHENGMA);
            return result;
        }
        Elements noResult = document.select("p.noresult_tit");
        if (noResult != null && noResult.size() != 0) {
            result.setStatus(SpiderStatus.NO_RESULT);
            return result;
        }
        Element parentDiv = document.getElementById("pl_weibo_direct");
        if(parentDiv==null)
            return result;
        Elements element = parentDiv.select("div.WB_cardwrap.S_bg2.clearfix");

        if (element == null || element.size() == 0) {
            result.setStatus(SpiderStatus.NO_RESULT);
            return result;
        }
        Status status = null;
        List<Status> statusList = new ArrayList<Status>();
        Element tempElements = null;
        for (int i = 0; i < element.size(); i++) {
            Element temp = element.get(i);
            tempElements = JsoupUtils.getFirstElementByAttribute(temp, "action-type", "feed_list_item");
            if (tempElements == null)
                continue;
            else {
                status = new Status();
                //抽取mid
                status.setMid(tempElements.attr("mid"));
                status.setId(status.getMid());
                status.setIdstr(Long.parseLong(status.getMid()));
                //抽取发布者名称
                Elements user = tempElements.select("a.W_texta.W_fb");
                status.setScreen_name(user.text());

                Elements source = tempElements.select("div.feed_from.W_textb");
                Source source1 = null;
                if (source.text().contains("来自")) {
                    source1 = new Source(source.text().split("来自")[1], "");
                } else {
                    source1 = new Source(source.text(), "");
                }

                Element touElement = JsoupUtils.selectFirstElement(temp,"img.W_face_radius");
                status.setAvatar(touElement.attr("src"));
                status.setSource(source1);
                Elements contents = tempElements.select("p.comment_txt");
                Element content = contents.get(0);
                Elements times = tempElements.select("a.W_textb");
                Element time = times.get(0);
                //抽取微博内容
                status.setText(content.text());
                //发布时间
                status.setCreatedAt(new Date(Long.parseLong(time.attr("date"))));
                Elements repost_comments = tempElements.select("div.feed_action.clearfix");
                Elements rs_comments = repost_comments.get(0).select("li");
                String repost = rs_comments.get(1).text().replaceAll("转发", "").trim();
                Element urlElement = JsoupUtils.selectFirstElement(rs_comments.get(1),"a");
                String url = urlElement.attr("action-data");
                String[] temps = url.split("&");
                String[] temp1 = null;
                if(temps!=null&&temps.length>0){
                    for(String line:temps){
                        temp1 = line.split("=");
                        if(temp1!=null&&temp1.length>0){
                            if(temp1[0].equals("uid")){
                                long uid = Long.parseLong(temp1[1].trim());
                                status.setUser_id(uid);
                                log.info("parse uid = "+uid);
                            }
                        }
                    }
                }

                //转发
                if (!"".equals(repost)) {
                    status.setRepostsCount(Integer.parseInt(repost));
                }
                //评论
                String comment = rs_comments.get(2).text().replaceAll("评论", "").trim();
                if (!"".equals(comment)) {
                    status.setCommentsCount(Integer.parseInt(comment));
                }
                //赞
                String zan = rs_comments.get(3).text().trim();
                if (!"".equals(zan)) {
                    status.setAttitudesCount(Integer.parseInt(zan));
                }
                statusList.add(status);
            }
        }
        result.setItems(statusList);
        return result;
    }
}

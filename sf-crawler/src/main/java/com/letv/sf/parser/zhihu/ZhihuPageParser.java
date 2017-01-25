package com.letv.sf.parser.zhihu;

import com.letv.sf.config.CrawlerSlotType;
import com.letv.sf.config.SpiderConfig;
import com.letv.sf.config.YuqingState;
import com.letv.sf.config.YuqingType;
import com.letv.sf.entity.beidou.BeidouEntity;
import com.letv.sf.entity.common.CrawlerResultEntity;
import com.letv.sf.entity.yuqing.YuqingArticle;
import com.letv.sf.entity.yuqing.YuqingComment;
import com.letv.sf.entity.zhihu.ZhihuQuestionEntity;
import com.letv.sf.http.HttpResult;
import com.letv.sf.http.HttpToolFactory;
import com.letv.sf.parser.Parse;
import com.letv.sf.utils.DateUtils;
import com.letv.sf.utils.JsoupUtils;
import com.letv.sf.utils.MD5;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by yangyong3 on 2016/12/9.
 * 知乎问题页解析
 */
public class ZhihuPageParser extends ZhihuBaseParse implements Parse<ZhihuQuestionEntity, BeidouEntity> {
    public ZhihuQuestionEntity parse(HttpResult<BeidouEntity> httpPage) throws Exception {
        ZhihuQuestionEntity entity = new ZhihuQuestionEntity();
        CrawlerResultEntity<YuqingArticle> resultEntity = new CrawlerResultEntity<YuqingArticle>();
        Document document = Jsoup.parse(httpPage.getContent());
        YuqingArticle question = parseQuestion(document, httpPage.getMeta(), httpPage.getUrl());
        List<YuqingArticle> answers = parseAnswers(document, httpPage.getMeta(), httpPage.getUrl());
        resultEntity.setItems(answers);
        entity.setAnswers(resultEntity);
        entity.setQuestion(question);
        return entity;
    }

    private List<YuqingArticle> parseAnswers(Document document, BeidouEntity meta, String question_url) {
        List<YuqingArticle> articles = new ArrayList<YuqingArticle>();
        Element answerElement = document.getElementById("zh-question-answer-wrap");
        if (answerElement == null)
            return articles;
        Elements answerElements = JsoupUtils.selectElements(answerElement, "div.zm-item-answer.zm-item-expanded");
        if (answerElements == null || answerElements.size() == 0)
            return articles;
        YuqingArticle article = null;
        Element answer = null;
        for (int i = 0; i < answerElements.size(); i++) {
            answer = answerElements.get(i);
            article = parseAnswer(answer, meta, question_url);
            if (article != null)
                articles.add(article);
        }
        return articles;
    }

    private YuqingArticle parseAnswer(Element answer, BeidouEntity meta, String question_url) {
        YuqingArticle article = new YuqingArticle();
        List<YuqingComment> comments = new ArrayList<YuqingComment>();
        article.setSource(YuqingType.ZHIHU.getName());
        article.setYqid_answer(YuqingType.ZHIHU.getName() + "_" + MD5.md5(question_url));
        Element authorElement = JsoupUtils.selectFirstElement(answer, "a.author-link");
        if (authorElement != null) {
            article.setAuthor(authorElement.text());

        }
        Element zanElement = JsoupUtils.selectFirstElement(answer, "span.js-voteCount");
        if (zanElement != null) {
            int zanCount = Integer.parseInt(zanElement.text());
            article.setAttitudes_count(zanCount);

        }
        Element contentElement = JsoupUtils.selectFirstElement(answer, "div.zm-item-rich-text.expandable.js-collapse-body");
        if (contentElement != null) {
            String url = "https://www.zhihu.com" + contentElement.attr("data-entry-url");
            article.setUrl(url);

            article.setMd5(MD5.md5(article.getUrl()));
            Element descElement = JsoupUtils.selectFirstElement(contentElement, "div.zh-summary.summary.clearfix");
            if (descElement != null) {
                article.setDescription(descElement.text());
            }
            Element bodyElement = JsoupUtils.selectFirstElement(contentElement, "div.zm-editable-content.clearfix");
            if (bodyElement != null) {
                article.setBody(bodyElement.text());
            }
        }
        Element footerElement = JsoupUtils.selectFirstElement(answer, "div.zm-item-meta.answer-actions.clearfix.js-contentActions");
        if (footerElement != null) {
            Element commentCountElement = JsoupUtils.selectFirstElement(footerElement, "a.meta-item.toggle-comment.js-toggleCommentBox");
            if (commentCountElement != null) {
                int commentCount = findCommentCount(commentCountElement.text());
                article.setReplyCount(commentCount);
            }
            Element publishTimeElement = JsoupUtils.selectFirstElement(footerElement, "a.answer-date-link.meta-item");
            if (publishTimeElement != null) {
                long timestamp = findPublishTime(publishTimeElement.text());
                article.setCreated_at(DateUtils.dateString(new Date(timestamp), SpiderConfig.created_time_format));
                article.setCreated_long(timestamp);
            }
        }
        Element commentListElement = JsoupUtils.selectFirstElement(answer, "a.zg-anchor-hidden.ac");
        String commentListId = "";
        if (commentListElement != null) {
            commentListId = findCommentListId(commentListElement.attr("name"));
            comments = getComments(comments, commentListId, 1, meta);
            article.setComments(comments);
        }
        article.setSource(YuqingType.ZHIHU.getName());
        article.setYuqing_id(article.getSource() + "_" + article.getMd5());
        article.setYqid_answer(YuqingType.ZHIHU.getName() + "_" + MD5.md5(question_url));
        article.setBeidou_id(meta.getBeidou_id());
        article.setBeidou_name(meta.getEvent_name());
        article.setState(YuqingState.FETCHED_COMMENT.getState());
        article.setUpdate_time(DateUtils.dateString(new Date(), SpiderConfig.insert_update_time_format));
        article.setInsert_time(DateUtils.dateString(new Date(), SpiderConfig.insert_update_time_format));
        return article;
    }


    private YuqingArticle parseQuestion(Document document, BeidouEntity meta, String url) {
        YuqingArticle article = new YuqingArticle();
        Element replyCountElement = JsoupUtils.selectFirstElement(document, "h3#zh-question-answer-num");
        if (replyCountElement != null) {
            article.setReplyCount(findAnswerCount(replyCountElement.text()));
        }
        Element commentElement = JsoupUtils.selectFirstElement(document, "a.toggle-comment.meta-item");
        if (commentElement != null) {
            article.setCommentCount(findCommentCount(commentElement.text()));
        }
        Element descElement = document.getElementById("zh-question-detail");
        if (descElement != null) {
            String question_id = descElement.attr("data-resourceid");
            String params = "{\"question_id\":" + question_id + "}";
            String questionCommentUrl = SpiderConfig.getZhihuQuestionCommentUrl() + "?params=" + params;
            HttpResult result = HttpToolFactory.getHttpToolGroupByType(CrawlerSlotType.zhihu.toString()).getUrlByJsoup(questionCommentUrl, false);
            if (result != null) {
                Document questionCommentDocument = Jsoup.parse(result.getContent());
                List<YuqingComment> comments = parseQuestionComments(questionCommentDocument, meta);
                article.setComments(comments);
            }

        }
        return article;
    }

    private List<YuqingComment> parseQuestionComments(Document document, BeidouEntity meta) {
        Elements commentElements = JsoupUtils.selectElements(document, "div.zm-item-comment");
        if (commentElements == null || commentElements.size() == 0)
            return null;
        List<YuqingComment> comments = new ArrayList<YuqingComment>();
        YuqingComment comment = null;
        for (int i = 0; i < commentElements.size(); i++) {
            comment = parseQuestionComment(commentElements.get(i), meta);
            if (comment != null)
                comments.add(comment);
        }
        return comments;
    }

    private YuqingComment parseQuestionComment(Element element, BeidouEntity meta) {
        YuqingComment comment = new YuqingComment();
        comment.setSource(YuqingType.ZHIHU_QUESTION_COMMENT.getName());
        comment.setBeidou_id(meta.getBeidou_id());
        comment.setInsert_time(DateUtils.dateString(new Date(), SpiderConfig.insert_update_time_format));
        comment.setYqrpid(comment.getSource() + "_" + element.attr("data-id"));
        Element authorElement = JsoupUtils.selectFirstElement(element, "a.zm-item-link-avatar");
        if (authorElement != null) {
            comment.setAuthor(authorElement.attr("title"));
        }
        Element bodyElement = JsoupUtils.selectFirstElement(element, "div.zm-comment-content");
        if (bodyElement != null) {
            comment.setBody(bodyElement.text());
        }
        Element timeElement = JsoupUtils.selectFirstElement(element, "span.date");
        if (timeElement != null) {
            comment.setCreated_at(timeElement.text());
            String time = timeElement.text().trim();
            try {
                comment.setCreated_long(DateUtils.parseDate(time, "yyyy-MM-dd").getTime());
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        Element footerElement = JsoupUtils.selectFirstElement(element, "div.zm-comment-ft");
        if (footerElement != null) {
            Element zanElement = JsoupUtils.selectFirstElement(footerElement, "em");
            if (zanElement != null) {
                comment.setAttitudes_count(Integer.parseInt(zanElement.text()));
            }
        }
        return comment;
    }
}

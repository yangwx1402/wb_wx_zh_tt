package com.letv.sf.parser.weibo;

import com.letv.sf.entity.beidou.BeidouEntity;
import com.letv.sf.entity.common.SpiderStatus;
import com.letv.sf.entity.common.CrawlerPageInfo;
import com.letv.sf.entity.common.CrawlerResultEntity;
import com.letv.sf.http.HttpResult;
import com.letv.sf.parser.Parse;
import weibo4j.model.Comment;
import weibo4j.model.CommentWapper;

/**
 * Created by yangyong3 on 2016/12/1.
 * 微博评论解析
 */
public class WeiboCommentParser implements Parse<CrawlerResultEntity<Comment>,BeidouEntity> {

    public CrawlerResultEntity<Comment> parse(HttpResult httpPage) throws Exception {
        CommentWapper wapper = Comment.constructWapperCommentsJson(httpPage.getContent());
        CrawlerResultEntity<Comment> comments = new CrawlerResultEntity<Comment>();
        comments.setItems(wapper.getComments());
        CrawlerPageInfo page = new CrawlerPageInfo();
        page.setTotlaNum(wapper.getTotalNumber());
        comments.setPage(page);
        comments.setStatus(SpiderStatus.SUCCESS);
        return comments;
    }
}

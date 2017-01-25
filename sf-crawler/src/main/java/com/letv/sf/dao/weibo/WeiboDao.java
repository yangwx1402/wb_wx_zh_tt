package com.letv.sf.dao.weibo;

import com.letv.sf.entity.weibo.BeidouMapping;
import com.letv.sf.entity.weibo.WeiboTopicEventInfo;
import com.letv.sf.entity.weibo.WeiboTrace;
import weibo4j.model.Comment;
import weibo4j.model.Status;
import weibo4j.model.User;

import java.util.Collection;
import java.util.List;

/**
 * Created by young.yang on 2016/11/29.
 */
public interface WeiboDao {

    public boolean existWeibo(long mid);

    public void saveWeibo(Status status);

    public void saveWeibos(List<Status> status);

    public void saveWeiboUser(User user);

    public void saveWeiboUers(Collection<User> users);

    public boolean existUser(long uid);

    public List<BeidouMapping> getMidbyState(int state);

    public void updateState(long mid,int state);

    public boolean existComment(long id);

    public void saveComment(Comment comment);

    public void saveComments(List<Comment> comments);

    public List<BeidouMapping> getWeiboTraceId(int limit);

    public void saveWeiboTrace(List<WeiboTrace> traceList);

    public WeiboTopicEventInfo exsitTopicEventInfo(String topicName) ;

    public Integer insertTopicEventInfo(WeiboTopicEventInfo topicEventInfo) ;

    public List<BeidouMapping> getCommentMids(int comment_limit,int limit);

    public List<WeiboTopicEventInfo> getWeiboTopicEventByTime(Long beidou_id,String start,String end);

    public void updateWeiboRCACount(List<WeiboTrace> traces);

    public List<BeidouMapping> getOfficialTraceId(String screen_names[],long start,long end);

}

package com.letv.sf.dao.weibo;

import com.letv.sf.config.SpiderConfig;
import com.letv.sf.dao.AbstractDao;
import com.letv.sf.dao.DataSourceFactory;
import com.letv.sf.dao.MongoDataSourceFactory;
import com.letv.sf.dao.TableNameFactory;
import com.letv.sf.entity.weibo.BeidouMapping;
import com.letv.sf.entity.weibo.WeiboTopicEventInfo;
import com.letv.sf.entity.weibo.WeiboTrace;

import com.letv.sf.filter.FilterFactory;
import com.letv.sf.utils.JsonUtils;
import com.letv.sf.utils.RegUtils;
import com.letv.sf.utils.WeiboIdUrlTrasfer;
import com.mongodb.client.MongoCollection;
import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;
import org.bson.Document;
import weibo4j.model.Comment;
import weibo4j.model.Status;
import weibo4j.model.StatusWapper;
import weibo4j.model.User;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * Created by yangyong3 on 2016/11/30.
 */
public class WeiboDaoByMysql extends AbstractDao implements WeiboDao {

    private static final Logger log = Logger.getLogger(WeiboDaoByMysql.class);

    public void saveWeibo(Status status) {
        Connection con = null;
        PreparedStatement ps = null;
        StringBuffer sql = new StringBuffer(100);
        try {
            sql.append("insert into " + TableNameFactory.getTableName(SpiderConfig.getSns_weibo_sina_timeline_chunwan_table(), status.getCreatedAt().getTime()) +
                    " (`beidou_id`                 ,\n" +
                    "`beidou_name`               ,\n" +
                    "`mid`                       ,\n" +
                    "`idstr`                     ,\n" +
                    "`text`                      ,\n" +
                    "`textLength`                ,\n" +
                    "`source`                    ,\n" +
                    "`favorited`                 ,\n" +
                    "`truncated`                 ,\n" +
                    "`geo`                       ,\n" +
                    "`user_id`                   ,\n" +
                    "`screen_name`               ,\n" +
                    "`profile_image_url`         ,\n" +
                    "`reposts_count`             ,\n" +
                    "`comments_count`            ,\n" +
                    "`attitudes_count`           ,\n" +
                    "`created_at`                ,\n" +
                    "`last_update_time`          ,\n" +
                    "`insert_time`, \n" +
                    "`type`,`state`,`topic_id`,weibo_url ) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
            con = DataSourceFactory.getDataSource().getConnection();
            log.info("sql - [" + sql.toString() + "]");
            ps = con.prepareStatement(sql.toString());
            ps.setLong(1, status.getBeidou_id());
            ps.setString(2, status.getBeidou_name());
            ps.setLong(3, Long.parseLong(status.getMid()));
            ps.setString(4, status.getMid());
            ps.setString(5, RegUtils.wordNumChar(status.getText()));
            ps.setInt(6, status.getText().length());
            if (status.getSource() != null)
                ps.setString(7, RegUtils.wordNumChar(status.getSource().getName()));
            else
                ps.setString(7, "");
            ps.setInt(8, 0);
            ps.setInt(9, 0);
            ps.setString(10, status.getGeo());
            if (status.getUser() != null) {
                status.setUser_id(Long.parseLong(status.getUser().getId()));
                status.setScreen_name(status.getUser().getScreenName());
            }
            ps.setLong(11, status.getUser_id() == null ? 0 : status.getUser_id());
            ps.setString(12, status.getScreen_name());
            ps.setString(13, status.getAvatar());
            ps.setInt(14, status.getRepostsCount());
            ps.setInt(15, status.getCommentsCount());
            ps.setInt(16, status.getAttitudesCount());
            ps.setTimestamp(17, new Timestamp(status.getCreatedAt().getTime()));
            ps.setTimestamp(18, new Timestamp(System.currentTimeMillis()));
            ps.setTimestamp(19, new Timestamp(System.currentTimeMillis()));
            ps.setString(20, status.getType());
            ps.setInt(21, SpiderConfig.getSinaWeiboSearchReadyFetchComment());
            ps.setInt(22, status.getTopic_id());
            ps.setString(23, "www.weibo.com/" + status.getUser_id() + "/" + WeiboIdUrlTrasfer.Mid2Uid(status.getMid()));
            ps.execute();
        } catch (Exception e) {
            log.error("saveWeibo error message is ", e);
            try {
                saveMongoCollection(SpiderConfig.getSns_weibo_sina_timeline_chunwan_table(), JsonUtils.toJson(status),"mid");
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        } finally {
            close(null, ps, con);
        }
    }

    public void saveWeibos(List<Status> statuses) {
        if (CollectionUtils.isEmpty(statuses))
            return;
        Connection con = null;
        PreparedStatement ps = null;
        StringBuffer sql = new StringBuffer(100);
        try {
            sql.append("insert into " + TableNameFactory.getTableName(SpiderConfig.getSns_weibo_sina_timeline_chunwan_table(), System.currentTimeMillis()) +
                    " (`beidou_id`                 ,\n" +
                    "`beidou_name`               ,\n" +
                    "`mid`                       ,\n" +
                    "`idstr`                     ,\n" +
                    "`text`                      ,\n" +
                    "`textLength`                ,\n" +
                    "`source`                    ,\n" +
                    "`favorited`                 ,\n" +
                    "`truncated`                 ,\n" +
                    "`geo`                       ,\n" +
                    "`user_id`                   ,\n" +
                    "`screen_name`               ,\n" +
                    "`profile_image_url`         ,\n" +
                    "`reposts_count`             ,\n" +
                    "`comments_count`            ,\n" +
                    "`attitudes_count`           ,\n" +
                    "`created_at`                ,\n" +
                    "`last_update_time`          ,\n" +
                    "`insert_time`, \n" +
                    "`type`,`state`,`topic_id`,weibo_url ) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
            con = DataSourceFactory.getDataSource().getConnection();
            log.info("sql - [" + sql.toString() + "]");
            ps = con.prepareStatement(sql.toString());
            for (Status status : statuses) {
                ps.setLong(1, status.getBeidou_id());
                ps.setString(2, status.getBeidou_name());
                ps.setLong(3, Long.parseLong(status.getMid()));
                ps.setString(4, status.getMid());
                ps.setString(5, RegUtils.wordNumChar(status.getText()));
                ps.setInt(6, status.getText().length());
                if (status.getSource() != null)
                    ps.setString(7, RegUtils.wordNumChar(status.getSource().getName()));
                else
                    ps.setString(7, "");
                ps.setInt(8, 0);
                ps.setInt(9, 0);
                ps.setString(10, status.getGeo());
                if (status.getUser() != null) {
                    status.setUser_id(Long.parseLong(status.getUser().getId()));
                    status.setScreen_name(status.getUser().getScreenName());
                }
                ps.setLong(11, status.getUser_id() == null ? 0 : status.getUser_id());
                ps.setString(12, status.getScreen_name());
                ps.setString(13, status.getAvatar());
                ps.setInt(14, status.getRepostsCount());
                ps.setInt(15, status.getCommentsCount());
                ps.setInt(16, status.getAttitudesCount());
                ps.setTimestamp(17, new Timestamp(status.getCreatedAt().getTime()));
                ps.setTimestamp(18, new Timestamp(System.currentTimeMillis()));
                ps.setTimestamp(19, new Timestamp(System.currentTimeMillis()));
                ps.setString(20, status.getType());
                ps.setInt(21, SpiderConfig.getSinaWeiboSearchReadyFetchComment());
                ps.setInt(22, status.getTopic_id());
                ps.setString(23, "www.weibo.com/" + status.getUser_id() + "/" + WeiboIdUrlTrasfer.Mid2Uid(status.getMid()));
                ps.addBatch();
            }
            ps.executeBatch();
        } catch (Exception e) {
            log.error("saveWeibo error message is ", e);
            for(Status status:statuses)
                try {
                    saveMongoCollection(SpiderConfig.getSns_weibo_sina_timeline_chunwan_table(), JsonUtils.toJson(status),"mid");
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
        } finally {
            close(null, ps, con);
        }
    }

    public void saveWeiboUser(User user) {
        Connection con = null;
        PreparedStatement ps = null;
        StringBuffer sql = new StringBuffer(100);
        try {
            sql.append("insert into " + TableNameFactory.getTableName(SpiderConfig.getSns_weibo_sina_person_chunwan_table(), user.getCreatedAt().getTime()) +
                    "(  `user_id`                   ,\n" +
                    "  `screen_name`               ,\n" +
                    "  `name`                      ,\n" +
                    "  `province`                  ,\n" +
                    "  `city`                      ,\n" +
                    "  `location`                  ,\n" +
                    "  `description`               ,\n" +
                    "  `profile_url`               ,\n" +
                    "  `gender`                    ,\n" +
                    "  `followers_count`           ,\n" +
                    "  `friends_count`             ,\n" +
                    "  `statuses_count`            ,\n" +
                    "  `favourites_count`          ,\n" +
                    "  `created_at`                ,\n" +
                    "  `avatar_large`              ,\n" +
                    "  `geo_enabled`               ,\n" +
                    "  `verified`                  ,\n" +
                    "  `verified_type`             ,\n" +
                    "  `verified_reason`            ,\n" +
                    "  `birthday`                  ,\n" +
                    "  `birthday_visible`          ,\n" +
                    "  `tags`                      ,\n" +
                    "  `last_update_time`          ,\n" +
                    "  `insert_time`               ,\n" +
                    "  `raw_birthday`              ,\n" +
                    "  `process_status`            ,\n" +
                    "  `process_status_tags`         ) " +
                    "values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
            log.info("sql--[" + sql.toString() + "]");
            con = DataSourceFactory.getDataSource().getConnection();
            ps = con.prepareStatement(sql.toString());
            ps.setLong(1, Long.parseLong(user.getId()));
            ps.setString(2, user.getScreenName());
            ps.setString(3, user.getName());
            ps.setInt(4, user.getProvince());
            ps.setInt(5, user.getCity());
            ps.setString(6, user.getLocation());
            ps.setString(7, RegUtils.wordNumChar(user.getDescription() == null ? "" : user.getDescription()));
            ps.setString(8, "");
            ps.setString(9, user.getGender());
            ps.setInt(10, user.getFollowersCount());
            ps.setInt(11, user.getFriendsCount());
            ps.setInt(12, user.getStatusesCount());
            ps.setInt(13, user.getFavouritesCount());
            ps.setTimestamp(14, new Timestamp(user.getCreatedAt().getTime()));
            ps.setString(15, user.getAvatarLarge());
            ps.setInt(16, 0);
            ps.setInt(17, user.isVerified() ? 1 : 0);
            ps.setString(18, user.getverifiedType() + "");
            ps.setString(19, user.getVerifiedReason());
            ps.setString(20, "");
            ps.setInt(21, 0);
            ps.setString(22, "");
            ps.setTimestamp(23, new Timestamp(System.currentTimeMillis()));
            ps.setTimestamp(24, new Timestamp(System.currentTimeMillis()));
            ps.setString(25, "");
            ps.setInt(26, 0);
            ps.setInt(27, 0);
            ps.execute();
        } catch (Exception e) {
            log.error("execute saveWeiboUser error ", e);
            try {
                saveMongoCollection(SpiderConfig.getSns_weibo_sina_person_chunwan_table(), JsonUtils.toJson(user),"uid");
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        } finally {
            close(null, ps, con);
        }

    }

    public void saveWeiboUers(Collection<User> users) {
        if (CollectionUtils.isEmpty(users))
            return;
        Connection con = null;
        PreparedStatement ps = null;
        StringBuffer sql = new StringBuffer(100);
        try {
            sql.append("insert into " + TableNameFactory.getTableName(SpiderConfig.getSns_weibo_sina_person_chunwan_table(), System.currentTimeMillis()) +
                    "(  `user_id`                   ,\n" +
                    "  `screen_name`               ,\n" +
                    "  `name`                      ,\n" +
                    "  `province`                  ,\n" +
                    "  `city`                      ,\n" +
                    "  `location`                  ,\n" +
                    "  `description`               ,\n" +
                    "  `profile_url`               ,\n" +
                    "  `gender`                    ,\n" +
                    "  `followers_count`           ,\n" +
                    "  `friends_count`             ,\n" +
                    "  `statuses_count`            ,\n" +
                    "  `favourites_count`          ,\n" +
                    "  `created_at`                ,\n" +
                    "  `avatar_large`              ,\n" +
                    "  `geo_enabled`               ,\n" +
                    "  `verified`                  ,\n" +
                    "  `verified_type`             ,\n" +
                    "  `verified_reason`            ,\n" +
                    "  `birthday`                  ,\n" +
                    "  `birthday_visible`          ,\n" +
                    "  `tags`                      ,\n" +
                    "  `last_update_time`          ,\n" +
                    "  `insert_time`               ,\n" +
                    "  `raw_birthday`              ,\n" +
                    "  `process_status`            ,\n" +
                    "  `process_status_tags`         ) " +
                    "values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
            log.info("sql--[" + sql.toString() + "]");
            con = DataSourceFactory.getDataSource().getConnection();
            ps = con.prepareStatement(sql.toString());
            for (User user : users) {
                ps.setLong(1, Long.parseLong(user.getId()));
                ps.setString(2, user.getScreenName());
                ps.setString(3, user.getName());
                ps.setInt(4, user.getProvince());
                ps.setInt(5, user.getCity());
                ps.setString(6, user.getLocation());
                ps.setString(7, RegUtils.wordNumChar(user.getDescription() == null ? "" : user.getDescription()));
                ps.setString(8, "");
                ps.setString(9, user.getGender());
                ps.setInt(10, user.getFollowersCount());
                ps.setInt(11, user.getFriendsCount());
                ps.setInt(12, user.getStatusesCount());
                ps.setInt(13, user.getFavouritesCount());
                ps.setTimestamp(14, new Timestamp(user.getCreatedAt().getTime()));
                ps.setString(15, user.getAvatarLarge());
                ps.setInt(16, 0);
                ps.setInt(17, user.isVerified() ? 1 : 0);
                ps.setString(18, user.getverifiedType() + "");
                ps.setString(19, user.getVerifiedReason());
                ps.setString(20, "");
                ps.setInt(21, 0);
                ps.setString(22, "");
                ps.setTimestamp(23, new Timestamp(System.currentTimeMillis()));
                ps.setTimestamp(24, new Timestamp(System.currentTimeMillis()));
                ps.setString(25, "");
                ps.setInt(26, 0);
                ps.setInt(27, 0);
                ps.addBatch();
            }
            ps.executeBatch();
        } catch (Exception e) {
            log.error("execute saveWeiboUser error ", e);
            for(User user:users)
                try {
                    saveMongoCollection(SpiderConfig.getSns_weibo_sina_person_chunwan_table(), JsonUtils.toJson(user),"id");
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
        } finally {
            close(null, ps, con);
        }
    }


    public boolean existWeibo(long mid) {
        String key = WEIBO_MID + mid;
        boolean flag = FilterFactory.exist(key);
        if (flag) {
            log.info(key + " exist in filter cache ");
            return true;
        }
        Connection con = null;
        Statement stmt = null;
        ResultSet rs = null;
        StringBuffer sql = new StringBuffer(100);
        try {
            con = DataSourceFactory.getDataSource().getConnection();
            sql.append("select mid from " + TableNameFactory.getTableName(SpiderConfig.getSns_weibo_sina_timeline_chunwan_table(), System.currentTimeMillis()) + " where mid =" + mid);
            stmt = con.createStatement();
            log.info("sql -[" + sql.toString() + "]");
            rs = stmt.executeQuery(sql.toString());
            if (rs.next()) {
                FilterFactory.add(key);
                return true;
            }
        } catch (Exception e) {
            log.error("execute existWeibo error ", e);
        } finally {
            close(rs, stmt, con);

        }
        return false;
    }

    public boolean existUser(long uid) {
        String key = WEIBO_USER_UID + uid;
        boolean flag = FilterFactory.exist(key);
        if (flag) {
            log.info(key + " exist in filter cache ");
            return true;
        }
        Connection con = null;
        Statement stmt = null;
        ResultSet rs = null;
        StringBuffer sb = new StringBuffer();
        try {
            con = DataSourceFactory.getDataSource().getConnection();
            stmt = con.createStatement();
            sb.append("select user_id from " + TableNameFactory.getTableName(SpiderConfig.getSns_weibo_sina_person_chunwan_table(), System.currentTimeMillis()) + " where user_id = " + uid);
            log.info("sql -[" + sb.toString() + "]");
            rs = stmt.executeQuery(sb.toString());
            if (rs.next()) {
                FilterFactory.add(key);
                return true;
            }
        } catch (Exception e) {
            log.error("execute existUser error ", e);
        } finally {
            close(rs, stmt, con);
        }

        return false;
    }

    public List<BeidouMapping> getMidbyState(int state) {
        Connection con = null;
        Statement stmt = null;
        ResultSet rs = null;
        StringBuffer sql = new StringBuffer(100);
        List<BeidouMapping> mids = new ArrayList<BeidouMapping>();
        BeidouMapping mapping = null;
        try {
            sql.append("select mid,beidou_id from " + TableNameFactory.getTableName(SpiderConfig.getSns_weibo_sina_timeline_chunwan_table(), System.currentTimeMillis()) + " where state = " + SpiderConfig.getSinaWeiboSearchReadyFetchComment());
            log.info("sql --[" + sql.toString() + "]");
            con = DataSourceFactory.getDataSource().getConnection();
            stmt = con.createStatement();
            rs = stmt.executeQuery(sql.toString());
            while (rs.next()) {
                mapping = new BeidouMapping();
                mapping.setMid(rs.getLong(1));
                mapping.setBeidou_id(rs.getString(2));
                mids.add(mapping);
            }

        } catch (Exception e) {
            log.error("execute getMidbyState error ", e);
        } finally {
            close(rs, stmt, con);
        }
        return mids;
    }

    public void updateState(long mid, int state) {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        StringBuffer sql = new StringBuffer(100);
        try {
            sql.append("update " + TableNameFactory.getTableName(SpiderConfig.getSns_weibo_sina_timeline_chunwan_table(), System.currentTimeMillis()) + " set state =? where mid =?");
            log.info("sql --[" + sql.toString() + "]");
            con = DataSourceFactory.getDataSource().getConnection();
            ps = con.prepareStatement(sql.toString());
            ps.setInt(1, state);
            ps.setLong(2, mid);
            ps.executeUpdate();
        } catch (Exception e) {
            log.error("execute updateState error ", e);
        } finally {
            close(null, ps, con);
        }
    }

    public boolean existComment(long id) {
        String key = WEIBO_COMMENT_CID + id;
        boolean flag = FilterFactory.exist(key);
        if (flag) {
            log.info(key + " exist in filter cache ");
            return true;
        }
        Connection con = null;
        Statement stmt = null;
        ResultSet rs = null;
        StringBuffer sql = new StringBuffer(100);
        try {
            sql.append("select id from " + TableNameFactory.getTableName(SpiderConfig.getSns_weibo_sina_comment_chunwan_table(), System.currentTimeMillis()) + " where id = " + id);
            log.info("sql --[" + sql.toString() + "]");
            con = DataSourceFactory.getDataSource().getConnection();
            stmt = con.createStatement();
            rs = stmt.executeQuery(sql.toString());
            if (rs.next()) {
                FilterFactory.add(key);
                return true;
            }
        } catch (Exception e) {
            log.error("execute existComment error ", e);
        } finally {
            close(rs, stmt, con);
        }
        return false;
    }

    public void saveComment(Comment comment) {
        Connection con = null;
        PreparedStatement ps = null;
        StringBuffer sql = new StringBuffer(100);
        try {
            sql.append("insert into  " + TableNameFactory.getTableName(SpiderConfig.getSns_weibo_sina_comment_chunwan_table(), System.currentTimeMillis()) + " " +
                    " (`id`,`mid`,`text`,`source`,`create_time`,`insert_time`,`user_id`,beidou_id) values(?,?,?,?,?,?,?,?)");
            log.info("sql --[" + sql.toString() + "]");
            con = DataSourceFactory.getDataSource().getConnection();
            ps = con.prepareStatement(sql.toString());
            ps.setLong(1, comment.getId());
            ps.setLong(2, comment.getSource_mid());
            ps.setString(3, RegUtils.wordNumChar(comment.getText()));
            ps.setString(4, RegUtils.wordNumChar(comment.getSource()));
            ps.setTimestamp(5, new Timestamp(comment.getCreatedAt().getTime()));
            ps.setTimestamp(6, new Timestamp(System.currentTimeMillis()));
            ps.setLong(7, Long.parseLong(comment.getUser().getId()));
            ps.setLong(8, comment.getBeidou_id());
            ps.execute();
        } catch (Exception e) {
            log.error("execute saveComment error ", e);
            try {
                saveMongoCollection(SpiderConfig.getSns_weibo_sina_comment_chunwan_table(), JsonUtils.toJson(comment),"id");
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        } finally {
            close(null, ps, con);
        }
    }

    public void saveComments(List<Comment> comments) {
        if (CollectionUtils.isEmpty(comments))
            return;
        Connection con = null;
        PreparedStatement ps = null;
        StringBuffer sql = new StringBuffer(100);
        try {
            sql.append("insert into  " + TableNameFactory.getTableName(SpiderConfig.getSns_weibo_sina_comment_chunwan_table(), System.currentTimeMillis()) + " " +
                    " (`id`,`mid`,`text`,`source`,`create_time`,`insert_time`,`user_id`,beidou_id) values(?,?,?,?,?,?,?,?)");
            log.info("sql --[" + sql.toString() + "]");
            con = DataSourceFactory.getDataSource().getConnection();
            ps = con.prepareStatement(sql.toString());
            for (Comment comment : comments) {
                ps.setLong(1, comment.getId());
                ps.setLong(2, comment.getSource_mid());
                ps.setString(3, RegUtils.wordNumChar(comment.getText()));
                ps.setString(4, RegUtils.wordNumChar(comment.getSource()));
                ps.setTimestamp(5, new Timestamp(comment.getCreatedAt().getTime()));
                ps.setTimestamp(6, new Timestamp(System.currentTimeMillis()));
                ps.setLong(7, Long.parseLong(comment.getUser().getId()));
                ps.setLong(8, comment.getBeidou_id());
                ps.addBatch();
            }
            ps.executeBatch();
        } catch (Exception e) {
            log.error("execute saveComment error ", e);
            for(Comment comment:comments)
                try {
                    saveMongoCollection(SpiderConfig.getSns_weibo_sina_comment_chunwan_table(), JsonUtils.toJson(comment),"id");
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
        } finally {
            close(null, ps, con);
        }
    }

    public List<BeidouMapping> getWeiboTraceId(int limit) {
        Connection con = null;
        PreparedStatement ps = null;
        StringBuffer sql = new StringBuffer(100);
        ResultSet rs = null;
        List<BeidouMapping> list = new ArrayList<BeidouMapping>();
        BeidouMapping mapping = null;
        try {
            sql.append("select mid,beidou_id from " + TableNameFactory.getTableName(SpiderConfig.getSns_weibo_sina_timeline_chunwan_table(), System.currentTimeMillis()) + " where created_at >? limit " + limit);
            log.info("sql --[" + sql.toString() + "]");
            con = DataSourceFactory.getDataSource().getConnection();
            ps = con.prepareStatement(sql.toString());
            Timestamp time = new Timestamp(System.currentTimeMillis() - SpiderConfig.getSinaWeiboTraceInterval());
            System.out.println(new Date(time.getTime()));
            ps.setTimestamp(1, time);
            rs = ps.executeQuery();
            while (rs.next()) {
                mapping = new BeidouMapping();
                mapping.setMid(rs.getLong(1));
                mapping.setBeidou_id(rs.getString(2));
                list.add(mapping);
            }
        } catch (Exception e) {
            log.error("execute getWeiboTraceId error ", e);
        } finally {
            close(rs, ps, con);
        }
        return list;
    }

    public void saveWeiboTrace(List<WeiboTrace> traceList) {
        Connection con = null;
        PreparedStatement ps = null;
        StringBuffer sql = new StringBuffer(100);
        try {
            sql.append("insert into " + TableNameFactory.getTableName(SpiderConfig.getSns_weibo_sina_chunwan_trace_table(), System.currentTimeMillis())
                    + "  (`mid`,reposts_count ,comments_count ,attitudes_count,beidou_id) values(?,?,?,?,?)");
            log.info("sql --[" + sql.toString() + "]");
            con = DataSourceFactory.getDataSource().getConnection();
            ps = con.prepareStatement(sql.toString());
            for (WeiboTrace trace : traceList) {
                ps.setLong(1, trace.getMid());
                ps.setInt(2, trace.getReposts_count());
                ps.setInt(3, trace.getComment_count());
                ps.setInt(4, trace.getAttitudes_count());
                ps.setString(5, trace.getBeidou_id());
                ps.addBatch();
            }
            ps.executeBatch();
        } catch (Exception e) {
            log.error("execute saveWeiboTrace error ", e);
        } finally {
            close(null, ps, con);
        }
    }

    public WeiboTopicEventInfo exsitTopicEventInfo(String topicName) {
        Connection con = null;
        PreparedStatement ps = null;
        StringBuffer sql = new StringBuffer(100);
        ResultSet rs = null;
        WeiboTopicEventInfo info = null;
        try {
            con = DataSourceFactory.getDataSource().getConnection();
            sql.append("select zip_id from event_info_zip where zip_value = ?");
            log.info("sql --[" + sql.toString() + "]");
            ps = con.prepareStatement(sql.toString());
            ps.setString(1, topicName);
            rs = ps.executeQuery();
            if (rs.next()) {
                info = new WeiboTopicEventInfo();
                info.setZip_id(rs.getInt(1));
            }
        } catch (Exception e) {
            log.error("execute exsitTopicEventInfo error ", e);
        } finally {
            close(rs, ps, con);
        }
        return info;
    }

    public Integer insertTopicEventInfo(WeiboTopicEventInfo topicEventInfo) {
        Connection con = null;
        PreparedStatement ps = null;
        StringBuffer sql = new StringBuffer(100);
        ResultSet rs = null;
        try {
            con = DataSourceFactory.getDataSource().getConnection();
            sql.append("insert into event_info_zip(beidou_id,type,status,zip_value,insert_time) values(?,?,?,?,?) ");
            log.info("sql --[" + sql.toString() + "]");
            ps = con.prepareStatement(sql.toString(), Statement.RETURN_GENERATED_KEYS);
            ps.setLong(1, topicEventInfo.getBeidou_id());
            ps.setString(2, topicEventInfo.getType());
            ps.setString(3, topicEventInfo.getStatus());
            ps.setString(4, topicEventInfo.getZip_value());
            ps.setTimestamp(5, topicEventInfo.getInsert_time());
            ps.execute();
            rs = ps.getGeneratedKeys();
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (Exception e) {
            log.error("execute insertTopicEventInfo error ", e);
        } finally {
            close(null, ps, con);
        }
        return null;
    }

    public List<BeidouMapping> getCommentMids(int comment_limit, int limit) {
        Connection con = null;
        PreparedStatement ps = null;
        StringBuffer sql = new StringBuffer(100);
        ResultSet rs = null;
        List<BeidouMapping> list = new ArrayList<BeidouMapping>();
        BeidouMapping mapping = null;
        try {
            sql.append("select distinct mid,beidou_id from " + TableNameFactory.getTableName(SpiderConfig.getSns_weibo_sina_chunwan_trace_table(), System.currentTimeMillis()) + " where insert_time >? and comments_count>" + comment_limit + " order by insert_time desc limit " + limit);
            log.info("sql --[" + sql.toString() + "]");
            con = DataSourceFactory.getDataSource().getConnection();
            ps = con.prepareStatement(sql.toString());
            Timestamp time = new Timestamp(System.currentTimeMillis() - SpiderConfig.getSinaWeiboTraceInterval());
            System.out.println(new Date(time.getTime()));
            ps.setTimestamp(1, time);
            rs = ps.executeQuery();
            while (rs.next()) {
                mapping = new BeidouMapping();
                mapping.setMid(rs.getLong(1));
                mapping.setBeidou_id(rs.getString(2));
                list.add(mapping);
            }
        } catch (Exception e) {
            log.error("execute getCommentMids error ", e);
        } finally {
            close(rs, ps, con);
        }
        return list;
    }

    public List<WeiboTopicEventInfo> getWeiboTopicEventByTime(Long beidou_id, String start, String end)   {
        Connection con = null;
        Statement stmt = null;
        StringBuffer sql = new StringBuffer(100);
        ResultSet rs = null;
        List<WeiboTopicEventInfo> list = new ArrayList<WeiboTopicEventInfo>();
        WeiboTopicEventInfo mapping = null;
        try {
            sql.append("select * from event_info_zip where beidou_id=" + beidou_id + " and insert_time >='" + start + "' and insert_time < '" + end + "'");
            log.info("sql --[" + sql.toString() + "]");
            con = DataSourceFactory.getDataSource().getConnection();
            stmt = con.createStatement();
            rs = stmt.executeQuery(sql.toString());
            while (rs.next()) {
                mapping = new WeiboTopicEventInfo();
                mapping.setZip_value(rs.getString("zip_value"));
                mapping.setBeidou_id(rs.getLong("beidou_id"));
                list.add(mapping);
            }
        } catch (Exception e) {
            log.error("execute getCommentMids error ", e);
        } finally {
            close(rs, stmt, con);
        }
        return list;
    }

    public void updateWeiboRCACount(List<WeiboTrace> traces)  {
        if (CollectionUtils.isEmpty(traces))
            return;
        Connection con = null;
        PreparedStatement ps = null;
        StringBuffer sql = new StringBuffer(100);
        ResultSet rs = null;
        try {
            sql.append("update " + TableNameFactory.getTableName(SpiderConfig.getSns_weibo_sina_timeline_chunwan_table(), System.currentTimeMillis())
                    + " set reposts_count=? , comments_count=? , attitudes_count=? , last_update_time=? where mid=?");
            log.info("sql --[" + sql.toString() + "]");
            con = DataSourceFactory.getDataSource().getConnection();
            ps = con.prepareStatement(sql.toString());
            for (WeiboTrace trace : traces) {
                ps.setInt(1, trace.getReposts_count());
                ps.setInt(2, trace.getComment_count());
                ps.setInt(3, trace.getAttitudes_count());
                ps.setTimestamp(4, new Timestamp(System.currentTimeMillis()));
                ps.setLong(5, trace.getMid());
                ps.addBatch();
            }
            ps.executeBatch();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            close(null, ps, con);
        }
    }

    @Override
    public List<BeidouMapping> getOfficialTraceId(String[] screen_names, long start, long end) {
        Connection con = null;
        PreparedStatement ps = null;
        StringBuffer sql = new StringBuffer(100);
        ResultSet rs = null;
        List<BeidouMapping> list = new ArrayList<BeidouMapping>();
        BeidouMapping mapping = null;
        StringBuffer screen_name = new StringBuffer();
        if(screen_names==null||screen_names.length==0)
            return list;
        for(int i=0;i<screen_names.length;i++){
            if(i == screen_names.length-1){
                screen_name.append("'"+screen_names[i]+"'");
            }else{
                screen_name.append("'"+screen_names[i]+"',");
            }
        }
        try {
            sql.append("select mid,beidou_id from " + TableNameFactory.getTableName(SpiderConfig.getSns_weibo_sina_timeline_chunwan_table(), System.currentTimeMillis()) + " where created_at >=? and created_at <? and screen_name in ("+screen_name.toString()+")");
            log.info("sql --[" + sql.toString() + "]");
            con = DataSourceFactory.getDataSource().getConnection();
            ps = con.prepareStatement(sql.toString());
            ps.setTimestamp(1, new Timestamp(start));
            ps.setTimestamp(2,new Timestamp(end));
            rs = ps.executeQuery();
            while (rs.next()) {
                mapping = new BeidouMapping();
                mapping.setMid(rs.getLong(1));
                mapping.setBeidou_id(rs.getString(2));
                list.add(mapping);
            }
        } catch (Exception e) {
            log.error("execute getWeiboTraceId error ", e);
        } finally {
            close(rs, ps, con);
        }
        return list;
    }

    private void saveMongoCollection(String tableName, String jsondata, String id_field) throws IOException {
        Map<String,Object> data = JsonUtils.fromJson(jsondata,Map.class);
        MongoCollection<Document> collection = MongoDataSourceFactory.getMongoCollection(tableName);
        Document find = new Document(id_field, data.get(id_field));
        Document rs = collection.find(find).first();
        if (rs == null)
            collection.insertOne(new Document(data));
    }
}

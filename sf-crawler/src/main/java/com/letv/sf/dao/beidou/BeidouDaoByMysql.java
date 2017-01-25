package com.letv.sf.dao.beidou;

import com.letv.sf.config.ContentType;
import com.letv.sf.dao.AbstractDao;
import com.letv.sf.dao.DataSourceFactory;
import com.letv.sf.entity.beidou.BeidouEntity;
import com.letv.sf.entity.weibo.BeidouMapping;

import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;
import weibo4j.model.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by yangyong3 on 2016/12/1.
 */
public class BeidouDaoByMysql extends AbstractDao implements BeidouDao {

    private static Logger log = Logger.getLogger(BeidouDaoByMysql.class);

    public List<BeidouEntity> getKeywords(ContentType type,String date) {
        Connection con = null;
        Statement stmt = null;
        ResultSet rs = null;
        List<BeidouEntity> keywords = new ArrayList<BeidouEntity>();
        StringBuffer sql = new StringBuffer(100);
        try {
            sql.append("select * from festival_event_info ");
            con = DataSourceFactory.getDataSource().getConnection();
            log.info("sql --[" + sql.toString() + "]");
            stmt = con.createStatement();
            rs = stmt.executeQuery(sql.toString());
            long beidou_id = 0;
            String event_name = null;
            String tags = null;
            String[] temp = null;
            BeidouEntity keyword = null;
            while (rs.next()) {
                beidou_id = rs.getLong("beidou_id");
                event_name = rs.getString("event_name");
                if (ContentType.search == type) {
                    tags = rs.getString("search_tags");
                } else if (ContentType.official == type) {
                    tags = rs.getString("weibo");
                } else if (ContentType.topic == type) {
                    tags = rs.getString("weibo_index_topic");
                } else if(ContentType.chunwan == type){
                    tags = rs.getString("search_tags");
                }
                temp = tags.split("@@@");
                for (String line : temp) {
                    keyword = new BeidouEntity();
                    keyword.setBeidou_id(beidou_id);
                    keyword.setEvent_name(event_name);
                    keyword.setTag(line);
                    keywords.add(keyword);
                }
            }
        } catch (Exception e) {
            log.error("getKeywords error message is ",e);
        } finally {
            close(rs, stmt, con);
        }
        return keywords;
    }

    public void saveBeidouMapping(BeidouMapping beidou, User user) {
        Connection con = null;
        PreparedStatement ps = null;
        StringBuffer sql = new StringBuffer(100);
        try {
            sql.append("insert into sns_weibo_sina_beidou_mapping(`beidou_id`,`user_id`,`source`,`type`) values (?,?,?,?)");
            con = DataSourceFactory.getDataSource().getConnection();
            log.info("sql --[" + sql.toString() + "]");
            ps = con.prepareStatement(sql.toString());
            ps.setString(1, beidou.getBeidou_id() + "");
            ps.setLong(2, Long.parseLong(user.getId()));
            ps.setString(3, beidou.getCrawl_source());
            ps.setString(4, beidou.getCrawl_type());
            ps.execute();
        } catch (Exception e) {
            log.error("save sns_weibo_sina_beidou_mapping error message is ",e);
        } finally {
            close(null, ps, con);
        }
    }

    public void saveBeidouMappings(BeidouMapping beidou, Collection<User> users) {
        if(CollectionUtils.isEmpty(users))
            return;
        Connection con = null;
        PreparedStatement ps = null;
        StringBuffer sql = new StringBuffer(100);
        try {
            sql.append("insert into sns_weibo_sina_beidou_mapping(`beidou_id`,`user_id`,`source`,`type`) values (?,?,?,?)");
            con = DataSourceFactory.getDataSource().getConnection();
            log.info("sql --[" + sql.toString() + "]");
            ps = con.prepareStatement(sql.toString());
            for(User user:users) {
                ps.setString(1, beidou.getBeidou_id() + "");
                ps.setLong(2, Long.parseLong(user.getId()));
                ps.setString(3, beidou.getCrawl_source());
                ps.setString(4, beidou.getCrawl_type());
                ps.addBatch();
            }
            ps.executeBatch();
        } catch (Exception e) {
            log.error("save sns_weibo_sina_beidou_mapping error message is ",e);
        } finally {
            close(null, ps, con);
        }
    }

    public List<BeidouEntity> listAllBeidou() {
        Connection con = null;
        Statement stmt = null;
        ResultSet rs = null;
        List<BeidouEntity> beidous = new ArrayList<BeidouEntity>();
        StringBuffer sql = new StringBuffer(100);
        BeidouEntity beidou = null;
        try {
            sql.append("select * from festival_event_info ");
            con = DataSourceFactory.getDataSource().getConnection();
            log.info("sql --[" + sql.toString() + "]");
            stmt = con.createStatement();
            rs = stmt.executeQuery(sql.toString());
            while(rs.next()){
                beidou = new BeidouEntity();
                beidou.setBeidou_id(rs.getLong("beidou_id"));
                beidou.setEvent_name(rs.getString("event_name"));
                beidous.add(beidou);
            }
        }catch (Exception e){
            log.error("listAllBeidou error message is ",e);
        }finally {
            close(rs,stmt,con);
        }
        return beidous;
    }

    public List<BeidouEntity> getMoiveOfficial(String date) {
        return null;
    }


}

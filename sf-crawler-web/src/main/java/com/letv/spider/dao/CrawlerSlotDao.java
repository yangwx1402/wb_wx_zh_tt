package com.letv.spider.dao;

import com.gargoylesoftware.htmlunit.util.Cookie;

import com.letv.spider.entity.CrawlerSlotConfig;
import org.apache.commons.io.output.ByteArrayOutputStream;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.util.CollectionUtils;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


/**
 * Created by yangyong3 on 2016/12/1.
 */
public class CrawlerSlotDao extends AbstractDao {

    private static final Logger log = Logger.getLogger(CrawlerSlotDao.class);

    public List<CrawlerSlotConfig> getAllSlot() {
        return getSlotByType(null, 1);
    }

    public CrawlerSlotConfig getSlotByRandomAndType(String type) {
        List<CrawlerSlotConfig> configs = getSlotByRandomAndType(type, 1);
        if (!CollectionUtils.isEmpty(configs))
            return configs.get(0);
        return null;
    }

    private List<CrawlerSlotConfig> getSlotByRandomAndType(String type, int limit) {
        Connection con = null;
        Statement stmt = null;
        ResultSet rs = null;
        StringBuffer sql = new StringBuffer(100);
        List<CrawlerSlotConfig> slotConfigs = new ArrayList<CrawlerSlotConfig>();
        CrawlerSlotConfig config = null;
        try {
            con = DataSourceFactory.getDataSource().getConnection();
            sql.append("select * from sns_crawl_slot_config where 1=1 ");
            if (!StringUtils.isBlank(type)) {
                sql.append(" and type = '" + type + "'");
            }
            sql.append(" and in_use = 1 order by rand() limit " + limit);
            log.info("sql--[" + sql.toString() + "]");
            stmt = con.createStatement();
            rs = stmt.executeQuery(sql.toString());
            while (rs.next()) {
                config = new CrawlerSlotConfig();
                config.setId(rs.getInt(1));
                config.setUsername(rs.getString(2));
                config.setPassword(rs.getString(3));
                config.setProxy_ip(rs.getString(4));
                config.setProxy_port(rs.getInt(5));
                config.setProxy_user(rs.getString(6));
                config.setProxy_pass(rs.getString(7));
                config.setWeibo_key(rs.getString(9));
                config.setType(rs.getString(10));
                config.setCookie(rs.getString(11));
                slotConfigs.add(config);
            }
        } catch (Exception e) {
            log.error("getSlotByType error message is ", e);
        } finally {
            close(rs, stmt, con);
        }
        return slotConfigs;
    }

    public void updateCookie(String username, String password, String type, String cookies) {
        Connection con = null;
        PreparedStatement ps = null;
        StringBuffer sql = new StringBuffer(100);
        try {
            sql.append("update sns_crawl_slot_config set cookie = ? where username=? and password = ? and type = ? ");
            log.info("sql--[" + sql.toString() + "]");
            con = DataSourceFactory.getDataSource().getConnection();
            ps = con.prepareStatement(sql.toString());
            ps.setString(1, cookies);
            ps.setString(2, username);
            ps.setString(3, password);
            ps.setString(4, type);
            ps.executeUpdate();
        } catch (Exception e) {
            log.error("updateCookie error message is ", e);
        } finally {
            close(null, ps, con);
        }
    }

    public void updateCookie(String username, String password, String type, String cookies, Set<Cookie> cookieSet) {
        Connection con = null;
        PreparedStatement ps = null;
        StringBuffer sql = new StringBuffer(100);
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectOutputStream oos = null;
        try {
            sql.append("update sns_crawl_slot_config set cookie = ?,cookieset=? where username=? and password = ? and type = ? ");
            log.info("sql--[" + sql.toString() + "]");
            con = DataSourceFactory.getDataSource().getConnection();
            ps = con.prepareStatement(sql.toString());
            oos = new ObjectOutputStream(bos);
            if (cookieSet == null)
                cookieSet = new HashSet<Cookie>();
            oos.writeObject(cookieSet);
            ps.setString(1, cookies);
            ps.setBytes(2, bos.toByteArray());
            ps.setString(3, username);
            ps.setString(4, password);
            ps.setString(5, type);
            ps.executeUpdate();
        } catch (Exception e) {
            log.error("updateCookie error message is ", e);
        } finally {
            close(null, ps, con);
            if (oos != null)
                try {
                    oos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            if (bos != null)
                try {
                    bos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }

        }
    }

    public String getCookie(String username, String password, String type) {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        StringBuffer sql = new StringBuffer(100);
        List<CrawlerSlotConfig> slotConfigs = new ArrayList<CrawlerSlotConfig>();
        CrawlerSlotConfig config = null;
        try {
            con = DataSourceFactory.getDataSource().getConnection();
            sql.append("select cookie from sns_crawl_slot_config where username=? and password=? and type=? ");
            log.info("sql--[" + sql.toString() + "]");
            ps = con.prepareStatement(sql.toString());
            ps.setString(1, username);
            ps.setString(2, password);
            ps.setString(3, type);
            rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getString("cookie");
            }
        } catch (Exception e) {
            log.error("getCookie error message is ", e);
        } finally {
            close(rs, ps, con);
        }
        return null;
    }

    public Set<Cookie> getCookies(String username, String password, String type) {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        StringBuffer sql = new StringBuffer(100);
        ObjectInputStream ois = null;
        ByteArrayInputStream bis = null;
        try {
            con = DataSourceFactory.getDataSource().getConnection();
            sql.append("select cookieset from sns_crawl_slot_config where username=? and password=? and type=? ");
            log.info("sql--[" + sql.toString() + "]");
            ps = con.prepareStatement(sql.toString());
            ps.setString(1, username);
            ps.setString(2, password);
            ps.setString(3, type);
            rs = ps.executeQuery();
            if (rs.next()) {
                if (rs.getBytes(1) != null) {
                    bis = new ByteArrayInputStream(rs.getBytes(1));
                    ois = new ObjectInputStream(bis);
                    return (Set<Cookie>) ois.readObject();
                }
            }
        } catch (Exception e) {
            log.error("getCookie error message is ", e);
        } finally {
            close(rs, ps, con);
            if (bis != null)
                try {
                    bis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            if (ois != null)
                try {
                    ois.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
        }
        return new HashSet<Cookie>();
    }

    public CrawlerSlotConfig getSlotById(int id) {
        Connection con = null;
        Statement stmt = null;
        ResultSet rs = null;
        StringBuffer sql = new StringBuffer(100);
        CrawlerSlotConfig config = null;
        try {
            con = DataSourceFactory.getDataSource().getConnection();
            sql.append("select * from sns_crawl_slot_config where id =" + id);
            log.info("sql--[" + sql.toString() + "]");
            stmt = con.createStatement();
            rs = stmt.executeQuery(sql.toString());
            if (rs.next()) {
                config = new CrawlerSlotConfig();
                config.setId(rs.getInt(1));
                config.setUsername(rs.getString(2));
                config.setPassword(rs.getString(3));
                config.setProxy_ip(rs.getString(4));
                config.setProxy_port(rs.getInt(5));
                config.setProxy_user(rs.getString(6));
                config.setProxy_pass(rs.getString(7));
                config.setWeibo_key(rs.getString(9));
                config.setType(rs.getString(10));
                config.setCookie(rs.getString(11));
            }
        } catch (Exception e) {
            log.error("getSlotByType error message is ", e);
        } finally {
            close(rs, stmt, con);
        }
        return config;
    }

    public List<CrawlerSlotConfig> getSlotByType(String type, int in_use) {
        Connection con = null;
        Statement stmt = null;
        ResultSet rs = null;
        StringBuffer sql = new StringBuffer(100);
        List<CrawlerSlotConfig> slotConfigs = new ArrayList<CrawlerSlotConfig>();
        CrawlerSlotConfig config = null;
        try {
            con = DataSourceFactory.getDataSource().getConnection();
            sql.append("select * from sns_crawl_slot_config where 1=1 ");
            if (!StringUtils.isBlank(type)) {
                sql.append(" and type = '" + type + "'");
            }

            sql.append(" and in_use = " + in_use);
            sql.append(" order by rand() ");
            log.info("sql--[" + sql.toString() + "]");
            stmt = con.createStatement();
            rs = stmt.executeQuery(sql.toString());
            while (rs.next()) {
                config = new CrawlerSlotConfig();
                config.setId(rs.getInt(1));
                config.setUsername(rs.getString(2));
                config.setPassword(rs.getString(3));
                config.setProxy_ip(rs.getString(4));
                config.setProxy_port(rs.getInt(5));
                config.setProxy_user(rs.getString(6));
                config.setProxy_pass(rs.getString(7));
                config.setWeibo_key(rs.getString(9));
                config.setType(rs.getString(10));
                config.setCookie(rs.getString(11));
                slotConfigs.add(config);
            }
        } catch (Exception e) {
            log.error("getSlotByType error message is ", e);
        } finally {
            close(rs, stmt, con);
        }
        return slotConfigs;
    }

    public void updateIsCode(int id, int is_code) {
        Connection con = null;
        PreparedStatement ps = null;
        StringBuffer sql = new StringBuffer(100);
        try {
            sql.append("update sns_crawl_slot_config set is_code = ? where id=? ");
            log.info("sql--[" + sql.toString() + "]");
            con = DataSourceFactory.getDataSource().getConnection();
            ps = con.prepareStatement(sql.toString());
            ps.setInt(1,is_code);
            ps.setInt(2,id);
            ps.executeUpdate();
        } catch (Exception e) {
            log.error("updateCookie error message is ", e);
        } finally {
            close(null, ps, con);
        }
    }
}

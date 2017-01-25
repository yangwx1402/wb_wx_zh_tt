package com.letv.spider.dao;

import com.letv.spider.entity.CrawlerSlotConfig;

import java.sql.*;

/**
 * Created by yangyong3 on 2017/1/18.
 */
public class CookieDao extends AbstractDao{

    public void updateCookie(String username, String password, String cookie) {
        Connection con = null;
        PreparedStatement ps = null;
        try {
            String sql = "update sns_crawl_slot_config set cookie = ? where username=? and password=? and type=?";
            con = DataSourceFactory.getDataSource().getConnection();
            ps = con.prepareStatement(sql);
            System.out.println("sql --[" + sql + "]");
            ps.setString(1, cookie);
            ps.setString(2, username);
            ps.setString(3, password);
            ps.setString(4, "weibo");
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            close(null,ps,con);
        }
    }
}

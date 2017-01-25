package com.letv.sf.mysql;

import com.letv.sf.dao.DataSourceFactory;
import com.letv.sf.utils.WeiboIdUrlTrasfer;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

/**
 * Created by yangyong3 on 2016/12/20.
 */
public class ProcessWeiboUrl {
    public static void main(String[] args) throws Exception {
      Connection con = DriverManager.getConnection("jdbc:mysql://10.183.91.237:3306/beidou_datastore?useUnicode=true&characterEncoding=utf-8","beidou_datastore_w","ZjY2ZTI3YWVlOTc");
        Statement stmt = con.createStatement();
        String sql = "select user_id,mid from sns_weibo_sina_posts_chunwan where weibo_url is null";
        ResultSet rs = stmt.executeQuery(sql);
        String mid = null;
        String uid = null;
        String url = null;
        while(rs.next()){
            stmt = con.createStatement();
            mid = rs.getString("mid");
            uid = rs.getString("user_id");
            url = "www.weibo.com/"+uid+"/"+ WeiboIdUrlTrasfer.Mid2Uid(mid);
            sql = "update sns_weibo_sina_posts_chunwan set weibo_url='"+url+"' where mid = '"+mid+"'";
            stmt.executeUpdate(sql);
        }
        rs.close();
        stmt.close();
        con.close();

    }
}

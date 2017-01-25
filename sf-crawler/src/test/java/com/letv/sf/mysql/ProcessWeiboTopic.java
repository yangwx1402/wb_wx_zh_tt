package com.letv.sf.mysql;

import com.letv.sf.dao.DaoFactory;
import com.letv.sf.entity.weibo.WeiboTopicEventInfo;
import com.letv.sf.utils.StringUtils;
import com.letv.sf.utils.WeiboIdUrlTrasfer;

import java.sql.*;

/**
 * Created by yangyong3 on 2016/12/28.
 */
public class ProcessWeiboTopic {
    public static void main(String[] args) throws Exception {

        Connection con = DriverManager.getConnection("jdbc:mysql://10.183.91.237:3306/beidou_datastore?useUnicode=true&characterEncoding=utf-8", "beidou_datastore_w", "ZjY2ZTI3YWVlOTc");
        Statement stmt = con.createStatement();
        String sql = "select `text`,mid,beidou_id from sns_weibo_sina_posts_chunwan where `type`='topic' and topic_id = 0";
        ResultSet rs = stmt.executeQuery(sql);
        String mid = null;
        String text = null;
        String topicName = null;
        String beidou_id = null;
        WeiboTopicEventInfo info = null;
        while (rs.next()) {
            stmt = con.createStatement();
            mid = rs.getString("mid");
            text = rs.getString("text");
            beidou_id = rs.getString("beidou_id");
            topicName = StringUtils.findTwoCharContent(text, "#");
            info = DaoFactory.weiboDao.exsitTopicEventInfo(topicName);
            if (info != null) {
                sql = "update sns_weibo_sina_posts_chunwan set topic_id=" + info.getZip_id() + " where mid = " + mid + "";
                stmt.executeUpdate(sql);
            }
            else{
                info = new WeiboTopicEventInfo();
                info.setBeidou_id(Long.parseLong(beidou_id));
                info.setStatus("A");
                info.setType("wbtopic");
                info.setZip_value(topicName);
                DaoFactory.weiboDao.insertTopicEventInfo(info);
            }

        }
        rs.close();
        stmt.close();
        con.close();

    }
}

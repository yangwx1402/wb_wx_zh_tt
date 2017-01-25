package com.letv.sf.dataexport;

import com.letv.sf.dao.DataSourceFactory;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by yangyong3 on 2016/12/8.
 */
public class DataExportExample {
    public List<Object[]> findData(String sql) throws SQLException {
        Connection con = null;
        Statement stmt = null;
        ResultSet rs = null;
        List<Object[]> objs = new ArrayList<Object[]>();
        Object[] obj = null;
        try {
            con = DataSourceFactory.getDataSource().getConnection();
            stmt = con.createStatement();
            rs = stmt.executeQuery(sql);
            int count = rs.getMetaData().getColumnCount();
            while (rs.next()) {
                obj = new Object[count];
                for (int i = 0; i < count; i++) {
                    obj[i] = rs.getObject(i+1);
                }
                objs.add(obj);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            rs.close();
            stmt.close();
            con.close();
        }
        return objs;
    }
    public static void main(String[] args) throws SQLException, IOException {
        DataExportExample example = new DataExportExample();
        DataMapper mapper = new CommentDataMapper();
        String sql = "SELECT DISTINCT star.beidou_id,star.beidou_name,person.user_id,person.screen_name,person.name, person.gender ,person.raw_birthday,person.location,person.tags FROM sns_weibo_sina_posts_star star,`sns_weibo_sina_person` person WHERE star.beidou_id IN (4049753245152200,\n" +
                "4049740742251713,\n" +
                "4049988906326684,\n" +
                "4049809473654050,\n" +
                "4049822246173003,\n" +
                "4049826343140824,\n" +
                "4049818818587594,\n" +
                "4049823856196563,\n" +
                "4049820127286072,\n" +
                "4049831435444105,\n" +
                "4049823906335200,\n" +
                "4049993679167921,\n" +
                "4049817522469429,\n" +
                "4047965322978330,\n" +
                "4047981366379953,\n" +
                "4049783804802026,\n" +
                "4049820987085442,\n" +
                "4049796249103700,\n" +
                "4049820987085442,\n" +
                "4049991591113971,\n" +
                "4049768277103961)\n" +
                "AND star.user_id = person.user_id ORDER BY star.mid\n";
        List<Object[]> objs = example.findData(sql);
        for(Object[] obj:objs){
            FileUtils.writeStringToFile(new File("E:\\data.csv"),mapper.mapper(obj)+"\n","utf-8",true);
        }
    }
}

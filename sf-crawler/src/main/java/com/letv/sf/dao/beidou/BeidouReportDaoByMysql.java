package com.letv.sf.dao.beidou;

import com.letv.sf.dao.AbstractDao;
import com.letv.sf.dao.DataSourceFactory;
import com.letv.sf.entity.beidou.BeidouReport;
import com.letv.sf.entity.beidou.BeidouReportTable;
import org.apache.log4j.Logger;
import org.bson.Document;
import org.bson.conversions.Bson;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by yangyong3 on 2016/12/14.
 */
public class BeidouReportDaoByMysql extends AbstractDao implements BeidouReportDao {

    private static final Logger log = Logger.getLogger(BeidouReportDaoByMysql.class);

    public BeidouReport countReport(String sql) {
        Connection con = null;
        Statement stmt = null;
        ResultSet rs = null;
        BeidouReport report = null;
        try {
            con = DataSourceFactory.getDataSource().getConnection();
            log.info("sql --[" + sql.toString() + "]");
            stmt = con.createStatement();
            rs = stmt.executeQuery(sql.toString());
            if (rs.next()) {
                report = new BeidouReport();
                report.setBeidou_name(rs.getString("beidou_name"));
                report.setName(rs.getString("name"));
                report.setNum((long) rs.getInt("num"));
            }
        } catch (Exception e) {
            log.error("countReport error message is ", e);
        } finally {
            close(rs, stmt, con);
        }
        return report;
    }

    public List<BeidouReport> countReports(String sql) {
        Connection con = null;
        Statement stmt = null;
        ResultSet rs = null;
        BeidouReport report = null;
        List<BeidouReport> result = new ArrayList<BeidouReport>();
        try {
            con = DataSourceFactory.getDataSource().getConnection();
            log.info("sql --[" + sql.toString() + "]");
            stmt = con.createStatement();
            rs = stmt.executeQuery(sql.toString());
            while (rs.next()) {
                report = new BeidouReport();
                report.setBeidou_name(rs.getString("beidou_name"));
                report.setName(rs.getString("name"));
                report.setNum((long) rs.getInt("num"));
                result.add(report);
            }
        } catch (Exception e) {
            log.error("countReport error message is ", e);
        } finally {
            close(rs, stmt, con);
        }
        return result;
    }

    public BeidouReport computeReport(String tableName, Map<String, Object> params, String desc) {
        return null;
    }

    public List<BeidouReport> computeReports(String tableName, Map<String, Object> params, String desc) {
        return null;
    }

    public BeidouReportTable computeReportTable(String sql,Map<Long,String> beidou_mapping,String crawl_date) {
        Connection con = null;
        String[] fields = null;
        Statement stmt = null;
        ResultSet rs = null;
        BeidouReport report = null;
        ResultSetMetaData meta = null;
        BeidouReportTable table = new BeidouReportTable();
        Object[] data = null;
        List<Object[]> result = new ArrayList<Object[]>();
        try {
            con = DataSourceFactory.getDataSource().getConnection();
            log.info("sql --[" + sql.toString() + "]");
            stmt = con.createStatement();
            rs = stmt.executeQuery(sql.toString());
            meta = rs.getMetaData();
            int length = meta.getColumnCount();
            fields = new String[length];
            for (int i = 0; i < length; i++) {
                fields[i] = meta.getColumnName(i + 1);
            }

            while (rs.next()) {
                data = new Object[length];
                for (int i = 0; i < length; i++) {
                    data[i] = rs.getObject(i + 1);
                }
                result.add(data);
            }
            table.setData(result);
            table.setFields(fields);
        } catch (Exception e) {
            log.error("countReport error message is ", e);
        } finally {
            close(rs, stmt, con);
        }
        return table;
    }

    public Map<Long, String> getCatalogBeidouMapping(String crawl_date) {

        return null;
    }

    public Long count(String tableName, Bson sql) {
        return null;
    }
}

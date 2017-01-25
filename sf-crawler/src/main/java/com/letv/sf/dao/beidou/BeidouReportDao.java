package com.letv.sf.dao.beidou;

import com.letv.sf.entity.beidou.BeidouReport;
import com.letv.sf.entity.beidou.BeidouReportTable;
import org.bson.conversions.Bson;

import java.util.List;
import java.util.Map;

/**
 * Created by yangyong3 on 2016/12/14.
 */
public interface BeidouReportDao {

    public BeidouReport countReport(String sql);

    public List<BeidouReport> countReports(String sql);

    public BeidouReport computeReport(String tableName,Map<String,Object> params,String desc);

    public List<BeidouReport> computeReports(String tableName,Map<String,Object> params,String desc);

    public BeidouReportTable computeReportTable(String sql,Map<Long,String> beidou_mapping,String crawl_date);

    public Map<Long,String> getCatalogBeidouMapping(String crawl_date);

    public Long count(String tableName, Bson sql);
}

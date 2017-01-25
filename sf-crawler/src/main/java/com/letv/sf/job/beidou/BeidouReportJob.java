package com.letv.sf.job.beidou;

import com.letv.sf.config.YuqingType;
import com.letv.sf.dao.DaoFactory;
import com.letv.sf.entity.beidou.BeidouEntity;
import com.letv.sf.entity.beidou.BeidouReport;
import com.letv.sf.entity.beidou.BeidouReportLabel;
import com.letv.sf.entity.beidou.BeidouReportTable;
import com.letv.sf.utils.SendMailUtil;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.bson.Document;
import org.quartz.*;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by yangyong3 on 2016/12/14.
 * 北斗爬虫报告
 */
@DisallowConcurrentExecution
public class BeidouReportJob implements Job {

    private static final long interval = 1000 * 60 * 60 * 24;

    private static final int top_n = 30;

    private static final DateFormat df = new SimpleDateFormat("yyyy-MM-dd");

    private static final String[] weibo_all_header = new String[]{"名称", "微博数量", "微博话题数量", "微博评论", "微博用户"};

    private static final String[] yuqing_all_header = new String[]{"名称", "微信数量", "微信评论", "知乎问题", "知乎问题评论","知乎答案","知乎答案评论","百度帖子"};

    public void execute(JobExecutionContext context) throws JobExecutionException {
        JobDataMap map = null;
        if (context != null) {
            map = context.getJobDetail().getJobDataMap();
        }
        StringBuilder email_content = new StringBuilder(1024);
        List<String> email_list = getEmailList(map);
        String start = df.format(new Date(System.currentTimeMillis() - interval));
        String end = df.format(new Date(System.currentTimeMillis()));
        String weiboAllsql = "SELECT e.beidou_name,e.weibo_num,e.topic_num,e.weibo_comment,f.weibo_user FROM (SELECT c.*,d.weibo_comment FROM (SELECT a.*,b.topic_num FROM (SELECT beidou_id,beidou_name,COUNT(beidou_id) AS weibo_num FROM `sns_weibo_sina_posts` GROUP BY beidou_id ) a LEFT JOIN\n" +
                "(SELECT beidou_id,beidou_name,COUNT(beidou_id) AS topic_num  FROM `sns_weibo_sina_posts` WHERE `type`='topic' GROUP BY beidou_id ) b ON\n" +
                "a.beidou_id = b.beidou_id) c LEFT JOIN (SELECT `x`.beidou_id,COUNT(`y`.id) AS weibo_comment FROM `sns_weibo_sina_posts` `x`,`sns_weibo_sina_comment` `y` WHERE `x`.mid = `y`.mid GROUP BY `x`.beidou_id\n" +
                ") d ON c.beidou_id = d.beidou_id GROUP BY c.beidou_id) e LEFT JOIN (SELECT beidou_id, COUNT(user_id) AS weibo_user FROM `sns_weibo_sina_beidou_mapping` GROUP BY beidou_id ) f\n" +
                "ON e.beidou_id = f.`beidou_id` ORDER BY weibo_num DESC LIMIT " + top_n;
        BeidouReportTable weiboAlltable = DaoFactory.mysqlReport.computeReportTable(weiboAllsql,null,null);
        String email = generateEmailString("微博全量报告", weibo_all_header, weiboAlltable);
        email_content.append(email + "\n");
        String weiboAddSql = "SELECT e.beidou_name,e.weibo_num,e.topic_num,e.weibo_comment,f.weibo_user FROM (SELECT c.*,d.weibo_comment FROM (SELECT a.*,b.topic_num FROM (SELECT beidou_id,beidou_name,COUNT(beidou_id) AS weibo_num FROM `sns_weibo_sina_posts` WHERE created_at >='" + start + "' AND created_at<'" + end + "' GROUP BY beidou_id ) a LEFT JOIN\n" +
                "(SELECT beidou_id,beidou_name,COUNT(beidou_id) AS topic_num  FROM `sns_weibo_sina_posts` WHERE `type`='topic' AND created_at >='" + start + "' AND created_at<'" + end + "' GROUP BY beidou_id ) b ON\n" +
                "a.beidou_id = b.beidou_id) c LEFT JOIN (SELECT `x`.beidou_id,COUNT(`y`.id) AS weibo_comment FROM `sns_weibo_sina_posts` `x`,`sns_weibo_sina_comment` `y` WHERE `x`.mid = `y`.mid AND `y`.insert_time >='" + start + "' AND `y`.insert_time<'" + end + "' GROUP BY `x`.beidou_id\n" +
                ") d ON c.beidou_id = d.beidou_id GROUP BY c.beidou_id) e LEFT JOIN (SELECT beidou_id, COUNT(user_id) AS weibo_user FROM `sns_weibo_sina_beidou_mapping` WHERE insert_time >='" + start + "' AND insert_time<'" + end + "' GROUP BY beidou_id ) f\n" +
                "ON e.beidou_id = f.`beidou_id` ORDER BY weibo_num DESC LIMIT " + top_n;
        BeidouReportTable weiboAddTable = DaoFactory.mysqlReport.computeReportTable(weiboAddSql,null,null);
        email = generateEmailString(start + "微博增量报告", weibo_all_header, weiboAddTable);
        email_content.append(email+"\n");

        Map<Long,String> beidou_mapping = getBeidouMapping(start);

        String bson = "[\n" +
                "     {$match:{source:'"+ YuqingType.WEIXIN.getName()+"'}},\n" +
                "     {$group:{_id:\"$beidou_id\", count: {$sum:1}}},\n" +
                "     {$sort:{count:-1}},\n" +
                "     {$limit:"+top_n+"}\n" +
                "]";

        System.out.println(bson);
        BeidouReportTable weixinAllTable = generateMongoReport(bson,beidou_mapping,null);
        String weixinAllEmail = generateEmailString("微信知乎百度贴吧全量报告",yuqing_all_header,weixinAllTable);
        email_content.append(weixinAllEmail+"\n");
        bson = "[\n" +
                "     {$match:{insert_time:'"+start+"',source:'"+ YuqingType.WEIXIN.getName()+"'}},\n" +
                "     {$group:{_id:\"$beidou_id\", count: {$sum:1}}},\n" +
                "     {$sort:{count:-1}},\n" +
                "     {$limit:"+top_n+"}\n" +
                "]";
        BeidouReportTable weixinAddTable = generateMongoReport(bson,beidou_mapping,start);
        String weixinAddEmail = generateEmailString(start+"微信知乎百度贴吧增量报告",yuqing_all_header,weixinAddTable);
        email_content.append(weixinAddEmail);
        for (String address : email_list)
            SendMailUtil.sendEmail(address, address, start + "微博爬虫报告", email_content.toString(), true);
    }

    private Map<Long,String> getBeidouMapping(String start){
        Map<Long,String> beidou_mapping = DaoFactory.mongoReport.getCatalogBeidouMapping(start);
        List<BeidouEntity> entities = DaoFactory.beidouDao.listAllBeidou();
        for(BeidouEntity entity:entities){
            beidou_mapping.put(entity.getBeidou_id(),entity.getEvent_name());
        }
        return beidou_mapping;
    }

    private BeidouReportTable generateMongoReport(String json,Map<Long,String> beidou_mapping,String crawl_date) {
        return DaoFactory.mongoReport.computeReportTable(json,beidou_mapping,crawl_date);
    }

    private BeidouReport computeYuqing(String desc, String tableName, Map<String, Object> params) {
        return DaoFactory.mongoReport.computeReport(tableName, params, desc);
    }

    private List<BeidouReport> computeYuqings(String desc, String tableName, Map<String, Object> params) {
        return DaoFactory.mongoReport.computeReports(tableName, params, desc);
    }

    private List<String> getEmailList(JobDataMap map) {
        List<String> result = new ArrayList<String>();
        if (map == null || !map.containsKey("email")) {
            result.add("yangyong3@le.com");
            return result;
        }
        String emailString = map.getString("email");
        String[] emailList = emailString.split(";");

        if (emailList != null && emailList.length > 0) {
            for (String email : emailList) {
                result.add(email);
            }
        }
        return result;
    }

    private String generateEmailMessage(String title, String[] header, List<BeidouReport> labels, String starttime) {
        StringBuilder sb = new StringBuilder(100);
        sb.append("<html>\n<body>\n");
        sb.append("<h1>" + starttime + title + ":</h1>\n");
        sb.append("<table width='500' height='200' border=\"1\">\n<tr>");
        for (String head : header) {
            sb.append("<td>" + head + "</td>");
        }
        sb.append("</tr>\n");
        for (BeidouReport report : labels) {
            sb.append("<tr>");
            sb.append("<td>" + report.getBeidou_name() + "</td>");
            sb.append("<td>" + report.getName() + "</td>");
            sb.append("<td>" + report.getNum() + "</td>");
            sb.append("</tr>\n");
        }
        sb.append("</table>\n</body>\n</html>");
        return sb.toString();
    }

    private String generateEmailString(String title, String[] header, BeidouReportTable table) {
        StringBuilder sb = new StringBuilder(100);
        sb.append("<html>\n<body>\n");
        sb.append("<h1>" + title + ":</h1>\n");
        sb.append("<table width='500' height='200' border=\"1\">\n<tr>");
        for (String head : header) {
            sb.append("<td>" + head + "</td>");
        }
        sb.append("</tr>\n");
        for (Object[] temp : table.getData()) {
            sb.append("<tr>");
            for (Object obj : temp) {
                sb.append("<td>" + (obj == null ? "0" : obj) + "</td>");
            }

            sb.append("</tr>\n");
        }
        sb.append("</table>\n</body>\n</html>");
        return sb.toString();
    }

    private BeidouReportLabel compute(String sql, String field, String starttime, String endtime) {
        BeidouReportLabel label = new BeidouReportLabel();
        BeidouReport all = DaoFactory.mysqlReport.countReport(sql);
        BeidouReport add = DaoFactory.mysqlReport.countReport(sql + " and " + field + ">='" + starttime + "' and " + field + " < '" + endtime + "'");
        label.setAll(all);
        label.setAdd(add);
        return label;
    }

    private List<BeidouReport> computes(String sql, String field, String starttime, String endtime) {
        if (!StringUtils.isBlank(field) && !StringUtils.isBlank(starttime) && !StringUtils.isBlank(endtime)) {
            return DaoFactory.mysqlReport.countReports(sql + " and " + field + ">='" + starttime + "' and " + field + " < '" + endtime + "'");
        } else
            return DaoFactory.mysqlReport.countReports(sql);
    }

    public static void main(String[] args) throws JobExecutionException {
        BeidouReportJob job = new BeidouReportJob();
        job.execute(null);
        //job.generateMongoReport("");
    }
}

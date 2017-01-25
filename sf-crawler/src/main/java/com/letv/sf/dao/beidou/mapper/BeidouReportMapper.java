package com.letv.sf.dao.beidou.mapper;

import com.letv.sf.config.SpiderConfig;
import com.letv.sf.config.YuqingType;
import com.letv.sf.dao.DaoFactory;
import com.letv.sf.dao.mapper.MongoDocMapper;
import com.letv.sf.utils.StringUtils;
import com.mongodb.util.JSON;
import org.bson.Document;
import org.bson.conversions.Bson;
import sun.security.provider.ConfigFile;

import java.util.Map;

/**
 * Created by yangyong3 on 2016/12/26.
 */
public class BeidouReportMapper implements MongoDocMapper<Object[]> {

    private Map<Long, String> beidou_mapping;

    private String crawl_date;

    public BeidouReportMapper(Map<Long, String> beidou_mapping) {
        this.beidou_mapping = beidou_mapping;
    }

    public String getCrawl_date() {
        return crawl_date;
    }

    public void setCrawl_date(String crawl_date) {
        this.crawl_date = crawl_date;
    }

    public Document mapper(Object[] objects) {

        return null;
    }

    public Object[] deMapper(Document document) {
        Object[] temp = new Object[8];
        Long beidou_id = document.getLong("_id");
        temp[0] = beidou_mapping.get(beidou_id);
        temp[1] = document.getInteger("count");
        String json = null;
        if(org.apache.commons.lang.StringUtils.isBlank(crawl_date)){
            json = "{beidou_id:" + beidou_id + ",source:'" + YuqingType.WEIXIN_COMMENT.getName() + "'}";
        }else{
            json = "{beidou_id:" + beidou_id + ",insert_time:\"" + crawl_date + "\",source:'" + YuqingType.WEIXIN_COMMENT.getName() + "'}";
        }
        temp[2] = DaoFactory.mongoReport.count(SpiderConfig.getWeixinZhihuBaiduMongoCommentCollection(), (Bson) JSON.parse(json));
        Document zhihuquestion_document = new Document();
        StringBuffer sb = new StringBuffer();
        sb.append("function(){\n");
        if (!org.apache.commons.lang.StringUtils.isBlank(crawl_date)) {
            sb.append(" if(this.insert_time !='" + crawl_date + "') return false ;");
        }
        sb.append("    if(this.beidou_id != " + beidou_id + ")\n" +
                "        return false;\n" +
                "    if(this.source!='"+YuqingType.ZHIHU.getName()+"')\n" +
                "        return false;\n" +
                "    if(this.yqpid == this.yqid_answer)\n" +
                "        return true;\n" +
                "    else\n" +
                "        return false;\n" +
                "}");
        zhihuquestion_document.put("$where", sb.toString());
        System.out.println(zhihuquestion_document.toJson());
        temp[3] = DaoFactory.mongoReport.count(SpiderConfig.getWeixinZhihuBaiduMongoArticleCollection(), zhihuquestion_document);
        sb.setLength(0);
        if(org.apache.commons.lang.StringUtils.isBlank(crawl_date)){
            sb.append("{\"source\":'"+YuqingType.ZHIHU_QUESTION_COMMENT.getName()+"',\"beidou_id\":"+beidou_id+"}");
        }else
        sb.append("{\"source\":'"+YuqingType.ZHIHU_QUESTION_COMMENT.getName()+"',\"insert_time\":'"+crawl_date+"',\"beidou_id\":"+beidou_id+"}");
        temp[4] = DaoFactory.mongoReport.count(SpiderConfig.getWeixinZhihuBaiduMongoCommentCollection(),(Bson)JSON.parse(sb.toString()));
        Document zhihuAnswerDocument = new Document();
        sb.setLength(0);
        sb.append("function(){\n");
        if (!org.apache.commons.lang.StringUtils.isBlank(crawl_date)) {
            sb.append(" if(this.insert_time !='" + crawl_date + "') return false; ");
        }
        sb.append("    if(this.beidou_id != " + beidou_id + ")\n" +
                "        return false;\n" +
                "    if(this.source!='"+YuqingType.ZHIHU.getName()+"')\n" +
                "        return false;\n" +
                "    if(this.yqpid != this.yqid_answer)\n" +
                "        return true;\n" +
                "    else\n" +
                "        return false;\n" +
                "}");
        zhihuAnswerDocument.append("$where",sb.toString());
        temp[5] = DaoFactory.mongoReport.count(SpiderConfig.getWeixinZhihuBaiduMongoArticleCollection(),zhihuAnswerDocument);
        sb.setLength(0);
        if(org.apache.commons.lang.StringUtils.isBlank(crawl_date)){
            sb.append("{\"source\":'"+YuqingType.ZHIHU_ANSNER_COMMENT.getName()+"',\"beidou_id\":"+beidou_id+"}");
        }else
            sb.append("{\"source\":'"+YuqingType.ZHIHU_ANSNER_COMMENT.getName()+"',\"insert_time\":'"+crawl_date+"',\"beidou_id\":"+beidou_id+"}");
        temp[6] = DaoFactory.mongoReport.count(SpiderConfig.getWeixinZhihuBaiduMongoCommentCollection(),(Bson)JSON.parse(sb.toString()));
        sb.setLength(0);
        if(org.apache.commons.lang.StringUtils.isBlank(crawl_date)){
            sb.append("{\"source\":'"+YuqingType.BAIDU.getName()+"',\"beidou_id\":"+beidou_id+"}");
        }else
            sb.append("{\"source\":'"+YuqingType.BAIDU.getName()+"',\"insert_time\":'"+crawl_date+"',\"beidou_id\":"+beidou_id+"}");
        temp[7] = DaoFactory.mongoReport.count(SpiderConfig.getWeixinZhihuBaiduMongoArticleCollection(),(Bson)JSON.parse(sb.toString()));
        return temp;
    }
}

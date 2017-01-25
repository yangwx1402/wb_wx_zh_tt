package com.letv.sf.job.beidou;

import com.letv.sf.dao.DaoFactory;
import com.letv.sf.entity.weibo.WeiboTopicEventInfo;
import com.letv.sf.utils.SendMailUtil;
import org.apache.commons.collections.CollectionUtils;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by yangyong3 on 2017/1/6.
 */
public class BeidouChunwanTopicJob implements Job {

    private static final long interval = 1000 * 60 * 60 * 24;

    private static final DateFormat df = new SimpleDateFormat("yyyy-MM-dd");

    public void execute(JobExecutionContext context) throws JobExecutionException {
        JobDataMap map = null;
        if (context != null) {
            map = context.getJobDetail().getJobDataMap();
        }
        List<String> email_list = getEmailList(map);
        long beidou_id = 291701001l;
        String start = df.format(new Date(System.currentTimeMillis() - interval));
        String end = df.format(new Date(System.currentTimeMillis()));
        StringBuffer sb = new StringBuffer();
        try {
            List<WeiboTopicEventInfo> topics = DaoFactory.weiboDao.getWeiboTopicEventByTime(beidou_id, start, end);
            if (!CollectionUtils.isEmpty(topics)) {
                for (WeiboTopicEventInfo info : topics) {
                    sb.append(info.getZip_value() + "<br>");
                }
                for (String email : email_list) {
                    SendMailUtil.sendEmail(email, email, start + "春晚相关话题", sb.toString(), true);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
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
    public static void main(String[] args) throws JobExecutionException {
        BeidouChunwanTopicJob job = new BeidouChunwanTopicJob();
        job.execute(null);
    }
}

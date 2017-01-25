package com.letv.sf.job.weibo;

import com.letv.sf.config.ContentType;
import com.letv.sf.dao.DaoFactory;
import com.letv.sf.entity.beidou.BeidouEntity;
import com.letv.sf.entity.weibo.BeidouMapping;
import org.apache.commons.collections.CollectionUtils;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import java.util.List;

/**
 * Created by yangyong3 on 2017/1/24.
 */
@DisallowConcurrentExecution
public class OffcialWeiboTraceJob extends BaseWeiboTrace implements Job {

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        try {
            List<BeidouEntity> entities = DaoFactory.beidouDao.getKeywords(ContentType.official, null);
            String[] screen_names = new String[entities.size()];
            for (int i = 0; i < entities.size(); i++) {
                screen_names[i] = entities.get(i).getTag();
            }
            long now = System.currentTimeMillis();
            long before = System.currentTimeMillis() - (1000 * 60 * 60 * 24 * 2);
            List<BeidouMapping> mappings = DaoFactory.weiboDao.getOfficialTraceId(screen_names, before, now);
            if (CollectionUtils.isEmpty(mappings))
                return;
            process(mappings);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws JobExecutionException {
        OffcialWeiboTraceJob job = new OffcialWeiboTraceJob();
        job.execute(null);
    }
}

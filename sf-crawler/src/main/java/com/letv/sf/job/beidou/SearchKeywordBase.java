package com.letv.sf.job.beidou;

import com.letv.sf.config.ContentType;
import com.letv.sf.config.SpiderConfig;
import com.letv.sf.dao.beidou.BeidouDao;
import com.letv.sf.entity.beidou.BeidouEntity;
import com.letv.sf.mq.MessageFactory;
import com.letv.sf.mq.MessagePriority;
import com.letv.sf.utils.DateUtils;
import org.apache.commons.collections.CollectionUtils;

import java.io.IOException;
import java.util.Date;
import java.util.List;

/**
 * Created by yangyong3 on 2016/12/28.
 */
public class SearchKeywordBase {

    private static final long interval = 1000 * 60 * 60 * 24;


    protected void pubKeywordToMessageQueue(BeidouDao beidouDao, ContentType type, String queueName, boolean clear, MessagePriority priority) throws IOException {
        Date time = new Date();
        String date = DateUtils.dateString(time, SpiderConfig.insert_update_time_format);
        List<BeidouEntity> beidouEntities = beidouDao.getKeywords(type, date);
        if (CollectionUtils.isEmpty(beidouEntities)) {
            if (type == ContentType.movie || type == ContentType.official||type==ContentType.weixin) {
                date = DateUtils.dateString(new Date(time.getTime() - interval), SpiderConfig.insert_update_time_format);
                beidouEntities = beidouDao.getKeywords(type, date);
                if (CollectionUtils.isEmpty(beidouEntities))
                    return;
            } else
                return;
        }
        if (clear)
            MessageFactory.clear(queueName);
        for (BeidouEntity entity : beidouEntities) {
            MessageFactory.pub(queueName, entity, priority);
        }
    }

}

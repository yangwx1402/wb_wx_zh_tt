package com.letv.sf.job.thread.zhihu;

import com.letv.sf.config.SpiderConfig;
import com.letv.sf.entity.yuqing.YuqingArticle;
import com.letv.sf.job.zhihu.ZhihuProcess;
import com.letv.sf.mq.MessageFactory;
import org.apache.log4j.Logger;

/**
 * Created by yangyong3 on 2016/12/27.
 */
public class ZhihuQuestionPageCrawlerThread extends ZhihuProcess implements Runnable {

    private static final Logger log = Logger.getLogger(ZhihuProcess.class);

    private static final int default_seed = 10;

    public void run() {
        YuqingArticle article = null;
        while (true) {
            try {
                article = MessageFactory.sub(SpiderConfig.getZhihuQuestionQueueName(), YuqingArticle.class);
                if (article != null)
                    process(article);
                Thread.sleep(SpiderConfig.getSleepTimes(default_seed));
            } catch (Exception e) {
                e.printStackTrace();
                log.error("crawler zhihu question page error url is " + article.getUrl(), e);
                try {
                    Thread.sleep(SpiderConfig.getSleepTimes(default_seed));
                } catch (InterruptedException e1) {
                    e1.printStackTrace();
                }
            }
        }
    }
}

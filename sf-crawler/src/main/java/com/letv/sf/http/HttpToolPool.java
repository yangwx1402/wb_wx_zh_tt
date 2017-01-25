package com.letv.sf.http;

import com.letv.sf.dao.DaoFactory;
import com.letv.sf.entity.common.CrawlerSlotConfig;
import com.letv.sf.entity.common.HttpToolGroup;
import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;

import java.util.Date;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Created by yangyong3 on 2016/12/1.
 */
public class HttpToolPool extends AbstractHttpTool implements HttpTool {


    private static final Logger log = Logger.getLogger(HttpToolPool.class);

    private static boolean init_flag = true;

    private static final Map<String, BlockingQueue<HttpToolGroup>> httpPools = new Hashtable<String, BlockingQueue<HttpToolGroup>>();

    /**
     * 初始化爬取器池
     */
    private synchronized final void init() {
        if (init_flag) {
            log.info("start init httpToolPool  " + new Date());
            List<CrawlerSlotConfig> slots = null;
            slots = DaoFactory.weiboSlotDao.getAllSlot();
            HttpToolGroup group = null;
            if (CollectionUtils.isEmpty(slots)) {
                iniDefaultHttpToolFactory();
            } else {
                for (CrawlerSlotConfig config : slots) {
                    try {
                        group = initSlot(config);
                        if (httpPools.containsKey(config.getType())) {
                            httpPools.get(config.getType()).add(group);
                        } else {
                            LinkedBlockingQueue<HttpToolGroup> groups = new LinkedBlockingQueue<HttpToolGroup>();
                            groups.offer(group);
                            httpPools.put(config.getType(), groups);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                init_flag = false;
                log.info("end init httpToolPool  " + new Date());
            }
        }
    }


    private void iniDefaultHttpToolFactory() {

    }

    /**
     * 采用随机的策略来获取HttpTool来抓取页面，防止新浪屏蔽
     *
     * @return
     */

    public synchronized HttpToolGroup getHttpToolGroupByType(String type) {
        return getHttpToolGroupByCache(type);
    }


    private synchronized HttpToolGroup getHttpToolGroupByCache(String type) {
        init();
        if (httpPools.containsKey(type)) {
            try {
                return httpPools.get(type).take();
            } catch (InterruptedException e) {
                e.printStackTrace();
                return null;
            }
        }
        return null;
    }

    public void returnTool(HttpToolGroup group) {
        if (httpPools.containsKey(group.getType())) {
            httpPools.get(group.getType()).offer(group);
        }
    }

    public synchronized void destory() {
        init_flag = false;
        if (httpPools != null && httpPools.size() > 0)
            httpPools.clear();
    }

    public int size(String type) {
        if(httpPools.containsKey(type))
            return httpPools.get(type).size();
        return 0;
    }
}

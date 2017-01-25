package com.letv.sf.http;

import com.letv.sf.dao.DaoFactory;
import com.letv.sf.entity.common.CrawlerSlotConfig;
import com.letv.sf.entity.common.HttpToolGroup;

import java.io.IOException;

/**
 * Created by yangyong3 on 2017/1/11.
 */
public class HttpToolDefault extends AbstractHttpTool implements HttpTool {
    public HttpToolGroup getHttpToolGroupByType(String type) {
        CrawlerSlotConfig config = DaoFactory.weiboSlotDao.getSlotByRandomAndType(type);
        try {
            return initSlot(config);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void returnTool(HttpToolGroup group) {
    }

    public void destory() {
    }

    public int size(String type) {
        return 0;
    }


}

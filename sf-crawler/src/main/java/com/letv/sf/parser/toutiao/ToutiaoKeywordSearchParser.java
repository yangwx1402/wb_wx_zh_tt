package com.letv.sf.parser.toutiao;

import com.letv.sf.config.SpiderConfig;
import com.letv.sf.entity.beidou.BeidouEntity;
import com.letv.sf.entity.common.CrawlerPageInfo;
import com.letv.sf.entity.common.CrawlerResultEntity;
import com.letv.sf.http.HttpResult;
import com.letv.sf.parser.Parse;
import com.letv.sf.utils.DateUtils;
import org.apache.commons.collections.CollectionUtils;
import org.codehaus.jackson.map.ObjectMapper;

import java.util.*;

/**
 * Created by yangyong3 on 2017/1/5.
 */
public class ToutiaoKeywordSearchParser implements Parse<CrawlerResultEntity<Map<String, Object>>, BeidouEntity> {

    private static final ObjectMapper mapper = new ObjectMapper();

    public CrawlerResultEntity<Map<String, Object>> parse(HttpResult<BeidouEntity> httpPage) throws Exception {
        CrawlerResultEntity<Map<String, Object>> result = new CrawlerResultEntity<Map<String, Object>>();
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        Map<String, Object> map = mapper.readValue(httpPage.getContent(), Map.class);
        if (!map.containsKey("data")) {
            result.setItems(list);
            result.setPage(new CrawlerPageInfo(false));
            return result;
        }
        List<Map<String, Object>> temp = (List<Map<String, Object>>) map.get("data");
        Map<String, Object> copy = null;
        if (!CollectionUtils.isEmpty(temp)) {
            for (Map<String, Object> element : temp) {
                copy = new HashMap<String, Object>();
                if (!element.containsKey("id") || !element.containsKey("title"))
                    continue;
                for (Map.Entry<String, Object> entry : element.entrySet()) {
                    if (!SpiderConfig.getToutiaoArticleRemoveJsonFields().contains(entry.getKey()))
                        copy.put(entry.getKey(), entry.getValue());
                }
                copy.put("beidou_id", httpPage.getMeta().getBeidou_id());
                copy.put("search_tag", httpPage.getMeta().getTag());
                copy.put("beidou_name", httpPage.getMeta().getEvent_name());
                copy.put("insert_time", DateUtils.dateString(new Date(), SpiderConfig.insert_update_time_format));
                copy.put("state", SpiderConfig.getSinaWeiboSearchReadyFetchComment());
                copy.put("text", copy.get("abstract") == null ? "" : copy.get("abstract"));
                list.add(copy);
            }
            //说明还有下一页
            result.setPage(new CrawlerPageInfo(true));
        } else {
            //说明没有了已经
            result.setPage(new CrawlerPageInfo(false));
        }
        result.setItems(list);
        return result;
    }
}

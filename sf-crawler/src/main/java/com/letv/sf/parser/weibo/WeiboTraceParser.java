package com.letv.sf.parser.weibo;

import com.letv.sf.entity.common.CrawlerResultEntity;
import com.letv.sf.entity.weibo.BeidouMapping;
import com.letv.sf.entity.weibo.WeiboTrace;
import com.letv.sf.http.HttpResult;
import com.letv.sf.parser.Parse;
import weibo4j.org.json.JSONArray;
import weibo4j.org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yangyong3 on 2016/12/2.
 * 微博趋势跟踪解析
 */
public class WeiboTraceParser implements Parse<CrawlerResultEntity<WeiboTrace>,BeidouMapping> {
    public CrawlerResultEntity<WeiboTrace> parse(HttpResult<BeidouMapping> httpPage) throws Exception {
        JSONArray array = new JSONArray(httpPage.getContent());
        CrawlerResultEntity<WeiboTrace> result = new CrawlerResultEntity<WeiboTrace>();
        JSONObject jsonObject = null;
        List<WeiboTrace> list = new ArrayList<WeiboTrace>();
        WeiboTrace trace = null;
        if (array != null && array.length() > 0) {
            for (int i = 0; i < array.length(); i++) {
                trace = new WeiboTrace();
                jsonObject = (JSONObject) array.get(i);
                if (jsonObject.has("id")) {
                    trace.setMid(jsonObject.getLong("id"));
                }
                if (jsonObject.has("reposts")) {
                    trace.setReposts_count(jsonObject.getInt("reposts"));
                }
                if (jsonObject.has("comments")) {
                    trace.setComment_count(jsonObject.getInt("comments"));
                }
                if (jsonObject.has("attitudes")) {
                    trace.setAttitudes_count(jsonObject.getInt("attitudes"));
                }
                trace.setBeidou_id(httpPage.getMeta().getBeidou_id());
                list.add(trace);
            }
        }
        result.setItems(list);
        return result;
    }
}

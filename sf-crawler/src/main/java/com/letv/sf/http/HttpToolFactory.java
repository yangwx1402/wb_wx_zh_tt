package com.letv.sf.http;

import com.letv.sf.config.SpiderConfig;
import com.letv.sf.entity.common.HttpToolGroup;

/**
 * Created by yangyong3 on 2016/12/1.
 */
public class HttpToolFactory {

    private static final HttpTool pool = new HttpToolPool();
    private static final HttpTool defaultt = new HttpToolDefault();
    private static final HttpTool random = new HttpToolByRandom();
    private static final HttpToolByRedis redis = new HttpToolByRedis();

    private static HttpTool getHttpTool() {
        HttpTool tool = null;
        if("redis".equals(SpiderConfig.getSpiderHttpToolGroupSelectRule())){
            tool = redis;
        }else if ("pool".equals(SpiderConfig.getSpiderHttpToolGroupSelectRule())) {
            tool = pool;
        } else if ("random".equals(SpiderConfig.getSpiderHttpToolGroupSelectRule())) {
            tool = random;
        } else
            tool = defaultt;
        return tool;
    }

    public static synchronized HttpToolGroup getHttpToolGroupByType(String type) {
        HttpTool tool = getHttpTool();
        HttpToolGroup group = tool.getHttpToolGroupByType(type);
        if (group != null) {
            System.out.println("get a httpgroup rule=" + SpiderConfig.getSpiderHttpToolGroupSelectRule() + "type="+type+",pool = " + tool + ",tool=" + group+",now size is "+tool.size(type));
        } else {
            group = defaultt.getHttpToolGroupByType(type);
            System.out.println("get group error return default group ,rule=" + SpiderConfig.getSpiderHttpToolGroupSelectRule() + ",pool = " + tool + ",tool=" + group);
        }
        return group;
    }

    public static void returnTool(HttpToolGroup group) {
        HttpTool tool = getHttpTool();
        tool.returnTool(group);
        System.out.println("return group type is "+group.getType()+"now httpPools size  is -" + tool.size(group.getType()));
    }

    public static synchronized void destory() {
        redis.destory();
        pool.destory();
        random.destory();
        defaultt.destory();
    }

    public static void size(String type){

    }

}

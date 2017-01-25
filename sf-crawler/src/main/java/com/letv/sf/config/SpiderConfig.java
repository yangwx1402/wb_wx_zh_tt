package com.letv.sf.config;

import com.letv.sf.entity.common.config.*;
import com.letv.sf.utils.XmlUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.util.*;

/**
 * Created by yangyong3 on 2016/11/30.
 */
public class SpiderConfig {

    private static final Logger log = Logger.getLogger(SpiderConfig.class);

    public static final String SPIDER_REDIS_PREFIX = "SPIDER_SF:";

    //配置文件解析完以后的对象
    private static SpiderRoot spiderRoot;
    //配置文件中所有的property
    private static Map<String, String> properties;

    private static Set<String> priority_beidou_cache;

    private static final Random rand = new Random();

    public static final String insert_update_time_format = "yyyy-MM-dd";

    public static final String created_time_format = "yyyy-MM-dd HH:mm:ss";

    public static String getSns_weibo_sina_person_chunwan_table() {
        return getProperty("sns_weibo_sina_person_chunwan_table");
    }

    public static String getSns_weibo_sina_timeline_chunwan_table() {
        return getProperty("sns_weibo_sina_timeline_chunwan_table");
    }

    public static String getSns_weibo_sina_comment_chunwan_table() {
        return getProperty("sns_weibo_sina_comment_chunwan_table");
    }

    public static String getSns_weibo_sina_chunwan_trace_table() {
        return getProperty("sns_weibo_sina_chunwan_trace_table");
    }

    public static int getWeiboCommentFetchThreshold() {
        return Integer.parseInt(getProperty("SINA_WEIBO_COMMENT_FETCHER_THRESHOLD") == null ? "50" : getProperty("SINA_WEIBO_COMMENT_FETCHER_THRESHOLD"));
    }

    public static String getKeyWordQueueName() {
        return getProperty("KEY_WORD_QUEUE_NAME");
    }

    public static String getTopicKeyWordQueueName() {
        return getProperty("TOPIC_KEY_WORD_QUEUE_NAME");
    }

    public static String getWeixinXinKeyWordQueueName() {
        return getProperty("WEIXIN_XIN_KEY_WORD_QUEUE_NAME");
    }

    public static String getWeixinCommentSeedQueueName() {
        return getProperty("WEIXIN_PAGE_COMMENT_QUEUE_NAME");
    }

    public static int getWeiboSearchTimeIntervalHour() {
       return Integer.parseInt(getProperty("SINA_WEIBO_SEARCH_TIME_INTERVAL_HOUR") == null ? "0" : getProperty("SINA_WEIBO_SEARCH_TIME_INTERVAL_HOUR"));
    }

    public static String getZhihuKeyWordQueueName() {
        return getProperty("ZHIHU_KEY_WORD_QUEUE_NAME");
    }

    public static String getWeiboCommentQueueName() {
        return getProperty("WEIBO_COMMENT_QUEUE_NAME");
    }

    public static String getWeiboOfficialQueueName() {
        return getProperty("WEIBO_OFFICIAL_QUQUE_NAME");
    }

    public static String getZhihuQuestionQueueName() {
        return getProperty("ZHIHU_QUESTION_QUEUE_NAME");
    }

    public static String getSinaWeiboLoginUrl() {
        return getProperty("SINA_WEIBO_LOGIN_URL");
    }

    public static String getSinaWeiboLoginUsernameId() {
        return getProperty("SINA_WEIBO_LOGIN_USERNAME_ID");
    }

    public static String getSinaWeiboLoginPasswordId() {
        return getProperty("SINA_WEIBO_LOGIN_PASSWORD_ID");
    }

    public static String getSinaWeiboLoginButtonXpath() {
        return getProperty("SINA_WEIBO_LOGIN_BUTTON_XPATH");
    }

    public static String getCrawlerUserAgent() {
        return getProperty("CRAWLER_USER_AGENT");
    }

    public static long getSinaWeiboSleepTimeBase() {
        return Long.parseLong(getProperty("SINA_WEIBO_SLEEP_TIME_BASE"));
    }

    public static Integer getSinaWeiboSleepTimeIntervalLimit() {
        return Integer.parseInt(getProperty("SINA_WEIBO_SLEEP_TIME_INTERVAL_LIMIT"));
    }

    public static String getSinaWeiboSearchUrl() {
        return getProperty("SINA_WEIBO_SEARCH_URL");
    }

    public static String getSinaWeiboSearchUrlSubffix() {
        return getProperty("SINA_WEIBO_SEARCH_URL_SUBFFIX");
    }

    public static int getSinaWeiboSearchMaxPage() {
        return Integer.parseInt(getProperty("SINA_WEIBO_SEARCH_MAX_PAGE"));
    }

    public static String getSinaWeiboUserApiUrl() {
        return getProperty("SINA_WEIBO_USER_API_URL");
    }

    public static int getSinaWeiboSearchReadyFetchComment() {
        return Integer.parseInt(getProperty("SINA_WEIBO_SEARCH_READY_FETCH_COMMENT"));
    }

    public static int getSinaWeiboSearchFetchedComment() {
        return Integer.parseInt(getProperty("SINA_WEIBO_SEARCH_FETCHED_COMMENT"));
    }

    public static String getSinaWeiboCommetApiUrl() {
        return getProperty("SINA_WEIBO_COMMET_API_URL");
    }

    public static int getSinaWeiboCommentLimitNum() {
        return Integer.parseInt(getProperty("SINA_WEIBO_COMMENT_LIMIT_NUM"));
    }

    public static String getSinaWeiboTopicApiUrl() {
        return getProperty("SINA_WEIBO_TOPIC_API_URL");
    }

    public static String getSinaWeiboTraceApiUrl() {
        return getProperty("SINA_WEIBO_TRACE_API_URL");
    }

    public static long getSinaWeiboTraceInterval() {
        return Long.parseLong(getProperty("SINA_WEIBO_TRACE_INTERVAL"));
    }

    public static String getWeixinSearchUrl() {
        return getProperty("WEIXIN_SEARCH_URL");
    }

    public static String getWeixinCommentUrl() {
        return getProperty("WEIXIN_COMMENT_URL");
    }

    public static int getWeixinSearchPageLimit() {
        return Integer.parseInt(getProperty("WEIXIN_SEARCH_PAGE_LIMIT"));
    }

    public static String getZhihuSearchUrl() {
        return getProperty("ZHIHU_SEARCH_URL");
    }

    public static int getZhihuSearchPageLimit() {
        return Integer.parseInt(getProperty("ZHIHU_SEARCH_PAGE_LIMIT"));
    }

    public static String getWeixinZhihuBaiduMongoMdataCollection() {
        return getProperty("WEIXIN_ZHIHU_BAIDU_MONGO_MDATA_COLLECTION");
    }

    public static String getWeixinZhihuBaiduMongoArticleCollection() {
        return getProperty("WEIXIN_ZHIHU_BAIDU_MONGO_ARTICLE_COLLECTION");
    }

    public static String getWeixinZhihuBaiduMongoCommentCollection() {
        return getProperty("WEIXIN_ZHIHU_BAIDU_MONGO_COMMENT_COLLECTION");
    }

    public static String getToutiaoArticleMongoCollectionName() {
        return getProperty("TOUTIAO_ARTICLE_MONGO_COLLECTION_NAME");
    }

    public static String getToutiaoArticleCommentMongoCollectionName() {
        return getProperty("TOUTIAO_ARTICLE_COMMENT_MONGO_COLLECTION_NAME");
    }

    public static String getToutiaoCommentQueueName() {
        return getProperty("TOUTIAO_COMMENT_QUEUE_NAME");
    }

    public static String getToutiaoPageCommentUrl() {
        return getProperty("TOUTIAO_PAGE_COMMENT_URL");
    }

    public static int getToutiaoPageCommentSizeLimit() {
        return Integer.parseInt(getProperty("TOUTIAO_PAGE_COMMENT_SIZE_LIMIT") == null ? "1000" : getProperty("TOUTIAO_PAGE_COMMENT_SIZE_LIMIT"));
    }

    public static Set<String> getToutiaoArticleRemoveJsonFields() {
        Set<String> set = new HashSet<String>();
        String temp = getProperty("TOUTIAO_ARTICLE_REMOVE_JSON_FIELDS");
        if (!StringUtils.isBlank(temp))
            for (String line : temp.split(",")) {
                set.add(line.trim());
            }
        return set;
    }

    public static Set<String> getToutiaoPageCommentRemoveJsonFields() {
        Set<String> set = new HashSet<String>();
        String temp = getProperty("TOUTIAO_PAGE_COMMENT_REMOVE_JSON_FIELDS");
        if (!StringUtils.isBlank(temp))
            for (String line : temp.split(",")) {
                set.add(line.trim());
            }
        return set;
    }

    public static String getToutiaoKeywordSearchUrl() {
        return getProperty("TOUTIAO_KEYWORD_SEARCH_URL");
    }

    public static int getToutiaoKeywordSearchPageSize() {
        return getProperty("TOUTIAO_KEYWORD_SEARCH_PAGE_SIZE") == null ? 20 : Integer.parseInt(getProperty("TOUTIAO_KEYWORD_SEARCH_PAGE_SIZE"));
    }

    public static int getToutiaoKeyWordSearchLimitPageCount() {
        return getProperty("TOUTIAO_KEYWORD_SEARCH_LIMIT_PAGE_COUNT") == null ? 10 : Integer.parseInt(getProperty("TOUTIAO_KEYWORD_SEARCH_LIMIT_PAGE_COUNT"));
    }

    public static String getToutiaoSearchKeywordQueueName() {
        return getProperty("TOUTIAO_KEYWORD_SEARCH_QUEUE_NAME");
    }

    public static String getZhihuQuestionCommentUrl() {
        return getProperty("ZHIHU_QUESTION_COMMENT_URL");
    }

    public static String getZhihuAnswerCommentUrl() {
        return getProperty("ZHIHU_ANSWER_COMMENT_URL");
    }

    public static int getBaiduPostPageLimit() {
        return Integer.parseInt(getProperty("BAIDU_POST_PAGE_LIMIT"));
    }

    public static String getWeiboMoiveCatalogMongoCollection() {
        return getProperty("WEIBO_MOIVE_CATALOG_MONGO_COLLECTION");
    }

    public static Set<String> getSpiderCrawlerPriorityBeidous() {
        if (priority_beidou_cache == null) {
            priority_beidou_cache = new HashSet<String>();
            String beidou_ids = getProperty("SPIDER_CRAWLER_PRIORITY_BEIDOU");
            String[] temp = null;
            if (!StringUtils.isEmpty(beidou_ids)) {
                temp = beidou_ids.split(",");
                for (String line : temp) {
                    priority_beidou_cache.add(line);
                }
            }
        }
        return priority_beidou_cache;
    }

    /**
     * 获取爬虫爬取网页的间隔周期
     *
     * @return
     */
    public static long getSleepTimes(int seed) {
        long time = getSinaWeiboSleepTimeBase() + rand.nextInt(getSinaWeiboSleepTimeIntervalLimit()) * 1000 + rand.nextInt(seed) * 1000;
        log.info(Thread.currentThread().getName() + "sleep time -[" + time + "]");
        return time;
    }

    public static String getSpiderHttpToolGroupSelectRule() {
        return getProperty("SPIDER_HTTP_TOOL_GROUP_SELECT_RULE");
    }

    /**
     * 解析配置文件
     *
     * @param configPath
     */
    private synchronized static void initConfig(String configPath) {
        if (configPath == null) {
            configPath = SpiderConfig.class.getResource("/").getPath() + File.separator + "spider-config.xml";
        }
        if (spiderRoot == null) {
            String xml = null;
            try {
                xml = FileUtils.readFileToString(new File(configPath), "utf-8");
                XmlUtils xmlUtils = new XmlUtils(new Class[]{SpiderExecutor.class, SpiderJob.class, SpiderJobParam.class
                        , SpiderJobTrigger.class, SpiderMessagePool.class, SpiderMonitor.class, SpiderProperty.class, SpiderRoot.class
                        , SpiderThread.class, RedisConfig.class});
                spiderRoot = xmlUtils.fromXml(xml, SpiderRoot.class);
                properties = new Hashtable<String, String>();
                if (!CollectionUtils.isEmpty(spiderRoot.getProperties())) {
                    for (SpiderProperty property : spiderRoot.getProperties()) {
                        properties.put(property.getName().trim(), property.getValue().trim());
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    /**
     * 获取配置文件对应的对象信息
     *
     * @return
     */
    public synchronized static SpiderRoot getSpiderConfig() {
        initConfig(null);
        return spiderRoot;
    }

    /**
     * 通过该方法获取配置文件中的property信息
     *
     * @param key
     * @return
     */
    public synchronized static String getProperty(String key) {
        initConfig(null);
        return properties.get(key);
    }
}

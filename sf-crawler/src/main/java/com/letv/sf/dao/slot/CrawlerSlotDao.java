package com.letv.sf.dao.slot;

import com.gargoylesoftware.htmlunit.util.Cookie;
import com.letv.sf.entity.common.CrawlerSlotConfig;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by yangyong3 on 2016/12/1.
 */
public interface CrawlerSlotDao {

    public List<CrawlerSlotConfig> getWeiboSlot();

    public List<CrawlerSlotConfig> getWeixinSlot();

    public List<CrawlerSlotConfig> getZhihuSlot();

    public List<CrawlerSlotConfig> getBaiduSlot();

    public List<CrawlerSlotConfig> getAllSlot();

    public CrawlerSlotConfig getSlotByRandomAndType(String type);

    public void updateCookie(String username,String password,String type,String cookies);

    public void updateCookie(String username,String password,String type,String cookies,Set<Cookie> cookieSet);

    public String getCookie(String username, String password, String type);

    public Set<Cookie> getCookies(String username, String password, String type);

    public CrawlerSlotConfig getSlotById(int id);

    public List<CrawlerSlotConfig> getSlotByType(String type, int in_use);

    public void updateIsCode(int id,int is_code);
}

package com.letv.sf.http.cookie.support;

import com.gargoylesoftware.htmlunit.*;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.util.Cookie;
import com.letv.sf.config.SpiderConfig;
import com.letv.sf.dao.DaoFactory;
import com.letv.sf.entity.common.CookieEntity;
import com.letv.sf.entity.common.CrawlerSlotConfig;
import com.letv.sf.entity.common.ProxyInfo;
import com.letv.sf.http.cookie.CookieCrawler;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Created by yangyong3 on 2016/12/15.
 */
public abstract class AbstractCookieCrawler implements CookieCrawler {

    protected static final int js_timeout = 30000;

    protected WebClient webClient;

    protected CrawlerSlotConfig slotConfig;

    protected ProxyInfo proxy;

    private CookieEntity cookies;

    private static final Logger log = Logger.getLogger(WeiboCrawler.class);

    public AbstractCookieCrawler() {
    }

    public AbstractCookieCrawler(CrawlerSlotConfig slotConfig) {
        this.slotConfig = slotConfig;
        this.proxy = getProxy(slotConfig);
    }

    protected ProxyInfo getProxy(CrawlerSlotConfig config) {
        if (org.apache.commons.lang3.StringUtils.isBlank(config.getProxy_ip()))
            return null;
        ProxyInfo proxyInfo = new ProxyInfo();
        proxyInfo.setIp(config.getProxy_ip());
        proxyInfo.setPort(config.getProxy_port());
        proxyInfo.setUsername(config.getProxy_user());
        proxyInfo.setPassword(config.getProxy_pass());
        return proxyInfo;
    }

    private String cookie2String(Set<Cookie> cookies) {
        if (CollectionUtils.isEmpty(cookies))
            return null;
        StringBuilder sb = new StringBuilder(100);
        for (Cookie cookie : cookies) {
            sb.append(cookie.getName() + "=" + cookie.getValue() + ";");
        }
        return sb.toString();
    }

    /**
     * 从数据库中获取配置好的cookie
     *
     * @param username
     * @param password
     * @param type
     * @return
     */
    public CookieEntity getCookeFromPersist(String username, String password, String type) {
        if (cookies != null) {
            return cookies;
        }
        Set<Cookie> result = DaoFactory.weiboSlotDao.getCookies(username, password, type);
        if (!CollectionUtils.isEmpty(result)) {
            for (Cookie cookie : result) {
                //加载cookie
                getWebClient().getCookieManager().addCookie(cookie);
            }
            cookies = new CookieEntity();
            cookies.setCookieSet(result);
            return cookies;
        }
        String cookeString = DaoFactory.weiboSlotDao.getCookie(username, password, type);
        if (!StringUtils.isBlank(cookeString)) {
            //加载cookie
            getWebClient().addRequestHeader("Cookie", cookeString);
            cookies = new CookieEntity();
            cookies.setCookieString(cookeString);
            return cookies;
        }
        return cookies;
    }

    protected Map<String, String> getCookieMap(Set<Cookie> cookies) {
        if (CollectionUtils.isEmpty(cookies))
            return new HashMap<String, String>();
        Map<String, String> map = new HashMap<String, String>();
        for (Cookie cookie : cookies) {
            map.put(cookie.getName(), cookie.getValue());
        }
        return map;
    }

    /**
     * 持久化 cookie 信息
     *
     * @param username
     * @param password
     * @param type
     * @param cookies
     */
    protected void persistCookie(String username, String password, String type, Set<Cookie> cookies) {
        String cookieString = cookie2String(cookies);
        DaoFactory.weiboSlotDao.updateCookie(username, password, type, cookieString, cookies);
    }

    protected abstract Map<String, String> analogLogin() throws IOException;

    /**
     * 实现网站自动登录获取cookie
     *
     * @return cookie信息
     * @throws IOException
     */
    public Map<String, String> login() throws IOException {
        if (StringUtils.isBlank(slotConfig.getUsername()) || StringUtils.isBlank(slotConfig.getPassword())) {
            throw new IOException("login username and password 不能为空");
        }
        Map<String, String> cookieMap = new HashMap<String, String>();
        CookieEntity cookieCache = getCookeFromPersist(slotConfig.getUsername(), slotConfig.getPassword(), slotConfig.getType());
        //所有库里已经有cookie,那么就加载cookie
        if (cookieCache != null) {
            if (!CollectionUtils.isEmpty(cookieCache.getCookieSet())) {
                for (Cookie cookie : cookieCache.getCookieSet()) {
                    cookieMap.put(cookie.getName(), cookie.getValue());
                }
                return cookieMap;
            }
            if (!StringUtils.isBlank(cookieCache.getCookieString())) {
                String[] temp = cookieCache.getCookieString().split(";");
                String[] temp1 = null;
                for (String line : temp) {
                    if (!StringUtils.isBlank(line)) {
                        temp1 = line.split("=");
                        cookieMap.put(temp1[0], temp1[1]);
                    }
                }
                return cookieMap;
            }
        } else {
            cookieMap = analogLogin();
            return cookieMap;
        }
        return new HashMap<String, String>();
    }

    protected WebClient getWebClient() {
        if (webClient == null) {
            webClient = new WebClient(BrowserVersion.CHROME);
            webClient.getOptions().setJavaScriptEnabled(true);
            webClient.getOptions().setCssEnabled(false);
            webClient.addRequestHeader("User-Agent", SpiderConfig.getCrawlerUserAgent());
            if (proxy != null) {
                webClient.getOptions().setProxyConfig(new ProxyConfig(proxy.getIp(), proxy.getPort()));
                DefaultCredentialsProvider credentialsProvider = (DefaultCredentialsProvider) webClient.getCredentialsProvider();
                credentialsProvider.addCredentials(proxy.getUsername(), proxy.getPassword());
                webClient.setCredentialsProvider(credentialsProvider);
            }
            webClient.getOptions().setThrowExceptionOnScriptError(false);
            webClient.setAjaxController(new NicelyResynchronizingAjaxController());
            webClient.setJavaScriptTimeout(js_timeout);
            webClient.getOptions().setTimeout(js_timeout * 2);
        }
        return webClient;
    }

    public String fetchUrl(String url) throws Exception {
        long start = System.currentTimeMillis();
        try {
            login();
        } catch (IOException e) {
            throw new Exception("登录失败", e);
        }
        log.info("weiboCrawler crawler url -[" + url + "]");
        HtmlPage page = null;
        try {
            page = getWebClient().getPage(url);
        } catch (Exception e) {
            log.error("抓取失败,proxy is " + proxy, e);
        }
        getWebClient().waitForBackgroundJavaScript(js_timeout);
        log.info("weiboCrawler crawler url -[" + url + "] end cost time -[" + (System.currentTimeMillis() - start) + "]");
        close();
        if(page!=null)
            return page.asXml();
        return "";
    }


    public void close() {
        if (webClient != null) {
            webClient.close();
        }
    }

}

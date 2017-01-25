package com.letv.sf.http.cookie.support;

import com.gargoylesoftware.htmlunit.html.HtmlInput;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.util.Cookie;
import com.letv.sf.config.SpiderConfig;
import com.letv.sf.entity.common.CrawlerSlotConfig;
import com.letv.sf.http.cookie.CookieCrawler;
import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Created by yangyong3 on 2016/11/29.
 * 通过自动执行网页登录微博获取cookie信息
 */
public class WeiboCrawler extends AbstractCookieCrawler implements CookieCrawler {

    private static final Object lock = new Object();

    private static final Logger log = Logger.getLogger(WeiboCrawler.class);

    public WeiboCrawler() {
    }

    public WeiboCrawler(CrawlerSlotConfig slotConfig) {
        super(slotConfig);
    }

    /**
     * 实现自动登录微博
     *
     * @return cookie
     * @throws IOException
     */
    protected Map<String, String> analogLogin() throws IOException {
        Set<Cookie> cookieCache = null;
        Map<String, String> cookieMap = new HashMap<String, String>();
        synchronized (lock) {
            HtmlPage loginPage = getWebClient().getPage(SpiderConfig.getSinaWeiboLoginUrl());
            getWebClient().waitForBackgroundJavaScript(js_timeout);
            HtmlInput ln = loginPage.getHtmlElementById(SpiderConfig.getSinaWeiboLoginUsernameId());
            HtmlInput pwd = loginPage.getHtmlElementById(SpiderConfig.getSinaWeiboLoginPasswordId());
            HtmlInput btn = loginPage.getFirstByXPath(SpiderConfig.getSinaWeiboLoginButtonXpath());
            ln.setAttribute("value", slotConfig.getUsername());
            pwd.setAttribute("value", slotConfig.getPassword());
            HtmlPage page2 = btn.click();
            cookieCache = getWebClient().getCookieManager().getCookies();
            if (!CollectionUtils.isEmpty(cookieCache)) {
                persistCookie(slotConfig.getUsername(), slotConfig.getPassword(), slotConfig.getType(), cookieCache);
                for (Cookie cookie : cookieCache) {
                    cookieMap.put(cookie.getName(), cookie.getValue());
                }
            }
            return cookieMap;
        }
    }
}

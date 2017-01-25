package com.letv.sf.crawler;

import com.gargoylesoftware.htmlunit.*;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.letv.sf.config.SpiderConfig;
import com.letv.sf.dao.DaoFactory;
import com.letv.sf.entity.common.CrawlerSlotConfig;

import java.io.IOException;

/**
 * Created by yangyong3 on 2017/1/17.
 */
public class WebClientTest {
    public static void main(String[] args) throws IOException {
        CrawlerSlotConfig config = DaoFactory.weiboSlotDao.getSlotById(175);
        WebClient webClient = null;
        webClient = new WebClient(BrowserVersion.CHROME);
        webClient.getOptions().setJavaScriptEnabled(true);
        webClient.getOptions().setCssEnabled(false);
        webClient.addRequestHeader("User-Agent", SpiderConfig.getCrawlerUserAgent());
        webClient.getOptions().setRedirectEnabled(true);
        webClient.getOptions().setThrowExceptionOnFailingStatusCode(true);
//        webClient.getOptions().setProxyConfig(new ProxyConfig(config.getProxy_ip(), config.getProxy_port()));
//        DefaultCredentialsProvider credentialsProvider = (DefaultCredentialsProvider) webClient.getCredentialsProvider();
//        credentialsProvider.addCredentials(config.getProxy_user(), config.getProxy_pass());
//        webClient.setCredentialsProvider(credentialsProvider);
        webClient.getOptions().setThrowExceptionOnScriptError(false);
        webClient.setAjaxController(new NicelyResynchronizingAjaxController());
        webClient.addRequestHeader("Cookie",config.getCookie());
        HtmlPage page = webClient.getPage("http://s.weibo.com/weibo/%25E6%2598%25A5%25E6%2599%259A?topnav=1&wvr=6&b=1");
        webClient.waitForBackgroundJavaScript(60000);
        System.out.println(page.asXml());
    }
}

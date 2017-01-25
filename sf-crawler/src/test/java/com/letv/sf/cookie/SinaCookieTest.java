package com.letv.sf.cookie;

import com.gargoylesoftware.htmlunit.*;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.util.Cookie;
import com.letv.sf.config.SpiderConfig;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Created by yangyong3 on 2017/1/13.
 */
public class SinaCookieTest {

    public static String fetchUrl(String cookie, String url,Map<String,String> cookies) throws Exception {
        WebClient webClient = null;
        if (webClient == null) {
            webClient = new WebClient(BrowserVersion.CHROME);
            webClient.getOptions().setJavaScriptEnabled(true);
            webClient.getOptions().setCssEnabled(false);
            webClient.addRequestHeader("User-Agent", SpiderConfig.getCrawlerUserAgent());
            webClient.getOptions().setThrowExceptionOnScriptError(false);
            webClient.setAjaxController(new NicelyResynchronizingAjaxController());
            webClient.getOptions().setTimeout(30000);
            webClient.addRequestHeader("Cookie",cookie);
        }
        HtmlPage page = webClient.getPage(url);
        webClient.waitForBackgroundJavaScript(60000);

        return page.asXml();
    }

    public static void main(String[] args) throws Exception {
        String cookie = "WEB2_APACHE2_TC=e335f5192b5248f106f41f5a0324eab4;UWSGI_BX=a2aa6590be843a477d6dccfa408844bb;WEB2_APACHE2_YZ=d9f26ec119ae0fdccd8f566056496d91;ULV=1484708666776:1:1:1:123.125.26.230_1484708700.250690:;SINAGLOBAL=123.125.26.230_1484646842.298753;zid=6920ff5a18885a04fae4f463b6070a4c;sina_hy=1;UNID=E61A7D7B07BD24587EDB5E;tsc=3_587edb5c_587edb5c_0_1;a=WYgTc0bHdEo5;Apache=123.125.26.230_1484708700.250690;WEB3_PHP-FPM_BX=cc2ff44a3b2fa0d1af91f143585520ed;PHPSESSID=fo65nv4ou4jklf59d5a27hgcl1;TUIJIAN=usrmdinst_1;WEB3_PHP-FPM_BX=7d84fb9f704e8ecc0959cae9cba0803b;bdshare_firstime=1484708666320;UOR=,my.sina.com.cn,;NSC_wjq_txfjcp_mjotij=ffffffff094113d645525d5f4f58455e445a4a423660;WEB2_APACHE2_YZ=9127729b41cd6a54b9d698deb9dca6f5;U_TRS2=000000e6.b8c97330.587edb5b.ee5de70b;U_TRS1=000000e6.b8b47330.587edb5b.50d32311;SSOLoginState=1484708699;ALF=1516244699;SRF=1484708699;SRT=D.QqHBJZ4oSQEqOmMbPGYGS4uGi-oP4Zb9QrkuMCsHNEYdPqYTW!MpMERt4EPKRcsrA4kJP!o4TsVuiFsGT4PAdEbaJGi9MbHoN!sYW-9KM-b!NmkMI8t7*B.vAflW-P9Rc0lR-yk9DvnJqiQVbiRVPBtS!r3JZPQVqbgVdWiMZ4siOzu4DbmKPVsdFbJROVqSOYTiQzn4ePCUeHMT-Pgi49ndDPIJcYPSrnlMc0k4biIOOWOUqHZSOBbJcM1OFyHJGEJ5mjkODmpV4oCNrHJ5mjlODEI5!noTCuJ5mkiOmzkI4noTDPJ5mjkODmpV4oCNrHJ5mkoODEfI!oCI8sJ5mkoODmp5!noTGEr;SUHB=0TPFs7LvQkh1MC;SUBP=0033WrSXqPxfM725Ws9jqgMF55529P9D9W5XiLa73e8Z5r0RuBnrQkej5JpX5K2hUgL.Fo-RSKM7So2ce0e2dJLoI0qLxK-L12qLBKBLxK.L1KzL1h.LxKMLB--L1h5LxK-L12qLBKBLxKqL1h.LB.-LxKqL12zL1hqt;SUB=_2A251eqsLDeTxGeNG7lUR9i_KyD-IHXVW8ZvDrDV8PUNbmtAKLVLTkW94mGiEPXY_0siFRqJMawjNGidHKQ..;SCF=AmakNvXt9-ZnVKVf9g0l6jQhDuFzdKdQHqlLkJ-9lcKRo_7hPCEXrRlzNqnM9BXp3FCnvJJbfEGmye7KxH7AzWs.;ALF=1487300699;SSOLoginState=1484708699;ALF=1487300699;SSOLoginState=1484708699;cREMloginname=keji8023367%40163.com;sso_info=v02m6alo5qztKWRk5SljoSYpZCTkKWRk6SljpOIpY6DpKWRk5ylkJSUpY6DgKWRk5SljoSYpZCTkKWRk5iljoOkpZCklKWRk5iljoSIpY6DmKadlqWkj5OUuI2TnLCNo4S2jLOMwA==;LT=1484708699;ALF=1516244699;ALC=ac%3D27%26bt%3D1484708699%26cv%3D5.0%26et%3D1516244699%26scf%3D%26uid%3D5857061633%26vf%3D0%26vs%3D0%26vt%3D0%26es%3Db3d7b257ea9e76821324ee3f7ea81c36;SUBP=0033WrSXqPxfM725Ws9jqgMF55529P9D9W5XiLa73e8Z5r0RuBnrQkej5NHD95Qf1h-NehqpSoe0Ws4Dqcjci--fiKysi-2Xi--4iK.EiKn4i--Ni-88iKn7i--fiKysi-2Xi--ciKn4i-i8i--ciKyFiKnc;SUB=_2A251eqsLDeTxGeNG7lUR9i_KyD-IHXVW8ZvDrDV_PUNbm9AKLUSmkW8mT3Ktoqb87-VeSiFp6UfwFqLk8Q..;tgc=TGT-NTg1NzA2MTYzMw==-1484708699-xd-EC1BB89A67B51C538564049780E9386B-1;SCF=AmakNvXt9-ZnVKVf9g0l6jQhDuFzdKdQHqlLkJ-9lcKRo_7hPCEXrRlzNqnM9BXp3Mviz3ypFzGeszT0bZ4chyM.;ULOGIN_IMG=xd-e22181cfbf8d6b43d0fa7d8f9692a9b5d5bb;";
        String url = "http://s.weibo.com/weibo/%25E6%2598%25A5%25E6%2599%259A?topnav=1&wvr=6&b=1";
        Map<String,String> cookieCache = new HashMap<>();
        if (!StringUtils.isBlank(cookie)){
            String[] temp = cookie.split(";");
            String[] temp1 = null;
            for (String line : temp) {
                if (!StringUtils.isBlank(line)) {
                    temp1 = line.split("=");
                    cookieCache.put(temp1[0], temp1[1]);
                }
            }
        }
        String html = SinaCookieTest.fetchUrl(cookie, url,cookieCache);
        System.out.println(html);
    }
}

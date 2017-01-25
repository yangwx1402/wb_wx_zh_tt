package com.letv.sf.entity.common;

import com.letv.sf.http.HttpResult;
import com.letv.sf.http.HttpToolFactory;
import com.letv.sf.http.HttpUtils;
import com.letv.sf.http.JsoupClient;
import com.letv.sf.http.cookie.CookieCrawler;
import com.letv.sf.http.cookie.support.WeiboCrawler;

import java.io.IOException;
import java.util.Map;

/**
 * Created by yangyong3 on 2016/12/1.
 */
public class HttpToolGroup {

    private Integer id;

    private JsoupClient jsoupClient;

    private CookieCrawler cookieCrawler;

    private HttpUtils httpUtils;

    private String app_key;

    private String type;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getApp_key() {
        return app_key;
    }

    public void setApp_key(String app_key) {
        this.app_key = app_key;
    }

    public JsoupClient getJsoupClient() {
        return jsoupClient;
    }

    public void setJsoupClient(JsoupClient jsoupClient) {
        this.jsoupClient = jsoupClient;
    }

    public CookieCrawler getCookieCrawler() {
        return cookieCrawler;
    }

    public void setCookieCrawler(CookieCrawler cookieCrawler) {
        this.cookieCrawler = cookieCrawler;
    }

    public HttpUtils getHttpUtils() {
        return httpUtils;
    }

    public void setHttpUtils(HttpUtils httpUtils) {
        this.httpUtils = httpUtils;
    }

    /**
     * 抓取网页,该方法可以解析网页中的js脚本
     * @param url 网页url
     * @return
     * @throws Exception
     */
    public HttpResult getUrlByHtmlUnit(String url){
        String content = null;
        HttpResult httpResult = new HttpResult();
        if (cookieCrawler != null) {
            try {
                content = cookieCrawler.fetchUrl(url);
                HttpToolFactory.returnTool(this);
            } catch (Exception e) {
                e.printStackTrace();
                HttpToolFactory.returnTool(this);
            }
            httpResult.setContent(content);
        }
        return httpResult;
    }

    /**
     * 通过jsoup抓取网页
     * @param url 网页url
     * @param needCookie  是否需要cookie,如果需要cookie就通过CookieCrawler类来获取cookie
     * @return
     * @throws IOException
     */
    public HttpResult getUrlByJsoup(String url, boolean needCookie) {
        HttpResult result = null;
        if (jsoupClient != null) {
            Map<String, String> cookies = null;
            try {
                if (needCookie) {
                    cookies = cookieCrawler.login();
                    if (cookies != null && cookies.size() > 0)
                        jsoupClient.setCookies(cookies);
                }
                result = jsoupClient.get(url);
                HttpToolFactory.returnTool(this);
            }catch (Exception e){
                e.printStackTrace();
                HttpToolFactory.returnTool(this);
            }
        }
        return result;
    }

    /**
     * jsoup post
     * @param url 网页url
     * @param params  post参数
     * @param needCookie 是否需要cookie
     * @return
     * @throws IOException
     * @throws ClassNotFoundException
     */
    public HttpResult postUrlByJsoup(String url, Map<String, String> params, boolean needCookie)  {
        if (jsoupClient != null) {
            Map<String, String> cookies = null;
            try {
                if (needCookie) {
                    cookies = cookieCrawler.login();
                    if (cookies != null && cookies.size() > 0)
                        jsoupClient.setCookies(cookies);
                }
                HttpResult result = jsoupClient.post(url, params);
                HttpToolFactory.returnTool(this);
                return result;
            }catch (Exception e){
                e.printStackTrace();
                HttpToolFactory.returnTool(this);
            }
        }
        return null;
    }
}

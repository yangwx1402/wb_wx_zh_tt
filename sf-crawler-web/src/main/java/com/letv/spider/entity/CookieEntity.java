package com.letv.spider.entity;

import com.gargoylesoftware.htmlunit.util.Cookie;

import java.io.Serializable;
import java.util.Set;

/**
 * Created by yangyong3 on 2017/1/16.
 */
public class CookieEntity implements Serializable {
    private String cookieString;

    private Set<Cookie> cookieSet;

    public String getCookieString() {
        return cookieString;
    }

    public void setCookieString(String cookieString) {
        this.cookieString = cookieString;
    }

    public Set<Cookie> getCookieSet() {
        return cookieSet;
    }

    public void setCookieSet(Set<Cookie> cookieSet) {
        this.cookieSet = cookieSet;
    }
}

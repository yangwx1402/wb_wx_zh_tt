package com.letv.sf.entity.common;

import com.gargoylesoftware.htmlunit.util.Cookie;
import org.apache.commons.collections.CollectionUtils;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Created by yangyong3 on 2017/1/16.
 */
public class CookieEntity implements Serializable{
    private String cookieString;

    private Set<Cookie> cookieSet;

    private Map<String,String> cookieMap = new HashMap<String,String>();

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

    public Map<String, String> getCookieMap() {
        if(!CollectionUtils.isEmpty(cookieSet)){

        }
        return cookieMap;
    }

    public void setCookieMap(Map<String, String> cookieMap) {
        this.cookieMap = cookieMap;
    }
}

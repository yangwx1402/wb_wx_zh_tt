package com.letv.spider.entity;

import java.io.Serializable;

/**
 * Created by yangyong3 on 2016/12/1.
 */
public class CrawlerSlotConfig implements Serializable {

    private Integer id;

    private String username;

    private String password;

    private String proxy_ip;

    private Integer proxy_port;

    private String proxy_user;

    private String proxy_pass;

    private String cookie;

    private String weibo_key;

    private String type;

    public String getProxy_user() {
        return proxy_user;
    }

    public void setProxy_user(String proxy_user) {
        this.proxy_user = proxy_user;
    }

    public String getProxy_pass() {
        return proxy_pass;
    }

    public void setProxy_pass(String proxy_pass) {
        this.proxy_pass = proxy_pass;
    }

    public String getCookie() {
        return cookie;
    }

    public void setCookie(String cookie) {
        this.cookie = cookie;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }



    public String getProxy_ip() {
        return proxy_ip;
    }

    public void setProxy_ip(String proxy_ip) {
        this.proxy_ip = proxy_ip;
    }

    public Integer getProxy_port() {
        return proxy_port;
    }

    public void setProxy_port(Integer proxy_port) {
        this.proxy_port = proxy_port;
    }

    public String getWeibo_key() {
        return weibo_key;
    }

    public void setWeibo_key(String weibo_key) {
        this.weibo_key = weibo_key;
    }
}

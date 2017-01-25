package com.letv.sf.entity.common;

/**
 * Created by yangyong3 on 2016/12/1.
 */
public class ProxyInfo {

    private String ip;

    private int port;

    private String name;

    private String username;

    private String password;

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

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String toString() {
        return "proxy_ip=" + this.getIp() + ",proxy_port=" + this.getPort() + ",proxy_user=" + this.username + ",proxy_pass=" + this.password;
    }
}

package com.letv.sf.http;

import java.io.Serializable;

/**
 * Created by yangyong3 on 2016/11/29.
 * Crawler抓取网页返回结果
 */
public class HttpResult<T> implements Serializable{
    //status code
    private int code;
    //网页文本
    private String content;
    //错误信息
    private String message;
    //url
    private String url;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    private T meta;

    public T getMeta() {
        return meta;
    }

    public void setMeta(T meta) {
        this.meta = meta;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}

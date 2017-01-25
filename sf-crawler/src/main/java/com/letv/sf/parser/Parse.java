package com.letv.sf.parser;

import com.letv.sf.http.HttpResult;
import weibo4j.org.json.JSONException;

import java.io.IOException;

/**
 * Created by yangyong3 on 2016/11/30.
 */
public interface Parse<T,Y> {
    public T parse(HttpResult<Y> httpPage) throws Exception;
}

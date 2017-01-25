package com.letv.sf.parser.weibo;

import com.letv.sf.entity.beidou.BeidouEntity;
import com.letv.sf.http.HttpResult;
import com.letv.sf.parser.Parse;
import weibo4j.model.User;
import weibo4j.org.json.JSONObject;

/**
 * Created by yangyong3 on 2016/11/30.
 * 微博用户解析
 */
public class WeiboUserParser implements Parse<User,BeidouEntity> {
    public User parse(HttpResult httpPage) throws Exception {
        User user = new User(new JSONObject(httpPage.getContent()));
        return user;
    }
}

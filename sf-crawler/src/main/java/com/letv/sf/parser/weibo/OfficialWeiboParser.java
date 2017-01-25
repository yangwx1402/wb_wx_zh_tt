package com.letv.sf.parser.weibo;

import com.letv.sf.entity.beidou.BeidouEntity;
import com.letv.sf.http.HttpResult;
import com.letv.sf.parser.Parse;
import weibo4j.model.Status;
import weibo4j.model.StatusWapper;

import java.io.IOException;
import java.util.List;

/**
 * Created by yangyong3 on 2016/11/30.
 * 微博官微微博解析
 */
public class OfficialWeiboParser implements Parse<List<Status>,BeidouEntity> {
    public List<Status> parse(HttpResult httpPage) throws Exception {
        StatusWapper wapper = Status.constructWapperStatusJson(httpPage.getContent());
        return wapper.getStatuses();
    }
}

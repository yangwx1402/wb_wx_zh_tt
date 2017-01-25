package com.letv.sf.entity.baidu;

import com.letv.sf.entity.yuqing.YuqingArticle;
import com.letv.sf.entity.yuqing.YuqingMdata;

import java.util.List;

/**
 * Created by yangyong3 on 2016/12/12.
 */
public class BaiduPostEntity {
    private YuqingMdata mdata;

    private List<YuqingArticle> posts;

    public YuqingMdata getMdata() {
        return mdata;
    }

    public void setMdata(YuqingMdata mdata) {
        this.mdata = mdata;
    }

    public List<YuqingArticle> getPosts() {
        return posts;
    }

    public void setPosts(List<YuqingArticle> posts) {
        this.posts = posts;
    }
}

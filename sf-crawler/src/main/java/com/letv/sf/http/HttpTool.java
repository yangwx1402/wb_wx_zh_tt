package com.letv.sf.http;

import com.letv.sf.entity.common.HttpToolGroup;

/**
 * Created by yangyong3 on 2017/1/11.
 */
public interface HttpTool {

    public HttpToolGroup getHttpToolGroupByType(String type);

    public void returnTool(HttpToolGroup group);

    public void destory();

    public int size(String type);
}

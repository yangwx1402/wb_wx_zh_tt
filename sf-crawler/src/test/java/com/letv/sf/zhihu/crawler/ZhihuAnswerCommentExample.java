package com.letv.sf.zhihu.crawler;

import com.letv.sf.entity.yuqing.YuqingComment;
import com.letv.sf.parser.zhihu.ZhihuBaseParse;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yangyong3 on 2016/12/12.
 */
public class ZhihuAnswerCommentExample extends ZhihuBaseParse {
    public static void main(String[] args){
        ZhihuAnswerCommentExample example = new ZhihuAnswerCommentExample();
        List<YuqingComment> comments = new ArrayList<YuqingComment>();
        comments = example.getComments(comments,"28581369",1,null);
        System.out.println(comments.size());
    }
}

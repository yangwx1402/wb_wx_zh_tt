package com.letv.sf.config;

/**
 * Created by yangyong3 on 2016/12/6.
 * 舆情类型,对应表中source字段
 */
public enum YuqingType {

    //4中舆情类型
    //微信
    WEIXIN("01"),
    //知乎
    ZHIHU("02"),
    //天涯
    TIANYA("03"),
    //百度
    BAIDU("04"),

    TOUTIAO("05"),

    //下面为评论类型
    //微信评论
    WEIXIN_COMMENT("010"),
    //知乎回答,未用
    ZHIHU_ANSWER("020"),
    //知乎答案评论
    ZHIHU_ANSNER_COMMENT("021"),
    //知乎问题 未用
    ZHIHU_QUESTION("022"),
    //知乎问题评论
    ZHIHU_QUESTION_COMMENT("023"),
    //天涯评论
    TIANYA_COMMENT("030"),
    //百度贴吧评论
    BAIDU_TIEBA_COMMENT("040"),

    TOUTIAO_COMMENT("050");

    String name;

    YuqingType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}

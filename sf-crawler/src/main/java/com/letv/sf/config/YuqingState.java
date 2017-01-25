package com.letv.sf.config;

/**
 * Created by yangyong3 on 2016/12/6.
 * 舆情评论处理状态
 */
public enum YuqingState {
    //未抓取评论
    NO_FETCH_COMMENT(0),
    //已抓取评论
    FETCHED_COMMENT(1);

    private int state;
    YuqingState(int state){
        this.state = state;
    }

    public int getState(){
        return state;
    }
}

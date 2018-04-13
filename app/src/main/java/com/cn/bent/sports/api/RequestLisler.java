package com.cn.bent.sports.api;

/**
 * Created by Administrator on 2018/4/13/013.
 */

public interface RequestLisler<T>{
    void onSucess(int whichRequest, T t);
    void on_error(int whichRequest, Throwable e);
}

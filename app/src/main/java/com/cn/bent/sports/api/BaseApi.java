package com.cn.bent.sports.api;

import android.content.Context;

import com.zhl.network.RxRetrofit;

/**
 * Created by Administrator on 2017/9/14.
 */

public class BaseApi {
    public static ApiService apiService;


    public static ApiService getDefaultService(Context context) {
        if (apiService == null) {
            apiService = RxRetrofit.getRetrofit(ConstantValues.BASE_URL,context).create(ApiService.class);
        }
        return apiService;
    }
}

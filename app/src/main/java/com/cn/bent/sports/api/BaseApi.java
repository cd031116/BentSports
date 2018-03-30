package com.cn.bent.sports.api;

import android.content.Context;

import com.cn.bent.sports.base.BaseConfig;
import com.cn.bent.sports.bean.LoginResult;
import com.cn.bent.sports.utils.Constants;
import com.cn.bent.sports.utils.SaveObjectUtils;
import com.yuyh.library.imgsel.common.Constant;
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
    public static ApiService getJavaDefaultService(Context context) {
        if (apiService == null) {
            apiService = RxRetrofit.getRetrofit(ConstantValues.JAVA_URL,context).create(ApiService.class);
        }
        return apiService;
    }
    public static ApiService getJavaLoginService(Context context) {
        if (apiService == null) {
            apiService = RxRetrofit.getLoginRetrofit(ConstantValues.JAVA_URL,context).create(ApiService.class);
        }
        return apiService;
    }
    public static ApiService getJavaLoginDefaultService(Context context) {
            LoginResult user= SaveObjectUtils.getInstance(context).getObject(Constants.USER_INFO, null);
            apiService = RxRetrofit.getLoginRetrofit(ConstantValues.JAVA_URL,context,user.getAccess_token()).create(ApiService.class);
        return apiService;
    }
}

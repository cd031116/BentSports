package com.cn.bent.sports.api;


import com.cn.bent.sports.bean.LoginBase;
import com.zhl.network.huiqu.HuiquResult;
import com.zhl.network.huiqu.HuiquTBResult;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

/**
 * Created by necer on 2017/6/28.
 */

public interface ApiService {


    /* getcode
     *
     * @return
     */
    @FormUrlEncoded
    @POST("outdoor/user/getCheckCode")
    Observable<HuiquTBResult> getcode(
            @Field("phone") String phone);

    /**
     * 新闻评论
     *
     * @return
     */
    @FormUrlEncoded
    @POST("outdoor/user/insertMemberInfo")
    Observable<HuiquResult<LoginBase>> Loging(
            @Field("mobile") String mobile,
            @Field("code") String code);

}

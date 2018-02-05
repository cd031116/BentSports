package com.cn.bent.sports.api;


import com.cn.bent.sports.bean.AddScoreEntity;
import com.cn.bent.sports.bean.AllFinishEntity;
import com.cn.bent.sports.bean.LoginBase;
import com.cn.bent.sports.bean.PhotoPath;
import com.cn.bent.sports.bean.RankEntity;
import com.cn.bent.sports.bean.UserMsgEntity;
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
            @Field("mobile") String phone);

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

    /**
     * @return
     */
    @FormUrlEncoded
    @POST("outdoor/user/addscore")
    Observable<AddScoreEntity> addScore(
            @Field("cur_user_id") String mobile,
            @Field("score") int code,
            @Field("game_id") int game_id);

    /**
     * 获得用户的基本信息
     *
     * @return
     */
    @FormUrlEncoded
    @POST("outdoor/user/getUserMsg")
    Observable<HuiquResult<UserMsgEntity>> getUserMsg(
            @Field("cur_user_id") String cur_user_id);

    /**
     * 全部任务完成界面
     *
     * @return
     */
    @FormUrlEncoded
    @POST("outdoor/user/getGameRecord")
    Observable<HuiquResult<AllFinishEntity>> getGameRecord(
            @Field("cur_user_id") String cur_user_id);

    @GET("outdoor/user/ranklist")
    Observable<HuiquResult<RankEntity>> getRankList();

    /*
            *
     *
             * @return
             */
    @FormUrlEncoded
    @POST("outdoor/user/modifyUserMsg")
    Observable<HuiquTBResult> modifyUserMsg(
            @Field("cur_user_id") String cur_user_id,
            @Field("type") String type,
            @Field("nickname") String nickname);
    /*
                *
         *
                 * @return
                 */
    @FormUrlEncoded
    @POST("outdoor/user/modifyUserMsg")
    Observable<HuiquResult<PhotoPath>> modifyUserPhoto(
            @Field("cur_user_id") String cur_user_id,
            @Field("type") String type,
            @Field("img") String img);


    /*
      *
       *
        * @return
      */
    @FormUrlEncoded
    @POST("outdoor/user/startGame")
    Observable<HuiquTBResult> startGame(
            @Field("cur_user_id") String cur_user_id);

}

package com.cn.bent.sports.api;


import com.cn.bent.sports.bean.AddScoreEntity;
import com.cn.bent.sports.bean.AllFinishEntity;
import com.cn.bent.sports.bean.LinesPointsDetailEntity;
import com.cn.bent.sports.bean.LinesPointsEntity;
import com.cn.bent.sports.bean.LoginBase;
import com.cn.bent.sports.bean.PhotoPath;
import com.cn.bent.sports.bean.PlayMapBean;
import com.cn.bent.sports.bean.RailBean;
import com.cn.bent.sports.bean.RankEntity;
import com.cn.bent.sports.bean.ScenicPointsEntity;
import com.cn.bent.sports.bean.ScenicSpotEntity;
import com.cn.bent.sports.bean.UserMsgEntity;
import com.zhl.network.huiqu.HuiquResult;
import com.zhl.network.huiqu.HuiquTBResult;
import com.zhl.network.huiqu.JavaResult;
import com.zhl.network.huiqu.ResponseResult;

import java.util.List;

import io.reactivex.Observable;
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
     * 获取石燕湖数据
     *
     * @param scenicSpotId
     * @return
     */
    @FormUrlEncoded
    @POST("outdoor?str=getSpotsByParkCode")
    Observable<ResponseResult<ScenicSpotEntity>> getscenicSpotData(
            @Field("scenicSpotId") String scenicSpotId);

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


    @GET("outdoor/map/getFenceAndDot")
    Observable<HuiquResult<RailBean>> getFenceAndDot();


    /**
     * 获取线路
     * @param id
     * @return
     */
    @GET("api/travel/scenic/noauth/{id}/lines")
    Observable<JavaResult<List<LinesPointsEntity>>> getScenicLines(@Path("id") String id);

    /**
     * 获取线路
     * @param id
     * @return
     */
    @GET("api/travel/line/noauth/{id}/detail")
    Observable<JavaResult<LinesPointsDetailEntity>> getScenicLinesDetail(@Path("id") int id);

    /**
     * 获取点位
     * @param id
     * @return
     */
    @GET("api/travel/scenic/noauth/{id}/points")
    Observable<JavaResult<ScenicPointsEntity>> getScenicPoints(@Path("id") String id);

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


    /**
     * @return
     */
    @FormUrlEncoded
    @POST("outdoor/map/getMapMsg")
    Observable<HuiquResult<PlayMapBean>> getMapMsg(
            @Field("cur_user_id") String cur_user_id);
}

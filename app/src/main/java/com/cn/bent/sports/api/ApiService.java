package com.cn.bent.sports.api;


import com.cn.bent.sports.bean.AddScoreEntity;
import com.cn.bent.sports.bean.AllFinishEntity;
import com.cn.bent.sports.bean.GameDetail;
import com.cn.bent.sports.bean.GameInfo;
import com.cn.bent.sports.bean.GamePotins;
import com.cn.bent.sports.bean.GameTeamScoreEntity;
import com.cn.bent.sports.bean.LinesDetailEntity;
import com.cn.bent.sports.bean.LinesPointsDetailEntity;
import com.cn.bent.sports.bean.LinesPointsEntity;
import com.cn.bent.sports.bean.LoginBase;
import com.cn.bent.sports.bean.LoginResult;
import com.cn.bent.sports.bean.MemberDataEntity;
import com.cn.bent.sports.bean.PhotoPath;
import com.cn.bent.sports.bean.PlayMapBean;
import com.cn.bent.sports.bean.PointsDetailEntity;
import com.cn.bent.sports.bean.RailBean;
import com.cn.bent.sports.bean.RankEntity;
import com.cn.bent.sports.bean.ScenicPointsEntity;
import com.cn.bent.sports.bean.ScenicSpotEntity;
import com.cn.bent.sports.bean.TeamGame;
import com.cn.bent.sports.bean.UserMsgEntity;
import com.cn.bent.sports.view.activity.youle.bean.JoinTeam;
import com.cn.bent.sports.view.activity.youle.bean.UserInfo;
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
import retrofit2.http.Query;

/**
 * Created by necer on 2017/6/28.
 */

public interface ApiService {


    /* getcode
     *
     * @return
     */
    @FormUrlEncoded
    @POST("se/oauth/token")
    Observable<LoginResult> loginWithPass(
            @Field("grant_type") String grant_type,
            @Field("username") String username,
            @Field("password") String password);

    @FormUrlEncoded
    @POST("se/oauth/token")
    Observable<LoginResult> loginWithRefreshToken(
            @Field("grant_type") String grant_type,
            @Field("refresh_token") String refresh_token);


    @GET("api/travel/applcations")
    Observable<JavaResult<String>> getWebSocket();

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

    /*获取用户信息
    * */
    @GET("api/basic/user")
    Observable<JavaResult<UserInfo>> getUserInfo();

    /**
     * 登录
     *
     * @return
     */
    @FormUrlEncoded
    @POST("se/oauth/token")
    Observable<LoginResult> Loging(
            @Field("grant_type") String grant_type,
            @Field("username") String username,
            @Field("password") String password);


    /**
     * 微信登录
     *
     * @return
     */
    @FormUrlEncoded
    @POST("se/oauth/token")
    Observable<LoginResult> weichatLogin(
            @Field("grant_type") String grant_type,
            @Field("username") String unicid,
            @Field("password") String password,
            @Field("source") String source,
            @Field("nickName") String nickName,
            @Field("avatarUrl") String avatarUrl
    );

    /**
     * 获取景区游戏列表
     *
     * @return
     */
    @GET("api/travel/game/")
    Observable<JavaResult<List<GameInfo>>> getGameList(
            @Query("scenicId") String scenicId);

    /**
     * 获取游戏详情
     *
     * @return
     */
    @GET("api/travel/game/{id}")
    Observable<JavaResult<GameDetail>> getGameDetail(
            @Path("id") String id);

    /**
     * 准备页面
     *
     * @return
     */
    @GET("api/travel/game/{id}/prepare")
    Observable<JavaResult<String>> getGamePrapre(
            @Path("id") String id);

    /**
     * 获取游戏点标
     *
     * @return
     */
    @GET("api/travel/game/{id}/game_points")
    Observable<JavaResult<List<GamePotins>>> getPoints(
            @Path("id") String id, @Query("gameLineId") String gameLineId);

    /**
     * 获取团队积分情况
     *
     * @return
     */
    @GET("api/travel/game_team/{teamId}/score")
    Observable<JavaResult<List<GameTeamScoreEntity>>> getTeamScore(
            @Path("id") String id, @Path("teamId") String teamId);

    /**
     * 获取某个游戏点的完成情况
     *
     * @return
     */
    @GET("api/travel/game_team/{teamId}/task/{gamePointId}")
    Observable<JavaResult<List<GameTeamScoreEntity>>> getPointTask(
            @Path("teamId") String id, @Path("gamePointId") String gamePointId);


    /**
     * 强制退赛
     *
     * @return
     */
    @FormUrlEncoded
    @POST("api/travel/game_team/{teamId}/task")
    Observable<JavaResult<Boolean>> outTeamGame(@Path("teamId") String teamId, @Field("timing") String timing);

    /**
     * 开始游戏
     *
     * @return
     */
    @FormUrlEncoded
    @POST("api/travel/game_team/{teamId}/_start")
    Observable<JavaResult<Boolean>> startTeamGame(@Field("teamId") String teamId);

    /**
     * 创建组队
     *
     * @return
     */
    @FormUrlEncoded
    @POST("api/travel/game_team")
    Observable<JavaResult<TeamGame>> getTeamGame(@Field("gameId") String gameId);


    /*teamId 加入组队
    * */
    @FormUrlEncoded
    @POST("api/travel/game_team/{teamId}/_join")
    Observable<JavaResult<JoinTeam>> joinTeamGame(@Path("teamId") String teamId);


    @GET("outdoor/map/getFenceAndDot")
    Observable<HuiquResult<RailBean>> getFenceAndDot();


    /**
     * 获取线路
     *
     * @param id
     * @return
     */
    @GET("api/travel/scenic/noauth/{id}/lines")
    Observable<JavaResult<List<LinesPointsEntity>>> getScenicLines(@Path("id") String id);

    /**
     * 获取线路详情
     *
     * @param id
     * @return
     */
    @GET("api/travel/line/noauth/{id}/detail")
    Observable<JavaResult<LinesDetailEntity>> getScenicLinesDetail(@Path("id") int id);

    /**
     * 获取点位
     *
     * @param id
     * @return
     */
    @GET("api/travel/scenic/noauth/{id}/points")
    Observable<JavaResult<ScenicPointsEntity>> getScenicPoints(@Path("id") String id);

    /**
     * 获取当前点位详情
     *
     * @param id
     * @return
     */
    @GET("api/travel/point/noauth/vo/{id}")
    Observable<JavaResult<PointsDetailEntity>> getPointsDetailData(@Path("id") String id);

    /**
     * 获取队员信息（后面的接口都只有id 头像等信息前端需要存起来）
     *
     * @param teamId
     * @return
     */
    @GET("api/travel//game_team/{teamId}/members")
    Observable<JavaResult<List<MemberDataEntity>>> getMemberDetailData(@Path("teamId") int teamId);

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

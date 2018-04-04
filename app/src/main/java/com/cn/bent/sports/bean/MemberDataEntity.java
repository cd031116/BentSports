package com.cn.bent.sports.bean;

import org.aisen.android.component.orm.annotation.PrimaryKey;

import java.io.Serializable;

/**
 * Created by dawn on 2018/3/31.
 * 游戏队伍成员信息
 */

public class MemberDataEntity implements Serializable {
    public MemberDataEntity() {
    }
    /**
     * avatar : string
     * gameTeamId : 0
     * latitude : 0
     * longitude : 0
     * nickname : string
     * userId : 0
     */
    @PrimaryKey(column = "userId")
    private int userId;
    private String avatar;
    private int gameTeamId;
    private int latitude;
    private int longitude;
    private String nickname;
    private int score;

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public int getGameTeamId() {
        return gameTeamId;
    }

    public void setGameTeamId(int gameTeamId) {
        this.gameTeamId = gameTeamId;
    }

    public int getLatitude() {
        return latitude;
    }

    public void setLatitude(int latitude) {
        this.latitude = latitude;
    }

    public int getLongitude() {
        return longitude;
    }

    public void setLongitude(int longitude) {
        this.longitude = longitude;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }
}

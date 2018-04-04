package com.cn.bent.sports.view.activity.youle.bean;

import java.io.Serializable;

/**
 * Created by Administrator on 2018/3/31/031.
 */

public class JoinTeam implements Serializable {


    /**
     * avatar : string
     * gameTeamId : 0
     * latitude : 0
     * longitude : 0
     * nickname : string
     * userId : 0
     */

    private String avatar;
    private int gameTeamId;
    private double latitude;
    private double longitude;
    private String nickname;
    private int userId;

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

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public void setLatitude(int latitude) {
        this.latitude = latitude;
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

    public JoinTeam(String avatar, int gameTeamId, double latitude, double longitude, String nickname, int userId) {
        this.avatar = avatar;
        this.gameTeamId = gameTeamId;
        this.latitude = latitude;
        this.longitude = longitude;
        this.nickname = nickname;
        this.userId = userId;
    }
}

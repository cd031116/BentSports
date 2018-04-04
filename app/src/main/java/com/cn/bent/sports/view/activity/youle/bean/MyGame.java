package com.cn.bent.sports.view.activity.youle.bean;

import java.io.Serializable;

/**
 * Created by Administrator on 2018/4/3/003.
 */

public class MyGame implements Serializable{


    /**
     * gameTeamId : 7
     * gameId : 1
     * state : 5
     * startTime : 2018-03-30T11:34:55
     * expires : 86400
     * cover : http://pic29.photophoto.cn/20131204/0034034499213463_b.jpg
     * title : string
     * type : 1
     * leaderId : 16
     */

    private int gameTeamId;
    private int gameId;
    private int state;
    private String startTime;
    private int expires;
    private String cover;
    private String title;
    private String type;
    private int leaderId;

    public int getGameTeamId() {
        return gameTeamId;
    }

    public void setGameTeamId(int gameTeamId) {
        this.gameTeamId = gameTeamId;
    }

    public int getGameId() {
        return gameId;
    }

    public void setGameId(int gameId) {
        this.gameId = gameId;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public int getExpires() {
        return expires;
    }

    public void setExpires(int expires) {
        this.expires = expires;
    }

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getLeaderId() {
        return leaderId;
    }

    public void setLeaderId(int leaderId) {
        this.leaderId = leaderId;
    }
}

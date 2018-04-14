package com.cn.bent.sports.view.activity.youle.bean;

import java.io.Serializable;

/**
 * Created by Administrator on 2018/4/14/014.
 */

public class TeamDetail implements Serializable {


    /**
     * gamePointId : 0
     * gameTeamId : 0
     * id : 0
     * score : 0
     * state : 0
     * userId : 0
     */

    private int gamePointId;
    private int gameTeamId;
    private int id;
    private int score;
    private int state;
    private int userId;

    public int getGamePointId() {
        return gamePointId;
    }

    public void setGamePointId(int gamePointId) {
        this.gamePointId = gamePointId;
    }

    public int getGameTeamId() {
        return gameTeamId;
    }

    public void setGameTeamId(int gameTeamId) {
        this.gameTeamId = gameTeamId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }
}

package com.cn.bent.sports.bean;

import java.io.Serializable;

/**
 * Created by Administrator on 2018/3/30/030.
 */

public class TeamGame implements Serializable {


    /**
     * id : 7
     * gameId : 1
     * gameLineId : 1
     * leaderId : 16
     * score : 0
     * timing : 0
     * teamName : string的队伍
     * avatar : string
     * teamMemberMax : 0
     * teamMemberReal : 1
     * state : 1
     * startTime : 2018-03-30T10:29:34.71
     */

    private int id;
    private int gameId;
    private int gameLineId;
    private int leaderId;
    private int score;
    private int timing;
    private String teamName;
    private String avatar;
    private int teamMemberMax;
    private int teamMemberReal;
    private int state;
    private String startTime;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getGameId() {
        return gameId;
    }

    public void setGameId(int gameId) {
        this.gameId = gameId;
    }

    public int getGameLineId() {
        return gameLineId;
    }

    public void setGameLineId(int gameLineId) {
        this.gameLineId = gameLineId;
    }

    public int getLeaderId() {
        return leaderId;
    }

    public void setLeaderId(int leaderId) {
        this.leaderId = leaderId;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public int getTiming() {
        return timing;
    }

    public void setTiming(int timing) {
        this.timing = timing;
    }

    public String getTeamName() {
        return teamName;
    }

    public void setTeamName(String teamName) {
        this.teamName = teamName;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public int getTeamMemberMax() {
        return teamMemberMax;
    }

    public void setTeamMemberMax(int teamMemberMax) {
        this.teamMemberMax = teamMemberMax;
    }

    public int getTeamMemberReal() {
        return teamMemberReal;
    }

    public void setTeamMemberReal(int teamMemberReal) {
        this.teamMemberReal = teamMemberReal;
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
}

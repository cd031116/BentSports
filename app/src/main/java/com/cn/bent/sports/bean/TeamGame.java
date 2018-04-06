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
     * passRate:50
     */

    private int id;//组队id
    private int gameId;//游戏id
    private int gameLineId;//游戏线路id ,
    private int leaderId;//队长id
    private int score;//最终得分 ,
    private int timing;//耗时
    private String teamName;
    private String avatar;
    private int teamMemberMax;
    private int teamMemberReal;//实际队员数量 ,
    private int state;
    private int passRate;//通过率
    private String startTime;
    private int gameType;

    public int getGameType() {
        return gameType;
    }

    public void setGameType(int gameType) {
        this.gameType = gameType;
    }

    public int getPassRate() {
        return passRate;
    }

    public void setPassRate(int passRate) {
        this.passRate = passRate;
    }

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

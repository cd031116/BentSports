package com.cn.bent.sports.bean;

import com.google.gson.annotations.SerializedName;

import org.aisen.android.component.orm.annotation.PrimaryKey;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2018/3/30/030.
 */

public class GamePotins implements Serializable {


    /**
     * alias : string
     * hasQuestion : false
     * hasTask : false
     * id : 0
     * latitude : 0
     * longitude : 0
     * major : 0
     * orderNo : 0
     * score : 0
     * state : 0
     */
    @PrimaryKey(column = "id")
    private int id;//游戏点ID
    private String alias;//别名
    private boolean hasQuestion;//是否有线上T题目
    private boolean hasTask;//是否有线下游戏
    private int major;
    private int orderNo;
    private int score;
    private int state;
    /**
     * longitude : 113.088702
     * latitude : 28.013602
     * teamTaskDetails : [{"userId":1,"score":1},{"userId":17,"score":1},{"userId":2,"score":1},{"userId":4,"score":1}]
     */

    private double longitude;
    private double latitude;
    private List<TeamTaskDetailsBean> teamTaskDetails;

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public boolean isHasQuestion() {
        return hasQuestion;
    }

    public void setHasQuestion(boolean hasQuestion) {
        this.hasQuestion = hasQuestion;
    }

    public boolean isHasTask() {
        return hasTask;
    }

    public void setHasTask(boolean hasTask) {
        this.hasTask = hasTask;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }


    public void setLatitude(int latitude) {
        this.latitude = latitude;
    }


    public void setLongitude(int longitude) {
        this.longitude = longitude;
    }

    public int getMajor() {
        return major;
    }

    public void setMajor(int major) {
        this.major = major;
    }

    public int getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(int orderNo) {
        this.orderNo = orderNo;
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

    public List<TeamTaskDetailsBean> getTeamTaskDetails() {
        return teamTaskDetails;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public void setTeamTaskDetails(List<TeamTaskDetailsBean> teamTaskDetails) {
        this.teamTaskDetails = teamTaskDetails;
    }

    public static class TeamTaskDetailsBean implements Serializable{
        /**
         * userId : 1
         * score : 1
         */

        private int userId;
        private int score;

        public int getUserId() {
            return userId;
        }

        public void setUserId(int userId) {
            this.userId = userId;
        }


        public int getScore() {
            return score;
        }

        public void setScore(int score) {
            this.score = score;
        }
    }
}

package com.cn.bent.sports.bean;

import java.io.Serializable;

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

    private String alias;
    private boolean hasQuestion;//是否有线上T题目
    private boolean hasTask;//是否有线下游戏
    private int id;
    private int latitude;
    private int longitude;
    private int major;
    private int orderNo;
    private int score;
    private int state;

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
}

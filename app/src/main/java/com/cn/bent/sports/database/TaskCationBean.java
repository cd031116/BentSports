package com.cn.bent.sports.database;

import org.aisen.android.component.orm.annotation.PrimaryKey;

import java.io.Serializable;

/**
 * Created by lyj on 2018/1/30 0030.
 * description
 */

public class TaskCationBean  implements Serializable {

    @PrimaryKey(column = "tId")
    private  int tId;
    private String user_id;
    private String times;
    private String longitude;
    private String latitude;
    private  String name;
    private boolean isshow;
    private boolean isCheck;

    public boolean isCheck() {
        return isCheck;
    }

    public void setCheck(boolean check) {
        isCheck = check;
    }

    public boolean isshow() {
        return isshow;
    }

    public void setIsshow(boolean isshow) {
        this.isshow = isshow;
    }

    public int gettId() {
        return tId;
    }

    public void settId(int tId) {
        this.tId = tId;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getTimes() {
        return times;
    }

    public void setTimes(String times) {
        this.times = times;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public TaskCationBean(int tId, String user_id, String times, String longitude, String latitude, String name, boolean isshow, boolean isCheck) {
        this.tId = tId;
        this.user_id = user_id;
        this.times = times;
        this.longitude = longitude;
        this.latitude = latitude;
        this.name = name;
        this.isshow = isshow;
        this.isCheck=isCheck;

    }
}

package com.cn.bent.sports;

import java.io.Serializable;

/**
 * Created by lyj on 2018/1/29 0029.
 * description
 */

public class LoctionBean implements Serializable {
    private String longitude;
    private String latitude;
    private  String name;
    private  String tId;

    public String gettId() {
        return tId;
    }

    public void settId(String tId) {
        this.tId = tId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public LoctionBean(String longitude, String latitude, String name, String tId) {
        this.longitude = longitude;
        this.latitude = latitude;
        this.name = name;
        this.tId = tId;
    }
}

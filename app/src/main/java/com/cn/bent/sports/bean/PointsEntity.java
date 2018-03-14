package com.cn.bent.sports.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by dawn on 2018/3/8.
 */

public class PointsEntity implements Serializable{

    /**
     * pointId : 1
     * location : {"longitude":113.088702,"latitude":28.013602}
     * iBeacons : [{"major":10001,"minor":2}]
     * type : 1
     * mp3 : http://wxxcx.zhonghuilv.net/upload/outdoor/scenic_dot/1.mp3
     * name : 龙舟基地
     * iconUrl : http://wxxcx.zhonghuilv.net/xxx.png
     * pointImg : http://wxxcx.zhonghuilv.net/xxx.png
     */

    private String pointId;
    private LocationBean location;
    private int type;
    private String mp3;
    private String name;
    private String iconUrl;
    private String pointImg;
    private List<IBeaconsBean> iBeacons;
    private boolean isShow;

    public boolean isShow() {
        return isShow;
    }

    public void setShow(boolean show) {
        isShow = show;
    }

    public String getPointId() {
        return pointId;
    }

    public void setPointId(String pointId) {
        this.pointId = pointId;
    }

    public LocationBean getLocation() {
        return location;
    }

    public void setLocation(LocationBean location) {
        this.location = location;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getMp3() {
        return mp3;
    }

    public void setMp3(String mp3) {
        this.mp3 = mp3;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIconUrl() {
        return iconUrl;
    }

    public void setIconUrl(String iconUrl) {
        this.iconUrl = iconUrl;
    }

    public String getPointImg() {
        return pointImg;
    }

    public void setPointImg(String pointImg) {
        this.pointImg = pointImg;
    }

    public List<IBeaconsBean> getIBeacons() {
        return iBeacons;
    }

    public void setIBeacons(List<IBeaconsBean> iBeacons) {
        this.iBeacons = iBeacons;
    }

    public static class LocationBean implements Serializable{
        /**
         * longitude : 113.088702
         * latitude : 28.013602
         */

        private double longitude;
        private double latitude;

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
    }

    public static class IBeaconsBean {
        /**
         * major : 10001
         * minor : 2
         */

        private int major;
        private int minor;

        public int getMajor() {
            return major;
        }

        public void setMajor(int major) {
            this.major = major;
        }

        public int getMinor() {
            return minor;
        }

        public void setMinor(int minor) {
            this.minor = minor;
        }
    }
}

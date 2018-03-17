package com.cn.bent.sports.bean;

import org.aisen.android.component.orm.annotation.PrimaryKey;

import java.io.Serializable;
import java.util.List;

/**
 * Created by dawn on 2018/3/8.
 */

public class PointsEntity implements Serializable{


    /**
     * pointId : 44
     * location : {"longitude":113.088348,"latitude":28.006678}
     * iBeacons : [{"major":10003,"minor":1}]
     * type : 2
     * mp3 : http://222.240.60.23:8091/Upload/video/11hepinggeguangchang.mp3
     * name : 和平鸽广场
     * iconUrl :
     * pointImg : http://222.240.60.23:8091/Upload/img/HPGGC.jpg
     */
    @PrimaryKey(column = "pointId")
    private int pointId;
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

    public int getPointId() {
        return pointId;
    }

    public void setPointId(int pointId) {
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
         * longitude : 113.088348
         * latitude : 28.006678
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

    public static class IBeaconsBean implements Serializable{
        /**
         * major : 10003
         * minor : 1
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

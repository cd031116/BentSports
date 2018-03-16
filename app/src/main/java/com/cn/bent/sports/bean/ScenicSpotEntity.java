package com.cn.bent.sports.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by dawn on 2018/3/16.
 */

public class ScenicSpotEntity implements Serializable{

    /**
     * scenicSpotName : 石燕湖生态公园
     * points : [{"pointId":10,"location":{"longitude":113.085237,"latitude":28.013309},"iBeacons":[],"type":3,"mp3":"","name":"游客中心旁","iconUrl":"","pointImg":""},{"pointId":62,"location":{"longitude":113.086761,"latitude":28.013132},"iBeacons":[{"major":10044,"minor":1}],"type":2,"mp3":"http://222.240.60.23:8091/Upload/video/2guandiguquan.mp3","name":"关帝古泉","iconUrl":"","pointImg":"http://222.240.60.23:8091/Upload/img/GDGQ.jpg"}]
     * centerPoint : {"longitude":113.088369,"latitude":28.010505}
     */

    private String scenicSpotName;
    private CenterPointBean centerPoint;
    private List<PointsEntity> points;

    public String getScenicSpotName() {
        return scenicSpotName;
    }

    public void setScenicSpotName(String scenicSpotName) {
        this.scenicSpotName = scenicSpotName;
    }

    public CenterPointBean getCenterPoint() {
        return centerPoint;
    }

    public void setCenterPoint(CenterPointBean centerPoint) {
        this.centerPoint = centerPoint;
    }

    public List<PointsEntity> getPoints() {
        return points;
    }

    public void setPoints(List<PointsEntity> points) {
        this.points = points;
    }

    public static class CenterPointBean {
        /**
         * longitude : 113.088369
         * latitude : 28.010505
         */

        private String longitude;
        private String latitude;

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
    }
}

package com.cn.bent.sports.bean;

import java.io.Serializable;

/**
 * Created by dawn on 2018/3/30.
 */

public class PointsDetailEntity implements Serializable{

    /**
     * id : 4
     * scenicId : 1
     * major : 10017
     * pointName : 财神庙
     * longitude : 113.089641
     * latitude : 28.010695
     * type : 2
     * state : true
     * mp3 : /Upload/video/caishengmiao.mp3
     * imagesUrl : /Upload/img/CSM.jpg
     * pointImg : /Upload/img/syh.jpg
     * details : 进入石燕湖公园大门后，再沿着水泥路走一小段路，便能来到财神庙。这财神庙静静地掩盖在路旁的树林之中，是一座已有上百年历史的庙宇了，供奉着的是家喻户晓的道教五路财神——赵公明。说到财神庙，您知道吗？每逢农历正月初五到七月二十二，全世界的人口中约有四分之一的人都要祭拜财神爷，可以说是这信仰的人口数量是非常宏大的，而这拥有了上百年历史的财神庙，可以想象，那是承载了多少人的信念与追求呀。正如您眼前所看到的，财神庙前有长长的石梯，整座庙宇厚墙薄瓦，显示出的是一种庄严与肃穆的神圣感。而又因为坐落在石燕湖景区内，所以它除了是人们祈福烧香的许愿之地，还是纵览石燕湖景色的绝佳位置。您若在天朗气清的日子，站在财神庙前的平台上，您会看到群山环绕四周，放眼过去那便是碧波的湖水，十分美丽，从而让您对您的石燕湖之旅有了一个绝好的第一印象。
     */

    private int id;
    private int scenicId;
    private int major;
    private String pointName;
    private double longitude;
    private double latitude;
    private int type;
    private boolean state;
    private String mp3;
    private String imagesUrl;
    private String pointImg;
    private String details;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getScenicId() {
        return scenicId;
    }

    public void setScenicId(int scenicId) {
        this.scenicId = scenicId;
    }

    public int getMajor() {
        return major;
    }

    public void setMajor(int major) {
        this.major = major;
    }

    public String getPointName() {
        return pointName;
    }

    public void setPointName(String pointName) {
        this.pointName = pointName;
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

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public boolean isState() {
        return state;
    }

    public void setState(boolean state) {
        this.state = state;
    }

    public String getMp3() {
        return mp3;
    }

    public void setMp3(String mp3) {
        this.mp3 = mp3;
    }

    public String getImagesUrl() {
        return imagesUrl;
    }

    public void setImagesUrl(String imagesUrl) {
        this.imagesUrl = imagesUrl;
    }

    public String getPointImg() {
        return pointImg;
    }

    public void setPointImg(String pointImg) {
        this.pointImg = pointImg;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }
}

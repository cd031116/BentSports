package com.cn.bent.sports.bean;

import java.io.Serializable;

/**
 * Created by dawn on 2018/3/21.
 */

public class LinesPointsEntity implements Serializable{


    /**
     * id : 1
     * state : 1
     * scenicId : 1
     * lineName : 石燕湖线路1
     * pointCount : 8
     * points : [[28.013768,113.085929],[28.013233,113.086755],[28.013086,113.087098],[28.012897,113.087457],[28.012821,113.087747],[28.012674,113.088166],[28.012722,113.088536],[28.012674,113.088686],[28.012452,113.088852],[28.012253,113.089131],[28.011912,113.089442],[28.011632,113.089453],[28.0115,113.089694],[28.010931,113.089845],[28.010704,113.089737],[28.010013,113.0897],[28.009662,113.089732],[28.009596,113.089668],[28.009477,113.089716],[28.009387,113.089855],[28.009241,113.089887],[28.008975,113.08985],[28.008715,113.089979],[28.00863,113.089963],[28.008625,113.08971],[28.008464,113.089592],[28.008118,113.089651],[28.00799,113.089609],[28.007768,113.089566],[28.00772,113.089453],[28.00773,113.089458],[28.007394,113.089297],[28.007157,113.088949],[28.007062,113.088949],[28.006716,113.088498],[28.006243,113.087983],[28.005243,113.086873],[28.005551,113.086529],[28.00512,113.085666],[28.004732,113.085178],[28.004509,113.084593],[28.004557,113.084346],[28.005144,113.083332],[28.005461,113.083021],[28.005598,113.082624],[28.005674,113.0819]]
     */

    private int id;
    private int state;
    private int scenicId;
    private String lineName;
    private int pointCount;
    private String points;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public int getScenicId() {
        return scenicId;
    }

    public void setScenicId(int scenicId) {
        this.scenicId = scenicId;
    }

    public String getLineName() {
        return lineName;
    }

    public void setLineName(String lineName) {
        this.lineName = lineName;
    }

    public int getPointCount() {
        return pointCount;
    }

    public void setPointCount(int pointCount) {
        this.pointCount = pointCount;
    }

    public String getPoints() {
        return points;
    }

    public void setPoints(String points) {
        this.points = points;
    }
}

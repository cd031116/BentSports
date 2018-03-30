package com.cn.bent.sports.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by dawn on 2018/3/30.
 */

public class LinesDetailEntity implements Serializable {

    private List<List<Double>> points;
    private List<ScenicPointsEntity.PointsBean> voicePoints;

    public List<List<Double>> getPoints() {
        return points;
    }

    public void setPoints(List<List<Double>> points) {
        this.points = points;
    }

    public List<ScenicPointsEntity.PointsBean> getVoicePoints() {
        return voicePoints;
    }

    public void setVoicePoints(List<ScenicPointsEntity.PointsBean> voicePoints) {
        this.voicePoints = voicePoints;
    }
}

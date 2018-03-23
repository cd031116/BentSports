package com.cn.bent.sports.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by dawn on 2018/3/23.
 */

public class LinesPointsDetailEntity implements Serializable{


    private List<List<Double>> points;
    private List<?> voicePoints;

    public List<List<Double>> getPoints() {
        return points;
    }

    public void setPoints(List<List<Double>> points) {
        this.points = points;
    }

    public List<?> getVoicePoints() {
        return voicePoints;
    }

    public void setVoicePoints(List<?> voicePoints) {
        this.voicePoints = voicePoints;
    }
}

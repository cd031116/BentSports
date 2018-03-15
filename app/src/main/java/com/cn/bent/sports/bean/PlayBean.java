package com.cn.bent.sports.bean;

import java.io.Serializable;

/**
 * Created by lyj on 2018/3/8 0008.
 * description
 */

public class PlayBean implements Serializable {
    private int curentPosition;

    private int totalPosition;


    public int getCurentPosition() {
        return curentPosition;
    }

    public void setCurentPosition(int curentPosition) {
        this.curentPosition = curentPosition;
    }

    public int getTotalPosition() {
        return totalPosition;
    }

    public void setTotalPosition(int totalPosition) {
        this.totalPosition = totalPosition;
    }
}

package com.cn.bent.sports.bean;

import java.io.Serializable;

/**
 * Created by dawn on 2018/1/31.
 */

public class AddScoreEntity implements Serializable{

    /**
     * addStatus : 1
     */

    private int addStatus;

    public int getAddStatus() {
        return addStatus;
    }

    public void setAddStatus(int addStatus) {
        this.addStatus = addStatus;
    }
}

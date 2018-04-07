package com.cn.bent.sports.view.activity.youle.bean;

import java.io.Serializable;

/**
 * Created by Administrator on 2018/4/7/007.
 */

public class ScanType implements Serializable{
    private String type;
    private String param;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getParam() {
        return param;
    }

    public void setParam(String param) {
        this.param = param;
    }

    public ScanType(String type, String param) {
        this.type = type;
        this.param = param;
    }
}

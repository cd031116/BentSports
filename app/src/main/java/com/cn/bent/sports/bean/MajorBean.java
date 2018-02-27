package com.cn.bent.sports.bean;

import java.io.Serializable;

/**
 * Created by lyj on 2018/2/27 0027.
 * description
 */

public class MajorBean implements Serializable {
    private String major;
    private String minor;

    public String getMajor() {
        return major;
    }

    public void setMajor(String major) {
        this.major = major;
    }

    public String getMinor() {
        return minor;
    }

    public void setMinor(String minor) {
        this.minor = minor;
    }
}

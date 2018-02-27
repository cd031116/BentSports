package com.cn.bent.sports.bean;

import java.io.Serializable;

/**
 * Created by lyj on 2018/2/26 0026.
 * description
 */

public class FenceInfo implements Serializable {
    private String fence_id;
    private String dot_long;
    private String other_dot_long;
    private String scenic_spot_id;
    private String dot_lat;
    private String other_dot_lat;

    public String getFence_id() {
        return fence_id;
    }

    public void setFence_id(String fence_id) {
        this.fence_id = fence_id;
    }

    public String getDot_long() {
        return dot_long;
    }

    public void setDot_long(String dot_long) {
        this.dot_long = dot_long;
    }

    public String getOther_dot_long() {
        return other_dot_long;
    }

    public void setOther_dot_long(String other_dot_long) {
        this.other_dot_long = other_dot_long;
    }

    public String getScenic_spot_id() {
        return scenic_spot_id;
    }

    public void setScenic_spot_id(String scenic_spot_id) {
        this.scenic_spot_id = scenic_spot_id;
    }

    public String getDot_lat() {
        return dot_lat;
    }

    public void setDot_lat(String dot_lat) {
        this.dot_lat = dot_lat;
    }

    public String getOther_dot_lat() {
        return other_dot_lat;
    }

    public void setOther_dot_lat(String other_dot_lat) {
        this.other_dot_lat = other_dot_lat;
    }
}

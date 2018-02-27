package com.cn.bent.sports.bean;

import android.widget.LinearLayout;

import java.io.Serializable;
import java.util.List;

/**
 * Created by lyj on 2018/2/26 0026.
 * description
 */

public class PlayMapBean implements Serializable {

    private List<MapDot> place_list;

    public List<MapDot> getPlace_list() {
        return place_list;
    }

    public void setPlace_list(List<MapDot> place_list) {
        this.place_list = place_list;
    }
}

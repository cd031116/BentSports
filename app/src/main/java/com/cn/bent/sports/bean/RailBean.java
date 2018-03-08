package com.cn.bent.sports.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by lyj on 2018/2/26 0026.
 * description
 */

public class RailBean implements Serializable {
    private FenceInfo fence;
    private List<MapDot> mp3_tag;

    public FenceInfo getFence() {
        return fence;
    }

    public void setFence(FenceInfo fence) {
        this.fence = fence;
    }

    public List<MapDot> getMp3_tag() {
        return mp3_tag;
    }

    public void setMp3_tag(List<MapDot> mp3_tag) {
        this.mp3_tag = mp3_tag;
    }
}

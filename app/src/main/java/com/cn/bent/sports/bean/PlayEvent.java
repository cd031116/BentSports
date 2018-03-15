package com.cn.bent.sports.bean;

/**
 * Created by lyj on 2018/3/7 0007.
 * description
 */

public class PlayEvent {
private String paths;
private boolean isHuan;
    public boolean isHuan() {
        return isHuan;
    }

    public String getPaths() {
        return paths;
    }

    public  PlayEvent(String paths,boolean huan) {
        this.paths = paths;
        this.isHuan = huan;
    }
}

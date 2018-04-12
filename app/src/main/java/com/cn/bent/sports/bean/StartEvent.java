package com.cn.bent.sports.bean;

/**
 * Created by lyj on 2018/3/8 0008.
 * description
 */

public class StartEvent {

    private boolean isStart;
    private String paths;
    public boolean isStart() {
        return isStart;
    }

    public String getPaths() {
        return paths;
    }

    public  StartEvent(boolean isStart,String paths) {
        this.isStart = isStart;
        this.paths=paths;
    }

}

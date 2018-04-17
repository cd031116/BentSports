package com.cn.bent.sports.view.activity.youle.bean;

import java.io.Serializable;

/**
 * Created by Administrator on 2018/4/7/007.
 */

public class ScanType implements Serializable{
    private String type;
    private String param;
    private int gameId;

    public int getGameId() {
        return gameId;
    }

    public void setGameId(int gameId) {
        this.gameId = gameId;
    }

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

    public ScanType(String type, String param, int gameId) {
        this.type = type;
        this.param = param;
        this.gameId = gameId;
    }

}

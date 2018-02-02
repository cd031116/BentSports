package com.cn.bent.sports.bean;

import java.io.Serializable;

/**
 * Created by dawn on 2018/1/31.
 */

public class GameEntity implements Serializable {

    /**
     * uid : 1
     * scord : 6
     * gameid : 2
     */

    private String uid;
    private int scord;
    private int gameid;

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public int getScord() {
        return scord;
    }

    public void setScord(int scord) {
        this.scord = scord;
    }

    public int getGameid() {
        return gameid;
    }

    public void setGameid(int gameid) {
        this.gameid = gameid;
    }
}

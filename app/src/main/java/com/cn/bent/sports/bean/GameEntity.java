package com.cn.bent.sports.bean;

import java.io.Serializable;

/**
 * Created by dawn on 2018/1/31.
 */

public class GameEntity implements Serializable {

    /**
     * uid : 1
     * scord : 6
     * gameId : 2
     */

    private String uid;
    private int scord;
    private int gameId;

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

    public int getGameId() {
        return gameId;
    }

    public void setGameId(int gameId) {
        this.gameId = gameId;
    }
}

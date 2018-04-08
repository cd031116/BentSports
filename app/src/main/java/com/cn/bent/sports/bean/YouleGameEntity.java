package com.cn.bent.sports.bean;

import java.io.Serializable;

/**
 * Created by dawn on 2018/4/8.
 */

public class YouleGameEntity implements Serializable{

    /**
     * teamId : 222
     * scord : 43
     * gamePointId : 4
     */

    private String teamId;
    private int scord;
    private int type;
    private String gamePointId;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getTeamId() {
        return teamId;
    }

    public void setTeamId(String teamId) {
        this.teamId = teamId;
    }

    public int getScord() {
        return scord;
    }

    public void setScord(int scord) {
        this.scord = scord;
    }

    public String getGamePointId() {
        return gamePointId;
    }

    public void setGamePointId(String gamePointId) {
        this.gamePointId = gamePointId;
    }
}

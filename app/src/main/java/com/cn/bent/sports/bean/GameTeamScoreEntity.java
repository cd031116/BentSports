package com.cn.bent.sports.bean;

import java.io.Serializable;

/**
 * Created by dawn on 2018/4/2.
 * 获取团队积分实体类
 */

public class GameTeamScoreEntity implements Serializable {

    /**
     * score : 0
     * userId : 0
     */

    private int score;
    private int userId;

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }
}

package com.cn.bent.sports.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by dawn on 2018/2/2.
 */

public class AllFinishEntity implements Serializable{


    /**
     * game_record : [{"score":20,"name":"猜灯谜"},{"score":20,"name":"红包雨"},{"score":20,"name":"灯笼"},{"score":20,"name":"拯救小拓"},{"score":20,"name":"财神庙"},{"score":20,"name":"熊出没"}]
     * total_score : 120
     */

    private int total_score;
    private List<GameRecordBean> game_record;

    public int getTotal_score() {
        return total_score;
    }

    public void setTotal_score(int total_score) {
        this.total_score = total_score;
    }

    public List<GameRecordBean> getGame_record() {
        return game_record;
    }

    public void setGame_record(List<GameRecordBean> game_record) {
        this.game_record = game_record;
    }

    public static class GameRecordBean implements Serializable{
        /**
         * score : 20
         * name : 猜灯谜
         */

        private int score;
        private String name;

        public int getScore() {
            return score;
        }

        public void setScore(int score) {
            this.score = score;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }
}

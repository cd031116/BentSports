package com.cn.bent.sports.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by dawn on 2018/1/31.
 */

public class UserMsgEntity implements Serializable {

    /**
     * userMsg : {"id":4,"nickname":"爸爸","headimg":"https://ss1.bdstatic.com/70cFuXSh_Q1YnxGkpoWK1HF6hhy/it/u=12867320,655225767&fm=27&gp=0.jpg","score":427}
     * card_num : [{"game_id":1,"count":1,"status":1},{"game_id":2,"count":1,"status":1},{"game_id":3,"count":1,"status":1},{"game_id":4,"count":1,"status":1},{"game_id":5,"count":1,"status":1},{"game_id":6,"count":0,"status":0}]
     */

    private UserMsgBean userMsg;
    private List<CardNumBean> card_num;

    public UserMsgBean getUserMsg() {
        return userMsg;
    }

    public void setUserMsg(UserMsgBean userMsg) {
        this.userMsg = userMsg;
    }

    public List<CardNumBean> getCard_num() {
        return card_num;
    }

    public void setCard_num(List<CardNumBean> card_num) {
        this.card_num = card_num;
    }

    public static class UserMsgBean {
        /**
         * id : 4
         * nickname : 爸爸
         * headimg : https://ss1.bdstatic.com/70cFuXSh_Q1YnxGkpoWK1HF6hhy/it/u=12867320,655225767&fm=27&gp=0.jpg
         * score : 427
         */

        private int id;
        private String nickname;
        private String headimg;
        private int score;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getNickname() {
            return nickname;
        }

        public void setNickname(String nickname) {
            this.nickname = nickname;
        }

        public String getHeadimg() {
            return headimg;
        }

        public void setHeadimg(String headimg) {
            this.headimg = headimg;
        }

        public int getScore() {
            return score;
        }

        public void setScore(int score) {
            this.score = score;
        }
    }

    public static class CardNumBean {
        /**
         * game_id : 1
         * count : 1
         * status : 1
         */

        private int game_id;
        private int count;
        private int status;

        public int getGame_id() {
            return game_id;
        }

        public void setGame_id(int game_id) {
            this.game_id = game_id;
        }

        public int getCount() {
            return count;
        }

        public void setCount(int count) {
            this.count = count;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }
    }
}

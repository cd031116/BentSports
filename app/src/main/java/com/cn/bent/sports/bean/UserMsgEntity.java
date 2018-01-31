package com.cn.bent.sports.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by dawn on 2018/1/31.
 */

public class UserMsgEntity implements Serializable {

    /**
     * userMsg : {"user_id":4,"nickname":"zhla183729c912","headimg":"https://ss1.bdstatic.com/70cFuXSh_Q1YnxGkpoWK1HF6hhy/it/u=12867320,655225767&fm=27&gp=0.jpg","score":93,"card_num":[{"game_id":1,"count":1}]}
     */

    private UserMsgBean userMsg;

    public UserMsgBean getUserMsg() {
        return userMsg;
    }

    public void setUserMsg(UserMsgBean userMsg) {
        this.userMsg = userMsg;
    }

    public static class UserMsgBean {
        /**
         * user_id : 4
         * nickname : zhla183729c912
         * headimg : https://ss1.bdstatic.com/70cFuXSh_Q1YnxGkpoWK1HF6hhy/it/u=12867320,655225767&fm=27&gp=0.jpg
         * score : 93
         * card_num : [{"game_id":1,"count":1}]
         */

        private int user_id;
        private String nickname;
        private String headimg;
        private int score;
        private List<CardNumBean> card_num;

        public int getUser_id() {
            return user_id;
        }

        public void setUser_id(int user_id) {
            this.user_id = user_id;
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

        public List<CardNumBean> getCard_num() {
            return card_num;
        }

        public void setCard_num(List<CardNumBean> card_num) {
            this.card_num = card_num;
        }

        public static class CardNumBean {
            /**
             * game_id : 1
             * count : 1
             */

            private int game_id;
            private int count;

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
        }
    }
}

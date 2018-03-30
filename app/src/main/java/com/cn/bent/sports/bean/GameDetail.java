package com.cn.bent.sports.bean;

import java.io.Serializable;

/**
*aunthor lyj
* create 2018/3/30/030 10:59  游戏详情DTO
**/

public class GameDetail implements Serializable {


    /**
     * id : 1
     * gameLineId : 1
     * scenicId : 1
     * title : string
     * address : string
     * cover : string
     * price : 0
     * type : 1
     * lineName : string
     * pointCount : 0
     * passRate : 30
     * maxPeople : 0
     * orderNo : 1
     * gameDetail : {"id":1,"gameId":1,"detail":"string","tip":"string","notice":"string"}
     * state : 1
     */

    private int id;
    private int gameLineId;
    private int scenicId;
    private String title;
    private String address;
    private String cover;
    private int price;
    private int type;
    private String lineName;
    private int pointCount;
    private int passRate;
    private int maxPeople;
    private int orderNo;
    private GameDetailBean gameDetail;
    private int state;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getGameLineId() {
        return gameLineId;
    }

    public void setGameLineId(int gameLineId) {
        this.gameLineId = gameLineId;
    }

    public int getScenicId() {
        return scenicId;
    }

    public void setScenicId(int scenicId) {
        this.scenicId = scenicId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getLineName() {
        return lineName;
    }

    public void setLineName(String lineName) {
        this.lineName = lineName;
    }

    public int getPointCount() {
        return pointCount;
    }

    public void setPointCount(int pointCount) {
        this.pointCount = pointCount;
    }

    public int getPassRate() {
        return passRate;
    }

    public void setPassRate(int passRate) {
        this.passRate = passRate;
    }

    public int getMaxPeople() {
        return maxPeople;
    }

    public void setMaxPeople(int maxPeople) {
        this.maxPeople = maxPeople;
    }

    public int getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(int orderNo) {
        this.orderNo = orderNo;
    }

    public GameDetailBean getGameDetail() {
        return gameDetail;
    }

    public void setGameDetail(GameDetailBean gameDetail) {
        this.gameDetail = gameDetail;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public static class GameDetailBean implements Serializable{
        /**
         * id : 1
         * gameId : 1
         * detail : string
         * tip : string
         * notice : string
         */

        private int id;
        private int gameId;
        private String detail;
        private String tip;
        private String notice;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getGameId() {
            return gameId;
        }

        public void setGameId(int gameId) {
            this.gameId = gameId;
        }

        public String getDetail() {
            return detail;
        }

        public void setDetail(String detail) {
            this.detail = detail;
        }

        public String getTip() {
            return tip;
        }

        public void setTip(String tip) {
            this.tip = tip;
        }

        public String getNotice() {
            return notice;
        }

        public void setNotice(String notice) {
            this.notice = notice;
        }
    }
}

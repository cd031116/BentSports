package com.cn.bent.sports.bean;

import java.io.Serializable;

/**
 * Created by Administrator on 2018/3/30/030.
 */

public class GamePotins implements Serializable {


    /**
     * alias : string
     * description : string
     * expires : 0
     * gameLineId : 0
     * hasQuestion : false
     * hasTask : false
     * id : 0
     * major : 0
     * offlineTask : {"description":"string","fileUrl":"string","gamePointId":0,"id":0,"score":0,"taskType":0}
     * orderNo : 0
     * pointId : 0
     * state : false
     * tipImage : string
     */

    private String alias;
    private String description;
    private int expires;
    private int gameLineId;
    private boolean hasQuestion;
    private boolean hasTask;
    private int id;
    private int major;
    private OfflineTaskBean offlineTask;
    private int orderNo;
    private int pointId;
    private boolean state;
    private String tipImage;

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getExpires() {
        return expires;
    }

    public void setExpires(int expires) {
        this.expires = expires;
    }

    public int getGameLineId() {
        return gameLineId;
    }

    public void setGameLineId(int gameLineId) {
        this.gameLineId = gameLineId;
    }

    public boolean isHasQuestion() {
        return hasQuestion;
    }

    public void setHasQuestion(boolean hasQuestion) {
        this.hasQuestion = hasQuestion;
    }

    public boolean isHasTask() {
        return hasTask;
    }

    public void setHasTask(boolean hasTask) {
        this.hasTask = hasTask;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getMajor() {
        return major;
    }

    public void setMajor(int major) {
        this.major = major;
    }

    public OfflineTaskBean getOfflineTask() {
        return offlineTask;
    }

    public void setOfflineTask(OfflineTaskBean offlineTask) {
        this.offlineTask = offlineTask;
    }

    public int getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(int orderNo) {
        this.orderNo = orderNo;
    }

    public int getPointId() {
        return pointId;
    }

    public void setPointId(int pointId) {
        this.pointId = pointId;
    }

    public boolean isState() {
        return state;
    }

    public void setState(boolean state) {
        this.state = state;
    }

    public String getTipImage() {
        return tipImage;
    }

    public void setTipImage(String tipImage) {
        this.tipImage = tipImage;
    }

    public static class OfflineTaskBean implements Serializable {
        /**
         * description : string
         * fileUrl : string
         * gamePointId : 0
         * id : 0
         * score : 0
         * taskType : 0
         */

        private String description;
        private String fileUrl;
        private int gamePointId;
        private int id;
        private int score;
        private int taskType;

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getFileUrl() {
            return fileUrl;
        }

        public void setFileUrl(String fileUrl) {
            this.fileUrl = fileUrl;
        }

        public int getGamePointId() {
            return gamePointId;
        }

        public void setGamePointId(int gamePointId) {
            this.gamePointId = gamePointId;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getScore() {
            return score;
        }

        public void setScore(int score) {
            this.score = score;
        }

        public int getTaskType() {
            return taskType;
        }

        public void setTaskType(int taskType) {
            this.taskType = taskType;
        }
    }
}

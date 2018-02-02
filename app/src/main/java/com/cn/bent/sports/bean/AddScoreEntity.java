package com.cn.bent.sports.bean;

import java.io.Serializable;

/**
 * Created by dawn on 2018/1/31.
 */

public class AddScoreEntity implements Serializable{


    /**
     * code : 1
     * msg : 操作成功
     * body : {"addStatus":1}
     */

    private int code;
    private String msg;
    private BodyBean body;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public BodyBean getBody() {
        return body;
    }

    public void setBody(BodyBean body) {
        this.body = body;
    }

    public static class BodyBean implements Serializable{
        /**
         * addStatus : 1
         */

        private int addStatus;

        public int getAddStatus() {
            return addStatus;
        }

        public void setAddStatus(int addStatus) {
            this.addStatus = addStatus;
        }
    }
}

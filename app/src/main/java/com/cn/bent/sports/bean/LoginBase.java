package com.cn.bent.sports.bean;



import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by lyj on 2018/1/30 0030.
 * description
 */

public class LoginBase implements Serializable {
    @SerializedName("id")
     private String member_id;
    private String mobile;
    private String nickname;
    private String headimg;
    private String add_time;
    private String score;

    public String getMember_id() {
        return member_id;
    }

    public void setMember_id(String member_id) {
        this.member_id = member_id;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
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

    public String getAdd_time() {
        return add_time;
    }

    public void setAdd_time(String add_time) {
        this.add_time = add_time;
    }

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }
}

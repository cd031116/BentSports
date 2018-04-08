package com.cn.bent.sports.view.activity.youle.bean;

import java.io.Serializable;

/**
 * Created by Administrator on 2018/3/31/031.
 */

public class UserInfo implements Serializable{


    /**
     * avatar : string
     * email : string
     * id : 0
     * lastLoginTime : 2018-03-31T03:10:20.134Z
     * nickname : string
     * password : string
     * phone : string
     * sex : 0
     * source : string
     * state : 0
     * userType : 0
     * username : string
     * wxOpenid : string
     */

    private String avatar;
    private String email;
    private int id;
    private String lastLoginTime;
    private String nickname;
    private String password;
    private String phone;
    private int sex;
    private String source;
    private int state;
    private int userType;
    private String username;
    private String wxOpenid;

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLastLoginTime() {
        return lastLoginTime;
    }

    public void setLastLoginTime(String lastLoginTime) {
        this.lastLoginTime = lastLoginTime;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public int getSex() {
        return sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public int getUserType() {
        return userType;
    }

    public void setUserType(int userType) {
        this.userType = userType;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getWxOpenid() {
        return wxOpenid;
    }

    public void setWxOpenid(String wxOpenid) {
        this.wxOpenid = wxOpenid;
    }

}

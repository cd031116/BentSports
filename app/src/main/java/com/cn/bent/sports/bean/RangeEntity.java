package com.cn.bent.sports.bean;

import java.io.Serializable;

/**
 * Created by dawn on 2018/1/30.
 */

public class RangeEntity implements Serializable {
    private String name;
    private String jifen;
    private String num;
    private String head_img;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getJifen() {
        return jifen;
    }

    public void setJifen(String jifen) {
        this.jifen = jifen;
    }

    public String getNum() {
        return num;
    }

    public void setNum(String num) {
        this.num = num;
    }

    public String getHead_img() {
        return head_img;
    }

    public void setHead_img(String head_img) {
        this.head_img = head_img;
    }
}

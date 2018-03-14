package com.cn.bent.sports.database;

import org.aisen.android.component.orm.annotation.PrimaryKey;

import java.io.Serializable;

/**
 * Created by lyj on 2018/3/14 0014.
 * description
 */

public class QueueBean implements Serializable{
    public QueueBean() {
    }
    @PrimaryKey(column = "mp3")
    private  String mp3;


    public String getMp3() {
        return mp3;
    }

    public void setMp3(String mp3) {
        this.mp3 = mp3;
    }

    public QueueBean(String mp3) {
        this.mp3 = mp3;
    }
}

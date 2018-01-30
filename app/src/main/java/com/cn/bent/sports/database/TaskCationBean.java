package com.cn.bent.sports.database;

import org.aisen.android.component.orm.annotation.PrimaryKey;

import java.io.Serializable;

/**
 * Created by lyj on 2018/1/30 0030.
 * description
 */

public class TaskCationBean  implements Serializable {

    @PrimaryKey(column = "tId")
    private  String tId;
    private String user_id;
    private String times;
    private String longitude;
    private String latitude;
    private  String name;






}

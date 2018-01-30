package com.cn.bent.sports.database;

import android.content.Context;

import org.aisen.android.component.orm.SqliteUtility;
import org.aisen.android.component.orm.SqliteUtilityBuilder;

/**
 * Created by lyj on 2018/1/30 0030.
 * description
 TaskCationDB */

public class TaskCationDB {
    static void setup(Context context) {
        new SqliteUtilityBuilder().configDBName("TaskCationDB").configVersion(1).build(context);
    }

    static SqliteUtility getDB() {
        return SqliteUtility.getInstance("TaskCationDB");
    }
}

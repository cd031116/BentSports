package com.cn.bent.sports.database;

import android.content.Context;

import org.aisen.android.component.orm.SqliteUtility;
import org.aisen.android.component.orm.SqliteUtilityBuilder;

/**
 * Created by lyj on 2018/4/4/004.//游戏点
 */

public class PlayPointDb{

    static void setup(Context context) {
        new SqliteUtilityBuilder().configDBName("PlayPointDb").configVersion(1).build(context);
    }

    static SqliteUtility getDB() {
        return SqliteUtility.getInstance("PlayPointDb");
    }

}

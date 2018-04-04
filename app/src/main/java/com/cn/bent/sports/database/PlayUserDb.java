package com.cn.bent.sports.database;

import android.content.Context;

import org.aisen.android.component.orm.SqliteUtility;
import org.aisen.android.component.orm.SqliteUtilityBuilder;

/**
 * Created by Administrator on 2018/4/4/004.
 */

public class PlayUserDb{
    static void setup(Context context) {
        new SqliteUtilityBuilder().configDBName("PlayUserDb").configVersion(1).build(context);
    }

    static SqliteUtility getDB() {
        return SqliteUtility.getInstance("PlayUserDb");
    }

}

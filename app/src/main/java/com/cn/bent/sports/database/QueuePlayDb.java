package com.cn.bent.sports.database;

import android.content.Context;

import org.aisen.android.component.orm.SqliteUtility;
import org.aisen.android.component.orm.SqliteUtilityBuilder;

/**
 * Created by lyj on 2018/3/14 0014.
 * description
 */

public class QueuePlayDb {
    static void setup(Context context) {
        new SqliteUtilityBuilder().configDBName("QueuePlayDb").configVersion(1).build(context);
    }

    static SqliteUtility getDB() {
        return SqliteUtility.getInstance("QueuePlayDb");
    }
}

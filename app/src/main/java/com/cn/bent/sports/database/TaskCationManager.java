package com.cn.bent.sports.database;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lyj on 2018/1/30 0030.
 * description
 */

public class TaskCationManager {
    private static final List<TaskCationBean> mTaskList = new ArrayList<>();
    public static void setup(Context context) {
        TaskCationDB.setup(context);

        mTaskList.clear();
        mTaskList.addAll(TaskCationDB.getDB().select(null, TaskCationBean.class));
    }


    public static List<TaskCationBean> getHistory() {
        return mTaskList;
    }

}

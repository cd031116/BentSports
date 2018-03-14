package com.cn.bent.sports.database;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lyj on 2018/3/14 0014.
 * description
 */

public class QueueManager {

    private static final List<QueueBean> mTaskList = new ArrayList<>();

    public static void setup(Context context) {
        QueuePlayDb.setup(context);
    }

    //插入集合数据
    public static void insert(List<TaskCationBean> list) {
        QueuePlayDb.getDB().insert(null, list);
    }

    //删除所有
    public static void clear() {
        mTaskList.clear();
        QueuePlayDb.getDB().deleteAll(null, QueueBean.class);
    }

    //更新数据
    public static void update(QueueBean list) {
        QueuePlayDb.getDB().deleteAll(null, QueueBean.class);
        QueuePlayDb.getDB().insert(null, list);
    }

    //更新数据
    public static String getMp3() {
        mTaskList.clear();
        mTaskList.addAll(QueuePlayDb.getDB().select(null, QueueBean.class));
        if(mTaskList.size()>0){
            return mTaskList.get(0).getMp3();
        }else {
            return"";
        }
    }

}

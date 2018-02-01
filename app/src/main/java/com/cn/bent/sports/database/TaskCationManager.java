package com.cn.bent.sports.database;

import android.content.Context;

import com.cn.bent.sports.utils.DataUtils;

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
    }

    //获得所有
    public static List<TaskCationBean> getHistory() {
        mTaskList.clear();
        mTaskList.addAll(TaskCationDB.getDB().select(null, TaskCationBean.class));
        for(TaskCationBean bean:mTaskList){
            if(DataUtils.getlongs()<Long.parseLong(bean.getTimes())){
                bean.setIsshow(true);
            }else {
                bean.setIsshow(false);
            }
        }
        return mTaskList;
    }

    public static int getSize(){
        List<TaskCationBean> mTaskLists=  TaskCationDB.getDB().select(null, TaskCationBean.class);
        return mTaskLists.size();
    }



    //插入集合数据
    public static void insert(List<TaskCationBean> list) {
       TaskCationDB.getDB().insert(null, list);
    }

    //删除所有
    public static void clear() {
        mTaskList.clear();
        TaskCationDB.getDB().deleteAll(null, TaskCationBean.class);
    }

    //更新数据
    public static void update(String index,long times) {
        String id_t=(Integer.parseInt(index)-1)+"";
        List<TaskCationBean> mList=TaskCationDB.getDB().select(null, TaskCationBean.class);
        for(TaskCationBean infos:mList){
            if (id_t.endsWith(String.valueOf(infos.gettId()))){
                infos.setTimes(times+"");
            }
        }
        TaskCationDB.getDB().deleteAll(null, TaskCationBean.class);
        TaskCationDB.getDB().insert(null, mList);
    }


}

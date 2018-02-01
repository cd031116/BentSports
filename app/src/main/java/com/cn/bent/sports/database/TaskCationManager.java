package com.cn.bent.sports.database;

import android.content.Context;
import android.util.Log;

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
            if(System.currentTimeMillis()<Long.parseLong(bean.getTimes())){
                bean.setIsshow(true);
                mTaskList.remove(bean);
            }else {
                bean.setIsshow(false);
            }
        }
        return mTaskList;
    }


    //获得所有
    public static boolean noMore() {
        boolean ismore=true;
        List<TaskCationBean> noMoreLists=  TaskCationDB.getDB().select(null, TaskCationBean.class);
        for(TaskCationBean bean:noMoreLists){
            if(System.currentTimeMillis()>Long.parseLong(bean.getTimes())) {
                ismore=false;
            }
        }
        return ismore;
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
                Log.i("test","times="+times);
            }
        }
        TaskCationDB.getDB().deleteAll(null, TaskCationBean.class);
        TaskCationDB.getDB().insert(null, mList);
    }


}

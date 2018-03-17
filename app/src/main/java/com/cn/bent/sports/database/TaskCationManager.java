package com.cn.bent.sports.database;

import android.content.Context;
import android.util.Log;

import com.cn.bent.sports.bean.PointsEntity;
import com.cn.bent.sports.utils.DataUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lyj on 2018/1/30 0030.
 * description
 */

public class TaskCationManager {
    private static final List<PointsEntity> mTaskList = new ArrayList<>();

    public static void setup(Context context) {
        TaskCationDB.setup(context);
    }

    //获得所有
    public static List<PointsEntity> getHistory() {
        mTaskList.addAll(TaskCationDB.getDB().select(null, PointsEntity.class));
        return mTaskList;
    }


    //获得当前播放实体类
    public static PointsEntity getIsNow() {
        boolean ismore = true;
        List<PointsEntity> noMoreLists = TaskCationDB.getDB().select(null, PointsEntity.class);
        for (PointsEntity bean : noMoreLists) {
            if (bean.isNow()) {
                return bean;
            }
        }
        return null;
    }


    public static int getSize() {
        List<PointsEntity> mTaskLists = TaskCationDB.getDB().select(null, PointsEntity.class);
        return mTaskLists.size();
    }


    //插入集合数据(初始一次)
    public static void insert(List<PointsEntity> list) {
        for (PointsEntity bean : list) {
            bean.setNow(false);
            bean.setPlay(false);
            bean.setQuen(false);
        }
        TaskCationDB.getDB().insert(null, list);
    }

    //删除所有
    public static void clear() {
        mTaskList.clear();
        TaskCationDB.getDB().deleteAll(null, PointsEntity.class);
    }

    //查询已经播放
    public static boolean isPlay(int position) {
        List<PointsEntity> mTaskLists = TaskCationDB.getDB().select(null, PointsEntity.class);
        return mTaskLists.get(position).isPlay();
    }
    //查询是不是当前播放
    public static boolean isNow(int position) {
        List<PointsEntity> mTaskLists = TaskCationDB.getDB().select(null, PointsEntity.class);
        return mTaskLists.get(position).isNow();
    }

    //设置已经播放
    public static void updatePlay(PointsEntity enty) {
        enty.setPlay(true);
        TaskCationDB.getDB().update(null, enty);
    }

    //设置当前播放
    public static void updateNowPlay(int position) {
        List<PointsEntity> mTaskLists = TaskCationDB.getDB().select(null, PointsEntity.class);
        for (PointsEntity info : mTaskLists) {
            info.setNow(false);
        }
        mTaskLists.get(position).setNow(true);
        mTaskLists.get(position).setQuen(false);
        TaskCationDB.getDB().deleteAll(null, PointsEntity.class);
        TaskCationDB.getDB().insert(null, mTaskLists);
    }

    //重置所有播放
    public static void updatePlay() {
        List<PointsEntity> mTaskLists = TaskCationDB.getDB().select(null, PointsEntity.class);
        for (PointsEntity info : mTaskLists) {
            info.setNow(false);
        }
        TaskCationDB.getDB().deleteAll(null, PointsEntity.class);
        TaskCationDB.getDB().insert(null, mTaskLists);
    }


    //设置进入队列
    public static void addQuen(int position) {
        List<PointsEntity> mTaskLists = TaskCationDB.getDB().select(null, PointsEntity.class);
        for (PointsEntity info : mTaskLists) {
            info.setQuen(false);
        }
        mTaskLists.get(position).setQuen(true);
        TaskCationDB.getDB().deleteAll(null, PointsEntity.class);
        TaskCationDB.getDB().insert(null, mTaskLists);
    }

    //更新不在队列
    public static void deleteQuen(int position) {
        List<PointsEntity> mTaskLists = TaskCationDB.getDB().select(null, PointsEntity.class);
        mTaskLists.get(position).setQuen(false);
        TaskCationDB.getDB().deleteAll(null, PointsEntity.class);
        TaskCationDB.getDB().insert(null, mTaskLists);
    }
    //是否有队列
    public static int getQuen() {
        List<PointsEntity> mTaskLists = TaskCationDB.getDB().select(null, PointsEntity.class);
       for(int i=0;i<mTaskLists.size();i++){
           if (mTaskLists.get(i).isQuen()){
            return i;
           }
       }
       return -1;
    }
}

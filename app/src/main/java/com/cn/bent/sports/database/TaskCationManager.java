package com.cn.bent.sports.database;

import android.content.Context;
import android.util.Log;

import com.cn.bent.sports.bean.ScenicPointsEntity;
import com.cn.bent.sports.utils.ImageUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lyj on 2018/1/30 0030.
 * description
 */

public class TaskCationManager {

    public static void setup(Context context) {
        TaskCationDB.setup(context);
    }

    //获得所有
    public static List<ScenicPointsEntity.PointsBean> getHistory() {
        return TaskCationDB.getDB().select(null, ScenicPointsEntity.PointsBean.class);
    }


    //获得当前播放实体类
    public static ScenicPointsEntity.PointsBean getIsNow() {
        boolean ismore = true;
        List<ScenicPointsEntity.PointsBean> noMoreLists = TaskCationDB.getDB().select(null, ScenicPointsEntity.PointsBean.class);
        for (ScenicPointsEntity.PointsBean bean : noMoreLists) {
            if (bean.isNow()) {
                return bean;
            }
        }
        return null;
    }


    public static int getSize() {
        List<ScenicPointsEntity.PointsBean> mTaskLists = TaskCationDB.getDB().select(null, ScenicPointsEntity.PointsBean.class);
        return mTaskLists.size();
    }


    //插入集合数据(初始一次)
    public static void insert(List<ScenicPointsEntity.PointsBean> list) {
        clear();
        for (ScenicPointsEntity.PointsBean bean : list) {
            bean.setNow(false);
            bean.setPlay(false);
            bean.setQuen(false);
        }
        TaskCationDB.getDB().insert(null, list);
    }

    //删除所有
    public static void clear() {
        TaskCationDB.getDB().deleteAll(null, ScenicPointsEntity.PointsBean.class);
    }

    //查询已经播放
    public static boolean isPlay(int position) {
        List<ScenicPointsEntity.PointsBean> mTaskLists = TaskCationDB.getDB().select(null, ScenicPointsEntity.PointsBean.class);
        return mTaskLists.get(position).isPlay();
    }
    //查询是不是当前播放
    public static boolean isNow(int position) {
        List<ScenicPointsEntity.PointsBean> mTaskLists = TaskCationDB.getDB().select(null, ScenicPointsEntity.PointsBean.class);
        return mTaskLists.get(position).isNow();
    }

    //设置已经播放
    public static void sethavePlay(String paths) {
        List<ScenicPointsEntity.PointsBean> mTaskLists = TaskCationDB.getDB().select(null, ScenicPointsEntity.PointsBean.class);
        for (ScenicPointsEntity.PointsBean info : mTaskLists) {
            if(info.getType()==2&&info.getMp3()!=null){
            }
            if(info.getType()==2&&info.getMp3()!=null&&paths.equals(info.getMp3())){
                Log.i("dddd", "getType=");
                info.setPlay(true);
                TaskCationDB.getDB().update(null,info);
                break;
            }
        }
    }

    //设置当前播放
    public static void updateNowPlay(int position) {
        List<ScenicPointsEntity.PointsBean> mTaskLists = TaskCationDB.getDB().select(null, ScenicPointsEntity.PointsBean.class);
        for (ScenicPointsEntity.PointsBean info : mTaskLists) {
            info.setNow(false);
        }
        mTaskLists.get(position).setNow(true);
        mTaskLists.get(position).setQuen(false);
        TaskCationDB.getDB().update(null, mTaskLists);
    }


    //
    public static void updatePlay(String paths) {
        List<ScenicPointsEntity.PointsBean> mTaskLists = TaskCationDB.getDB().select(null, ScenicPointsEntity.PointsBean.class);
        for (ScenicPointsEntity.PointsBean info : mTaskLists) {
            if(info.getMp3().equals(paths)){
                info.setNow(false);
                TaskCationDB.getDB().update(null,info);
                break;
            }
        }
    }


    //重置所有播放
    public static void updatePlay() {
        List<ScenicPointsEntity.PointsBean> mTaskLists = TaskCationDB.getDB().select(null, ScenicPointsEntity.PointsBean.class);
        for (ScenicPointsEntity.PointsBean info : mTaskLists) {
            info.setNow(false);
        }
        TaskCationDB.getDB().deleteAll(null, ScenicPointsEntity.PointsBean.class);
        TaskCationDB.getDB().insert(null, mTaskLists);
    }


    //设置进入队列
    public static void addQuen(int position) {
        List<ScenicPointsEntity.PointsBean> mTaskLists = TaskCationDB.getDB().select(null, ScenicPointsEntity.PointsBean.class);
        for (ScenicPointsEntity.PointsBean info : mTaskLists) {
            info.setQuen(false);
        }
        mTaskLists.get(position).setQuen(true);
        TaskCationDB.getDB().deleteAll(null, ScenicPointsEntity.PointsBean.class);
        TaskCationDB.getDB().insert(null, mTaskLists);
    }

    //更新不在队列
    public static void deleteQuen(int position) {
        List<ScenicPointsEntity.PointsBean> mTaskLists = TaskCationDB.getDB().select(null, ScenicPointsEntity.PointsBean.class);
        mTaskLists.get(position).setQuen(false);
        TaskCationDB.getDB().deleteAll(null, ScenicPointsEntity.PointsBean.class);
        TaskCationDB.getDB().insert(null, mTaskLists);
    }
    //是否有队列
    public static int getQuen() {
        List<ScenicPointsEntity.PointsBean> mTaskLists = TaskCationDB.getDB().select(null, ScenicPointsEntity.PointsBean.class);
       for(int i=0;i<mTaskLists.size();i++){
           if (mTaskLists.get(i).isQuen()){
            return i;
           }
       }
       return -1;
    }
}

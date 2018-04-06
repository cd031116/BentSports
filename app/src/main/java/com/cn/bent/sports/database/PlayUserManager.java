package com.cn.bent.sports.database;

import android.content.Context;
import android.util.Log;

import com.cn.bent.sports.bean.MemberDataEntity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2018/4/4/004.
 */

public class PlayUserManager {

    private static final List<MemberDataEntity> mTaskList = new ArrayList<>();

    public static void setup(Context context) {
        PlayUserDb.setup(context);
    }

    //获得所有
    public static List<MemberDataEntity> getHistory() {
        mTaskList.clear();
        mTaskList.addAll(PlayUserDb.getDB().select(null,MemberDataEntity.class));
        return mTaskList;
    }

    //插入集合数据(初始一次)
    public static void insert(List<MemberDataEntity> list){
        clear();
        PlayUserDb.getDB().insert(null, list);
    }

    //删除所有
    public static void clear() {
        PlayUserDb.getDB().deleteAll(null,MemberDataEntity.class);
    }

    public static MemberDataEntity getPlayUser(int userId){
        List<MemberDataEntity> mList = PlayUserDb.getDB().select(null,MemberDataEntity.class);
        for (MemberDataEntity bean:mList) {
            if (userId==bean.getUserId()) {
                return bean;
            }
        }
       return null;
    }


    //更新分数
    public static void updatePlay(int userId,int score) {
        List<MemberDataEntity> mTaskLists = PlayUserDb.getDB().select(null, MemberDataEntity.class);
        for (MemberDataEntity info : mTaskLists) {
            Log.d("tttt", "updatePlay: "+info.getUserId()+"---:"+userId+"---:"+score);
            if(info.getUserId()==userId){
                info.setScore(score);
                PlayUserDb.getDB().update(null,info);
                break;
            }
        }
    }
}

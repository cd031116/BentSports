package com.cn.bent.sports.database;

import android.content.Context;

import com.cn.bent.sports.bean.MemberDataEntity;
import com.cn.bent.sports.bean.ScenicPointsEntity;

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
        TaskCationDB.getDB().deleteAll(null,MemberDataEntity.class);
    }

    public static MemberDataEntity getPlayUser(String userId){
        List<MemberDataEntity> mList = PlayUserDb.getDB().select(null,MemberDataEntity.class);
        for (MemberDataEntity bean:mList) {
            if (userId.equals(bean.getUserId())) {
                return bean;
            }
        }
       return null;
    }

}

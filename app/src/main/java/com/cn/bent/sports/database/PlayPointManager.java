package com.cn.bent.sports.database;

import android.content.Context;

import com.cn.bent.sports.bean.GamePotins;
import com.cn.bent.sports.bean.MemberDataEntity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lyj on 2018/4/4/004.
 */

public class PlayPointManager  {

    private static final List<GamePotins> mTaskList = new ArrayList<>();

    public static void setup(Context context) {
        PlayPointDb.setup(context);
    }
    //获得所有
    public static List<GamePotins> getHistory() {
        mTaskList.addAll(PlayPointDb.getDB().select(null,GamePotins.class));
        return mTaskList;
    }
    //插入集合数据(初始一次)
    public static void insert(List<GamePotins> list){
        clear();
        PlayPointDb.getDB().insert(null, list);
    }
    //删除所有
    public static void clear() {
        PlayPointDb.getDB().deleteAll(null,GamePotins.class);
    }


    public static long getScore(){
        long total=0;
        List<GamePotins> mlist   =PlayPointDb.getDB().select(null,GamePotins.class);
        for (GamePotins potins : mlist) {
            if(potins.getState()==2){
                total=total+potins.getScore();
            }
        }
        return  total;
    }

    public static int getHavaPlay(){
        int total=0;
        List<GamePotins> mlist   =PlayPointDb.getDB().select(null,GamePotins.class);
        for (GamePotins potins : mlist) {
            if (potins.getState()==1){
                total=total+1;
            }
        }
        return  total;
    }

//是否已完成
    public static boolean isHavaPlay(){
        boolean isHava=true;
        List<GamePotins> mlist   =PlayPointDb.getDB().select(null,GamePotins.class);
        for (GamePotins potins : mlist) {
            if (potins.getState()==-1){
                isHava=false;
            }
        }
        return  isHava;
    }


}

package com.cn.bent.sports.utils;

import android.content.Context;

import com.cn.bent.sports.api.ConstantValues;
import com.cn.bent.sports.base.BaseConfig;

/**
 * 常用的常量
 * Created by lyj on 2017/8/1
 */
public class Constants {
    public static final String SOKET_PATH="soket_path";
    public static final String USER="USER";
    public static final String USER_BASE="user_base";
    public static final String USER_INFO="user_info";
    public static final String DOT_INFO="dot_info";
    public static final String DOT_LIST="dot_list";
    public static final String IS_FIRST="is_first";
    public static final String IS_TIME="is_time";
    public static final String TEST_URL="http://aihw.zhonghuilv.net/";
    public static final String BASE_URL="https://wxxcx.zhonghuilv.net/game/";
    public static final String IS_SHOWS="is_shows";
    public static final String LU_XIAN="lu_xian";
    public static final String PLAY_POSION="play_posion";
    public static final String NOW_PLAY="now_play";
    public static final String NOW_POION="now_poion";
    public static final String AR_KEY="uHF95ree4fY6nj6NrhAFZ3HwZvpQfexUpb0DQoy9vM0733bZ3kczl95yxtZ0sTNgTZE6idFXRx0Ej5UtJ0FEUGFUZLxdKpsH8UOg0J2spZUdLTOUlwtjimxW8yer0GReXJJgUvF1n8yiK7TkkX7hEDliysPlF4CYQaolaOsE4LufSDcBqPU83rPtbsFohb6FNX4iSA4b";
    public static final String LOGIN_TOKEN="Basic YWktdHJhdmVsLWFwcDphOWQ1NGQyYS03YzgxLTQwOWItOGFlOC1lY2NiZmU5NTAxNzc=";

    public static String getsocket(Context context){
        return ConstantValues.JAVA_URL_WEBSOCKET;
    }

}

package com.cn.bent.sports.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by lyj on 2018/2/26 0026.
 * description
 */

public class MapDot implements Serializable {
    private String place_id;
    private List<String> iBeacons;
    private String longitude;
    private String latitude;
    private String game_id;
    private String type;
    private String mp3;
    private String mp4;
    private String add_time;
    private String status;
    private String scenic_spot_id;
    private String name;
    private String game_url;
    private String is_play;
    private boolean isCheck;

    public boolean isCheck() {
        return isCheck;
    }

    public void setCheck(boolean check) {
        isCheck = check;
    }

    public String getPlace_id() {
        return place_id;
    }

    public void setPlace_id(String place_id) {
        this.place_id = place_id;
    }

    public List<String> getiBeacons() {
        return iBeacons;
    }

    public void setiBeacons(List<String> iBeacons) {
        this.iBeacons = iBeacons;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getGame_id() {
        return game_id;
    }

    public void setGame_id(String game_id) {
        this.game_id = game_id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getMp3() {
        return mp3;
    }

    public void setMp3(String mp3) {
        this.mp3 = mp3;
    }

    public String getMp4() {
        return mp4;
    }

    public void setMp4(String mp4) {
        this.mp4 = mp4;
    }

    public String getAdd_time() {
        return add_time;
    }

    public void setAdd_time(String add_time) {
        this.add_time = add_time;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getScenic_spot_id() {
        return scenic_spot_id;
    }

    public void setScenic_spot_id(String scenic_spot_id) {
        this.scenic_spot_id = scenic_spot_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGame_url() {
        return game_url;
    }

    public void setGame_url(String game_url) {
        this.game_url = game_url;
    }

    public String getIs_play() {
        return is_play;
    }

    public void setIs_play(String is_play) {
        this.is_play = is_play;
    }
}

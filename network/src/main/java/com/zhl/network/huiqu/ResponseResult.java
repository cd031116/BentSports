package com.zhl.network.huiqu;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Administrator on 2017/8/29.
 */

public class ResponseResult<T> {

    private String msg;
    private int code;
    private T data;

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}

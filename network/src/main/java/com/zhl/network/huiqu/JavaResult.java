package com.zhl.network.huiqu;

/**
 * Created by Administrator on 2017/8/29.
 */

public class JavaResult<T> {

    private String errmsg;
    private int code;
    private T data;

    public String getErrmsg() {
        return errmsg;
    }

    public void setErrmsg(String errmsg) {
        this.errmsg = errmsg;
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

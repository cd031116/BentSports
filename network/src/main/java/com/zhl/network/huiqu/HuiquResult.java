package com.zhl.network.huiqu;

/**
 * Created by Administrator on 2017/8/29.
 */

public class HuiquResult<T> {

    private String msg;
    private int code;
    private T body;

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

    public T getBody() {
        return body;
    }

    public void setBody(T body) {
        this.body = body;
    }
}

package com.zhl.network;

import com.google.gson.annotations.SerializedName;

/**
 * Created by dawn on 2018/1/2.
 */

public class HttpStatus {
    @SerializedName("code")
    private int mCode;
    @SerializedName("errmsg")
    private String mMessage;

    public int getCode() {
        return mCode;
    }

    public String getMessage() {
        return mMessage;
    }

    /**
     * API是否请求失败
     *
     * @return 失败返回true, 成功返回false
     */
    public boolean isCodeInvalid() {
        return mCode != 0;
    }
}

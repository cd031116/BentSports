package com.zhl.network;

import java.io.IOException;

/**
 * Created by necer on 2017/6/29.
 */

public class ApiGsonException extends RuntimeException {
    private int mErrorCode;

    public ApiGsonException(int errorCode, String errorMessage) {
        super(errorMessage);
        mErrorCode = errorCode;
    }

    /**
     * 判断是否是token失效
     *
     * @return 失效返回true, 否则返回false;
     */
//    public boolean isTokenExpried() {
//        return mErrorCode == Constants.TOKEN_EXPRIED;
//    }
}

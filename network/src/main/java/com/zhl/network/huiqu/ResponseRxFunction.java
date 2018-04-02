package com.zhl.network.huiqu;

import android.support.annotation.NonNull;
import android.util.Log;

import com.zhl.network.ApiException;

import io.reactivex.functions.Function;

/**
 * Created by lyj on 2017/8/29.
 */

public class ResponseRxFunction<T> implements Function<ResponseResult<T>, T> {
    @Override
    public T apply(@NonNull ResponseResult<T> responseResult) throws Exception {
        int code = responseResult.getCode();
        Log.e("ssss", "apply code: " + code + "\napply getMsg:" + responseResult.getMsg());
        if (code != 1 || responseResult.getData() == null) {
            throw new ApiException(responseResult.getMsg());
        }

        Log.e("dawn." + getClass().getSimpleName(), responseResult.getMsg());
        return responseResult.getData();
    }
}

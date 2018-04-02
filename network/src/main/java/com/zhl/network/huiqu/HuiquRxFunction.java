package com.zhl.network.huiqu;

import android.support.annotation.NonNull;
import android.util.Log;

import io.reactivex.functions.Function;

import com.zhl.network.ApiException;

/**
 * Created by lyj on 2017/8/29.
 */

public class HuiquRxFunction<T> implements Function<HuiquResult<T>, T> {
    @Override
    public T apply(@NonNull HuiquResult<T> huiquResult) throws Exception {
        int code = huiquResult.getCode();
        Log.e("ssss", "apply code: " + code + "\napply getMsg:" + huiquResult.getMsg());
        if (code != 1 || huiquResult.getBody() == null) {
            throw new ApiException(huiquResult.getMsg());
        }

        Log.e("dawn." + getClass().getSimpleName(), huiquResult.getMsg());
        return huiquResult.getBody();
    }
}

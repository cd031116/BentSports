package com.zhl.network.huiqu;

import android.support.annotation.NonNull;
import android.util.Log;

import com.zhl.network.ApiException;

import io.reactivex.functions.Function;

/**
 * Created by lyj on 2017/8/29.
 */

public class JavaRxFunction<T> implements Function<JavaResult<T>, T> {
    @Override
    public T apply(@NonNull JavaResult<T> javaResult) throws Exception {
        int code = javaResult.getCode();
        Log.e("ssss", "apply code java: " + code + "\napply getMsg:" + javaResult.getErrmsg());
        if (code != 0 || javaResult.getData() == null) {
            throw new ApiException(javaResult.getErrmsg());
        }

        Log.e("dawn." + getClass().getSimpleName(), javaResult.getErrmsg());
        return javaResult.getData();
    }
}

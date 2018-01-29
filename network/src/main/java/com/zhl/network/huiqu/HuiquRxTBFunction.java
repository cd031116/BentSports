package com.zhl.network.huiqu;

import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Function;

/**
 * Created by Administrator on 2017/8/29.
 */

public class HuiquRxTBFunction<T> implements Function<T, T> {

    @Override
    public T apply(@NonNull T httpResult) throws Exception {

        return httpResult;
    }
}

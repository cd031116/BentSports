package com.zhl.network;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import java.io.EOFException;
import java.net.BindException;
import java.net.ConnectException;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/**
 * Created by necer on 2017/6/28.
 */

public abstract class RxObserver<T> implements Observer<T> {


    private RxManager mRxManager;
    private int mWhichRequest;
    private String mKey;

    private boolean isShowDialog;
    private Dialog mDialog;
    private Context mContext;


    public RxObserver(Context context, String key, int whichRequest, boolean isShowDialog) {
        this.mContext = context;
        this.mKey = key;
        this.isShowDialog = isShowDialog;
        this.mWhichRequest = whichRequest;
        mDialog = new ProgressDialog(context);
        mRxManager = RxManager.getInstance();
    }


    @Override
    public final void onSubscribe(Disposable d) {
        mRxManager.add(mKey, d);
        if (isShowDialog) {
            mDialog.show();
        }
        onStart(mWhichRequest);
    }

    @Override
    public final void onNext(T value) {
        onSuccess(mWhichRequest, value);
        Log.d("ssss", "onNext: onSuccess" );
    }

    @Override
    public final void onError(Throwable e) {
        Log.e("ssss", "onError: " + e.getMessage());
        if (mDialog.isShowing()) {
            mDialog.dismiss();
        }
        if (e instanceof EOFException || e instanceof ConnectException || e instanceof SocketException || e instanceof BindException || e instanceof SocketTimeoutException || e instanceof UnknownHostException) {
            Toast.makeText(mContext, "网络异常，请稍后重试！", Toast.LENGTH_SHORT).show();
            onError(mWhichRequest, e);
        } else if (e instanceof ApiException) {
            onError(mWhichRequest, e);
        } else if (e instanceof ApiGsonException) {
            onError(mWhichRequest, e);
        } else {
            onError(mWhichRequest, e);
        }
    }

    @Override
    public final void onComplete() {
        if (mDialog.isShowing()) {
            mDialog.dismiss();
        }
    }

    public abstract void onSuccess(int whichRequest, T t);

    public abstract void onError(int whichRequest, Throwable e);

    public void onStart(int whichRequest) {

    }

}

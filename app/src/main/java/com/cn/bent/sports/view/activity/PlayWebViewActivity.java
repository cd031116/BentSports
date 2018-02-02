package com.cn.bent.sports.view.activity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Chronometer;

import com.cn.bent.sports.R;
import com.cn.bent.sports.api.BaseApi;
import com.cn.bent.sports.base.BaseActivity;
import com.cn.bent.sports.base.BaseConfig;
import com.cn.bent.sports.bean.AddScoreEntity;
import com.cn.bent.sports.bean.GameEntity;
import com.cn.bent.sports.bean.LoginBase;
import com.cn.bent.sports.bean.ReFreshEvent;
import com.cn.bent.sports.database.TaskCationManager;
import com.cn.bent.sports.utils.Constants;
import com.cn.bent.sports.utils.DataUtils;
import com.cn.bent.sports.utils.SaveObjectUtils;
import com.cn.bent.sports.utils.ToastUtils;
import com.cn.bent.sports.widget.ToastDialog;
import com.google.gson.Gson;
import com.zhl.network.RxObserver;
import com.zhl.network.RxSchedulers;
import com.zhl.network.huiqu.HuiquRxTBFunction;

import org.greenrobot.eventbus.EventBus;

import butterknife.Bind;
import butterknife.OnClick;

public class PlayWebViewActivity extends BaseActivity {

    @Bind(R.id.webview)
    WebView mWebView;
    @Bind(R.id.cut_down)
    Chronometer timer;
    LoginBase user;
    String gameId;
    private int MAX_REQUEST = 2;
    private int isRequestNum = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    protected int getLayoutId() {
        return R.layout.activity_play_web_view;
    }

    @Override
    public void initView() {
        super.initView();
        gameId = getIntent().getStringExtra("gameId");
        user = (LoginBase) SaveObjectUtils.getInstance(this).getObject(Constants.USER_INFO, null);
        long longValue = BaseConfig.getInstance(this).getLongValue(Constants.IS_TIME, 0);
        timer.setBase(longValue);//计时器清零
        int hour = (int) ((SystemClock.elapsedRealtime() - timer.getBase()) / 1000 / 60 / 60);
        timer.setFormat("0" + String.valueOf(hour) + ":%s");
        timer.start();
        initWebView();
    }

    private void initWebView() {
        WebSettings webSettings = mWebView.getSettings();

        webSettings.setJavaScriptEnabled(true);
        webSettings.setUseWideViewPort(true);
        webSettings.setLoadWithOverviewMode(true);
        webSettings.setBuiltInZoomControls(true);
        webSettings.setDisplayZoomControls(false);
        webSettings.setCacheMode(WebSettings.LOAD_DEFAULT);// 开启 DOM storage API 功能
        webSettings.setDomStorageEnabled(true);
        //开启 database storage API 功能
        webSettings.setDatabaseEnabled(true);
        //开启 Application Caches 功能
        webSettings.setAppCacheEnabled(true);

        webSettings.setSupportZoom(false);
        mWebView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY); //取消滚动条白边效果
        webSettings.setDefaultTextEncodingName("UTF-8");
        webSettings.setBlockNetworkImage(false);

        // 设置与Js交互的权限
        webSettings.setJavaScriptEnabled(true);
        // 设置允许JS弹窗
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
//        gameId = (int) (Math.random() * 5 + 1) + "";
        Log.e("dasa", "initWebView: " + gameId);
        switch (Integer.parseInt(gameId)) {
            case 1:
                mWebView.loadUrl("http://aihw.zhonghuilv.net/hby/index.html?uid=" + user.getMember_id() + "&etype=android&gameid="+gameId);
                break;
            case 2:
                mWebView.loadUrl("http://aihw.zhonghuilv.net/aihuwai/index.html?uid=" + user.getMember_id() + "&etype=android&gameid="+gameId);
                break;
            case 3:
                mWebView.loadUrl("http://aihw.zhonghuilv.net/denglong/index.html?uid=" + user.getMember_id() + "&etype=android&gameid="+gameId);
                break;
            case 4:
                mWebView.loadUrl("http://aihw.zhonghuilv.net/caishenmiao/index.html?uid=" + user.getMember_id() + "&etype=android&gameid="+gameId);
                break;
            case 5:
                mWebView.loadUrl("http://aihw.zhonghuilv.net/cdm/index.html?uid=" + user.getMember_id() + "&etype=android&gameid="+gameId);
                break;
            case 6:
                mWebView.loadUrl("http://aihw.zhonghuilv.net/xcm/index.html?uid=" + user.getMember_id() + "&etype=android&gameid="+gameId);
                break;
        }
        mWebView.addJavascriptInterface(new JSInterface(), "native");
    }

    @Override
    public void initData() {
        super.initData();
    }

    class JSInterface {
        @JavascriptInterface
        public void h5Result(String ss) {
            Gson gson = new Gson();
            GameEntity gameEntity = gson.fromJson(ss, GameEntity.class);
            addScroe(gameEntity);
        }
    }

    private void addScroe(final GameEntity gameEntity) {
        showAlert("正在上传积分。。", true);
        BaseApi.getDefaultService(this).addScore(gameEntity.getUid(), gameEntity.getScord(), gameEntity.getGameid())
                .map(new HuiquRxTBFunction<AddScoreEntity>())
                .compose(RxSchedulers.<AddScoreEntity>io_main())
                .subscribe(new RxObserver<AddScoreEntity>(this, "addScore", 1, false) {
                    @Override
                    public void onSuccess(int whichRequest, AddScoreEntity addScoreEntity) {
                        if (addScoreEntity.getBody().getAddStatus() == 1) {
                            dismissAlert();
                            TaskCationManager.update(gameId + "", DataUtils.getlongs());
                            LoginBase user = (LoginBase) SaveObjectUtils.getInstance(PlayWebViewActivity.this).getObject(Constants.USER_INFO, null);
                            setScore(user, gameEntity);
                            EventBus.getDefault().post(new ReFreshEvent());
                            if (TaskCationManager.noMore()) {
                                timer.setBase(SystemClock.elapsedRealtime());//计时器清零
                                timer.stop();
                                Intent intent = new Intent(PlayWebViewActivity.this, AllFinishActivity.class);
                                intent.putExtra("time", SystemClock.elapsedRealtime());
                                startActivity(intent);
                            } else
                                toContinue();
                        } else {
                            ToastUtils.showShortToast(PlayWebViewActivity.this, addScoreEntity.getMsg());
                            dismissAlert();
                        }
                        isRequestNum = 1;
                    }

                    private void toContinue() {
                        Intent intent = new Intent(PlayWebViewActivity.this, ContinueActivity.class);
                        intent.putExtra("game", gameEntity);
                        startActivity(intent);
                    }

                    @Override
                    public void onError(int whichRequest, Throwable e) {
                        dismissAlert();
                        if (isRequestNum < MAX_REQUEST) {
                            isRequestNum++;
                            ToastUtils.showShortToast(PlayWebViewActivity.this, "积分上传失败,正在重新上传积分");
                            addScroe(gameEntity);
                        } else {
                            ToastUtils.showShortToast(PlayWebViewActivity.this, "网络异常，积分上传失败，请重新玩此游戏");
                            toContinue();
                        }
                    }
                });
    }

    private void setScore(LoginBase user, GameEntity gameEntity) {
        if (user.getScore() != null)
            user.setScore(Integer.parseInt(user.getScore()) + gameEntity.getScord() + "");
        else
            user.setScore(gameEntity.getScord() + "");
        SaveObjectUtils.getInstance(PlayWebViewActivity.this).setObject(Constants.USER_INFO, user);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mWebView != null)
            mWebView.destroy();
    }

    @OnClick(R.id.top_left)
    void onClick(View view) {
        showDialog();
    }

    @Override
    public void onBackPressed() {
        showDialog();
    }

    private void showDialog() {
        new ToastDialog(this, R.style.dialog, "是否离开游戏", new ToastDialog.OnCloseListener() {
            @Override
            public void onClick(Dialog dialog, boolean confirm) {
                if (confirm) {
                    finish();
                } else {
                }
                dialog.dismiss();
            }
        }).setTitle("提示").show();
    }
}

package com.cn.bent.sports.view.activity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import com.cn.bent.sports.R;
import com.cn.bent.sports.api.BaseApi;
import com.cn.bent.sports.base.BaseActivity;
import com.cn.bent.sports.base.BaseConfig;
import com.cn.bent.sports.bean.AddScoreEntity;
import com.cn.bent.sports.bean.GameEntity;
import com.cn.bent.sports.bean.InfoEvent;
import com.cn.bent.sports.bean.LoginBase;
import com.cn.bent.sports.bean.ReFreshEvent;
import com.cn.bent.sports.database.TaskCationManager;
import com.cn.bent.sports.utils.Constants;
import com.cn.bent.sports.utils.DataUtils;
import com.cn.bent.sports.utils.SaveObjectUtils;
import com.cn.bent.sports.utils.ToastUtils;
import com.cn.bent.sports.widget.GameDialog;
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
    TextView timer;
    LoginBase user;
    String gameId;
    private int MAX_REQUEST = 2;
    private int isRequestNum = 1;
    private Handler handler2;
    private String gameUrl;

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
        gameUrl = getIntent().getStringExtra("gameUrl");
        user = (LoginBase) SaveObjectUtils.getInstance(this).getObject(Constants.USER_INFO, null);
        handler2 = new Handler();
        setTimes();
        initWebView();
    }

    private void setTimes() {
        handler2.postDelayed(runnable2, 1000);
    }

    Runnable runnable2 = new Runnable() {
        long longValue = BaseConfig.getInstance(PlayWebViewActivity.this).getLongValue(Constants.IS_TIME, 0);

        @Override
        public void run() {
            handler2.postDelayed(this, 1000);
            Log.i("tttt", "currentTimeMillis=" + (System.currentTimeMillis() - longValue) / 1000);
            if (((System.currentTimeMillis() - longValue) / 1000) >= 2 * 60 * 60) {
                handler2.removeCallbacks(runnable2);
                timer.setText("02.00.00");
            } else {
                timer.setText(DataUtils.getDateToTime(System.currentTimeMillis() - longValue));
            }

        }
    };


    private void initWebView() {
        WebSettings webSettings = mWebView.getSettings();
        mWebView.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
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
        webSettings.setBlockNetworkImage(true);
        mWebView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY); //取消滚动条白边效果
        mWebView.setWebViewClient(new WebViewClient() {
            //覆盖shouldOverrideUrlLoading 方法
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });
        webSettings.setDefaultTextEncodingName("UTF-8");
        webSettings.setBlockNetworkImage(false);

        // 设置与Js交互的权限
        webSettings.setJavaScriptEnabled(true);
        // 设置允许JS弹窗
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
        mWebView.loadUrl(gameUrl + "?uid=" + user.getMember_id() + "&etype=android&gameid=" + gameId);
        mWebView.addJavascriptInterface(new JSInterface(), "native");
        switch (Integer.parseInt(gameId))
        {
            case 1:
                showDialogMsg(getResources().getString(R.string.hong_bao));
                break;
            case 12:
                showDialogMsg(getResources().getString(R.string.deng_long));
                break;
            case 13:
                showDialogMsg(getResources().getString(R.string.chou_qian));
                break;
            case 14:
                showDialogMsg(getResources().getString(R.string.cai_deng_mi));
                break;
            case 16:
                showDialogMsg(getResources().getString(R.string.no_die));
                break;
            case 17:
                showDialogMsg(getResources().getString(R.string.xiong_chumo));
                break;
        }
    }

    @Override
    public void initData() {
        super.initData();
    }

    class JSInterface {
        @JavascriptInterface
        public void h5Result(String ss) {
            Log.e("dasa", "h5Result: " + ss);
            Gson gson = new Gson();
            GameEntity gameEntity = gson.fromJson(ss, GameEntity.class);
            addScroe(gameEntity);
            Log.d("dasa", "h5Result: " + gameEntity.getGameid() + ",getScord:" + gameEntity.getScord() + ",getUid:" + gameEntity.getUid());
        }
    }

    private void showDialogMsg(String names) {
        new GameDialog(this, R.style.dialog, new GameDialog.OnCloseListener() {
            @Override
            public void onClick(Dialog dialog, boolean confirm) {

                if (confirm) {

                } else {
                    finish();
                }
                dialog.dismiss();
            }
        }).setTitle(names).show();
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
//                            TaskCationManager.update(gameId + "", DataUtils.getlongs());
                            LoginBase user = (LoginBase) SaveObjectUtils.getInstance(PlayWebViewActivity.this).getObject(Constants.USER_INFO, null);
                            setScore(user, gameEntity);
                            EventBus.getDefault().post(new InfoEvent());
                            EventBus.getDefault().post(new ReFreshEvent());
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
                            ToastUtils.showShortToast(PlayWebViewActivity.this, "网络异常，积分上传失败，请重新提交成绩");
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
        if (mWebView != null) {
            mWebView.clearCache(true);
            mWebView.destroy();
        }
        if (handler2 != null)
            handler2.removeCallbacks(runnable2);
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

package com.cn.bent.sports.view.activity.youle.play;

import android.app.Dialog;
import android.util.Log;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.cn.bent.sports.R;
import com.cn.bent.sports.api.BaseApi;
import com.cn.bent.sports.base.BaseActivity;
import com.cn.bent.sports.bean.GameEntity;
import com.cn.bent.sports.bean.LoginResult;
import com.cn.bent.sports.utils.Constants;
import com.cn.bent.sports.utils.SaveObjectUtils;
import com.cn.bent.sports.widget.CompletionDialog;
import com.cn.bent.sports.widget.GameErrorDialog;
import com.cn.bent.sports.widget.GameFailDialog;
import com.google.gson.Gson;
import com.zhl.network.RxObserver;
import com.zhl.network.RxSchedulers;
import com.zhl.network.huiqu.JavaResult;
import com.zhl.network.huiqu.JavaRxFunction;

import butterknife.Bind;
import butterknife.OnClick;
import io.reactivex.Observable;

/**
 * Created by dawn on 2018/4/2.
 * 游戏web页面
 */

public class GameWebActivity extends BaseActivity {

    @Bind(R.id.webview)
    WebView mWebView;

    private String teamId, gamePointId;
    private boolean task, question;


    @Override
    protected int getLayoutId() {
        return R.layout.game_web_layout;
    }

    @Override
    public void initView() {

        LoginResult user = SaveObjectUtils.getInstance(this).getObject(Constants.USER_INFO, null);
        String access_token = user.getAccess_token();

        teamId = getIntent().getStringExtra("teamId");
        gamePointId = getIntent().getStringExtra("gamePointId");
        task = getIntent().getExtras().getBoolean("task");
        question = getIntent().getExtras().getBoolean("question");

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
//        mWebView.loadUrl(gameUrl + "?uid=" + user.getMember_id() + "&etype=android&gameid=" + gameId);
        mWebView.addJavascriptInterface(new JSInterface(), "native");
    }

    class JSInterface {
        @JavascriptInterface
        public void h5Result(String ss) {
            Log.e("dasa", "h5Result: " + ss);
            Gson gson = new Gson();
            GameEntity gameEntity = gson.fromJson(ss, GameEntity.class);
            finishTask(gameEntity, 1);
            Log.d("dasa", "h5Result: " + gameEntity.getGameid() + ",getScord:" + gameEntity.getScord() + ",getUid:" + gameEntity.getUid());
        }
    }

    private void finishTask(GameEntity gameEntity, int game_mode) {
        Observable<JavaResult<Boolean>> javaResultObservable;
        showAlert("正在获取...", true);
        if (game_mode == 1)
            javaResultObservable = BaseApi.getJavaLoginDefaultService(this)
                    .finishOfflineGame(70, 4, 23, 123);
        else
            javaResultObservable = BaseApi.getJavaLoginDefaultService(this)
                    .finishOnlineGame(70, 4, 23, 123);
        javaResultObservable.map(new JavaRxFunction<Boolean>())
                .compose(RxSchedulers.<Boolean>io_main())
                .subscribe(new RxObserver<Boolean>(this, TAG, 1, false) {
                    @Override
                    public void onSuccess(int whichRequest, Boolean aBoolean) {
                        dismissAlert();
                        if (aBoolean)
                            showSuccessDialog("game_name", 30);
                        else
                            showErrorDialog("game_name", 30);
                    }

                    @Override
                    public void onError(int whichRequest, Throwable e) {
                        dismissAlert();
                        if (e.getMessage().equals("回答错误"))
                            showAlertDialog();
                    }
                });
    }

    private void showAlertDialog() {
        new GameErrorDialog(GameWebActivity.this, R.style.dialog, new GameErrorDialog.OnCloseListener() {
            @Override
            public void onClick(Dialog dialog, int index) {
                dialog.dismiss();
                finish();
            }
        }).show();
    }

    private void showErrorDialog(String game_name, int score) {
        new GameFailDialog(GameWebActivity.this, R.style.dialog, new GameFailDialog.OnCloseListener() {
            @Override
            public void onClick(Dialog dialog, String index) {
                dialog.dismiss();
                finish();
            }
        }).setName(game_name).setScore(score).show();
    }

    private void showSuccessDialog(String game_name, int score) {
        new CompletionDialog(GameWebActivity.this, R.style.dialog, new CompletionDialog.OnCloseListener() {
            @Override
            public void onClick(Dialog dialog, String index) {
                dialog.dismiss();
                finish();
            }
        }).setName(game_name).setScore(score).show();
    }

    @Override
    public void initData() {
        super.initData();
    }

    @OnClick(R.id.map_return)
    void onClick(View view) {
        finish();
    }
}

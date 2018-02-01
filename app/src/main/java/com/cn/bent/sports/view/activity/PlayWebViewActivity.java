package com.cn.bent.sports.view.activity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;

import com.cn.bent.sports.R;
import com.cn.bent.sports.base.BaseActivity;
import com.cn.bent.sports.bean.GameEntity;
import com.cn.bent.sports.bean.LoginBase;
import com.cn.bent.sports.utils.Constants;
import com.cn.bent.sports.utils.SaveObjectUtils;
import com.cn.bent.sports.widget.ToastDialog;
import com.google.gson.Gson;

import butterknife.Bind;
import butterknife.OnClick;

public class PlayWebViewActivity extends BaseActivity {

    @Bind(R.id.webview)
    WebView mWebView;
    LoginBase user;
    String gameId;

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
        initWebView();
    }

    private void initWebView() {
        WebSettings webSettings = mWebView.getSettings();

        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.getSettings().setUseWideViewPort(true);
        mWebView.getSettings().setLoadWithOverviewMode(true);
        mWebView.getSettings().setBuiltInZoomControls(true);
        mWebView.getSettings().setDisplayZoomControls(false);
        mWebView.getSettings().setCacheMode(WebSettings.LOAD_DEFAULT);// 开启 DOM storage API 功能
        mWebView.getSettings().setDomStorageEnabled(true);
        //开启 database storage API 功能
        mWebView.getSettings().setDatabaseEnabled(true);
        //开启 Application Caches 功能
        mWebView.getSettings().setAppCacheEnabled(true);

        mWebView.getSettings().setSupportZoom(false);
        mWebView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY); //取消滚动条白边效果
        mWebView.getSettings().setDefaultTextEncodingName("UTF-8");
        mWebView.getSettings().setBlockNetworkImage(false);

        // 设置与Js交互的权限
        webSettings.setJavaScriptEnabled(true);
        // 设置允许JS弹窗
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);


         gameId = (int) (Math.random() * 5 + 1)+"";
        Log.e("dasa", "initWebView: " + gameId);
//        mWebView.loadUrl("http://192.168.17.48:8080/?uid=" + user.getMember_id() + "&etype=android");
        switch (Integer.parseInt(gameId)) {
            case 1:
                mWebView.loadUrl("http://aihw.zhonghuilv.net/hby/index.html?uid=" + user.getMember_id() + "&etype=android");
                break;
            case 2:
                mWebView.loadUrl("http://aihw.zhonghuilv.net/aihuwai/index.html?uid=" + user.getMember_id() + "&etype=android");
                break;
            case 3:
                mWebView.loadUrl("http://aihw.zhonghuilv.net/denglong/index.html?uid=" + user.getMember_id() + "&etype=android");
                break;
            case 4:
                mWebView.loadUrl("http://aihw.zhonghuilv.net/caishenmiao/index.html?uid=" + user.getMember_id() + "&etype=android");
                break;
            case 5:
                mWebView.loadUrl("http://aihw.zhonghuilv.net/cdm/index.html?uid=" + user.getMember_id() + "&etype=android");
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
            Log.e("dasa", "h5Result: " + ss);
            Gson gson = new Gson();
            GameEntity gameEntity = gson.fromJson(ss, GameEntity.class);
            Intent intent = new Intent(PlayWebViewActivity.this, ContinueActivity.class);
            intent.putExtra("game", gameEntity);
            startActivity(intent);

        }
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

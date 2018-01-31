package com.cn.bent.sports.view.activity;

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
import com.google.gson.Gson;

import butterknife.Bind;

public class PlayWebViewActivity extends BaseActivity {

    @Bind(R.id.webview)
    WebView mWebView;
    int uid = 4;

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

        int gameId = (int) (Math.random() * 5 + 1);
        Log.e("dasa", "initWebView: " + gameId);
        switch (gameId) {

            case 1:
                mWebView.loadUrl("http://aihw.zhonghuilv.net/aihuwai/index.html?uid=" + uid + "&etype=1");
                break;
            case 2:
                mWebView.loadUrl("http://aihw.zhonghuilv.net/caishenmiao/index.html?uid=" + uid + "&etype=1");
                break;
            case 3:
                mWebView.loadUrl("http://aihw.zhonghuilv.net/hby/index.html?uid=" + uid + "&etype=1");
                break;
            case 4:
                mWebView.loadUrl("http://aihw.zhonghuilv.net/denglong/index.html?uid=" + uid + "&etype=1");
                break;
            case 5:
                mWebView.loadUrl("http://aihw.zhonghuilv.net/aihuwai/index.html?uid=" + uid + "&etype=1");
                break;
        }
        // 先载入JS代码
//        mWebView.loadUrl("http://test.cwygp.com/bunengsi/index.html?uid=1&etype=1");
//        mWebView.loadUrl("http://192.168.16.118:3000/index.html?uid=1");

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
            Intent intent = new Intent(PlayWebViewActivity.this, ContinueActivity.class);
            intent.putExtra("game", gameEntity);
            startActivity(intent);

        }
    }


}

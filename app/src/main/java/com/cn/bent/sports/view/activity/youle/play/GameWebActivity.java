package com.cn.bent.sports.view.activity.youle.play;

import android.util.Log;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.cn.bent.sports.R;
import com.cn.bent.sports.base.BaseActivity;
import com.cn.bent.sports.bean.GameEntity;
import com.cn.bent.sports.view.activity.PlayWebViewActivity;
import com.google.gson.Gson;

import butterknife.Bind;

/**
 * Created by dawn on 2018/4/2.
 */

public class GameWebActivity extends BaseActivity {

    @Bind(R.id.webview)
    WebView mWebView;


    @Override
    protected int getLayoutId() {
        return R.layout.game_web_layout;
    }

    @Override
    public void initView() {
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
            finishTask(gameEntity);
            Log.d("dasa", "h5Result: " + gameEntity.getGameid() + ",getScord:" + gameEntity.getScord() + ",getUid:" + gameEntity.getUid());
        }
    }

    private void finishTask(GameEntity gameEntity) {

    }

    @Override
    public void initData() {
        super.initData();
    }
}

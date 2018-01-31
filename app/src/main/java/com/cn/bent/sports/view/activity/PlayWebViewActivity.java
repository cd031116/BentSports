package com.cn.bent.sports.view.activity;

import android.os.Bundle;
import android.util.Log;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;

import com.cn.bent.sports.R;
import com.cn.bent.sports.base.BaseActivity;

import butterknife.Bind;

public class PlayWebViewActivity extends BaseActivity {

    @Bind(R.id.webview)
    WebView mWebView ;

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

        // 设置与Js交互的权限
        webSettings.setJavaScriptEnabled(true);
        // 设置允许JS弹窗
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);

        // 先载入JS代码
        // 格式规定为:file:///android_asset/文件名.html
        mWebView.loadUrl("http://test.cwygp.com/test.html?uid="+1);

        mWebView.addJavascriptInterface(new JSInterface (),"native");
    }

    @Override
    public void initData() {
        super.initData();
    }
    class JSInterface {
        @JavascriptInterface
        public void h5Result(String ss) {
            Log.e("dasa", "h5Result: " + ss);
        }
    }
}

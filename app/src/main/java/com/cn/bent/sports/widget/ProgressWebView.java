package com.cn.bent.sports.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

/**
 * Created by Administrator on 2018/4/16/016.
 */

public class ProgressWebView extends WebView{
        private Webviewprogressbar progressbar;//进度条的矩形（进度线）
        private Handler handler;
        private WebView mwebview;
        public ProgressWebView(Context context, AttributeSet attrs) {
            super(context, attrs);
            //实例化进度条
            progressbar = new Webviewprogressbar(context);
            //设置进度条的size
            progressbar.setLayoutParams(new ViewGroup.LayoutParams
                    (ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            //刚开始时候进度条不可见
            progressbar.setVisibility(GONE);
            addView(progressbar);
            //把进度条添加到webview里面
            //初始化handle
            handler = new Handler();
            mwebview = this;

            initsettings();
        }
        private void initsettings() {
            // 初始化设置
            WebSettings msettings = this.getSettings();
            msettings.setJavaScriptEnabled(true);//开启javascript
            msettings.setDomStorageEnabled(true);//开启dom
            msettings.setDefaultTextEncodingName("UTF-8");
            setWebViewClient(new mywebclient());
            setWebChromeClient(new mywebchromeclient());
        }
/**
 * 自定义webchromeclient
 */
private class mywebchromeclient extends WebChromeClient {
    @Override
    public void onProgressChanged(WebView view, int newProgress) {
        super.onProgressChanged(view, newProgress);
        if (newProgress == 100) {
            progressbar.setprogress(100);
            handler.postDelayed(runnable, 200);//0.2秒后隐藏进度条
        } else if (progressbar.getVisibility() == GONE) {
            progressbar.setVisibility(VISIBLE);
        }
        //设置初始进度10，这样会显得效果真一点，总不能从1开始吧
        if (newProgress < 10) {
            newProgress = 10;
        }
        //不断更新进度
        progressbar.setprogress(newProgress);
    }
}
private class mywebclient extends WebViewClient {
    /**
     * 加载过程中 拦截加载的地址url
     *
     * @param view
     * @param url 被拦截的url
     * @return
     */
    @Override
    public boolean shouldOverrideUrlLoading(WebView view, String url) {
        mwebview.loadUrl(url);
        return true;
    }

    @Override
    public void onLoadResource(WebView view, String url) {
        super.onLoadResource(view, url);
    }
    /**
     * 页面加载完成回调的方法
     *
     * @param view
     * @param url
     */
    @Override
    public void onPageFinished(WebView view, String url) {
        super.onPageFinished(view, url);
        // 关闭图片加载阻塞
        view.getSettings().setBlockNetworkImage(false);
    }
    /**
     * 页面开始加载调用的方法
     *
     * @param view
     * @param url
     * @param favicon
     */
    @Override
    public void onPageStarted(WebView view, String url, Bitmap favicon) {
        super.onPageStarted(view, url, favicon);
    }
    @Override
    public void onScaleChanged(WebView view, float oldscale, float newscale) {
        super.onScaleChanged(view, oldscale, newscale);
        ProgressWebView.this.requestFocus();
        ProgressWebView.this.requestFocusFromTouch();
    }
}
    /**
     *刷新界面（此处为加载完成后进度消失）
     */
    private Runnable runnable = new Runnable() {

        @Override
        public void run() {
            progressbar.setVisibility(GONE);
        }
    };



}

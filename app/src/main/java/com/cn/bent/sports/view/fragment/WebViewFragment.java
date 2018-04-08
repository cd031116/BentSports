package com.cn.bent.sports.view.fragment;

import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;

import com.cn.bent.sports.R;
import com.cn.bent.sports.api.ConstantValues;
import com.cn.bent.sports.base.BaseFragment;
import com.jwenfeng.library.pulltorefresh.BaseRefreshListener;
import com.jwenfeng.library.pulltorefresh.PullToRefreshLayout;
import com.kennyc.view.MultiStateView;

import butterknife.Bind;

/**
 * Created by Administrator on 2018/4/8/008.
 */

public class WebViewFragment extends BaseFragment{
    @Bind(R.id.webview)
    WebView webView;
   private String t_contents;
    public static WebViewFragment newInstance(String contents) {
        WebViewFragment fragment = new WebViewFragment();
        Bundle bundle = new Bundle();
        bundle.putString("datas", contents);
        fragment.setArguments(bundle);
        return fragment;
    }
    @Override
    protected int getLayoutId() {
        return R.layout.layout_web;
    }

    @Override
    protected void initView(View view, Bundle savedInstanceState) {
        t_contents = (String) getArguments().getString("datas");
    }

    @Override
    protected void initData() {
        webView.loadDataWithBaseURL(null,t_contents, "text/html", "UTF-8", null);

    }






}

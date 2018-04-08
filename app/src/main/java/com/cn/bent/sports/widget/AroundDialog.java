package com.cn.bent.sports.widget;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cn.bent.sports.R;
import com.cn.bent.sports.utils.SupportMultipleScreensUtil;

/**
 * Created by dawn on 2018/3/7.
 */

public class AroundDialog extends Dialog implements View.OnClickListener {
    private TextView quanbu;
    private TextView yuyind;
    private TextView wc;
    private TextView gouwu;
    private TextView canting;
    private TextView jiudian;
    private TextView tingcc;
    private TextView damen;
    private Context mContext;
    private OnCloseListener listener;
    private LinearLayout sx_guanbi;


    public AroundDialog(Context context) {
        super(context);
        this.mContext = context;
    }


    public AroundDialog(Context context, int themeResId) {
        super(context, themeResId);
        this.mContext = context;
    }

    public AroundDialog(Context context, int themeResId, OnCloseListener listener) {
        super(context, themeResId);
        this.mContext = context;
        this.listener = listener;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.around_layout);
        View rootView = findViewById(android.R.id.content);
        SupportMultipleScreensUtil.init(mContext);
        SupportMultipleScreensUtil.scale(rootView);
        setCanceledOnTouchOutside(false);
        setCancelable(false);
        initView();
    }

    private void initView() {
        quanbu = (TextView) findViewById(R.id.quanbu);
        yuyind = (TextView) findViewById(R.id.yuyind);
        wc = (TextView) findViewById(R.id.wc);
        gouwu = (TextView) findViewById(R.id.gouwu);
        canting = (TextView) findViewById(R.id.canting);
        jiudian = (TextView) findViewById(R.id.jiudian);
        tingcc = (TextView) findViewById(R.id.tingcc);
        damen = (TextView) findViewById(R.id.damen);
        sx_guanbi = (LinearLayout) findViewById(R.id.sx_guanbi);
        quanbu.setOnClickListener(this);
        yuyind.setOnClickListener(this);
        wc.setOnClickListener(this);
        gouwu.setOnClickListener(this);
        canting.setOnClickListener(this);
        jiudian.setOnClickListener(this);
        tingcc.setOnClickListener(this);
        damen.setOnClickListener(this);
        sx_guanbi.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.quanbu:
                listener.onClick(this, 1);
                break;
            case R.id.yuyind:
                listener.onClick(this, 2);
                break;
            case R.id.wc:
                listener.onClick(this, 3);
                break;
            case R.id.gouwu:
                listener.onClick(this, 4);
                break;
            case R.id.canting:
                listener.onClick(this, 5);
                break;
            case R.id.jiudian:
                listener.onClick(this, 6);
                break;
            case R.id.tingcc:
                listener.onClick(this, 7);
                break;
            case R.id.damen:
                listener.onClick(this, 8);
                break;
            case R.id.sx_guanbi:
                listener.onClick(this, 0);
                break;
        }
    }

    public interface OnCloseListener {
        void onClick(Dialog dialog, int confirm);
    }
}


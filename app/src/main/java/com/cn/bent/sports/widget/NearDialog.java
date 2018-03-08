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

public class NearDialog extends Dialog implements View.OnClickListener {
    private TextView tingcc;
    private TextView gouwu;
    private TextView wc;
    private TextView canting;
    private TextView jiudian;
    private TextView damen;
    private TextView title;
    private TextView cancel;
    private TextView submit;
    private Context mContext;
    private OnCloseListener listener;
    private LinearLayout goto_layout;
    private int c_index = 0;
    private LinearLayout sx_guanbi;


    public NearDialog(Context context) {
        super(context);
        this.mContext = context;
    }


    public NearDialog(Context context, int themeResId) {
        super(context, themeResId);
        this.mContext = context;
    }

    public NearDialog(Context context, int themeResId, OnCloseListener listener) {
        super(context, themeResId);
        this.mContext = context;
        this.listener = listener;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.near_layout);
        View rootView = findViewById(android.R.id.content);
        SupportMultipleScreensUtil.init(mContext);
        SupportMultipleScreensUtil.scale(rootView);
        setCanceledOnTouchOutside(false);
        setCancelable(false);
        initView();
    }

    private void initView() {
        goto_layout = (LinearLayout) findViewById(R.id.goto_layout);
        goto_layout.setVisibility(View.GONE);
        tingcc = (TextView) findViewById(R.id.tingcc);
        gouwu = (TextView) findViewById(R.id.gouwu);
        wc = (TextView) findViewById(R.id.wc);
        canting = (TextView) findViewById(R.id.canting);
        jiudian = (TextView) findViewById(R.id.jiudian);
        damen = (TextView) findViewById(R.id.damen);
        title = (TextView) findViewById(R.id.title);
        cancel = (TextView) findViewById(R.id.cancel);
        submit = (TextView) findViewById(R.id.submit);
        sx_guanbi = (LinearLayout) findViewById(R.id.sx_guanbi);
        wc.setOnClickListener(this);
        gouwu.setOnClickListener(this);
        tingcc.setOnClickListener(this);
        canting.setOnClickListener(this);
        jiudian.setOnClickListener(this);
        damen.setOnClickListener(this);
        cancel.setOnClickListener(this);
        submit.setOnClickListener(this);
        sx_guanbi.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.wc:
                c_index = 1;
                title.setText("是否前往最近的卫生间");
                goto_layout.setVisibility(View.VISIBLE);
                break;
            case R.id.gouwu:
                c_index = 2;
                title.setText("是否前往最近的购物中心");
                goto_layout.setVisibility(View.VISIBLE);
                break;
            case R.id.tingcc:
                c_index = 3;
                title.setText("是否前往最近的停车场");
                goto_layout.setVisibility(View.VISIBLE);
                break;
            case R.id.canting:
                c_index = 4;
                title.setText("是否前往最近的餐厅");
                goto_layout.setVisibility(View.VISIBLE);
                break;
            case R.id.jiudian:
                c_index = 5;
                title.setText("是否前往最近的酒店");
                goto_layout.setVisibility(View.VISIBLE);
                break;
            case R.id.damen:
                c_index = 6;
                title.setText("是否前往最近的大门");
                goto_layout.setVisibility(View.VISIBLE);
                break;
            case R.id.cancel:
                c_index = 0;
                goto_layout.setVisibility(View.GONE);
                break;
            case R.id.submit:
                listener.onClick(this,false,  c_index);
                break;
            case R.id.sx_guanbi:
                listener.onClick(this, false, 0);
                c_index = 0;
                break;
        }
    }

    public interface OnCloseListener {
        void onClick(Dialog dialog, boolean confirm, int index);
    }
}



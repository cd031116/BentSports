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
    private Context mContext;
    private OnCloseListener listener;
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
        tingcc = (TextView) findViewById(R.id.tingcc);
        gouwu = (TextView) findViewById(R.id.gouwu);
        wc = (TextView) findViewById(R.id.wc);
        canting = (TextView) findViewById(R.id.canting);
        jiudian = (TextView) findViewById(R.id.jiudian);
        damen = (TextView) findViewById(R.id.damen);
        sx_guanbi = (LinearLayout) findViewById(R.id.sx_guanbi);
        wc.setOnClickListener(this);
        gouwu.setOnClickListener(this);
        tingcc.setOnClickListener(this);
        canting.setOnClickListener(this);
        jiudian.setOnClickListener(this);
        damen.setOnClickListener(this);
        sx_guanbi.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        String ms = "";
        switch (v.getId()) {
            case R.id.wc:
                c_index = 1;
                ms = "是否前往最近的卫生间";
                break;
            case R.id.gouwu:
                c_index = 2;
                ms = "是否前往最近的购物中心";
                break;
            case R.id.tingcc:
                c_index = 3;
                ms = "是否前往最近的停车场";
                break;
            case R.id.canting:
                c_index = 4;
                ms = "是否前往最近的餐厅";
                break;
            case R.id.jiudian:
                c_index = 5;
                ms = "是否前往最近的酒店";
                break;
            case R.id.damen:
                c_index = 6;
                ms = "是否前往最近的大门";
                break;
            case R.id.sx_guanbi:
                c_index = 0;
                ms = "";
                break;
        }
        listener.onClick(this, ms, c_index);
    }

    public interface OnCloseListener {
        void onClick(Dialog dialog, String confirm, int index);
    }
}



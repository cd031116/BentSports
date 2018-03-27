package com.cn.bent.sports.widget;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.cn.bent.sports.R;
import com.cn.bent.sports.utils.SupportMultipleScreensUtil;

/**
 * Created by dawn on 2018/3/24.
 */

public class OutGameDialog extends Dialog implements View.OnClickListener {
    private TextView out_game;
    private TextView quxiao;
    private Context mContext;
    private OnClickListener listener;


    public OutGameDialog(Context context) {
        super(context);
        this.mContext = context;
    }


    public OutGameDialog(Context context, int themeResId) {
        super(context, themeResId);
        this.mContext = context;
    }

    public OutGameDialog(Context context, int themeResId, OnClickListener listener) {
        super(context, themeResId);
        this.mContext = context;
        this.listener = listener;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.out_game_dialog_layout);
        View rootView = findViewById(android.R.id.content);
        SupportMultipleScreensUtil.init(mContext);
        SupportMultipleScreensUtil.scale(rootView);
        setCanceledOnTouchOutside(false);
        setCancelable(false);
        initView();
    }

    private void initView() {
        out_game = (TextView) findViewById(R.id.out_game);
        quxiao = (TextView) findViewById(R.id.quxiao);
        out_game.setOnClickListener(this);
        quxiao.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.out_game:
                listener.onClick(this, 1);
                break;
            case R.id.quxiao:
                listener.onClick(this, 2);
                break;
        }
    }

    public interface OnClickListener {
        void onClick(Dialog dialog, int confirm);
    }
}



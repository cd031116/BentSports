package com.cn.bent.sports.widget;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.cn.bent.sports.R;
import com.cn.bent.sports.utils.SupportMultipleScreensUtil;

/**
 * Created by dawn on 2018/3/24.
 */

public class TimeOverDialog extends Dialog implements View.OnClickListener {
    private TextView look_rank;
    private ImageView task_close;
    private TextView score_text;
    private TextView time_text;
    private Context mContext;
    private OnCloseListener listener;
    private int c_index = 0;


    public TimeOverDialog(Context context) {
        super(context);
        this.mContext = context;
    }


    public TimeOverDialog(Context context, int themeResId) {
        super(context, themeResId);
        this.mContext = context;
    }

    public TimeOverDialog(Context context, int themeResId, OnCloseListener listener) {
        super(context, themeResId);
        this.mContext = context;
        this.listener = listener;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.time_over_dialog_layout);
        View rootView = findViewById(android.R.id.content);
        SupportMultipleScreensUtil.init(mContext);
        SupportMultipleScreensUtil.scale(rootView);
        setCanceledOnTouchOutside(false);
        setCancelable(false);
        initView();
    }

    private void initView() {
        look_rank = (TextView) findViewById(R.id.look_rank);
        task_close = (ImageView) findViewById(R.id.task_close);
        time_text = (TextView) findViewById(R.id.time_text);
        score_text = (TextView) findViewById(R.id.score_text);
        look_rank.setOnClickListener(this);
        task_close.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.look_rank:
                listener.onClick(this, "");
                break;
            case R.id.task_close:
                listener.onClick(this, "");
                break;
        }
    }

    public interface OnCloseListener {
        void onClick(Dialog dialog, String confirm);
    }
}


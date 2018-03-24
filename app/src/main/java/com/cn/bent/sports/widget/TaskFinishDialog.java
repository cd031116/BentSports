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

public class TaskFinishDialog extends Dialog implements View.OnClickListener {
    private TextView look_rank;
    private ImageView task_close;
    private TextView score;
    private Context mContext;
    private OnCloseListener listener;
    private int c_index = 0;


    public TaskFinishDialog(Context context) {
        super(context);
        this.mContext = context;
    }


    public TaskFinishDialog(Context context, int themeResId) {
        super(context, themeResId);
        this.mContext = context;
    }

    public TaskFinishDialog(Context context, int themeResId, OnCloseListener listener) {
        super(context, themeResId);
        this.mContext = context;
        this.listener = listener;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.task_finish_dialog_layout);
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
        score = (TextView) findViewById(R.id.score);
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



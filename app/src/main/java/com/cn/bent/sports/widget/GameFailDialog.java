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

public class GameFailDialog extends Dialog implements View.OnClickListener {
    private TextView know_complete;
    private Context mContext;
    private OnCloseListener listener;
    private int score;
    private String name;


    public GameFailDialog(Context context) {
        super(context);
        this.mContext = context;
    }


    public GameFailDialog(Context context, int themeResId) {
        super(context, themeResId);
        this.mContext = context;
    }

    public GameFailDialog(Context context, int themeResId, OnCloseListener listener) {
        super(context, themeResId);
        this.mContext = context;
        this.listener = listener;
    }

    public GameFailDialog setScore(int score) {
        this.score = score;
        return this;
    }

    public GameFailDialog setName(String name) {

        this.name = name;
        return this;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.game_fail_dialog_layout);
        View rootView = findViewById(android.R.id.content);
        SupportMultipleScreensUtil.init(mContext);
        SupportMultipleScreensUtil.scale(rootView);
        setCanceledOnTouchOutside(false);
        setCancelable(false);
        initView();
    }

    private void initView() {

        TextView score_text = (TextView) findViewById(R.id.score);
        TextView name_text = (TextView) findViewById(R.id.name);
        know_complete = (TextView) findViewById(R.id.know_complete);
        know_complete.setOnClickListener(this);
        score_text.setText("+ "+score);
        name_text.setText(name);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.know_complete:
                listener.onClick(this, "");
                break;
        }
    }

    public interface OnCloseListener {
        void onClick(Dialog dialog, String confirm);
    }
}



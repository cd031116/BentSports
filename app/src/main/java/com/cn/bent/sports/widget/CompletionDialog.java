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
 * Created by dawn on 2018/3/24.
 */

public class CompletionDialog extends Dialog implements View.OnClickListener {
    private TextView know_complete;
    private int score;
    private Context mContext;
    private OnCloseListener listener;
    private int c_index = 0;
    private String name;


    public CompletionDialog(Context context) {
        super(context);
        this.mContext = context;
    }


    public CompletionDialog(Context context, int themeResId) {
        super(context, themeResId);
        this.mContext = context;
    }

    public CompletionDialog(Context context, int themeResId, OnCloseListener listener) {
        super(context, themeResId);
        this.mContext = context;
        this.listener = listener;
    }

    public CompletionDialog setScore(int score) {
        this.score = score;
        return this;
    }

    public CompletionDialog setName(String name) {

        this.name = name;
        return this;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.complete_dialog_layout);
        View rootView = findViewById(android.R.id.content);
        SupportMultipleScreensUtil.init(mContext);
        SupportMultipleScreensUtil.scale(rootView);
        setCanceledOnTouchOutside(false);
        setCancelable(false);
        initView();
    }

    private void initView() {
        know_complete = (TextView) findViewById(R.id.know_complete);
        TextView score_text = (TextView) findViewById(R.id.score);
        TextView game_name_text = (TextView) findViewById(R.id.game_name);
        score_text.setText("+ "+score);
        game_name_text.setText(name);
        know_complete.setOnClickListener(this);

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



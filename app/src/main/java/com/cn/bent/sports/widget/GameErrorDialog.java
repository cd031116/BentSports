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

public class GameErrorDialog extends Dialog implements View.OnClickListener {
    private TextView know_complete,msg;
    private Context mContext;
    private OnCloseListener listener;
    private String msga="";

    public GameErrorDialog(Context context) {
        super(context);
        this.mContext = context;
    }


    public GameErrorDialog(Context context, int themeResId) {
        super(context, themeResId);
        this.mContext = context;
    }

    public GameErrorDialog(Context context, int themeResId, OnCloseListener listener) {
        super(context, themeResId);
        this.mContext = context;
        this.listener = listener;
    }


    public GameErrorDialog setMsg(String setMsg) {
      this.msga=setMsg;
        return this;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.game_error_dialog_layout);
        View rootView = findViewById(android.R.id.content);
        SupportMultipleScreensUtil.init(mContext);
        SupportMultipleScreensUtil.scale(rootView);
        msg= (TextView) rootView.findViewById(R.id.msg);
        msg.setText(msga);
        setCanceledOnTouchOutside(false);
        setCancelable(false);
        initView();
    }

    private void initView() {
        know_complete = (TextView) findViewById(R.id.know_complete);
        know_complete.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.know_complete:
                listener.onClick(this, 1);
                break;
        }
    }

    public interface OnCloseListener {
        void onClick(Dialog dialog, int confirm);
    }
}



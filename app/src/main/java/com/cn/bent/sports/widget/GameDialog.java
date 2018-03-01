package com.cn.bent.sports.widget;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.cn.bent.sports.R;
import com.cn.bent.sports.utils.SupportMultipleScreensUtil;


/**
 * Created by lyj on 2018/1/9 0009.
 * description
 */

public class GameDialog extends Dialog implements View.OnClickListener {
    private int imgResId = 0;
    private TextView titleTxt;
    private TextView submitTxt;
    private TextView cancelTxt;
    private Context mContext;
    private String content;
    private OnCloseListener listener;
    private String positiveName;
    private String negativeName;
    private String title;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public GameDialog(Context context) {
        super(context);
        this.mContext = context;
    }

    public GameDialog(Context context, String content) {
        super(context, R.style.dialog);
        this.mContext = context;
        this.content = content;
    }

    public GameDialog(Context context, int themeResId, String content) {
        super(context, themeResId);
        this.mContext = context;
        this.content = content;
    }

    public GameDialog(Context context, int themeResId, OnCloseListener
            listener) {
        super(context, themeResId);
        this.mContext = context;
        this.listener = listener;
    }

    public GameDialog(Context context, int themeResId, int imgResId, OnCloseListener
            listener) {
        super(context, themeResId);
        this.mContext = context;
        this.imgResId = imgResId;
        this.listener = listener;
    }

    public GameDialog(Context context, int themeResId, String content, OnCloseListener
            listener) {
        super(context, themeResId);
        this.mContext = context;
        this.content = content;
        this.listener = listener;
    }

    protected GameDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        this.mContext = context;
    }

    public GameDialog setTitle(String title) {
        this.title = title;
        return this;
    }

    public GameDialog setPositiveButton(String name) {
        this.positiveName = name;
        return this;
    }

    public GameDialog setNegativeButton(String name) {
        this.negativeName = name;
        return this;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.game_dialog);
        View rootView = findViewById(android.R.id.content);
        SupportMultipleScreensUtil.init(mContext);
        SupportMultipleScreensUtil.scale(rootView);
        setCanceledOnTouchOutside(false);
        setCancelable(false);
        initView();
    }

    private void initView() {
        titleTxt = (TextView) findViewById(R.id.title);
        submitTxt = (TextView) findViewById(R.id.submit);
        cancelTxt = (TextView) findViewById(R.id.cancel);
        submitTxt.setOnClickListener(this);
        cancelTxt.setOnClickListener(this);
        if (!TextUtils.isEmpty(positiveName)) {
            submitTxt.setText(positiveName);
        }
        if (imgResId == 0)
            titleTxt.setText(title);
        else {
            titleTxt.setText(title);
            Drawable nav_up = mContext.getResources().getDrawable(imgResId);
            nav_up.setBounds(0, 0, nav_up.getMinimumWidth(), nav_up.getMinimumHeight());
            titleTxt.setCompoundDrawables(null, null, null, nav_up);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.submit:
                listener.onClick(this, true);
                break;
            case R.id.cancel:
                listener.onClick(this, false);
                break;
        }
    }

    public interface OnCloseListener {
        void onClick(Dialog dialog, boolean confirm);
    }
}

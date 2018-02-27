package com.cn.bent.sports.view.poupwindow;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;

import com.cn.bent.sports.R;
import com.cn.bent.sports.utils.SupportMultipleScreensUtil;

/**
 * Created by lyj on 2018/2/26 0026.
 * description
 */

public class MainPoupWindow extends PopupWindow {
    private Activity mContext;
    private View view;
    private ImageView go_to;
    private ItemInclick itemsOnClick;
    public MainPoupWindow(Activity mContext,ItemInclick itemsOnClickd) {
        this.mContext=mContext;
        this.itemsOnClick = itemsOnClickd;
        this.view = LayoutInflater.from(mContext).inflate(R.layout.detail_window, null);

        go_to= (ImageView) view.findViewById(R.id.go_to);
        go_to.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               dismiss();
            }
        });



    /* 设置弹出窗口特征 */
        // 设置视图
        this.setOutsideTouchable(true);
        // 设置弹出窗体的宽和高
        this.setHeight(RelativeLayout.LayoutParams.MATCH_PARENT);
        this.setWidth(RelativeLayout.LayoutParams.MATCH_PARENT);
        this.setContentView(view);
        // 设置弹出窗体可点击
        SupportMultipleScreensUtil.scale(view);
        this.setFocusable(true);
        // 实例化一个ColorDrawable颜色为半透明
        ColorDrawable dw = new ColorDrawable(0xb0000000);
        // 设置弹出窗体的背景
        this.setBackgroundDrawable(dw);
        // 设置弹出窗体显示时的动画，从底部向上弹出
//        this.setAnimationStyle(R.style.select_anim);
        this.getContentView().measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
    }

    public interface ItemInclick {
        void ItemClick();
    }

}
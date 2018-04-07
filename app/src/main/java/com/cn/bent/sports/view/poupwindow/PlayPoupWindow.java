package com.cn.bent.sports.view.poupwindow;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cn.bent.sports.R;
import com.cn.bent.sports.utils.SupportMultipleScreensUtil;
import com.cn.bent.sports.view.activity.DetailDotActivity;

/**
 * Created by lyj on 2018/3/9 0009.
 * description
 */

public class PlayPoupWindow extends PopupWindow {
    private  Activity mContext;
    private View view;
    private ImageView  go_detail;
    private TextView name_game, go_task,juli;
    private RelativeLayout main_top;
    private ItemInclick itemsOnClick;
    public PlayPoupWindow(Activity context, String path, String sound_path, ItemInclick itemsOnClickd) {
        this.mContext = context;
        this.itemsOnClick = itemsOnClickd;
        this.view = LayoutInflater.from(mContext).inflate(R.layout.play_item, null);
        SupportMultipleScreensUtil.scale(view);
        main_top = (RelativeLayout) view.findViewById(R.id.main_top);
        go_detail = (ImageView) view.findViewById(R.id.go_detail);
        go_detail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mContext.startActivity(new Intent(mContext, DetailDotActivity.class));
            }
        });
        main_top.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

    /* 设置弹出窗口特征 */
        // 设置视图
        // 设置弹出窗体的宽和高
        this.setHeight(RelativeLayout.LayoutParams.MATCH_PARENT);
        this.setWidth(RelativeLayout.LayoutParams.MATCH_PARENT);
        this.setContentView(view);
        // 设置弹出窗体可点击
        SupportMultipleScreensUtil.scale(view);
        this.setOutsideTouchable(true);
//        this.setFocusable(true);
        view.setFocusable(true);
        // 实例化一个ColorDrawable颜色为半透明
        ColorDrawable dw = new ColorDrawable(0xb0000000);
        // 设置弹出窗体的背景
        this.setBackgroundDrawable(dw);
        // 设置弹出窗体显示时的动画，从底部向上弹出
        this.setAnimationStyle(R.style.select_anim);
        this.getContentView().measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
    }


    public interface ItemInclick {
        void ItemClick(int position);
    }



}

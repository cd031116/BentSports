package com.cn.bent.sports.view.poupwindow;

import android.app.Activity;
import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;

import com.cn.bent.sports.R;
import com.cn.bent.sports.base.BaseConfig;
import com.cn.bent.sports.utils.Constants;
import com.cn.bent.sports.utils.SupportMultipleScreensUtil;

import java.io.IOException;

/**
 * Created by lyj on 2018/2/26 0026.
 * description
 */

public class MainPoupWindow extends PopupWindow {
    private Activity mContext;
    private View view;
    private ImageView go_to;
    private ItemInclick itemsOnClick;
    private MediaPlayer mPlayer;

    public MainPoupWindow(final Activity mContext, ItemInclick itemsOnClickd) {
        this.mContext = mContext;
        this.itemsOnClick = itemsOnClickd;
        this.view = LayoutInflater.from(mContext).inflate(R.layout.detail_window, null);
        SupportMultipleScreensUtil.scale(view);
        mPlayer = new MediaPlayer();
        mPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        view.setFocusable(true);
        playSund();
        go_to = (ImageView) view.findViewById(R.id.go_to);
        go_to.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mPlayer != null) {
                    mPlayer.stop();
                    mPlayer.release();
                }
                BaseConfig.getInstance(mContext).setStringValue(Constants.IS_SHOWS,"0");
                dismiss();
            }
        });

        /* 设置弹出窗口特征 */
        // 设置视图
        this.setOutsideTouchable(false);
        // 设置弹出窗体的宽和高
        this.setHeight(RelativeLayout.LayoutParams.MATCH_PARENT);
        this.setWidth(RelativeLayout.LayoutParams.MATCH_PARENT);
        this.setContentView(view);
        BaseConfig.getInstance(mContext).setStringValue(Constants.IS_SHOWS,"1");
        // 设置弹出窗体可点击
        this.setFocusable(false);
        // 实例化一个ColorDrawable颜色为半透明
        ColorDrawable dw = new ColorDrawable(0xb0000000);
        // 设置弹出窗体的背景
        this.setBackgroundDrawable(dw);
        // 设置弹出窗体显示时的动画，从底部向上弹出
//        this.setAnimationStyle(R.style.select_anim);
        this.getContentView().measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
    }


    public void setclose(){
        if (mPlayer != null) {
            mPlayer.stop();
            mPlayer.release();
        }
        dismiss();
    }
    public interface ItemInclick {
        void ItemClick();
    }

    private void playSund() {
        mPlayer.reset();
        try {
            AssetFileDescriptor fileDescriptor = mContext.getAssets().openFd("pre_app.mp3");
            mPlayer.setDataSource(fileDescriptor.getFileDescriptor(),
                    fileDescriptor.getStartOffset(),
                    fileDescriptor.getLength());
            mPlayer.prepare();
            mPlayer.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}

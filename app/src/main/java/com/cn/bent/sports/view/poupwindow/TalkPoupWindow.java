package com.cn.bent.sports.view.poupwindow;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapRegionDecoder;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cn.bent.sports.R;
import com.cn.bent.sports.base.BaseConfig;
import com.cn.bent.sports.utils.Constants;
import com.cn.bent.sports.utils.SaveObjectUtils;
import com.cn.bent.sports.utils.SupportMultipleScreensUtil;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by lyj on 2018/2/26 0026.
 * description
 */

public class TalkPoupWindow extends PopupWindow {
    private   Activity mContext;
    private View view;
    private ImageView close_ima;
    private TextView travel_name, context_t;
    private MediaPlayer mPlayer;
    private ItemInclick itemsOnClick;

    public TalkPoupWindow(final Activity mContext,String names_t, String path, String shuoming, ItemInclick itemsOnClickd) {
        this.mContext = mContext;
        this.itemsOnClick = itemsOnClickd;
        this.view = LayoutInflater.from(mContext).inflate(R.layout.talk_window, null);
        SupportMultipleScreensUtil.scale(view);
        context_t = (TextView) view.findViewById(R.id.context_t);
        travel_name = (TextView) view.findViewById(R.id.travel_name);
        close_ima = (ImageView) view.findViewById(R.id.close_ima);
        mPlayer = new MediaPlayer();
        mPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        playSund(path);
        close_ima.setOnClickListener(new View.OnClickListener() {
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
        context_t.setText(shuoming);
        travel_name.setText(names_t);

    /* 设置弹出窗口特征 */
        // 设置视图
        this.setOutsideTouchable(true);
        // 设置弹出窗体的宽和高
        this.setHeight(RelativeLayout.LayoutParams.MATCH_PARENT);
        this.setWidth(RelativeLayout.LayoutParams.MATCH_PARENT);
        this.setContentView(view);
        BaseConfig.getInstance(mContext).setStringValue(Constants.IS_SHOWS,"1");
        // 设置弹出窗体可点击
        SupportMultipleScreensUtil.scale(view);
        this.setFocusable(false);
        view.setFocusable(true);
        // 实例化一个ColorDrawable颜色为半透明
        ColorDrawable dw = new ColorDrawable(0xb0000000);
        // 设置弹出窗体的背景
        this.setBackgroundDrawable(dw);
        // 设置弹出窗体显示时的动画，从底部向上弹出
        this.setAnimationStyle(R.style.select_anim);
        this.getContentView().measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        this.getContentView().setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    if (mPlayer != null) {
                        mPlayer.stop();
                        mPlayer.release();
                    }
                    dismiss();
                    return true;
                }
                return false;
            }
        });

    }

    public interface ItemInclick {
        void ItemClick();
    }

    private void playSund(final String paths) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                mPlayer.reset();
                try {
                    mPlayer.setDataSource(paths);
                    mPlayer.prepare();
                    mPlayer.start();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public void setclose(){
        if (mPlayer != null) {
            mPlayer.stop();
            mPlayer.release();
        }
        dismiss();
    }
}

package com.cn.bent.sports.view.poupwindow;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapRegionDecoder;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cn.bent.sports.R;
import com.cn.bent.sports.base.BaseConfig;
import com.cn.bent.sports.bean.GamePotins;
import com.cn.bent.sports.bean.TeamGame;
import com.cn.bent.sports.utils.Constants;
import com.cn.bent.sports.utils.SupportMultipleScreensUtil;
import com.cn.bent.sports.view.activity.youle.PlayMultActivity;
import com.cn.bent.sports.widget.OneTaskFinishDialog;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by lyj on 2018/2/26 0026.
 * description
 */

public class DoTaskPoupWindow extends PopupWindow {
    private Activity mContext;
    private View view;
    private ImageView  close_ima, wancheng;
    private TextView name_game, go_task, juli, need_people;
    private LinearLayout line_s;
    private ItemInclick itemsOnClick;
    private ItemOnclick itemOnClick;
    private MediaPlayer mPlayer;
    private WebView webview;

    public DoTaskPoupWindow(final Activity context, boolean isDo, final GamePotins gamePotins, final TeamGame teamGame, String distance, final ItemOnclick itemsOnClickd) {
        this.mContext = context;
        this.itemOnClick = itemsOnClickd;
        this.view = LayoutInflater.from(mContext).inflate(R.layout.do_task_window, null);

        SupportMultipleScreensUtil.scale(view);
        name_game = (TextView) view.findViewById(R.id.name_game);
        line_s = (LinearLayout) view.findViewById(R.id.line_s);
        go_task = (TextView) view.findViewById(R.id.go_task);
        close_ima = (ImageView) view.findViewById(R.id.close_ima);
        juli = (TextView) view.findViewById(R.id.juli);
        wancheng = (ImageView) view.findViewById(R.id.wancheng);
        need_people = (TextView) view.findViewById(R.id.need_people);
        webview= (WebView) view.findViewById(R.id.webview);
        if (isDo) {
            go_task.setVisibility(View.VISIBLE);
            line_s.setVisibility(View.GONE);
        } else {
            go_task.setVisibility(View.GONE);
            line_s.setVisibility(View.VISIBLE);
        }
        if (gamePotins.getState() == 1) {
            int passNum = gamePotins.getTeamTaskDetails().size();
            int allNum;
            if (teamGame.getPassRate() * teamGame.getTeamMemberReal() % 100 == 0)
                allNum = teamGame.getPassRate() * teamGame.getTeamMemberReal() / 100;
            else
                allNum = teamGame.getPassRate() * teamGame.getTeamMemberReal() / 100 + 1;
            int needNum = allNum - passNum;
            if (needNum > 0) {
                need_people.setVisibility(View.VISIBLE);
                String finish_num_str = "还需" + needNum + "人完成";
                SpannableStringBuilder builder = new SpannableStringBuilder(finish_num_str);
                builder.setSpan(new ForegroundColorSpan(mContext.getResources().getColor(R.color.color_fd7d6f)), 2, finish_num_str.length() - 3, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                need_people.setText(builder);
            } else
                need_people.setVisibility(View.GONE);
        } else if (gamePotins.getState()==-1){
            int allNum;
            if (teamGame.getPassRate() * teamGame.getTeamMemberReal() % 100 == 0)
                allNum = teamGame.getPassRate() * teamGame.getTeamMemberReal() / 100;
            else
                allNum = teamGame.getPassRate() * teamGame.getTeamMemberReal() / 100 + 1;
            Log.d("dddd", "DoTaskPoupWindow: "+allNum+"--real:"+teamGame.getTeamMemberReal());
            if (allNum>0){
                need_people.setVisibility(View.VISIBLE);
                String finish_num_str = "还需" + allNum + "人完成";
                SpannableStringBuilder builder = new SpannableStringBuilder(finish_num_str);
                builder.setSpan(new ForegroundColorSpan(mContext.getResources().getColor(R.color.color_fd7d6f)), 2, finish_num_str.length() - 3, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                need_people.setText(builder);
            }
            else
                need_people.setVisibility(View.GONE);
        }else
            need_people.setVisibility(View.GONE);

        webview.loadDataWithBaseURL(null, gamePotins.getDescription(), "text/html", "UTF-8", null);

        name_game.setText(gamePotins.getAlias());
        juli.setText(distance);
        close_ima.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                itemOnClick.ItemClick(0,gamePotins,teamGame);
            }
        });
        go_task.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                itemOnClick.ItemClick(1,gamePotins,teamGame);
            }
        });
        wancheng.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                itemOnClick.ItemClick(2,gamePotins,teamGame);
            }
        });
        mContext.setFinishOnTouchOutside(true);

        /* 设置弹出窗口特征 */
        // 设置视图
        // 设置弹出窗体的宽和高
        setWindow(mContext);
    }

    public DoTaskPoupWindow(final Activity mContext, String names, boolean isDo, String path, String sound_path, String distance, ItemInclick itemsOnClickd) {
        this.mContext = mContext;
        this.itemsOnClick = itemsOnClickd;
        this.view = LayoutInflater.from(mContext).inflate(R.layout.do_task_window, null);
        SupportMultipleScreensUtil.scale(view);
        mPlayer = new MediaPlayer();
        mPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        name_game = (TextView) view.findViewById(R.id.name_game);
        line_s = (LinearLayout) view.findViewById(R.id.line_s);
        go_task = (TextView) view.findViewById(R.id.go_task);
        close_ima = (ImageView) view.findViewById(R.id.close_ima);
        juli = (TextView) view.findViewById(R.id.juli);
        wancheng = (ImageView) view.findViewById(R.id.wancheng);
        if (isDo) {
            go_task.setVisibility(View.VISIBLE);
            line_s.setVisibility(View.GONE);
        } else {
            go_task.setVisibility(View.GONE);
            line_s.setVisibility(View.VISIBLE);
            playSund(sound_path);
        }
        name_game.setText(names);
        juli.setText(distance);
        close_ima.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mPlayer != null) {
                    mPlayer.stop();
                    mPlayer.release();
                }
                itemsOnClick.ItemClick(0);
            }
        });
        go_task.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mPlayer != null) {
                    mPlayer.stop();
                    mPlayer.release();
                }
                itemsOnClick.ItemClick(1);
            }
        });
        wancheng.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        mContext.setFinishOnTouchOutside(true);
//
//        try {
//            InputStream inputStreamF = mContext.getAssets().open(path + ".jpg");
//            //获得图片的宽、高
//            BitmapFactory.Options tmpOptions = new BitmapFactory.Options();
//            tmpOptions.inJustDecodeBounds = true;
//            BitmapFactory.decodeStream(inputStreamF, null, tmpOptions);
//            int width = tmpOptions.outWidth;
//            int height = tmpOptions.outHeight;
//
//            InputStream inputStreamS = mContext.getAssets().open(path + ".jpg");
//            //设置显示图片的中心区域
//            BitmapRegionDecoder bitmapRegionDecoder = BitmapRegionDecoder.newInstance(inputStreamS, false);
//            BitmapFactory.Options options = new BitmapFactory.Options();
//            options.inPreferredConfig = Bitmap.Config.RGB_565;
//            Bitmap bitmap = bitmapRegionDecoder.decodeRegion(new Rect(0, 0, width, height), options);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
        setWindow(mContext);


    }

    private void setWindow(Activity mContext) {
    /* 设置弹出窗口特征 */
        // 设置视图
        // 设置弹出窗体的宽和高
        this.setHeight(RelativeLayout.LayoutParams.MATCH_PARENT);
        this.setWidth(RelativeLayout.LayoutParams.MATCH_PARENT);
        this.setContentView(view);
        BaseConfig.getInstance(mContext).setStringValue(Constants.IS_SHOWS, "1");
        // 设置弹出窗体可点击
        SupportMultipleScreensUtil.scale(view);
//        this.setOutsideTouchable(true);
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


    public void setvisib(boolean istrue) {
        if (istrue) {
            go_task.setVisibility(View.VISIBLE);
            line_s.setVisibility(View.GONE);
        } else {
            go_task.setVisibility(View.GONE);
            line_s.setVisibility(View.VISIBLE);
        }
    }

    public void setDistance(String istrue) {
        juli.setText(istrue);
    }

    public void setNeedPeople(SpannableStringBuilder istrue) {
        need_people.setText(istrue);
    }

    public interface ItemInclick {
        void ItemClick(int position);
    }
    public interface ItemOnclick {
        void ItemClick(int position,GamePotins gamePotins,TeamGame teamGame);
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

    public void setclose() {
        if (mPlayer != null) {

            mPlayer.stop();
            mPlayer.release();
        }
        itemsOnClick.ItemClick(0);
    }
}

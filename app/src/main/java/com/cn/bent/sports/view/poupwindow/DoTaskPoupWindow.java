package com.cn.bent.sports.view.poupwindow;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapRegionDecoder;
import android.graphics.Rect;
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

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by lyj on 2018/2/26 0026.
 * description
 */

public class DoTaskPoupWindow extends PopupWindow {
    private Activity mContext;
    private View view;
    private ImageView image_bg,close_ima;
    private TextView name_game,go_task;
    private LinearLayout line_s;
    private ItemInclick itemsOnClick;
    public DoTaskPoupWindow(Activity mContext,boolean isDo,String path,ItemInclick itemsOnClickd) {
        this.mContext=mContext;
        this.itemsOnClick = itemsOnClickd;
        this.view = LayoutInflater.from(mContext).inflate(R.layout.do_task_window, null);
        image_bg= (ImageView) view.findViewById(R.id.image_bg);
        name_game= (TextView) view.findViewById(R.id.name_game);
        line_s= (LinearLayout) view.findViewById(R.id.line_s);
        go_task= (TextView) view.findViewById(R.id.go_task);
        close_ima= (ImageView) view.findViewById(R.id.close_ima);

        if(isDo){
            go_task.setVisibility(View.VISIBLE);
            line_s.setVisibility(View.GONE);
        }else {
            go_task.setVisibility(View.GONE);
            line_s.setVisibility(View.VISIBLE);
        }

        close_ima.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        go_task.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                itemsOnClick.ItemClick();
            }
        });

        try {
            InputStream inputStreamF = mContext.getAssets().open(path+".jpg");
            //获得图片的宽、高
            BitmapFactory.Options tmpOptions = new BitmapFactory.Options();
            tmpOptions.inJustDecodeBounds = true;
            BitmapFactory.decodeStream(inputStreamF, null, tmpOptions);
            int width = tmpOptions.outWidth;
            int height = tmpOptions.outHeight;

            InputStream inputStreamS = mContext.getAssets().open(path+".jpg");
            //设置显示图片的中心区域
            BitmapRegionDecoder bitmapRegionDecoder = BitmapRegionDecoder.newInstance(inputStreamS, false);
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inPreferredConfig = Bitmap.Config.RGB_565;
            Bitmap bitmap = bitmapRegionDecoder.decodeRegion(new Rect(0, 0, width, height), options);
            image_bg.setImageBitmap(bitmap);
        } catch (IOException e) {
            e.printStackTrace();
        }


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
        this.setAnimationStyle(R.style.select_anim);
        this.getContentView().measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
    }

    public interface ItemInclick {
        void ItemClick();
    }
}
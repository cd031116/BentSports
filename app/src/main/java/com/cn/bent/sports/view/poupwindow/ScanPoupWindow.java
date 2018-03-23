package com.cn.bent.sports.view.poupwindow;

import android.app.Activity;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.amap.api.maps.AMapUtils;
import com.amap.api.maps.model.LatLng;
import com.cn.bent.sports.R;
import com.cn.bent.sports.base.BaseConfig;
import com.cn.bent.sports.bean.ScenicPointsEntity;
import com.cn.bent.sports.recyclebase.CommonAdapter;
import com.cn.bent.sports.recyclebase.ViewHolder;
import com.cn.bent.sports.utils.Constants;
import com.cn.bent.sports.utils.SupportMultipleScreensUtil;
import com.vondear.rxtools.view.RxQRCode;

import java.util.List;

/**
 * Created by lyj on 2018/3/23 0023.
 * description
 */

public class ScanPoupWindow  extends PopupWindow {
    private Activity mContext;
    private View view;
    private TextView travel_all;
    private RelativeLayout main_top;
    private ImageView mIvQrCode;
    private String strs;
    private RecyclerView r_list;
    private ItemInclick itemsOnClick;
    public ScanPoupWindow(Activity mContext,String strs,ItemInclick itemsOnClickd) {
        this.mContext = mContext;
        this.itemsOnClick = itemsOnClickd;
        this.strs=strs;
        this.view = LayoutInflater.from(mContext).inflate(R.layout.creat_poup_poup, null);
        SupportMultipleScreensUtil.scale(view);
        main_top= (RelativeLayout) view.findViewById(R.id.main_top);
        mIvQrCode= (ImageView) view.findViewById(R.id.mIvQrCode);
        main_top.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        createSrq(strs);

    /* 设置弹出窗口特征 */
        // 设置视图
        // 设置弹出窗体的宽和高
        this.setHeight(RelativeLayout.LayoutParams.WRAP_CONTENT);
        this.setWidth(RelativeLayout.LayoutParams.MATCH_PARENT);
        this.setContentView(view);
        BaseConfig.getInstance(mContext).setStringValue(Constants.IS_SHOWS,"1");
        // 设置弹出窗体可点击
        this.setOutsideTouchable(true);
        this.setFocusable(true);
        // 实例化一个ColorDrawable颜色为半透明
        ColorDrawable dw = new ColorDrawable(0x00000000);
        // 设置弹出窗体的背景
        this.setBackgroundDrawable(dw);
        // 设置弹出窗体显示时的动画，从底部向上弹出
        this.setAnimationStyle(R.style.select_anim);
        this.getContentView().measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
    }

    public interface ItemInclick {
        void ItemClick(int position);
    }


    private void createSrq(String stars){
        if(TextUtils.isEmpty(stars)){
            return;
        }
        //二维码生成方式一  推荐此方法
        RxQRCode.builder(stars).
                backColor(mContext.getResources().getColor(R.color.white)).
                codeColor(mContext.getResources().getColor(R.color.black)).
                codeSide(600).
                into(mIvQrCode);
    }
}

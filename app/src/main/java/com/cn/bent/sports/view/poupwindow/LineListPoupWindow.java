package com.cn.bent.sports.view.poupwindow;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapRegionDecoder;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.amap.api.maps.AMapUtils;
import com.amap.api.maps.model.LatLng;
import com.cn.bent.sports.R;
import com.cn.bent.sports.base.BaseConfig;
import com.cn.bent.sports.bean.PointsEntity;
import com.cn.bent.sports.recyclebase.CommonAdapter;
import com.cn.bent.sports.recyclebase.ViewHolder;
import com.cn.bent.sports.utils.Constants;
import com.cn.bent.sports.utils.SupportMultipleScreensUtil;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 * Created by lyj on 2018/3/13 0013.
 * description
 */

public class LineListPoupWindow extends PopupWindow {
    private Activity mContext;
    private View view;
    private TextView travel_all;
    private LinearLayout line_s;
    private RecyclerView r_list;
    private ItemInclick itemsOnClick;
    private CommonAdapter<PointsEntity> mAdapter;
    private List<PointsEntity> mList;
    private LatLng mLatlng;
    public LineListPoupWindow(Activity mContext,LatLng startLatlng, List<PointsEntity> mList, ItemInclick itemsOnClickd) {
        this.mContext = mContext;
        this.mList=mList;
        this.mLatlng=startLatlng;
        this.itemsOnClick = itemsOnClickd;
        this.view = LayoutInflater.from(mContext).inflate(R.layout.list_poup_item, null);
        SupportMultipleScreensUtil.scale(view);
        travel_all = (TextView) view.findViewById(R.id.travel_all);
        r_list = (RecyclerView) view.findViewById(R.id.r_list);
        mAdapter=new CommonAdapter<PointsEntity>(mContext,R.layout.recycl_item_poup,mList) {
            @Override
            protected void convert(ViewHolder holder, PointsEntity s,final int position) {
                holder.setText(R.id.t_name,s.getName());
                String julis = String.valueOf(AMapUtils.calculateLineDistance(mLatlng, new LatLng(s.getLocation().getLatitude(),s.getLocation().getLongitude())));
                holder.setText(R.id.distance,julis+"M");
                holder.setOnClickListener(R.id.main_top, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        itemsOnClick.ItemClick(position);
                    }
                });
            }
        };
        r_list.setLayoutManager(new LinearLayoutManager(mContext));
        r_list.setAdapter(mAdapter);

    /* 设置弹出窗口特征 */
        // 设置视图
        // 设置弹出窗体的宽和高
        this.setHeight(RelativeLayout.LayoutParams.MATCH_PARENT);
        this.setWidth(RelativeLayout.LayoutParams.MATCH_PARENT);
        this.setContentView(view);
        BaseConfig.getInstance(mContext).setStringValue(Constants.IS_SHOWS,"1");
        // 设置弹出窗体可点击
        SupportMultipleScreensUtil.scale(view);
        this.setOutsideTouchable(true);
        this.setFocusable(true);
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

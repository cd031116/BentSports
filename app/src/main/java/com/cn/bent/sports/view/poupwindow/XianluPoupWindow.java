package com.cn.bent.sports.view.poupwindow;

import android.app.Activity;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.amap.api.maps.AMapUtils;
import com.amap.api.maps.model.LatLng;
import com.cn.bent.sports.R;
import com.cn.bent.sports.base.BaseConfig;
import com.cn.bent.sports.bean.PointsEntity;
import com.cn.bent.sports.bean.ScenicPointsEntity;
import com.cn.bent.sports.recyclebase.CommonAdapter;
import com.cn.bent.sports.recyclebase.ViewHolder;
import com.cn.bent.sports.utils.Constants;
import com.cn.bent.sports.utils.SupportMultipleScreensUtil;

import java.util.List;

/**
 * Created by lyj on 2018/3/13 0013.
 * description
 */

public class XianluPoupWindow extends PopupWindow {
    private Activity mContext;
    private View view;
    private LinearLayout line_s;
    private RecyclerView r_list;
    private ItemInclick itemsOnClick;
    private CommonAdapter<List<ScenicPointsEntity.PointsBean>> mAdapter;
    private List<List<ScenicPointsEntity.PointsBean>> mList;

    public XianluPoupWindow(Activity mContext, List<List<ScenicPointsEntity.PointsBean>> mList, ItemInclick itemsOnClickd) {
        this.mContext = mContext;
        this.mList=mList;
        this.itemsOnClick = itemsOnClickd;
        this.view = LayoutInflater.from(mContext).inflate(R.layout.luxian_poup_item, null);
        SupportMultipleScreensUtil.scale(view);
        r_list = (RecyclerView) view.findViewById(R.id.luxian_list);
        mAdapter=new CommonAdapter<List<ScenicPointsEntity.PointsBean>>(mContext,R.layout.luxian_list_item,mList) {
            @Override
            protected void convert(ViewHolder holder, List<ScenicPointsEntity.PointsBean> pointsEntityList,final int position) {
                holder.setText(R.id.xianlu_name,(position+1)+"、石燕湖线路");
                holder.setText(R.id.xianlu_num,pointsEntityList.size()+"");
                holder.getConvertView().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
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

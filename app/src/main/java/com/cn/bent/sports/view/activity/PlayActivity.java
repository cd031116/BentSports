package com.cn.bent.sports.view.activity;

import android.app.Dialog;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.CameraPosition;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MyLocationStyle;
import com.cn.bent.sports.R;
import com.cn.bent.sports.base.BaseActivity;
import com.cn.bent.sports.base.BaseFragment;
import com.cn.bent.sports.widget.OutGameDialog;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by dawn on 2018/3/27
 * 单人游戏地图界面
 */

public class PlayActivity extends BaseActivity implements AMap.OnMyLocationChangeListener {

    @Bind(R.id.mapView)
    MapView mapView;
    @Bind(R.id.time)
    TextView time;
    @Bind(R.id.score)
    TextView score;
    @Bind(R.id.all_task)
    TextView all_task;
    @Bind(R.id.finish_task)
    TextView finish_task;
    @Bind(R.id.task_finish_layout)
    LinearLayout task_finish_layout;
    @Bind(R.id.map_game_ms_layout)
    RelativeLayout map_game_ms_layout;

    private AMap aMap;
    private float mCurrentZoom = 18f;


    @Override
    protected int getLayoutId() {
        return R.layout.map_one_layout;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mapView.onCreate(savedInstanceState);
    }

    @Override
    public void initView() {
        super.initView();
        task_finish_layout.setVisibility(View.GONE);
        if (aMap == null) {
            aMap = mapView.getMap();
        }
        aMap.getUiSettings().setZoomControlsEnabled(false);//去掉高德地图右下角隐藏的缩放按钮
        aMap.setOnMarkerClickListener(mMarkerClickListener);
        aMap.setOnCameraChangeListener(onCameraChangeListener);
    }


    @Override
    public void initData() {

    }

    @OnClick({R.id.map_scan, R.id.map_more})
    void onClick(View view) {
        switch (view.getId()) {
            case R.id.map_scan:
                break;
            case R.id.map_more:
                OpenOutDialog();
                break;
        }
    }

    /**
     * 定位方法
     */
    public void startLocation() {
        aMap.setMyLocationEnabled(true);
        // 设置定位的类型为定位模式，有定位、跟随或地图根据面向方向旋转几种
        MyLocationStyle myLocationStyle = new MyLocationStyle();
        myLocationStyle.interval(2 * 1000);
        myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATION_ROTATE_NO_CENTER);
        myLocationStyle.myLocationIcon(BitmapDescriptorFactory
                .fromResource(R.drawable.dangqwz));
        myLocationStyle.strokeColor(Color.parseColor("#F9DEDE"));// 设置圆形的边框颜色
        myLocationStyle.radiusFillColor(Color.argb(100, 249, 222, 222));//
        aMap.setMyLocationStyle(myLocationStyle);
        aMap.setOnMyLocationChangeListener(this);
    }

    @Override
    public void onMyLocationChange(Location location) {
        if (location != null) {
            aMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(location.getLatitude(), location.getLongitude()), mCurrentZoom));
        }
    }

    /**
     * marker 点击事件
     */
    AMap.OnMarkerClickListener mMarkerClickListener = new AMap.OnMarkerClickListener() {
        @Override
        public boolean onMarkerClick(Marker marker) {
            return false;
        }
    };

    /**
     * 地图缩放比
     */
    AMap.OnCameraChangeListener onCameraChangeListener = new AMap.OnCameraChangeListener() {
        @Override
        public void onCameraChange(CameraPosition cameraPosition) {

        }

        @Override
        public void onCameraChangeFinish(CameraPosition cameraPosition) {
            mCurrentZoom = cameraPosition.zoom;//获取手指缩放地图后的值
        }
    };

    /**
     * 退出dialog
     */
    private void OpenOutDialog() {
        OutGameDialog outGameDialog= new OutGameDialog(PlayActivity.this, R.style.dialog, new OutGameDialog.OnClickListener() {
            @Override
            public void onClick(Dialog dialog, int confirm) {
                dialog.dismiss();
                if (confirm == 1) {
                    //TODO 退出比赛接口
                }
            }
        });
        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
        Window window = outGameDialog.getWindow();
        layoutParams.copyFrom(window.getAttributes());
        layoutParams.gravity = Gravity.BOTTOM;
        window.setAttributes(layoutParams);
        outGameDialog.show();
    }

}

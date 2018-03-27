package com.cn.bent.sports.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.services.core.LatLonPoint;
import com.cn.bent.sports.R;
import com.cn.bent.sports.base.BaseActivity;
import com.vondear.rxtools.view.RxToast;

import butterknife.Bind;
import butterknife.OnClick;

public class GetLocationActivity extends BaseActivity {
    @Bind(R.id.text)
    TextView text;
    //声明AMapLocationClient类对象
    public AMapLocationClient mLocationClient = null;
    //声明AMapLocationClientOption对象
    public AMapLocationClientOption mLocationOption = null;
    private String latitude, longitude;
    private boolean isFirstLoc = true;
    AMapLocationListener mAMapLocationListener = new AMapLocationListener() {
        @Override
        public void onLocationChanged(AMapLocation aMapLocation) {
            if (aMapLocation != null) {
                if (aMapLocation.getErrorCode() == 0) {
                    //可在其中解析amapLocation获取相应内容。
                    if(isFirstLoc){
                        isFirstLoc = false;
                        latitude = String.valueOf(aMapLocation.getLatitude());
                        longitude = String.valueOf(aMapLocation.getLongitude());
                        text.setText("latitude="+latitude+"||longitude="+longitude);
                        mLocationClient.stopLocation();
                    }

                } else {
                    //定位失败时，可通过ErrCode（错误码）信息来确定失败的原因，errInfo是错误信息，详见错误码表。
                    RxToast.warning("获取位置信息失败!");
                }
            }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_get_location;
    }

    @Override
    public void initView() {
        super.initView();
        mLocationClient = new AMapLocationClient(GetLocationActivity.this);
//设置定位回调监听
        mLocationClient.setLocationListener(mAMapLocationListener);
//初始化AMapLocationClientOption对象
        mLocationOption = new AMapLocationClientOption();
        //设置定位模式为AMapLocationMode.Hight_Accuracy，高精度模式。
        mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
        //给定位客户端对象设置定位参数
        mLocationClient.setLocationOption(mLocationOption);
        //设置定位模式为AMapLocationMode.Device_Sensors，仅设备模式。
        mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Device_Sensors);
    }

    @Override
    public void initData() {
        super.initData();
    }


    @OnClick({R.id.huoqu})
    void conlick(View view) {
        switch (view.getId()) {
            case R.id.huoqu:
                mLocationClient.startLocation();
                text.setText("");
                break;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(mLocationClient!=null){
            mLocationClient.stopLocation();
            mLocationClient.onDestroy();
        }

    }
}

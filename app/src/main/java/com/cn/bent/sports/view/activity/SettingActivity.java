package com.cn.bent.sports.view.activity;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.cn.bent.sports.R;
import com.cn.bent.sports.base.BaseActivity;
import com.cn.bent.sports.bean.LoginBase;
import com.cn.bent.sports.ibeacon.ScanActivity;
import com.cn.bent.sports.utils.Constants;
import com.cn.bent.sports.utils.SaveObjectUtils;
import com.cn.bent.sports.widget.ToastDialog;

import butterknife.Bind;
import butterknife.OnClick;

public class SettingActivity extends BaseActivity {
    @Bind(R.id.name_t)
    TextView name_t;
    @Bind(R.id.phone_t)
    TextView phone_t;
    @Bind(R.id.user_photo)
    ImageView user_photo;

    private LoginBase user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_setting;
    }

    @Override
    public void initView() {
        super.initView();

    }

    @Override
    public void onResume() {
        super.onResume();
        user = SaveObjectUtils.getInstance(SettingActivity.this).getObject(Constants.USER_INFO, null);
        if (TextUtils.isEmpty(user.getNickname())) {
            name_t.setText("未设置");
            name_t.setTextColor(Color.parseColor("#999999"));
        } else {
            name_t.setText(user.getNickname());
            name_t.setTextColor(Color.parseColor("#333333"));
            phone_t.setText(user.getMobile());
            RequestOptions requestOptions = RequestOptions.circleCropTransform();
            Glide.with(SettingActivity.this).load(user.getHeadimg())
                    .apply(requestOptions)
                    .into(user_photo);
        }
    }

    @Override
    public void initData() {
        super.initData();
    }

    @OnClick({R.id.top_left, R.id.top_image, R.id.ni_ceng, R.id.login,R.id.user_photo})
    void conlick(View view) {
        switch (view.getId()) {
            case R.id.ni_ceng:
                startActivity(new Intent(SettingActivity.this, ChangeNameActivity.class));
                break;
            case R.id.top_image:
            case R.id.top_left:
                SettingActivity.this.finish();
                break;
            case R.id.login:
//                obtainLoc();
                startActivity(new Intent(this,AllFinishActivity.class));
//                showDialogMsg("确定退出当前账号？");
                break;
            case R.id.user_photo:
                startActivity(new Intent(SettingActivity.this, GetLocationActivity.class));
                break;
        }
    }


    private void showDialogMsg(String names) {
        new ToastDialog(this, R.style.dialog, names, new ToastDialog.OnCloseListener() {
            @Override
            public void onClick(Dialog dialog, boolean confirm) {
                if (confirm) {
                    startActivity(new Intent(SettingActivity.this, LoginActivity.class));
                } else {

                }
                dialog.dismiss();
            }
        }).setTitle("提示").show();
    }

    //声明mlocationClient对象
    public AMapLocationClient mlocationClient;
    public AMapLocationClientOption mLocationOption = null;

    private void obtainLoc() {
//声明mLocationOption对象
        mlocationClient = new AMapLocationClient(this);
//初始化定位参数
        mLocationOption = new AMapLocationClientOption();
//设置定位监听
        mlocationClient.setLocationListener(mAMapLocationListener);
//设置定位模式为高精度模式，Battery_Saving为低功耗模式，Device_Sensors是仅设备模式
        mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
//设置定位间隔,单位毫秒,默认为2000ms
        mLocationOption.setInterval(2000);
//设置定位参数
        mlocationClient.setLocationOption(mLocationOption);
// 此方法为每隔固定时间会发起一次定位请求，为了减少电量消耗或网络流量消耗，
// 注意设置合适的定位时间的间隔（最小间隔支持为1000ms），并且在合适时间调用stopLocation()方法来取消定位请求
// 在定位结束后，在合适的生命周期调用onDestroy()方法
// 在单次定位情况下，定位无论成功与否，都无需调用stopLocation()方法移除请求，定位sdk内部会移除
//启动定位
        mlocationClient.startLocation();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mlocationClient.stopLocation();
        mlocationClient.onDestroy();
    }

    AMapLocationListener mAMapLocationListener = new AMapLocationListener() {

        @Override
        public void onLocationChanged(AMapLocation amapLocation) {
            if (amapLocation != null) {
                if (amapLocation.getErrorCode() == 0) {
                    //定位成功回调信息，设置相关消息
                    amapLocation.getLocationType();//获取当前定位结果来源，如网络定位结果，详见定位类型表
                    amapLocation.getLatitude();//获取纬度
                    amapLocation.getLongitude();//获取经度
                    amapLocation.getAccuracy();//获取精度信息
                    phone_t.setText("" + amapLocation.getLatitude() + "," + amapLocation.getLongitude());
                } else {
                    //显示错误信息ErrCode是错误码，errInfo是错误信息，详见错误码表。
                    Log.e("AmapError", "location Error, ErrCode:"
                            + amapLocation.getErrorCode() + ", errInfo:"
                            + amapLocation.getErrorInfo());
                }
            }
        }
    };


}

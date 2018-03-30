package com.cn.bent.sports.view.activity.youle;

import android.app.Dialog;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
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
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.MyLocationStyle;
import com.amap.api.services.core.LatLonPoint;
import com.cn.bent.sports.R;
import com.cn.bent.sports.api.BaseApi;
import com.cn.bent.sports.base.BaseActivity;
import com.cn.bent.sports.bean.LoginBase;
import com.cn.bent.sports.utils.Constants;
import com.cn.bent.sports.utils.DataUtils;
import com.cn.bent.sports.utils.SaveObjectUtils;
import com.cn.bent.sports.utils.SupportMultipleScreensUtil;
import com.cn.bent.sports.utils.ToastUtils;
import com.cn.bent.sports.view.activity.BitmapManager;
import com.cn.bent.sports.view.poupwindow.DoTaskPoupWindow;
import com.cn.bent.sports.view.poupwindow.TalkPoupWindow;
import com.cn.bent.sports.widget.OutGameDialog;
import com.minew.beacon.BeaconValueIndex;
import com.minew.beacon.BluetoothState;
import com.minew.beacon.MinewBeacon;
import com.minew.beacon.MinewBeaconManager;
import com.vondear.rxtools.RxActivityTool;
import com.vondear.rxtools.activity.ActivityScanerCode;
import com.vondear.rxtools.view.RxToast;
import com.zhl.network.RxManager;
import com.zhl.network.RxObserver;
import com.zhl.network.RxSchedulers;
import com.zhl.network.huiqu.HuiquRxTBFunction;
import com.cn.bent.sports.bean.LoginResult;
import com.zhl.network.huiqu.JavaRxFunction;

import java.util.ArrayList;
import java.util.List;

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
    private static final int SCAN_CODE = 101;
    private MinewBeaconManager mMinewBeaconManager;
    private LatLonPoint lp = new LatLonPoint(28.008977, 113.088063);//
    private DoTaskPoupWindow mopupWindow;
    private TalkPoupWindow soundWindow;
    private LoginBase user;
    private boolean isBlue = false;
    private static final int REQUEST_ENABLE_BT = 2;
    private String asd;


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
        user = SaveObjectUtils.getInstance(this).getObject(Constants.USER_INFO, null);
        mMinewBeaconManager = MinewBeaconManager.getInstance(this);
        checkBluetooth();
        if (aMap == null) {
            aMap = mapView.getMap();
        }
        aMap.getUiSettings().setZoomControlsEnabled(false);//去掉高德地图右下角隐藏的缩放按钮
        aMap.setOnMarkerClickListener(mMarkerClickListener);
        aMap.setOnCameraChangeListener(onCameraChangeListener);

        time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<String> mlist = new ArrayList<>();
                mlist.add("xiao");
                mlist.add("拖");
                mlist.add("坨");
                mlist.add("拓");
                DialogManager.getInstance(PlayActivity.this).showTaskOneFinishDialog("极限挑战", "28");
            }
        });

        score.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("getWebSocket", "onClick: "+asd);
                BaseApi.getJavaLoginDefaultService(PlayActivity.this).getWebSocket()
                        .map(new JavaRxFunction<String>())
                        .compose(RxSchedulers.<String>io_main())
                        .subscribe(new RxObserver<String>(PlayActivity.this, "getWebSocket", 2, false) {
                            @Override
                            public void onSuccess(int whichRequest, String s) {
                                Log.d("loginWithPass", " getWebSocket onSuccess: "+s);
                            }

                            @Override
                            public void onError(int whichRequest, Throwable e) {
                                Log.d("loginWithPass", "getWebSocket onError: " + e.getMessage());
                            }
                        });
            }
        });
        finish_task.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BaseApi.getJavaLoginService(PlayActivity.this).loginWithPass("password", "mdzz1", "123456")
                        .map(new HuiquRxTBFunction<LoginResult>())
                        .compose(RxSchedulers.<LoginResult>io_main())
                        .subscribe(new RxObserver<LoginResult>(PlayActivity.this, "loginWithPass", 1, false) {
                            @Override
                            public void onSuccess(int whichRequest, LoginResult loginResult) {
                                Log.d("loginWithPass", "onSuccess: " + loginResult.getAccess_token() + ",getRefresh_token:" + loginResult.getRefresh_token());
                               asd= loginResult.getAccess_token();

                            }

                            @Override
                            public void onError(int whichRequest, Throwable e) {
                                Log.d("loginWithPass", "onError: " + e.getMessage());
                            }
                        });
            }
        });

    }


    @Override
    public void initData() {
//        startLocation();
        setMarker();
    }

    private void setMarker() {
        View view = this.getLayoutInflater().inflate(R.layout.marker_dedai, null);
        TextView textView = (TextView) view.findViewById(R.id.text_num);
        textView.setText("8");
        SupportMultipleScreensUtil.init(this);
        SupportMultipleScreensUtil.scale(view);
        MarkerOptions markerOption = new MarkerOptions();
        markerOption.position(new LatLng(lp.getLatitude(), lp.getLongitude()));
        markerOption.draggable(true);//设置Marker可拖动
        markerOption.icon(BitmapManager.getInstance().getBitmapDescriptor4View(view));
        // 将Marker设置为贴地显示，可以双指下拉地图查看效果
        markerOption.setFlat(true);//设置marker平贴地图效果
        Marker marker = aMap.addMarker(markerOption);
        aMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(lp.getLatitude(), lp.getLongitude()), mCurrentZoom));

    }

    @OnClick({R.id.map_scan, R.id.map_more})
    void onClick(View view) {
        switch (view.getId()) {
            case R.id.map_scan:
                RxActivityTool.skipActivityForResult(this, ActivityScanerCode.class, SCAN_CODE);
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
     * check Bluetooth state
     */
    private void checkBluetooth() {
        BluetoothState bluetoothState = mMinewBeaconManager.checkBluetoothState();
        switch (bluetoothState) {
            case BluetoothStateNotSupported:
                ToastUtils.showShortToast(this, "手机不支持蓝牙");
                break;
            case BluetoothStatePowerOff:
                showBLEDialog();
                break;
            case BluetoothStatePowerOn:
                initListener();
                break;
        }
    }

    private void initListener() {
        if (DataUtils.isBlue(this)) {
            mMinewBeaconManager.startScan();
        }
        mMinewBeaconManager.setDeviceManagerDelegateListener(new com.minew.beacon.MinewBeaconManagerListener() {
            /**
             *   if the manager find some new beacon, it will call back this method.
             *
             *  @param minewBeacons  new beacons the manager scanned
             */
            @Override
            public void onAppearBeacons(List<MinewBeacon> minewBeacons) {

            }

            /**
             *  if a beacon didn't update data in 10 seconds, we think this beacon is out of rang, the manager will call back this method.
             *
             *  @param minewBeacons beacons out of range
             */
            @Override
            public void onDisappearBeacons(List<com.minew.beacon.MinewBeacon> minewBeacons) {
            }

            /**
             *  the manager calls back this method every 1 seconds, you can get all scanned beacons.
             *
             *  @param minewBeacons all scanned beacons
             */
            @Override
            public void onRangeBeacons(List<MinewBeacon> minewBeacons) {
                if (minewBeacons != null && minewBeacons.size() > 0) {
//                    String distance = String.valueOf(AMapUtils.calculateLineDistance(mStartPoint, mEndPoint));
                    for (MinewBeacon beacon : minewBeacons) {
                        String majer = beacon.getBeaconValue(BeaconValueIndex.MinewBeaconValueIndex_Major).getStringValue();
//                        for (int i = 0; i < mPointsList.size(); i++) {
//                            String jieguo = String.valueOf(mPointsList.get(i).getMajor());
//                            if (jieguo.equals(majer)) {
//                                if (!TextUtils.isEmpty(mPointsList.get(i).getMp3())) {
//                                    QueueManager.update(new QueueBean(mPointsList.get(i).getMp3()));
//                                    if (TaskCationManager.isPlay(i) || TaskCationManager.isNow(i)) {
//
//                                    } else {
//                                        chanVioce(i);
//                                    }
//                                    break;
//                                }
//                            }
//                        }
                    }
                }
            }

            /**
             *  the manager calls back this method when BluetoothStateChanged.
             *
             *  @param state BluetoothState
             */
            @Override
            public void onUpdateState(com.minew.beacon.BluetoothState state) {
                switch (state) {
                    case BluetoothStatePowerOn:
                        RxToast.info("蓝牙打开");
                        break;
                    case BluetoothStatePowerOff:
                        RxToast.info("蓝牙关闭");
                        break;
                }
            }
        });
    }

    private void showBLEDialog() {
        if (!isBlue) {
            Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableIntent, REQUEST_ENABLE_BT);
        }
    }

    /**
     * 退出dialog
     */
    private void OpenOutDialog() {
        OutGameDialog outGameDialog = new OutGameDialog(PlayActivity.this, R.style.dialog, new OutGameDialog.OnClickListener() {
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQUEST_ENABLE_BT:
                Log.d("dddd", "onActivityResult: " + mMinewBeaconManager.checkBluetoothState());
                if (mMinewBeaconManager.checkBluetoothState().equals(BluetoothState.BluetoothStatePowerOn)) {
                    isBlue = true;
                    initListener();
                }
                if (mMinewBeaconManager.checkBluetoothState().equals(BluetoothState.BluetoothStatePowerOff)) {
                    isBlue = false;
                    checkBluetooth();
                }
                break;
            case SCAN_CODE:
                RxToast.success("扫描成功");
                break;
        }

    }


    @Override
    public void onPause() {
        super.onPause();
        //在activity执行onPause时执行mMapView.onPause ()，暂停地图的绘制
        mapView.onPause();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        //在activity执行onSaveInstanceState时执行mMapView.onSaveInstanceState (outState)，保存地图当前的状态
        mapView.onSaveInstanceState(outState);
    }

    @Override
    public void onDestroy() {
        if (DataUtils.isBlue(PlayActivity.this) && mMinewBeaconManager != null) {
            mMinewBeaconManager.stopScan();
        }
        mapView.onDestroy();
        // 关闭定位图层
        aMap.setMyLocationEnabled(false);
        mapView.getMap().clear();
        mapView.onDestroy();
        mapView = null;
        if (mopupWindow != null && mopupWindow.isShowing()) {
            mopupWindow.dismiss();
        }
        super.onDestroy();
    }
}

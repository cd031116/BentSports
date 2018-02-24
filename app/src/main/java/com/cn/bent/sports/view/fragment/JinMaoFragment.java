package com.cn.bent.sports.view.fragment;

import android.app.Dialog;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps.AMap;
import com.amap.api.maps.AMapUtils;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.LatLngBounds;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.cn.bent.sports.R;
import com.cn.bent.sports.api.BaseApi;
import com.cn.bent.sports.base.BaseConfig;
import com.cn.bent.sports.base.BaseFragment;
import com.cn.bent.sports.bean.LoginBase;
import com.cn.bent.sports.bean.ReFreshEvent;
import com.cn.bent.sports.database.TaskCationBean;
import com.cn.bent.sports.database.TaskCationManager;
import com.cn.bent.sports.overlay.AMapUtil;
import com.cn.bent.sports.scan.CaptureActivity;
import com.cn.bent.sports.utils.Constants;
import com.cn.bent.sports.utils.DataUtils;
import com.cn.bent.sports.utils.SaveObjectUtils;
import com.cn.bent.sports.utils.ToastUtils;
import com.cn.bent.sports.view.activity.PlayWebViewActivity;
import com.cn.bent.sports.view.activity.RuleActivity;
import com.cn.bent.sports.view.activity.ZoomActivity;
import com.cn.bent.sports.widget.GameDialog;
import com.cn.bent.sports.widget.ToastDialog;
import com.minew.beacon.BluetoothState;
import com.minew.beacon.MinewBeaconManager;
import com.uuzuche.lib_zxing.activity.CodeUtils;
import com.zhl.network.RxObserver;
import com.zhl.network.RxSchedulers;
import com.zhl.network.huiqu.HuiquRxTBFunction;
import com.zhl.network.huiqu.HuiquTBResult;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by lyj on 2018/2/5 0005.
 * description
 */

public class JinMaoFragment extends BaseFragment implements AMap.OnMarkerClickListener {

    public static JinMaoFragment newInstance() {
        JinMaoFragment fragment = new JinMaoFragment();
//        Bundle bundle = new Bundle();
//        fragment.setArguments(bundle);
        return fragment;
    }

    @Bind(R.id.map_view)
    MapView mapView;

    @Bind(R.id.start_view)
    RelativeLayout start_view;
    @Bind(R.id.name_game)
    TextView name_game;
    @Bind(R.id.juli)
    TextView juli;
    @Bind(R.id.title)
    TextView title;
    @Bind(R.id.line_s)
    LinearLayout line_s;
    @Bind(R.id.go_task)
    TextView go_task;
    @Bind(R.id.ji_timer)
    TextView ji_timer;
    @Bind(R.id.jifen_t)
    TextView jifen_t;

    AMap aMap;
    private List<TaskCationBean> mLoction = new ArrayList<>();
    private List<Marker> mList = new ArrayList<Marker>();
    //声明AMapLocationClient类对象
    public AMapLocationClient mLocationClient = null;
    //声明AMapLocationClientOption对象
    public AMapLocationClientOption mLocationOption = null;
    private String latitude, longitude;
    private boolean isLocal = false;
    private boolean isFirstLoc = true;
    //----------------
    private LatLng mStartPoint;//起点，116.335891,39.942295
    private LatLng mEndPoint;//终点，116.481288,39.995576
    private final int ROUTE_TYPE_WALK = 3;
    private Marker noMarker;
    private static final int REQUEST_ENABLE_BT = 2;
    private static final int REQUEST_Scan = 12;
    private MinewBeaconManager mMinewBeaconManager;
    private String t_ids;
    private long times_s = 0;
    private Handler handler2;
    private LoginBase user;
    private boolean isBlue = false;
    //---------------------
    AMapLocationListener mAMapLocationListener = new AMapLocationListener() {
        @Override
        public void onLocationChanged(AMapLocation aMapLocation) {
            if (aMapLocation != null) {
                isLocal = true;
                if (aMapLocation.getErrorCode() == 0) {
                    //可在其中解析amapLocation获取相应内容。
                    latitude = String.valueOf(aMapLocation.getLatitude());
                    longitude = String.valueOf(aMapLocation.getLongitude());
                    mStartPoint = new LatLng(aMapLocation.getLatitude(), aMapLocation.getLongitude());
                    if (isFirstLoc) {
                        addLocaToMap();
                        isFirstLoc = false;
                    } else
                        addLocaismap();
                } else {
                    isLocal = false;
                    //定位失败时，可通过ErrCode（错误码）信息来确定失败的原因，errInfo是错误信息，详见错误码表。
                    //ToastUtils.showShortToast(getActivity(), "获取位置信息失败!");
                }
            }
        }
    };


    @Override
    protected int getLayoutId() {
        return R.layout.do_task_fragment;
    }

    @Override
    protected void initView(View view, Bundle savedInstanceState) {
        mLoction = TaskCationManager.getHistory();
        user = SaveObjectUtils.getInstance(getActivity()).getObject(Constants.USER_INFO, null);
        handler2 = new Handler();
        EventBus.getDefault().register(this);
        mapView.onCreate(savedInstanceState);
        mMinewBeaconManager = MinewBeaconManager.getInstance(getActivity());
        if (aMap == null) {
            aMap = mapView.getMap();
        }
        String path = getActivity().getFilesDir() + "/bent/sport.data";
        aMap.setCustomMapStylePath(path);
        aMap.setMapCustomEnable(true);//true 开启; false 关闭
        LatLng southwestLatLng = new LatLng(28.014501, 113.093844);
        LatLng northeastLatLng = new LatLng(28.01675, 113.096928);
//        LatLng southwestLatLng = new LatLng(28.10226, 112.977386);
//        LatLng northeastLatLng = new LatLng(28.122171, 113.004337);
        LatLngBounds latLngBounds = new LatLngBounds(southwestLatLng, northeastLatLng);
        aMap.setMapStatusLimits(latLngBounds);
        aMap.moveCamera(CameraUpdateFactory.newLatLngBounds(latLngBounds, 50));
        // 绑定 Marker 被点击事件
        aMap.setOnMarkerClickListener(this);
    }

    @Override
    protected void initData() {
        mLocationClient = new AMapLocationClient(getActivity());
//设置定位回调监听
        mLocationClient.setLocationListener(mAMapLocationListener);
//初始化AMapLocationClientOption对象
        mLocationOption = new AMapLocationClientOption();
        //设置定位模式为AMapLocationMode.Hight_Accuracy，高精度模式。
        mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
        //设置定位模式为AMapLocationMode.Device_Sensors，仅设备模式。
//        mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Device_Sensors);
        mLocationOption.setInterval(1000 * 3);
        //给定位客户端对象设置定位参数
        mLocationClient.setLocationOption(mLocationOption);
        //启动定位
        mLocationOption.setOnceLocation(false);
        mLocationClient.startLocation();
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        for (int i = 0; i < mList.size(); i++) {
            if (marker.equals(mList.get(i))) {
                t_ids = String.valueOf(mLoction.get(i).gettId());
                showDialogMsg("是否前往该地点", i);
            }
        }
        return true;
    }

    private void setcheck() {
        for (TaskCationBean hs : mLoction) {
            hs.setCheck(false);
        }
        aMap.clear();
    }


    private void addMarkersToMap() {
        ArrayList<MarkerOptions> markerOptionlst = new ArrayList<MarkerOptions>();
        if (mLoction != null) {
            if (mList != null) {
                mList.clear();
            }
            for (TaskCationBean hs : mLoction) {
                if (!hs.isshow()) {
                    String longitude = hs.getLongitude();
                    String latitude = hs.getLatitude();
                    double dlat = Double.valueOf(latitude).doubleValue();//纬度
                    double dlong = Double.valueOf(longitude).doubleValue();//经度
                    MarkerOptions markerOption = new MarkerOptions();
                    markerOption.position(new LatLng(dlat, dlong));
                    markerOption.title(hs.getName());
                    markerOption.draggable(false);
                    if (hs.isCheck()) {
                        markerOption.icon(
                                BitmapDescriptorFactory.fromBitmap(BitmapFactory
                                        .decodeResource(getResources(),
                                                R.drawable.zb_icon)));
                    } else {
                        markerOption.icon(
                                BitmapDescriptorFactory.fromBitmap(BitmapFactory
                                        .decodeResource(getResources(),
                                                R.drawable.zuobiao)));
                    }
                    markerOptionlst.add(markerOption);
                    aMap.addMarker(markerOption);
                    mList.add(aMap.addMarker(markerOption));
                }
            }
        }
    }

    /**
     * 在地图上添加marker
     */
    private void addLocaismap() {
        if (TextUtils.isEmpty(latitude)) {
            return;
        }

        if (noMarker != null) {
            noMarker.remove();
        }
        LatLng latlng = new LatLng(Double.valueOf(latitude), Double.valueOf(longitude));
        noMarker = aMap.addMarker(new MarkerOptions()
                .position(latlng)
                .icon(BitmapDescriptorFactory.fromBitmap(BitmapFactory
                        .decodeResource(getResources(), R.drawable.dangqwz)))
                .draggable(true));
        aMap.setMyLocationEnabled(false);
        setviewS();
    }

    /**
     * 在地图上添加marker
     */
    private void addLocaToMap() {
        if (TextUtils.isEmpty(latitude)) {
            return;
        }
        aMap.clear();
        LatLng latlng = new LatLng(Double.valueOf(latitude), Double.valueOf(longitude));
        noMarker = aMap.addMarker(new MarkerOptions()
                .position(latlng)
                .icon(BitmapDescriptorFactory.fromBitmap(BitmapFactory
                        .decodeResource(getResources(), R.drawable.dangqwz)))
                .draggable(true));
//        aMap.moveCamera(CameraUpdateFactory.changeLatLng(latlng));
//        aMap.moveCamera(CameraUpdateFactory.zoomTo(15));
        aMap.setMyLocationEnabled(false);
        addMarkersToMap();
    }


    @OnClick({R.id.shuoming, R.id.dao_lan, R.id.go_task})
    void onclik(View v) {
        switch (v.getId()) {
            case R.id.shuoming:
                startActivity(new Intent(getActivity(), RuleActivity.class));
                break;
            case R.id.dao_lan:
                startActivity(new Intent(getActivity(), ZoomActivity.class));
                break;
            case R.id.go_task:
                if ("1".endsWith(t_ids)) {
                    showDialogMsg(getResources().getString(R.string.hong_bao));
                }
                if ("2".endsWith(t_ids)) {
                    showDialogMsg(getResources().getString(R.string.no_die));
                }
                if ("3".endsWith(t_ids)) {
                    showDialogMsg(getResources().getString(R.string.deng_long));
                }
                if ("4".endsWith(t_ids)) {
                    showDialogMsg(getResources().getString(R.string.chou_qian));
                }
                if ("5".endsWith(t_ids)) {
                    showDialogMsg(getResources().getString(R.string.cai_deng_mi));
                }
                if ("6".endsWith(t_ids)) {
                    showDialogMsg(getResources().getString(R.string.xiong_chumo));
                }
                break;
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(ReFreshEvent event) {
        mLoction = TaskCationManager.getHistory();
        BaseConfig bf = BaseConfig.getInstance(getActivity());
        if (mLoction.size() <= 0) {
            handler2.removeCallbacks(runnable2);
            bf.setLongValue(Constants.IS_TIME, 0);
            ji_timer.setText("已通关");
        }
    }

    /**
     * 方法必须重写
     */
    @Override
    public void onResume() {
        super.onResume();
        BaseConfig bf = BaseConfig.getInstance(getActivity());
        times_s = bf.getLongValue(Constants.IS_TIME, 0);
        if (times_s > 0) {
            setTimes();
        }
        if (mLoction.size() <= 0) {
            bf.setLongValue(Constants.IS_TIME, 0);
            ji_timer.setText("已通关");
        }
        mapView.onResume();
        addLocaToMap();
        LoginBase user = SaveObjectUtils.getInstance(getActivity()).getObject(Constants.USER_INFO, null);
        if (user.getScore() != null) {
            jifen_t.setText(user.getScore());
        }
    }


    private void setTimes() {
        handler2.postDelayed(runnable2, 1000);
    }


    Runnable runnable2 = new Runnable() {
        @Override
        public void run() {
            handler2.postDelayed(this, 1000);
            if (((System.currentTimeMillis() - times_s) / 1000) >= 2 * 60 * 60) {
                handler2.removeCallbacks(runnable2);
                ji_timer.setText("02.00.00");
            } else {
                ji_timer.setText(DataUtils.getDateToTime(System.currentTimeMillis() - times_s));
            }

        }
    };


    private void showDialogMsg(String names) {
        new GameDialog(getActivity(), R.style.dialog, new GameDialog.OnCloseListener() {
            @Override
            public void onClick(Dialog dialog, boolean confirm) {
                if (confirm) {
                    mEndPoint = null;
                    start_view.setVisibility(View.GONE);
                    go_task.setVisibility(View.GONE);
                    start_view.setVisibility(View.GONE);
                    if ("5".endsWith(t_ids)) {
                        Intent intent = new Intent(getActivity(), CaptureActivity.class);
                        startActivityForResult(intent, REQUEST_Scan);
                    } else {
                        Intent intent = new Intent(getActivity(), PlayWebViewActivity.class);
                        intent.putExtra("gameId", t_ids);
                        startActivity(intent);
                    }
                } else {

                }
                dialog.dismiss();
            }
        }).setTitle(names).show();
    }

    /**
     * 方法必须重写
     */
    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();

    }

    @Override
    public void onStop() {
        super.onStop();
        if (handler2 != null) {
            handler2.removeCallbacks(runnable2);
        }
    }

    /**
     * 方法必须重写
     */
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    /**
     * 方法必须重写
     */
    @Override
    public void onDestroy() {
        super.onDestroy();
        if (handler2 != null) {
            handler2.removeCallbacks(runnable2);
        }
        mapView.onDestroy();
        if (mLocationClient != null) {
            mLocationClient.stopLocation();
            mLocationClient.onDestroy();
        }
        EventBus.getDefault().unregister(this);
    }


    private void setviewS() {
        if (mStartPoint == null || mEndPoint == null) {

        } else {
            String distance = String.valueOf(AMapUtils.calculateLineDistance(mStartPoint, mEndPoint));
            juli.setText(AMapUtil.getFriendlyLength((int) (Double.parseDouble(distance))));
            if ((int) (Double.parseDouble(distance)) <= 20) {
                //打开蓝牙
                checkBluetooth();
            }
        }
    }


    private void setview() {

        if (mStartPoint == null || mEndPoint == null) {

        } else {
            start_view.setVisibility(View.VISIBLE);
            line_s.setVisibility(View.VISIBLE);
            go_task.setVisibility(View.GONE);
            if ("1".endsWith(t_ids)) {
                name_game.setText("红包雨");
            }
            if ("2".endsWith(t_ids)) {
                name_game.setText("拯救小拓");
            }
            if ("3".endsWith(t_ids)) {
                name_game.setText("点亮所有灯笼");
            }
            if ("4".endsWith(t_ids)) {
                name_game.setText("财神庙求签");
            }
            if ("5".endsWith(t_ids)) {
                name_game.setText("猜灯谜");
            }
            if ("6".endsWith(t_ids)) {
                name_game.setText("愤怒的小鸟");
            }
            String distance = String.valueOf(AMapUtils.calculateLineDistance(mStartPoint, mEndPoint));
            juli.setText(AMapUtil.getFriendlyLength((int) (Double.parseDouble(distance))));
            if ((int) (Double.parseDouble(distance)) <= 20) {
                //打开蓝牙
                checkBluetooth();
            }
        }
    }


    private void showDialogMsg(String names, final int position) {
        new ToastDialog(getActivity(), R.style.dialog, names, new ToastDialog.OnCloseListener() {
            @Override
            public void onClick(Dialog dialog, boolean confirm) {
                if (confirm) {
                    setcheck();
                    isBlue=false;
                    mLoction.get(position).setCheck(true);
                    mEndPoint = null;
                    mEndPoint = new LatLng(Double.valueOf(mLoction.get(position).getLatitude()).doubleValue(),
                            Double.valueOf(mLoction.get(position).getLongitude()).doubleValue());
                    addLocaToMap();
                    setview();
                    if (times_s <= 0) {
                        if (mStartPoint != null) {
                            login();
                            BaseConfig bgs = BaseConfig.getInstance(getActivity());
                            bgs.setLongValue(Constants.IS_TIME, System.currentTimeMillis());
                            times_s = bgs.getLongValue(Constants.IS_TIME, 0);
                            setTimes();
                        }
                    }
                } else {

                }
                dialog.dismiss();
            }
        }).setTitle("提示").show();
    }

    //-------------------------------通知后台
    private void login() {
        BaseApi.getDefaultService(getActivity()).startGame(user.getMember_id())
                .map(new HuiquRxTBFunction<HuiquTBResult>())
                .compose(RxSchedulers.<HuiquTBResult>io_main())
                .subscribe(new RxObserver<HuiquTBResult>(getActivity(), "changeName", 1, false) {
                    @Override
                    public void onSuccess(int whichRequest, HuiquTBResult info) {
                    }

                    @Override
                    public void onError(int whichRequest, Throwable e) {
                        dismissAlert();

                    }
                });
    }

    //-------------------------------------------蓝牙

    /**
     * check Bluetooth state
     */
    private void checkBluetooth() {
        Log.d("aaaa", "checkBluetooth: 1");
        BluetoothState bluetoothState = mMinewBeaconManager.checkBluetoothState();
        Log.d("aaaa", "checkBluetooth: 2" + bluetoothState);
        switch (bluetoothState) {
            case BluetoothStateNotSupported:
                ToastUtils.showShortToast(getActivity(), "手机不支持蓝牙");
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

        mMinewBeaconManager.startScan();
        mMinewBeaconManager.setDeviceManagerDelegateListener(new com.minew.beacon.MinewBeaconManagerListener() {
            /**
             *   if the manager find some new beacon, it will call back this method.
             *
             *  @param minewBeacons  new beacons the manager scanned
             */
            @Override
            public void onAppearBeacons(List<com.minew.beacon.MinewBeacon> minewBeacons) {

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
            public void onRangeBeacons(List<com.minew.beacon.MinewBeacon> minewBeacons) {
                if (minewBeacons != null && minewBeacons.size() > 0) {
                    line_s.setVisibility(View.GONE);
                    go_task.setVisibility(View.VISIBLE);
                    //弹游戏
                    try {
                        BluetoothAdapter.getDefaultAdapter().disable();
                    } catch (Exception e) {
                    }
                    mEndPoint = null;
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
                        Toast.makeText(getActivity(), "蓝牙打开", Toast.LENGTH_SHORT).show();
                        break;
                    case BluetoothStatePowerOff:
                        Toast.makeText(getActivity(), "蓝牙关闭", Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        });
    }

    private void showBLEDialog() {
        if (!isBlue) {
            Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableIntent, REQUEST_ENABLE_BT);
            isBlue = true;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQUEST_ENABLE_BT:
                if (mMinewBeaconManager.checkBluetoothState().equals(BluetoothState.BluetoothStatePowerOn)) {
                    isBlue = false;
                    initListener();
                }
                break;
            case REQUEST_Scan:
                if (null != data) {
                    Bundle bundle = data.getExtras();
                    if (bundle == null) {
                        return;
                    }
                    if (bundle.getInt(CodeUtils.RESULT_TYPE) == CodeUtils.RESULT_SUCCESS) {
                        String result = bundle.getString(CodeUtils.RESULT_STRING);
                        if ("B33832EF5EFF3EFF30B1B646B6F2410F".endsWith(result)) {
                            Intent intent = new Intent(getActivity(), PlayWebViewActivity.class);
                            intent.putExtra("gameId", t_ids);
                            startActivity(intent);
                            mMinewBeaconManager.stopScan();
                        } else {
                            ToastUtils.showShortToast(getActivity(), "二维码不匹配");
                        }

                    } else if (bundle.getInt(CodeUtils.RESULT_TYPE) == CodeUtils.RESULT_FAILED) {
                        ToastUtils.showShortToast(getActivity(), "二维码不匹配");
                    }
                } else {
                    ToastUtils.showShortToast(getActivity(), "无结果");
                }
                break;
        }
    }


}

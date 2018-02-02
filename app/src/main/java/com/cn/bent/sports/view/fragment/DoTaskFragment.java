package com.cn.bent.sports.view.fragment;

import android.app.Dialog;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.SystemClock;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Chronometer;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.services.core.AMapException;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.route.BusRouteResult;
import com.amap.api.services.route.DriveRouteResult;
import com.amap.api.services.route.RideRouteResult;
import com.amap.api.services.route.RouteSearch;
import com.amap.api.services.route.WalkPath;
import com.amap.api.services.route.WalkRouteResult;
import com.cn.bent.sports.R;
import com.cn.bent.sports.base.BaseConfig;
import com.cn.bent.sports.base.BaseFragment;
import com.cn.bent.sports.bean.LoginBase;
import com.cn.bent.sports.bean.ReFreshEvent;
import com.cn.bent.sports.database.TaskCationBean;
import com.cn.bent.sports.database.TaskCationManager;
import com.cn.bent.sports.ibeacon.UserRssi;
import com.cn.bent.sports.overlay.AMapUtil;
import com.cn.bent.sports.overlay.WalkRouteOverlay;
import com.cn.bent.sports.utils.Constants;
import com.cn.bent.sports.utils.SaveObjectUtils;
import com.cn.bent.sports.utils.ToastUtils;
import com.cn.bent.sports.view.activity.PlayWebViewActivity;
import com.cn.bent.sports.view.activity.RuleActivity;
import com.cn.bent.sports.view.activity.ZoomActivity;
import com.cn.bent.sports.widget.GameDialog;
import com.cn.bent.sports.widget.ToastDialog;
import com.minew.beacon.BluetoothState;
import com.minew.beacon.MinewBeaconManager;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by lyj on 2018/1/29 0029.
 * description
 */

public class DoTaskFragment extends BaseFragment implements AMap.OnMarkerClickListener, RouteSearch.OnRouteSearchListener {
    public static DoTaskFragment newInstance() {
        DoTaskFragment fragment = new DoTaskFragment();
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
    Chronometer ji_timer;
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
    private WalkRouteResult mWalkRouteResult;
    //----------------
    private RouteSearch mRouteSearch;
    private LatLonPoint mStartPoint;//起点，116.335891,39.942295
    private LatLonPoint mEndPoint;//终点，116.481288,39.995576
    private final int ROUTE_TYPE_WALK = 3;
    private Marker noMarker;
    UserRssi comp = new UserRssi();
    private boolean isWark = false;
    private static final int REQUEST_ENABLE_BT = 2;
    private MinewBeaconManager mMinewBeaconManager;
    private String t_ids;

    //---------------------
    AMapLocationListener mAMapLocationListener = new AMapLocationListener() {
        @Override
        public void onLocationChanged(AMapLocation aMapLocation) {
            if (aMapLocation != null) {
                isLocal = true;
                if (aMapLocation.getErrorCode() == 0) {
                    //可在其中解析amapLocation获取相应内容。
                    isFirstLoc = false;
                    latitude = String.valueOf(aMapLocation.getLatitude());
                    longitude = String.valueOf(aMapLocation.getLongitude());
                    mStartPoint = new LatLonPoint(aMapLocation.getLatitude(), aMapLocation.getLongitude());
                    setmLoctions();
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
        EventBus.getDefault().register(this);
        mapView.onCreate(savedInstanceState);
        mMinewBeaconManager = MinewBeaconManager.getInstance(getActivity());
        if (aMap == null) {
            aMap = mapView.getMap();
        }
        String path = getActivity().getFilesDir() + "/bent/sport.data";
        Log.d("kkkk", "initView: " + path);
        aMap.setCustomMapStylePath(path);
        aMap.setMapCustomEnable(true);//true 开启; false 关闭
        // 绑定 Marker 被点击事件
        aMap.setOnMarkerClickListener(this);
        mRouteSearch = new RouteSearch(getActivity());
        mRouteSearch.setRouteSearchListener(this);
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
        //给定位客户端对象设置定位参数
        mLocationClient.setLocationOption(mLocationOption);
        //设置定位模式为AMapLocationMode.Device_Sensors，仅设备模式。
        mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Device_Sensors);
        mLocationOption.setInterval(1000 * 10);
        //启动定位
        mLocationOption.setOnceLocation(true);
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

    private void setmLoctions() {
        if (noMarker != null) {
            noMarker.remove();
        }
        if (TextUtils.isEmpty(latitude)) {
            return;
        }
        if (isWark) {
            aMap.clear();
            searchRouteResult(ROUTE_TYPE_WALK, RouteSearch.WalkDefault);
        }
        LatLng latlng = new LatLng(Double.valueOf(latitude), Double.valueOf(longitude));
        noMarker = aMap.addMarker(new MarkerOptions()
                .position(latlng)
                .icon(BitmapDescriptorFactory.fromBitmap(BitmapFactory
                        .decodeResource(getResources(), R.drawable.dangqwz)))
                .draggable(true));
        if (!isWark) {
            aMap.moveCamera(CameraUpdateFactory.changeLatLng(latlng));
            aMap.moveCamera(CameraUpdateFactory.zoomTo(15));
        }
        aMap.setMyLocationEnabled(false);
        addMarkersToMap();
    }

    /**
     * 在地图上添加marker
     */
    private void addLocaToMap() {
        if (noMarker != null) {
            noMarker.remove();
        }
        if (TextUtils.isEmpty(latitude)) {
            return;
        }
//
        LatLng latlng = new LatLng(Double.valueOf(latitude), Double.valueOf(longitude));
        noMarker = aMap.addMarker(new MarkerOptions()
                .position(latlng)
                .icon(BitmapDescriptorFactory.fromBitmap(BitmapFactory
                        .decodeResource(getResources(), R.drawable.dangqwz)))
                .draggable(true));
        if (!isWark) {
            aMap.moveCamera(CameraUpdateFactory.changeLatLng(latlng));
            aMap.moveCamera(CameraUpdateFactory.zoomTo(15));
        }
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
                    showDialogMsg("开始游戏");
                }
                if ("2".endsWith(t_ids)) {
                    showDialogMsg("开始游戏");
                }
                if ("3".endsWith(t_ids)) {
                    showDialogMsg("开始游戏");
                }
                if ("4".endsWith(t_ids)) {
                    showDialogMsg("开始游戏");
                }
                if ("5".endsWith(t_ids)) {
                    showDialogMsg("开始游戏");
                }
                if ("6".endsWith(t_ids)) {
                    showDialogMsg("开始游戏");
                }
                break;
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(ReFreshEvent event) {
        aMap.clear();
    }

    /**
     * 方法必须重写
     */
    @Override
    public void onResume() {
        super.onResume();
        mLoction = TaskCationManager.getHistory();
        if(mLoction.size()<=0){
            BaseConfig bf = BaseConfig.getInstance(getActivity());
            bf.setLongValue(Constants.IS_TIME, 0);
        }
        //打开蓝牙
        checkBluetooth();
        mapView.onResume();
        addLocaToMap();
        LoginBase user = SaveObjectUtils.getInstance(getActivity()).getObject(Constants.USER_INFO, null);
        if (user.getScore() != null) {
            jifen_t.setText(user.getScore());
        }
        setTimes();
    }

    private void setTimes() {
        BaseConfig bf = BaseConfig.getInstance(getActivity());
        final long times = bf.getLongValue(Constants.IS_TIME, 0);
        if (times > 0) {
            if (ji_timer != null) {
                ji_timer.start();
            }
            ji_timer.setBase(times);//计时器清零
            int hour = (int) ((SystemClock.elapsedRealtime() - ji_timer.getBase()) / 1000 / 60 / 60);
            ji_timer.setFormat("0" + String.valueOf(hour) + ":%s");
            ji_timer.start();
        }else {
            ji_timer.setBase(SystemClock.elapsedRealtime());
        }
    }

    private void showDialogMsg(String names) {
        new GameDialog(getActivity(), R.style.dialog, new GameDialog.OnCloseListener() {
            @Override
            public void onClick(Dialog dialog, boolean confirm) {
                if (confirm) {
                    Intent intent = new Intent(getActivity(), PlayWebViewActivity.class);
                    intent.putExtra("gameId", t_ids);
                    startActivity(intent);
                    mMinewBeaconManager.stopScan();
                    line_s.setVisibility(View.VISIBLE);
                    go_task.setVisibility(View.GONE);
                    start_view.setVisibility(View.GONE);
                    isWark = false;
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
        mapView.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    /**
     * 开始搜索路径规划方案
     */
    public void searchRouteResult(int routeType, int mode) {
        if (mStartPoint == null) {
            ToastUtils.showShortToast(getActivity(), "定位中，稍后再试...");
            return;
        }
        final RouteSearch.FromAndTo fromAndTo = new RouteSearch.FromAndTo(
                mStartPoint, mEndPoint);
        if (routeType == ROUTE_TYPE_WALK) {// 步行路径规划
            aMap.removecache();
            RouteSearch.WalkRouteQuery query = new RouteSearch.WalkRouteQuery(fromAndTo, mode);
            mRouteSearch.calculateWalkRouteAsyn(query);// 异步路径规划步行模式查询
        }
    }


    @Override
    public void onBusRouteSearched(BusRouteResult busRouteResult, int i) {

    }

    @Override
    public void onDriveRouteSearched(DriveRouteResult driveRouteResult, int i) {

    }

    WalkRouteOverlay walkRouteOverlay;

    @Override
    public void onWalkRouteSearched(WalkRouteResult result, int errorCode) {
        if (errorCode == AMapException.CODE_AMAP_SUCCESS) {
            if (result != null && result.getPaths() != null) {
                if (result.getPaths().size() > 0) {
                    mWalkRouteResult = result;
                    final WalkPath walkPath = result.getPaths()
                            .get(0);
                    WalkRouteOverlay walkRouteOverlay = new WalkRouteOverlay(
                            getActivity(), aMap, walkPath,
                            mWalkRouteResult.getStartPos(),
                            mWalkRouteResult.getTargetPos());
                    walkRouteOverlay.removeFromMap();
                    walkRouteOverlay.addToMap();
                    walkRouteOverlay.zoomToSpan();
                    isWark = true;
                    int dis = (int) walkPath.getDistance();
                    setview(dis);
                } else if (result != null && result.getPaths() == null) {
                    ToastUtils.showShortToast(getActivity(), "无结果");
                }
            } else {
                ToastUtils.showShortToast(getActivity(), "无结果");
            }
        } else {
            ToastUtils.showShortToast(getActivity(), "无结果");
        }
    }

    @Override
    public void onRideRouteSearched(RideRouteResult rideRouteResult, int errorCode) {

    }

    private void setview(int distance) {
        start_view.setVisibility(View.VISIBLE);
        juli.setText(AMapUtil.getFriendlyLength(distance));
        if ("1".endsWith(t_ids)) {
            name_game.setText("红包雨");
        }
        if ("2".endsWith(t_ids)) {
            name_game.setText("财神庙求签");
        }
        if ("3".endsWith(t_ids)) {
            name_game.setText("猜灯谜");
        }
        if ("4".endsWith(t_ids)) {
            name_game.setText("拯救小托");
        }
        if ("5".endsWith(t_ids)) {
            name_game.setText("");
        }
        if ("6".endsWith(t_ids)) {
            name_game.setText("点亮所有灯笼");
        }


    }

    BaseConfig bgs = BaseConfig.getInstance(getActivity());
    long times = bgs.getLongValue(Constants.IS_TIME, 0);

    private void showDialogMsg(String names, final int position) {
        new ToastDialog(getActivity(), R.style.dialog, names, new ToastDialog.OnCloseListener() {
            @Override
            public void onClick(Dialog dialog, boolean confirm) {
                if (confirm) {
                    setcheck();
                    mLoction.get(position).setCheck(true);
                    mEndPoint = null;
                    mEndPoint = new LatLonPoint(Double.valueOf(mLoction.get(position).getLatitude()).doubleValue(),
                            Double.valueOf(mLoction.get(position).getLongitude()).doubleValue());
                    aMap.clear();
                    searchRouteResult(ROUTE_TYPE_WALK, RouteSearch.WalkDefault);
                    addLocaToMap();
                    if (times <= 0) {
                        if (mStartPoint != null) {
                            bgs.setLongValue(Constants.IS_TIME, SystemClock.elapsedRealtime());
                            setTimes();
                        }
                    }
                } else {

                }
                dialog.dismiss();
            }
        }).setTitle("提示").show();
    }

    //-------------------------------------------蓝牙

    /**
     * check Bluetooth state
     */
    private void checkBluetooth() {
        BluetoothState bluetoothState = mMinewBeaconManager.checkBluetoothState();
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
                Log.e("dasd", "dasd: " + minewBeacons.size());
                if (minewBeacons != null && minewBeacons.size() > 0) {
                    line_s.setVisibility(View.GONE);
                    go_task.setVisibility(View.VISIBLE);
                    //弹游戏
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
                        Toast.makeText(getActivity(), "蓝牙关闭", Toast.LENGTH_SHORT).show();
                        break;
                    case BluetoothStatePowerOff:
                        Toast.makeText(getActivity(), "蓝牙打开", Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        });
    }

    private void showBLEDialog() {
        ToastUtils.showShortToast(getActivity(), "已到达目的地附近,请打开蓝牙");
        Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
        startActivityForResult(enableIntent, REQUEST_ENABLE_BT);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQUEST_ENABLE_BT:
                initListener();
                break;
        }
    }

}
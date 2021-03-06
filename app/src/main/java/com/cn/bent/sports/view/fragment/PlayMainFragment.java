package com.cn.bent.sports.view.fragment;

import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.location.AMapLocationClient;
import com.amap.api.maps.AMap;
import com.amap.api.maps.AMapUtils;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.LatLngBounds;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.MyLocationStyle;
import com.cn.bent.sports.R;
import com.cn.bent.sports.base.BaseConfig;
import com.cn.bent.sports.base.BaseFragment;
import com.cn.bent.sports.bean.MajorBean;
import com.cn.bent.sports.bean.MapDot;
import com.cn.bent.sports.bean.PlayMapBean;
import com.cn.bent.sports.bean.RailBean;
import com.cn.bent.sports.bean.ReFreshEvent;
import com.cn.bent.sports.utils.Constants;
import com.cn.bent.sports.utils.DataUtils;
import com.cn.bent.sports.utils.SaveObjectUtils;
import com.cn.bent.sports.view.activity.ArActivity;
import com.cn.bent.sports.view.activity.OfflineActivity;
import com.cn.bent.sports.view.activity.PlayWebViewActivity;
import com.cn.bent.sports.view.activity.RuleActivity;
import com.cn.bent.sports.view.activity.ZoomActivity;
import com.cn.bent.sports.view.poupwindow.DoTaskPoupWindow;
import com.cn.bent.sports.view.poupwindow.TalkPoupWindow;
import com.google.zxing.Result;
import com.minew.beacon.BeaconValueIndex;
import com.minew.beacon.BluetoothState;
import com.minew.beacon.MinewBeacon;
import com.minew.beacon.MinewBeaconManager;
import com.vondear.rxtools.activity.ActivityScanerCode;
import com.vondear.rxtools.interfaces.OnRxScanerListener;
import com.vondear.rxtools.view.RxToast;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by lyj on 2018/2/26 0026.
 * description
 */

public class PlayMainFragment extends BaseFragment implements AMap.OnMarkerClickListener, AMap.OnMyLocationChangeListener {
    public static PlayMainFragment newInstance() {
        PlayMainFragment fragment = new PlayMainFragment();
//        Bundle bundle = new Bundle();
//        fragment.setArguments(bundle);
        return fragment;
    }

    @Bind(R.id.map_view)
    MapView mapView;
    @Bind(R.id.ji_timer)
    TextView ji_timer;
    @Bind(R.id.jifen_t)
    TextView jifen_t;
    @Bind(R.id.qiehuan)
    ImageView daodan;

    AMap aMap;
    private List<Marker> mList = new ArrayList<Marker>();
    //声明AMapLocationClient类对象
    public AMapLocationClient mLocationClient = null;
    //声明AMapLocationClientOption对象
    //----------------
    private LatLng mStartPoint;//起点，116.335891,39.942295
    private LatLng mEndPoint;//终点，116.481288,39.995576
    private static final int REQUEST_ENABLE_BT = 2;
    private static final int REQUEST_Scan = 12;
    private MinewBeaconManager mMinewBeaconManager;
    private int t_ids = -1;
    private long times_s = 0;
    private Handler handler2;
    private boolean isBlue = false;
    private List<MapDot> place_list = new ArrayList<>();
    private DoTaskPoupWindow mopupWindow;
    private TalkPoupWindow soundWindow;
    private boolean isGame = false;
    //---------------------


    @Override
    protected int getLayoutId() {
        return R.layout.do_task_fragment;
    }

    @Override
    protected void initView(View view, Bundle savedInstanceState) {
        EventBus.getDefault().register(this);
        handler2 = new Handler();
        mapView.onCreate(savedInstanceState);
        mMinewBeaconManager = MinewBeaconManager.getInstance(getActivity());
        checkBluetooth();
        if (aMap == null) {
            aMap = mapView.getMap();
        }
        String path = getActivity().getFilesDir() + "/bent/sport.data";
        aMap.setCustomMapStylePath(path);
        aMap.setMapCustomEnable(true);//true 开启; false 关闭
        RailBean railBean = SaveObjectUtils.getInstance(getActivity()).getObject(Constants.DOT_INFO, null);
        LatLng southwestLatLng = new LatLng(Double.valueOf(railBean.getFence().getDot_lat()).doubleValue(), Double.valueOf(railBean.getFence().getDot_long()).doubleValue());
        LatLng northeastLatLng = new LatLng(Double.valueOf(railBean.getFence().getOther_dot_lat()).doubleValue(), Double.valueOf(railBean.getFence().getOther_dot_long()).doubleValue());
//        LatLng southwestLatLng = new LatLng(28.084042,112.956461);
//        LatLng northeastLatLng = new LatLng(28.157773,113.019118);
        LatLngBounds latLngBounds = new LatLngBounds(southwestLatLng, northeastLatLng);
        aMap.setMapStatusLimits(latLngBounds);
        aMap.moveCamera(CameraUpdateFactory.newLatLngBounds(latLngBounds, 50));
        // 绑定 Marker 被点击事件
        aMap.setOnMarkerClickListener(this);
    }

    @Override
    protected void initData() {
        addLocaToMap();
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        for (int i = 0; i < mList.size(); i++) {
            if (marker.equals(mList.get(i))) {
                if (place_list.get(i).getType().equals("2")) {
                    isGame = false;
                    shousoundPoup(place_list.get(i).getName(), place_list.get(i).getMp3(), place_list.get(i).getDesc(), i);
                    break;
                } else {
                    if (place_list.get(i).getIs_play().equals("0")) {
                        t_ids = i;
                        isGame = true;
                        setcheck();
                        place_list.get(t_ids).setCheck(true);
                        addMarkersToMap();
                        mEndPoint = null;
                        mEndPoint = new LatLng(Double.valueOf(place_list.get(i).getLatitude()).doubleValue(),
                                Double.valueOf(place_list.get(i).getLongitude()).doubleValue());
                        shouPoup(place_list.get(t_ids).getGame_name(), false, place_list.get(t_ids).getGame_id(), place_list.get(t_ids).getMp3());
                        break;
                    } else {
                        isGame = false;
                    }
                }
            }
        }
        return true;
    }

    private void setcheck() {
        for (MapDot hs : place_list) {
            hs.setCheck(false);
        }
    }


    private void addMarkersToMap() {
        aMap.clear();
        ArrayList<MarkerOptions> markerOptionlst = new ArrayList<MarkerOptions>();
        if (place_list != null) {
            if (mList != null) {
                mList.clear();
            }
            for (MapDot hs : place_list) {
                String longitude = hs.getLongitude();
                String latitude = hs.getLatitude();
                double dlat = Double.valueOf(latitude).doubleValue();//纬度
                double dlong = Double.valueOf(longitude).doubleValue();//经度
                MarkerOptions markerOption = new MarkerOptions();
                markerOption.position(new LatLng(dlat, dlong));
                markerOption.title(hs.getName());
                markerOption.draggable(false);
                if (hs.isCheck() && hs.getIs_play().equals("0") && !hs.getType().equals("2")) {
                    markerOption.icon(
                            BitmapDescriptorFactory.fromBitmap(BitmapFactory
                                    .decodeResource(getResources(),
                                            R.drawable.zb_icon)));
                } else if (hs.getIs_play().equals("0") && !hs.isCheck() && !hs.getType().equals("2")) {
                    markerOption.icon(
                            BitmapDescriptorFactory.fromBitmap(BitmapFactory
                                    .decodeResource(getResources(),
                                            R.drawable.zuobiao)));
                } else if (hs.getIs_play().equals("1") && !hs.getType().equals("2")) {
                    markerOption.icon(
                            BitmapDescriptorFactory.fromBitmap(BitmapFactory
                                    .decodeResource(getResources(),
                                            R.drawable.completed)));

                } else {
                    markerOption.icon(
                            BitmapDescriptorFactory.fromBitmap(BitmapFactory
                                    .decodeResource(getResources(),
                                            R.drawable.radio_icon)));
                }
                markerOptionlst.add(markerOption);
                aMap.addMarker(markerOption);
                mList.add(aMap.addMarker(markerOption));
            }
        }
    }

    /**
     * 显示定位蓝点
     */
    private void addLocaToMap() {
        aMap.setMyLocationEnabled(true);
        // 设置定位的类型为定位模式，有定位、跟随或地图根据面向方向旋转几种
        MyLocationStyle myLocationStyle = new MyLocationStyle();
        myLocationStyle.interval(2 * 1000);
        myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATION_ROTATE_NO_CENTER);
//        myLocationStyle.strokeWidth(1.0f);
        myLocationStyle.myLocationIcon(BitmapDescriptorFactory
                .fromResource(R.drawable.dangqwz));
        myLocationStyle.strokeColor(Color.parseColor("#F9DEDE"));// 设置圆形的边框颜色
        myLocationStyle.radiusFillColor(Color.argb(100, 249, 222, 222));//
        aMap.setMyLocationStyle(myLocationStyle);
        aMap.setOnMyLocationChangeListener(this);
    }


    @OnClick({R.id.shuoming, R.id.dao_lan, R.id.go_task, R.id.qiehuan})
    void onclik(View v) {
        switch (v.getId()) {
            case R.id.qiehuan:
                if (daodan.isSelected()) {
                    RxToast.normal("语音导览关闭");
                    daodan.setSelected(false);
                    aMap.clear();
                    PlayMapBean beans = SaveObjectUtils.getInstance(getActivity()).getObject(Constants.DOT_LIST, null);
                    if (place_list.size() > 0) {
                        place_list.clear();
                    }
                    if (beans != null) {
                        place_list.addAll(beans.getPlace_list());
                    }
                    addMarkersToMap();
                } else {
                    RxToast.normal("语音导览打开");
                    daodan.setSelected(true);
                    aMap.clear();
                    RailBean railBean = SaveObjectUtils.getInstance(getActivity()).getObject(Constants.DOT_INFO, null);
                    place_list.addAll(railBean.getMp3_tag());
                    Log.i("tttt", "place_list+" + place_list.size());
                    addMarkersToMap();
                }
                break;
            case R.id.shuoming:
                startActivity(new Intent(getActivity(), RuleActivity.class));
                break;
            case R.id.dao_lan:
                startActivity(new Intent(getActivity(), ZoomActivity.class));
                break;
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(ReFreshEvent event) {

    }

    private void settimesd() {
        BaseConfig bf = BaseConfig.getInstance(getActivity());
        boolean iswanc = false;
        for (MapDot dot : place_list) {
            if (!dot.getType().equals("2") && dot.getIs_play().equals("0")) {
                iswanc = true;
                break;
            }
        }
        if (!iswanc) {
            handler2.removeCallbacks(runnable2);
            bf.setLongValue(Constants.IS_TIME, 0);
            ji_timer.setText("已通关");
            if (mMinewBeaconManager != null) {
                try {
                    mMinewBeaconManager.stopScan();
                }catch (Exception e){}
            }
        }
    }

    /**
     * 方法必须重写
     */
    @Override
    public void onResume() {
        super.onResume();
        if (DataUtils.isBlue(getActivity())&&mMinewBeaconManager != null) {
            mMinewBeaconManager.startScan();
        }
        BaseConfig bf = BaseConfig.getInstance(getActivity());
        times_s = bf.getLongValue(Constants.IS_TIME, 0);
        if (times_s > 0) {
            setTimes();
        }
        mapView.onResume();
        addLocaToMap();
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
                for (MapDot hs : place_list) {
                    if (hs.getIs_play().equals("0")) {
                        hs.setIs_play("1");
                    }
                }
                ji_timer.setText("02.00.00");
            } else {
                ji_timer.setText(DataUtils.getDateToTime(System.currentTimeMillis() - times_s));
            }
        }
    };

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
        if (DataUtils.isBlue(getActivity())&&mMinewBeaconManager != null) {
            mMinewBeaconManager.stopScan();
        }
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


    //    boolean isFirst=false;
    private void shouPoup(String ganme_name, boolean isShow, String photo, String sound_path) {
        String distance = "";
        if (DataUtils.isBlue(getActivity())&&mMinewBeaconManager != null) {
            mMinewBeaconManager.startScan();
        }
        if (mEndPoint != null && mStartPoint != null) {
            distance = String.valueOf(AMapUtils.calculateLineDistance(mStartPoint, mEndPoint));
        }
        if (mopupWindow != null && mopupWindow.isShowing()) {
            mopupWindow.setvisib(isShow);
            mopupWindow.setDistance((int) (Double.parseDouble(distance)) + "m");
        } else {
            mopupWindow = new DoTaskPoupWindow(getActivity(), ganme_name, isShow, photo, sound_path, (int) (Double.parseDouble(distance)) + "m", itemsOnClick);
            mopupWindow.showAtLocation(getActivity().findViewById(R.id.map_view),
                    Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
        }
    }

    private void shousoundPoup(String names, String paths, String shuom, int position) {
        if (soundWindow != null) {
            soundWindow.dismiss();
        }
        Log.d("tttt", "shousoundPoup: " + t_ids + ",posi:" + position + ",name:" + names + ",path:" + paths);
        t_ids = position;
        soundWindow = new TalkPoupWindow(getActivity(), names, paths, shuom, null);
        soundWindow.showAtLocation(getActivity().findViewById(R.id.map_view),
                Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);

    }

    OnRxScanerListener listener=new OnRxScanerListener(){

        @Override
        public void onSuccess(String type, Result result) {

        }

        @Override
        public void onFail(String type, String message) {

        }
    };


    private DoTaskPoupWindow.ItemInclick itemsOnClick = new DoTaskPoupWindow.ItemInclick() {
        @Override
        public void ItemClick(int index) {
            BaseConfig bf = BaseConfig.getInstance(getActivity());
            bf.setStringValue(Constants.IS_SHOWS, "0");
            times_s = bf.getLongValue(Constants.IS_TIME, 0);
            if (times_s <= 0) {
                login();
            }
            if (index == 1) {
                if ("14".equals(place_list.get(t_ids).getGame_id())) {
                    Intent intent = new Intent(getActivity(), ActivityScanerCode.class);
                    startActivityForResult(intent, REQUEST_Scan);
                } else if ("15".equals(place_list.get(t_ids).getGame_id())) {
//                    Intent intent = new Intent(getActivity(), ArActivity.class);
//                    intent.putExtra("gameId", place_list.get(t_ids).getGame_id());
//                    startActivity(intent);
//                    t_ids = -1;
                } else if ("18".equals(place_list.get(t_ids).getGame_id())) {
                    Intent intent = new Intent(getActivity(), OfflineActivity.class);
                    intent.putExtra("gameId", place_list.get(t_ids).getGame_id());
                    startActivity(intent);
                    t_ids = -1;
                } else {
                    Intent intent = new Intent(getActivity(), PlayWebViewActivity.class);
                    intent.putExtra("gameId", place_list.get(t_ids).getGame_id());
                    intent.putExtra("gameUrl", place_list.get(t_ids).getGame_url());
                    startActivity(intent);
                    t_ids = -1;
                }
            } else {
                t_ids = -1;
            }
            isGame = false;
            mopupWindow.dismiss();
            if (DataUtils.isBlue(getActivity())&&mMinewBeaconManager != null) {
                mMinewBeaconManager.stopScan();
            }
        }
    };

    //-------------------------------通知后台
    private void login() {
    }

    //-------------------------------------------蓝牙

    /**
     * check Bluetooth state
     */
    private void checkBluetooth() {
        BluetoothState bluetoothState = mMinewBeaconManager.checkBluetoothState();
        switch (bluetoothState) {
            case BluetoothStateNotSupported:
                RxToast.normal("手机不支持蓝牙");
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
        if (DataUtils.isBlue(getActivity())) {
            mMinewBeaconManager.startScan();
        }
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
            public void onRangeBeacons(List<MinewBeacon> minewBeacons) {
                if (minewBeacons != null && minewBeacons.size() > 0) {
//                    String distance = String.valueOf(AMapUtils.calculateLineDistance(mStartPoint, mEndPoint));
                    if (place_list != null && place_list.size() > 0 && t_ids >= 0 && isGame) {
                        for (MinewBeacon beacon : minewBeacons) {
                            String majer = beacon.getBeaconValue(BeaconValueIndex.MinewBeaconValueIndex_Major).getStringValue() + beacon.getBeaconValue(BeaconValueIndex.MinewBeaconValueIndex_Minor).getStringValue();
                            if (majer != null) {
                                for (MajorBean cheeck : place_list.get(t_ids).getiBeacons()) {
                                    String jieguo = cheeck.getMajor() + cheeck.getMinor();
                                    if (jieguo.equals(majer)) {
                                        shouPoup(place_list.get(t_ids).getName(), true, place_list.get(t_ids).getGame_id(), place_list.get(t_ids).getMp3());
                                        break;
                                    }
                                }
                            }
                        }
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
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
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
            case REQUEST_Scan:
                if (null != data) {
                    Bundle bundle = data.getExtras();
                    if (bundle == null) {
                        return;
                    }
                    String jieguo=data.getStringExtra("SCAN_RESULT");
                    if ("B33832EF5EFF3EFF30B1B646B6F2410F".equals(jieguo)) {
                        Intent intent = new Intent(getActivity(), PlayWebViewActivity.class);
                        intent.putExtra("gameId", place_list.get(t_ids).getGame_id());
                        intent.putExtra("gameUrl", place_list.get(t_ids).getGame_url());
                        startActivity(intent);
                    }else {
                        RxToast.warning("二维码不匹配");
                    }
                    break;
                }
        }
    }


    @Override
    public void onMyLocationChange(Location location) {
        mStartPoint = new LatLng(location.getLatitude(), location.getLongitude());
        if (mEndPoint != null) {
            String distance = String.valueOf(AMapUtils.calculateLineDistance(mStartPoint, mEndPoint));
            if (mopupWindow != null && mopupWindow.isShowing()) {
                mopupWindow.setDistance((int) (Double.parseDouble(distance)) + "m");
            }
        }
    }

}

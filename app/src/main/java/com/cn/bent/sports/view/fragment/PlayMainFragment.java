package com.cn.bent.sports.view.fragment;

import android.app.Dialog;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
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
import com.amap.api.maps.model.LatLngBounds;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.cn.bent.sports.R;
import com.cn.bent.sports.api.BaseApi;
import com.cn.bent.sports.base.BaseConfig;
import com.cn.bent.sports.base.BaseFragment;
import com.cn.bent.sports.bean.LoginBase;
import com.cn.bent.sports.bean.MapDot;
import com.cn.bent.sports.bean.PlayMapBean;
import com.cn.bent.sports.bean.RailBean;
import com.cn.bent.sports.bean.ReFreshEvent;
import com.cn.bent.sports.scan.CaptureActivity;
import com.cn.bent.sports.utils.Constants;
import com.cn.bent.sports.utils.DataUtils;
import com.cn.bent.sports.utils.SaveObjectUtils;
import com.cn.bent.sports.utils.ToastUtils;
import com.cn.bent.sports.view.activity.OfflineActivity;
import com.cn.bent.sports.view.activity.PlayWebViewActivity;
import com.cn.bent.sports.view.activity.RuleActivity;
import com.cn.bent.sports.view.activity.ZoomActivity;
import com.cn.bent.sports.view.poupwindow.DoTaskPoupWindow;
import com.cn.bent.sports.view.poupwindow.TalkPoupWindow;
import com.cn.bent.sports.widget.ToastDialog;
import com.minew.beacon.BeaconValueIndex;
import com.minew.beacon.BluetoothState;
import com.minew.beacon.MinewBeacon;
import com.minew.beacon.MinewBeaconManager;
import com.uuzuche.lib_zxing.activity.CodeUtils;
import com.zhl.network.RxObserver;
import com.zhl.network.RxSchedulers;
import com.zhl.network.huiqu.HuiquRxFunction;
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
 * Created by lyj on 2018/2/26 0026.
 * description
 */

public class PlayMainFragment extends BaseFragment implements AMap.OnMarkerClickListener {
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
    public AMapLocationClientOption mLocationOption = null;
    private String latitude, longitude;
    //----------------
    private LatLng mStartPoint;//起点，116.335891,39.942295
    private LatLng mEndPoint;//终点，116.481288,39.995576
    private Marker noMarker;
    private static final int REQUEST_ENABLE_BT = 2;
    private static final int REQUEST_Scan = 12;
    private MinewBeaconManager mMinewBeaconManager;
    private int t_ids=-1;
    private long times_s = 0;
    private Handler handler2;
    private LoginBase user;
    private boolean isBlue = false;
    private List<MapDot> place_list=new ArrayList<>();
    private DoTaskPoupWindow mopupWindow;
    private TalkPoupWindow soundWindow;
    //---------------------
    AMapLocationListener mAMapLocationListener = new AMapLocationListener() {
        @Override
        public void onLocationChanged(AMapLocation aMapLocation) {
            if (aMapLocation != null) {
                if (aMapLocation.getErrorCode() == 0) {
                    //可在其中解析amapLocation获取相应内容。
                    latitude = String.valueOf(aMapLocation.getLatitude());
                    longitude = String.valueOf(aMapLocation.getLongitude());
                    mStartPoint = new LatLng(aMapLocation.getLatitude(), aMapLocation.getLongitude());
                    addLocaToMap();
                } else {
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
        user = SaveObjectUtils.getInstance(getActivity()).getObject(Constants.USER_INFO, null);
        getMapMsg();
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
        RailBean railBean=  SaveObjectUtils.getInstance(getActivity()).getObject(Constants.DOT_INFO, null);
        LatLng southwestLatLng = new LatLng( Double.valueOf(railBean.getFence().getDot_lat()).doubleValue(),  Double.valueOf(railBean.getFence().getDot_long()).doubleValue());
        LatLng northeastLatLng = new LatLng(Double.valueOf(railBean.getFence().getOther_dot_lat()).doubleValue(), Double.valueOf(railBean.getFence().getOther_dot_long()).doubleValue());
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
            if (marker.equals(mList.get(i))){
                t_ids =i;
                if(!TextUtils.isEmpty(place_list.get(i).getGame_url())){
                    if(place_list.get(i).getIs_play().endsWith("0")){
                        showDialogMsg("是否前往该地点", i);
                    }else {
//                    已经玩了的
                    }
                }else {
                    shousoundPoup(place_list.get(i).getMp3(),"");
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
                    if (hs.isCheck()&&hs.getIs_play().endsWith("0")) {
                        markerOption.icon(
                                BitmapDescriptorFactory.fromBitmap(BitmapFactory
                                        .decodeResource(getResources(),
                                                R.drawable.zb_icon)));
                    } else if (hs.getIs_play().endsWith("0")&&!hs.isCheck()){
                        markerOption.icon(
                                BitmapDescriptorFactory.fromBitmap(BitmapFactory
                                        .decodeResource(getResources(),
                                                R.drawable.zuobiao)));
                    }else if(hs.getIs_play().endsWith("1")){
                        markerOption.icon(
                                BitmapDescriptorFactory.fromBitmap(BitmapFactory
                                        .decodeResource(getResources(),
                                                R.drawable.completed)));

                    }else {
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
        aMap.setMyLocationEnabled(false);
    // 设置定位的类型为定位模式，有定位、跟随或地图根据面向方向旋转几种
        aMap.setMyLocationType(AMap.LOCATION_TYPE_LOCATE);
    }


    @OnClick({R.id.shuoming, R.id.dao_lan, R.id.go_task,R.id.qiehuan})
    void onclik(View v) {
        switch (v.getId()) {
            case R.id.qiehuan:
                if(daodan.isSelected()){
                    daodan.setSelected(false);
                    aMap.clear();
                    RailBean railBean=  SaveObjectUtils.getInstance(getActivity()).getObject(Constants.DOT_INFO, null);
                    List<MapDot> mapDots=railBean.getMp3_tag();
                    for(int i=0;i<mapDots.size();i++) {
                        if (place_list.contains(mapDots.get(i))) {
                            place_list.remove(i);
                            Log.i("tttt","contains-");
                            i--;
                        }
                    }
//                    for (MapDot inso:mapDots){
//                        place_list.remove(inso);
//                    }
                    Log.i("tttt","place_list-"+place_list.size());
                    addMarkersToMap();
                }else {
                    daodan.setSelected(true);
                    aMap.clear();
                    RailBean railBean=  SaveObjectUtils.getInstance(getActivity()).getObject(Constants.DOT_INFO, null);
                    place_list.addAll(railBean.getMp3_tag());
                    Log.i("tttt","place_list+"+place_list.size());
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
        BaseConfig bf = BaseConfig.getInstance(getActivity());
        getMapMsg();
        boolean iswanc=true;
        for(MapDot dot:place_list){
            if(dot.getIs_play().endsWith("0")){
                iswanc=false;
                break;
            }
        }
        if(iswanc){
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
                for (MapDot hs : place_list) {
                    if(hs.getIs_play().endsWith("0")){
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


    private void showDialogMsg(String names, final int position) {
        new ToastDialog(getActivity(), R.style.dialog, names, new ToastDialog.OnCloseListener() {
            @Override
            public void onClick(Dialog dialog, boolean confirm) {
                if (confirm) {
                    setcheck();
                    place_list.get(t_ids).setCheck(true);
                    mEndPoint = null;
                    mEndPoint = new LatLng(Double.valueOf(place_list.get(position).getLatitude()).doubleValue(),
                            Double.valueOf(place_list.get(position).getLongitude()).doubleValue());
                    shouPoup(false,place_list.get(t_ids).getGame_id());
                    if (times_s <= 0) {
                        if (mStartPoint != null) {
                            login();
                        }
                    }
                } else {

                }
                dialog.dismiss();
            }
        }).setTitle("提示").show();
    }

    private void  shouPoup(boolean isShow,String photo){
        if(mopupWindow!=null){
            mopupWindow.dismiss();
        }
        mopupWindow = new DoTaskPoupWindow(getActivity(), isShow,photo, itemsOnClick);
        mopupWindow.showAtLocation(getActivity().findViewById(R.id.map_view),
                Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);

    }

    private void  shousoundPoup(String paths,String shuom){
        if(soundWindow!=null){
            soundWindow.dismiss();
        }
        soundWindow = new TalkPoupWindow(getActivity(),paths, shuom,null);
        soundWindow.showAtLocation(getActivity().findViewById(R.id.map_view),
                Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);

    }

    private DoTaskPoupWindow.ItemInclick itemsOnClick = new DoTaskPoupWindow.ItemInclick() {
        @Override
        public void ItemClick() {
            if ("5".endsWith(place_list.get(t_ids).getGame_id())) {
                Intent intent = new Intent(getActivity(), CaptureActivity.class);
                startActivityForResult(intent, REQUEST_Scan);
            }else {
                Intent intent = new Intent(getActivity(), PlayWebViewActivity.class);
                intent.putExtra("gameId", place_list.get(t_ids).getGame_id());
                intent.putExtra("gameUrl", place_list.get(t_ids).getGame_url());
                startActivity(intent);
            }
            mopupWindow.dismiss();
        }
    };

    //-------------------------------通知后台
    private void login() {
        BaseApi.getDefaultService(getActivity()).startGame(user.getMember_id())
                .map(new HuiquRxTBFunction<HuiquTBResult>())
                .compose(RxSchedulers.<HuiquTBResult>io_main())
                .subscribe(new RxObserver<HuiquTBResult>(getActivity(), "changeName", 1, false) {
                    @Override
                    public void onSuccess(int whichRequest, HuiquTBResult info) {
                        BaseConfig bgs = BaseConfig.getInstance(getActivity());
                        bgs.setLongValue(Constants.IS_TIME, System.currentTimeMillis());
                        times_s = bgs.getLongValue(Constants.IS_TIME, 0);
                        setTimes();
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
            public void onRangeBeacons(List<MinewBeacon> minewBeacons) {
                if (minewBeacons != null && minewBeacons.size() > 0) {
//                    String distance = String.valueOf(AMapUtils.calculateLineDistance(mStartPoint, mEndPoint));
                    if (place_list != null&&place_list.size()>0&&t_ids>=0) {
                       for(MinewBeacon beacon:minewBeacons){
                           String majer=beacon.getBeaconValue(BeaconValueIndex.MinewBeaconValueIndex_Major).getStringValue();
                            if(place_list.get(t_ids).getMac().contains(majer)){
                                shouPoup(true,place_list.get(t_ids).getGame_id());
                                break;
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
                if (mMinewBeaconManager.checkBluetoothState().equals(BluetoothState.BluetoothStatePowerOff))
                    isBlue = false;
                checkBluetooth();
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

    //获取接口调用
    private void getMapMsg() {
        showAlert("......", true);
        BaseApi.getDefaultService(getActivity()).getMapMsg(user.getMember_id())
                .map(new HuiquRxFunction<PlayMapBean>())
                .compose(RxSchedulers.<PlayMapBean>io_main())
                .subscribe(new RxObserver<PlayMapBean>(getActivity(), "getMapMsg", 1, false) {
                    @Override
                    public void onSuccess(int whichRequest, PlayMapBean info) {
                        dismissAlert();
                        if(info.getPlace_list().size()>0){
                            aMap.clear();
                            if(place_list!=null){
                                place_list.clear();
                            }
                            place_list.addAll(info.getPlace_list());
                            if(daodan.isSelected()){
                                RailBean railBean=  SaveObjectUtils.getInstance(getActivity()).getObject(Constants.DOT_INFO, null);
                                place_list.addAll(railBean.getMp3_tag());
                            }
                            addMarkersToMap();
                            if(info.getIs_end().endsWith("1")){
                                Intent intent = new Intent(getActivity(), OfflineActivity.class);
                                intent.putExtra("gameId", place_list.get(t_ids).getGame_id());
                                startActivity(intent);
                            }
                        }
                    }

                    @Override
                    public void onError(int whichRequest, Throwable e) {
                        dismissAlert();
                    }
                });
    }

}

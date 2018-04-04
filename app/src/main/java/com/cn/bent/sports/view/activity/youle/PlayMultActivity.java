package com.cn.bent.sports.view.activity.youle;

import android.app.Dialog;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.amap.api.maps.AMap;
import com.amap.api.maps.AMapUtils;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.CameraPosition;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.MyLocationStyle;
import com.cn.bent.sports.R;
import com.cn.bent.sports.api.BaseApi;
import com.cn.bent.sports.base.BaseActivity;
import com.cn.bent.sports.base.BaseConfig;
import com.cn.bent.sports.bean.GamePotins;
import com.cn.bent.sports.bean.LoginResult;
import com.cn.bent.sports.bean.MajorBean;
import com.cn.bent.sports.bean.MapDot;
import com.cn.bent.sports.bean.ReFreshEvent;
import com.cn.bent.sports.bean.TeamGame;
import com.cn.bent.sports.utils.Constants;
import com.cn.bent.sports.utils.DataUtils;
import com.cn.bent.sports.utils.SaveObjectUtils;
import com.cn.bent.sports.view.activity.ArActivity;
import com.cn.bent.sports.view.activity.BitmapManager;
import com.cn.bent.sports.view.activity.OfflineActivity;
import com.cn.bent.sports.view.activity.PlayWebViewActivity;
import com.cn.bent.sports.view.activity.youle.bean.TaskPoint;
import com.cn.bent.sports.view.activity.youle.bean.UserInfo;
import com.cn.bent.sports.view.activity.youle.play.CompleteInfoActivity;
import com.cn.bent.sports.view.activity.youle.play.MemberEditActivity;
import com.cn.bent.sports.view.activity.youle.play.PrepareActivity;
import com.cn.bent.sports.view.poupwindow.DoTaskPoupWindow;
import com.cn.bent.sports.view.poupwindow.TalkPoupWindow;
import com.cn.bent.sports.widget.OneTaskFinishDialog;
import com.cn.bent.sports.widget.OutGameDialog;
import com.minew.beacon.BeaconValueIndex;
import com.minew.beacon.BluetoothState;
import com.minew.beacon.MinewBeacon;
import com.minew.beacon.MinewBeaconManager;
import com.vondear.rxtools.activity.ActivityScanerCode;
import com.vondear.rxtools.view.RxToast;
import com.zhl.network.RxObserver;
import com.zhl.network.RxSchedulers;
import com.zhl.network.huiqu.JavaRxFunction;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.java_websocket.WebSocket;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;
import io.reactivex.functions.Consumer;
import ua.naiksoftware.stomp.LifecycleEvent;
import ua.naiksoftware.stomp.Stomp;
import ua.naiksoftware.stomp.client.StompClient;
import ua.naiksoftware.stomp.client.StompMessage;

/**
 * Created by dawn on 2018/3/27.  多人游玩界面
 */

public class PlayMultActivity extends BaseActivity implements AMap.OnMarkerClickListener, AMap.OnMyLocationChangeListener {
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
    @Bind(R.id.line_one)
    RelativeLayout line_one;
    @Bind(R.id.line_two)
    RelativeLayout line_two;
    @Bind(R.id.time_two)
    TextView timing;//底部计时器
    @Bind(R.id.exit_game)
    ImageView exit_game;//个人
    @Bind(R.id.team_game)
    ImageView team_game;//团队

    private StompClient mStompClient;
    private AMap aMap;
    private float mCurrentZoom = 18f;
    //--------------------
    private List<Marker> mList = new ArrayList<Marker>();
    private LatLng mStartPoint;//起点，116.335891,39.942295
    private LatLng mEndPoint;//终点，116.481288,39.995576
    private static final int REQUEST_ENABLE_BT = 2;
    private static final int REQUEST_SCANS = 11;
    private static final int REQUEST_Scan = 12;
    private MinewBeaconManager mMinewBeaconManager;
    private int t_ids = -1;
    private long times_s = 0;
    private Handler handler2;
    private boolean isBlue = false;
    private DoTaskPoupWindow mopupWindow;
    private TalkPoupWindow soundWindow;
    private TeamGame teamGame;//传过来的对象
    //-------------------------------------------------

    private List<GamePotins> mGamePotinsList = new ArrayList<>();

    @Override
    protected int getLayoutId() {
        return R.layout.map_mult_layout;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
        mapView.onCreate(savedInstanceState);
    }

    @Override
    public void initView() {
        super.initView();
        teamGame = (TeamGame) getIntent().getSerializableExtra("teamGame");
        if(teamGame.getTeamMemberMax()<=1){
            exit_game.setVisibility(View.VISIBLE);
            team_game.setVisibility(View.GONE);
        }else {
            team_game.setVisibility(View.VISIBLE);
            exit_game.setVisibility(View.GONE);
        }

        createStompClient();
        getPoints();
        line_two.setVisibility(View.GONE);
        if (aMap == null) {
            aMap = mapView.getMap();
        }
        aMap.getUiSettings().setZoomControlsEnabled(false);//去掉高德地图右下角隐藏的缩放按钮
        aMap.setOnMarkerClickListener(this);
        aMap.setOnCameraChangeListener(onCameraChangeListener);
        EventBus.getDefault().register(this);

        handler2 = new Handler();
        mMinewBeaconManager = MinewBeaconManager.getInstance(this);
        checkBluetooth();
        if (aMap == null) {
            aMap = mapView.getMap();
        }
        String path = this.getFilesDir() + "/bent/sport.data";
        aMap.setCustomMapStylePath(path);
        aMap.setMapCustomEnable(true);//true 开启; false 关闭
//        RailBean railBean = SaveObjectUtils.getInstance(this).getObject(Constants.DOT_INFO, null);
//        LatLng southwestLatLng = new LatLng(Double.valueOf(railBean.getFence().getDot_lat()).doubleValue(), Double.valueOf(railBean.getFence().getDot_long()).doubleValue());
//        LatLng northeastLatLng = new LatLng(Double.valueOf(railBean.getFence().getOther_dot_lat()).doubleValue(), Double.valueOf(railBean.getFence().getOther_dot_long()).doubleValue());
//        LatLng southwestLatLng = new LatLng(28.084042,112.956461);
//        LatLng northeastLatLng = new LatLng(28.157773,113.019118);
//        LatLngBounds latLngBounds = new LatLngBounds(southwestLatLng, northeastLatLng);
//        aMap.setMapStatusLimits(latLngBounds);
//        aMap.moveCamera(CameraUpdateFactory.newLatLngBounds(latLngBounds, 50));
        // 绑定 Marker 被点击事件
        aMap.setOnMarkerClickListener(this);
    }


    @Override
    public void initData() {
        addLocaToMap();
        startLocation();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(ReFreshEvent event) {


    }


    @OnClick({R.id.map_scan, R.id.map_return, R.id.look_rank, R.id.finish_situation, R.id.exit_game,R.id.team_game})
    void onClick(View view) {
        switch (view.getId()) {
            case R.id.map_scan:
                Intent intent = new Intent(PlayMultActivity.this, ActivityScanerCode.class);
                startActivityForResult(intent, REQUEST_SCANS);
                break;
            case R.id.map_return:
                PlayMultActivity.this.finish();
                break;
            case R.id.look_rank:
                startActivity(new Intent(PlayMultActivity.this, RankingListActivity.class));
                break;
            case R.id.finish_situation:
                startActivity(new Intent(PlayMultActivity.this, CompleteInfoActivity.class));
                break;
            case R.id.exit_game:
                OpenOutDialog();
                break;
            case R.id.team_game:
                Intent intent1 = new Intent(PlayMultActivity.this, MemberEditActivity.class);
                intent1.putExtra("type", "team");
                intent1.putExtra("gameTeamId", teamGame.getId());
                startActivity(intent1);
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
    public boolean onMarkerClick(Marker marker) {
        for (int i = 0; i < mList.size(); i++) {
            if (marker.equals(mList.get(i))) {

                if(mGamePotinsList.get(i).getState()==-1){//未开始
                    getPointGame(teamGame.getId(),mGamePotinsList.get(i).getId(),!mGamePotinsList.get(i).isHasQuestion(),mGamePotinsList.get(i).isHasTask());
                    t_ids=i;
                    break;
                }else if (mGamePotinsList.get(i).getState()==1||mGamePotinsList.get(i).getState()==2) {
                    new OneTaskFinishDialog(PlayMultActivity.this, R.style.dialog, new OneTaskFinishDialog.OnClickListener() {
                        @Override
                        public void onClick(Dialog dialog, int index) {
                            dialog.dismiss();
                        }
                    }).setListData(teamGame,mGamePotinsList.get(i).getId()).show();
                    break;
                }
            }
        }
        return true;
    }


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





    @Override
    public void onResume() {
        super.onResume();
        if (DataUtils.isBlue(PlayMultActivity.this) && mMinewBeaconManager != null) {
            mMinewBeaconManager.startScan();
        }
        //在activity执行onResume时执行mMapView.onResume ()，重新绘制加载地图
        mapView.onResume();
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

    /**
     * 方法必须重写
     */
    @Override

    public void onDestroy() {
        super.onDestroy();
        if (DataUtils.isBlue(PlayMultActivity.this) && mMinewBeaconManager != null) {
            mMinewBeaconManager.stopScan();
        }
        mapView.onDestroy();
        // 关闭定位图层
        aMap.setMyLocationEnabled(false);
        mapView.getMap().clear();
        mapView.onDestroy();
        mapView = null;
        EventBus.getDefault().unregister(this);
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

    //    boolean isFirst=false;
    private void shouPoup(String ganme_name, boolean isShow, String photo, String sound_path) {
        String distance = "";
        if (DataUtils.isBlue(this) && mMinewBeaconManager != null) {
            mMinewBeaconManager.startScan();
        }
        if (mEndPoint != null && mStartPoint != null) {
            distance = String.valueOf(AMapUtils.calculateLineDistance(mStartPoint, mEndPoint));
        }
        if (mopupWindow != null && mopupWindow.isShowing()) {
            mopupWindow.setvisib(isShow);
            mopupWindow.setDistance((int) (Double.parseDouble(distance)) + "m");
        } else {
            mopupWindow = new DoTaskPoupWindow(this, ganme_name, isShow, photo, sound_path, (int) (Double.parseDouble(distance)) + "m", itemsOnClick);
            mopupWindow.showAtLocation(this.findViewById(R.id.map_view),
                    Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
        }
    }

    private void shousoundPoup(String names, String paths, String shuom, int position) {
        if (soundWindow != null) {
            soundWindow.dismiss();
        }
        Log.d("tttt", "shousoundPoup: " + t_ids + ",posi:" + position + ",name:" + names + ",path:" + paths);
        t_ids = position;
        soundWindow = new TalkPoupWindow(this, names, paths, shuom, null);
        soundWindow.showAtLocation(this.findViewById(R.id.map_view),
                Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);

    }

    private DoTaskPoupWindow.ItemInclick itemsOnClick = new DoTaskPoupWindow.ItemInclick() {
        @Override
        public void ItemClick(int index) {
            BaseConfig bf = BaseConfig.getInstance(PlayMultActivity.this);
            bf.setStringValue(Constants.IS_SHOWS, "0");
            times_s = bf.getLongValue(Constants.IS_TIME, 0);
            if (times_s <= 0) {
//                login();
            }
//            if (index == 1) {
//                if ("14".equals(place_list.get(t_ids).getGame_id())) {
//                    Intent intent = new Intent(PlayMultActivity.this, ActivityScanerCode.class);
//                    startActivityForResult(intent, REQUEST_Scan);
//                } else if ("15".equals(place_list.get(t_ids).getGame_id())) {
//                    Intent intent = new Intent(PlayMultActivity.this, ArActivity.class);
//                    intent.putExtra("gameId", place_list.get(t_ids).getGame_id());
//                    startActivity(intent);
//                    t_ids = -1;
//                } else if ("18".equals(place_list.get(t_ids).getGame_id())) {
//                    Intent intent = new Intent(PlayMultActivity.this, OfflineActivity.class);
//                    intent.putExtra("gameId", place_list.get(t_ids).getGame_id());
//                    startActivity(intent);
//                    t_ids = -1;
//                } else {
//                    Intent intent = new Intent(PlayMultActivity.this, PlayWebViewActivity.class);
//                    intent.putExtra("gameId", place_list.get(t_ids).getGame_id());
//                    intent.putExtra("gameUrl", place_list.get(t_ids).getGame_url());
//                    startActivity(intent);
//                    t_ids = -1;
//                }
//            } else {
//                t_ids = -1;
//            }
            mopupWindow.dismiss();
            if (DataUtils.isBlue(PlayMultActivity.this) && mMinewBeaconManager != null) {
                mMinewBeaconManager.stopScan();
            }
        }
    };

//-------------------------------------------蓝牙

    /**
     * check Bluetooth state
     */
    private void checkBluetooth() {
        BluetoothState bluetoothState = mMinewBeaconManager.checkBluetoothState();
        switch (bluetoothState) {
            case BluetoothStateNotSupported:
                RxToast.warning("手机不支持蓝牙");
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
        if (DataUtils.isBlue(PlayMultActivity.this)) {
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
                    if (mGamePotinsList != null && mGamePotinsList.size() > 0 && t_ids >= 0) {
                        for (MinewBeacon beacon : minewBeacons) {
                            String majer = beacon.getBeaconValue(BeaconValueIndex.MinewBeaconValueIndex_Major).getStringValue() ;
                            if (majer != null) {
                                    if (majer.equals(mGamePotinsList.get(t_ids).getMajor()+"")) {
//                                        shouPoup(place_list.get(t_ids).getName(), true, place_list.get(t_ids).getGame_id(), place_list.get(t_ids).getMp3());
                                        t_ids=-1;
                                        break;
                                    }
                            }
                        }
                    }
                }
            }

            /**
             *  the manager calls back this method when BluetoothStateChanged.
             *  @param state BluetoothState
             */
            @Override
            public void onUpdateState(com.minew.beacon.BluetoothState state) {
                switch (state) {
                    case BluetoothStatePowerOn:
                        RxToast.normal("蓝牙打开");
                        break;
                    case BluetoothStatePowerOff:
                        RxToast.normal("蓝牙关闭");
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
                    String jieguo = data.getStringExtra("SCAN_RESULT");
                    if ("B33832EF5EFF3EFF30B1B646B6F2410F".equals(jieguo)) {//玩游戏
                        Intent intent = new Intent(this, PlayWebViewActivity.class);
//                        intent.putExtra("gameId", place_list.get(t_ids).getGame_id());
//                        intent.putExtra("gameUrl", place_list.get(t_ids).getGame_url());
                        startActivity(intent);
                    } else {
                        RxToast.error("二维码不匹配");
                    }
                    break;
                }
            case REQUEST_SCANS:
                if (null != data) {
                    Bundle bundle = data.getExtras();
                    if (bundle == null) {
                        return;
                    }
                    String jieguo = data.getStringExtra("SCAN_RESULT");
                    if ("".equals(jieguo)) {


                    } else {
                        RxToast.error("二维码不匹配");
                    }
                    break;
                }
        }
    }

    @Override
    public void onMyLocationChange(Location location) {
        mStartPoint = new LatLng(location.getLatitude(), location.getLongitude());
        aMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(location.getLatitude(), location.getLongitude()), mCurrentZoom));
        if (mEndPoint != null) {
            String distance = String.valueOf(AMapUtils.calculateLineDistance(mStartPoint, mEndPoint));
            if (mopupWindow != null && mopupWindow.isShowing()) {
                mopupWindow.setDistance((int) (Double.parseDouble(distance)) + "m");
            }
        }
    }

    /**
     * 退出dialog
     */
    private void OpenOutDialog() {
        OutGameDialog outGameDialog = new OutGameDialog(PlayMultActivity.this, R.style.dialog, new OutGameDialog.OnClickListener() {
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


    private void getPoints() {
        showAlert("正在获取...", true);
        BaseApi.getJavaLoginDefaultService(PlayMultActivity.this).getGamePoints(1 + "")
                .map(new JavaRxFunction<List<GamePotins>>())
                .compose(RxSchedulers.<List<GamePotins>>io_main())
                .subscribe(new RxObserver<List<GamePotins>>(PlayMultActivity.this, TAG, 1, false) {
                    @Override
                    public void onSuccess(int whichRequest, List<GamePotins> info) {
                        dismissAlert();
                        mGamePotinsList = info;
                        for (GamePotins gamePotins : info) {
                            setOverLay(gamePotins.getState(), gamePotins);
                        }
                    }

                    @Override
                    public void onError(int whichRequest, Throwable e) {
                        dismissAlert();

                        RxToast.error(e.getMessage());
                    }
                });
    }

    private void setOverLay(int index, GamePotins gamePotins) {

        MarkerOptions markerOption = new MarkerOptions();
        markerOption.position(new LatLng(gamePotins.getLatitude(), gamePotins.getLongitude()));
        markerOption.draggable(true);//设置Marker可拖动
        markerOption.title(gamePotins.getId() + "");
        markerOption.icon(BitmapManager.getInstance().getGameBitmapDescriptor(index));
        // 将Marker设置为贴地显示，可以双指下拉地图查看效果
        markerOption.setFlat(true);//设置marker平贴地图效果
        Marker marker = aMap.addMarker(markerOption);

    }

    //--------------------------------------------------长连接
    private void createStompClient() {
        LoginResult user = SaveObjectUtils.getInstance(PlayMultActivity.this).getObject(Constants.USER_INFO, null);
        try {
            mStompClient = Stomp.over(WebSocket.class, "ws://" + Constants.getsocket(PlayMultActivity.this) + "/websocket?access_token=" + user.getAccess_token());
            mStompClient.connect();
        } catch (Exception e) {
            Log.i("tttt", "msg=" + e.getMessage());
        }
        mStompClient.lifecycle().subscribe(new Consumer<LifecycleEvent>() {
            @Override
            public void accept(LifecycleEvent lifecycleEvent) {
                Log.d("tttt", "lifecycleEvent=" + lifecycleEvent.getType());
                switch (lifecycleEvent.getType()) {
                    case OPENED:
                        Log.d("tttt", "Stomp connection opened");
                        getMsg();
                        getpointsMsg();
                        getAddressMsg();
                        break;

                    case ERROR:
                        Log.e("tttt", "Stomp Error", lifecycleEvent.getException());

                        break;
                    case CLOSED:
                        Log.d("tttt", "Stomp connection closed");
                        createStompClient();
                        break;
                }
            }
        });
    }
    //监听游戏完成
    private void getMsg() {
        mStompClient.topic("/topic/+" + teamGame.getId()+ "" + "/status").subscribe(new Consumer<StompMessage>() {
            @Override
            public void accept(StompMessage stompMessage) throws Exception {
                String msg = stompMessage.getPayload().trim();
                String datas="";
                try {
                    JSONObject jsonObject = new JSONObject();
                    JSONObject obj = jsonObject.getJSONObject(msg);
                    datas =obj.getString("data");
                }catch (Exception e){

                }
                if("GAME_OVER".equals(datas)){

                }
            }
        });

    }

    //监听任务完成
    private void getpointsMsg() {
        mStompClient.topic("/topic/+" + teamGame.getId()+ "" + "/pass").subscribe(new Consumer<StompMessage>() {
            @Override
            public void accept(StompMessage stompMessage) throws Exception {
                String msg = stompMessage.getPayload().trim();
                String datas="";
                try {
                    JSONObject jsonObject = new JSONObject();
                    JSONObject obj = jsonObject.getJSONObject(msg);
                    datas =obj.getString("data");
                }catch (Exception e){

                }
            /*   "data": {
                "gamePointId": 0,
                        "score": 0,
                        "timing": 0,
                        "type": 0,
                        "userId": 0
            },*/
            }
        });

    }
    ///user/{username}/topic/{teamId}/getLocations
    //获取队员地理位置
    private void getAddressMsg() {
        UserInfo user = SaveObjectUtils.getInstance(PlayMultActivity.this).getObject(Constants.USER_BASE, null);
        mStompClient.topic("/user"+user.getNickname()+"/topic/+" + teamGame.getId()+ "" + "/getLocations").subscribe(new Consumer<StompMessage>() {
            @Override
            public void accept(StompMessage stompMessage) throws Exception {
                String msg = stompMessage.getPayload().trim();
                String datas="";
                try {
                    JSONObject jsonObject = new JSONObject();
                    JSONObject obj = jsonObject.getJSONObject(msg);
                    datas =obj.getString("data");
                }catch (Exception e){

                }
              /*  "data": [
                {
                    "avatar": "string",
                        "gameTeamId": 0,
                        "latitude": 0,
                        "longitude": 0,
                        "nickname": "string",
                        "userId": 0
                }*/
            }
        });
    }

    private  void getPointGame(long teamGameId,long pointId,boolean xianxia,boolean xxtimu){
        showAlert("正在获取任务...", true);
        BaseApi.getJavaLoginDefaultService(PlayMultActivity.this).getPointGame(teamGameId,pointId,xianxia,xxtimu)
                .map(new JavaRxFunction<TaskPoint>())
                .compose(RxSchedulers.<TaskPoint>io_main())
                .subscribe(new RxObserver<TaskPoint>(PlayMultActivity.this, TAG, 1, false) {
                    @Override
                    public void onSuccess(int whichRequest, TaskPoint info) {
                        dismissAlert();
//                        shousoundPoup(place_list.get(i).getName(), place_list.get(i).getMp3(), place_list.get(i).getDesc(), i);
//                    if (place_list.get(i).getIs_play().equals("0")) {
//                        t_ids = i;
//                        isGame = true;
//                        setcheck();
//                        place_list.get(t_ids).setCheck(true);
//                        mEndPoint = null;
//                        mEndPoint = new LatLng(Double.valueOf(place_list.get(i).getLatitude()).doubleValue(),
//                                Double.valueOf(place_list.get(i).getLongitude()).doubleValue());
//                        shouPoup(place_list.get(t_ids).getGame_name(), false, place_list.get(t_ids).getGame_id(), place_list.get(t_ids).getMp3());
//                        break;
//                    } else {
//                        isGame = false;
//                    }
                    }

                    @Override
                    public void onError(int whichRequest, Throwable e) {
                        dismissAlert();

                        RxToast.error(e.getMessage());
                    }
                });

    }


}

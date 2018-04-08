package com.cn.bent.sports.view.activity;

import android.Manifest;
import android.app.Dialog;
import android.bluetooth.BluetoothAdapter;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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
import com.amap.api.maps.model.Polyline;
import com.amap.api.maps.model.PolylineOptions;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.route.BusRouteResult;
import com.amap.api.services.route.DriveRouteResult;
import com.amap.api.services.route.RideRouteResult;
import com.amap.api.services.route.RouteSearch;
import com.amap.api.services.route.WalkPath;
import com.amap.api.services.route.WalkRouteResult;
import com.cn.bent.sports.R;
import com.cn.bent.sports.api.BaseApi;
import com.cn.bent.sports.base.BaseActivity;
import com.cn.bent.sports.bean.LinesDetailEntity;
import com.cn.bent.sports.bean.LinesPointsEntity;
import com.cn.bent.sports.bean.PlayBean;
import com.cn.bent.sports.bean.PlayEvent;
import com.cn.bent.sports.bean.ScenicPointsEntity;
import com.cn.bent.sports.bean.StartEvent;
import com.cn.bent.sports.database.QueueBean;
import com.cn.bent.sports.database.QueueManager;
import com.cn.bent.sports.database.TaskCationManager;
import com.cn.bent.sports.evevt.DistanceEvent;
import com.cn.bent.sports.evevt.ShowPoupEvent;
import com.cn.bent.sports.evevt.ShowSubscriber;
import com.cn.bent.sports.recyclebase.CommonAdapter;
import com.cn.bent.sports.recyclebase.ViewHolder;
import com.cn.bent.sports.sensor.UpdateUiCallBack;
import com.cn.bent.sports.utils.Constants;
import com.cn.bent.sports.utils.DataUtils;
import com.cn.bent.sports.utils.SaveObjectUtils;
import com.cn.bent.sports.utils.StepData;
import com.cn.bent.sports.view.poupwindow.LineListPoupWindow;
import com.cn.bent.sports.view.poupwindow.XianluPoupWindow;
import com.cn.bent.sports.view.service.MusicService;
import com.cn.bent.sports.view.service.StepService;
import com.cn.bent.sports.widget.AroundDialog;
import com.cn.bent.sports.widget.ExxitDialog;
import com.cn.bent.sports.widget.GotoWhereDialog;
import com.cn.bent.sports.widget.NearDialog;
import com.cn.bent.sports.widget.RouteTool;
import com.minew.beacon.BeaconValueIndex;
import com.minew.beacon.BluetoothState;
import com.minew.beacon.MinewBeacon;
import com.minew.beacon.MinewBeaconManager;
import com.vondear.rxtools.view.RxToast;
import com.zhl.network.RxObserver;
import com.zhl.network.RxSchedulers;
import com.zhl.network.huiqu.JavaRxFunction;

import org.aisen.android.component.eventbus.NotificationCenter;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by dawn on 2018/3/8.
 */

public class MapActivity extends BaseActivity implements AMap.OnMyLocationChangeListener, RouteSearch.OnRouteSearchListener {
    @Bind(R.id.mapView)
    MapView mMapView;
    @Bind(R.id.shaixuan)
    TextView shaixuan;
    @Bind(R.id.fujin)
    TextView fujin;
    @Bind(R.id.waik_num)
    TextView waik_num;
    @Bind(R.id.luxian)
    TextView luxian;
    @Bind(R.id.yinp_bf)
    ImageView yinp_bf;
    @Bind(R.id.juli)
    TextView mjuli;
    @Bind(R.id.tour_name)
    TextView tour_name;
    @Bind(R.id.bsgj_layout)
    CheckBox stepCheckBox;
    @Bind(R.id.yuyin_bf)
    CheckBox yyCheckBox;
    @Bind(R.id.tour_list)
    RecyclerView tour_list;

    private boolean isFirstLoc = true; // 是否首次定位
    private boolean isShowLuxian = true; // 是否显示路线
    float mCurrentZoom = 18f;//默认地图缩放比例值

    List<LatLng> points = new ArrayList<LatLng>();//位置点集合
    LatLng last = new LatLng(0, 0);//上一个定位点
    private AnimationDrawable animationDrawable;
    private AMap aMap;
    private LatLonPoint lp = new LatLonPoint(28.008977, 113.088063);//
    private List<ScenicPointsEntity.PointsBean> mPointsList = new ArrayList<ScenicPointsEntity.PointsBean>();

    private Map<Integer, ScenicPointsEntity.PointsBean> mPointsEntityMap = new HashMap<>();
    private Map<Integer, Marker> mMarkerMap = new HashMap<>();
    private List<List<ScenicPointsEntity.PointsBean>> mPointsEntityList = new ArrayList<List<ScenicPointsEntity.PointsBean>>();
    ServiceConnection serviceConnection;
    MusicService.MusicController mycontrol;
    private boolean isBind = false;
    private static final int READ_PHONE_STATE = 100;
    private MinewBeaconManager mMinewBeaconManager;
    private static final int REQUEST_ENABLE_BT = 2;
    private boolean isBlue = false;
    private static final int GPS_REQUEST_CODE = 10;
    private ScenicPointsEntity.PointsBean mPointsEntity;
    private LatLng startLatlng;
    private Polyline polyline;
    private boolean isShowPolyLine = false;
    private List<LatLng> pointLatLngs = new ArrayList<LatLng>();//位置点集合
    private LineListPoupWindow mopupWindow;
    private XianluPoupWindow xlWindow;
    private CommonAdapter<ScenicPointsEntity.PointsBean> mAdapter;
    private RouteSearch routeSearch;
    private List<ScenicPointsEntity.PointsBean> voicePoints = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mMapView.onCreate(savedInstanceState);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.marker_map_layout;
    }

    @Override
    public void initView() {
        super.initView();
        mMinewBeaconManager = MinewBeaconManager.getInstance(this);
        checkBluetooth();
        NotificationCenter.defaultCenter().subscriber(ShowPoupEvent.class, disevent);
        EventBus.getDefault().register(this);
        if (aMap == null) {
            aMap = mMapView.getMap();
        }
        String path = this.getFilesDir() + "/bent/sport.data";
        aMap.setCustomMapStylePath(path);
        aMap.setMapCustomEnable(true);//true 开启; false 关闭
        aMap.showMapText(false);//关闭文字
        aMap.getUiSettings().setZoomControlsEnabled(false);//去掉高德地图右下角隐藏的缩放按钮
        aMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(lp.getLatitude(), lp.getLongitude()), 17));
        // 绑定海量点点击事件
//        aMap.setOnMultiPointClickListener(multiPointClickListener);
        aMap.setOnMarkerClickListener(mMarkerClickListener);
        aMap.setOnCameraChangeListener(onCameraChangeListener);
        routeSearch = new RouteSearch(this);
        routeSearch.setRouteSearchListener(this);
        stepCheckBox.setOnCheckedChangeListener(stepCheckedChangeListener);
        if (Build.VERSION.SDK_INT >= 23) {
            showPermission();
        } else {
            openGPSSettings();//定位方法
        }
        setupService();
        Intent intent = new Intent(this, MusicService.class);
        startService(intent);
        if (serviceConnection == null) {
            serviceConnection = new ServiceConnection() {
                @Override
                public void onServiceConnected(ComponentName name, IBinder service) {
                    mycontrol = (MusicService.MusicController) service;
                    checkPause();
                    //设置进度条的最大长度
                    //连接之后启动子线程设置当前进度
                }

                @Override
                public void onServiceDisconnected(ComponentName name) {

                }
            };
            //以绑定方式连接服务
            bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE);
        }
    }

    /**
     * 开启计步服务
     */
    private void setupService() {
        Intent intent = new Intent(this, StepService.class);
        isBind = bindService(intent, conn, Context.BIND_AUTO_CREATE);
        startService(intent);
    }

    /**
     * 用于查询应用服务（application Service）的状态的一种interface，
     * 更详细的信息可以参考Service 和 context.bindService()中的描述，
     * 和许多来自系统的回调方式一样，ServiceConnection的方法都是进程的主线程中调用的。
     */
    ServiceConnection conn = new ServiceConnection() {
        /**
         * 在建立起于Service的连接时会调用该方法，目前Android是通过IBind机制实现与服务的连接。
         * @param name 实际所连接到的Service组件名称
         * @param service 服务的通信信道的IBind，可以通过Service访问对应服务
         */
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            StepService stepService = ((StepService.StepBinder) service).getService();
            //设置初始化数据
            waik_num.setText(stepService.getStepCount() + "");
            //设置步数监听回调
            stepService.registerCallback(new UpdateUiCallBack() {
                @Override
                public void updateUi(int stepCount) {
                    waik_num.setText(stepCount + "");
                    sendStepMsg(stepCount);
                }
            });
        }

        /**
         *
         * 当与Service之间的连接丢失的时候会调用该方法，
         * 这种情况经常发生在Service所在的进程崩溃或者被Kill的时候调用，
         * 此方法不会移除与Service的连接，当服务重新启动的时候仍然会调用 onServiceConnected()。
         * @param name 丢失连接的组件名称
         */
        @Override
        public void onServiceDisconnected(ComponentName name) {
        }
    };

    @OnClick({R.id.top_image})
    void onclik(View v) {
        switch (v.getId()) {
            case R.id.top_image:
                finish();
                break;
        }
    }

    private void checkPause() {
        if (mPointsEntity != null) {
            tour_name.setText(mPointsEntity.getPointName());
        }
        if (mycontrol != null && mycontrol.isPlay()) {
            yinp_bf.setBackgroundResource(R.drawable.play_anim);
            animationDrawable = (AnimationDrawable) yinp_bf.getBackground();
            if (animationDrawable != null && !animationDrawable.isRunning()) {
                animationDrawable.start();
            }

        } else if (mycontrol != null) {
            PlayBean info = SaveObjectUtils.getInstance(getApplicationContext()).getObject(Constants.PLAY_POSION, null);
            if (info != null) {
                if (animationDrawable != null && animationDrawable.isRunning()) {
                    animationDrawable.stop();
                }
                yinp_bf.setBackgroundResource(R.drawable.tizhibf);
            }
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(StartEvent event) {
        if (event.isStart()) {
            SaveObjectUtils.getInstance(getApplicationContext()).setObject(Constants.NOW_POION, mPointsEntity);
            yinp_bf.setBackgroundResource(R.drawable.play_anim);
            animationDrawable = (AnimationDrawable) yinp_bf.getBackground();
            if (animationDrawable != null && !animationDrawable.isRunning()) {
                animationDrawable.start();
            }
        } else {
            if (animationDrawable != null && animationDrawable.isRunning()) {
                animationDrawable.stop();
            }
            SaveObjectUtils.getInstance(getApplicationContext()).setObject(Constants.NOW_POION, null);
            yinp_bf.setBackgroundResource(R.drawable.tizhibf);
            if (TaskCationManager.getQuen() >= 0) {
                chanVioce(TaskCationManager.getQuen());
            }
        }
    }

    @Override
    public void initData() {
        super.initData();
        getdot();

    }

    /**
     * 获取点位数据
     */
    private void getdot() {
        showAlert("正在获取...", true);
        BaseApi.getJavaLoginDefaultService(this).getScenicPoints("1")
                .map(new JavaRxFunction<ScenicPointsEntity>())
                .compose(RxSchedulers.<ScenicPointsEntity>io_main())
                .subscribe(new RxObserver<ScenicPointsEntity>(MapActivity.this, "getScenicPoints", 1, false) {
                    @Override
                    public void onSuccess(int whichRequest, ScenicPointsEntity scenicPointsEntity) {
                        dismissAlert();
                        if (scenicPointsEntity != null)
                            lp = new LatLonPoint(scenicPointsEntity.getLatitude(), scenicPointsEntity.getLatitude());
                        if (scenicPointsEntity != null && scenicPointsEntity.getPoints() != null && scenicPointsEntity.getPoints().size() > 0) {
                            Log.d("dddd", "onSuccess: " + scenicPointsEntity.getPoints().get(0).getPointName());

                            for (ScenicPointsEntity.PointsBean pointsBean : scenicPointsEntity.getPoints()) {
                                pointsBean.setMp3(Constants.JAVA_YUN_URL + pointsBean.getMp3());
                                pointsBean.setImagesUrl(Constants.JAVA_YUN_URL + pointsBean.getImagesUrl());
                                pointsBean.setPointImg(Constants.JAVA_YUN_URL + pointsBean.getPointImg());
                                mPointsList.add(pointsBean);
                                mPointsEntityMap.put(pointsBean.getId(), pointsBean);
                                setOverLay(pointsBean.getType(), pointsBean);
                            }
                            if (TaskCationManager.getSize() <= 0) {
                                TaskCationManager.insert(scenicPointsEntity.getPoints());
                            }
                            mPointsEntityList.add(mPointsList);
                            mPointsEntityList.add(mPointsList);
                        }
                    }

                    @Override
                    public void onError(int whichRequest, Throwable e) {
                        dismissAlert();
                        RxToast.error(e.getMessage());
                    }
                });
    }

    /**
     * 获取线路数据
     */
    private void getXianluData() {
        BaseApi.getJavaLoginDefaultService(this).getScenicLines("1")
                .map(new JavaRxFunction<List<LinesPointsEntity>>())
                .compose(RxSchedulers.<List<LinesPointsEntity>>io_main())
                .subscribe(new RxObserver<List<LinesPointsEntity>>(this, "getScenicLines", 1, false) {
                    @Override
                    public void onSuccess(int whichRequest, List<LinesPointsEntity> linesPointsEntityList) {
                        shouLuxianPoup(linesPointsEntityList);
                    }

                    @Override
                    public void onError(int whichRequest, Throwable e) {
                        Log.d("dddd", "onError: " + e.getMessage());
                    }
                });
    }

    /**
     * 获取线路详情数据
     */
    private void getXianluDetailData(int linesId) {
        tour_list.setVisibility(View.VISIBLE);
        isShowLuxian = false;
        if (polyline != null)
            polyline.remove();
        BaseApi.getJavaLoginDefaultService(this).getScenicLinesDetail(linesId)
                .map(new JavaRxFunction<LinesDetailEntity>())
                .compose(RxSchedulers.<LinesDetailEntity>io_main())
                .subscribe(new RxObserver<LinesDetailEntity>(this, "getScenicLines", 1, false) {
                    @Override
                    public void onSuccess(int whichRequest, final LinesDetailEntity linesPointsDetailEntity) {
                        Log.d("dddd", "onSuccess: " + linesPointsDetailEntity.getPoints().size());
                        if (linesPointsDetailEntity.getPoints() != null && linesPointsDetailEntity.getPoints().size() > 0) {
                            aMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
                                    new LatLng(linesPointsDetailEntity.getPoints().get(0).get(0), linesPointsDetailEntity.getPoints().get(0).get(1)), mCurrentZoom));
                            List<LatLng> latLngs = new ArrayList<LatLng>();
                            for (List<Double> doubles : linesPointsDetailEntity.getPoints()) {
                                latLngs.add(new LatLng(doubles.get(0), doubles.get(1)));
                            }
                            polyline = aMap.addPolyline(new PolylineOptions().
                                    addAll(latLngs).width(10).color(Color.argb(255, 237, 237, 237)));
                        }
                        if (linesPointsDetailEntity.getVoicePoints() != null && linesPointsDetailEntity.getVoicePoints().size() > 0) {
                            voicePoints = linesPointsDetailEntity.getVoicePoints();
                            mAdapter = new CommonAdapter<ScenicPointsEntity.PointsBean>(MapActivity.this, R.layout.tour_line_item, linesPointsDetailEntity.getVoicePoints()) {
                                @Override
                                protected void convert(final ViewHolder holder, final ScenicPointsEntity.PointsBean pointsBean, final int position) {
                                    holder.setText(R.id.tour_num, (position + 1) + "");
                                    holder.setText(R.id.tour_name, pointsBean.getPointName());

                                    holder.getConvertView().setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            mPointsEntity = pointsBean;
                                            for (ScenicPointsEntity.PointsBean entity : linesPointsDetailEntity.getVoicePoints()) {
                                                entity.setShow(false);
                                            }
                                            pointsBean.setShow(true);
                                            aMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(pointsBean.getLatitude(),
                                                    pointsBean.getLongitude()), mCurrentZoom));
                                            mAdapter.notifyDataSetChanged();
                                        }
                                    });
                                    if (pointsBean.isShow()) {
                                        if (pointsBean.getType() == 2 && !TextUtils.isEmpty(pointsBean.getMp3())) {
                                            addAnimMarker(pointsBean);
                                            playMarkerAudio(pointsBean);
                                        }
                                        holder.getView(R.id.tour_num).setBackground(MapActivity.this.getResources().getDrawable(R.drawable.tour_choose_item_bg));
                                        ((TextView) holder.getView(R.id.tour_name)).setTextColor(MapActivity.this.getResources().getColor(R.color.color_fd7d6f));
                                    } else {
                                        holder.getView(R.id.tour_num).setBackground(MapActivity.this.getResources().getDrawable(R.drawable.tour_item_bg));
                                        TextView textView = (TextView) holder.getView(R.id.tour_name);
                                        textView.setTextColor(MapActivity.this.getResources().getColor(R.color.color_666666));
                                    }
                                }
                            };
                            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(MapActivity.this);
                            linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
                            tour_list.setLayoutManager(linearLayoutManager);
                            tour_list.setAdapter(mAdapter);
                        }
                    }

                    @Override
                    public void onError(int whichRequest, Throwable e) {
                        Log.d("dddd", "onError: " + e.getMessage());
                    }
                });
    }

    /**
     * 步行轨迹
     */
    OnCheckedChangeListener stepCheckedChangeListener = new OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
            //清除上一次轨迹，避免重叠绘画
            Log.d("dddd", "onCheckedChanged isChecked: " + isChecked + ",points.size():" + points.size());
            if (isChecked) {
                isShowPolyLine = true;
//                //将points集合中的点绘制轨迹线条图层，显示在地图上
//                polyline = aMap.addPolyline(new PolylineOptions().
//                        addAll(points).width(15).color(0xAAD1D1D1));
            } else {
                isShowPolyLine = false;
                if (polyline != null)
                    polyline.remove();
            }
        }
    };

    @OnClick({R.id.shaixuan, R.id.fujin, R.id.zhankai, R.id.yinp_bf, R.id.dingwei, R.id.luxian})
    void onCLick(View view) {
        switch (view.getId()) {
            case R.id.shaixuan:
                gotoAroundPlace();
                break;
            case R.id.fujin:
                gotoNearPlace();
                break;
            case R.id.dingwei:
                Log.d("dddd", "onCLick: " + startLatlng.latitude);
                if (startLatlng == null)
                    if (Build.VERSION.SDK_INT >= 23)
                        showPermission();
                    else
                        openGPSSettings();//定位方法
                else
                    aMap.moveCamera(CameraUpdateFactory.newLatLngZoom(startLatlng, mCurrentZoom));
                break;
            case R.id.luxian:
                setLuxianPng(isShowLuxian);
                if (polyline != null)
                    polyline.remove();
                if (isShowLuxian) {
                    getXianluData();
                } else {
                    polyline = null;
                    tour_list.setVisibility(View.GONE);
                    isShowLuxian = true;
                }
                break;
            case R.id.zhankai:
                Intent intent1 = new Intent(this, BottomPlayActivity.class);
                intent1.putExtra("enty", mPointsEntity);
                intent1.putExtra("startLatlng", startLatlng);
                this.startActivity(intent1);
                this.overridePendingTransition(R.anim.slide_bottom_in, R.anim.slide_bottom_out);
                break;
            case R.id.yinp_bf:
                if (mycontrol == null) {
                    break;
                }
                if (mycontrol.isPlay()) {
                    if (animationDrawable != null && animationDrawable.isRunning()) {
                        animationDrawable.stop();
                    }
                    mycontrol.pause();
                    yinp_bf.setBackgroundResource(R.drawable.tizhibf);
                } else if (mycontrol.isHave()) {
                    mycontrol.play();
                    yinp_bf.setBackgroundResource(R.drawable.play_anim);
                    animationDrawable = (AnimationDrawable) yinp_bf.getBackground();
                    if (animationDrawable != null && !animationDrawable.isRunning()) {
                        animationDrawable.start();
                    }
                }
                break;
        }
    }

    private void shouLuxianPoup(List<LinesPointsEntity> linesPointsEntityList) {
        xlWindow = new XianluPoupWindow(MapActivity.this, linesPointsEntityList, luxianItemsOnClick);
        xlWindow.showAtLocation(this.findViewById(R.id.mapView),
                Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
    }

    private XianluPoupWindow.ItemInclick luxianItemsOnClick = new XianluPoupWindow.ItemInclick() {
        @Override
        public void ItemClick(final int index) {
            xlWindow.dismiss();
            getXianluDetailData(index);
            aMap.clear();
            for (int i = 0; i < mPointsEntityList.get(index).size(); i++) {
                setMarkerLay(mPointsEntityList.get(index).get(i).getType());
            }
//            chooseItem = index;
        }
    };

    @Override
    public void onBusRouteSearched(BusRouteResult busRouteResult, int i) {

    }

    @Override
    public void onDriveRouteSearched(DriveRouteResult driveRouteResult, int i) {

    }

    @Override
    public void onWalkRouteSearched(WalkRouteResult walkRouteResult, int i) {
        WalkPath walkPath = walkRouteResult.getPaths().get(0);
        RouteTool walkRouteOverlay = new RouteTool(this, aMap, walkPath, walkRouteResult.getStartPos(),
                walkRouteResult.getTargetPos());
        for (int index = 1; index <= pointLatLngs.size(); index++) {
            Log.d("dddd", "onWalkRouteSearched: " + walkRouteResult.getStartPos().getLatitude()
                    + ",latitude:" + pointLatLngs.get(index).latitude);
            if (walkRouteResult.getStartPos().getLatitude() == pointLatLngs.get(index).latitude &&
                    walkRouteResult.getStartPos().getLongitude() == pointLatLngs.get(index).longitude) {
                walkRouteOverlay.getStartBitmapDescriptor(R.drawable.position_1);

                break;
            }
        }
        walkRouteOverlay.setView(R.color.color_fd7d6f, 18f);
        walkRouteOverlay.setNodeIconVisibility(false);//隐藏转弯的节点
        walkRouteOverlay.addToMap();
    }

    @Override
    public void onRideRouteSearched(RideRouteResult rideRouteResult, int i) {

    }

    private void setLuxianPng(boolean isShowLuxian) {
        Drawable drawable = null;
        if (isShowLuxian)
            drawable = getResources().getDrawable(R.drawable.close_line);
        else
            drawable = getResources().getDrawable(R.drawable.luxian_1);
        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());//这句一定要加
        luxian.setCompoundDrawables(null, drawable, null, null);//setCompoundDrawables用来设置图片显示在文本的哪一端
    }

    /**
     * 检测GPS是否打开
     *
     * @return
     */
    private boolean checkGPSIsOpen() {
        boolean isOpen;
        LocationManager locationManager = (LocationManager) this
                .getSystemService(Context.LOCATION_SERVICE);
        isOpen = locationManager.isProviderEnabled(android.location.LocationManager.GPS_PROVIDER);
        return isOpen;
    }

    /**
     * 跳转GPS设置
     */
    private void openGPSSettings() {
        if (checkGPSIsOpen()) {
            startLocation(); //自己写的定位方法
        } else {
            new AlertDialog.Builder(this)
                    .setTitle(R.string.notifyTitle)
                    .setMessage(R.string.gpsNotifyMsg)
                    .setPositiveButton(R.string.setting,
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    //跳转GPS设置界面
                                    Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                                    startActivityForResult(intent, GPS_REQUEST_CODE);
                                }
                            })
                    .setCancelable(false)
                    .show();
        }
    }

    public void showPermission() {
        Log.d("dddd", "showPermission PERMISSION_DENIED: " + PackageManager.PERMISSION_DENIED
                + ",ACCESS_COARSE_LOCATION:" + Manifest.permission.ACCESS_COARSE_LOCATION
                + ",PERMISSION_GRANTED:" + PackageManager.PERMISSION_GRANTED);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE)
                != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(this, "没有权限,请手动开启定位权限", Toast.LENGTH_SHORT).show();
            // 申请一个（或多个）权限，并提供用于回调返回的获取码（用户定义）
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.READ_PHONE_STATE}, READ_PHONE_STATE);
        } else {
            openGPSSettings();
        }
    }

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

    //前往筛选点
    private void gotoAroundPlace() {
        AroundDialog aroundDialog = new AroundDialog(MapActivity.this, R.style.dialog, new AroundDialog.OnCloseListener() {
            @Override
            public void onClick(Dialog dialog, int index) {
                if (index != 0)
                    chooseAroundPlace(index);
                dialog.dismiss();
            }
        });
        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
        Window window = aroundDialog.getWindow();
        layoutParams.copyFrom(window.getAttributes());
        if (isShowLuxian)
            layoutParams.y = 188;
        else
            layoutParams.y = 448;
        layoutParams.gravity = Gravity.TOP;
        window.setAttributes(layoutParams);
        aroundDialog.show();
    }

    //根据下表筛选marker点
    private void chooseAroundPlace(int index) {

        aMap.clear();
        switch (index) {
            case 1:
                //TODO 全部
                for (Integer i : mPointsEntityMap.keySet()) {
                    setOverLay(mPointsEntityMap.get(i).getType(), mPointsEntityMap.get(i));
                }
                break;
            case 2:
                setMarkerLay(2);
                break;
            case 3:
                setMarkerLay(3);
                break;
            case 4:
                setMarkerLay(4);
                break;
            case 5:
                setMarkerLay(5);
                break;
            case 6:
                setMarkerLay(6);
                break;
            case 7:
                setMarkerLay(7);
                break;
            case 8:
                setMarkerLay(8);
                break;
        }
    }

    private void setMarkerLay(int index) {
        for (Integer i : mPointsEntityMap.keySet()) {
            if (index == mPointsEntityMap.get(i).getType())
                setOverLay(mPointsEntityMap.get(i).getType(), mPointsEntityMap.get(i));
        }
    }

    private void setOverLay(int index, ScenicPointsEntity.PointsBean pointsBean) {

        MarkerOptions markerOption = new MarkerOptions();
        markerOption.position(new LatLng(pointsBean.getLatitude(), pointsBean.getLongitude()));
        markerOption.draggable(true);//设置Marker可拖动
        markerOption.title(pointsBean.getId() + "");
        markerOption.icon(BitmapManager.getInstance().getBitmapDescriptor(index));
        // 将Marker设置为贴地显示，可以双指下拉地图查看效果
        markerOption.setFlat(true);//设置marker平贴地图效果
        Marker marker = aMap.addMarker(markerOption);
        mMarkerMap.put(pointsBean.getId(), marker);

    }

    //前往附近点
    private void gotoNearPlace() {
        new NearDialog(MapActivity.this, R.style.dialog, new NearDialog.OnCloseListener() {
            @Override
            public void onClick(final Dialog dialog, String ms, final int index) {
                Log.d("dddd", "onClick: " + index);
                if (index != 0)
                    new GotoWhereDialog(MapActivity.this, R.style.dialog, new GotoWhereDialog.OnCloseListener() {
                        @Override
                        public void onClick(Dialog gotoDialog, boolean confirm) {
                            if (confirm) {
                                //TODO 去最近卫生间
                                goFujinPlace(index);
                                dialog.dismiss();
                            } else {
                            }
                            gotoDialog.dismiss();

                        }
                    }).setTitle(ms).show();
                else
                    dialog.dismiss();
            }
        }).show();

    }

    /**
     * 最近的地方
     *
     * @param index 1、卫生间，2、购物，3、停车场，4、餐厅，5、酒店住宿，6、大门
     */
    private void goFujinPlace(int index) {
        LatLng secLatlng = new LatLng(lp.getLatitude(), lp.getLongitude());
        float minDistance = AMapUtils.calculateLineDistance(startLatlng, new LatLng(0.0, 0.0));
        switch (index) {
            case 1:
                calculateLatlng(3, minDistance, secLatlng);
                break;
            case 2:
                calculateLatlng(4, minDistance, secLatlng);
                break;
            case 3:
                calculateLatlng(7, minDistance, secLatlng);
                break;
            case 4:
                calculateLatlng(5, minDistance, secLatlng);
                break;
            case 5:
                calculateLatlng(6, minDistance, secLatlng);
                break;
            case 6:
                calculateLatlng(8, minDistance, secLatlng);
                break;
        }
    }

    private void calculateLatlng(int index, float minDistance, LatLng secLatlng) {
        for (Integer integer : mPointsEntityMap.keySet()) {
            if (index == mPointsEntityMap.get(integer).getType()) {
                float lineDistance = AMapUtils.calculateLineDistance(startLatlng,
                        new LatLng(mPointsEntityMap.get(integer).getLatitude(), mPointsEntityMap.get(integer).getLongitude()));
                if (minDistance > lineDistance) {
                    minDistance = lineDistance;
                    mPointsEntity = mPointsEntityMap.get(integer);
                    secLatlng = new LatLng(mPointsEntityMap.get(integer).getLatitude(), mPointsEntityMap.get(integer).getLongitude());
                    Log.d("dddd", "minDistance latitude: " + secLatlng.latitude + ",longitude:" + secLatlng.longitude);
                }
            }
        }
        Log.d("dddd", "calculateLatlng latitude: " + secLatlng.latitude + ",longitude:" + secLatlng.longitude);
        Intent intent=new Intent(this, WalkNaviActivity.class);
        intent.putExtra("enty",mPointsEntity);
        intent.putExtra("startLatlng",secLatlng);
        startActivity(intent);
        aMap.moveCamera(CameraUpdateFactory.newLatLngZoom(secLatlng, mCurrentZoom));
    }

    @Override
    public void onMyLocationChange(Location location) {
        if (location != null) {
            startLatlng = new LatLng(location.getLatitude(), location.getLongitude());
            if (mPointsEntity != null) {
                String distance = String.valueOf(AMapUtils.calculateLineDistance(startLatlng, new LatLng(mPointsEntity.getLatitude(), mPointsEntity.getLongitude())));
                mjuli.setText((int) (Double.parseDouble(distance)) + "M");
                NotificationCenter.defaultCenter().publish(new DistanceEvent(distance));
            }

            if (isFirstLoc) {
                LatLng ll = null;
                ll = getMostAccuracyLocation(location);
                if (ll == null) {
                    return;
                }
                isFirstLoc = false;
                points.add(ll);//加入集合
                last = ll;

                return;//画轨迹最少得2个点，首地定位到这里就可以返回了
            }
            //从第二个点开始
            LatLng ll = new LatLng(location.getLatitude(), location.getLongitude());
            //sdk回调gps位置的频率是1秒1个，位置点太近动态画在图上不是很明显，可以设置点之间距离大于为5米才添加到集合中
            if (AMapUtils.calculateLineDistance(last, ll) < 5) {
                return;
            }

            points.add(ll);//如果要运动完成后画整个轨迹，位置点都在这个集合中
            last = ll;
            if (isShowPolyLine) {
                if (polyline != null)
                    polyline.remove();
                //将points集合中的点绘制轨迹线条图层，显示在地图上
                polyline = aMap.addPolyline(new PolylineOptions().
                        addAll(points).width(15).color(0xAAD1D1D1));
            }
        }
    }

    /**
     * 首次定位很重要，选一个精度相对较高的起始点
     * 你可以将location.getRadius()>25中的过滤半径调大，比如>40，
     * 并且将连续5个点之间的距离DistanceUtil.getDistance(last, ll ) > 5也调大一点，比如>10，
     * gps的精度半径一般是10-50米
     */
    private LatLng getMostAccuracyLocation(Location location) {

        LatLng ll = new LatLng(location.getLatitude(), location.getLongitude());
        Log.d("dddd", "getMostAccuracyLocation getAccuracy: " + AMapUtils.calculateLineDistance(last, ll));

        if (AMapUtils.calculateLineDistance(last, ll) > 20) {
            last = ll;
            points.clear();//有任意连续两点位置大于10，重新取点
            return null;
        }
        points.add(ll);
        last = ll;
        return ll;
    }

    AMap.OnMarkerClickListener mMarkerClickListener = new AMap.OnMarkerClickListener() {
        @Override
        public boolean onMarkerClick(Marker marker) {
            mPointsEntity = mPointsEntityMap.get(Integer.parseInt(marker.getTitle()));
            Log.d("dddd", "onPointClick: " + marker.getTitle() + ",getPointId:" + mPointsEntity.getId() + "，mp3:" + mPointsEntity.getMp3());
            if (mPointsEntity.getType() == 2 && !TextUtils.isEmpty(mPointsEntity.getMp3()) && (mPointsEntity.getMp3()).endsWith(".mp3")) {
                addAnimMarker(mPointsEntity);
                playMarkerAudio(mPointsEntity);
                notifyRecyChanged();
            }
            return true;
        }
    };

    private void notifyRecyChanged() {
        if (voicePoints != null && voicePoints.size() > 0) {
            for (int i = 0; i < voicePoints.size(); i++) {
                voicePoints.get(i).setShow(false);
                if (mPointsEntity.getId() == voicePoints.get(i).getId()) {
                    voicePoints.get(i).setShow(true);
                    tour_list.smoothScrollToPosition(i);
                }
            }
            mAdapter.notifyDataSetChanged();
        }
    }

    private void playMarkerAudio(ScenicPointsEntity.PointsBean pointsBean) {
        EventBus.getDefault().post(new PlayEvent(pointsBean.getMp3(), true));
        tour_name.setText(pointsBean.getPointName());
        if (startLatlng != null) {
            String distance = String.valueOf(AMapUtils.calculateLineDistance(startLatlng, new LatLng(pointsBean.getLatitude(), pointsBean.getLongitude())));
            mjuli.setText((int) (Double.parseDouble(distance)) + "M");
            NotificationCenter.defaultCenter().publish(new DistanceEvent(distance));
        }
    }

    private void addAnimMarker(ScenicPointsEntity.PointsBean pointsBean) {
        for (Integer integer : mMarkerMap.keySet()) {
            mMarkerMap.get(integer).remove();
            setOverLay(mPointsEntityMap.get(integer).getType(), mPointsEntityMap.get(integer));
        }
        mMarkerMap.get(pointsBean.getId()).remove();
        MarkerOptions markerOption = new MarkerOptions();
        markerOption.position(new LatLng(pointsBean.getLatitude(), pointsBean.getLongitude()))
                .icons(BitmapManager.getInstance().getBitmapDescriptorOverlay())
                .title(pointsBean.getId() + "");
        Marker marker1 = aMap.addMarker(markerOption);
        mMarkerMap.put(pointsBean.getId(), marker1);
    }

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
    public void onStop() {
        super.onStop();

    }

    @Override
    public void onDestroy() {
        if (DataUtils.isBlue(MapActivity.this) && mMinewBeaconManager != null) {
            mMinewBeaconManager.stopScan();
        }
        EventBus.getDefault().unregister(this);
        unbindService(serviceConnection);
        NotificationCenter.defaultCenter().unsubscribe(ShowPoupEvent.class, disevent);
        if (isBind) {
            this.unbindService(conn);
        }
        //在activity执行onDestroy时执行mMapView.onDestroy()，销毁地图
        mMapView.onDestroy();
        // 关闭定位图层
        aMap.setMyLocationEnabled(false);
        mMapView.getMap().clear();
        mMapView.onDestroy();
        mMapView = null;
        if (mopupWindow != null && mopupWindow.isShowing()) {
            mopupWindow.dismiss();
        }
        super.onDestroy();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (DataUtils.isBlue(MapActivity.this) && mMinewBeaconManager != null) {
            mMinewBeaconManager.startScan();
        }
        //在activity执行onResume时执行mMapView.onResume ()，重新绘制加载地图
        mMapView.onResume();
        mPointsEntity = SaveObjectUtils.getInstance(getApplicationContext()).getObject(Constants.NOW_POION, null);
        checkPause();
    }

    @Override
    public void onPause() {
        super.onPause();
        //在activity执行onPause时执行mMapView.onPause ()，暂停地图的绘制
        mMapView.onPause();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        //在activity执行onSaveInstanceState时执行mMapView.onSaveInstanceState (outState)，保存地图当前的状态
        mMapView.onSaveInstanceState(outState);
    }

    //Android6.0申请权限的回调方法
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            // requestCode即所声明的权限获取码，在checkSelfPermission时传入
            case READ_PHONE_STATE:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // 获取到权限，作相应处理（调用定位SDK应当确保相关权限均被授权，否则可能引起定位失败）
                    openGPSSettings();
                } else {
                    // 没有获取到权限，做特殊处理
                    Toast.makeText(getApplicationContext(), "获取位置权限失败，请手动开启", Toast.LENGTH_SHORT).show();
                }
                break;
            default:
                break;

        }
    }

    //刷新距离
    ShowSubscriber disevent = new ShowSubscriber() {
        @Override
        public void onEvent(ShowPoupEvent event) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    shouPoup();
                }
            }, 50);
        }
    };

    private void shouPoup() {
        mopupWindow = new LineListPoupWindow(MapActivity.this, startLatlng, mPointsList, itemsOnClick);
        mopupWindow.showAtLocation(this.findViewById(R.id.mapView),
                Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
    }

    private LineListPoupWindow.ItemInclick itemsOnClick = new LineListPoupWindow.ItemInclick() {
        @Override
        public void ItemClick(int index) {
            mopupWindow.dismiss();
            mPointsEntity = mPointsList.get(index);
            if (mPointsEntity.getType() == 2 && !TextUtils.isEmpty(mPointsEntity.getMp3())) {
                addAnimMarker(mPointsEntity);
                playMarkerAudio(mPointsEntity);
            }
            aMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(mPointsEntity.getLatitude(), mPointsEntity.getLongitude()), mCurrentZoom));
            if (!isShowLuxian)
                notifyRecyChanged();
        }
    };

    //蓝牙模块---------------------------------------------------------------------------------------------------------------
    private void showBLEDialog() {
        if (!isBlue) {
            Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableIntent, REQUEST_ENABLE_BT);
        }
    }

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
        if (DataUtils.isBlue(MapActivity.this)) {
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
                if (minewBeacons != null && minewBeacons.size() > 0 && yyCheckBox.isChecked()) {
//                    String distance = String.valueOf(AMapUtils.calculateLineDistance(mStartPoint, mEndPoint));
                    for (MinewBeacon beacon : minewBeacons) {
                        String majer = beacon.getBeaconValue(BeaconValueIndex.MinewBeaconValueIndex_Major).getStringValue();
                        for (int i = 0; i < mPointsList.size(); i++) {
                            String jieguo = String.valueOf(mPointsList.get(i).getMajor());
                            if (jieguo.equals(majer)) {
                                if (!TextUtils.isEmpty(mPointsList.get(i).getMp3()) && (mPointsList.get(i).getMp3()).endsWith(".mp3")) {
                                    QueueManager.update(new QueueBean(mPointsList.get(i).getMp3()));
                                    if (TaskCationManager.isPlay(i) || TaskCationManager.isNow(i)) {

                                    } else {
                                        chanVioce(i);
                                    }
                                    break;
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
                        Toast.makeText(MapActivity.this, "蓝牙打开", Toast.LENGTH_SHORT).show();
                        break;
                    case BluetoothStatePowerOff:
                        Toast.makeText(MapActivity.this, "蓝牙关闭", Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        });
    }

    ExxitDialog mDialog;

    //------------------------切换语音
    long sTime = 0;

    private void chanVioce(final int positon) {
        //没有播放过又不是当前播放的
        final String clickpath = mPointsList.get(positon).getMp3();
        if (System.currentTimeMillis() - sTime < 7000) {
            return;
        }
        sTime = System.currentTimeMillis();
        if (mycontrol.isPlay()) {
            TaskCationManager.addQuen(positon);
            if (mDialog != null && mDialog.isShowing()) {
                return;
            }

            mDialog = new ExxitDialog(MapActivity.this, R.style.dialog, "正在介绍当前景点,是否切换至下一个点?", new ExxitDialog.OnCloseListener() {
                @Override
                public void onClick(Dialog dialog, boolean confirm) {
                    if (confirm) {
                        mDialog.dismiss();
                        EventBus.getDefault().post(new PlayEvent(clickpath, true));
                        TaskCationManager.updateNowPlay(positon);
                        QueueManager.clear();
                        mPointsEntity = mPointsList.get(positon);
                        tour_name.setText(mPointsEntity.getPointName());
                    } else {
                        QueueManager.clear();
                        mDialog.dismiss();
                    }
                }
            });
            mDialog.setPositiveButton("切换").show();
        } else {
            EventBus.getDefault().post(new PlayEvent(clickpath, true));
            TaskCationManager.updateNowPlay(positon);
            mPointsEntity = mPointsList.get(positon);
            tour_name.setText(mPointsEntity.getPointName());
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
            case GPS_REQUEST_CODE:
                openGPSSettings();
                break;

        }
    }


    private void sendStepMsg(int steps) {
        if (!StepData.getInstance(MapActivity.this).isThanNow(5)) {
            return;
        }
        BaseApi.getJavaLoginDefaultService(MapActivity.this).sendStep(steps)
                .map(new JavaRxFunction<Boolean>())
                .compose(RxSchedulers.<Boolean>io_main())
                .subscribe(new RxObserver<Boolean>(MapActivity.this, TAG, 1, false) {
                    @Override
                    public void onSuccess(int whichRequest, Boolean aBoolean) {
                        StepData.getInstance(MapActivity.this).setStepDataValue(System.currentTimeMillis());

                    }

                    @Override
                    public void onError(int whichRequest, Throwable e) {

                    }
                });
    }

}

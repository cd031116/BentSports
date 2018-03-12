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
import android.os.IBinder;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
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
import com.amap.api.maps.model.BitmapDescriptor;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.CameraPosition;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.MultiPointItem;
import com.amap.api.maps.model.MultiPointOverlay;
import com.amap.api.maps.model.MultiPointOverlayOptions;
import com.amap.api.maps.model.MyLocationStyle;
import com.amap.api.maps.model.Polyline;
import com.amap.api.maps.model.PolylineOptions;
import com.amap.api.services.core.LatLonPoint;
import com.cn.bent.sports.R;
import com.cn.bent.sports.api.BaseApi;
import com.cn.bent.sports.base.BaseActivity;
import com.cn.bent.sports.base.BaseConfig;
import com.cn.bent.sports.bean.MajorBean;
import com.cn.bent.sports.bean.PlayBean;
import com.cn.bent.sports.bean.PlayEvent;
import com.cn.bent.sports.bean.PointsEntity;
import com.cn.bent.sports.bean.RailBean;
import com.cn.bent.sports.bean.StartEvent;
import com.cn.bent.sports.evevt.DistanceEvent;
import com.cn.bent.sports.sensor.UpdateUiCallBack;
import com.cn.bent.sports.utils.Constants;
import com.cn.bent.sports.utils.DataUtils;
import com.cn.bent.sports.utils.SaveObjectUtils;
import com.cn.bent.sports.utils.ToastUtils;
import com.cn.bent.sports.view.service.MusicService;
import com.cn.bent.sports.view.service.StepService;
import com.cn.bent.sports.widget.AroundDialog;
import com.cn.bent.sports.widget.ExxitDialog;
import com.cn.bent.sports.widget.GotoWhereDialog;
import com.cn.bent.sports.widget.NearDialog;
import com.minew.beacon.BeaconValueIndex;
import com.minew.beacon.BluetoothState;
import com.minew.beacon.MinewBeacon;
import com.minew.beacon.MinewBeaconManager;
import com.zhl.network.RxObserver;
import com.zhl.network.RxSchedulers;
import com.zhl.network.huiqu.HuiquRxFunction;

import org.aisen.android.component.eventbus.NotificationCenter;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by dawn on 2018/3/8.
 */

public class MapActivity extends BaseActivity implements AMap.OnMyLocationChangeListener {
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
    private boolean isFirstLoc = true; // 是否首次定位
    private boolean isShowRec = true; // 是否显示列表
    private boolean isShowLuxian = true; // 是否显示路线
    float mCurrentZoom = 18f;//默认地图缩放比例值

    List<LatLng> points = new ArrayList<LatLng>();//位置点集合
    LatLng last = new LatLng(0, 0);//上一个定位点
    private AnimationDrawable animationDrawable;
    private AMap aMap;
    private LatLonPoint lp = new LatLonPoint(28.008977, 113.088063);//
    private List<PointsEntity> mPointsList = new ArrayList<PointsEntity>();
    ServiceConnection serviceConnection;
    MusicService.MusicController mycontrol;
    private boolean isBind = false;
    private static final int READ_PHONE_STATE = 100;
    private MinewBeaconManager mMinewBeaconManager;
    private static final int REQUEST_ENABLE_BT = 2;
    private boolean isBlue = false;
    private static final int GPS_REQUEST_CODE = 10;
    private  PointsEntity mPointsEntity;
    private   LatLng startLatlng;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mMapView = (MapView) findViewById(R.id.mapView);
        mMapView.onCreate(savedInstanceState);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.marker_map_layout;
    }

    @Override
    public void initView() {
        super.initView();
        EventBus.getDefault().register(this);
        if (aMap == null) {
            aMap = mMapView.getMap();
        }
        aMap.showMapText(false);//关闭文字
//        aMap.showBuildings(false);//关闭3d楼块
        aMap.getUiSettings().setZoomControlsEnabled(false);//去掉高德地图右下角隐藏的缩放按钮
        aMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(lp.getLatitude(), lp.getLongitude()), 17));
        // 绑定海量点点击事件
        aMap.setOnMultiPointClickListener(multiPointClickListener);
        // 绑定 Marker 被点击事件
        aMap.setOnMarkerClickListener(markerClickListener);
        stepCheckBox.setOnCheckedChangeListener(stepCheckedChangeListener);
        yyCheckBox.setOnCheckedChangeListener(yyCheckedChangeListener);
        setupService();
        Intent intent = new Intent(this, MusicService.class);
        startService(intent);
        if (serviceConnection == null) {
            serviceConnection = new ServiceConnection() {
                @Override
                public void onServiceConnected(ComponentName name, IBinder service) {
                    mycontrol = (MusicService.MusicController) service;
                    Log.i("dddd", "mycontrol");
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
            Log.d("StepService", "stepService=");
            //设置步数监听回调
            stepService.registerCallback(new UpdateUiCallBack() {
                @Override
                public void updateUi(int stepCount) {
                    Log.d("StepService", "stepCount=" + stepCount);
                    waik_num.setText(stepCount + "");
                }
            });
        }

        /**
         * 当与Service之间的连接丢失的时候会调用该方法，
         * 这种情况经常发生在Service所在的进程崩溃或者被Kill的时候调用，
         * 此方法不会移除与Service的连接，当服务重新启动的时候仍然会调用 onServiceConnected()。
         * @param name 丢失连接的组件名称
         */
        @Override
        public void onServiceDisconnected(ComponentName name) {
            Log.d("StepService", "ComponentName=");
        }
    };


    private void checkPause() {
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
            yinp_bf.setBackgroundResource(R.drawable.play_anim);
            animationDrawable = (AnimationDrawable) yinp_bf.getBackground();
            if (animationDrawable != null && !animationDrawable.isRunning()) {
                animationDrawable.start();
            }
        } else {
            if (animationDrawable != null && animationDrawable.isRunning()) {
                animationDrawable.stop();
            }
            yinp_bf.setBackgroundResource(R.drawable.tizhibf);
        }
    }

    @Override
    public void initData() {
        super.initData();
        getdot();

    }

    private void getdot() {
        showAlert("......", true);
        BaseApi.getDefaultService(this).getFenceAndDot()
                .map(new HuiquRxFunction<RailBean>())
                .compose(RxSchedulers.<RailBean>io_main())
                .subscribe(new RxObserver<RailBean>(MapActivity.this, "login", 1, false) {
                    @Override
                    public void onSuccess(int whichRequest, RailBean info) {
                        dismissAlert();
                        List<LatLng> pointLatLngs = new ArrayList<LatLng>();//位置点集合
                        for (int i = 0; i < info.getMp3_tag().size(); i++) {
                            PointsEntity pointsEntity = new PointsEntity();
                            pointsEntity.setPointId(info.getMp3_tag().get(i).getPlace_id());
                            pointsEntity.setType(i / 2 + 2);
                            PointsEntity.LocationBean locationBean = new PointsEntity.LocationBean();
                            locationBean.setLatitude(Double.parseDouble(info.getMp3_tag().get(i).getLatitude()));
                            locationBean.setLongitude(Double.parseDouble(info.getMp3_tag().get(i).getLongitude()));
                            pointsEntity.setLocation(locationBean);
                            pointsEntity.setMp3(info.getMp3_tag().get(i).getMp3());
                            mPointsList.add(pointsEntity);
                            LatLng latLng = new LatLng(Double.parseDouble(info.getMp3_tag().get(i).getLatitude()), Double.parseDouble(info.getMp3_tag().get(i).getLongitude()));
                            pointLatLngs.add(latLng);
                        }
                        Polyline polyline = aMap.addPolyline(new PolylineOptions().
                                addAll(pointLatLngs).width(14).color(0xAA0000FF));
                        polyline.remove();
                        for (int i = 0; i < pointLatLngs.size(); i++) {
                            MarkerOptions markerOption = new MarkerOptions();
                            markerOption.position(new LatLng(pointLatLngs.get(i).latitude, pointLatLngs.get(i).longitude));
                            markerOption.draggable(false);
                            markerOption.icon(BitmapManager.getInstance().getBitmapDescriptor(i + 1));
                            aMap.addMarker(markerOption);
                        }
                    }

                    @Override
                    public void onError(int whichRequest, Throwable e) {
                        dismissAlert();
                        ToastUtils.showLongToast(MapActivity.this, e.getMessage());
                    }
                });
    }

    /**
     * 步行轨迹
     */
    OnCheckedChangeListener stepCheckedChangeListener = new OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
            if (isChecked) {

            } else {

            }
        }
    };

    /**
     * 自动讲解
     */
    OnCheckedChangeListener yyCheckedChangeListener = new OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
            if (isChecked) {

            } else {

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
                if (isShowLuxian)
                    if (Build.VERSION.SDK_INT >= 23) {
                        showPermission();
                    } else {
                        openGPSSettings();//定位方法
                    }
                else
                    aMap.setMyLocationEnabled(false);
                break;
            case R.id.luxian:
                if (isShowLuxian) {
                    setLuxianPng(isShowLuxian);
                    isShowLuxian = false;
                } else {
                    setLuxianPng(isShowLuxian);
                    isShowLuxian = true;
                }
                break;
            case R.id.zhankai:
                Intent intent1 = new Intent(this, BottomPlayActivity.class);
                intent1.putExtra("enty",mPointsEntity);
                intent1.putExtra("startLatlng",startLatlng);
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

    private void setLuxianPng(boolean isShowLuxian) {
        Drawable drawable = null;
        if (isShowLuxian)
            drawable = getResources().getDrawable(R.drawable.luxian_1);
        else
            drawable = getResources().getDrawable(R.drawable.close_line);
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
            //没有打开则弹出对话框
            new AlertDialog.Builder(this)
                    .setTitle(R.string.notifyTitle)
                    .setMessage(R.string.gpsNotifyMsg)
                    // 拒绝, 退出应用
                    .setNegativeButton(R.string.cancel,
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    finish();
                                }
                            })

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
        Log.d("dddd", "showPermission: " + ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                + ",ACCESS_FINE_LOCATION:" + ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                + ",READ_PHONE_STATE:" + ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE)
                + ",PERMISSION_GRANTED:" + PackageManager.PERMISSION_GRANTED);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE)
                != PackageManager.PERMISSION_GRANTED) {
            Log.d("dddd", "showPermission 没有权限,请手动开启定位权限: ");
            Toast.makeText(this, "没有权限,请手动开启定位权限", Toast.LENGTH_SHORT).show();
            // 申请一个（或多个）权限，并提供用于回调返回的获取码（用户定义）
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.READ_PHONE_STATE}, READ_PHONE_STATE);
        } else {
            Log.d("dddd", "showPermission openGPSSettings: ");
            openGPSSettings();
        }
    }

    public void startLocation() {
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
        aMap.setOnCameraChangeListener(new AMap.OnCameraChangeListener() {
            @Override
            public void onCameraChange(CameraPosition cameraPosition) {

            }

            @Override
            public void onCameraChangeFinish(CameraPosition cameraPosition) {
                mCurrentZoom = cameraPosition.zoom;//获取手指缩放地图后的值
            }
        });
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
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        Window window = aroundDialog.getWindow();
        lp.copyFrom(window.getAttributes());
        if (isShowRec)
            lp.y = 448;
        else
            lp.y = 188;
        lp.gravity = Gravity.TOP;
        window.setAttributes(lp);
        aroundDialog.show();
    }

    //根据下表筛选marker点
    private void chooseAroundPlace(int index) {
        switch (index) {
            case 1:
                //TODO 全部
                aMap.clear();
                for (PointsEntity pointsEntity : mPointsList) {
//                    MarkerOptions markerOption = new MarkerOptions();
//                    markerOption.position(new LatLng(pointsEntity.getLocation().getLatitude(), pointsEntity.getLocation().getLongitude()));
//                    markerOption.title(pointsEntity.getPointId());
//                    markerOption.draggable(false);
//                    markerOption.icon(BitmapManager.getInstance().getBitmapDescriptor(pointsEntity.getType()));
//                    aMap.addMarker(markerOption);
                    setOverLay(Integer.parseInt(pointsEntity.getPointId()));
                }
                break;
            case 2:
                setOverLay(2);
                break;
            case 3:
                setOverLay(3);
                break;
            case 4:
                setOverLay(4);
                break;
            case 5:
                setOverLay(5);
                break;
            case 6:
                setOverLay(6);
                break;
            case 7:
                setOverLay(7);
                break;
            case 8:
                setOverLay(8);
                break;
        }
    }

    private void setOverLay(int index) {
        aMap.clear();
        MultiPointOverlayOptions overlayOptions = new MultiPointOverlayOptions();
        BitmapDescriptor bitmapDescriptor = BitmapManager.getInstance().getBitmapDescriptor(index);
        overlayOptions.icon(bitmapDescriptor);//设置图标
        MultiPointOverlay multiPointOverlay = aMap.addMultiPointOverlay(overlayOptions);
        List<MultiPointItem> multiPointItemList = new ArrayList<MultiPointItem>();
        for (PointsEntity pointsEntity : mPointsList) {
            if (pointsEntity.getType() == index) {
                LatLng latLng = new LatLng(pointsEntity.getLocation().getLatitude(), pointsEntity.getLocation().getLongitude());
                MultiPointItem multiPointItem = new MultiPointItem(latLng);
                multiPointItem.setCustomerId(pointsEntity.getPointId());
                multiPointItem.setObject(pointsEntity);
                multiPointItemList.add(multiPointItem);
            }
        }
        multiPointOverlay.setItems(multiPointItemList);
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
     * @param index 1、卫生间，2、卫生间，3、卫生间，4、卫生间，5、卫生间，6、卫生间
     */
    private void goFujinPlace(int index) {
        switch (index) {
            case 1:
                break;
            case 2:
                break;
            case 3:
                break;
            case 4:
                break;
            case 5:
                break;
            case 6:
                break;
        }
    }

    @Override
    public void onMyLocationChange(Location location) {
        if (location != null) {
            startLatlng=new LatLng(location.getLatitude(), location.getLongitude());
            if(mPointsEntity!=null){
                String distance = String.valueOf(AMapUtils.calculateLineDistance(startLatlng, new LatLng(mPointsEntity.getLocation().getLatitude(),mPointsEntity.getLocation().getLongitude())))+"M";
                mjuli.setText(distance);
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
//清除上一次轨迹，避免重叠绘画
            mMapView.getMap().clear();
            //将points集合中的点绘制轨迹线条图层，显示在地图上
            Polyline polyline = aMap.addPolyline(new PolylineOptions().
                    addAll(points).width(15).color(0xAAD1D1D1));

        }
    }

    /**
     * 首次定位很重要，选一个精度相对较高的起始点
     * 注意：如果一直显示gps信号弱，说明过滤的标准过高了，
     * 你可以将location.getRadius()>25中的过滤半径调大，比如>40，
     * 并且将连续5个点之间的距离DistanceUtil.getDistance(last, ll ) > 5也调大一点，比如>10，
     * 这里不是固定死的，你可以根据你的需求调整，如果你的轨迹刚开始效果不是很好，你可以将半径调小，两点之间距离也调小，
     * gps的精度半径一般是10-50米
     */
    private LatLng getMostAccuracyLocation(Location location) {

        Log.d("dddd", "getMostAccuracyLocation getAccuracy: " + location.getAccuracy());
        if (location.getAccuracy() > 40) {//gps位置精度大于40米的点直接弃用
            return null;
        }

        LatLng ll = new LatLng(location.getLatitude(), location.getLongitude());

//        if (AMapUtils.calculateLineDistance(last, ll) > 10) {
//            last = ll;
//            points.clear();//有任意连续两点位置大于10，重新取点
//            return null;
//        }
        points.add(ll);
        last = ll;
        //有5个连续的点之间的距离小于10，认为gps已稳定，以最新的点为起始点
//        if (points.size() >= 5) {
//            points.clear();
//            return ll;
//        }
        return ll;
    }


    // 定义海量点点击事件
    AMap.OnMultiPointClickListener multiPointClickListener = new AMap.OnMultiPointClickListener() {
        // 海量点中某一点被点击时回调的接口
        // 返回 true 则表示接口已响应事件，否则返回false
        @Override
        public boolean onPointClick(MultiPointItem pointItem) {
            Log.d("dddd", "onPointClick: " + pointItem.getLatLng().latitude);
             mPointsEntity = (PointsEntity) pointItem.getObject();
            Log.d("dddd", "onPointClick: " + pointItem.getCustomerId() + ",getPointId:" + mPointsEntity.getPointId() + "，mp3:" + mPointsEntity.getMp3());
            EventBus.getDefault().post(new PlayEvent(mPointsEntity.getMp3(), true));
            return false;
        }
    };
    // 定义 Marker 点击事件监听
    AMap.OnMarkerClickListener markerClickListener = new AMap.OnMarkerClickListener() {
        // marker 对象被点击时回调的接口
        // 返回 true 则表示接口已响应事件，否则返回false
        @Override
        public boolean onMarkerClick(Marker marker) {
            Log.d("dddd", "onMarkerClick: " + marker.getTitle());
            return false;
        }
    };

    @Override
    public void onStop() {
        super.onStop();
        if (DataUtils.isBlue(MapActivity.this) && mMinewBeaconManager != null) {
            mMinewBeaconManager.stopScan();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        unbindService(serviceConnection);
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
    }

    @Override
    public void onResume() {
        super.onResume();
        if (DataUtils.isBlue(MapActivity.this) && mMinewBeaconManager != null) {
            mMinewBeaconManager.startScan();
        }
        //在activity执行onResume时执行mMapView.onResume ()，重新绘制加载地图
        mMapView.onResume();
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
                ToastUtils.showShortToast(MapActivity.this, "手机不支持蓝牙");
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
                if (minewBeacons != null && minewBeacons.size() > 0) {
//                    String distance = String.valueOf(AMapUtils.calculateLineDistance(mStartPoint, mEndPoint));
                    for (MinewBeacon beacon : minewBeacons) {
                        String majer = beacon.getBeaconValue(BeaconValueIndex.MinewBeaconValueIndex_Major).getStringValue() + beacon.getBeaconValue(BeaconValueIndex.MinewBeaconValueIndex_Minor).getStringValue();
                        for (int i = 0; i < mPointsList.size(); i++) {
                            for (PointsEntity.IBeaconsBean cheeck : mPointsList.get(i).getIBeacons()) {
                                String jieguo = String.valueOf(cheeck.getMajor()) + String.valueOf(cheeck.getMinor());
                                if (jieguo.equals(majer)) {
                                    chanVioce(i);
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
    private void chanVioce(final int positon) {
      final String clickpath=   mPointsList.get(positon).getMp3();
        if (mycontrol.isPlay()) {
            BaseConfig bg = BaseConfig.getInstance(getApplicationContext());
            String nowpaths = bg.getStringValue(Constants.NOW_PLAY, "");
            if (nowpaths.equals(clickpath)) {
                return;
            }
            if (mDialog != null && mDialog.isShowing()) {
                return;
            }
            mDialog = new ExxitDialog(MapActivity.this, R.style.dialog, "正在介绍当前景点,是否切换至下一个点?", new ExxitDialog.OnCloseListener() {
                @Override
                public void onClick(Dialog dialog, boolean confirm) {
                    if (confirm) {
                        dialog.dismiss();
                        EventBus.getDefault().post(new PlayEvent(clickpath, true));
                        mPointsEntity=mPointsList.get(positon);
                        tour_name.setText(mPointsEntity.getName());
                    } else {
                        dialog.dismiss();
                    }
                }
            });
            mDialog.setPositiveButton("切换").show();

        } else {
            EventBus.getDefault().post(new PlayEvent(clickpath, false));
            mPointsEntity=mPointsList.get(positon);
            tour_name.setText(mPointsEntity.getName());
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

    private void ExitFunction() {
        new ExxitDialog(MapActivity.this, R.style.dialog, "确定退出AI旅行?", new ExxitDialog.OnCloseListener() {
            @Override
            public void onClick(Dialog dialog, boolean confirm) {
                if (confirm) {
                    dialog.dismiss();
                    MapActivity.this.finish();
                } else {
                    dialog.dismiss();
                }
            }
        }).show();
    }


    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            ExitFunction();
            return true;
        }else {
            return super.onKeyDown(keyCode, event);
        }
    }
}

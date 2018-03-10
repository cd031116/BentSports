package com.cn.bent.sports.view.activity;

import android.app.Dialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
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
import com.cn.bent.sports.bean.PlayBean;
import com.cn.bent.sports.bean.PointsEntity;
import com.cn.bent.sports.bean.RailBean;
import com.cn.bent.sports.bean.StartEvent;
import com.cn.bent.sports.utils.Constants;
import com.cn.bent.sports.utils.NiceUtil;
import com.cn.bent.sports.utils.SaveObjectUtils;
import com.cn.bent.sports.utils.ToastUtils;
import com.cn.bent.sports.view.service.MusicService;
import com.cn.bent.sports.widget.AroundDialog;
import com.cn.bent.sports.widget.GotoWhereDialog;
import com.cn.bent.sports.widget.NearDialog;
import com.zhl.network.RxObserver;
import com.zhl.network.RxSchedulers;
import com.zhl.network.huiqu.HuiquRxFunction;

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

public class MapActivity extends BaseActivity implements AMap.OnMyLocationChangeListener{
    @Bind(R.id.mapView)
    MapView mMapView;
    @Bind(R.id.shaixuan)
    TextView shaixuan;
    @Bind(R.id.fujin)
    TextView fujin;

    @Bind(R.id.yinp_bf)
    ImageView yinp_bf;
    private boolean isFirstLoc = true; // 是否首次定位
    private boolean isShowRec = true; // 是否显示列表
    float mCurrentZoom = 18f;//默认地图缩放比例值
    private AMapLocationClient mLocationClient;
    private AMapLocationClientOption mLocationOption;

    List<LatLng> points = new ArrayList<LatLng>();//位置点集合
    LatLng last = new LatLng(0, 0);//上一个定位点
    private  AnimationDrawable animationDrawable;
    private AMap aMap;
    private LatLonPoint lp = new LatLonPoint(28.008977, 113.088063);//
    private List<PointsEntity> mPointsList = new ArrayList<PointsEntity>();
    ServiceConnection serviceConnection;
    MusicService.MusicController mycontrol;
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



    private void checkPause() {
        if (mycontrol != null && mycontrol.isPlay()) {
            yinp_bf.setBackgroundResource(R.drawable.play_anim);
            animationDrawable = (AnimationDrawable) yinp_bf.getBackground();
            if (animationDrawable != null && !animationDrawable.isRunning()) {
                animationDrawable.start();
            }
        } else if (mycontrol != null) {
            PlayBean info= SaveObjectUtils.getInstance(getApplicationContext()).getObject(Constants.PLAY_POSION,null);
            if(info!=null){
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
//                        List<LatLng> pointLatLngs = new ArrayList<LatLng>();//位置点集合
                        for (int i = 0; i < info.getMp3_tag().size(); i++) {
                            PointsEntity pointsEntity = new PointsEntity();
                            pointsEntity.setPointId(info.getMp3_tag().get(i).getPlace_id());
                            pointsEntity.setType(i / 2 + 2);
                            PointsEntity.LocationBean locationBean = new PointsEntity.LocationBean();
                            locationBean.setLatitude(Double.parseDouble(info.getMp3_tag().get(i).getLatitude()));
                            locationBean.setLongitude(Double.parseDouble(info.getMp3_tag().get(i).getLongitude()));
                            pointsEntity.setLocation(locationBean);
                            mPointsList.add(pointsEntity);
//                            LatLng latLng=new LatLng(Double.parseDouble(info.getMp3_tag().get(i).getLatitude()),Double.parseDouble(info.getMp3_tag().get(i).getLongitude()));
//                            pointLatLngs.add(latLng);
                        }
//                        aMap.addPolyline(new PolylineOptions().
//                                addAll(pointLatLngs).width(10).color(0xAAFF0000));
                    }

                    @Override
                    public void onError(int whichRequest, Throwable e) {
                        dismissAlert();
                        ToastUtils.showLongToast(MapActivity.this, e.getMessage());
                    }
                });
    }

    @OnClick({R.id.shaixuan, R.id.fujin,R.id.zhankai,R.id.yinp_bf,R.id.dingwei})
    void onCLick(View view) {
        switch (view.getId()) {
            case R.id.shaixuan:
                gotoAroundPlace();
                break;
            case R.id.fujin:
                gotoNearPlace();
                break;
            case R.id.dingwei:
                startLocation();
                break;
            case R.id.zhankai:
                Intent intent1 = new Intent(this,BottomPlayActivity.class);
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
                } else if(mycontrol.isHave()){
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

    public void startLocation() {
         Log.e("David", "GPS是否打开 " + LocationManager.GPS_PROVIDER);
         Log.e("David", "网络定位是否打开 " + LocationManager.NETWORK_PROVIDER);
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
                    MarkerOptions markerOption = new MarkerOptions();
                    markerOption.position(new LatLng(pointsEntity.getLocation().getLatitude(), pointsEntity.getLocation().getLongitude()));
                    markerOption.title(pointsEntity.getPointId());
                    markerOption.draggable(false);
                    markerOption.icon(getBitmapDescriptor(pointsEntity.getType()));
                    aMap.addMarker(markerOption);
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
        BitmapDescriptor bitmapDescriptor = getBitmapDescriptor(index);
        overlayOptions.icon(bitmapDescriptor);//设置图标
        MultiPointOverlay multiPointOverlay = aMap.addMultiPointOverlay(overlayOptions);
        List<MultiPointItem> multiPointItemList = new ArrayList<MultiPointItem>();
        for (PointsEntity pointsEntity : mPointsList) {
            if (pointsEntity.getType() == index) {
                LatLng latLng = new LatLng(pointsEntity.getLocation().getLatitude(), pointsEntity.getLocation().getLongitude());
                MultiPointItem multiPointItem = new MultiPointItem(latLng);
                multiPointItem.setCustomerId(pointsEntity.getPointId());
                multiPointItemList.add(multiPointItem);
            }
        }
        multiPointOverlay.setItems(multiPointItemList);
    }

    private BitmapDescriptor getBitmapDescriptor(int index) {
        BitmapDescriptor bitmapDescriptor = null;
        switch (index) {
            case 1:
                bitmapDescriptor = BitmapDescriptorFactory
                        .fromResource(R.drawable.yuyin_1);
                break;
            case 2:
                bitmapDescriptor = BitmapDescriptorFactory
                        .fromResource(R.drawable.yuyin_1);
                break;
            case 3:
                bitmapDescriptor = BitmapDescriptorFactory
                        .fromResource(R.drawable.wc1);
                break;
            case 4:
                bitmapDescriptor = BitmapDescriptorFactory
                        .fromResource(R.drawable.gouwu1);
                break;
            case 5:
                bitmapDescriptor = BitmapDescriptorFactory
                        .fromResource(R.drawable.canting1);
                break;
            case 6:
                bitmapDescriptor = BitmapDescriptorFactory
                        .fromResource(R.drawable.jiudian1);
                break;
            case 7:
                bitmapDescriptor = BitmapDescriptorFactory
                        .fromResource(R.drawable.tichec1);
                break;
            case 8:
                bitmapDescriptor = BitmapDescriptorFactory
                        .fromResource(R.drawable.damen1);
                break;
            default:
                bitmapDescriptor = BitmapDescriptorFactory
                        .fromResource(R.drawable.yuyin_1);
                break;
        }
        return bitmapDescriptor;
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
                    addAll(points).width(10).color(0xAAFF0000));

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

        if (AMapUtils.calculateLineDistance(last, ll) > 10) {
            last = ll;
            points.clear();//有任意连续两点位置大于10，重新取点
            return null;
        }
        points.add(ll);
        last = ll;
        //有5个连续的点之间的距离小于10，认为gps已稳定，以最新的点为起始点
        if (points.size() >= 5) {
            points.clear();
            return ll;
        }
        return null;
    }


    // 定义海量点点击事件
    AMap.OnMultiPointClickListener multiPointClickListener = new AMap.OnMultiPointClickListener() {
        // 海量点中某一点被点击时回调的接口
        // 返回 true 则表示接口已响应事件，否则返回false
        @Override
        public boolean onPointClick(MultiPointItem pointItem) {
            Log.d("dddd", "onPointClick: " + pointItem.getLatLng().latitude);
            Log.d("dddd", "onPointClick: " + pointItem.getCustomerId());
            return false;
        }
    };
    // 定义 Marker 点击事件监听
    AMap.OnMarkerClickListener markerClickListener = new AMap.OnMarkerClickListener() {
        // marker 对象被点击时回调的接口
        // 返回 true 则表示接口已响应事件，否则返回false
        @Override
        public boolean onMarkerClick(Marker marker) {
            return false;
        }
    };

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        unbindService(serviceConnection);
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
}

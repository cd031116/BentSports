package com.cn.bent.sports.view.activity;

import android.app.Dialog;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
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
import com.cn.bent.sports.bean.PointsEntity;
import com.cn.bent.sports.bean.RailBean;
import com.cn.bent.sports.utils.ToastUtils;
import com.cn.bent.sports.widget.AroundDialog;
import com.cn.bent.sports.widget.GotoWhereDialog;
import com.cn.bent.sports.widget.NearDialog;
import com.zhl.network.RxObserver;
import com.zhl.network.RxSchedulers;
import com.zhl.network.huiqu.HuiquRxFunction;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by dawn on 2018/3/8.
 */

public class MapActivity extends BaseActivity {
    @Bind(R.id.mapView)
    MapView mMapView;
    @Bind(R.id.shaixuan)
    TextView shaixuan;
    @Bind(R.id.fujin)
    TextView fujin;


    private boolean isFirstLoc = true; // 是否首次定位
    private boolean isShowRec = true; // 是否显示列表
    float mCurrentZoom = 18f;//默认地图缩放比例值
    private AMapLocationClient mLocationClient;
    private AMapLocationClientOption mLocationOption;

    List<LatLng> points = new ArrayList<LatLng>();//位置点集合
    LatLng last = new LatLng(0, 0);//上一个定位点

    private AMap aMap;
    private LatLonPoint lp = new LatLonPoint(28.008977, 113.088063);//
    private List<PointsEntity> mPointsList = new ArrayList<PointsEntity>();

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
                        for (int i = 0; i < info.getMp3_tag().size(); i++) {
                            PointsEntity pointsEntity = new PointsEntity();
                            pointsEntity.setPointId(info.getMp3_tag().get(i).getPlace_id());
                            pointsEntity.setType(i / 2 + 2);
                            PointsEntity.LocationBean locationBean = new PointsEntity.LocationBean();
                            locationBean.setLatitude(Double.parseDouble(info.getMp3_tag().get(i).getLatitude()));
                            locationBean.setLongitude(Double.parseDouble(info.getMp3_tag().get(i).getLongitude()));
                            pointsEntity.setLocation(locationBean);
                            mPointsList.add(pointsEntity);
                        }
                    }

                    @Override
                    public void onError(int whichRequest, Throwable e) {
                        dismissAlert();
                        ToastUtils.showLongToast(MapActivity.this, e.getMessage());
                    }
                });
    }

    @OnClick({R.id.shaixuan, R.id.fujin})
    void onCLick(View view) {
        switch (view.getId()) {
            case R.id.shaixuan:
                gotoAroundPlace();
                break;
            case R.id.fujin:
                gotoNearPlace();
                break;
        }
    }

    public void startLocation() {
        MyLocationStyle myLocationStyle;
        myLocationStyle = new MyLocationStyle();//初始化定位蓝点样式类myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATION_ROTATE);//连续定位、且将视角移动到地图中心点，定位点依照设备方向旋转，并且会跟随设备移动。（1秒1次定位）如果不设置myLocationType，默认也会执行此种模式。
        myLocationStyle.showMyLocation(true);
        aMap.setMyLocationStyle(myLocationStyle);//设置定位蓝点的Style
//aMap.getUiSettings().setMyLocationButtonEnabled(true);设置默认定位按钮是否显示，非必需设置。
        aMap.setMyLocationEnabled(true);// 设置为true表示启动显示定位蓝点，false表示隐藏定位蓝点并不进行定位，默认是false。

        aMap.setOnCameraChangeListener(new AMap.OnCameraChangeListener() {
            @Override
            public void onCameraChange(CameraPosition cameraPosition) {

            }

            @Override
            public void onCameraChangeFinish(CameraPosition cameraPosition) {
                mCurrentZoom = cameraPosition.zoom;//获取手指缩放地图后的值
            }
        });
        aMap.setOnMyLocationChangeListener(new AMap.OnMyLocationChangeListener() {
            @Override
            public void onMyLocationChange(Location location) {

            }
        });

        mLocationClient = new AMapLocationClient(this);
//设置定位回调监听
        mLocationClient.setLocationListener(mAMapLocationListener);
//初始化AMapLocationClientOption对象
        mLocationOption = new AMapLocationClientOption();
        //设置定位模式为AMapLocationMode.Hight_Accuracy，高精度模式。
        mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
        //设置定位模式为AMapLocationMode.Device_Sensors，仅设备模式。
//        mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Device_Sensors);
        mLocationOption.setInterval(1000 * 2);
        //给定位客户端对象设置定位参数
        mLocationClient.setLocationOption(mLocationOption);
        //启动定位
        mLocationOption.setOnceLocation(false);
        mLocationClient.startLocation();
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

    AMapLocationListener mAMapLocationListener = new AMapLocationListener() {
        @Override
        public void onLocationChanged(AMapLocation aMapLocation) {
            if (aMapLocation != null) {
                if (isFirstLoc) {
                    LatLng ll = null;
                    ll = getMostAccuracyLocation(aMapLocation);
                    if (ll == null) {
                        return;
                    }
                    isFirstLoc = false;
                    points.add(ll);//加入集合
                    last = ll;

                    return;//画轨迹最少得2个点，首地定位到这里就可以返回了
                }
                //从第二个点开始
                LatLng ll = new LatLng(aMapLocation.getLatitude(), aMapLocation.getLongitude());
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
    };

    /**
     * 首次定位很重要，选一个精度相对较高的起始点
     * 注意：如果一直显示gps信号弱，说明过滤的标准过高了，
     * 你可以将location.getRadius()>25中的过滤半径调大，比如>40，
     * 并且将连续5个点之间的距离DistanceUtil.getDistance(last, ll ) > 5也调大一点，比如>10，
     * 这里不是固定死的，你可以根据你的需求调整，如果你的轨迹刚开始效果不是很好，你可以将半径调小，两点之间距离也调小，
     * gps的精度半径一般是10-50米
     */
    private LatLng getMostAccuracyLocation(AMapLocation location) {

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
        //在activity执行onDestroy时执行mMapView.onDestroy()，销毁地图
        mMapView.onDestroy();
        mLocationClient.unRegisterLocationListener(mAMapLocationListener);
        if (mLocationClient != null && mLocationClient.isStarted()) {
            mLocationClient.stopLocation();
        }
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

package com.cn.bent.sports.view.activity;

import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.BitmapDescriptor;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.MultiPointItem;
import com.amap.api.maps.model.MultiPointOverlay;
import com.amap.api.maps.model.MultiPointOverlayOptions;
import com.amap.api.services.core.LatLonPoint;
import com.cn.bent.sports.R;
import com.cn.bent.sports.api.BaseApi;
import com.cn.bent.sports.base.BaseActivity;
import com.cn.bent.sports.bean.PointsEntity;
import com.cn.bent.sports.bean.RailBean;
import com.cn.bent.sports.utils.ToastUtils;
import com.cn.bent.sports.widget.AroundDialog;
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
                            pointsEntity.setType(i / 4 + 2);
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

    @Override
    public void onDestroy() {
        super.onDestroy();
        //在activity执行onDestroy时执行mMapView.onDestroy()，销毁地图
        mMapView.onDestroy();
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
        lp.x = 22;
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
                //TODO 去卫生间
                setOverLay(2);
                break;
            case 3:
                //TODO 去卫生间
                setOverLay(3);
                break;
            case 4:
                //TODO 去卫生间
                setOverLay(4);
                break;
            case 5:
                //TODO 去卫生间
                setOverLay(5);
                break;
            case 6:
                //TODO 去卫生间
                setOverLay(6);
                break;
            case 7:
                //TODO 去卫生间
                setOverLay(7);
                break;
            case 8:
                //TODO 去卫生间
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
                        .fromResource(R.drawable.dangqwz);
                break;
            case 2:
                bitmapDescriptor = BitmapDescriptorFactory
                        .fromResource(R.drawable.dangqwz);
                break;
            case 3:
                bitmapDescriptor = BitmapDescriptorFactory
                        .fromResource(R.drawable.shijian);
                break;
            case 4:
                bitmapDescriptor = BitmapDescriptorFactory
                        .fromResource(R.drawable.shuoming);
                break;
            default:
                bitmapDescriptor = BitmapDescriptorFactory
                        .fromResource(R.drawable.shijian);
                break;
        }
        return bitmapDescriptor;
    }


    //前往附近点
    private void gotoNearPlace() {
        NearDialog nearDialog = new NearDialog(MapActivity.this, R.style.dialog, new NearDialog.OnCloseListener() {
            @Override
            public void onClick(Dialog dialog, boolean confirm, int index) {

                Log.d("dddd", "onClick: " + index);
                switch (index) {
                    case 1:
                        //TODO 去卫生间
                        break;
                    case 2:
                        //TODO 去卫生间
                        break;
                    case 3:
                        //TODO 去卫生间
                        break;
                    case 4:
                        //TODO 去卫生间
                        break;
                    case 5:
                        //TODO 去卫生间
                        break;
                    case 6:
                        //TODO 去卫生间
                        break;
                    default:
                        break;
                }
                dialog.dismiss();
            }
        });

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        Window window = nearDialog.getWindow();
        lp.copyFrom(window.getAttributes());
        lp.x = 22;
        lp.y = 488;
        lp.gravity = Gravity.TOP;
        window.setAttributes(lp);
        nearDialog.show();
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
}

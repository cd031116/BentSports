package com.cn.bent.sports.view.fragment;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

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
import com.cn.bent.sports.base.BaseFragment;
import com.cn.bent.sports.database.TaskCationBean;
import com.cn.bent.sports.database.TaskCationManager;
import com.cn.bent.sports.overlay.AMapUtil;
import com.cn.bent.sports.overlay.WalkRouteOverlay;
import com.cn.bent.sports.utils.ToastUtils;
import com.cn.bent.sports.view.activity.LoginActivity;
import com.cn.bent.sports.view.activity.SettingActivity;
import com.cn.bent.sports.widget.ToastDialog;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

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
    @Bind(R.id.name)
    TextView name;
    @Bind(R.id.juli)
    TextView juli;
    @Bind(R.id.title)
    TextView title;

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
    private RouteSearch mRouteSearch;
    private WalkRouteResult mWalkRouteResult;
    private LatLonPoint mStartPoint;//起点，116.335891,39.942295
    private LatLonPoint mEndPoint;//终点，116.481288,39.995576
    private final int ROUTE_TYPE_WALK = 3;
    //---------------------
    AMapLocationListener mAMapLocationListener = new AMapLocationListener() {
        @Override
        public void onLocationChanged(AMapLocation aMapLocation) {
            if (aMapLocation != null) {
                isLocal = true;
                if (aMapLocation.getErrorCode() == 0) {
//可在其中解析amapLocation获取相应内容。
                    if (isFirstLoc) {
                        isFirstLoc = false;
                        latitude = String.valueOf(aMapLocation.getLatitude());
                        longitude = String.valueOf(aMapLocation.getLongitude());
                        mStartPoint = new LatLonPoint(aMapLocation.getLatitude(), aMapLocation.getLongitude());
                        addLocaToMap();
                    }
                } else {
                    isLocal = false;
                    //定位失败时，可通过ErrCode（错误码）信息来确定失败的原因，errInfo是错误信息，详见错误码表。
//                    ToastUtils.showShortToast(getActivity(), "获取位置信息失败!");
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
        mapView.onCreate(savedInstanceState);
        if (aMap == null) {
            aMap = mapView.getMap();
        }
        // 绑定 Marker 被点击事件
        aMap.setOnMarkerClickListener(this);
        mRouteSearch = new RouteSearch(getActivity());
        mRouteSearch.setRouteSearchListener(this);
    }

    @Override
    protected void initData() {
        mLoction = TaskCationManager.getHistory();
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
        //启动定位
        mLocationOption.setOnceLocation(true);
        mLocationClient.startLocation();
        addMarkersToMap();
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        for (int i = 0; i < mList.size(); i++) {
            if (marker.equals(mList.get(i))) {
                showDialogMsg("是否前往该地点", i);
            }
        }
        return true;
    }

    private void setcheck() {
        for (TaskCationBean hs : mLoction) {
            hs.setCheck(false);
        }
        for (Marker marker : mList) {
            marker.remove();
        }
    }


    private void addMarkersToMap() {
        ArrayList<MarkerOptions> markerOptionlst = new ArrayList<MarkerOptions>();
        Log.i("tttt", "size=" + mLoction.size());
        if (mLoction != null) {
            if(mList!=null){
                mList.clear();
            }
            for (TaskCationBean hs : mLoction) {
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


    /**
     * 在地图上添加marker
     */
    private void addLocaToMap() {
        if (TextUtils.isEmpty(latitude)) {
            return;
        }
        LatLng latlng = new LatLng(Double.valueOf(latitude), Double.valueOf(longitude));
        Marker marker = aMap.addMarker(new MarkerOptions()
                .position(latlng)
                .icon(BitmapDescriptorFactory.fromBitmap(BitmapFactory
                        .decodeResource(getResources(), R.drawable.dangqwz)))
                .draggable(true));
        aMap.moveCamera(CameraUpdateFactory.changeLatLng(latlng));
        aMap.moveCamera(CameraUpdateFactory.zoomTo(17));
        aMap.setMyLocationEnabled(false);
    }

    /**
     * 方法必须重写
     */
    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
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

    @Override
    public void onWalkRouteSearched(WalkRouteResult result, int errorCode) {
        if (errorCode == AMapException.CODE_AMAP_SUCCESS) {
            if (result != null && result.getPaths() != null) {
                if (result.getPaths().size() > 0) {
                    mWalkRouteResult = result;
                    final WalkPath walkPath = mWalkRouteResult.getPaths()
                            .get(0);

                    WalkRouteOverlay walkRouteOverlay = new WalkRouteOverlay(
                            getActivity(), aMap, walkPath,
                            mWalkRouteResult.getStartPos(),
                            mWalkRouteResult.getTargetPos());
                    walkRouteOverlay.removeFromMap();
                    walkRouteOverlay.addToMap();
                    walkRouteOverlay.zoomToSpan();
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
    }


    private void showDialogMsg(String names, final int position) {
        new ToastDialog(getActivity(), R.style.dialog, names, new ToastDialog.OnCloseListener() {
            @Override
            public void onClick(Dialog dialog, boolean confirm) {
                if (confirm) {
                    setcheck();
                    mLoction.get(position).setCheck(true);
                    addMarkersToMap();
                    mEndPoint = new LatLonPoint(Double.valueOf(mLoction.get(position).getLatitude()).doubleValue(),
                            Double.valueOf(mLoction.get(position).getLongitude()).doubleValue());
                    searchRouteResult(ROUTE_TYPE_WALK, RouteSearch.WalkDefault);
                } else {

                }
                dialog.dismiss();
            }
        }).setTitle("提示").show();
    }
}
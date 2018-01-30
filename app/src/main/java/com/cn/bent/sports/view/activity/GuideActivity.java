package com.cn.bent.sports.view.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.amap.api.maps.AMap;
import com.amap.api.maps.MapView;
import com.amap.api.navi.AMapHudViewListener;
import com.amap.api.navi.AMapNavi;
import com.amap.api.navi.AMapNaviListener;
import com.amap.api.navi.AMapNaviView;
import com.amap.api.navi.AMapNaviViewListener;
import com.amap.api.navi.enums.NaviType;
import com.amap.api.navi.model.NaviLatLng;
import com.cn.bent.sports.R;
import com.cn.bent.sports.base.BaseActivity;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import butterknife.Bind;

public class GuideActivity extends BaseActivity {
    @Bind(R.id.navi_view)
    MapView mapview;
    AMap aMap;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mapview.onCreate(savedInstanceState);
        aMap=mapview.getMap();

        aMap.setCustomMapStylePath("/sdcard/style.data");
        aMap.setMapCustomEnable(true);//true 开启; false 关闭

    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_guide;
    }

    @Override
    public void initView() {

    }

    @Override
    public void initData() {
        super.initData();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}

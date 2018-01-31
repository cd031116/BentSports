package com.cn.bent.sports.view.activity;

import android.os.Bundle;

import com.amap.api.maps.AMap;
import com.amap.api.maps.MapView;
import com.cn.bent.sports.R;
import com.cn.bent.sports.base.BaseActivity;

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

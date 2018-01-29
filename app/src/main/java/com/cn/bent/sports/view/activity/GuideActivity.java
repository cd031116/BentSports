package com.cn.bent.sports.view.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.amap.api.maps.AMap;
import com.amap.api.navi.AMapHudViewListener;
import com.amap.api.navi.AMapNavi;
import com.amap.api.navi.AMapNaviListener;
import com.amap.api.navi.AMapNaviView;
import com.amap.api.navi.AMapNaviViewListener;
import com.amap.api.navi.enums.NaviType;
import com.amap.api.navi.model.NaviLatLng;
import com.cn.bent.sports.R;
import com.cn.bent.sports.base.BaseActivity;

import butterknife.Bind;

public class GuideActivity extends BaseActivity {
    @Bind(R.id.navi_view)
    AMapNaviView mAMapNavi;
    AMap aMap;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }


    @Override
    protected int getLayoutId() {
        return R.layout.activity_guide;
    }

    @Override
    public void initView() {
        aMap=mAMapNavi.getMap();
        aMap.setCustomMapStylePath("style.data");
    }

    @Override
    public void initData() {
        super.initData();
    }

    @Override
    public void onResume() {
        super.onResume();
        mAMapNavi.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mAMapNavi.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mAMapNavi.onDestroy();
    }
}

package com.cn.bent.sports.view.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.cn.bent.sports.R;
import com.cn.bent.sports.base.BaseFragment;
import com.cn.bent.sports.ibeacon.ScanActivity;
import com.cn.bent.sports.view.activity.GuideActivity;
import com.cn.bent.sports.view.activity.SettingActivity;

import butterknife.OnClick;

/**
 * Created by lyj on 2018/1/29 0029.
 * description
 */

public class IsMeFragment extends BaseFragment{

    public static IsMeFragment newInstance() {
        IsMeFragment fragment = new IsMeFragment();
//        Bundle bundle = new Bundle();
//        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.is_me_fragment;
    }

    @Override
    protected void initView(View view, Bundle savedInstanceState) {

    }

    @Override
    protected void initData() {

    }


    @OnClick({R.id.setting,R.id.scan})
    void conlick(View view){
        switch (view.getId()){
            case R.id.setting:
            startActivity(new Intent(getActivity(), SettingActivity.class));
                break;
            case R.id.scan:
                startActivity(new Intent(getActivity(), ScanActivity.class));
                break;
        }
    }
}

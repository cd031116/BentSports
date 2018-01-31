package com.cn.bent.sports.view.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;

import com.cn.bent.sports.R;
import com.cn.bent.sports.base.BaseFragment;
import com.cn.bent.sports.event.CardEvent;
import com.cn.bent.sports.ibeacon.ScanActivity;
import com.cn.bent.sports.view.activity.GuideActivity;
import com.cn.bent.sports.view.activity.PlayWebViewActivity;
import com.cn.bent.sports.view.activity.SettingActivity;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by lyj on 2018/1/29 0029.
 * description
 */

public class IsMeFragment extends BaseFragment {

    @Bind(R.id.liunianshou)
    ImageView liunianshou;
    @Bind(R.id.caidengmi)
    ImageView caidengmi;
    @Bind(R.id.fangbianpao)
    ImageView fangbianpao;
    @Bind(R.id.hongbaoyu)
    ImageView hongbaoyu;
    @Bind(R.id.diandenglong)
    ImageView diandenglong;
    @Bind(R.id.jixiangqian)
    ImageView jixiangqian;


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
        EventBus.getDefault().register(this);//注册
    }

    @Subscribe
    public void onEventMainThread(CardEvent event) {
        switch (event.getGameId()) {
            case 2:
                liunianshou.setBackground(getResources().getDrawable(R.drawable.liunianshou));
                break;
            case 3:
                caidengmi.setBackground(getResources().getDrawable(R.drawable.caidengmi));
                break;
            case 4:
                fangbianpao.setBackground(getResources().getDrawable(R.drawable.fangbianpao));
                break;
            case 5:
                hongbaoyu.setBackground(getResources().getDrawable(R.drawable.hongbaoyu));
                break;
            case 6:
                diandenglong.setBackground(getResources().getDrawable(R.drawable.diandenglong));
                break;
            case 7:
                jixiangqian.setBackground(getResources().getDrawable(R.drawable.jixiangqian));
                break;

        }
    }

    @OnClick({R.id.setting, R.id.scan, R.id.user_photo})
    void conlick(View view) {
        switch (view.getId()) {
            case R.id.setting:
                startActivity(new Intent(getActivity(), SettingActivity.class));
                break;
            case R.id.scan:
                startActivity(new Intent(getActivity(), ScanActivity.class));
                break;
            case R.id.user_photo:
                startActivity(new Intent(getActivity(), PlayWebViewActivity.class));
                break;
        }
    }
}

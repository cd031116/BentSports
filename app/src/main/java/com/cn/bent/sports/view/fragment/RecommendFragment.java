package com.cn.bent.sports.view.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.cn.bent.sports.R;
import com.cn.bent.sports.base.BaseFragment;
import com.cn.bent.sports.view.activity.MapActivity;

import butterknife.OnClick;

/**
 * Created by lyj on 2018/3/19 0019.
 * description
 */

public class RecommendFragment extends BaseFragment {
    public static RecommendFragment newInstance() {
        RecommendFragment fragment = new RecommendFragment();
//        Bundle bundle = new Bundle();
//        fragment.setArguments(bundle);
        return fragment;
    }


    @Override
    protected int getLayoutId() {
        return R.layout.recommend_fragment;
    }

    @Override
    protected void initView(View view, Bundle savedInstanceState) {

    }

    @Override
    protected void initData() {

    }

    @OnClick({R.id.go_daolan})
    void onclick(View view){
        switch (view.getId()){
            case R.id.go_daolan:
                startActivity(new Intent(getActivity(), MapActivity.class));
                break;
        }
    }
}

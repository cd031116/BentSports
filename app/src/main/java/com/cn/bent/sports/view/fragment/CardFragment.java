package com.cn.bent.sports.view.fragment;

import android.os.Bundle;
import android.view.View;

import com.cn.bent.sports.R;
import com.cn.bent.sports.base.BaseFragment;

/**
 * Created by lyj on 2018/3/19 0019.
 * description
 */

public class CardFragment extends BaseFragment {
    public static CardFragment newInstance() {
        CardFragment fragment = new CardFragment();
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


}

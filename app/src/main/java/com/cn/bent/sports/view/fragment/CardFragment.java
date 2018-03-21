package com.cn.bent.sports.view.fragment;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.cn.bent.sports.R;
import com.cn.bent.sports.adapter.CardFragmentAdapter;
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
        return R.layout.card_fragment;
    }

    @Override
    protected void initView(View view, Bundle savedInstanceState) {
        String[] titles = new String[]{getResources().getString(R.string.is_choose),
                getResources().getString(R.string.all),
                getResources().getString(R.string.is_me)};
        ViewPager viewPager = (ViewPager) view.findViewById(R.id.mViewPager);
        ImageView publish_btn = (ImageView) view.findViewById(R.id.publish_btn);
        CardFragmentAdapter adapter = new CardFragmentAdapter(getActivity().getSupportFragmentManager(),titles);
        viewPager.setAdapter(adapter);

        //TabLayout
        android.support.design.widget.TabLayout tabs = (android.support.design.widget.TabLayout)view. findViewById(R.id.tabs);
        tabs.setupWithViewPager(viewPager);
        publish_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }

    @Override
    protected void initData() {

    }


}

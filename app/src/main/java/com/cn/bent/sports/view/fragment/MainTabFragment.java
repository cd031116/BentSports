package com.cn.bent.sports.view.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.cn.bent.sports.R;
import com.cn.bent.sports.base.BaseFragment;
import com.cn.bent.sports.bean.RangeEntity;
import com.cn.bent.sports.recyclebase.CommonAdapter;
import com.cn.bent.sports.recyclebase.ViewHolder;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

/**
 * Created by lyj on 2018/1/29 0029.
 * description
 */

public class MainTabFragment extends BaseFragment {
    @Bind(R.id.range_list)
    RecyclerView range_list;

    public static MainTabFragment newInstance(int type) {
        MainTabFragment fragment = new MainTabFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("type", type);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.main_fragment;
    }

    @Override
    protected void initView(View view, Bundle savedInstanceState) {
        range_list.setLayoutManager(new LinearLayoutManager(getActivity()));
        setRecyclerView();
    }

    @Override
    protected void initData() {

    }

    private void setRecyclerView() {
        List<RangeEntity> list = new ArrayList<>();
        RangeEntity rangeEntity = new RangeEntity();
        rangeEntity.setName("sss");
        rangeEntity.setJifen("1234");
        rangeEntity.setNum("2");
        rangeEntity.setHead_img("http://pic4.40017.cn/scenery/destination/2016/08/15/14/3aPCtM_740x350_00.jpg");
        list.add(rangeEntity);
        list.add(rangeEntity);
        list.add(rangeEntity);
        list.add(rangeEntity);
        CommonAdapter<RangeEntity> mAdapter = new CommonAdapter<RangeEntity>(getActivity(), R.layout.item_range, list) {
            @Override
            protected void convert(ViewHolder holder, RangeEntity rangeEntity, int position) {
                holder.setText(R.id.range_num, rangeEntity.getNum());
                holder.setText(R.id.range_name, rangeEntity.getName());
                holder.setText(R.id.range_jifen, rangeEntity.getJifen());
                ImageView NormalInfoImg = (ImageView) holder.getView(R.id.img_head);
                RequestOptions requestOptions = RequestOptions.circleCropTransform();
                Glide.with(NormalInfoImg.getContext()).load(rangeEntity.getHead_img())
                        .apply(requestOptions)
                        .into(NormalInfoImg);
            }
        };
        range_list.setAdapter(mAdapter);
    }
}

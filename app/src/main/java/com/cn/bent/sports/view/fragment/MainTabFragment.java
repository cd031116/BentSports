package com.cn.bent.sports.view.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.cn.bent.sports.R;
import com.cn.bent.sports.api.BaseApi;
import com.cn.bent.sports.base.BaseFragment;
import com.cn.bent.sports.bean.RangeEntity;
import com.cn.bent.sports.bean.RankEntity;
import com.cn.bent.sports.recyclebase.CommonAdapter;
import com.cn.bent.sports.recyclebase.ViewHolder;
import com.cn.bent.sports.view.activity.ZoomActivity;
import com.zhl.network.RxObserver;
import com.zhl.network.RxSchedulers;
import com.zhl.network.huiqu.HuiquRxFunction;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
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
    @Bind(R.id.head_1)
    ImageView head_1;
    @Bind(R.id.name_1)
    TextView name_1;
    @Bind(R.id.jifen_1)
    TextView jifen_1;
    @Bind(R.id.head_2)
    ImageView head_2;
    @Bind(R.id.name_2)
    TextView name_2;
    @Bind(R.id.jifen_2)
    TextView jifen_2;
    @Bind(R.id.head_3)
    ImageView head_3;
    @Bind(R.id.name_3)
    TextView name_3;
    @Bind(R.id.jifen_3)
    TextView jifen_3;

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
    }

    private void setRecyclerView(List<RankEntity.RankListBean> rankListBeen) {

        CommonAdapter<RankEntity.RankListBean> mAdapter = new CommonAdapter<RankEntity.RankListBean>(getActivity(), R.layout.item_range, rankListBeen) {
            @Override
            protected void convert(ViewHolder holder, RankEntity.RankListBean rangeEntity, int position) {

                holder.setText(R.id.range_num, position + 4 + "");
                holder.setText(R.id.range_name, rangeEntity.getNickname());
                holder.setText(R.id.range_jifen, rangeEntity.getScore() + "");
                ImageView NormalInfoImg = (ImageView) holder.getView(R.id.img_head);
                RequestOptions requestOptions = RequestOptions.circleCropTransform();
                Glide.with(NormalInfoImg.getContext()).load(rangeEntity.getHeadimg())
                        .apply(requestOptions)
                        .into(NormalInfoImg);

            }
        };
        range_list.setAdapter(mAdapter);
        range_list.addItemDecoration(new DividerItemDecoration(getActivity(),R.drawable.list_divider));
    }

    @Override
    protected void initData() {

        BaseApi.getDefaultService(getActivity()).getRankList()
                .map(new HuiquRxFunction<RankEntity>())
                .compose(RxSchedulers.<RankEntity>io_main())
                .subscribe(new RxObserver<RankEntity>(getActivity(), "getRankList", 1, false) {
                    @Override
                    public void onSuccess(int whichRequest, RankEntity rankEntity) {
                        if (rankEntity != null && rankEntity.getRankList() != null && rankEntity.getRankList().size() > 0) {
                            if (rankEntity.getRankList().size() > 0)
                                setOneView(rankEntity.getRankList().get(0));
                            if (rankEntity.getRankList().size() > 1)
                                setTwoView(rankEntity.getRankList().get(1));
                            if (rankEntity.getRankList().size() > 2)
                                setThreeView(rankEntity.getRankList().get(2));
                            if (rankEntity.getRankList().size() > 3) {
                                for (int i = 0; i < 3; i++)
                                    rankEntity.getRankList().remove(0);
                                setRecyclerView(rankEntity.getRankList());
                            }
                        }
                    }

                    @Override
                    public void onError(int whichRequest, Throwable e) {

                    }
                });
    }

    private void setThreeView(RankEntity.RankListBean rankListBean) {
        name_3.setText(rankListBean.getNickname());
        jifen_3.setText(rankListBean.getScore()+"");
        RequestOptions requestOptions = RequestOptions.circleCropTransform();
        Glide.with(head_3.getContext()).load(rankListBean.getHeadimg())
                .apply(requestOptions)
                .into(head_3);
    }

    private void setTwoView(RankEntity.RankListBean rankListBean) {
        name_2.setText(rankListBean.getNickname());
        jifen_2.setText(rankListBean.getScore()+"");
        RequestOptions requestOptions = RequestOptions.circleCropTransform();
        Glide.with(head_2.getContext()).load(rankListBean.getHeadimg())
                .apply(requestOptions)
                .into(head_2);
    }

    private void setOneView(RankEntity.RankListBean rankListBean) {
        name_1.setText(rankListBean.getNickname());
        jifen_1.setText(rankListBean.getScore()+"");
        RequestOptions requestOptions = RequestOptions.circleCropTransform();
        Glide.with(head_1.getContext()).load(rankListBean.getHeadimg())
                .apply(requestOptions)
                .into(head_1);
    }

}

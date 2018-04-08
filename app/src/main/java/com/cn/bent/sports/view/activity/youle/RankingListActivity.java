package com.cn.bent.sports.view.activity.youle;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.cn.bent.sports.R;
import com.cn.bent.sports.api.BaseApi;
import com.cn.bent.sports.base.BaseActivity;
import com.cn.bent.sports.bean.GameRankEntity;
import com.cn.bent.sports.bean.ReFreshEvent;
import com.cn.bent.sports.recyclebase.CommonAdapter;
import com.cn.bent.sports.recyclebase.ViewHolder;
import com.cn.bent.sports.utils.Constants;
import com.cn.bent.sports.utils.SaveObjectUtils;
import com.cn.bent.sports.view.activity.youle.bean.QueryInfo;
import com.cn.bent.sports.view.activity.youle.bean.UserInfo;
import com.cn.bent.sports.widget.DividerItemDecoration;
import com.cn.bent.sports.widget.pull.LoadMoreFooterView;
import com.lvr.library.recyclerview.HRecyclerView;
import com.lvr.library.recyclerview.OnLoadMoreListener;
import com.zhl.network.RxObserver;
import com.zhl.network.RxSchedulers;
import com.zhl.network.huiqu.HuiquRxTBFunction;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

public class RankingListActivity extends BaseActivity implements OnLoadMoreListener {
    @Bind(R.id.range_list)
    HRecyclerView range_list;

    private UserInfo user;
    private int gameId;
    private int pageIndex = 1;
    private boolean isShow = false;
    private LoadMoreFooterView mLoadMoreFooterView;
    private List<GameRankEntity.ListBean> mListBean = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_ranking_list;
    }

    @Override
    public void initView() {
        super.initView();
        range_list.setLayoutManager(new LinearLayoutManager(this));
        range_list.setLoadMoreEnabled(true);
        range_list.setOnLoadMoreListener(this);
        mLoadMoreFooterView = (LoadMoreFooterView) range_list.getLoadMoreFooterView();
        user = (UserInfo) SaveObjectUtils.getInstance(this).getObject(Constants.USER_BASE, null);
        gameId = getIntent().getIntExtra("gameId", 1);
    }

    @Override
    public void initData() {
        getRankData();
    }


    @Override
    public void onResume() {
        super.onResume();
    }

    private void setRecyclerView(List<GameRankEntity.ListBean> rankListBeen, int type) {

        CommonAdapter<GameRankEntity.ListBean> mAdapter = new CommonAdapter<GameRankEntity.ListBean>(this, R.layout.item_range, rankListBeen) {
            @Override
            protected void convert(ViewHolder holder, GameRankEntity.ListBean rangeEntity, int position) {
                holder.setText(R.id.range_num, (pageIndex - 2) * 10 + rangeEntity.getRank() + "");
                holder.setText(R.id.range_name, rangeEntity.getTeamName());
                holder.setText(R.id.range_jifen, rangeEntity.getScore() + "");
                ImageView NormalInfoImg = (ImageView) holder.getView(R.id.img_head);
                RequestOptions requestOptions = RequestOptions.circleCropTransform();
                Glide.with(NormalInfoImg.getContext()).load(rangeEntity.getAvatar())
                        .apply(requestOptions)
                        .into(NormalInfoImg);

            }
        };
        if (type == 1)
            range_list.setAdapter(mAdapter);
        else
            mAdapter.notifyItemInserted(rankListBeen.size() + 1);
        range_list.addItemDecoration(new DividerItemDecoration(this, R.drawable.list_divider));
    }

    private void getRankData() {
        BaseApi.getJavaLoginDefaultService(this).getRankList(new QueryInfo(true, pageIndex, 10, gameId))
                .map(new HuiquRxTBFunction<GameRankEntity>())
                .compose(RxSchedulers.<GameRankEntity>io_main())
                .subscribe(new RxObserver<GameRankEntity>(this, "getRankList", 1, false) {
                    @Override
                    public void onSuccess(int whichRequest, GameRankEntity rankEntity) {
                        if (rankEntity != null) {
                            mListBean.addAll(rankEntity.getList());
                            setView(mListBean, 1);
                            pageIndex++;
                        }
                    }

                    @Override
                    public void onError(int whichRequest, Throwable e) {

                    }
                });
    }

    private void setView(List<GameRankEntity.ListBean> rankEntity, int type) {
        if (rankEntity != null && rankEntity.size() > 0) {
            Log.d(TAG, "setView: " + rankEntity.size() + "---" + pageIndex);
            if (type == 1) {
                setRankHeadView(rankEntity);
            }
            setRecyclerView(rankEntity, type);
        }
    }

    private void setRankHeadView(List<GameRankEntity.ListBean> rankEntity) {

        View headView = LayoutInflater.from(this).inflate(R.layout.rank_head_view, null, false);
        ImageView head_1 = (ImageView) headView.findViewById(R.id.head_1);
        TextView name_1 = (TextView) headView.findViewById(R.id.name_1);
        TextView jifen_1 = (TextView) headView.findViewById(R.id.jifen_1);
        ImageView head_2 = (ImageView) headView.findViewById(R.id.head_2);
        TextView name_2 = (TextView) headView.findViewById(R.id.name_2);
        TextView jifen_2 = (TextView) headView.findViewById(R.id.jifen_2);
        ImageView head_3 = (ImageView) headView.findViewById(R.id.head_3);
        TextView name_3 = (TextView) headView.findViewById(R.id.name_3);
        TextView jifen_3 = (TextView) headView.findViewById(R.id.jifen_3);
        TextView range_num = (TextView) headView.findViewById(R.id.range_num);
        TextView range_jifen = (TextView) headView.findViewById(R.id.range_jifen);
        TextView range_name = (TextView) headView.findViewById(R.id.range_name);
        ImageView img_head = (ImageView) headView.findViewById(R.id.img_head);
        RelativeLayout my_range_layout = (RelativeLayout) headView.findViewById(R.id.my_range_layout);

        int index = 0;
        for (int i = 0; i < rankEntity.size(); i++) {
            if ((user.getId() + "").equals(rankEntity.get(i).getId())) {
                isShow = true;
                index = rankEntity.get(i).getRank();
                break;
            }
        }
        if (isShow) {
            my_range_layout.setVisibility(View.VISIBLE);
            range_num.setText(String.valueOf((pageIndex - 1) * 10 + index));
            range_jifen.setText(String.valueOf(rankEntity.get(index).getScore()));
            range_name.setText(rankEntity.get(index).getTeamName());
            RequestOptions requestOptions = RequestOptions.circleCropTransform();
            Glide.with(img_head.getContext()).load(rankEntity.get(index).getAvatar())
                    .apply(requestOptions)
                    .into(img_head);
        } else
            my_range_layout.setVisibility(View.GONE);
        if (rankEntity.size() > 0) {
            name_1.setText(rankEntity.get(0).getTeamName());
            jifen_1.setText(rankEntity.get(0).getScore() + "");
            RequestOptions requestOptions = RequestOptions.circleCropTransform();
            Glide.with(head_1.getContext()).load(rankEntity.get(0).getAvatar())
                    .apply(requestOptions)
                    .into(head_1);
        }
        if (rankEntity.size() > 1) {
            name_2.setText(rankEntity.get(1).getTeamName());
            jifen_2.setText(rankEntity.get(1).getScore() + "");
            RequestOptions requestOptions = RequestOptions.circleCropTransform();
            Glide.with(head_2.getContext()).load(rankEntity.get(1).getAvatar())
                    .apply(requestOptions)
                    .into(head_2);
        }
        if (rankEntity.size() > 2) {
            name_3.setText(rankEntity.get(2).getTeamName());
            jifen_3.setText(rankEntity.get(2).getScore() + "");
            RequestOptions requestOptions = RequestOptions.circleCropTransform();
            Glide.with(head_3.getContext()).load(rankEntity.get(2).getAvatar())
                    .apply(requestOptions)
                    .into(head_3);
        }
        if (rankEntity.size() > 3) {
            for (int i = 0; i < 3; i++)
                rankEntity.remove(0);
        }
        range_list.addHeaderView(headView);
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onLoadMore() {
        Log.d(TAG, " onLoadMore:");
        if (mLoadMoreFooterView.canLoadMore()) {
            mLoadMoreFooterView.setStatus(LoadMoreFooterView.Status.LOADING);
            BaseApi.getJavaLoginDefaultService(this).getRankList(new QueryInfo(true, pageIndex++, 10, gameId))
                    .map(new HuiquRxTBFunction<GameRankEntity>())
                    .compose(RxSchedulers.<GameRankEntity>io_main())
                    .subscribe(new RxObserver<GameRankEntity>(this, "getRankList", 2, false) {
                        @Override
                        public void onSuccess(int whichRequest, GameRankEntity rankEntity) {
                            Log.d(TAG, "onSuccess onLoadMore:" + rankEntity.getList().size());
                            if (rankEntity != null && rankEntity.getList().size() > 0) {
                                if (!rankEntity.isIsLastPage()) {
                                    mListBean.addAll(rankEntity.getList());
                                    setView(mListBean, 2);
                                    mLoadMoreFooterView.setStatus(LoadMoreFooterView.Status.GONE);
                                } else
                                    mLoadMoreFooterView.setStatus(LoadMoreFooterView.Status.THE_END);
                            } else
                                mLoadMoreFooterView.setStatus(LoadMoreFooterView.Status.THE_END);
                        }

                        @Override
                        public void onError(int whichRequest, Throwable e) {
                            mLoadMoreFooterView.setStatus(LoadMoreFooterView.Status.ERROR);
                        }
                    });
        }
    }
}

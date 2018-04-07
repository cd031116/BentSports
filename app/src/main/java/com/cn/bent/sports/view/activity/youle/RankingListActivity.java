package com.cn.bent.sports.view.activity.youle;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
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
import com.zhl.network.RxObserver;
import com.zhl.network.RxSchedulers;
import com.zhl.network.huiqu.HuiquRxTBFunction;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import butterknife.Bind;

public class RankingListActivity extends BaseActivity {
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

    @Bind(R.id.range_num)
    TextView range_num;
    @Bind(R.id.my_range_layout)
    RelativeLayout my_range_layout;
    @Bind(R.id.range_name)
    TextView range_name;
    @Bind(R.id.img_head)
    ImageView img_head;
    @Bind(R.id.range_jifen)
    TextView range_jifen;
    private UserInfo user;
    private int gameId;
    private int pageIndex = 1;
    private boolean isShow = false;

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
        EventBus.getDefault().register(this);
        range_list.setLayoutManager(new LinearLayoutManager(this));
        user = (UserInfo) SaveObjectUtils.getInstance(this).getObject(Constants.USER_BASE, null);
        gameId = getIntent().getIntExtra("gameId", 1);
    }

    @Override
    public void initData() {
        super.initData();
    }


    @Override
    public void onResume() {
        super.onResume();
        getRankData();
    }

    private void setRecyclerView(List<GameRankEntity.ListBean> rankListBeen) {

        CommonAdapter<GameRankEntity.ListBean> mAdapter = new CommonAdapter<GameRankEntity.ListBean>(this, R.layout.item_range, rankListBeen) {
            @Override
            protected void convert(ViewHolder holder, GameRankEntity.ListBean rangeEntity, int position) {

                holder.setText(R.id.range_num, position + 4 + "");
                holder.setText(R.id.range_name, rangeEntity.getTeamName());
                holder.setText(R.id.range_jifen, rangeEntity.getScore() + "");
                ImageView NormalInfoImg = (ImageView) holder.getView(R.id.img_head);
                RequestOptions requestOptions = RequestOptions.circleCropTransform();
                Glide.with(NormalInfoImg.getContext()).load(rangeEntity.getAvatar())
                        .apply(requestOptions)
                        .into(NormalInfoImg);

            }
        };
        range_list.setNestedScrollingEnabled(false);
        range_list.setAdapter(mAdapter);
        range_list.addItemDecoration(new DividerItemDecoration(this, R.drawable.list_divider));
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(ReFreshEvent event) {
        getRankData();
    }

    private void getRankData() {
        BaseApi.getJavaLoginDefaultService(this).getRankList(new QueryInfo(true, pageIndex, 10, gameId))
                .map(new HuiquRxTBFunction<GameRankEntity>())
                .compose(RxSchedulers.<GameRankEntity>io_main())
                .subscribe(new RxObserver<GameRankEntity>(this, "getRankList", 1, false) {
                    @Override
                    public void onSuccess(int whichRequest, GameRankEntity rankEntity) {
                        if (rankEntity != null && rankEntity.getList() != null && rankEntity.getList().size() > 0) {
                            int index = 0;
                            for (int i = 0; i < rankEntity.getList().size(); i++) {
                                if ((user.getId() + "").equals(rankEntity.getList().get(i).getId())) {
                                    isShow = true;
                                    index = i;
                                    break;
                                }
                            }
                            if (isShow) {
                                my_range_layout.setVisibility(View.VISIBLE);
                                range_num.setText(String.valueOf((pageIndex - 1) * 10 + index + 1));
                                range_jifen.setText(String.valueOf(rankEntity.getList().get(index).getScore()));
                                range_name.setText(rankEntity.getList().get(index).getTeamName());
                                RequestOptions requestOptions = RequestOptions.circleCropTransform();
                                Glide.with(img_head.getContext()).load(rankEntity.getList().get(index).getAvatar())
                                        .apply(requestOptions)
                                        .into(img_head);
                            } else
                                my_range_layout.setVisibility(View.GONE);
                            if (pageIndex == 1) {
                                if (rankEntity.getList().size() > 0)
                                    setOneView(rankEntity.getList().get(0));
                                if (rankEntity.getList().size() > 1)
                                    setTwoView(rankEntity.getList().get(1));
                                if (rankEntity.getList().size() > 2)
                                    setThreeView(rankEntity.getList().get(2));
                            }
                            if (rankEntity.getList().size() > 3) {
                                for (int i = 0; i < 3; i++)
                                    rankEntity.getList().remove(0);
                                setRecyclerView(rankEntity.getList());
                            }
                        }
                    }

                    @Override
                    public void onError(int whichRequest, Throwable e) {

                    }
                });
    }

    private void setThreeView(GameRankEntity.ListBean rankListBean) {
        name_3.setText(rankListBean.getTeamName());
        jifen_3.setText(rankListBean.getScore() + "");
        RequestOptions requestOptions = RequestOptions.circleCropTransform();
        Glide.with(head_3.getContext()).load(rankListBean.getAvatar())
                .apply(requestOptions)
                .into(head_3);
    }

    private void setTwoView(GameRankEntity.ListBean rankListBean) {
        name_2.setText(rankListBean.getTeamName());
        jifen_2.setText(rankListBean.getScore() + "");
        RequestOptions requestOptions = RequestOptions.circleCropTransform();
        Glide.with(head_2.getContext()).load(rankListBean.getAvatar())
                .apply(requestOptions)
                .into(head_2);
    }

    private void setOneView(GameRankEntity.ListBean rankListBean) {
        name_1.setText(rankListBean.getTeamName());
        jifen_1.setText(rankListBean.getScore() + "");
        RequestOptions requestOptions = RequestOptions.circleCropTransform();
        Glide.with(head_1.getContext()).load(rankListBean.getAvatar())
                .apply(requestOptions)
                .into(head_1);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}

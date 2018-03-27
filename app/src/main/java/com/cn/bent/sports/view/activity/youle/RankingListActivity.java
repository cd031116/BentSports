package com.cn.bent.sports.view.activity.youle;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.cn.bent.sports.R;
import com.cn.bent.sports.api.BaseApi;
import com.cn.bent.sports.base.BaseActivity;
import com.cn.bent.sports.bean.LoginBase;
import com.cn.bent.sports.bean.RankEntity;
import com.cn.bent.sports.bean.ReFreshEvent;
import com.cn.bent.sports.recyclebase.CommonAdapter;
import com.cn.bent.sports.recyclebase.ViewHolder;
import com.cn.bent.sports.utils.Constants;
import com.cn.bent.sports.utils.SaveObjectUtils;
import com.cn.bent.sports.widget.DividerItemDecoration;
import com.zhl.network.RxObserver;
import com.zhl.network.RxSchedulers;
import com.zhl.network.huiqu.HuiquRxFunction;

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
    @Bind(R.id.range_name)
    TextView range_name;
    @Bind(R.id.img_head)
    ImageView img_head;
    @Bind(R.id.range_jifen)
    TextView range_jifen;
    private LoginBase user;


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
        user = (LoginBase) SaveObjectUtils.getInstance(this).getObject(Constants.USER_INFO, null);
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

    private void setRecyclerView(List<RankEntity.RankListBean> rankListBeen) {

        CommonAdapter<RankEntity.RankListBean> mAdapter = new CommonAdapter<RankEntity.RankListBean>(this, R.layout.item_range, rankListBeen) {
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
        range_list.setNestedScrollingEnabled(false);
        range_list.setAdapter(mAdapter);
        range_list.addItemDecoration(new DividerItemDecoration(this, R.drawable.list_divider));
    }



    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(ReFreshEvent event) {
        getRankData();
    }

    private void getRankData() {
        BaseApi.getDefaultService(this).getRankList()
                .map(new HuiquRxFunction<RankEntity>())
                .compose(RxSchedulers.<RankEntity>io_main())
                .subscribe(new RxObserver<RankEntity>(this, "getRankList", 1, false) {
                    @Override
                    public void onSuccess(int whichRequest, RankEntity rankEntity) {
                        if (rankEntity != null && rankEntity.getRankList() != null && rankEntity.getRankList().size() > 0) {
                            for (int i = 0; i < rankEntity.getRankList().size(); i++) {
                                if (user.getMember_id().equals(rankEntity.getRankList().get(i).getUser_id())) {
                                    range_num.setText(String.valueOf(i + 1));
                                    range_jifen.setText(String.valueOf(rankEntity.getRankList().get(i).getScore()));
                                    range_name.setText(rankEntity.getRankList().get(i).getNickname());
                                    RequestOptions requestOptions = RequestOptions.circleCropTransform();
                                    Glide.with(img_head.getContext()).load(rankEntity.getRankList().get(i).getHeadimg())
                                            .apply(requestOptions)
                                            .into(img_head);
                                    break;
                                }
                            }
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
        jifen_3.setText(rankListBean.getScore() + "");
        RequestOptions requestOptions = RequestOptions.circleCropTransform();
        Glide.with(head_3.getContext()).load(rankListBean.getHeadimg())
                .apply(requestOptions)
                .into(head_3);
    }

    private void setTwoView(RankEntity.RankListBean rankListBean) {
        name_2.setText(rankListBean.getNickname());
        jifen_2.setText(rankListBean.getScore() + "");
        RequestOptions requestOptions = RequestOptions.circleCropTransform();
        Glide.with(head_2.getContext()).load(rankListBean.getHeadimg())
                .apply(requestOptions)
                .into(head_2);
    }

    private void setOneView(RankEntity.RankListBean rankListBean) {
        name_1.setText(rankListBean.getNickname());
        jifen_1.setText(rankListBean.getScore() + "");
        RequestOptions requestOptions = RequestOptions.circleCropTransform();
        Glide.with(head_1.getContext()).load(rankListBean.getHeadimg())
                .apply(requestOptions)
                .into(head_1);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}

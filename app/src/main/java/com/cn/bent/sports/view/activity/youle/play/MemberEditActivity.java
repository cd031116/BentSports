package com.cn.bent.sports.view.activity.youle.play;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.cn.bent.sports.R;
import com.cn.bent.sports.api.BaseApi;
import com.cn.bent.sports.base.BaseActivity;
import com.cn.bent.sports.bean.GameTeamScoreEntity;
import com.cn.bent.sports.bean.MemberDataEntity;
import com.cn.bent.sports.bean.TeamGame;
import com.cn.bent.sports.database.PlayUserManager;
import com.cn.bent.sports.recyclebase.CommonAdapter;
import com.cn.bent.sports.recyclebase.ViewHolder;
import com.cn.bent.sports.widget.DividerItemDecoration;
import com.vondear.rxtools.view.RxToast;
import com.zhl.network.RxObserver;
import com.zhl.network.RxSchedulers;
import com.zhl.network.huiqu.JavaRxFunction;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import butterknife.Bind;

/**
 * Created by dawn on 2018/3/31.
 */

public class MemberEditActivity extends BaseActivity {

    @Bind(R.id.m_edit)
    LinearLayout m_edit;
    @Bind(R.id.member_head)
    ImageView member_head;
    @Bind(R.id.member_name)
    TextView member_name;
    @Bind(R.id.m_finish)
    TextView m_finish;
    @Bind(R.id.m_all)
    TextView m_all;
    @Bind(R.id.member_list)
    RecyclerView member_list;

    private int gameTeamId;
    private int leadId = -100;

    @Override
    protected int getLayoutId() {
        return R.layout.luxian_member_layout;
    }

    @Override
    public void initView() {
        String type = getIntent().getStringExtra("type");
        gameTeamId = getIntent().getIntExtra("gameTeamId", 1);
        member_list.setLayoutManager(new LinearLayoutManager(this));
        if (!TextUtils.isEmpty(type) && ("personal".equals(type))) {
            m_edit.setVisibility(View.GONE);
            obtainMemberInfo();
        } else if (!TextUtils.isEmpty(type) && ("team".equals(type))) {
            m_edit.setVisibility(View.VISIBLE);
            obtainMemberInfo();
        } else {
            m_edit.setVisibility(View.GONE);
            obtainTeamScore();
        }
        getGameDetail();
    }

    private void getGameDetail() {
        showAlert("正在获取...", true);
        BaseApi.getJavaLoginDefaultService(MemberEditActivity.this).getTeamInfo(gameTeamId + "")
                .map(new JavaRxFunction<TeamGame>())
                .compose(RxSchedulers.<TeamGame>io_main())
                .subscribe(new RxObserver<TeamGame>(MemberEditActivity.this, TAG, 1, false) {
                    @Override
                    public void onSuccess(int whichRequest, TeamGame info) {
                        dismissAlert();
                        member_name.setText(info.getTeamName());
                        m_finish.setText(info.getTeamMemberReal() + "");
                        m_all.setText(info.getTeamMemberMax() + "");
                        member_name.setText(info.getTeamName());
                        leadId = info.getLeaderId();
                        Glide.with(MemberEditActivity.this).load(info.getAvatar()).into(member_head);
                    }

                    @Override
                    public void onError(int whichRequest, Throwable e) {
                        dismissAlert();
                        RxToast.error(e.getMessage());
                    }
                });
    }

    @Override
    public void initData() {

    }


    private void obtainTeamScore() {
        BaseApi.getJavaLoginDefaultService(this).getTeamScore(gameTeamId)
                .map(new JavaRxFunction<List<GameTeamScoreEntity>>())
                .compose(RxSchedulers.<List<GameTeamScoreEntity>>io_main())
                .subscribe(new RxObserver<List<GameTeamScoreEntity>>(this, TAG, 2, false) {
                    @Override
                    public void onSuccess(int whichRequest, List<GameTeamScoreEntity> gameTeamScoreEntities) {
                        for (GameTeamScoreEntity gameTeamScoreEntity : gameTeamScoreEntities) {
                            PlayUserManager.updatePlay(gameTeamScoreEntity.getUserId(), gameTeamScoreEntity.getScore());
                        }
                        setRecyView( PlayUserManager.getHistory());
                    }
//                        PlayUserManager.insert(history);
//                            compareDaXiao(history);

                    @Override
                    public void onError(int whichRequest, Throwable e) {

                    }
                });
    }

    /**
     * 从大到小排序
     *
     * @param history
     */
    private void compareDaXiao(List<MemberDataEntity> history) {
        Collections.sort(history, new Comparator<MemberDataEntity>() {
            @Override
            public int compare(MemberDataEntity user1, MemberDataEntity user2) {
                Integer id1 = user1.getScore();
                Integer id2 = user2.getScore();
                //可以按User对象的其他属性排序，只要属性支持compareTo方法
                return id2.compareTo(id1);
            }
        });
    }

    private void setRecyView(List<MemberDataEntity> historyList) {
        member_list.setLayoutManager(new LinearLayoutManager(this));
        if (historyList != null && historyList.size() > 0) {
            CommonAdapter mAdapter = new CommonAdapter<MemberDataEntity>(this, R.layout.one_task_finish_item, historyList) {
                @Override
                protected void convert(ViewHolder holder, MemberDataEntity memberDataEntity, int position) {
                    if (memberDataEntity.getScore() == 0)
                        holder.setText(R.id.item_score, "未完成");
                    else
                        holder.setText(R.id.item_score, memberDataEntity.getScore() + "");
                    holder.setText(R.id.item_name, memberDataEntity.getNickname() + "");
                    ImageView view = (ImageView) holder.getView(R.id.item_img);
                    Glide.with(mContext).load(memberDataEntity.getAvatar()).into(view);

                }
            };
            member_list.setAdapter(mAdapter);
        }
    }

    private void obtainMemberInfo() {
        BaseApi.getJavaLoginDefaultService(this).getMemberDetailData(gameTeamId + "")
                .map(new JavaRxFunction<List<MemberDataEntity>>())
                .compose(RxSchedulers.<List<MemberDataEntity>>io_main())
                .subscribe(new RxObserver<List<MemberDataEntity>>(this, TAG, 3, false) {
                    @Override
                    public void onSuccess(int whichRequest, List<MemberDataEntity> memberDataEntities) {

                        if (memberDataEntities != null && memberDataEntities.size() > 0)
                            setRecyList(memberDataEntities);
                    }

                    @Override
                    public void onError(int whichRequest, Throwable e) {
                        RxToast.error(e.getMessage());
                    }
                });
    }

    private void setRecyList(List<MemberDataEntity> memberDataEntities) {

        CommonAdapter mAdapter = new CommonAdapter<MemberDataEntity>(this, R.layout.luxian_member_item, memberDataEntities) {

            @Override
            protected void convert(ViewHolder holder, MemberDataEntity memberDataEntity, int position) {
                holder.setText(R.id.m_name, memberDataEntity.getNickname());
                ImageView view = (ImageView) holder.getView(R.id.img_head);
                ImageView img_head_cap = (ImageView) holder.getView(R.id.img_head_cap);
                if (leadId == memberDataEntity.getUserId())
                    img_head_cap.setVisibility(View.VISIBLE);
                else
                    img_head_cap.setVisibility(View.GONE);
//                Glide.with(MemberEditActivity.this).load(memberDataEntity.getAvatar()).into(view);
            }
        };
        member_list.setAdapter(mAdapter);
        member_list.addItemDecoration(new DividerItemDecoration(this, R.drawable.list_divider));
    }
}

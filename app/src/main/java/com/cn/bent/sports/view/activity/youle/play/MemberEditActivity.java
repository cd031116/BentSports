package com.cn.bent.sports.view.activity.youle.play;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.cn.bent.sports.R;
import com.cn.bent.sports.api.BaseApi;
import com.cn.bent.sports.api.RequestLisler;
import com.cn.bent.sports.api.RxRequest;
import com.cn.bent.sports.base.BaseActivity;
import com.cn.bent.sports.bean.GameTeamScoreEntity;
import com.cn.bent.sports.bean.MemberDataEntity;
import com.cn.bent.sports.bean.TeamGame;
import com.cn.bent.sports.recyclebase.CommonAdapter;
import com.cn.bent.sports.recyclebase.ViewHolder;
import com.cn.bent.sports.utils.ImageUtils;
import com.cn.bent.sports.widget.DividerItemDecoration;
import com.vondear.rxtools.view.RxToast;
import com.zhl.network.RxSchedulers;
import com.zhl.network.huiqu.JavaRxFunction;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.OnClick;

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
    private String gameName;

    @Override
    protected int getLayoutId() {
        return R.layout.luxian_member_layout;
    }

    @Override
    public void initView() {
        String type = getIntent().getStringExtra("type");
        gameTeamId = getIntent().getIntExtra("gameTeamId", 1);
        gameName = getIntent().getStringExtra("gameName");
        member_list.setLayoutManager(new LinearLayoutManager(this));
        if (!TextUtils.isEmpty(type) && ("personal".equals(type))) {
            m_edit.setVisibility(View.GONE);
            getGameDetail(1);
        } else if (!TextUtils.isEmpty(type) && ("team".equals(type))) {
            m_edit.setVisibility(View.VISIBLE);
            getGameDetail(1);
        } else {
            m_edit.setVisibility(View.GONE);
            getGameDetail(2);
            getMemberInfo();
        }
    }

    /**
     * 获取队员信息
     */
    private void getMemberInfo() {
        showAlert("正在获取队员积分信息", true);
        BaseApi.getJavaLoginDefaultService(this).getMemberDetailData(gameTeamId + "")
                .map(new JavaRxFunction<List<MemberDataEntity>>())
                .compose(RxSchedulers.<List<MemberDataEntity>>io_main())
                .subscribe(new RxRequest<>(this, TAG, 3, new RequestLisler<List<MemberDataEntity>>() {
                    @Override
                    public void onSucess(int whichRequest, List<MemberDataEntity> memberDataEntities) {
                        Map<Integer,MemberDataEntity> memberDataEntityMap=new HashMap<>();
                        for (MemberDataEntity memberDataEntity : memberDataEntities)
                            memberDataEntityMap.put(memberDataEntity.getUserId(),memberDataEntity);
                        obtainTeamScore(memberDataEntityMap);
                    }

                    @Override
                    public void on_error(int whichRequest, Throwable e) {
                        RxToast.error("获取队员信息失败");
                    }
                }));

    }

    /**
     * 获取队伍信息
     */
    private void getGameDetail(final int type) {
        showAlert("正在获取...", true);
        BaseApi.getJavaLoginDefaultService(MemberEditActivity.this).getTeamInfo(gameTeamId + "")
                .map(new JavaRxFunction<TeamGame>())
                .compose(RxSchedulers.<TeamGame>io_main())
                .subscribe(new RxRequest<>(MemberEditActivity.this, TAG, 1, new RequestLisler<TeamGame>() {
                    @Override
                    public void onSucess(int whichRequest, TeamGame info) {
                        dismissAlert();
                        member_name.setText(info.getTeamName());
                        m_finish.setText(info.getTeamMemberReal() + "");
                        m_all.setText("/" + info.getTeamMemberMax());
                        member_name.setText(info.getTeamName());
                        leadId = info.getLeaderId();
                        if (type==1)
                            obtainMemberInfo();
                        RequestOptions myOptions = new RequestOptions()
                                .centerCrop()
                                .circleCropTransform();
                        Glide.with(MemberEditActivity.this).load(ImageUtils.getImageUrl(info.getAvatar()))
                                .apply(myOptions).into(member_head);
                    }

                    @Override
                    public void on_error(int whichRequest, Throwable e) {
                        dismissAlert();
                        RxToast.error(e.getMessage());
                    }
                }));
    }

    @Override
    public void initData() {

    }

    @OnClick({R.id.close_btn, R.id.m_edit})
    void onClick(View view) {

        switch (view.getId()) {
            case R.id.close_btn:
                this.finish();
                break;
            case R.id.m_edit:
                if (!TextUtils.isEmpty(gameName)){
                Intent intent = new Intent(this, OrganizeActivity.class);
                intent.putExtra("gameTeamId", String.valueOf(gameTeamId));
                intent.putExtra("gameName", gameName);
                startActivity(intent);
                finish();
                }
                break;
        }

    }

    /**
     * 获取团队积分情况
     * @param memberDataEntityMap
     */
    private void obtainTeamScore(final Map<Integer, MemberDataEntity> memberDataEntityMap) {
        BaseApi.getJavaLoginDefaultService(this).getTeamScore(gameTeamId)
                .map(new JavaRxFunction<List<GameTeamScoreEntity>>())
                .compose(RxSchedulers.<List<GameTeamScoreEntity>>io_main())
                .subscribe(new RxRequest<>(this, TAG, 2, new RequestLisler<List<GameTeamScoreEntity>>() {
                    @Override
                    public void onSucess(int whichRequest, List<GameTeamScoreEntity> gameTeamScoreEntities) {
                        dismissAlert();
                        List<MemberDataEntity> memberDataEntityList=new ArrayList<>();
                        if (gameTeamScoreEntities!=null&&gameTeamScoreEntities.size()>0){
                            for (GameTeamScoreEntity gameTeamScoreEntity : gameTeamScoreEntities) {
                                MemberDataEntity memberDataEntity = memberDataEntityMap.get(gameTeamScoreEntity.getUserId());
                                memberDataEntity.setScore(gameTeamScoreEntity.getScore());
                                memberDataEntityList.add(memberDataEntity);
                            }
                            compareDaXiao(memberDataEntityList);
                        }
                        else
                            for (Integer key : memberDataEntityMap.keySet()) {
                                memberDataEntityList.add(memberDataEntityMap.get(key));
                            }
                        setRecyView(memberDataEntityList);
                    }

                    @Override
                    public void on_error(int whichRequest, Throwable e) {
                        dismissAlert();
                        RxToast.error("获取队员积分信息失败");
                    }
                }));
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
                    holder.setText(R.id.item_score, memberDataEntity.getScore() + "分");
                    holder.setText(R.id.item_name, memberDataEntity.getNickname() + "");
                    ImageView view = (ImageView) holder.getView(R.id.item_img);
                    RequestOptions myOptions = new RequestOptions()
                            .centerCrop()
                            .circleCropTransform();
                    Glide.with(mContext).load(ImageUtils.getImageUrl(memberDataEntity.getAvatar()))
                            .apply(myOptions).into(view);

                }
            };
            member_list.setAdapter(mAdapter);
        }
    }

    /**
     * 获取队员信息
     */
    private void obtainMemberInfo() {
        BaseApi.getJavaLoginDefaultService(this).getMemberDetailData(gameTeamId + "")
                .map(new JavaRxFunction<List<MemberDataEntity>>())
                .compose(RxSchedulers.<List<MemberDataEntity>>io_main())
                .subscribe(new RxRequest<>(this, TAG, 3, new RequestLisler<List<MemberDataEntity>>() {
                    @Override
                    public void onSucess(int whichRequest, List<MemberDataEntity> memberDataEntities) {
                        if (memberDataEntities != null && memberDataEntities.size() > 0) {
                            Map<Integer,Boolean> isLeader=new HashMap<>();
                            for (MemberDataEntity memberDataEntity : memberDataEntities) {
                                isLeader.put(memberDataEntity.getUserId(),false);
                                if (leadId==memberDataEntity.getUserId())
                                    isLeader.put(memberDataEntity.getUserId(),true);
                            }
                            setRecyList(memberDataEntities,isLeader);
                        }
                    }

                    @Override
                    public void on_error(int whichRequest, Throwable e) {
                        RxToast.error(e.getMessage());
                    }
                }));

    }

    private void setRecyList(List<MemberDataEntity> memberDataEntities, final Map<Integer, Boolean> isLeaderMap) {

        CommonAdapter mAdapter = new CommonAdapter<MemberDataEntity>(this, R.layout.luxian_member_item, memberDataEntities) {

            @Override
            protected void convert(ViewHolder holder, MemberDataEntity memberDataEntity, int position) {
                holder.setText(R.id.m_name, memberDataEntity.getNickname());
                ImageView view = (ImageView) holder.getView(R.id.img_head);
                ImageView img_head_cap = (ImageView) holder.getView(R.id.img_head_cap);
                if (isLeaderMap.get(memberDataEntity.getUserId()))
                    img_head_cap.setVisibility(View.VISIBLE);
                else
                    img_head_cap.setVisibility(View.GONE);
                RequestOptions myOptions = new RequestOptions()
                        .centerCrop()
                        .circleCropTransform();
                Glide.with(MemberEditActivity.this).load(ImageUtils.getImageUrl(memberDataEntity.getAvatar()))
                        .apply(myOptions).into(view);
            }
        };
        member_list.setAdapter(mAdapter);
        member_list.addItemDecoration(new DividerItemDecoration(this, R.drawable.list_divider));
    }
}

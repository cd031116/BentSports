package com.cn.bent.sports.view.activity.youle.play;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.cn.bent.sports.R;
import com.cn.bent.sports.api.BaseApi;
import com.cn.bent.sports.api.RequestLisler;
import com.cn.bent.sports.api.RxRequest;
import com.cn.bent.sports.base.BaseActivity;
import com.cn.bent.sports.bean.GamePotins;
import com.cn.bent.sports.bean.MemberDataEntity;
import com.cn.bent.sports.bean.TeamGame;
import com.cn.bent.sports.recyclebase.CommonAdapter;
import com.cn.bent.sports.recyclebase.ViewHolder;
import com.cn.bent.sports.utils.ImageUtils;
import com.cn.bent.sports.view.activity.youle.bean.TeamDetail;
import com.vondear.rxtools.view.RxToast;
import com.zhl.network.RxSchedulers;
import com.zhl.network.huiqu.JavaRxFunction;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * aunthor lyj
 * create 2018/3/27/027 15:03  团队任务完成详情
 **/
public class CompleteInfoActivity extends BaseActivity {
    @Bind(R.id.member_list)
    RecyclerView member_list;
    @Bind(R.id.member_head)
    ImageView member_head;
    @Bind(R.id.m_finish)
    TextView m_finish;
    @Bind(R.id.member_name)
    TextView member_name;
    @Bind(R.id.m_all)
    TextView m_all;


    private int gameTeamId;
    private CommonAdapter<TeamDetail> mAdapter;
    private List<TeamDetail> mList = new ArrayList<>();
    private List<MemberDataEntity> dList = new ArrayList<>();
    private List<GamePotins> mpoints = new ArrayList<>();
    private int leadId = -100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_complete_info;
    }

    @Override
    public void initView() {
        super.initView();
        gameTeamId = getIntent().getIntExtra("gameTeamId", 1);
        String mlist = getIntent().getStringExtra("mlsit");
        mpoints.addAll(JSON.parseArray(mlist, GamePotins.class));
    }

    @Override
    public void initData() {
        super.initData();
        getGameDetail();
        obtainMemberInfo();
        setview();
    }


    private void getGameDetail() {
        BaseApi.getJavaLoginDefaultService(CompleteInfoActivity.this).getTeamInfo(gameTeamId + "")
                .map(new JavaRxFunction<TeamGame>())
                .compose(RxSchedulers.<TeamGame>io_main())
                .subscribe(new RxRequest<>(CompleteInfoActivity.this, TAG, 1, new RequestLisler<TeamGame>() {
                    @Override
                    public void onSucess(int whichRequest, TeamGame info) {
                        member_name.setText(info.getTeamName());
                        m_finish.setText(info.getTeamMemberReal() + "");
                        m_all.setText("/" + info.getTeamMemberMax());
                        member_name.setText(info.getTeamName());
                        leadId = info.getLeaderId();
                        RequestOptions myOptions = new RequestOptions()
                                .centerCrop()
                                .circleCropTransform();
                        Glide.with(CompleteInfoActivity.this).load(ImageUtils.getImageUrl(info.getAvatar()))
                                .apply(myOptions).into(member_head);
                    }

                    @Override
                    public void on_error(int whichRequest, Throwable e) {
                        RxToast.error(e.getMessage());
                    }
                }));
    }


    private void getTeamDetail() {
        BaseApi.getJavaLoginDefaultService(CompleteInfoActivity.this).GetTeamDrtail(gameTeamId + "")
                .map(new JavaRxFunction<List<TeamDetail>>())
                .compose(RxSchedulers.<List<TeamDetail>>io_main())
                .subscribe(new RxRequest<>(CompleteInfoActivity.this, TAG, 1, new RequestLisler<List<TeamDetail>>() {
                    @Override
                    public void onSucess(int whichRequest, List<TeamDetail> info) {
                        dismissAlert();
                        if (info != null) {
                            mList.clear();
                        }
                        mAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void on_error(int whichRequest, Throwable e) {
                        dismissAlert();
                        RxToast.error(e.getMessage());
                    }
                }));
    }

    private void obtainMemberInfo() {
        showAlert("正在获取...", true);
        BaseApi.getJavaLoginDefaultService(this).getMemberDetailData(gameTeamId + "")
                .map(new JavaRxFunction<List<MemberDataEntity>>())
                .compose(RxSchedulers.<List<MemberDataEntity>>io_main())
                .subscribe(new RxRequest<>(this, TAG, 3, new RequestLisler<List<MemberDataEntity>>() {
                    @Override
                    public void onSucess(int whichRequest, List<MemberDataEntity> memberDataEntities) {
                        dList.clear();
                        if (memberDataEntities != null) {
                            dList.addAll(memberDataEntities);
                        }
                        getTeamDetail();
                    }

                    @Override
                    public void on_error(int whichRequest, Throwable e) {
                        RxToast.error(e.getMessage());
                    }
                }));

    }

    private void setview() {
        mAdapter = new CommonAdapter<TeamDetail>(CompleteInfoActivity.this, R.layout.luxan_all_detail, mList) {
            @Override
            protected void convert(ViewHolder holder, TeamDetail teamDetail, int position) {
                holder.setText(R.id.user_name, getUserName(teamDetail.getUserId()));
                holder.setRunderWithUrl(R.id.img_head, getUserImage(teamDetail.getUserId()));
                holder.setText(R.id.point_name, getPointNamr(teamDetail.getGamePointId()));
                if (teamDetail.getState() == 1) {
                    holder.setText(R.id.m_score, teamDetail.getScore() + "分");
                } else {
                    holder.setText(R.id.m_score, "未完成");
                }

            }
        };
        member_list.setLayoutManager(new LinearLayoutManager(CompleteInfoActivity.this));
        member_list.setAdapter(mAdapter);
    }


    private String getUserName(int userId) {
        for (MemberDataEntity besn : dList) {
            if (besn.getUserId() == userId) {
                return besn.getNickname();
            }
        }
        return "";
    }

    private String getUserImage(int userId) {
        for (MemberDataEntity besn : dList) {
            if (besn.getUserId() == userId) {
                return besn.getAvatar();
            }
        }
        return "";
    }

    private String getPointNamr(int pointid) {
        for (GamePotins besn : mpoints) {
            if (besn.getId() == pointid) {
                return besn.getAlias();
            }
        }
        return "";
    }

    @OnClick({R.id.close_imag})
    void onclick(View v) {
        switch (v.getId()) {
            case R.id.close_imag:
                finish();
                break;
        }
    }
}

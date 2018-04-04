package com.cn.bent.sports.view.activity.youle.play;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.cn.bent.sports.R;
import com.cn.bent.sports.api.BaseApi;
import com.cn.bent.sports.base.BaseActivity;
import com.cn.bent.sports.bean.MemberDataEntity;
import com.cn.bent.sports.bean.TeamGame;
import com.cn.bent.sports.recyclebase.CommonAdapter;
import com.cn.bent.sports.recyclebase.ViewHolder;
import com.cn.bent.sports.widget.DividerItemDecoration;
import com.vondear.rxtools.view.RxToast;
import com.zhl.network.RxObserver;
import com.zhl.network.RxSchedulers;
import com.zhl.network.huiqu.JavaRxFunction;

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
        if (!TextUtils.isEmpty(type) && ("personal".equals(type)))
            m_edit.setVisibility(View.GONE);
        else
            m_edit.setVisibility(View.VISIBLE);
        gameTeamId = getIntent().getIntExtra("gameTeamId", 1);
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
        BaseApi.getJavaLoginDefaultService(this).getMemberDetailData(gameTeamId + "")
                .map(new JavaRxFunction<List<MemberDataEntity>>())
                .compose(RxSchedulers.<List<MemberDataEntity>>io_main())
                .subscribe(new RxObserver<List<MemberDataEntity>>(this, TAG, 1, false) {
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
        member_list.setLayoutManager(new LinearLayoutManager(this));
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

package com.cn.bent.sports.view.activity.youle.play;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.cn.bent.sports.R;
import com.cn.bent.sports.api.BaseApi;
import com.cn.bent.sports.base.BaseActivity;
import com.cn.bent.sports.bean.GameDetail;
import com.cn.bent.sports.view.activity.youle.PlayMultActivity;
import com.cn.bent.sports.view.activity.youle.bean.JoinTeam;
import com.kennyc.view.MultiStateView;
import com.vondear.rxtools.view.RxToast;
import com.zhl.network.RxObserver;
import com.zhl.network.RxSchedulers;
import com.zhl.network.huiqu.JavaRxFunction;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * aunthor lyj
 * create 2018/3/31/031 10:18  组队队员界面
 **/
public class TeamMemberActivity extends BaseActivity {
    private String gameId = "";
    private JoinTeam bean;
    @Bind(R.id.image_cover)
    ImageView image_cover;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        gameId=getIntent().getExtras().getString("gameId");
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_team_member;
    }

    @Override
    public void initView() {
        super.initView();
        getGameDetail();
    }

    @Override
    public void initData() {
        super.initData();
    }

    @OnClick({R.id.look_peo})
    void onclick(View v) {
        switch (v.getId()) {
            case R.id.look_peo:
                Intent intent = new Intent(TeamMemberActivity.this, MemberEditActivity.class);
                intent.putExtra("type", "personal");
                intent.putExtra("gameTeamId", bean.getGameTeamId());
                startActivity(intent);
                break;
        }
    }


    private void getGameDetail() {
        showAlert("正在获取...", true);
        BaseApi.getJavaLoginDefaultService(TeamMemberActivity.this).getGameDetail(gameId)
                .map(new JavaRxFunction<GameDetail>())
                .compose(RxSchedulers.<GameDetail>io_main())
                .subscribe(new RxObserver<GameDetail>(TeamMemberActivity.this, TAG, 1, false) {
                    @Override
                    public void onSuccess(int whichRequest, GameDetail info) {
                        dismissAlert();
                        setview(info);
                    }
                    @Override
                    public void onError(int whichRequest, Throwable e) {
                        dismissAlert();
                        RxToast.error(e.getMessage());
                    }
                });
    }


    private void setview(GameDetail info) {
        Glide.with(TeamMemberActivity.this)
                .load(info.getCover())
                .into(image_cover);

    }

}

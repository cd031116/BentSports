package com.cn.bent.sports.view.activity.youle.play;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.cn.bent.sports.R;
import com.cn.bent.sports.api.BaseApi;
import com.cn.bent.sports.base.BaseActivity;
import com.cn.bent.sports.bean.GameDetail;
import com.cn.bent.sports.bean.TeamGame;
import com.cn.bent.sports.view.activity.youle.PlayMultActivity;
import com.kennyc.view.MultiStateView;
import com.vondear.rxtools.view.RxToast;
import com.zhl.network.RxObserver;
import com.zhl.network.RxSchedulers;
import com.zhl.network.huiqu.JavaRxFunction;

import butterknife.OnClick;

/**
 * aunthor lyj
 * create 2018/3/27/027 15:41   准备页面  队长
 **/
public class PrepareActivity extends BaseActivity {
    TeamGame teamGame;
    @Override
    protected int getLayoutId() {
        return R.layout.activity_prepare;
    }

    @Override
    public void initView() {
        super.initView();
        teamGame=(TeamGame) getIntent().getSerializableExtra("teamGame");
    }

    @Override
    public void initData() {
        super.initData();
    }

    @OnClick({R.id.start_t,R.id.look_peo})
    void onclick(View v) {
        switch (v.getId()) {
            case R.id.start_t:
                getGameDetail();
                break;
            case R.id.look_peo:
                    Intent intent=new Intent(PrepareActivity.this,MemberEditActivity.class);
                    intent.putExtra("type","team");
                 intent.putExtra("gameTeamId",teamGame.getId());
                 startActivity(intent);
                break;
        }
    }

    private void getGameDetail() {
        showAlert("正在获取...", true);
        BaseApi.getJavaLoginDefaultService(PrepareActivity.this).startTeamGame(teamGame.getId()+"")
                .map(new JavaRxFunction<Boolean>())
                .compose(RxSchedulers.<Boolean>io_main())
                .subscribe(new RxObserver<Boolean>(PrepareActivity.this, TAG, 1, false) {
                    @Override
                    public void onSuccess(int whichRequest, Boolean info) {
                        dismissAlert();
                        if (info){
                            Intent intent=new Intent(PrepareActivity.this,PlayMultActivity.class);
                            intent.putExtra("teamGame",teamGame);
                            startActivity(intent);
                            finish();
                        }else {
                            RxToast.error("开始失败");
                        }


                    }
                    @Override
                    public void onError(int whichRequest, Throwable e) {
                        dismissAlert();
                        RxToast.error(e.getMessage());
                    }
                });
    }




}

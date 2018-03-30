package com.cn.bent.sports.view.activity.youle.play;

import android.content.Intent;
import android.view.Gravity;
import android.view.View;

import com.cn.bent.sports.R;
import com.cn.bent.sports.api.BaseApi;
import com.cn.bent.sports.base.BaseActivity;
import com.cn.bent.sports.bean.GamePotins;
import com.cn.bent.sports.bean.LoginResult;
import com.cn.bent.sports.bean.TeamGame;
import com.cn.bent.sports.utils.Constants;
import com.cn.bent.sports.utils.SaveObjectUtils;
import com.cn.bent.sports.view.activity.youle.PlayMultActivity;
import com.cn.bent.sports.view.poupwindow.ScanPoupWindow;
import com.vondear.rxtools.view.RxToast;
import com.zhl.network.RxObserver;
import com.zhl.network.RxSchedulers;
import com.zhl.network.huiqu.JavaRxFunction;

import java.util.List;

import butterknife.OnClick;
/**
*aunthor lyj
* create 2018/3/27/027 15:39   组队界面
**/
public class OrganizeActivity extends BaseActivity {

    private ScanPoupWindow mopupWindow;
    private String gameLineId;
    private String id;
    TeamGame teamGame;
    @Override
    protected int getLayoutId() {
        return R.layout.activity_organize;
    }

    @Override
    public void initView() {
        super.initView();
        gameLineId=getIntent().getExtras().getString("gameLineId");
        id=getIntent().getExtras().getString("id");
        getPoints();
    }

    @Override
    public void initData() {
        super.initData();
    }

    @OnClick({R.id.creat_scan,R.id.sure_start})
    void onclick(View v){
        switch (v.getId()){
            case R.id.creat_scan:
                shouPoup(teamGame.getId()+"");
                break;
            case R.id.sure_start:

                Intent intent=new Intent(OrganizeActivity.this,PrepareActivity.class);
                intent.putExtra("id",id);
                intent.putExtra("gameLineId",gameLineId);
                startActivity(intent);
                break;
        }
    }

    private void shouPoup(String strs) {
        mopupWindow = new ScanPoupWindow(OrganizeActivity.this,strs, null);
        mopupWindow.showAtLocation(this.findViewById(R.id.top_view),
                Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
    }

    private void getPoints() {
        showAlert("正在获取...", true);
        BaseApi.getJavaLoginDefaultService(OrganizeActivity.this).getTeamGame(gameLineId)
                .map(new JavaRxFunction<TeamGame>())
                .compose(RxSchedulers.<TeamGame>io_main())
                .subscribe(new RxObserver<TeamGame>(OrganizeActivity.this, "getTeamGame", 1, false) {
                    @Override
                    public void onSuccess(int whichRequest,TeamGame info) {
                        dismissAlert();
                        teamGame=info;
                    }

                    @Override
                    public void onError(int whichRequest, Throwable e) {

                        dismissAlert();

                        RxToast.error(e.getMessage());
                    }
                });
    }



}

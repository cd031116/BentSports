package com.cn.bent.sports.view.activity.youle.play;

import android.content.Intent;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.cn.bent.sports.R;
import com.cn.bent.sports.api.BaseApi;
import com.cn.bent.sports.base.BaseActivity;
import com.cn.bent.sports.bean.GameDetail;
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

import butterknife.Bind;
import butterknife.OnClick;
import retrofit2.http.PUT;

/**
 * aunthor lyj
 * create 2018/3/27/027 15:39   组队界面
 **/
public class OrganizeActivity extends BaseActivity {
    @Bind(R.id.user_photo)
    ImageView user_photo;
    @Bind(R.id.total_num)
    TextView total_num;
    @Bind(R.id.join_num)
    TextView join_num;

    private ScanPoupWindow mopupWindow;
    private GameDetail gameInfo;
    TeamGame teamGame;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_organize;
    }

    @Override
    public void initView() {
        super.initView();
        gameInfo = (GameDetail) getIntent().getSerializableExtra("gameInfo");
        getPoints();
    }

    @Override
    public void initData() {
        super.initData();
    }

    @OnClick({R.id.creat_scan, R.id.sure_start,R.id.top_left,R.id.top_image})
    void onclick(View v) {
        switch (v.getId()) {
            case R.id.creat_scan:
                shouPoup(teamGame.getId() + "");
                break;
            case R.id.sure_start:
                Intent intent = new Intent(OrganizeActivity.this, PrepareActivity.class);
                intent.putExtra("teamGame", teamGame);
                startActivity(intent);
                finish();
                break;
            case R.id.top_left:
            case R.id.top_image:
                OrganizeActivity.this.finish();
                break;
        }
    }

    private void shouPoup(String strs) {
        mopupWindow = new ScanPoupWindow(OrganizeActivity.this, strs, null);
        mopupWindow.showAtLocation(this.findViewById(R.id.top_view),
                Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
    }

    private void getPoints() {
        showAlert("正在创建队伍...", true);
        BaseApi.getJavaLoginDefaultService(OrganizeActivity.this).getTeamGame(gameInfo.getGameDetail().getGameId() + "")
                .map(new JavaRxFunction<TeamGame>())
                .compose(RxSchedulers.<TeamGame>io_main())
                .subscribe(new RxObserver<TeamGame>(OrganizeActivity.this, TAG, 1, false) {
                    @Override
                    public void onSuccess(int whichRequest, TeamGame info) {
                        dismissAlert();
                        teamGame = info;
                        setview(info);
                    }

                    @Override
                    public void onError(int whichRequest, Throwable e) {
                        dismissAlert();
                        RxToast.error(e.getMessage());
                    }
                });
    }

    private void setview(TeamGame info) {
        RequestOptions myOptions = new RequestOptions()
                .centerCrop()
                .circleCropTransform();
        Glide.with(OrganizeActivity.this)
                .load(info.getAvatar())
                .apply(myOptions)
                .into(user_photo);
        total_num.setText(info.getTeamMemberMax()+"");
        join_num.setText(info.getTeamMemberReal()+"");

    }



}

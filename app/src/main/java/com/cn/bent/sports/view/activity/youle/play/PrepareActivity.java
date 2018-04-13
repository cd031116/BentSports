package com.cn.bent.sports.view.activity.youle.play;

import android.content.Intent;
import android.view.View;
import android.webkit.WebView;
import android.widget.TextView;

import com.cn.bent.sports.R;
import com.cn.bent.sports.api.BaseApi;
import com.cn.bent.sports.api.RequestLisler;
import com.cn.bent.sports.api.RxRequest;
import com.cn.bent.sports.base.BaseActivity;
import com.cn.bent.sports.bean.TeamGame;
import com.cn.bent.sports.view.activity.youle.PlayActivity;
import com.cn.bent.sports.view.activity.youle.PlayMultActivity;
import com.cn.bent.sports.view.activity.youle.bean.JoinTeam;
import com.vondear.rxtools.view.RxToast;
import com.zhl.network.RxObserver;
import com.zhl.network.RxSchedulers;
import com.zhl.network.huiqu.JavaRxFunction;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;


/**
 * aunthor lyj
 * create 2018/3/27/027 15:41   准备页面  队长
 **/
public class PrepareActivity extends BaseActivity {
    TeamGame teamGame;
    private List<JoinTeam> mList = new ArrayList<>();

    @Bind(R.id.webview)
    WebView webView;
    @Bind(R.id.top_title)
    TextView top_title;
    @Override
    protected int getLayoutId() {
        return R.layout.activity_prepare;
    }

    @Override
    public void initView() {
        super.initView();
        teamGame = (TeamGame) getIntent().getSerializableExtra("teamGame");
    }

    @Override
    public void initData() {
        super.initData();
        getGamePrapre();
        top_title.setText(teamGame.getTeamName());
    }

    @OnClick({R.id.start_t, R.id.look_peo,R.id.top_left,R.id.top_image})
    void onclick(View v) {
        switch (v.getId()) {
            case R.id.start_t:
                getGameDetail();
                break;
            case R.id.look_peo:
                Intent intent = new Intent(PrepareActivity.this, MemberEditActivity.class);
                intent.putExtra("type", "team");
                intent.putExtra("gameTeamId", teamGame.getId());
                startActivity(intent);
                break;
            case  R.id.top_left:
            case R.id.top_image:
                finish();
                break;
        }
    }

    private void getGameDetail() {
        showAlert("正在获取...", true);
        BaseApi.getJavaLoginDefaultService(PrepareActivity.this).startTeamGame((long)teamGame.getId())
                .map(new JavaRxFunction<Boolean>())
                .compose(RxSchedulers.<Boolean>io_main())
                .subscribe(new RxRequest<Boolean>(PrepareActivity.this, TAG, 1, new RequestLisler<Boolean>() {
                    @Override
                    public void onSucess(int whichRequest, Boolean info) {
                        dismissAlert();
                        if (info) {
//                            if(teamGame.getTeamMemberReal()>1){
                            Intent intent = new Intent(PrepareActivity.this, PlayMultActivity.class);
                            intent.putExtra("gameTeamId", teamGame.getId());
                            startActivity(intent);
//                            }else {
//                                Intent intent = new Intent(PrepareActivity.this, PlayActivity.class);
//                                intent.putExtra("gameTeamId", teamGame.getId());
//                                startActivity(intent);
//                            }
                            finish();
                        } else {
                            RxToast.error("开始失败");
                        }
                    }

                    @Override
                    public void on_error(int whichRequest, Throwable e) {
                        dismissAlert();
                        RxToast.error(e.getMessage());
                    }
                }));
    }

    private void getGamePrapre() {
        showAlert("正在获取...", true);
        BaseApi.getJavaLoginDefaultService(PrepareActivity.this).getGamePrapre((long)teamGame.getGameId())
                .map(new JavaRxFunction<String>())
                .compose(RxSchedulers.<String>io_main())
                .subscribe(new RxRequest<String>(PrepareActivity.this, TAG, 1, new RequestLisler<String>() {
                    @Override
                    public void onSucess(int whichRequest, String info) {
                        dismissAlert();
                        setview(info);
                    }

                    @Override
                    public void on_error(int whichRequest, Throwable e) {
                        dismissAlert();
                        RxToast.error(e.getMessage());
                    }
                }));
    }

    private void setview(String info) {
        webView.loadDataWithBaseURL(null,info, "text/html", "UTF-8", null);
    }


}

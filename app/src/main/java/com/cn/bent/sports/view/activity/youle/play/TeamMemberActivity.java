package com.cn.bent.sports.view.activity.youle.play;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.cn.bent.sports.R;
import com.cn.bent.sports.api.BaseApi;
import com.cn.bent.sports.base.BaseActivity;
import com.cn.bent.sports.utils.Constants;
import com.cn.bent.sports.view.activity.youle.bean.JoinTeam;
import com.vondear.rxtools.view.RxToast;
import com.zhl.network.RxObserver;
import com.zhl.network.RxSchedulers;
import com.zhl.network.huiqu.JavaRxFunction;


import butterknife.Bind;
import butterknife.OnClick;
import okhttp3.WebSocket;
import ua.naiksoftware.stomp.LifecycleEvent;
import ua.naiksoftware.stomp.Stomp;
import ua.naiksoftware.stomp.client.StompClient;

/**
 * aunthor lyj
 * create 2018/3/31/031 10:18  组队队员界面
 **/
public class TeamMemberActivity extends BaseActivity {
    private String teamId = "";
    private JoinTeam bean;
    @Bind(R.id.image_cover)
    ImageView image_cover;
    private StompClient mStompClient;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        teamId=getIntent().getExtras().getString("teamId");
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_team_member;
    }

    @Override
    public void initView() {
        super.initView();
        createStompClient();
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
        BaseApi.getJavaLoginDefaultService(TeamMemberActivity.this).getGamePrapre(teamId)
                .map(new JavaRxFunction<String>())
                .compose(RxSchedulers.<String>io_main())
                .subscribe(new RxObserver<String>(TeamMemberActivity.this, TAG, 1, false) {
                    @Override
                    public void onSuccess(int whichRequest, String info) {
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


    private void setview(String info) {


    }

    //
    private void createStompClient() {
        mStompClient = Stomp.over(WebSocket.class, Constants.getsocket(TeamMemberActivity.this));
        mStompClient.connect();
        Toast.makeText(TeamMemberActivity.this,"开始连接 192.168.0.46:8080",Toast.LENGTH_SHORT).show();
//        mStompClient.lifecycle().subscribe(new Action1<LifecycleEvent>() {
//            @Override
//            public void call(LifecycleEvent lifecycleEvent) {
//                switch (lifecycleEvent.getType()) {
//                    case OPENED:
//                        Log.d(TAG, "Stomp connection opened");
//
//                        break;
//
//                    case ERROR:
//                        Log.e(TAG, "Stomp Error", lifecycleEvent.getException());
////                        toast("连接出错");
//                        break;
//                    case CLOSED:
//                        Log.d(TAG, "Stomp connection closed");
////                        toast("连接关闭");
//                        break;
//                }
//            }
//        });
    }
}

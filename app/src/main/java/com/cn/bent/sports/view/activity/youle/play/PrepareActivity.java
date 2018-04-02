package com.cn.bent.sports.view.activity.youle.play;

import android.content.Intent;
import android.util.Log;
import android.view.View;

import com.alibaba.fastjson.JSON;
import com.cn.bent.sports.R;
import com.cn.bent.sports.api.BaseApi;
import com.cn.bent.sports.base.BaseActivity;
import com.cn.bent.sports.bean.LoginResult;
import com.cn.bent.sports.bean.TeamGame;
import com.cn.bent.sports.utils.Constants;
import com.cn.bent.sports.utils.SaveObjectUtils;
import com.cn.bent.sports.view.activity.youle.PlayMultActivity;
import com.cn.bent.sports.view.activity.youle.bean.JoinTeam;
import com.vondear.rxtools.view.RxToast;
import com.zhl.network.RxObserver;
import com.zhl.network.RxSchedulers;
import com.zhl.network.huiqu.JavaRxFunction;


import org.java_websocket.WebSocket;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.OnClick;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import ua.naiksoftware.stomp.LifecycleEvent;
import ua.naiksoftware.stomp.Stomp;
import ua.naiksoftware.stomp.client.StompClient;
import ua.naiksoftware.stomp.client.StompMessage;


/**
 * aunthor lyj
 * create 2018/3/27/027 15:41   准备页面  队长
 **/
public class PrepareActivity extends BaseActivity {
    private StompClient mStompClient;
    TeamGame teamGame;
    private List<JoinTeam> mList = new ArrayList<>();

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
        createStompClient();
        getGamePrapre();
    }

    @OnClick({R.id.start_t, R.id.look_peo})
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
        }
    }

    private void getGameDetail() {
        showAlert("正在获取...", true);
        BaseApi.getJavaLoginDefaultService(PrepareActivity.this).startTeamGame(teamGame.getId() + "")
                .map(new JavaRxFunction<Boolean>())
                .compose(RxSchedulers.<Boolean>io_main())
                .subscribe(new RxObserver<Boolean>(PrepareActivity.this, TAG, 1, false) {
                    @Override
                    public void onSuccess(int whichRequest, Boolean info) {
                        dismissAlert();
                        if (info) {
                            Intent intent = new Intent(PrepareActivity.this, PlayMultActivity.class);
                            intent.putExtra("teamGame", teamGame);
                            startActivity(intent);
                            finish();
                        } else {
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

    private void getGamePrapre() {
        showAlert("正在获取...", true);
        BaseApi.getJavaLoginDefaultService(PrepareActivity.this).getGamePrapre(teamGame.getGameId() + "")
                .map(new JavaRxFunction<String>())
                .compose(RxSchedulers.<String>io_main())
                .subscribe(new RxObserver<String>(PrepareActivity.this, TAG, 1, false) {
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

    //组队长连接
    private void createStompClient() {
        LoginResult user = SaveObjectUtils.getInstance(PrepareActivity.this).getObject(Constants.USER_INFO, null);
        Map<String, String> map = new HashMap<>();
        map.put("Authorization", "Bearer " + user.getAccess_token());
        try {
            mStompClient = Stomp.over(WebSocket.class, "ws://" + Constants.getsocket(PrepareActivity.this), map);
            mStompClient.connect();
        } catch (Exception e) {
            Log.i("tttt", "msg=" + e.getMessage());
        }
        mStompClient.lifecycle().subscribe(new Consumer<LifecycleEvent>() {
            @Override
            public void accept(LifecycleEvent lifecycleEvent) throws Exception {

            }
        });
    }

    //    //接受消息
    private void getMsg() {
        mStompClient.topic("api/travel/topic/+" + teamGame.getId() + "" + "/join_team").subscribe(new Consumer<StompMessage>() {
            @Override
            public void accept(StompMessage stompMessage) throws Exception {
                Log.e("tttt", "call: " + stompMessage.getStompCommand());
                mList.add(JSON.parseObject(stompMessage.getStompCommand().toString(), JoinTeam.class));
            }
        });

    }

    private void init() {


    }
}

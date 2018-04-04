package com.cn.bent.sports.view.activity.youle.play;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.cn.bent.sports.R;
import com.cn.bent.sports.api.BaseApi;
import com.cn.bent.sports.base.BaseActivity;
import com.cn.bent.sports.bean.LoginResult;
import com.cn.bent.sports.utils.Constants;
import com.cn.bent.sports.utils.SaveObjectUtils;
import com.cn.bent.sports.view.activity.youle.PlayMultActivity;
import com.cn.bent.sports.view.activity.youle.bean.JoinTeam;
import com.cn.bent.sports.view.activity.youle.bean.MyGame;
import com.vondear.rxtools.view.RxToast;
import com.zhl.network.RxObserver;
import com.zhl.network.RxSchedulers;
import com.zhl.network.huiqu.JavaRxFunction;


import butterknife.Bind;
import butterknife.OnClick;
import io.reactivex.functions.Consumer;
import okhttp3.WebSocket;
import ua.naiksoftware.stomp.LifecycleEvent;
import ua.naiksoftware.stomp.Stomp;
import ua.naiksoftware.stomp.client.StompClient;
import ua.naiksoftware.stomp.client.StompMessage;

/**
 * aunthor lyj
 * create 2018/3/31/031 10:18  组队队员界面
 **/
public class TeamMemberActivity extends BaseActivity {
    private MyGame myGame ;
    private JoinTeam bean;
    @Bind(R.id.image_cover)
    ImageView image_cover;
    private StompClient mStompClient;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_team_member;
    }

    @Override
    public void initView() {
        super.initView();
        myGame = (MyGame)getIntent().getSerializableExtra("myGame");
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
                intent.putExtra("gameTeamId", myGame.getGameTeamId());
                startActivity(intent);
                break;
        }
    }


    private void getGameDetail() {
        showAlert("正在获取...", true);
        BaseApi.getJavaLoginDefaultService(TeamMemberActivity.this).getGamePrapre((long)myGame.getGameId())
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
    //组队长连接
    private void createStompClient() {
        LoginResult user = SaveObjectUtils.getInstance(TeamMemberActivity.this).getObject(Constants.USER_INFO, null);
        try {
            mStompClient = Stomp.over(org.java_websocket.WebSocket.class, "ws://" + Constants.getsocket(TeamMemberActivity.this) + "/websocket?access_token=" + user.getAccess_token());
            mStompClient.connect();
        } catch (Exception e) {
            Log.i("tttt", "msg=" + e.getMessage());
        }
        mStompClient.lifecycle().subscribe(new Consumer<LifecycleEvent>() {
            @Override
            public void accept(LifecycleEvent lifecycleEvent) {
                Log.d("tttt", "lifecycleEvent=" + lifecycleEvent.getType());
                switch (lifecycleEvent.getType()) {
                    case OPENED:
                        Log.d("tttt", "Stomp connection opened");
                        getMsg();
                        break;

                    case ERROR:
                        Log.e("tttt", "Stomp Error", lifecycleEvent.getException());

                        break;
                    case CLOSED:
                        Log.d("tttt", "Stomp connection closed");
                        createStompClient();
                        break;
                }
            }
        });
    }

    //    //接受消息
    private void getMsg() {
        mStompClient.topic("/topic/+" + myGame.getGameTeamId()+ "" + "/status").subscribe(new Consumer<StompMessage>() {
            @Override
            public void accept(StompMessage stompMessage) throws Exception {
                String msg = stompMessage.getPayload().trim();
                String datas="";
                try {
                    JSONObject jsonObject = new JSONObject();
                    JSONObject obj = jsonObject.getJSONObject(msg);
                    datas =obj.getString("data");
                }catch (Exception e){

                }
                if("GAME_START".equals(datas)){
                    Intent intent = new Intent(TeamMemberActivity.this, PlayMultActivity.class);
                    intent.putExtra("gameTeamId",  myGame.getGameTeamId());
                    startActivity(intent);
                    finish();
                }
            }
        });

    }


    @OnClick({R.id.top_left,R.id.top_image})
    void onclik(View v){
        switch (v.getId()){
            case R.id.top_left:
            case R.id.top_image:
                TeamMemberActivity.this.finish();
                break;
        }
    }
}

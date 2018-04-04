package com.cn.bent.sports.view.activity.youle.play;

import android.content.Intent;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.cn.bent.sports.R;
import com.cn.bent.sports.api.BaseApi;
import com.cn.bent.sports.base.BaseActivity;
import com.cn.bent.sports.bean.GameDetail;
import com.cn.bent.sports.bean.LoginResult;
import com.cn.bent.sports.bean.MemberDataEntity;
import com.cn.bent.sports.bean.TeamGame;
import com.cn.bent.sports.database.PlayUserManager;
import com.cn.bent.sports.recyclebase.CommonAdapter;
import com.cn.bent.sports.recyclebase.ViewHolder;
import com.cn.bent.sports.utils.Constants;
import com.cn.bent.sports.utils.SaveObjectUtils;
import com.cn.bent.sports.view.activity.youle.MyRouteListActivity;
import com.cn.bent.sports.view.activity.youle.bean.JoinTeam;
import com.cn.bent.sports.view.activity.youle.bean.UserInfo;
import com.cn.bent.sports.view.poupwindow.ScanPoupWindow;
import com.kennyc.view.MultiStateView;
import com.vondear.rxtools.view.RxToast;
import com.zhl.network.RxObserver;
import com.zhl.network.RxSchedulers;
import com.zhl.network.huiqu.JavaRxFunction;

import org.java_websocket.WebSocket;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;
import io.reactivex.functions.Consumer;
import ua.naiksoftware.stomp.LifecycleEvent;
import ua.naiksoftware.stomp.Stomp;
import ua.naiksoftware.stomp.client.StompClient;
import ua.naiksoftware.stomp.client.StompMessage;

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
    @Bind(R.id.recycle)
    RecyclerView m_recycle;
    private CommonAdapter<MemberDataEntity> mAdapter;
    private List<MemberDataEntity> mList = new ArrayList<>();
    private ScanPoupWindow mopupWindow;
    private String gameTeamId;
    TeamGame teamGame;
    private StompClient mStompClient;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_organize;
    }

    @Override
    public void initView() {
        super.initView();
        gameTeamId =  getIntent().getExtras().getString("gameTeamId");
        createStompClient();
    }

    @Override
    public void initData() {
        super.initData();
        getGameDetail();
        getPeople();
        setList();
    }

    @OnClick({R.id.creat_scan, R.id.sure_start, R.id.top_left, R.id.top_image})
    void onclick(View v) {
        switch (v.getId()) {
            case R.id.creat_scan:
                shouPoup(teamGame.getId() + "");
                break;
            case R.id.sure_start:
                PlayUserManager.insert(mList);
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
        if(mopupWindow!=null&&mopupWindow.isShowing()){
            return;
        }
        mopupWindow = new ScanPoupWindow(OrganizeActivity.this, strs, null);
        mopupWindow.showAtLocation(this.findViewById(R.id.top_view),
                Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
    }

    private void getGameDetail() {
        showAlert("正在获取...", true);
        BaseApi.getJavaLoginDefaultService(OrganizeActivity.this).getTeamInfo(gameTeamId)
                .map(new JavaRxFunction<TeamGame>())
                .compose(RxSchedulers.<TeamGame>io_main())
                .subscribe(new RxObserver<TeamGame>(OrganizeActivity.this, TAG, 1, false) {
                    @Override
                    public void onSuccess(int whichRequest, TeamGame info) {
                        dismissAlert();
                        teamGame=info;
                        setview(info);
                    }
                    @Override
                    public void onError(int whichRequest, Throwable e) {
                        dismissAlert();
                        RxToast.error(e.getMessage());
                    }
                });
    }

    //获取人个数
    private void getPeople() {
        BaseApi.getJavaLoginDefaultService(OrganizeActivity.this).getMemberDetailData(gameTeamId)
                .map(new JavaRxFunction<List<MemberDataEntity>>())
                .compose(RxSchedulers.<List<MemberDataEntity>>io_main())
                .subscribe(new RxObserver<List<MemberDataEntity>>(OrganizeActivity.this, TAG, 1, false) {
                    @Override
                    public void onSuccess(int whichRequest, List<MemberDataEntity> info) {
                        dismissAlert();
                        if(info!=null){
                            if (mList!=null){
                                mList.clear();
                            }
                            mList.addAll(info);
                        }
                          mAdapter.notifyDataSetChanged();
                        join_num.setText(mList.size() + "");
                    }
                    @Override
                    public void onError(int whichRequest, Throwable e) {
                        dismissAlert();
                    }
                });
    }



    private void setview(TeamGame info) {
        getMsg();
        RequestOptions myOptions = new RequestOptions()
                .centerCrop()
                .circleCropTransform();
        Glide.with(OrganizeActivity.this)
                .load(info.getAvatar())
                .apply(myOptions)
                .into(user_photo);
        total_num.setText(info.getTeamMemberMax() + "");
        join_num.setText(info.getTeamMemberReal() + "");
//        UserInfo infos=SaveObjectUtils.getInstance(OrganizeActivity.this).getObject(Constants.USER_BASE, null);
//        JoinTeam bean=new JoinTeam();
//        bean.setAvatar(infos.getAvatar());
//        bean.setUserId(infos.getId());
//        bean.setNickname(infos.getNickname());

    }


    //组队长连接
    private void createStompClient() {
        LoginResult user = SaveObjectUtils.getInstance(OrganizeActivity.this).getObject(Constants.USER_INFO, null);
        try {
            mStompClient = Stomp.over(WebSocket.class, "ws://" + Constants.getsocket(OrganizeActivity.this) + "/websocket?access_token=" + user.getAccess_token());
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
        mStompClient.topic("/topic/+" + teamGame.getId() + "" + "/join_team").subscribe(new Consumer<StompMessage>() {
            @Override
            public void accept(StompMessage stompMessage) throws Exception {
                String msg = stompMessage.getPayload().trim();
                JSONObject jsonObject = new JSONObject();
                JSONObject obj = jsonObject.getJSONObject(msg);
                JSONObject datas = obj.getJSONObject("data");
                MemberDataEntity bean = JSON.parseObject(datas.toString(), MemberDataEntity.class);
                mList.add(bean);
                mAdapter.notifyDataSetChanged();
                join_num.setText(mList.size() + "");
            }
        });

    }

    private void setList() {
      final   UserInfo infos= SaveObjectUtils.getInstance(OrganizeActivity.this).getObject(Constants.USER_BASE, null);
        mAdapter = new CommonAdapter<MemberDataEntity>(OrganizeActivity.this, R.layout.organize_item, mList) {
            @Override
            protected void convert(ViewHolder holder, MemberDataEntity joinTeam, int position) {
                holder.setText(R.id.name_t,joinTeam.getNickname());
                holder.setRunderWithUrl(R.id.user_photo,joinTeam.getAvatar());
                if(infos.getId()==joinTeam.getUserId()){
                    holder.setVisible(R.id.top_h,true);
                }else {
                    holder.setVisible(R.id.top_h,false);
                }
            }
        };
        m_recycle.setLayoutManager(new GridLayoutManager(OrganizeActivity.this,4));
        m_recycle.setAdapter(mAdapter);
    }


}

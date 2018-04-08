package com.cn.bent.sports.view.fragment;

import android.app.Dialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.cn.bent.sports.R;
import com.cn.bent.sports.api.BaseApi;
import com.cn.bent.sports.base.BaseFragment;
import com.cn.bent.sports.bean.GameEntity;
import com.cn.bent.sports.bean.GameInfo;
import com.cn.bent.sports.evevt.ShowPoupEvent;
import com.cn.bent.sports.sensor.UpdateUiCallBack;
import com.cn.bent.sports.utils.Constants;
import com.cn.bent.sports.utils.CornersTransform;
import com.cn.bent.sports.utils.SaveObjectUtils;
import com.cn.bent.sports.utils.StepData;
import com.cn.bent.sports.view.activity.MapActivity;
import com.cn.bent.sports.view.activity.WalkRankListActivity;
import com.cn.bent.sports.view.activity.youle.MyRouteListActivity;
import com.cn.bent.sports.view.activity.youle.PlayActivity;
import com.cn.bent.sports.view.activity.PlayFunActivity;
import com.cn.bent.sports.view.activity.youle.PlayMultActivity;
import com.cn.bent.sports.view.activity.youle.bean.JoinTeam;
import com.cn.bent.sports.view.activity.youle.bean.TaskPoint;
import com.cn.bent.sports.view.activity.youle.bean.UserInfo;
import com.cn.bent.sports.view.activity.youle.play.GameWebActivity;
import com.cn.bent.sports.view.activity.youle.play.MemberEditActivity;
import com.cn.bent.sports.view.activity.youle.play.OrderDetailActivity;
import com.cn.bent.sports.view.activity.youle.play.TeamMemberActivity;
import com.cn.bent.sports.view.service.StepService;
import com.cn.bent.sports.widget.CompletionDialog;
import com.cn.bent.sports.widget.GameErrorDialog;
import com.cn.bent.sports.widget.GameFailDialog;
import com.kennyc.view.MultiStateView;
import com.vondear.rxtools.RxActivityTool;
import com.vondear.rxtools.activity.ActivityScanerCode;
import com.vondear.rxtools.view.RxToast;
import com.zhl.network.RxObserver;
import com.zhl.network.RxSchedulers;
import com.zhl.network.huiqu.JavaResult;
import com.zhl.network.huiqu.JavaRxFunction;

import org.aisen.android.component.eventbus.NotificationCenter;
import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.functions.Consumer;
import ua.naiksoftware.stomp.client.StompMessage;

/**
 * Created by lyj on 2018/3/19 0019.
 * description
 */

public class RecommendFragment extends BaseFragment {
    public static RecommendFragment newInstance() {
        RecommendFragment fragment = new RecommendFragment();
//        Bundle bundle = new Bundle();
//        fragment.setArguments(bundle);
        return fragment;
    }

    @Bind(R.id.walk_num)
    TextView walk_num;
    @Bind(R.id.play_one)
    ImageView play_one;
    @Bind(R.id.play_two)
    ImageView play_two;
    private boolean isBind = false;
    private static final int REQUEST_Scan = 12;
    private List<GameInfo> mList = new ArrayList<>();
    @Override
    protected int getLayoutId() {
        return R.layout.recommend_fragment;
    }

    @Override
    protected void initView(View view, Bundle savedInstanceState) {

    }

    @Override
    protected void initData() {
        getGameList();
        setupService();
    }

    @OnClick({R.id.go_daolan,R.id.scan_t,R.id.line_title,R.id.activity_one,R.id.seek_rank,R.id.see_my})
    void onclick(View view){
        switch (view.getId()){
            case R.id.go_daolan:
                startActivity(new Intent(getActivity(), MapActivity.class));
                break;
            case R.id.scan_t:
                Intent intent = new Intent(getActivity(), ActivityScanerCode.class);
                startActivityForResult(intent, REQUEST_Scan);
                break;
            case R.id.line_title:
                startActivity(new Intent(getActivity(), PlayFunActivity.class));
                break;
            case R.id.activity_one:
                RxToast.warning("敬请期待下个版本");
                break;
            case R.id.seek_rank:
                startActivity(new Intent(getActivity(), WalkRankListActivity.class));
                break;
            case R.id.see_my:
                startActivity(new Intent(getActivity(), MyRouteListActivity.class));
                break;
        }
    }

    private void showAlertDialog() {
        new GameErrorDialog(getActivity(), R.style.dialog, new GameErrorDialog.OnCloseListener() {
            @Override
            public void onClick(Dialog dialog, int index) {
                dialog.dismiss();
            }
        }).show();
    }

    private void showErrorDialog(String game_name, int score) {
        new GameFailDialog(getActivity(), R.style.dialog, new GameFailDialog.OnCloseListener() {
            @Override
            public void onClick(Dialog dialog, String index) {
                dialog.dismiss();
            }
        }).setName(game_name).setScore(score).show();
    }

    private void showSuccessDialog(String game_name, int score) {
        new CompletionDialog(getActivity(), R.style.dialog, new CompletionDialog.OnCloseListener() {
            @Override
            public void onClick(Dialog dialog, String index) {
                dialog.dismiss();
                getActivity().finish();
            }
        }).setName(game_name).setScore(score).show();
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQUEST_Scan:
                if (null != data) {
                    String jieguo=data.getStringExtra("SCAN_RESULT");
                    JSONObject jsonObject = JSONObject.parseObject(jieguo);
                    String  types = jsonObject.getString("type");
                    if("game".equals(types)){
                        getJoinTeam(jsonObject.getString("param"));
                    }else {
                        RxToast.warning("二维码不正确");
                    }
                    break;
                }
        }
    }

    private void getJoinTeam(final  String teamId ) {
        showAlert("正在加入组队...", true);
        BaseApi.getJavaLoginDefaultService(getActivity()).joinTeamGame(teamId )
                .map(new JavaRxFunction<JoinTeam>())
                .compose(RxSchedulers.<JoinTeam>io_main())
                .subscribe(new RxObserver<JoinTeam>(getActivity(), TAG, 1, false) {
                    @Override
                    public void onSuccess(int whichRequest, JoinTeam info) {
                        dismissAlert();
                        Intent intent=new Intent(getActivity(),TeamMemberActivity.class);
                        intent.putExtra("gameTeamId",Integer.parseInt(teamId));
                        startActivity(intent);
                    }

                    @Override
                    public void onError(int whichRequest, Throwable e) {
                        dismissAlert();
                        RxToast.error(e.getMessage());
                    }
                });
    }



    /**
     * 开启计步服务
     */
    private void setupService() {
        Intent intent = new Intent(getActivity(), StepService.class);
        isBind = getActivity().bindService(intent, conn, Context.BIND_AUTO_CREATE);
        getActivity().startService(intent);
    }
    /**
     * 用于查询应用服务（application Service）的状态的一种interface，
     * 更详细的信息可以参考Service 和 context.bindService()中的描述，
     * 和许多来自系统的回调方式一样，ServiceConnection的方法都是进程的主线程中调用的。
     */
    ServiceConnection conn = new ServiceConnection() {
        /**
         * 在建立起于Service的连接时会调用该方法，目前Android是通过IBind机制实现与服务的连接。
         * @param name 实际所连接到的Service组件名称
         * @param service 服务的通信信道的IBind，可以通过Service访问对应服务
         */
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            StepService stepService = ((StepService.StepBinder) service).getService();
            //设置初始化数据
            walk_num.setText(stepService.getStepCount() + "");
            //设置步数监听回调
            stepService.registerCallback(new UpdateUiCallBack() {
                @Override
                public void updateUi(int stepCount) {
                    walk_num.setText(stepCount + "");
                    sendStepMsg(stepCount);
                }
            });
        }

        /**
         *
         * 当与Service之间的连接丢失的时候会调用该方法，
         * 这种情况经常发生在Service所在的进程崩溃或者被Kill的时候调用，
         * 此方法不会移除与Service的连接，当服务重新启动的时候仍然会调用 onServiceConnected()。
         * @param name 丢失连接的组件名称
         */
        @Override
        public void onServiceDisconnected(ComponentName name){

        }
    };

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (isBind) {
            getActivity().unbindService(conn);
        }
    }

    private void sendStepMsg(int steps) {
        if(!StepData.getInstance(getActivity()).isThanNow(5)){
            return;
        }
        BaseApi.getJavaLoginDefaultService(getActivity()).sendStep(steps)
                .map(new JavaRxFunction<Boolean>())
                .compose(RxSchedulers.<Boolean>io_main())
                .subscribe(new RxObserver<Boolean>(getActivity(), TAG, 1, false) {
                    @Override
                    public void onSuccess(int whichRequest, Boolean aBoolean) {
                        StepData.getInstance(getActivity()).setStepDataValue(System.currentTimeMillis());

                    }
                    @Override
                    public void onError(int whichRequest, Throwable e) {

                    }
                });
    }

    private void getGameList() {
        BaseApi.getJavaLoginDefaultService(getActivity()).getGameList("1")
                .map(new JavaRxFunction<List<GameInfo>>())
                .compose(RxSchedulers.<List<GameInfo>>io_main())
                .subscribe(new RxObserver<List<GameInfo>>(getActivity(), TAG, 1, false) {
                    @Override
                    public void onSuccess(int whichRequest, List<GameInfo> info) {
                        if (info!=null){
                            mList.clear();
                            mList.addAll(info);
                            setviews(info);
                        }
                    }

                    @Override
                    public void onError(int whichRequest, Throwable e) {
                        dismissAlert();
                    }
                });
    }


    @OnClick({R.id.play_one,R.id.play_two})
    void onclik(View v){
        switch (v.getId()){
            case R.id.play_one:
                if (mList.size()>=1){
                    Intent intent = new Intent(getActivity(), OrderDetailActivity.class);
                    intent.putExtra("gameId", mList.get(0).getId());
                    startActivity(intent);
                }
                break;
            case R.id.play_two:
                if(mList.size()>=2){
                    Intent intent1 = new Intent(getActivity(), OrderDetailActivity.class);
                    intent1.putExtra("gameId", mList.get(1).getId());
                    startActivity(intent1);
                }

                break;
        }
    }

    private void setviews(List<GameInfo> info){
        if(info.size()<=0){
            return;
        }
        RequestOptions myOptions = new RequestOptions()
                .centerCrop()
                .transform(new CornersTransform(getActivity(),12));
        Glide.with(getActivity())
                .load(info.get(0).getCover())
                .apply(myOptions)
                .into(play_one);
        if (info.size()>1){
            Glide.with(getActivity())
                    .load(info.get(1).getCover())
                    .apply(myOptions)
                    .into(play_two);
        }

    }

}

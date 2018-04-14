package com.cn.bent.sports.view.activity;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.cn.bent.sports.R;
import com.cn.bent.sports.api.BaseApi;
import com.cn.bent.sports.api.RequestLisler;
import com.cn.bent.sports.api.RxRequest;
import com.cn.bent.sports.base.BaseActivity;
import com.cn.bent.sports.bean.StepInfo;
import com.cn.bent.sports.recyclebase.CommonAdapter;
import com.cn.bent.sports.recyclebase.ViewHolder;
import com.cn.bent.sports.sensor.UpdateUiCallBack;
import com.cn.bent.sports.utils.Constants;
import com.cn.bent.sports.utils.SaveObjectUtils;
import com.cn.bent.sports.view.activity.youle.bean.UserInfo;
import com.cn.bent.sports.view.service.StepService;
import com.kennyc.view.MultiStateView;
import com.zhl.network.RxSchedulers;
import com.zhl.network.huiqu.JavaRxFunction;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;

public class WalkRankListActivity extends BaseActivity {
    @Bind(R.id.walk_list)
    RecyclerView walk_list;
    @Bind(R.id.fuli_text)
    TextView fuli_text;
    @Bind(R.id.walk_num)
    TextView walk_num;
    @Bind(R.id.walk_rank)
    TextView walk_rank;
    @Bind(R.id.multiStateView)
    MultiStateView multiStateView;
    private CommonAdapter<StepInfo.ListBean> mAdapter;
    private List<StepInfo.ListBean> mList = new ArrayList<>();
    private boolean isBind = false;
    private int stepNum=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
    }
    @Override
    protected int getLayoutId() {
        return R.layout.activity_walk_rank_list;
    }

    @Override
    public void initView() {
        super.initView();
        getGameDetail();
        sertview();
        setupService();
    }

    @Override
    public void initData() {
        super.initData();
    }

    @OnClick({R.id.return_ima})
    void onclik(View view){
        switch (view.getId()){
            case R.id.return_ima:
                WalkRankListActivity.this.finish();
                break;
        }
    }

    //获取websocket连接
    private void getGameDetail() {
        showAlert("正在获取....", true);
        BaseApi.getJavaLoginDefaultService(WalkRankListActivity.this).getStepList()
                .map(new JavaRxFunction<StepInfo>())
                .compose(RxSchedulers.<StepInfo>io_main())
                .subscribe(new RxRequest<StepInfo>(WalkRankListActivity.this, TAG, 1, new RequestLisler<StepInfo>() {
                    @Override
                    public void onSucess(int whichRequest, StepInfo info) {
                        dismissAlert();
                        if (info.getList().size()>0){
                            multiStateView.setViewState(MultiStateView.VIEW_STATE_CONTENT);
                            if(mList!=null){
                                mList.clear();
                            }
                            mList.addAll(info.getList());
                            mAdapter.notifyDataSetChanged();
                            setMyView(info);
                        }else {
                            multiStateView.setViewState(MultiStateView.VIEW_STATE_EMPTY);
                        }
                    }

                    @Override
                    public void on_error(int whichRequest, Throwable e) {
                        dismissAlert();
                        multiStateView.setViewState(MultiStateView.VIEW_STATE_ERROR);
                    }
                }));
    }

    private void sertview() {
        mAdapter=new CommonAdapter<StepInfo.ListBean>(WalkRankListActivity.this,R.layout.walk_rank_list_item,mList) {
            @Override
            protected void convert(ViewHolder holder, StepInfo.ListBean listBean, int position) {
                if (position==0){
                    holder.setBackgroundRes(R.id.state,R.drawable.r_gold);
                }else  if (position==1){
                    holder.setBackgroundRes(R.id.state,R.drawable.r_silver);
                }else if (position==2){
                    holder.setBackgroundRes(R.id.state,R.drawable.r_copper);
                }else {
                    holder.setText(R.id.state,String.valueOf(position+1));
                }
                holder.setRunderWithUrl(R.id.user_photo,listBean.getAvatar());
                holder.setText(R.id.walk_num,listBean.getStep()+"");
                holder.setText(R.id.name,listBean.getNickname());
            }
        };
        walk_list.setLayoutManager(new LinearLayoutManager(WalkRankListActivity.this));
        walk_list.setAdapter(mAdapter);
        walk_list.setNestedScrollingEnabled(false);
    }

    private void setMyView( StepInfo infos){
        boolean ishave=false;
        UserInfo users= SaveObjectUtils.getInstance(WalkRankListActivity.this).getObject(Constants.USER_BASE,null);
             for (int i=0;i<infos.getList().size();i++){
                 if (infos.getList().get(i).getUserId()==users.getId()){
                     walk_num.setText(infos.getList().get(i).getStep()+"");
                     walk_rank.setText("当前第"+(i+1)+"名");
                     ishave=true;
                     break;
                 }
             }
                if(!ishave){
                    walk_rank.setText("未上榜");
                    walk_num.setText(stepNum+"");

                }
    }

//
    /**
     * 开启计步服务
     */
    private void setupService() {
        Intent intent = new Intent(WalkRankListActivity.this, StepService.class);
        isBind = WalkRankListActivity.this.bindService(intent, conn, Context.BIND_AUTO_CREATE);
        startService(intent);
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

            //设置步数监听回调
            stepService.registerCallback(new UpdateUiCallBack() {
                @Override
                public void updateUi(int stepCount) {
                    stepNum=stepCount;
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
           unbindService(conn);
        }
    }
}

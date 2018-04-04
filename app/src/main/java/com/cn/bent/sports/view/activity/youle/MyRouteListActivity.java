package com.cn.bent.sports.view.activity.youle;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.cn.bent.sports.R;
import com.cn.bent.sports.api.BaseApi;
import com.cn.bent.sports.base.BaseActivity;
import com.cn.bent.sports.recyclebase.CommonAdapter;
import com.cn.bent.sports.recyclebase.ViewHolder;
import com.cn.bent.sports.utils.Constants;
import com.cn.bent.sports.utils.SaveObjectUtils;
import com.cn.bent.sports.view.activity.youle.bean.JoinTeam;
import com.cn.bent.sports.view.activity.youle.bean.MyGame;
import com.cn.bent.sports.view.activity.youle.bean.UserInfo;
import com.cn.bent.sports.view.activity.youle.play.OrganizeActivity;
import com.cn.bent.sports.view.activity.youle.play.PrepareActivity;
import com.cn.bent.sports.view.activity.youle.play.TeamMemberActivity;
import com.jwenfeng.library.pulltorefresh.BaseRefreshListener;
import com.jwenfeng.library.pulltorefresh.PullToRefreshLayout;
import com.kennyc.view.MultiStateView;
import com.vondear.rxtools.RxTool;
import com.vondear.rxtools.view.RxToast;
import com.zhl.network.RxObserver;
import com.zhl.network.RxSchedulers;
import com.zhl.network.huiqu.JavaRxFunction;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;

/**
*aunthor lyj
* create 2018/3/27/027 15:27  我的线路列表
**/
public class MyRouteListActivity extends BaseActivity {
    @Bind(R.id.multiStateView)
    MultiStateView multiStateView;
    @Bind(R.id.refresh)
    PullToRefreshLayout refresh;
    @Bind(R.id.rlist)
    RecyclerView rlist;

    @Bind(R.id.top_title)
    TextView top_title;

    private CommonAdapter<MyGame> mAdapter;
    private List<MyGame> mList=new ArrayList<>();
    @Override
    protected int getLayoutId() {
        return R.layout.activity_my_route_list;
    }

    @Override
    public void initView() {
        super.initView();
        top_title.setText("我的路线");
        getMyRoute(true);
        setview();
    }

    @Override
    public void initData() {
        super.initData();
        refresh.setRefreshListener(new BaseRefreshListener() {
            @Override
            public void refresh() {
                getMyRoute(false);
            }

            @Override
            public void loadMore() {
                refresh.finishLoadMore();
            }
        });
        refresh.setCanLoadMore(false);
    }


    private void getMyRoute(boolean show) {
        if(show){
            showAlert("正在获取数据...", true);
        }
        BaseApi.getJavaLoginDefaultService(MyRouteListActivity.this).getMyRoute( )
                .map(new JavaRxFunction<List<MyGame>>())
                .compose(RxSchedulers.<List<MyGame>>io_main())
                .subscribe(new RxObserver<List<MyGame>>(MyRouteListActivity.this, TAG, 1, false) {
                    @Override
                    public void onSuccess(int whichRequest, List<MyGame> info) {
                        dismissAlert();
                        refresh.finishRefresh();
                        if (info.size()>0) {
                            multiStateView.setViewState(MultiStateView.VIEW_STATE_CONTENT);
                            if(mList!=null){
                                mList.clear();
                            }
                            mList.addAll(info);
                            mAdapter.notifyDataSetChanged();
                        }else {
                            multiStateView.setViewState(MultiStateView.VIEW_STATE_EMPTY);
                        }
                    }

                    @Override
                    public void onError(int whichRequest, Throwable e) {
                        dismissAlert();
                        refresh.finishRefresh();
                        multiStateView.setViewState(MultiStateView.VIEW_STATE_ERROR);
                        RxToast.error(e.getMessage());
                    }
                });
    }


    private void  setview(){
        mAdapter=new CommonAdapter<MyGame>(MyRouteListActivity.this,R.layout.my_route_item,mList) {
            @Override
            protected void convert(ViewHolder holder,final MyGame myGame, int position) {

                holder.setText(R.id.name,myGame.getTitle());
                holder.setRunderBackrund(R.id.photo,myGame.getCover(),MyRouteListActivity.this);
                if(myGame.getState()==1){
                    holder.setTextColor(R.id.state, Color.parseColor("#ffffff"));
                    holder.setText(R.id.state,"未开始");
                holder.setBackgroundRes(R.id.state,R.drawable.my_un_start);
                }
                if(myGame.getState()==2){
                    holder.setTextColor(R.id.state, Color.parseColor("#ffffff"));
                    holder.setText(R.id.state,"进行中");
                    holder.setBackgroundRes(R.id.state,R.drawable.my_playing);
                }
                if(myGame.getState()==3){
                    holder.setTextColor(R.id.state, Color.parseColor("#ffffff"));
                    holder.setText(R.id.state,"已完成");
                    holder.setBackgroundRes(R.id.state,R.drawable.my_finish);
                }

                if(myGame.getState()==4){
                    holder.setTextColor(R.id.state, Color.parseColor("#3fcfe4"));
                    holder.setText(R.id.state,"强制结束");
                    holder.setBackgroundRes(R.id.state,R.drawable.my_ending);
                }
                if(myGame.getState()==5){
                    holder.setTextColor(R.id.state, Color.parseColor("#fd7d6f"));
                    holder.setText(R.id.state,"超时结束");
                    holder.setBackgroundRes(R.id.state,R.drawable.my_ending);
                }
                holder.setOnClickListener(R.id.top_click, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        UserInfo infos= SaveObjectUtils.getInstance(MyRouteListActivity.this).getObject(Constants.USER_BASE, null);
                        if(myGame.getState()==1){
                            if(infos.getId()==myGame.getLeaderId()){
                                Intent intent = new Intent(MyRouteListActivity.this, OrganizeActivity.class);
                                intent.putExtra("gameTeamId", myGame.getGameTeamId()+"");
                                startActivity(intent);
                            }else {
                                Intent intent = new Intent(MyRouteListActivity.this, TeamMemberActivity.class);
                                intent.putExtra("gameTeamId", myGame.getGameTeamId()+"");
                                startActivity(intent);
                            }
                        }
                        if (myGame.getState()==2) {
                            Intent intent = new Intent(MyRouteListActivity.this, PlayMultActivity.class);
                            intent.putExtra("gameTeamId", myGame.getGameTeamId()+"");
                            startActivity(intent);
                        }
                        if (myGame.getState()==3) {
                          RxToast.success("已完成");
                        }
                        if (myGame.getState()==4) {
                            RxToast.warning("已强制结束");
                        }
                        if (myGame.getState()==5) {
                            RxToast.warning("超时结束");
                        }
                    }
                });
            }
        };
        rlist.setLayoutManager(new LinearLayoutManager(MyRouteListActivity.this));
        rlist.setAdapter(mAdapter);
    }


    @OnClick({R.id.top_left,R.id.top_image})
    void onclik(View v){
        switch (v.getId()){
            case R.id.top_left:
            case R.id.top_image:
                MyRouteListActivity.this.finish();
                break;
        }
    }
}

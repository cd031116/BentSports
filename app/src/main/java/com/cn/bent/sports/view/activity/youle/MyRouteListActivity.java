package com.cn.bent.sports.view.activity.youle;

import com.cn.bent.sports.R;
import com.cn.bent.sports.base.BaseActivity;
import com.jwenfeng.library.pulltorefresh.BaseRefreshListener;
import com.jwenfeng.library.pulltorefresh.PullToRefreshLayout;
import com.kennyc.view.MultiStateView;

import butterknife.Bind;

/**
*aunthor lyj
* create 2018/3/27/027 15:27  我的线路列表
**/
public class MyRouteListActivity extends BaseActivity {
    @Bind(R.id.multiStateView)
    MultiStateView multiStateView;
    @Bind(R.id.refresh)
    PullToRefreshLayout refresh;
    @Override
    protected int getLayoutId() {
        return R.layout.activity_my_route_list;
    }

    @Override
    public void initView() {
        super.initView();
    }

    @Override
    public void initData() {
        super.initData();
        refresh.setRefreshListener(new BaseRefreshListener() {
            @Override
            public void refresh() {
                refresh.finishRefresh();
            }

            @Override
            public void loadMore() {
                refresh.finishLoadMore();
            }
        });
    }
}

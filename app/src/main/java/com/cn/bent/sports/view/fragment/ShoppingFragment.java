package com.cn.bent.sports.view.fragment;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.cn.bent.sports.R;
import com.cn.bent.sports.base.BaseFragment;
import com.cn.bent.sports.utils.StatusBarUtil;
import com.cn.bent.sports.view.activity.youle.MyRouteListActivity;
import com.jwenfeng.library.pulltorefresh.BaseRefreshListener;
import com.jwenfeng.library.pulltorefresh.PullToRefreshLayout;
import com.kennyc.view.MultiStateView;

import butterknife.Bind;

/**
 * Created by lyj on 2018/3/19 0019.
 * description
 */

public class ShoppingFragment extends BaseFragment {
    public static ShoppingFragment newInstance() {
        ShoppingFragment fragment = new ShoppingFragment();
//        Bundle bundle = new Bundle();
//        fragment.setArguments(bundle);
        return fragment;
    }
    @Bind(R.id.top_image)
    ImageView top_image;
    @Bind(R.id.multiStateView)
    MultiStateView multiStateView;
    @Bind(R.id.refresh)
    PullToRefreshLayout refresh;
    @Override
    protected int getLayoutId() {
        return R.layout.shopping_fragment;
    }

    @Override
    protected void initView(View view, Bundle savedInstanceState) {
        multiStateView.setViewState(MultiStateView.VIEW_STATE_EMPTY);
        top_image.setVisibility(View.GONE);
    }

    @Override
    protected void initData() {
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

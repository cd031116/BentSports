package com.cn.bent.sports.view.activity;

import android.content.Intent;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.cn.bent.sports.R;
import com.cn.bent.sports.api.BaseApi;
import com.cn.bent.sports.api.RequestLisler;
import com.cn.bent.sports.api.RxRequest;
import com.cn.bent.sports.base.BaseActivity;
import com.cn.bent.sports.bean.GameInfo;
import com.cn.bent.sports.bean.LoginResult;
import com.cn.bent.sports.utils.CornersTransform;
import com.cn.bent.sports.utils.ImageUtils;
import com.cn.bent.sports.view.activity.youle.play.OrderDetailActivity;
import com.kennyc.view.MultiStateView;
import com.vondear.rxtools.view.RxToast;
import com.zhl.network.RxObserver;
import com.zhl.network.RxSchedulers;
import com.zhl.network.huiqu.JavaRxFunction;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;
import me.crosswall.lib.coverflow.CoverFlow;
import me.crosswall.lib.coverflow.core.PageItemClickListener;
import me.crosswall.lib.coverflow.core.PagerContainer;

/**
 * aunthor lyj
 * create 2018/3/27/027 15:24   趣玩线路列表
 **/
public class PlayFunActivity extends BaseActivity {
    @Bind(R.id.pager_container)
    PagerContainer mContainer;
    @Bind(R.id.multiStateView)
    MultiStateView multiStateView;
    @Bind(R.id.now_num)
    TextView now_num;
    @Bind(R.id.total_num)
    TextView total_num;
    ViewPager viewPager;
    MyPagerAdapter myPagerAdapter;
    private List<GameInfo> minfo = new ArrayList<>();

    public LoginResult user;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_play_fun;
    }

    @Override
    public void initView() {
        super.initView();
        viewPager = mContainer.getViewPager();
        myPagerAdapter = new MyPagerAdapter(minfo);
        viewPager.setAdapter(myPagerAdapter);
        viewPager.setOffscreenPageLimit(3);
        viewPager.setClipChildren(false);

        mContainer.setPageItemClickListener(new PageItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {

            }
        });

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                now_num.setText(position + 1 + "");
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        new CoverFlow.Builder()
                .with(viewPager)
                .scale(0f)
                .pagerMargin(0f)
                .spaceSize(0f)
                .rotationY(0f)
                .build();
    }

    @Override
    public void initData() {
        super.initData();
        getGameList();
    }

    private class MyPagerAdapter extends PagerAdapter {
        private List<GameInfo> mList = new ArrayList<>();

        public MyPagerAdapter(List<GameInfo> mdata) {
            this.mList = mdata;
        }

        @Override
        public Object instantiateItem(ViewGroup container, final int position) {

            View view = LayoutInflater.from(PlayFunActivity.this).inflate(R.layout.item_cover, null);
            ImageView imageView = (ImageView) view.findViewById(R.id.image_cover);
            TextView go_task = (TextView) view.findViewById(R.id.go_task);

            TextView name_p = (TextView) view.findViewById(R.id.name_p);
            TextView p_num = (TextView) view.findViewById(R.id.p_num);
            TextView num_dot = (TextView) view.findViewById(R.id.num_dot);
            TextView type_t = (TextView) view.findViewById(R.id.type_t);

            type_t.setText("依次穿越");

            num_dot.setText(mList.get(position).getPointCount() + "个点标");
            name_p.setText(mList.get(position).getTitle());
            p_num.setText(mList.get(position).getMaxPeople() + "人/组");
            go_task.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(PlayFunActivity.this, OrderDetailActivity.class);
                    intent.putExtra("gameId", mList.get(position).getId());
                    startActivity(intent);
                    finish();
                }
            });

            RequestOptions myOptions = new RequestOptions()
                    .centerCrop()
                    .transform(new CornersTransform(PlayFunActivity.this,8));
            Glide.with(PlayFunActivity.this)
                    .load(ImageUtils.getImageUrl(mList.get(position).getCover()))
                    .apply(myOptions)
                    .into(imageView);
            container.addView(view);
            return view;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

        @Override
        public int getCount() {
            return mList.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return (view == object);
        }
    }


    private void getGameList() {
        showAlert("正在获取...", true);
        BaseApi.getJavaLoginDefaultService(PlayFunActivity.this).getGameList("1")
                .map(new JavaRxFunction<List<GameInfo>>())
                .compose(RxSchedulers.<List<GameInfo>>io_main())
                .subscribe(new RxRequest<List<GameInfo>>(PlayFunActivity.this, TAG, 1, new RequestLisler<List<GameInfo>>() {
                    @Override
                    public void onSucess(int whichRequest, List<GameInfo> info) {
                        dismissAlert();
                        if (info.size()>0){
                            multiStateView.setViewState(MultiStateView.VIEW_STATE_CONTENT);
                            minfo.addAll(info);
                            myPagerAdapter.notifyDataSetChanged();
                            total_num.setText(info.size() + "");
                        }else {
                            multiStateView.setViewState(MultiStateView.VIEW_STATE_EMPTY);
                        }
                    }

                    @Override
                    public void on_error(int whichRequest, Throwable e) {
                        dismissAlert();
                        multiStateView.setViewState(MultiStateView.VIEW_STATE_ERROR);
                        RxToast.error(e.getMessage());
                    }
                }));

    }

    @OnClick({R.id.top_left})
    void onclik(View v) {
        switch (v.getId()) {
            case R.id.top_left:
                finish();
                break;
        }
    }
}

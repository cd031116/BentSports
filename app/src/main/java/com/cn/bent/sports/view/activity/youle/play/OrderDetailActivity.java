package com.cn.bent.sports.view.activity.youle.play;

import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.Options;
import com.bumptech.glide.request.RequestOptions;
import com.cn.bent.sports.MainActivity;
import com.cn.bent.sports.R;
import com.cn.bent.sports.api.BaseApi;
import com.cn.bent.sports.base.BaseActivity;
import com.cn.bent.sports.base.BaseConfig;
import com.cn.bent.sports.bean.GameDetail;
import com.cn.bent.sports.bean.GameInfo;
import com.cn.bent.sports.bean.LoginResult;
import com.cn.bent.sports.bean.TeamGame;
import com.cn.bent.sports.utils.Constants;
import com.cn.bent.sports.utils.ImageUtils;
import com.cn.bent.sports.utils.SaveObjectUtils;
import com.cn.bent.sports.view.activity.PlayFunActivity;
import com.cn.bent.sports.view.activity.youle.MyRouteListActivity;
import com.cn.bent.sports.view.activity.youle.RankingListActivity;
import com.cn.bent.sports.view.fragment.CardFragment;
import com.cn.bent.sports.view.fragment.IsMeFragment;
import com.cn.bent.sports.view.fragment.RecommendFragment;
import com.cn.bent.sports.view.fragment.ShoppingFragment;
import com.cn.bent.sports.view.fragment.WebViewFragment;
import com.cn.bent.sports.widget.MyScroview;
import com.kennyc.view.MultiStateView;
import com.vondear.rxtools.view.RxToast;
import com.zhl.network.RxObserver;
import com.zhl.network.RxSchedulers;
import com.zhl.network.huiqu.JavaRxFunction;

import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;

/**
*aunthor lyj
* create 2018/3/27/027 15:36   路线详情
**/
public class OrderDetailActivity extends BaseActivity implements MyScroview.OnScrollListener {
    @Bind(R.id.myscroview)
    MyScroview myscroview;
    @Bind(R.id.search02)
    LinearLayout search02;// 在MyScrollView里面的购买布局
    @Bind(R.id.search01)
    LinearLayout search01;
    @Bind(R.id.tab_mian)
    LinearLayout tab_mian;
    @Bind(R.id.tab1_t)
    TextView tab1_t;
    @Bind(R.id.tab1_v)
    TextView tab1_v;
    @Bind(R.id.tab2_t)
    TextView tab2_t;
    @Bind(R.id.tab2_v)
    TextView tab2_v;
    @Bind(R.id.tab3_t)
    TextView tab3_t;
    @Bind(R.id.tab3_v)
    TextView tab3_v;
    @Bind(R.id.multiStateView)
    MultiStateView multiStateView;
    @Bind(R.id.image_cover)
    ImageView image_cover;
    @Bind(R.id.name_t)
    TextView name_t;
    @Bind(R.id.group_price)
    TextView group_price;
    @Bind(R.id.type_t)
    TextView type_t;
    @Bind(R.id.num_dot)
    TextView num_dot;
    @Bind(R.id.p_num)
    TextView p_num;
    @Bind(R.id.address_t)
    TextView address_t;
    FragmentManager mFragmentMan;
    private int select = 1;
    private int topHeight;
    private int journeyHeight;
    private int payknowHeight;
    private String spot_team_id;
    private String deviceId;
    private String memberId;
    private String title;
    private String gameId;
    private GameDetail myGame;
    @Override
    protected int getLayoutId() {
        return R.layout.activity_order_detail;
    }

    @Override
    public void initView() {
        super.initView();
        gameId=getIntent().getExtras().getString("gameId");
        myscroview.setOnScrollListener(this);
        mFragmentMan = getSupportFragmentManager();
    }

    @Override
    public void initData() {
        super.initData();
        getGameDetail();
    }

    @Override
    public void onResume() {
        super.onResume();
    }


    private void changeview(int index) {
        tab1_t.setTextColor(Color.parseColor("#333333"));
        tab2_t.setTextColor(Color.parseColor("#333333"));
        tab3_t.setTextColor(Color.parseColor("#333333"));
        tab1_v.setSelected(false);
        tab2_v.setSelected(false);
        tab3_v.setSelected(false);
        if (index == 1) {
            changeFrament("afragment",0);
            tab1_t.setTextColor(Color.parseColor("#e11818"));
            tab1_v.setSelected(true);
        } else if (index == 2) {
            changeFrament("bfragment",1);
            tab2_t.setTextColor(Color.parseColor("#e11818"));
            tab2_v.setSelected(true);
        } else {
            changeFrament("cfragment",2);
            tab3_t.setTextColor(Color.parseColor("#e11818"));
            tab3_v.setSelected(true);
        }
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            topHeight = search02.getBottom() - search02.getHeight();
        }
    }

    @Override
    public void onScroll(int scrollY) {
        if (scrollY >= topHeight) {
            if (tab_mian.getParent() != search01) {
                search02.removeView(tab_mian);
                search01.addView(tab_mian);
            }
        } else {
            if (tab_mian.getParent() != search02) {
                search01.removeView(tab_mian);
                search02.addView(tab_mian);
            }

        }
    }


    @Override
    public void onStart() {
        super.onStart();

    }

    @Override
    public void onStop() {
        super.onStop();
        //结束轮播
    }


    @OnClick({R.id.submit,R.id.tab1_mian,R.id.tab2_mian,R.id.tab3_mian,R.id.go_rank,R.id.exit_image})
    void onclick(View v){
        switch (v.getId()){
            case R.id.submit:
                getOrgnize();
                break;
            case R.id.tab1_mian:
                changeview(1);
                break;
            case R.id.tab2_mian:
                changeview(2);
                break;
            case R.id.tab3_mian:
                changeview(3);
                break;
            case R.id.go_rank:
             Intent intent=new Intent(OrderDetailActivity.this, RankingListActivity.class);
                intent.putExtra("gameId",Integer.parseInt(gameId));
             startActivity(intent);
                break;
            case R.id.exit_image:
                finish();
                break;
        }
    }

    private void getGameDetail() {
        showAlert("正在获取...", true);
        BaseApi.getJavaLoginDefaultService(OrderDetailActivity.this).getGameDetail(gameId)
                .map(new JavaRxFunction<GameDetail>())
                .compose(RxSchedulers.<GameDetail>io_main())
                .subscribe(new RxObserver<GameDetail>(OrderDetailActivity.this, TAG, 1, false) {
                    @Override
                    public void onSuccess(int whichRequest, GameDetail info) {
                        dismissAlert();
                        multiStateView.setViewState(MultiStateView.VIEW_STATE_CONTENT);
                        myGame=info;
                        setview(info);
                        changeview(select);
                    }
                    @Override
                    public void onError(int whichRequest, Throwable e) {
                        dismissAlert();
                        multiStateView.setViewState(MultiStateView.VIEW_STATE_ERROR);
                        RxToast.error(e.getMessage());
                    }
                });
    }

    private void  setview(GameDetail info){
        RequestOptions myOptions = new RequestOptions()
                .centerCrop();
        Glide.with(OrderDetailActivity.this)
                .load(ImageUtils.getImageUrl(info.getCover()))
                .apply(myOptions)
                .into(image_cover);
        name_t.setText(info.getTitle());
        group_price.setText(info.getPrice()+"");
        if (info.getType()==1){
            type_t.setText("依次穿越");
        }
        if (info.getType()==2){
            type_t.setText("限时挑战");
        }
        if (info.getType()==3){
            type_t.setText("自由规划");
        }
        num_dot.setText(info.getPointCount()+"个点标");
        p_num.setText(info.getMaxPeople()+"人");
        address_t.setText(info.getAddress());
    }


    private void getOrgnize() {
        showAlert("正在提交...", true);
        BaseApi.getJavaLoginDefaultService(OrderDetailActivity.this).getTeamGame(gameId)
                .map(new JavaRxFunction<TeamGame>())
                .compose(RxSchedulers.<TeamGame>io_main())
                .subscribe(new RxObserver<TeamGame>(OrderDetailActivity.this, TAG, 1, false) {
                    @Override
                    public void onSuccess(int whichRequest, TeamGame info) {
                        dismissAlert();
                        Intent intent=new Intent(OrderDetailActivity.this,MyRouteListActivity.class);
                        startActivity(intent);
                        finish();
                    }

                    @Override
                    public void onError(int whichRequest, Throwable e) {
                        dismissAlert();
                        RxToast.error(e.getMessage());
                    }
                });
    }
//
// 3. 先进第二个或第三个的子模块，再返回首页
private String lastFragmentTag = null;

    private void changeFrament(String tag, int index) {
        BaseConfig bg = BaseConfig.getInstance(OrderDetailActivity.this);
        if (mFragmentMan != null) {
            // Add default fragments to view. Try to reuse old fragments or create new ones
            FragmentTransaction transaction = mFragmentMan.beginTransaction();
            // 当前的Fragment
            Fragment mCurrentFragment = mFragmentMan.findFragmentByTag(tag);
            // 前一次Fragment
            Fragment mLastFragment = null;
            if (!TextUtils.isEmpty(lastFragmentTag) && !lastFragmentTag.equals(tag)) {
                mLastFragment = mFragmentMan.findFragmentByTag(lastFragmentTag);
            }
            // 构造当前Fragment
            if (mCurrentFragment == null) {
                switch (index) {
                    case 0:
                        mCurrentFragment = WebViewFragment.newInstance(myGame.getGameDetail().getDetail());
                        break;
                    case 1:
                        mCurrentFragment = WebViewFragment.newInstance(myGame.getGameDetail().getNotice());//子Fragment实例
                        break;
                    case 2:
                        mCurrentFragment = WebViewFragment.newInstance(myGame.getGameDetail().getTip());
                        //子Fragment实例
                        break;
                }
                transaction.add(R.id.id_content, mCurrentFragment, tag);
            }
            // 显示当前Fragment
            else {
                transaction.show(mCurrentFragment);
            }
            // 隐藏前一次Fragment
            if (mLastFragment != null) {
                transaction.hide(mLastFragment);
            }
            transaction.commit();
            lastFragmentTag = tag;
        }
    }

}

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
import com.cn.bent.sports.MainActivity;
import com.cn.bent.sports.R;
import com.cn.bent.sports.api.BaseApi;
import com.cn.bent.sports.base.BaseActivity;
import com.cn.bent.sports.bean.GameInfo;
import com.cn.bent.sports.bean.LoginResult;
import com.cn.bent.sports.utils.Constants;
import com.cn.bent.sports.utils.SaveObjectUtils;
import com.cn.bent.sports.utils.ToastUtils;
import com.cn.bent.sports.view.activity.youle.PlayMultActivity;
import com.cn.bent.sports.view.activity.youle.play.OrderDetailActivity;
import com.vondear.rxtools.view.RxToast;
import com.zhl.network.RxObserver;
import com.zhl.network.RxSchedulers;
import com.zhl.network.huiqu.HuiquRxTBFunction;
import com.zhl.network.huiqu.JavaRxFunction;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import me.crosswall.lib.coverflow.CoverFlow;
import me.crosswall.lib.coverflow.core.PageItemClickListener;
import me.crosswall.lib.coverflow.core.PagerContainer;
/**
*aunthor lyj
* create 2018/3/27/027 15:24   趣玩线路列表
**/
public class PlayFunActivity extends BaseActivity {
    @Bind(R.id.pager_container)
    PagerContainer mContainer;

    @Bind(R.id.now_num)
    TextView now_num;
    @Bind(R.id.total_num)
    TextView total_num;
     ViewPager viewPager ;
    MyPagerAdapter myPagerAdapter;
    private List<GameInfo> minfo=new ArrayList<>();


    @Override
    protected int getLayoutId() {
        return R.layout.activity_play_fun;
    }

    @Override
    public void initView() {
        super.initView();
        total_num.setText(minfo.size()+"");
        viewPager = mContainer.getViewPager();
        myPagerAdapter=new MyPagerAdapter(minfo);
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
                now_num.setText(position+1+"");
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
        private List<GameInfo> mList=new ArrayList<>();
        public  MyPagerAdapter(List<GameInfo> mdata){
            this.mList=mdata;
        }
        @Override
        public Object instantiateItem(ViewGroup container,final int position) {

            View view = LayoutInflater.from(PlayFunActivity.this).inflate(R.layout.item_cover,null);
            ImageView imageView = (ImageView) view.findViewById(R.id.image_cover);
            TextView go_task=(TextView) view.findViewById(R.id.go_task);
            TextView name_p=(TextView) view.findViewById(R.id.name_p);
            TextView p_num=(TextView) view.findViewById(R.id.p_num);
            TextView num_dot=(TextView) view.findViewById(R.id.num_dot);
            TextView type_t=(TextView) view.findViewById(R.id.type_t);

            type_t.setText("依次穿越");

            num_dot.setText(mList.get(position).getPointCount()+"个点标");
            name_p.setText(mList.get(position).getTitle());
            p_num.setText(mList.get(position).getMaxPeople()+"人/组");
            go_task.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent intent=new Intent(PlayFunActivity.this,OrderDetailActivity.class);
                    intent.putExtra("gameId",mList.get(position).getId());
                    startActivity(intent);
                }
            });
            Glide.with(PlayFunActivity.this)
                    .load(mList.get(position).getCover())
                    .into(imageView);
            container.addView(view);
            return view;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View)object);
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
        BaseApi.getJavaLoginDefaultService(PlayFunActivity.this,user.getAccess_token()).getGameList("1")
                .map(new JavaRxFunction<List<GameInfo>>())
                .compose(RxSchedulers.<List<GameInfo>>io_main())
                .subscribe(new RxObserver<List<GameInfo>>(PlayFunActivity.this, "login", 1, false) {
                    @Override
                    public void onSuccess(int whichRequest, List<GameInfo> info) {

                        dismissAlert();
                        minfo.addAll(info);
                        myPagerAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onError(int whichRequest, Throwable e) {
                        dismissAlert();
                        RxToast.error(e.getMessage());
                    }
                });
    }



}

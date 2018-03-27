package com.cn.bent.sports.view.activity;

import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.cn.bent.sports.R;
import com.cn.bent.sports.base.BaseActivity;
import com.cn.bent.sports.utils.ToastUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import me.crosswall.lib.coverflow.CoverFlow;
import me.crosswall.lib.coverflow.core.PageItemClickListener;
import me.crosswall.lib.coverflow.core.PagerContainer;

public class PlayFunActivity extends BaseActivity {
    @Bind(R.id.pager_container)
    PagerContainer mContainer;

    @Bind(R.id.now_num)
    TextView now_num;
    @Bind(R.id.total_num)
    TextView total_num;
     ViewPager viewPager ;
    private List<String> minfo=new ArrayList<>();
    @Override
    protected int getLayoutId() {
        return R.layout.activity_play_fun;
    }

    @Override
    public void initView() {
        super.initView();
        for (int i=0;i<8;i++){
            minfo.add(i+"");
        }
        total_num.setText(minfo.size()+"");
        viewPager = mContainer.getViewPager();
        viewPager.setAdapter(new MyPagerAdapter(minfo));
        viewPager.setOffscreenPageLimit(4);
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
    }

    private class MyPagerAdapter extends PagerAdapter {
        private List<String> mList=new ArrayList<>();
        public  MyPagerAdapter(List<String> mdata){
            this.mList=mdata;
        }
        @Override
        public Object instantiateItem(ViewGroup container,final int position) {

            View view = LayoutInflater.from(PlayFunActivity.this).inflate(R.layout.item_cover,null);
            ImageView imageView = (ImageView) view.findViewById(R.id.image_cover);
            TextView go_task=(TextView) view.findViewById(R.id.go_task);
            go_task.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ToastUtils.showShortToast(PlayFunActivity.this,"position"+position+"");
                }
            });

            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
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
}

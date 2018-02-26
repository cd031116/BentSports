package com.cn.bent.sports;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import com.cn.bent.sports.base.BaseActivity;
import com.cn.bent.sports.base.BaseConfig;
import com.cn.bent.sports.bean.LoginBase;
import com.cn.bent.sports.bean.LookRankEvent;
import com.cn.bent.sports.database.TaskCationBean;
import com.cn.bent.sports.database.TaskCationManager;
import com.cn.bent.sports.utils.Constants;
import com.cn.bent.sports.utils.SaveObjectUtils;
import com.cn.bent.sports.view.activity.ChooseLuxianActivity;
import com.cn.bent.sports.view.fragment.DoTaskFragment;
import com.cn.bent.sports.view.fragment.IsMeFragment;
import com.cn.bent.sports.view.fragment.JinMaoFragment;
import com.cn.bent.sports.view.fragment.MainTabFragment;
import com.cn.bent.sports.view.fragment.PlayFragment;
import com.cn.bent.sports.view.poupwindow.MainPoupWindow;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;

public class MainActivity extends BaseActivity {
    @Bind({R.id.iamage1, R.id.iamage2, R.id.image_me})
    List<ImageButton> mTabs;
    @Bind({R.id.text1, R.id.text_2, R.id.is_me})
    List<TextView> mText;
    int selected = 1;
    FragmentManager mFragmentMan;
    private MainPoupWindow mopupWindow;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mFragmentMan = getSupportFragmentManager();
        mopupWindow = new MainPoupWindow(MainActivity.this,null);
        mopupWindow.showAtLocation(MainActivity.this.findViewById(R.id.main_top),
                Gravity.CENTER | Gravity.CENTER_HORIZONTAL, 0, 0);
        mopupWindow.setClippingEnabled(false);
    }


    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    public void initView() {
        super.initView();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        switchContent(1);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(LookRankEvent event) {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                switchContent(0);
            }
        }, 600);
    }

    @Override
    public void initData() {
        super.initData();
//        setdata();
        setdata();
    }


    // 3. 先进第二个或第三个的子模块，再返回首页
    private String lastFragmentTag = null;

    private void changeFrament(String tag, int index) {
        BaseConfig bg = BaseConfig.getInstance(MainActivity.this);
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
                        mCurrentFragment = MainTabFragment.newInstance(0);
                        break;
                    case 1:
                      int ab=  bg.getIntValue(Constants.LU_XIAN, -1);
                        mCurrentFragment = PlayFragment.newInstance();//子Fragment实例
//                        if(ab==1){
//                        }else {
//                            mCurrentFragment = PlayFragment.newInstance();//子Fragment实例
//                        }
                        break;
                    case 2:
                        mCurrentFragment = IsMeFragment.newInstance();
                        ;//子Fragment实例
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

    @OnClick({R.id.text1, R.id.iamage1,R.id.line1,R.id.line2,R.id.iamage2, R.id.image_me, R.id.is_me})
    void onclick(View v) {
        switch (v.getId()) {
            case R.id.line1:
            case R.id.iamage1:
            case R.id.text1:
                if (selected == 0) {
                    break;
                }
                selected = 0;
                switchContent(selected);
                break;
            case R.id.iamage2:
                if (selected == 1) {
                    break;
                }
                selected = 1;
                switchContent(selected);
                break;
            case R.id.line2:
            case R.id.image_me:
            case R.id.is_me:
                if (selected == 2) {
                    break;
                }
                selected = 2;
                switchContent(selected);
                break;
        }
    }

    void switchContent(int select) {
        initFoot();
        if (select == 0) {
            mTabs.get(0).setSelected(true);
            mText.get(0).setTextColor(Color.parseColor("#e11818"));
            changeFrament("aFragment", 0);
        } else if (select == 1) {
            mTabs.get(1).setSelected(true);
            mText.get(1).setTextColor(Color.parseColor("#e11818"));
            changeFrament("bFragment", 1);
        } else if (select == 2) {
            mTabs.get(2).setSelected(true);
            mText.get(2).setTextColor(Color.parseColor("#e11818"));
            changeFrament("cFragment", 2);
        }
    }

    private void initFoot() {
        mTabs.get(0).setSelected(false);
        mTabs.get(1).setSelected(false);
        mTabs.get(2).setSelected(false);
        mText.get(0).setTextColor(Color.parseColor("#333333"));
        mText.get(1).setTextColor(Color.parseColor("#333333"));
        mText.get(2).setTextColor(Color.parseColor("#333333"));
    }


    private void setdata() {
        if (TaskCationManager.getSize() <= 0) {
            LoginBase user = SaveObjectUtils.getInstance(MainActivity.this).getObject(Constants.USER_INFO, null);
            List<TaskCationBean> nList = new ArrayList<>();
            nList.add(new TaskCationBean(1, user.getMember_id(), "1", "113.087645", "28.012992", "歌舞广场", false, false));
            nList.add(new TaskCationBean(2, user.getMember_id(), "1", "113.087843", "28.007034", "丛林穿越 ", false, false));
            nList.add(new TaskCationBean(3, user.getMember_id(), "1", "113.085139", "28.003593", "沙滩", false, false));
            nList.add(new TaskCationBean(4, user.getMember_id(), "1", "113.089565", "28.010653", "财神庙", false, false));
            nList.add(new TaskCationBean(5, user.getMember_id(), "1", "113.089232", "28.007276", "月亮岛", false, false));
            nList.add(new TaskCationBean(6, user.getMember_id(), "1", "113.086443", "28.005701", "竹林", false, false));
            TaskCationManager.insert(nList);
        }
    }
    private void setJinmaodata() {
        if (TaskCationManager.getSize() <= 0) {
            LoginBase user = SaveObjectUtils.getInstance(MainActivity.this).getObject(Constants.USER_INFO, null);
            List<TaskCationBean> nList = new ArrayList<>();
//            nList.add(new TaskCationBean(1, user.getMember_id(), "1", "113.095731", "28.016436", "金茂入口", false, false));
//            nList.add(new TaskCationBean(2, user.getMember_id(), "1", "113.094396", "28.015356", "大门", false, false));
//            nList.add(new TaskCationBean(3, user.getMember_id(), "1", "113.095013", "28.015432", "玻璃门", false, false));
//            nList.add(new TaskCationBean(4, user.getMember_id(), "1", "113.096193", "28.015891", "旗杆", false, false));
//            nList.add(new TaskCationBean(6, user.getMember_id(), "1", "113.096686", "28.015669", "三岔路口", false, false));
            nList.add(new TaskCationBean(1, user.getMember_id(), "1", "112.984188", "28.110286", "金茂入口", false, false));
            nList.add(new TaskCationBean(2, user.getMember_id(), "1", "112.989424", "28.111137", "大门", false, false));
            nList.add(new TaskCationBean(3, user.getMember_id(), "1", "112.991677", "28.111648", "玻璃门", false, false));
            nList.add(new TaskCationBean(4, user.getMember_id(), "1", "112.990733", "28.114506", "旗杆", false, false));
            TaskCationManager.insert(nList);
        }
    }
}

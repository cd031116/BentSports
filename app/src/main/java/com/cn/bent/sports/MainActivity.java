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
import com.cn.bent.sports.view.fragment.PlayMainFragment;
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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mFragmentMan = getSupportFragmentManager();
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
                        mCurrentFragment = PlayMainFragment.newInstance();//子Fragment实例
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


}

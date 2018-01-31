package com.cn.bent.sports;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.cn.bent.sports.base.BaseActivity;
import com.cn.bent.sports.bean.LoginBase;
import com.cn.bent.sports.database.TaskCationBean;
import com.cn.bent.sports.database.TaskCationManager;
import com.cn.bent.sports.utils.Constants;
import com.cn.bent.sports.utils.SaveObjectUtils;
import com.cn.bent.sports.view.activity.LoginActivity;
import com.cn.bent.sports.view.fragment.DoTaskFragment;
import com.cn.bent.sports.view.fragment.IsMeFragment;
import com.cn.bent.sports.view.fragment.MainTabFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;

public class MainActivity extends BaseActivity {
    @Bind({R.id.iamage1, R.id.iamage2, R.id.image_me})
    List<ImageButton> mTabs;
    @Bind({R.id.text1, R.id.text_2, R.id.is_me})
    List<TextView> mText;
    int selected = 0;
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
    }

    @Override
    public void initData() {
        super.initData();
        switchContent(1);
        setdata();
    }


    // 3. 先进第二个或第三个的子模块，再返回首页
    private String lastFragmentTag = null;

    private void changeFrament(String tag, int index) {
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
                        mCurrentFragment = DoTaskFragment.newInstance();//子Fragment实例
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

    @OnClick({R.id.text1, R.id.iamage1, R.id.iamage2, R.id.image_me, R.id.is_me})
    void onclick(View v) {
        switch (v.getId()) {
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
        Log.i("tttt","setdata="+TaskCationManager.getSize() );
        if (TaskCationManager.getSize() <=0) {
            Log.i("tttt","getSize=");
            LoginBase user = SaveObjectUtils.getInstance(MainActivity.this).getObject(Constants.USER_INFO, null);
            List<TaskCationBean> nList = new ArrayList<>();
//            nList.add(new TaskCationBean(1, user.getMember_id(), "", "113.087689", "28.01294", "歌舞广场", false, false));
//            nList.add(new TaskCationBean(2, user.getMember_id(), "", "113.089341", "28.010676", "财神庙", false, false));
//            nList.add(new TaskCationBean(3, user.getMember_id(), "", "113.089148", "28.007219", "月亮岛", false, false));
//            nList.add(new TaskCationBean(4, user.getMember_id(), "", "113.088107", "28.00558", "竹林", false, false));
//            nList.add(new TaskCationBean(5, user.getMember_id(), "", "113.08631", "28.010676", "财神庙", false, false));
//            nList.add(new TaskCationBean(6, user.getMember_id(), "", "113.085055", "28.003998", "沙滩", false, false));
            nList.add(new TaskCationBean(1, "123", "", "112.983733", "28.117083", "歌舞广场", false, false));
            nList.add(new TaskCationBean(2,"123", "", "112.992531", "28.119808", "财神庙", false, false));
            nList.add(new TaskCationBean(3, "123", "", "112.996222", "28.115455", "月亮岛", false, false));
            TaskCationManager.insert(nList);
            Log.i("tttt","insert=");
        }
    }


}

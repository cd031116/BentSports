package com.cn.bent.sports;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;

import com.cn.bent.sports.base.BaseActivity;
import com.cn.bent.sports.base.BaseConfig;
import com.cn.bent.sports.utils.Constants;
import com.cn.bent.sports.view.fragment.IsMeFragment;
import com.cn.bent.sports.view.fragment.MainTabFragment;
import com.cn.bent.sports.view.fragment.PlayMainFragment;
import com.cn.bent.sports.view.poupwindow.MainPoupWindow;

public class MainActivity extends BaseActivity {
    int selected = 1;
    FragmentManager mFragmentMan;
    private MainPoupWindow ropupWindow;
//e11818
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
    public void onResume() {
        super.onResume();
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




    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (event.getAction() == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_BACK) {
            String index = BaseConfig.getInstance(MainActivity.this).getStringValue(Constants.IS_SHOWS, "1");
            Log.i("gggg", "index=" + index);
            if (index.endsWith("1")) {
                return false;
            } else {
                MainActivity.this.finish();
            }
        }
        return super.onKeyDown(keyCode, event);
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
    }


}

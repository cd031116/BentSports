package com.cn.bent.sports;

import android.app.Dialog;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.ashokvarma.bottomnavigation.BottomNavigationBar;
import com.ashokvarma.bottomnavigation.BottomNavigationItem;
import com.cn.bent.sports.api.BaseApi;
import com.cn.bent.sports.base.BaseActivity;
import com.cn.bent.sports.base.BaseConfig;
import com.cn.bent.sports.utils.Constants;
import com.cn.bent.sports.view.activity.youle.play.TeamMemberActivity;
import com.cn.bent.sports.view.fragment.CardFragment;
import com.cn.bent.sports.view.fragment.IsMeFragment;
import com.cn.bent.sports.view.fragment.RecommendFragment;
import com.cn.bent.sports.view.fragment.ShoppingFragment;
import com.cn.bent.sports.view.poupwindow.LineListPoupWindow;
import com.cn.bent.sports.view.poupwindow.MainPoupWindow;
import com.cn.bent.sports.widget.ExxitDialog;
import com.vondear.rxtools.view.RxToast;
import com.zhl.network.RxObserver;
import com.zhl.network.RxSchedulers;
import com.zhl.network.huiqu.JavaRxFunction;

import java.util.ArrayList;

import butterknife.Bind;

public class MainActivity extends BaseActivity implements BottomNavigationBar.OnTabSelectedListener {
    int selected = 1;
    FragmentManager mFragmentMan;
    private MainPoupWindow ropupWindow;
    //e11818
    @Bind(R.id.navigationBar)
    BottomNavigationBar navigationBar;
    private LineListPoupWindow mopupWindow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getGameDetail();
        BaseConfig.getInstance(MainActivity.this).setStringValue(Constants.SOKET_PATH,"aiws.huiqulx.com/my-websocket");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }

        mFragmentMan = getSupportFragmentManager();
    }


    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    public void initView() {
        super.initView();
        navigationBar.setMode(BottomNavigationBar.MODE_FIXED);
        navigationBar.setBackgroundStyle(BottomNavigationBar.BACKGROUND_STYLE_STATIC);
        navigationBar.addItem(new BottomNavigationItem(R.drawable.jinngx_w, "推荐").setActiveColor("#e11818").setInActiveColor("#777777"))
                .addItem(new BottomNavigationItem(R.drawable.shangd_w, "商店").setActiveColor("#e11818").setInActiveColor("#777777"))
                .addItem(new BottomNavigationItem(R.drawable.mingxp_w, "明信片").setActiveColor("#e11818").setInActiveColor("#777777"))
                .addItem(new BottomNavigationItem(R.drawable.wode_w, "我的").setActiveColor("#e11818").setInActiveColor("#777777"))
                .setFirstSelectedPosition(0)
                .initialise();
        navigationBar.setTabSelectedListener(this);
    //new BottomNavigationItem(R.drawable.wode1, "推荐").setInactiveIcon(ContextCompat.getDrawable(this, R.drawable.wode2))
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                switchContent(0);
            }
        },20);
    }

    @Override
    public void onResume() {
        super.onResume();

    }


    @Override
    public void initData() {
        super.initData();
    }

    /**
     * 设置默认的
     */
    private void setDefaultFragment() {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        transaction.replace(R.id.id_content, IsMeFragment.newInstance());
        transaction.commit();
    }

    private ArrayList<Fragment> getFragments() {
        ArrayList<Fragment> fragments = new ArrayList<>();
        fragments.add(IsMeFragment.newInstance());
        fragments.add(IsMeFragment.newInstance());
        fragments.add(IsMeFragment.newInstance());
        fragments.add(IsMeFragment.newInstance());
        fragments.add(IsMeFragment.newInstance());
        return fragments;
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
                        mCurrentFragment = RecommendFragment.newInstance();
                        break;
                    case 1:
                        mCurrentFragment = ShoppingFragment.newInstance();//子Fragment实例
                        break;
                    case 2:
                        mCurrentFragment = CardFragment.newInstance();
                        //子Fragment实例
                        break;
                    case 3:
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
    public void onDestroy() {
        super.onDestroy();
    }

    void switchContent(int select) {
        if (select == 0) {
            changeFrament("aFragment", 0);
        } else if (select == 1) {
            changeFrament("bFragment", 1);
        } else if (select == 2) {
            changeFrament("cFragment", 2);
        } else if (select == 3) {
            changeFrament("dFragment", 3);
        }
    }

    @Override
    public void onTabSelected(int position) {
        switchContent(position);
//        if (fragments != null) {
//            if (position < fragments.size()) {
//                FragmentManager fm = getSupportFragmentManager();
//                FragmentTransaction ft = fm.beginTransaction();
//                Fragment fragment = fragments.get(position);
//                if (fragment.isAdded()) {
//                    ft.replace(R.id.id_content, fragment);
//                } else {
//                    ft.add(R.id.id_content, fragment);
//                }
//                ft.commitAllowingStateLoss();
//            }
//        }
    }

    @Override
    public void onTabUnselected(int position) {
//        if (fragments != null) {
//            if (position < fragments.size()) {
//                FragmentManager fm = getSupportFragmentManager();
//                FragmentTransaction ft = fm.beginTransaction();
//                Fragment fragment = fragments.get(position);
//                ft.remove(fragment);
//                ft.commitAllowingStateLoss();
//            }
//        }
    }

    @Override
    public void onTabReselected(int position) {

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (event.getAction() == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_BACK) {
            ExitFunction();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    private void ExitFunction() {
        new ExxitDialog(MainActivity.this, R.style.dialog, "确定退出AI旅行?", new ExxitDialog.OnCloseListener() {
            @Override
            public void onClick(Dialog dialog, boolean confirm) {
                if (confirm) {
                    dialog.dismiss();
                    MainActivity.this.finish();
                } else {
                    dialog.dismiss();
                }
            }
        }).show();
    }

    //获取websocket连接
    private void getGameDetail() {
        BaseApi.getJavaLoginDefaultService(MainActivity.this).getWebSocket()
                .map(new JavaRxFunction<String>())
                .compose(RxSchedulers.<String>io_main())
                .subscribe(new RxObserver<String>(MainActivity.this, TAG, 1, false) {
                    @Override
                    public void onSuccess(int whichRequest, String info) {
                        dismissAlert();

                        Log.i("tttt","info="+info);
                    }
                    @Override
                    public void onError(int whichRequest, Throwable e) {
                        dismissAlert();
                        RxToast.error(e.getMessage());
                    }
                });
    }

}

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
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.cn.bent.sports.base.BaseActivity;
import com.cn.bent.sports.base.BaseConfig;
import com.cn.bent.sports.view.fragment.CardFragment;
import com.cn.bent.sports.view.fragment.IsMeFragment;
import com.cn.bent.sports.view.fragment.RecommendFragment;
import com.cn.bent.sports.view.fragment.ShoppingFragment;
import com.cn.bent.sports.view.poupwindow.LineListPoupWindow;
import com.cn.bent.sports.view.poupwindow.MainPoupWindow;
import com.cn.bent.sports.widget.ExxitDialog;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.OnClick;

public class MainActivity extends BaseActivity {
    int selected = 1;
    FragmentManager mFragmentMan;
    private MainPoupWindow ropupWindow;
    @Bind(R.id.text_one)
    TextView text_one;
    @Bind(R.id.text_four)
    TextView text_four;
    @Bind(R.id.image_one)
    ImageView image_one;
    @Bind(R.id.image_four)
    ImageView image_four;

    private int select=1;
    private LineListPoupWindow mopupWindow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mFragmentMan = getSupportFragmentManager();
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
    }


    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    public void initView() {
        super.initView();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                switchContent(0);
            }
        }, 20);
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
        text_one.setTextColor(Color.parseColor("#999999"));
        text_four.setTextColor(Color.parseColor("#999999"));
        image_one.setSelected(false);
        image_four.setSelected(false);
        if (select == 0) {
            image_one.setSelected(true);
            text_one.setTextColor(Color.parseColor("#fd7e6f"));
            changeFrament("aFragment", 0);
        } else if (select == 1) {
            changeFrament("bFragment", 1);
        } else if (select == 2) {
            changeFrament("cFragment", 2);
        } else if (select == 3) {
            image_four.setSelected(true);
            text_four.setTextColor(Color.parseColor("#fd7e6f"));
            changeFrament("dFragment", 3);
        }
    }

    @OnClick({R.id.image_one, R.id.text_one, R.id.image_four, R.id.text_four,})
    void onclik(View v) {
        switch (v.getId()) {
            case R.id.image_one:
            case R.id.text_one:
                if(select==1){
                    break;
                }
                select=0;
                switchContent(select);
                break;
            case R.id.image_four:
            case R.id.text_four:
                if(select==3){
                    break;
                }
                select=3;
                switchContent(select);
                break;
        }
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


}

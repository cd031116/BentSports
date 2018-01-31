package com.cn.bent.sports.view.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;

import com.cn.bent.sports.MainActivity;
import com.cn.bent.sports.R;
import com.cn.bent.sports.base.BaseActivity;
import com.cn.bent.sports.bean.LoginBase;
import com.cn.bent.sports.utils.Constants;
import com.cn.bent.sports.utils.SaveObjectUtils;

public class StartActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    @Override
    protected int getLayoutId(){
        return R.layout.activity_start;
    }

    @Override
    public void initView(){
        AlphaAnimation aa = new AlphaAnimation(0.5f, 1.0f);
        aa.setDuration(500);
        contentView.startAnimation(aa);
        aa.setAnimationListener(new Animation.AnimationListener(){
            @Override
            public void onAnimationEnd(Animation arg0){
                redirectTo();
            }

            @Override
            public void onAnimationRepeat(Animation animation){

            }
            @Override
            public void onAnimationStart(Animation animation){

            }
        });
    }

    @Override
    public void initData()  {
        super.initData();
    }

    private void redirectTo() {
        LoginBase user= SaveObjectUtils.getInstance(StartActivity.this).getObject(Constants.USER_INFO, null);
//        if(user==null){
//            startActivity(new Intent(StartActivity.this,LoginActivity.class));
//            StartActivity.this.finish();
//        }else {
//            startActivity(new Intent(StartActivity.this,MainActivity.class));
//            StartActivity.this.finish();
//        }
        startActivity(new Intent(StartActivity.this,MainActivity.class));
        StartActivity.this.finish();
    }
}

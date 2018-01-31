package com.cn.bent.sports.view.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;

import com.cn.bent.sports.MainActivity;
import com.cn.bent.sports.R;
import com.cn.bent.sports.base.BaseActivity;
import com.cn.bent.sports.base.SdcardPermissionAction;
import com.cn.bent.sports.bean.LoginBase;
import com.cn.bent.sports.utils.Constants;
import com.cn.bent.sports.utils.DataUtils;
import com.cn.bent.sports.utils.SaveObjectUtils;

import org.aisen.android.support.action.IAction;

import java.io.InputStream;

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
        if (this instanceof org.aisen.android.ui.activity.basic.BaseActivity) {
            org.aisen.android.ui.activity.basic.BaseActivity aisenBaseActivity =
                    (org.aisen.android.ui.activity.basic.BaseActivity) this;
            new IAction(aisenBaseActivity, new SdcardPermissionAction(aisenBaseActivity,
                    null)) {
                @Override
                public void doAction() {
                    MAsyncTask asyncTask = new MAsyncTask();
                    asyncTask.execute();//开始执行
                }
            }.run();
        }



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


    class  MAsyncTask extends AsyncTask<Void, Integer, String>{


        @Override
        protected String doInBackground(Void... params) {
            DataUtils.copyAssetsToDst(StartActivity.this,"style.data","bent/sport.data");
            redirectTo();
            return null;
        }

    }
}

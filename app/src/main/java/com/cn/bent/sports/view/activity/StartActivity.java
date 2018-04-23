package com.cn.bent.sports.view.activity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.cn.bent.sports.MainActivity;
import com.cn.bent.sports.R;
import com.cn.bent.sports.api.BaseApi;
import com.cn.bent.sports.base.BaseActivity;
import com.cn.bent.sports.base.BaseConfig;
import com.cn.bent.sports.base.MyApplication;
import com.cn.bent.sports.base.SdcardPermissionAction;
import com.cn.bent.sports.base.SensorsPermission;
import com.cn.bent.sports.bean.LoginResult;
import com.cn.bent.sports.utils.Constants;
import com.cn.bent.sports.utils.DataUtils;
import com.cn.bent.sports.utils.ImageUtils;
import com.cn.bent.sports.utils.SaveObjectUtils;
import com.vondear.rxtools.view.RxToast;
import com.zhl.network.RxObserver;
import com.zhl.network.RxSchedulers;
import com.zhl.network.huiqu.HuiquRxTBFunction;

import org.aisen.android.support.action.IAction;

import butterknife.Bind;


public class StartActivity extends BaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_start;
    }

    @Override
    public void initView() {
        AlphaAnimation aa = new AlphaAnimation(0.5f, 1.0f);
        aa.setDuration(500);
        contentView.startAnimation(aa);
        aa.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationEnd(Animation arg0) {
                redirectTo();

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }

            @Override
            public void onAnimationStart(Animation animation) {

            }
        });
    }

    @Override
    public void initData() {
        super.initData();
    }

    private void redirectTo() {
        BaseConfig bg = BaseConfig.getInstance(StartActivity.this);
        final int isFirst = bg.getIntValue(Constants.IS_FIRST, 0);
        LoginResult user= SaveObjectUtils.getInstance(StartActivity.this).getObject(Constants.USER_INFO, null);
        if (isFirst == 1) {
            if(user==null){
                startActivity(new Intent(StartActivity.this,LoginActivity.class));
            }else {
                startActivity(new Intent(StartActivity.this,MainActivity.class));
            }


            finish();
        } else {
            if (this instanceof org.aisen.android.ui.activity.basic.BaseActivity) {
                org.aisen.android.ui.activity.basic.BaseActivity aisenBaseActivity =
                        (org.aisen.android.ui.activity.basic.BaseActivity) this;
                new IAction(aisenBaseActivity, new SdcardPermissionAction(aisenBaseActivity,
                        null)) {
                    @Override
                    public void doAction() {
                        if (isFirst == 0) {
                            MAsyncTask asyncTask = new MAsyncTask();
                            asyncTask.execute();//开始执行
                        }
                    }
                }.run();
            }
        }
        if (this instanceof org.aisen.android.ui.activity.basic.BaseActivity) {
            org.aisen.android.ui.activity.basic.BaseActivity aisenBaseActivity =
                    (org.aisen.android.ui.activity.basic.BaseActivity) this;
            new IAction(aisenBaseActivity, new SensorsPermission(aisenBaseActivity,
                    null)) {
                @Override
                public void doAction() {

                }
            }.run();
        }




    }


    class MAsyncTask extends AsyncTask<Void, Integer, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(Void... params) {
            DataUtils.copyAssetsToDst(StartActivity.this, "style.data", "bent/sport.data");
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            BaseConfig bg = BaseConfig.getInstance(StartActivity.this);
            bg.setIntValue(Constants.IS_FIRST, 1);
            super.onPostExecute(s);
            startActivity(new Intent(StartActivity.this,LoginActivity.class));
            StartActivity.this.finish();
        }

    }


}

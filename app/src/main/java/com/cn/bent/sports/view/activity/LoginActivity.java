package com.cn.bent.sports.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.cn.bent.sports.MainActivity;
import com.cn.bent.sports.R;
import com.cn.bent.sports.api.BaseApi;
import com.cn.bent.sports.base.BaseActivity;
import com.cn.bent.sports.bean.LoginResult;
import com.cn.bent.sports.utils.Constants;
import com.cn.bent.sports.utils.SaveObjectUtils;
import com.cn.bent.sports.view.activity.youle.bean.UserInfo;
import com.vondear.rxtools.view.RxToast;
import com.zhl.network.RxObserver;
import com.zhl.network.RxSchedulers;
import com.zhl.network.huiqu.HuiquRxTBFunction;
import com.zhl.network.huiqu.JavaRxFunction;

import java.util.HashMap;

import butterknife.Bind;
import butterknife.OnClick;
import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.wechat.friends.Wechat;

public class LoginActivity extends BaseActivity implements Handler.Callback {
    @Bind(R.id.edit_photo)
    EditText edit_photo;
    @Bind(R.id.code_photo)
    EditText code_photo;
    @Bind(R.id.commit_btn)
    TextView commit_btn;
    @Bind(R.id.tcode)
    TextView t_code;
    TimerCount timerCount;
    boolean isPhone = false, isCode = false;
    private Handler handler;
    private static final int MSG_AUTH_CANCEL = 1;
    private static final int MSG_AUTH_COMPLETE = 3;
    private static final int MSG_AUTH_ERROR= 2;
    private boolean t_isCode=false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        handler=new Handler(Looper.getMainLooper(),this);
    }


    @Override
    protected int getLayoutId() {
        return R.layout.login_layout;
    }

    @Override
    public void initView() {
        super.initView();
        timerCount = new TimerCount(60000, 1000, t_code);
        commit_btn.setEnabled(false);
        edit_photo.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() == 11) {
                    t_code.setSelected(true);
                } else {
                    t_code.setSelected(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().length() > 0) {
                    isPhone = true;
                } else {
                    isPhone = false;
                }
                changeview();
            }
        });
        code_photo.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().length() > 0) {
                    isCode = true;
                } else {
                    isCode = false;
                }
                changeview();
            }
        });

    }


    @Override
    public void initData() {
        super.initData();

    }


    private void weiChatLogin( Platform platform) {
        BaseApi.getJavaLoginService(this).weichatLogin(3,"password", platform.getDb().get("unionid"),"1","ANDROID",platform.getDb().getUserName(),platform.getDb().getUserIcon())
                .map(new HuiquRxTBFunction<LoginResult>())
                .compose(RxSchedulers.<LoginResult>io_main())
                .subscribe(new RxObserver<LoginResult>(LoginActivity.this, "login", 1, false) {
                    @Override
                    public void onSuccess(int whichRequest, LoginResult info) {
                        dismissAlert();
                        SaveObjectUtils.getInstance(LoginActivity.this).setObject(Constants.USER_INFO, info);
                        getUserInfo();
                    }

                    @Override
                    public void onError(int whichRequest, Throwable e) {
                        dismissAlert();
                        RxToast.error(e.getMessage());
                    }
                });
    }

    private void loginCoe(String account, String code) {
        showAlert("正在登录...", true);
        BaseApi.getJavaLoginService(this).LogingCode(1,"password","ANDROID",account ,code)
                .map(new HuiquRxTBFunction<LoginResult>())
                .compose(RxSchedulers.<LoginResult>io_main())
                .subscribe(new RxObserver<LoginResult>(LoginActivity.this, "login", 1, false) {
                    @Override
                    public void onSuccess(int whichRequest, LoginResult info) {
                        SaveObjectUtils.getInstance(LoginActivity.this).setObject(Constants.USER_INFO, info);
                        Log.i("tttt","token="+info.getAccess_token());
                        dismissAlert();
                        getUserInfo();
//                        getdot();
                    }


                    @Override
                    public void onError(int whichRequest, Throwable e) {
                        dismissAlert();
                        RxToast.error(e.getMessage());
                    }
                });
    }
    private void loginCeshi(String account, String code) {
        showAlert("正在登录...", true);
        BaseApi.getJavaLoginService(this).LogingCode(-1,"password","ANDROID",account ,"123456")
                .map(new HuiquRxTBFunction<LoginResult>())
                .compose(RxSchedulers.<LoginResult>io_main())
                .subscribe(new RxObserver<LoginResult>(LoginActivity.this, "login", 1, false) {
                    @Override
                    public void onSuccess(int whichRequest, LoginResult info) {
                        SaveObjectUtils.getInstance(LoginActivity.this).setObject(Constants.USER_INFO, info);
                        Log.i("tttt","token="+info.getAccess_token());
                        dismissAlert();
                        getUserInfo();
//                        getdot();
                    }


                    @Override
                    public void onError(int whichRequest, Throwable e) {
                        dismissAlert();
                        RxToast.error(e.getMessage());
                    }
                });
    }



    private void getUserInfo() {
        showAlert("正在获取用户信息...", true);
        BaseApi.getJavaLoginDefaultService(LoginActivity.this)
                .getUserInfo()
                .map(new JavaRxFunction<UserInfo>())
                .compose(RxSchedulers.<UserInfo>io_main())
                .subscribe(new RxObserver<UserInfo>(LoginActivity.this, "getcode", 1, false) {
                    @Override
                    public void onSuccess(int whichRequest, UserInfo result) {
                        dismissAlert();
                        startActivity(new Intent(LoginActivity.this, MainActivity.class));
                        SaveObjectUtils.getInstance(LoginActivity.this).setObject(Constants.USER_BASE, result);
                        finish();
                    }

                    @Override
                    public void onError(int whichRequest, Throwable e) {
                        dismissAlert();
                        RxToast.error(e.getMessage());
                    }
                });

    }

    private void getcode(String account) {
        BaseApi.getJavaLoginService(LoginActivity.this).sendCode(account,1)
                .map(new JavaRxFunction<Boolean>())
                .compose(RxSchedulers.<Boolean>io_main())
                .subscribe(new RxObserver<Boolean>(LoginActivity.this, "getcode", 1, false) {
                    @Override
                    public void onSuccess(int whichRequest, Boolean result) {
                        timerCount.start();
                        t_isCode=true;
                    }

                    @Override
                    public void onError(int whichRequest, Throwable e) {
                        RxToast.error(e.getMessage());
                    }
                });

    }

    private void changeview() {
        if (isCode && isPhone) {
            commit_btn.setSelected(true);
            commit_btn.setEnabled(true);
        } else {
            commit_btn.setSelected(false);
            commit_btn.setEnabled(false);
        }
    }

    @OnClick({R.id.tcode, R.id.commit_btn, R.id.wechat_sign})
    void conlick(View view) {
        switch (view.getId()) {
            case R.id.tcode:
                String sd = edit_photo.getText().toString();
                if (TextUtils.isEmpty(sd)) {
                    RxToast.warning("请输入手机号");
                    break;
                }
                getcode(sd);
                break;
            case R.id.commit_btn:
                String sdf = edit_photo.getText().toString();
                if (!TextUtils.isEmpty(sdf)&&sdf.equals("ceshi001")){
                    loginCeshi(edit_photo.getText().toString(), code_photo.getText().toString());
                }else if (t_isCode){
                    loginCoe(edit_photo.getText().toString(), code_photo.getText().toString());
                }else {
                    RxToast.warning("请输入验证码");
                }
                break;
            case R.id.wechat_sign:
                Platform wechat = ShareSDK.getPlatform(Wechat.NAME);
                wechat.removeAccount(true);
                wechat.SSOSetting(false);
                wechat.setPlatformActionListener(paListener);
                wechat.authorize();
                wechat.showUser(null);
                showAlert("正在使用微信登录...", true);
                break;
        }
    }


    PlatformActionListener paListener=new PlatformActionListener() {
        @Override
        public void onComplete(Platform platform, int action, HashMap<String, Object> hashMap) {
            if (action == Platform.ACTION_USER_INFOR) {
                Message msg = new Message();
                msg.what = MSG_AUTH_COMPLETE;
                msg.arg2 = action;
                msg.obj =  new Object[] {platform.getName(), hashMap};
                handler.sendMessage(msg);
                Log.i("tttt","onComplete="+platform.getName());
            }
        }

        @Override
        public void onError(Platform platform, int action, Throwable throwable) {
            if (action == Platform.ACTION_USER_INFOR) {
                Message msg = new Message();
                msg.what = MSG_AUTH_ERROR;
                msg.arg2 = action;
                msg.obj =throwable;
                handler.sendMessage(msg);
            }
        }

        @Override
        public void onCancel(Platform platform, int action) {
            if (action == Platform.ACTION_USER_INFOR) {
                Message msg = new Message();
                msg.what = MSG_AUTH_CANCEL;
                msg.arg2 = action;
                msg.obj = platform;
                handler.sendMessage(msg);
            }
        }
    };

    @Override
    public boolean handleMessage(Message msg) {
        /**处理操作结果*/
        switch(msg.what) {
            case MSG_AUTH_CANCEL: {
                // 取消
                RxToast.normal("取消");
                dismissAlert();
            }
            break;
            case MSG_AUTH_ERROR: {
                // 失败
                dismissAlert();
                Throwable t = (Throwable) msg.obj;
                String text = "caught error: " + t.getMessage();
                RxToast.info(text);
                t.printStackTrace();
            } break;
            case MSG_AUTH_COMPLETE: {
                // 成功
                Object[] objs = (Object[]) msg.obj;
                String plat = (String) objs[0];
                Platform platform = ShareSDK.getPlatform(plat);
                weiChatLogin(platform);
            }
            break;
        }
        return false;
    }


    public class TimerCount extends CountDownTimer {
        private TextView bnt;

        public TimerCount(long millisInFuture, long countDownInterval,
                          TextView bnt) {
            super(millisInFuture, countDownInterval);
            this.bnt = bnt;
        }

        @Override
        public void onFinish() {
            bnt.setClickable(true);
            t_code.setText("重获验证码");
            bnt.setEnabled(true);
        }

        @Override
        public void onTick(long arg0) {
            // if(bnt!=null){
            bnt.setClickable(false);
            t_code.setText("验证码" + arg0 / 1000 + "S");
            bnt.setEnabled(false);
            // }
        }
    }
}

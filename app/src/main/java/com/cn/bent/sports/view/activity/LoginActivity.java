package com.cn.bent.sports.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.cn.bent.sports.MainActivity;
import com.cn.bent.sports.R;
import com.cn.bent.sports.api.BaseApi;
import com.cn.bent.sports.base.BaseActivity;
import com.cn.bent.sports.base.BaseConfig;
import com.cn.bent.sports.bean.InfoEvent;
import com.cn.bent.sports.bean.LoginBase;
import com.cn.bent.sports.utils.Constants;
import com.cn.bent.sports.utils.SaveObjectUtils;
import com.cn.bent.sports.utils.ToastUtils;
import com.zhl.network.RxObserver;
import com.zhl.network.RxSchedulers;
import com.zhl.network.huiqu.HuiquRxFunction;
import com.zhl.network.huiqu.HuiquRxTBFunction;
import com.zhl.network.huiqu.HuiquTBResult;

import org.greenrobot.eventbus.EventBus;

import butterknife.Bind;
import butterknife.OnClick;

public class LoginActivity extends BaseActivity {
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

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

    private void login(String account, String code) {
        showAlert("正在登录...", true);
        BaseApi.getDefaultService(this).Loging(account, code)
                .map(new HuiquRxFunction<LoginBase>())
                .compose(RxSchedulers.<LoginBase>io_main())
                .subscribe(new RxObserver<LoginBase>(LoginActivity.this, "login", 1, false) {
                    @Override
                    public void onSuccess(int whichRequest, LoginBase info) {
                        SaveObjectUtils.getInstance(LoginActivity.this).setObject(Constants.USER_INFO, info);
                        dismissAlert();

                        BaseConfig bg = BaseConfig.getInstance(LoginActivity.this);
//                        int luxian = bg.getIntValue(Constants.LU_XIAN, -1);

                        startActivity(new Intent(LoginActivity.this, MainActivity.class));
//                        if (luxian > 0) {
//                        } else {
//                            startActivity(new Intent(LoginActivity.this, ChooseLuxianActivity.class));
//                        }
                        finish();
                    }

                    @Override
                    public void onError(int whichRequest, Throwable e) {
                        dismissAlert();
                        ToastUtils.showLongToast(LoginActivity.this, e.getMessage());
                    }
                });
    }


    private void getcode() {
        BaseApi.getDefaultService(LoginActivity.this)
                .getcode(edit_photo.getText().toString())
                .map(new HuiquRxTBFunction<HuiquTBResult>())
                .compose(RxSchedulers.<HuiquTBResult>io_main())
                .subscribe(new RxObserver<HuiquTBResult>(LoginActivity.this, "getcode", 1, false) {
                    @Override
                    public void onSuccess(int whichRequest, HuiquTBResult result) {
                        if (!"1".equals(result.getCode())) {
                            ToastUtils.showShortToast(LoginActivity.this, "" + result.getMsg());
                        } else {

                        }
                        timerCount.start();
                    }

                    @Override
                    public void onError(int whichRequest, Throwable e) {
                        ToastUtils.showShortToast(LoginActivity.this, "" + e.getMessage());
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

    @OnClick({R.id.tcode, R.id.commit_btn})
    void conlick(View view) {
        switch (view.getId()) {
            case R.id.tcode:
                String sd = edit_photo.getText().toString();
                if (TextUtils.isEmpty(sd)) {
                    ToastUtils.showShortToast(LoginActivity.this, "请输入手机号");
                    break;
                }
                getcode();
                break;
            case R.id.commit_btn:
                login(edit_photo.getText().toString(), code_photo.getText().toString());
                break;
        }
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

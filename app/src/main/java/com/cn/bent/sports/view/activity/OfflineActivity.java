package com.cn.bent.sports.view.activity;

import android.content.Intent;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.cn.bent.sports.R;
import com.cn.bent.sports.api.BaseApi;
import com.cn.bent.sports.base.BaseActivity;
import com.cn.bent.sports.base.BaseConfig;
import com.cn.bent.sports.bean.AddScoreEntity;
import com.cn.bent.sports.bean.InfoEvent;
import com.cn.bent.sports.bean.LoginBase;
import com.cn.bent.sports.bean.ReFreshEvent;
import com.cn.bent.sports.utils.Constants;
import com.cn.bent.sports.utils.DataUtils;
import com.cn.bent.sports.utils.SaveObjectUtils;
import com.cn.bent.sports.utils.ToastUtils;
import com.zhl.network.RxObserver;
import com.zhl.network.RxSchedulers;
import com.zhl.network.huiqu.HuiquRxTBFunction;

import org.greenrobot.eventbus.EventBus;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by dawn on 2018/2/26.
 */

public class OfflineActivity extends BaseActivity {

    @Bind(R.id.commit)
    TextView commit;
    @Bind(R.id.commit_edit)
    TextView commit_edit;
    @Bind(R.id.cut_down)
    TextView timer;
    private Handler handler2;
    private int MAX_REQUEST = 2;
    private int isRequestNum = 1;
    private LoginBase user;
    private String gameId;

    @Override
    protected int getLayoutId() {
        return R.layout.down_game_layout;
    }

    @Override
    public void initView() {
        super.initView();
        user = (LoginBase) SaveObjectUtils.getInstance(this).getObject(Constants.USER_INFO, null);
        handler2 = new Handler();
        gameId = getIntent().getStringExtra("gameId");
        setTimes();
    }

    @Override
    public void initData() {
        super.initData();
    }

    @OnClick({R.id.top_left, R.id.top_image, R.id.commit})
    void onClick(View view) {
        switch (view.getId()) {
            case R.id.top_left:
            case R.id.top_image:
                finish();
                break;
            case R.id.commit:
                if ("123".equals(commit_edit.getText().toString().trim())) {
                    commitScore();
                } else {
                    ToastUtils.showShortToast(this,"游戏失败，再玩一次");
                }
                break;
        }
    }

    private void setTimes() {
        handler2.postDelayed(runnable2, 1000);
    }

    Runnable runnable2 = new Runnable() {
        long longValue = BaseConfig.getInstance(OfflineActivity.this).getLongValue(Constants.IS_TIME, 0);

        @Override
        public void run() {
            handler2.postDelayed(this, 1000);
            Log.i("tttt", "currentTimeMillis=" + (System.currentTimeMillis() - longValue) / 1000);
            if (((System.currentTimeMillis() - longValue) / 1000) >= 2 * 60 * 60) {
                handler2.removeCallbacks(runnable2);
                timer.setText("02.00.00");
            } else {
                timer.setText(DataUtils.getDateToTime(System.currentTimeMillis() - longValue));
            }

        }
    };

    private void commitScore() {
        BaseApi.getDefaultService(this).addScore(user.getMember_id(), 100, Integer.parseInt(gameId))
                .map(new HuiquRxTBFunction<AddScoreEntity>())
                .compose(RxSchedulers.<AddScoreEntity>io_main())
                .subscribe(new RxObserver<AddScoreEntity>(this, "addScore", 1, false) {
                    @Override
                    public void onSuccess(int whichRequest, AddScoreEntity addScoreEntity) {
                        if (addScoreEntity.getBody().getAddStatus() == 1) {
                            dismissAlert();
                            LoginBase user = (LoginBase) SaveObjectUtils.getInstance(OfflineActivity.this).getObject(Constants.USER_INFO, null);
                            setScore(user);
                            EventBus.getDefault().post(new InfoEvent());
                            EventBus.getDefault().post(new ReFreshEvent());
                            startActivity(new Intent(OfflineActivity.this, LastActivity.class));
                        } else {
                            ToastUtils.showShortToast(OfflineActivity.this, addScoreEntity.getMsg());
                            dismissAlert();
                        }
                        isRequestNum = 1;
                    }

                    @Override
                    public void onError(int whichRequest, Throwable e) {
                        dismissAlert();
                        if (isRequestNum < MAX_REQUEST) {
                            isRequestNum++;
                            ToastUtils.showShortToast(OfflineActivity.this, "积分上传失败,正在重新上传积分");
                            commitScore();
                        } else {
                            ToastUtils.showShortToast(OfflineActivity.this, "网络异常，积分上传失败，请重新玩此游戏");
                            startActivity(new Intent(OfflineActivity.this, LastActivity.class));
                        }
                    }
                });
    }

    private void setScore(LoginBase user) {
        if (user.getScore() != null)
            user.setScore(String.valueOf(Integer.parseInt(user.getScore()) + 100));
        else
            user.setScore(String.valueOf(100));
        SaveObjectUtils.getInstance(OfflineActivity.this).setObject(Constants.USER_INFO, user);
    }
}

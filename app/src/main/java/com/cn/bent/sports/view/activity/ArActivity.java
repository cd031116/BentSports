package com.cn.bent.sports.view.activity;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.cn.bent.sports.R;
//import com.cn.bent.sports.ar.GLView;
import com.cn.bent.sports.api.BaseApi;
import com.cn.bent.sports.ar.GLView;
import com.cn.bent.sports.base.BaseActivity;
import com.cn.bent.sports.bean.AddScoreEntity;
import com.cn.bent.sports.bean.GameEntity;
import com.cn.bent.sports.bean.InfoEvent;
import com.cn.bent.sports.bean.LoginBase;
import com.cn.bent.sports.bean.ReFreshEvent;
import com.cn.bent.sports.bean.VideoEvent;
import com.cn.bent.sports.utils.Constants;
import com.cn.bent.sports.utils.SaveObjectUtils;
import com.cn.bent.sports.utils.ToastUtils;
import com.zhl.network.RxObserver;
import com.zhl.network.RxSchedulers;
import com.zhl.network.huiqu.HuiquRxTBFunction;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.HashMap;

import butterknife.Bind;
import butterknife.OnClick;
import cn.easyar.engine.EasyAR;
import cn.easyar.Engine;

/**
 * Created by dawn on 2018/2/26.
 */

public class ArActivity extends BaseActivity {
    private static String key = "uHF95ree4fY6nj6NrhAFZ3HwZvpQfexUpb0DQoy9vM0733bZ3kczl95yxtZ0sTNgTZE6idFXRx0Ej5UtJ0FEUGFUZLxdKpsH8UOg0J2spZUdLTOUlwtjimxW8yer0GReXJJgUvF1n8yiK7TkkX7hEDliysPlF4CYQaolaOsE4LufSDcBqPU83rPtbsFohb6FNX4iSA4b";
    private GLView glView;

    @Bind(R.id.go_ahead)
    TextView go_ahead;
    private LoginBase user;
    private String gameId;
    private int MAX_REQUEST = 2;
    private int isRequestNum = 1;

    @Override
    protected int getLayoutId() {
        return R.layout.ar_layout;
    }

    @Override
    public void initView() {
        super.initView();
        user = (LoginBase) SaveObjectUtils.getInstance(this).getObject(Constants.USER_INFO, null);
        gameId = getIntent().getStringExtra("gameId");
    }

    @Override
    public void initData() {
        super.initData();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON, WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        EventBus.getDefault().register(this);
        if (!EasyAR.initialize(this, key)) {
            Log.e("HelloAR", "Initialization Failed.");
        }

        glView = new GLView(this);

        requestCameraPermission(new PermissionCallback() {
            @Override
            public void onSuccess() {
                ((ViewGroup) findViewById(R.id.preview)).addView(glView, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            }

            @Override
            public void onFailure() {
            }
        });
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(VideoEvent event) {
        go_ahead.setVisibility(View.VISIBLE);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @OnClick({R.id.top_left, R.id.top_image,R.id.go_ahead})
    void onClick(View view) {
        switch (view.getId()) {
            case R.id.top_left:
            case R.id.top_image:
                finish();
                break;
            case R.id.go_ahead:
                GameEntity gameEntity = new GameEntity();
                gameEntity.setUid(user.getMember_id());
                gameEntity.setGameid(Integer.parseInt(gameId));
                gameEntity.setScord(30);
                commitScore(gameEntity);
                break;
        }
    }

    private interface PermissionCallback {
        void onSuccess();

        void onFailure();
    }

    private HashMap<Integer, PermissionCallback> permissionCallbacks = new HashMap<Integer, PermissionCallback>();
    private int permissionRequestCodeSerial = 0;

    @TargetApi(23)
    private void requestCameraPermission(PermissionCallback callback) {
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                int requestCode = permissionRequestCodeSerial;
                permissionRequestCodeSerial += 1;
                permissionCallbacks.put(requestCode, callback);
                requestPermissions(new String[]{Manifest.permission.CAMERA}, requestCode);
            } else {
                callback.onSuccess();
            }
        } else {
            callback.onSuccess();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (permissionCallbacks.containsKey(requestCode)) {
            PermissionCallback callback = permissionCallbacks.get(requestCode);
            permissionCallbacks.remove(requestCode);
            boolean executed = false;
            for (int result : grantResults) {
                if (result != PackageManager.PERMISSION_GRANTED) {
                    executed = true;
                    callback.onFailure();
                }
            }
            if (!executed) {
                callback.onSuccess();
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (glView != null) {
            glView.onResume();
        }
    }

    @Override
    public void onPause() {
        if (glView != null) {
            glView.onPause();
        }
        super.onPause();
    }

    private void commitScore(final GameEntity gameEntity) {
        BaseApi.getDefaultService(this).addScore(user.getMember_id(), 30, Integer.parseInt(gameId))
                .map(new HuiquRxTBFunction<AddScoreEntity>())
                .compose(RxSchedulers.<AddScoreEntity>io_main())
                .subscribe(new RxObserver<AddScoreEntity>(this, "addScore", 1, false) {
                    @Override
                    public void onSuccess(int whichRequest, AddScoreEntity addScoreEntity) {
                        if (addScoreEntity.getBody().getAddStatus() == 1) {
                            dismissAlert();
                            LoginBase user = (LoginBase) SaveObjectUtils.getInstance(ArActivity.this).getObject(Constants.USER_INFO, null);
                            setScore(user);
                            EventBus.getDefault().post(new InfoEvent());
                            EventBus.getDefault().post(new ReFreshEvent());
                            toContinue();
                        } else {
                            ToastUtils.showShortToast(ArActivity.this, addScoreEntity.getMsg());
                            dismissAlert();
                        }
                        isRequestNum = 1;
                    }

                    private void toContinue() {
                        Intent intent = new Intent(ArActivity.this, ContinueActivity.class);
                        intent.putExtra("game", gameEntity);
                        startActivity(intent);
                    }

                    @Override
                    public void onError(int whichRequest, Throwable e) {
                        dismissAlert();
                        if (isRequestNum < MAX_REQUEST) {
                            isRequestNum++;
                            ToastUtils.showShortToast(ArActivity.this, "积分上传失败,正在重新上传积分");
                            commitScore(gameEntity);
                        } else {
                            ToastUtils.showShortToast(ArActivity.this, "网络异常，积分上传失败，请重新玩此游戏");
                            startActivity(new Intent(ArActivity.this, LastActivity.class));
                        }
                    }
                });
    }

    private void setScore(LoginBase user) {
        if (user.getScore() != null)
            user.setScore(String.valueOf(Integer.parseInt(user.getScore()) + 30));
        else
            user.setScore(String.valueOf(30));
        SaveObjectUtils.getInstance(ArActivity.this).setObject(Constants.USER_INFO, user);
    }
}

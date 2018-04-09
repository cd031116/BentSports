package com.cn.bent.sports.view.activity;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Dialog;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.TextView;

import com.cn.bent.sports.R;
import com.cn.bent.sports.ar.GLView;
import com.cn.bent.sports.base.BaseActivity;
import com.cn.bent.sports.bean.GameEntity;
import com.cn.bent.sports.bean.VideoEvent;
import com.cn.bent.sports.widget.GameDialog;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.HashMap;

import butterknife.Bind;
import butterknife.OnClick;
import cn.easyar.engine.EasyAR;

//import com.cn.bent.sports.ar.GLView;

/**
 * Created by dawn on 2018/2/26.
 */

public class ArActivity extends BaseActivity {
    private static String key = "uHF95ree4fY6nj6NrhAFZ3HwZvpQfexUpb0DQoy9vM0733bZ3kczl95yxtZ0sTNgTZE6idFXRx0Ej5UtJ0FEUGFUZLxdKpsH8UOg0J2spZUdLTOUlwtjimxW8yer0GReXJJgUvF1n8yiK7TkkX7hEDliysPlF4CYQaolaOsE4LufSDcBqPU83rPtbsFohb6FNX4iSA4b";
    private GLView glView;

    @Bind(R.id.go_ahead)
    TextView go_ahead;
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
        gameId = getIntent().getStringExtra("gameId");
        showDialogMsg(getResources().getString(R.string.ar_tuo),R.drawable.artuo);
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

    private void showDialogMsg(String names,int imgResId) {
        new GameDialog(this, R.style.dialog, imgResId,new GameDialog.OnCloseListener() {
            @Override
            public void onClick(Dialog dialog, boolean confirm) {

                if (confirm) {

                } else {
                    finish();
                }
                dialog.dismiss();
            }
        }).setTitle(names).show();
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
                gameEntity.setGameid(Integer.parseInt(gameId));
                gameEntity.setScord(30);
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


}

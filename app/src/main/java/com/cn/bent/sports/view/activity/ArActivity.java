package com.cn.bent.sports.view.activity;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import com.cn.bent.sports.R;
import com.cn.bent.sports.ar.GLView;
import com.cn.bent.sports.base.BaseActivity;
import com.cn.bent.sports.utils.Constants;

import java.util.HashMap;

import butterknife.OnClick;
import cn.easyar.engine.EasyAR;

/**
 * Created by dawn on 2018/2/26.
 */

public class ArActivity extends BaseActivity {
    private static String key = "laK9zbjrdzdwDAwcuP3Coxg883vAVIdG3EHLyBIYotrxBC0YUfKIelQxlr5DaznEMC5IbhQEeEtleuZaIL3YzRZ70uxrheajIGNt2KFCsQlqQEUCXPNPeQk7hIsEqlRgcVnTYOEhO3t5T3AwbV0jUBixdnhenmCUBRhrYtdalyxVNV4JnVEfRJKuECPYoHSq2skVidn4";
    private GLView glView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.ar_layout;
    }

    @Override
    public void initView() {
        super.initView();
        //设置屏幕常亮
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON, WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

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


    @OnClick({R.id.top_left, R.id.top_image})
    void onClick(View view) {
        finish();
    }

    @Override
    public void initData() {
        super.initData();
    }

    private interface PermissionCallback {
        void onSuccess();
        void onFailure();
    }

    private HashMap<Integer, PermissionCallback> permissionCallbacks = new HashMap<Integer, PermissionCallback>();
    private int permissionRequestCodeSerial = 0;

    @TargetApi(23)
    public void requestCameraPermission(PermissionCallback callback) {
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
        if (glView != null) { glView.onResume(); }

    }

    @Override
    public void onPause()
    {
        if (glView != null) { glView.onPause(); }
        super.onPause();
    }

}

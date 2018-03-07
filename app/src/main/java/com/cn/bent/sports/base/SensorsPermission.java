package com.cn.bent.sports.base;

import android.Manifest;

import org.aisen.android.support.action.IAction;
import org.aisen.android.support.permissions.APermissionsAction;
import org.aisen.android.ui.activity.basic.*;

/**
 * Created by lyj on 2018/3/7 0007.
 * description
 */

public class SensorsPermission  extends APermissionsAction {

    public SensorsPermission(org.aisen.android.ui.activity.basic.BaseActivity context, IAction parent) {
        super(context, parent, context.getActivityHelper(), Manifest.permission.BODY_SENSORS);
    }

    @Override
    protected void onPermissionDenied(boolean alwaysDenied) {
        if (alwaysDenied) {
            ((org.aisen.android.ui.activity.basic.BaseActivity) getContext()).showMessage("传感器权限被禁用了，请去设置界面打开此权限");
        }
        else {
            ((org.aisen.android.ui.activity.basic.BaseActivity) getContext()).showMessage("取消传感器授权,视频将步数无法获取");
        }
    }
}

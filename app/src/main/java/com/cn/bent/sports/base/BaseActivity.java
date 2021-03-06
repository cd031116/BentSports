package com.cn.bent.sports.base;

import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.cn.bent.sports.R;
import com.cn.bent.sports.bean.LoginResult;
import com.cn.bent.sports.utils.Constants;
import com.cn.bent.sports.utils.SaveObjectUtils;
import com.cn.bent.sports.utils.SupportMultipleScreensUtil;
import com.cn.bent.sports.view.activity.PlayFunActivity;
import com.cn.bent.sports.widget.ShowMsgDialog;
import com.readystatesoftware.systembartint.SystemBarTintManager;
import com.vondear.rxtools.view.dialog.RxDialogLoading;
import com.zhl.network.RxManager;

import butterknife.ButterKnife;


/*
*
* @author lyj
* @describe Activity基类
* @data 2017/11/9
* */


public class BaseActivity extends org.aisen.android.ui.activity.basic.BaseActivity implements BaseViewInterface {
    protected View contentView;
    private TextView msg;
     private boolean isActive=true;
    InputMethodManager manager;
    private RxDialogLoading progressDialog;
    public String TAG;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MyApplication.instance.getActivityManager().pushActivity(this);
        manager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);

        if (getLayoutId()!= 0){

            // setContentView(getLayoutId());
            contentView = View.inflate(this, getLayoutId(), null);
            setContentView(contentView);
            View rootView=findViewById(android.R.id.content);
            SupportMultipleScreensUtil.init(getApplication());
            SupportMultipleScreensUtil.scale(rootView);
        }
        ButterKnife.bind(this);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT){
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
        TAG = getPackageName() + "." + getClass().getSimpleName();
        initView();
        initData();
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        MyApplication.instance.getActivityManager().popActivity(this);
        RxManager.getInstance().clear(TAG);
//        if (EventBus.getDefault().isRegistered(this)){
//            EventBus.getDefault().unregister(this);
//        }
    }

    protected int getLayoutId(){
        return 0;
    }


    @Override
    public void initView() {

    }

    @Override
    public void initData() {

    }
    @Override
    public void finish() {
        super.finish();
//		this.overridePendingTransition(R.anim.slide_left_in, R.anim.slide_right_out);
    }
    @Override
    public void startActivity(Intent intent){
        super.startActivity(intent);
//		this.overridePendingTransition(R.anim.slide_right_in, R.anim.slide_left_out);
    }
    @Override
    public void startActivityForResult(Intent intent, int requestCode) {
        super.startActivityForResult(intent, requestCode);
//		this.overridePendingTransition(R.anim.slide_right_in, R.anim.slide_left_out);
    }

    protected void showPopwindow(PopupWindow popWin) {
        // -----------------------
        // 设置背景颜色变暗
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.alpha = 0.7f;
        getWindow().setAttributes(lp);
        popWin.setOnDismissListener(new PopupWindow.OnDismissListener(){
            @Override
            public void onDismiss() {
                WindowManager.LayoutParams lp = getWindow().getAttributes();
                lp.alpha = 1f;
                getWindow().setAttributes(lp);
            }
        });
        // -----------------------
    }


    /**
    * 显示加载图标
    * @param txt
    */
    public void showAlert(String txt,final boolean isCancel){
        if(!"".equals(txt)&&txt!=null){
            if(progressDialog==null){
                progressDialog=new RxDialogLoading(this,isCancel);
            }
            progressDialog.setLoadingText(txt);
            progressDialog.show();
        }
    }
    /**
     * 关闭加载图标
     */
    public void dismissAlert(){
        if(progressDialog!=null&&progressDialog.isShowing()){
            progressDialog.dismiss();
        }
    }

    public static boolean isConnect(Context context) {
        // 获取手机所有连接管理对象（包括对wi-fi,net等连接的管理）
        try {
            ConnectivityManager connectivity = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            if (connectivity != null) {
                // 获取网络连接管理的对象
                NetworkInfo info = connectivity.getActiveNetworkInfo();
                if (info != null && info.isConnected()) {
                    // 判断当前网络是否已经连接
                    if (info.getState() == NetworkInfo.State.CONNECTED) {
                        return true;
                    }
                }
            }
        } catch (Exception e) {
            Log.v("mcn", e.toString());
        }
        return false;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event){
        // TODO Auto-generated method stub
        if(event.getAction() == MotionEvent.ACTION_DOWN){
            if(getCurrentFocus()!=null && getCurrentFocus().getWindowToken()!=null){
                manager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            }
        }
        return super.onTouchEvent(event);
    }

    protected boolean onHome() {
        return false;
    }

    protected boolean onBack() {
        return false;
    }

    @Override
    public void onResume() {
        super.onResume();
        // TODO Auto-generated method stub

    }

    @Override
    public void onPause() {
        // TODO Auto-generated method stub
        super.onPause();
    }

    public String getClassName(){
        return this.getClass().getSimpleName();
    }


    @Override
    protected void onStop() {
        // TODO Auto-generated method stub
        super.onStop();
//        if(!isForeground(this))
//        {
            isActive = false;
//        }
    }

//    private boolean isForeground (Context context)
//    {
//        ActivityManager am = (ActivityManager)context.getSystemService(Context.ACTIVITY_SERVICE);
//        ComponentName cn = am.getRunningTasks(1).get(0).topActivity;
//        String currentPackageName = cn.getPackageName();
//        if(!TextUtils.isEmpty(currentPackageName) && currentPackageName.equals(getPackageName()))
//        {
//            return true ;
//        }
//
//        return false ;
//    }

    protected void Fresh() {

    }

}

package com.cn.bent.sports.base;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cn.bent.sports.utils.SupportMultipleScreensUtil;
import com.cn.bent.sports.widget.ShowMsgDialog;
import com.vondear.rxtools.view.dialog.RxDialogLoading;
import com.zhl.network.RxManager;

import butterknife.ButterKnife;


/**
 * Created by lyj on 2017/8/4.
 */

public abstract class BaseFragment extends Fragment {

    private RxDialogLoading progressDialog;

    protected Activity mActivity;
    public String TAG;
    /**
     * 获得全局的，防止使用getActivity()为空
     *
     * @param context
     */
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.mActivity = (Activity) context;

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container
            , Bundle savedInstanceState) {
        TAG = getActivity().getPackageName() + "." + getClass().getSimpleName();
        View view = LayoutInflater.from(mActivity)
                .inflate(getLayoutId(), container, false);
        ButterKnife.bind(this,view);
        initView(view, savedInstanceState);
        SupportMultipleScreensUtil.init(mActivity);
        SupportMultipleScreensUtil.scale(view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initData();
    }

    /**
     * 该抽象方法就是 onCreateView中需要的layoutID
     *
     * @return
     */
    protected abstract int getLayoutId();

    /**
     * 该抽象方法就是 初始化view
     *
     * @param view
     * @param savedInstanceState
     */
    protected abstract void initView(View view, Bundle savedInstanceState);

    /**
     * 执行数据的加载
     */
    protected abstract void initData();

    /**
     * 显示加载图标
     *
     * @param txt
     */
    public void showAlert(String txt, final boolean isCancel) {
        if(!"".equals(txt)&&txt!=null){
            if(progressDialog==null){
                progressDialog=new RxDialogLoading(getActivity(),isCancel);
            }
            progressDialog.setLoadingText(txt);
            progressDialog.show();
        }
    }

    /**
     * 关闭加载图标
     */
    public void dismissAlert() {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        RxManager.getInstance().clear(TAG);
    }
}

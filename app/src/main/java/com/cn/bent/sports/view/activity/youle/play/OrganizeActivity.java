package com.cn.bent.sports.view.activity.youle.play;

import android.content.Intent;
import android.view.Gravity;
import android.view.View;

import com.cn.bent.sports.R;
import com.cn.bent.sports.base.BaseActivity;
import com.cn.bent.sports.view.poupwindow.ScanPoupWindow;

import butterknife.OnClick;
/**
*aunthor lyj
* create 2018/3/27/027 15:39   组队界面
**/
public class OrganizeActivity extends BaseActivity {

    private ScanPoupWindow mopupWindow;
    @Override
    protected int getLayoutId() {
        return R.layout.activity_organize;
    }

    @Override
    public void initView() {
        super.initView();
    }

    @Override
    public void initData() {
        super.initData();
    }

    @OnClick({R.id.creat_scan,R.id.sure_start})
    void onclick(View v){
        switch (v.getId()){
            case R.id.creat_scan:
                shouPoup("组队口令");
                break;
            case R.id.sure_start:
                 startActivity(new Intent(OrganizeActivity.this,PrepareActivity.class));
                break;
        }
    }

    private void shouPoup(String strs) {
        mopupWindow = new ScanPoupWindow(OrganizeActivity.this,strs, null);
        mopupWindow.showAtLocation(this.findViewById(R.id.top_view),
                Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
    }
}

package com.cn.bent.sports.view.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.cn.bent.sports.MainActivity;
import com.cn.bent.sports.R;
import com.cn.bent.sports.base.BaseActivity;
import com.cn.bent.sports.base.BaseConfig;
import com.cn.bent.sports.bean.LoginBase;
import com.cn.bent.sports.database.TaskCationBean;
import com.cn.bent.sports.database.TaskCationManager;
import com.cn.bent.sports.utils.Constants;
import com.cn.bent.sports.utils.SaveObjectUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by dawn on 2018/2/5.
 */

public class ChooseLuxianActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_choose;
    }

    @Override
    public void initView() {
        super.initView();
    }

    @Override
    public void initData() {
        super.initData();
    }

    @OnClick({R.id.shiyanhu, R.id.jinmao})
    void OnClick(View view) {
        BaseConfig bg = BaseConfig.getInstance(ChooseLuxianActivity.this);
        switch (view.getId()) {
            case R.id.shiyanhu:
              bg.setIntValue(Constants.LU_XIAN, 1);
                break;
            case R.id.jinmao:
                bg.setIntValue(Constants.LU_XIAN, 2);
                break;
        }
    }



    private void setShiYanHudata() {
        if (TaskCationManager.getSize() <= 0) {
            LoginBase user = SaveObjectUtils.getInstance(ChooseLuxianActivity.this).getObject(Constants.USER_INFO, null);
            List<TaskCationBean> nList = new ArrayList<>();
            nList.add(new TaskCationBean(1, user.getMember_id(), "1", "113.087645", "28.012992", "歌舞广场", false, false));
            nList.add(new TaskCationBean(2, user.getMember_id(), "1", "113.087843", "28.007034", "丛林穿越 ", false, false));
            nList.add(new TaskCationBean(3, user.getMember_id(), "1", "113.085139", "28.003593", "沙滩", false, false));
            nList.add(new TaskCationBean(4, user.getMember_id(), "1", "113.089565", "28.010653", "财神庙", false, false));
            nList.add(new TaskCationBean(5, user.getMember_id(), "1", "113.089232", "28.007276", "月亮岛", false, false));
            nList.add(new TaskCationBean(6, user.getMember_id(), "1", "113.086443", "28.005701", "竹林", false, false));
            TaskCationManager.insert(nList);
        }
    }





}

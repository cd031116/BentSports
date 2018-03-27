package com.cn.bent.sports.view.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.cn.bent.sports.R;
import com.cn.bent.sports.base.BaseFragment;
import com.cn.bent.sports.utils.ToastUtils;
import com.cn.bent.sports.view.activity.MapActivity;
import com.cn.bent.sports.view.activity.PlayActivity;
import com.cn.bent.sports.view.activity.PlayFunActivity;
import com.cn.bent.sports.view.activity.PlayWebViewActivity;
import com.minew.beacon.BluetoothState;
import com.vondear.rxtools.RxActivityTool;
import com.vondear.rxtools.activity.ActivityScanerCode;

import butterknife.OnClick;

/**
 * Created by lyj on 2018/3/19 0019.
 * description
 */

public class RecommendFragment extends BaseFragment {
    public static RecommendFragment newInstance() {
        RecommendFragment fragment = new RecommendFragment();
//        Bundle bundle = new Bundle();
//        fragment.setArguments(bundle);
        return fragment;
    }
    private static final int REQUEST_Scan = 12;

    @Override
    protected int getLayoutId() {
        return R.layout.recommend_fragment;
    }

    @Override
    protected void initView(View view, Bundle savedInstanceState) {

    }

    @Override
    protected void initData() {

    }

    @OnClick({R.id.go_daolan,R.id.scan_t,R.id.line_title,R.id.activity_one})
    void onclick(View view){
        switch (view.getId()){
            case R.id.go_daolan:
                startActivity(new Intent(getActivity(), MapActivity.class));
                break;
            case R.id.scan_t:
                RxActivityTool.skipActivityForResult(getActivity(),ActivityScanerCode.class,REQUEST_Scan);
                break;
            case R.id.line_title:
                startActivity(new Intent(getActivity(), PlayFunActivity.class));
                break;
            case R.id.activity_one:
                startActivity(new Intent(getActivity(), PlayActivity.class));
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQUEST_Scan:
                if (null != data) {
                    Bundle bundle = data.getExtras();
                    if (bundle == null) {
                        return;
                    }
                    String jieguo=data.getStringExtra("SCAN_RESULT");

                    break;
                }
        }
    }

}

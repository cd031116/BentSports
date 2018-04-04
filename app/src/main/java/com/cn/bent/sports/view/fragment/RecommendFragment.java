package com.cn.bent.sports.view.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.cn.bent.sports.R;
import com.cn.bent.sports.api.BaseApi;
import com.cn.bent.sports.base.BaseFragment;
import com.cn.bent.sports.view.activity.MapActivity;
import com.cn.bent.sports.view.activity.WalkRankListActivity;
import com.cn.bent.sports.view.activity.youle.MyRouteListActivity;
import com.cn.bent.sports.view.activity.youle.PlayActivity;
import com.cn.bent.sports.view.activity.PlayFunActivity;
import com.cn.bent.sports.view.activity.youle.bean.JoinTeam;
import com.cn.bent.sports.view.activity.youle.play.TeamMemberActivity;
import com.vondear.rxtools.RxActivityTool;
import com.vondear.rxtools.activity.ActivityScanerCode;
import com.vondear.rxtools.view.RxToast;
import com.zhl.network.RxObserver;
import com.zhl.network.RxSchedulers;
import com.zhl.network.huiqu.JavaRxFunction;

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

    @OnClick({R.id.go_daolan,R.id.scan_t,R.id.line_title,R.id.activity_one,R.id.seek_rank,R.id.see_my})
    void onclick(View view){
        switch (view.getId()){
            case R.id.go_daolan:
                startActivity(new Intent(getActivity(), MapActivity.class));
                break;
            case R.id.scan_t:
                Intent intent = new Intent(getActivity(), ActivityScanerCode.class);
                startActivityForResult(intent, REQUEST_Scan);
                break;
            case R.id.line_title:
                startActivity(new Intent(getActivity(), PlayFunActivity.class));
                break;
            case R.id.activity_one:
                startActivity(new Intent(getActivity(), PlayActivity.class));
                break;
            case R.id.seek_rank:
                startActivity(new Intent(getActivity(), WalkRankListActivity.class));
                break;
            case R.id.see_my:
                startActivity(new Intent(getActivity(), MyRouteListActivity.class));
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQUEST_Scan:
                if (null != data) {
                    String jieguo=data.getStringExtra("SCAN_RESULT");
                    getJoinTeam(jieguo);
                    break;
                }
        }
    }


    private void getJoinTeam(final  String teamId ) {
        showAlert("正在加入组队...", true);
        BaseApi.getJavaLoginDefaultService(getActivity()).joinTeamGame(teamId )
                .map(new JavaRxFunction<JoinTeam>())
                .compose(RxSchedulers.<JoinTeam>io_main())
                .subscribe(new RxObserver<JoinTeam>(getActivity(), TAG, 1, false) {
                    @Override
                    public void onSuccess(int whichRequest, JoinTeam info) {
                        dismissAlert();
                        Intent intent=new Intent(getActivity(),TeamMemberActivity.class);
                        intent.putExtra("gameTeamId",info.getGameTeamId());
                        startActivity(intent);
                    }

                    @Override
                    public void onError(int whichRequest, Throwable e) {
                        dismissAlert();
                        RxToast.error(e.getMessage());
                    }
                });
    }


}

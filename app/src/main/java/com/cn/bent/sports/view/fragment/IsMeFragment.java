package com.cn.bent.sports.view.fragment;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.amap.api.services.core.LatLonPoint;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.cn.bent.sports.R;
import com.cn.bent.sports.api.BaseApi;
import com.cn.bent.sports.base.BaseFragment;
import com.cn.bent.sports.bean.LoginBase;
import com.cn.bent.sports.bean.LoginResult;
import com.cn.bent.sports.bean.ScenicPointsEntity;
import com.cn.bent.sports.bean.UserMsgEntity;
import com.cn.bent.sports.ibeacon.ScanActivity;
import com.cn.bent.sports.utils.Constants;
import com.cn.bent.sports.utils.SaveObjectUtils;
import com.cn.bent.sports.view.activity.MapActivity;
import com.cn.bent.sports.view.activity.PlayFunActivity;
import com.cn.bent.sports.view.activity.PlayWebViewActivity;
import com.cn.bent.sports.view.activity.SettingActivity;
import com.cn.bent.sports.view.activity.youle.MyRouteListActivity;
import com.zhl.network.RxObserver;
import com.zhl.network.RxSchedulers;
import com.zhl.network.huiqu.HuiquRxFunction;
import com.zhl.network.huiqu.JavaRxFunction;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by lyj on 2018/1/29 0029.
 * description
 */

public class IsMeFragment extends BaseFragment {
    @Bind(R.id.user_photo)
    ImageView user_photo;
    @Bind(R.id.nick_name)
    TextView nick_name;

    private LoginResult user;


    public static IsMeFragment newInstance() {
        IsMeFragment fragment = new IsMeFragment();
//        Bundle bundle = new Bundle();
//        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.is_me_fragment;
    }

    @Override
    protected void initView(View view, Bundle savedInstanceState) {
        user = (LoginResult) SaveObjectUtils.getInstance(getActivity()).getObject(Constants.USER_INFO, null);

    }

    @Override
    public void onResume() {
        super.onResume();

    }

    @Override
    protected void initData() {
    }

    private void setView(UserMsgEntity userMsgEntity) {
        if (getActivity() == null)
            return;
        nick_name.setText(userMsgEntity.getUserMsg().getNickname());
        RequestOptions requestOptions = RequestOptions.circleCropTransform();
        Glide.with(user_photo.getContext()).load(userMsgEntity.getUserMsg().getHeadimg())
                .apply(requestOptions)
                .into(user_photo);
        if (userMsgEntity.getCard_num() != null && userMsgEntity.getCard_num().size() > 0) {
            for (UserMsgEntity.CardNumBean cardBean : userMsgEntity.getCard_num()) {
                Log.d("tttt", "getGame_id: " + cardBean.getGame_id());
                switch (cardBean.getGame_id()) {
                    case 1:
                        if (cardBean.getStatus() == 1)
                            break;
                    case 12:
                        if (cardBean.getStatus() == 1)
                            break;
                    case 13:
                        if (cardBean.getStatus() == 1)
                            break;
                    case 14:
                        if (cardBean.getStatus() == 1)
                            break;
                    case 15:
                        if (cardBean.getStatus() == 1)
                            break;
                    case 16:
                        if (cardBean.getStatus() == 1)
                            break;
                    case 17:
                        if (cardBean.getStatus() == 1)
                            break;
                }
            }
        }
    }

    @OnClick({R.id.setting, R.id.user_photo,R.id.quwan,R.id.xitong})
    void conlick(View view) {
        switch (view.getId()) {
            case R.id.setting:
            case R.id.user_photo:

                break;
            case R.id.quwan:
                startActivity(new Intent(getActivity(), MyRouteListActivity.class));
                break;
            case R.id.xitong:
                startActivity(new Intent(getActivity(), SettingActivity.class));
                break;
        }
    }
}

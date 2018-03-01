package com.cn.bent.sports.view.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.cn.bent.sports.R;
import com.cn.bent.sports.api.BaseApi;
import com.cn.bent.sports.base.BaseFragment;
import com.cn.bent.sports.bean.LoginBase;
import com.cn.bent.sports.bean.UserMsgEntity;
import com.cn.bent.sports.ibeacon.ScanActivity;
import com.cn.bent.sports.utils.Constants;
import com.cn.bent.sports.utils.SaveObjectUtils;
import com.cn.bent.sports.view.activity.PlayWebViewActivity;
import com.cn.bent.sports.view.activity.SettingActivity;
import com.zhl.network.RxObserver;
import com.zhl.network.RxSchedulers;
import com.zhl.network.huiqu.HuiquRxFunction;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by lyj on 2018/1/29 0029.
 * description
 */

public class IsMeFragment extends BaseFragment {

    @Bind(R.id.liunianshou)
    ImageView liunianshou;
    @Bind(R.id.caidengmi)
    ImageView caidengmi;
    @Bind(R.id.fangbianpao)
    ImageView fangbianpao;
    @Bind(R.id.hongbaoyu)
    ImageView hongbaoyu;
    @Bind(R.id.diandenglong)
    ImageView diandenglong;
    @Bind(R.id.jixiangqian)
    ImageView jixiangqian;
    @Bind(R.id.kanyanchu)
    ImageView kanyanchu;
    @Bind(R.id.user_photo)
    ImageView user_photo;
    @Bind(R.id.nick_name)
    TextView nick_name;
    @Bind(R.id.score)
    TextView score;

    private LoginBase user;


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
        user = (LoginBase) SaveObjectUtils.getInstance(getActivity()).getObject(Constants.USER_INFO, null);

    }

    @Override
    public void onResume() {
        super.onResume();
        if (user != null) {
            BaseApi.getDefaultService(getActivity())
                    .getUserMsg(user.getMember_id())
                    .map(new HuiquRxFunction<UserMsgEntity>())
                    .compose(RxSchedulers.<UserMsgEntity>io_main())
                    .subscribe(new RxObserver<UserMsgEntity>(getActivity(), "getUserMsg", 1, false) {
                        @Override
                        public void onSuccess(int whichRequest, UserMsgEntity userMsgEntity) {
                            setView(userMsgEntity);
                        }

                        @Override
                        public void onError(int whichRequest, Throwable e) {

                        }
                    });
        }
    }

    @Override
    protected void initData() {
    }

    private void setView(UserMsgEntity userMsgEntity) {
        if(getActivity()==null)
        return;
        nick_name.setText(userMsgEntity.getUserMsg().getNickname());
        score.setText(userMsgEntity.getUserMsg().getScore() + "");
        RequestOptions requestOptions = RequestOptions.circleCropTransform();
        Glide.with(user_photo.getContext()).load(userMsgEntity.getUserMsg().getHeadimg())
                .apply(requestOptions)
                .into(user_photo);
        if (userMsgEntity.getCard_num() != null && userMsgEntity.getCard_num().size() > 0) {
            for (UserMsgEntity.CardNumBean cardBean : userMsgEntity.getCard_num()) {
                Log.d("tttt", "getGame_id: "+cardBean.getGame_id());
                switch (cardBean.getGame_id()) {
                    case 1:
                        if (cardBean.getStatus() == 1)
                            hongbaoyu.setBackground(getResources().getDrawable(R.drawable.hongbaoyu));
                        break;
                    case 12:
                        if (cardBean.getStatus() == 1)
                            diandenglong.setBackground(getResources().getDrawable(R.drawable.diandenglong));
                        break;
                    case 13:
                        if (cardBean.getStatus() == 1)
                            jixiangqian.setBackground(getResources().getDrawable(R.drawable.jixiangqian));
                        break;
                    case 14:
                        if (cardBean.getStatus() == 1)
                            caidengmi.setBackground(getResources().getDrawable(R.drawable.caidengmi));
                        break;
                    case 15:
                        if (cardBean.getStatus() == 1)
                            kanyanchu.setBackground(getResources().getDrawable(R.drawable.kanyanchu));
                        break;
                    case 16:
                        if (cardBean.getStatus() == 1)
                            liunianshou.setBackground(getResources().getDrawable(R.drawable.liunianshou));
                        break;
                    case 17:
                        if (cardBean.getStatus() == 1)
                            fangbianpao.setBackground(getResources().getDrawable(R.drawable.duichunlian));
                        break;
                }
            }
        }
    }

    @OnClick({R.id.setting,R.id.user_photo})
    void conlick(View view) {
        switch (view.getId()) {
            case R.id.setting:
            case R.id.user_photo:
                startActivity(new Intent(getActivity(), SettingActivity.class));
                break;
        }
    }
}

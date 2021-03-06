package com.cn.bent.sports.view.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.cn.bent.sports.R;
import com.cn.bent.sports.base.BaseFragment;
import com.cn.bent.sports.utils.Constants;
import com.cn.bent.sports.utils.ImageUtils;
import com.cn.bent.sports.utils.SaveObjectUtils;
import com.cn.bent.sports.utils.StatusBarUtil;
import com.cn.bent.sports.view.activity.SettingActivity;
import com.cn.bent.sports.view.activity.youle.MyRouteListActivity;
import com.cn.bent.sports.view.activity.youle.bean.UserInfo;
import com.vondear.rxtools.view.RxToast;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by lyj on 2018/1/29 0029.
 * description
 */

public class IsMeFragment extends BaseFragment {
    @Bind(R.id.user_photo)
    ImageView user_photo;
    @Bind(R.id.nickname)
    TextView nick_name;
    @Bind(R.id.scrore)
    TextView scrore;
    @Bind(R.id.get_num)
    TextView get_num;

    private UserInfo user;


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
    }

    @Override
    public void onResume() {
        super.onResume();
        user = (UserInfo) SaveObjectUtils.getInstance(getActivity()).getObject(Constants.USER_BASE, null);
        setView(user);
    }

    @Override
    protected void initData() {
    }

    private void setView(UserInfo userMsgEntity) {
        if (getActivity() == null)
            return;
        if(userMsgEntity==null){
            return;
        }
        nick_name.setText(TextUtils.isEmpty(userMsgEntity.getNickname())?"":userMsgEntity.getNickname());
        RequestOptions requestOptions = RequestOptions.circleCropTransform();
        Glide.with(user_photo.getContext()).load(ImageUtils.getImageUrl(userMsgEntity.getAvatar()))
                .apply(requestOptions)
                .into(user_photo);
        get_num.setText("0");
        scrore.setText("0");
    }

    @OnClick({R.id.setting, R.id.user_photo,R.id.quwan,R.id.xitong,R.id.user_rela,R.id.lixian,R.id.menpiao,R.id.duihuan,R.id.youhui,R.id.changjian,R.id.zhuanqu})
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
            case R.id.nick_name:
            case R.id.user_rela:
            case R.id.lixian:
            case R.id.menpiao:
            case R.id.duihuan:
            case R.id.jifen:
            case R.id.youhui:
            case R.id.changjian:
            case R.id.zhuanqu:
                RxToast.normal("敬请期待");
                break;
        }
    }
}

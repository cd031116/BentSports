package com.cn.bent.sports.view.activity;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.cn.bent.sports.MainActivity;
import com.cn.bent.sports.R;
import com.cn.bent.sports.api.BaseApi;
import com.cn.bent.sports.base.BaseActivity;
import com.cn.bent.sports.bean.LoginBase;
import com.cn.bent.sports.utils.Constants;
import com.cn.bent.sports.utils.ImageUtils;
import com.cn.bent.sports.utils.SaveObjectUtils;
import com.cn.bent.sports.utils.ToastUtils;
import com.cn.bent.sports.widget.ToastDialog;
import com.zhl.network.RxObserver;
import com.zhl.network.RxSchedulers;
import com.zhl.network.huiqu.HuiquRxTBFunction;
import com.zhl.network.huiqu.HuiquTBResult;
import com.zzti.fengyongge.imagepicker.PhotoSelectorActivity;

import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;

public class SettingActivity extends BaseActivity {
    @Bind(R.id.name_t)
    TextView name_t;
    @Bind(R.id.phone_t)
    TextView phone_t;
    @Bind(R.id.user_photo)
    ImageView user_photo;

    private LoginBase user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_setting;
    }

    @Override
    public void initView() {
        super.initView();

    }

    @Override
    public void onResume() {
        super.onResume();
        user = SaveObjectUtils.getInstance(SettingActivity.this).getObject(Constants.USER_INFO, null);
        if (TextUtils.isEmpty(user.getNickname())) {
            name_t.setText("未设置");
            name_t.setTextColor(Color.parseColor("#999999"));
        } else {
            name_t.setText(user.getNickname());
            name_t.setTextColor(Color.parseColor("#333333"));
            phone_t.setText(user.getMobile());
            RequestOptions requestOptions = RequestOptions.circleCropTransform();
            Glide.with(SettingActivity.this).load(user.getHeadimg())
                    .apply(requestOptions)
                    .into(user_photo);
        }
    }

    @Override
    public void initData() {
        super.initData();
    }

    @OnClick({R.id.top_left, R.id.top_image, R.id.ni_ceng, R.id.login,R.id.user_photo})
    void conlick(View view) {
        switch (view.getId()) {
            case R.id.ni_ceng:
                startActivity(new Intent(SettingActivity.this, ChangeNameActivity.class));
                break;
            case R.id.top_image:
            case R.id.top_left:
                SettingActivity.this.finish();
                break;
            case R.id.login:
                showDialogMsg("确定退出当前账号？");
                break;
            case R.id.user_photo:
                Intent intent = new Intent(SettingActivity.this, PhotoSelectorActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                intent.putExtra("limit", 1 );//number是选择图片的数量
                startActivityForResult(intent, 0);

                break;
        }
    }


    private void showDialogMsg(String names) {
        new ToastDialog(this, R.style.dialog, names, new ToastDialog.OnCloseListener() {
            @Override
            public void onClick(Dialog dialog, boolean confirm) {
                if (confirm) {
                    SaveObjectUtils.getInstance(SettingActivity.this).setObject(Constants.USER_INFO, null);
                    startActivity(new Intent(SettingActivity.this, LoginActivity.class));
                    SettingActivity.this.finish();
                } else {

                }
                dialog.dismiss();
            }
        }).setTitle("提示").show();
    }



    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case 0:
                if (data != null) {
                    List<String> paths = (List<String>) data.getExtras().getSerializable("photos");//path是选择拍照或者图片的地址数组
                    //处理代码
                    String image= ImageUtils.bitmapToBase64(ImageUtils.Str2BitmapByFilePath(paths.get(0)));
                    login(image);
                }
                break;
            default:
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }


    private void login(final String imag) {
        showAlert("正在提交...", true);
        BaseApi.getDefaultService(this).modifyUserPhoto(user.getMember_id(), "2", imag)
                .map(new HuiquRxTBFunction<HuiquTBResult>())
                .compose(RxSchedulers.<HuiquTBResult>io_main())
                .subscribe(new RxObserver<HuiquTBResult>(SettingActivity.this, "changeName", 1, false) {
                    @Override
                    public void onSuccess(int whichRequest, HuiquTBResult info) {
                        dismissAlert();
                        SaveObjectUtils.getInstance(SettingActivity.this).setObject(Constants.USER_INFO, user);
                        finish();
                    }

                    @Override
                    public void onError(int whichRequest, Throwable e) {
                        dismissAlert();
                        ToastUtils.showLongToast(SettingActivity.this, e.getMessage());
                    }
                });
    }


}

package com.cn.bent.sports.view.activity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.cn.bent.sports.R;
import com.cn.bent.sports.api.BaseApi;
import com.cn.bent.sports.base.BaseActivity;
import com.cn.bent.sports.base.MyApplication;
import com.cn.bent.sports.bean.InfoEvent;
import com.cn.bent.sports.bean.LoginBase;
import com.cn.bent.sports.bean.PhotoPath;
import com.cn.bent.sports.utils.Constants;
import com.cn.bent.sports.utils.ImageUtils;
import com.cn.bent.sports.utils.SaveObjectUtils;
import com.cn.bent.sports.widget.ToastDialog;
import com.vondear.rxtools.view.RxToast;
import com.yuyh.library.imgsel.ISNav;
import com.yuyh.library.imgsel.common.ImageLoader;
import com.yuyh.library.imgsel.config.ISListConfig;
import com.zhl.network.RxObserver;
import com.zhl.network.RxSchedulers;
import com.zhl.network.huiqu.HuiquRxFunction;

import org.greenrobot.eventbus.EventBus;

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
    private int REQUEST_CAMERA_CODE=102;
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
        // 自定义图片加载器
        ISNav.getInstance().init(new ImageLoader() {
            @Override
            public void displayImage(Context context, String path, ImageView imageView) {
                Glide.with(context).load(path).into(imageView);
            }
        });
        user = SaveObjectUtils.getInstance(SettingActivity.this).getObject(Constants.USER_INFO, null);
        if(user!=null){
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
    }

    @Override
    public void initData() {
        super.initData();
    }

    @OnClick({R.id.top_left, R.id.top_image, R.id.ni_ceng, R.id.login,R.id.user_photo,R.id.gai_photo})
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
            case R.id.gai_photo:
                ISListConfig config = new ISListConfig.Builder()
                        // 是否多选, 默认true
                        .multiSelect(false)
                        // 是否记住上次选中记录, 仅当multiSelect为true的时候配置，默认为true
                        .rememberSelected(false)
                        // “确定”按钮背景色
                        .btnBgColor(Color.GRAY)
                        // “确定”按钮文字颜色
                        .btnTextColor(Color.BLUE)
                        // 使用沉浸式状态栏
                        .statusBarColor(Color.parseColor("#3F51B5"))
                        // 返回图标ResId
                        .backResId(R.drawable.guanbi_hei)
                        // 标题
                        .title("图片")
                        // 标题文字颜色
                        .titleColor(Color.WHITE)
                        // TitleBar背景色
                        .titleBgColor(Color.parseColor("#3F51B5"))
                        // 裁剪大小。needCrop为true的时候配置
                        .cropSize(1, 1, 200, 200)
                        .needCrop(true)
                        // 第一个是否显示相机，默认true
                        .needCamera(true)
                        // 最大选择图片数量，默认9
                        .maxNum(1)
                        .build();
                ISNav.getInstance().toListActivity(this, config, REQUEST_CAMERA_CODE);
                break;
        }
    }


    private void showDialogMsg(String names) {
        new ToastDialog(this, R.style.dialog, names, new ToastDialog.OnCloseListener() {
            @Override
            public void onClick(Dialog dialog, boolean confirm) {
                if (confirm) {
                    SaveObjectUtils.getInstance(SettingActivity.this).setObject(Constants.USER_INFO, null);
                    MyApplication.instance.getActivityManager().popAllActivity();
                    startActivity(new Intent(SettingActivity.this, LoginActivity.class));
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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // 图片选择结果回调
        if (requestCode == REQUEST_CAMERA_CODE && resultCode == RESULT_OK && data != null) {
            List<String> pathList = data.getStringArrayListExtra("result");
            String image = ImageUtils.bitmapToBase64(ImageUtils.Str2BitmapByFilePath(pathList.get(0)));
            login(image);
        }
    }


    private void login(final String imag) {
        showAlert("正在提交...", true);
        BaseApi.getDefaultService(this).modifyUserPhoto(user.getMember_id(), "2", imag)
                .map(new HuiquRxFunction<PhotoPath>())
                .compose(RxSchedulers.<PhotoPath>io_main())
                .subscribe(new RxObserver<PhotoPath>(SettingActivity.this, "changeName", 1, false) {
                    @Override
                    public void onSuccess(int whichRequest, PhotoPath info) {
                        dismissAlert();
                        LoginBase users=SaveObjectUtils.getInstance(SettingActivity.this).getObject(Constants.USER_INFO, null);
                        users.setHeadimg(info.getHeadimgurl());
                        SaveObjectUtils.getInstance(SettingActivity.this).setObject(Constants.USER_INFO, users);

                        RequestOptions requestOptions = RequestOptions.circleCropTransform();
                        Glide.with(SettingActivity.this).load(info.getHeadimgurl())
                                .apply(requestOptions)
                                .into(user_photo);
                        RxToast.success("图像更改成功");
                        EventBus.getDefault().post(new InfoEvent());
                    }

                    @Override
                    public void onError(int whichRequest, Throwable e) {
                        dismissAlert();
                        RxToast.error( e.getMessage());
                    }
                });
    }


}

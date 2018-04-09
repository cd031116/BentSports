package com.cn.bent.sports.view.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.cn.bent.sports.R;
import com.cn.bent.sports.base.BaseActivity;
import com.vondear.rxtools.view.RxToast;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by dawn on 2017/12/4.
 */

public class FriendActivity extends BaseActivity {

    @Bind(R.id.report_list)
    RecyclerView recyclerView;
    @Bind(R.id.et_content)
    EditText etContent;
    @Bind(R.id.r_text)
    TextView r_text;
    @Bind(R.id.loc_text)
    TextView loc_text;
    @Bind(R.id.info_title)
    TextView info_title;

    private int maxSelectNum = 9;
    private List<String> imgSourceList = new ArrayList<>();
    private String member_id;

    //声明AMapLocationClient类对象
    public AMapLocationClient mLocationClient = null;
    //声明AMapLocationClientOption对象
    public AMapLocationClientOption mLocationOption = null;

    private boolean isFirstLoc = true;
    private boolean isHasFouces = false;
    AMapLocationListener mAMapLocationListener = new AMapLocationListener() {
        @Override
        public void onLocationChanged(AMapLocation aMapLocation) {
            if (aMapLocation != null) {
                if (aMapLocation.getErrorCode() == 0) {
//可在其中解析amapLocation获取相应内容。
                    if (isFirstLoc) {
                        isFirstLoc = false;
                        loc_text.setText(aMapLocation.getCity() + aMapLocation.getDistrict() + aMapLocation.getStreet());
                    }
                } else {
                    //定位失败时，可通过ErrCode（错误码）信息来确定失败的原因，errInfo是错误信息，详见错误码表。
                    Log.e("AmapError", "location Error, ErrCode:"
                            + aMapLocation.getErrorCode() + ", errInfo:"
                            + aMapLocation.getErrorInfo());
                }
            }
        }

    };

    @Override
    protected int getLayoutId() {
        return R.layout.report_layout;
    }


    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void initView() {
        r_text.setVisibility(View.VISIBLE);
        info_title.setText("发表咕咕");

    }


    private void setEtLayout(int type) {
        RelativeLayout.LayoutParams linearParams = (RelativeLayout.LayoutParams) etContent.getLayoutParams(); //取控件textView当前的布局参数
        if (type == 0)
            linearParams.height = getScreenH(this);
        if (type == 1)
            linearParams.height = getScreenH(this) / 5;
        etContent.setLayoutParams(linearParams);
    }

    /**
     * 设置图片地址
     */
    private void setSourcelist() {

    }


    @Override
    public void initData() {
        //初始化定位
        mLocationClient = new AMapLocationClient(getApplicationContext());
//设置定位回调监听
        mLocationClient.setLocationListener(mAMapLocationListener);
        setLocation();
    }

    private void setLocation() {
        //初始化AMapLocationClientOption对象
        mLocationOption = new AMapLocationClientOption();
        //设置定位模式为AMapLocationMode.Hight_Accuracy，高精度模式。
        mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
        //给定位客户端对象设置定位参数
        mLocationClient.setLocationOption(mLocationOption);
        //设置定位模式为AMapLocationMode.Device_Sensors，仅设备模式。
        mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Device_Sensors);
//启动定位
        mLocationOption.setOnceLocation(true);
        mLocationClient.startLocation();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mLocationClient.onDestroy();//销毁定位客户端。
        //销毁定位客户端之后，若要重新开启定位请重新New一个AMapLocationClient对象。
    }

    @OnClick({R.id.r_text, R.id.img_back, R.id.tupian, R.id.jianpan,R.id.loc_text})
    void onClick(View view) {
        switch (view.getId()) {
            case R.id.r_text:
                if (!TextUtils.isEmpty(member_id))
                    push2Service();
                else
                    loginBefore();
                break;
            case R.id.img_back:
                closeActivity();
                break;
            case R.id.loc_text:
                setLocation();
                break;
            case R.id.tupian:
                setEtLayout(1);
                picClick();
                break;
            case R.id.jianpan:
                if (isHasFouces) {
                    isHasFouces = false;
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(getWindow().getDecorView().getWindowToken(), 0);
                } else {
                    isHasFouces = true;
                    etContent.setFocusable(true);
                    etContent.setFocusableInTouchMode(true);
                    etContent.requestFocus();
                    etContent.findFocus();
                    InputMethodManager imm = (InputMethodManager) this.getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.showSoftInput(etContent, InputMethodManager.SHOW_FORCED);// 显示输入法
                }
                break;
        }
    }

    private void push2Service() {
        StringBuffer imgScStr = new StringBuffer();
        if (imgSourceList != null && imgSourceList.size() > 0)
            for (int i = 0; i < imgSourceList.size(); i++) {
                imgScStr.append(imgSourceList.get(i).toString());
                if (i != imgSourceList.size() - 1)
                    imgScStr.append(",");
            }
//        FriendsApi.getFriendsService(this)
//                .sendGuImg(Integer.valueOf(member_id), 1, loc_text.getText().toString(), etContent.getText().toString(), imgScStr.toString())
//                .map(new HuiquRxFunction<SendGuEntity>())
//                .compose(RxSchedulers.<SendGuEntity>io_main())
//                .subscribe(new RxObserver<SendGuEntity>(this, TAG, 1, true) {
//                    @Override
//                    public void onSuccess(int whichRequest, SendGuEntity sendGuEntity) {
//                        clearFlush();
//                        ToastUtils.showLongToast(FriendActivity.this,"发表成功");
//                        finish();
//                    }
//
//                    @Override
//                    public void onError(int whichRequest, Throwable e) {
//                        ToastUtils.showShortToast(FriendActivity.this, e.getMessage());
//                    }
//                });
    }

    private void clearFlush() {

    }

//    private GridImageAdapter.onAddPicClickListener onAddPicClickListener = new GridImageAdapter.onAddPicClickListener() {
//        @Override
//        public void onAddPicClick() {
//            picClick();
//        }
//
//    };

    private void picClick() {
        // 进入相册 以下是例子：不需要的api可以不写
//        PictureSelector.create(FriendActivity.this)
//                .openGallery(PictureMimeType.ofImage())// 全部.PictureMimeType.ofAll()、图片.ofImage()、视频.ofVideo()、音频.ofAudio()
//                .theme(themeId)// 主题样式设置 具体参考 values/styles   用法：R.style.picture.white.style
//                .maxSelectNum(maxSelectNum)// 最大图片选择数量
//                .minSelectNum(1)// 最小选择数量
//                .imageSpanCount(4)// 每行显示个数
//                .selectionMode(PictureConfig.MULTIPLE)// 多选 or 单选
//                .previewImage(true)// 是否可预览图片
//                .previewVideo(true)// 是否可预览视频
//                .enablePreviewAudio(true) // 是否可播放音频
//                .isCamera(true)// 是否显示拍照按钮
//                .isZoomAnim(true)// 图片列表点击 缩放效果 默认true
//                .enableCrop(false)// 是否裁剪
//                .compress(true)// 是否压缩
//                .synOrAsy(true)//同步true或异步false 压缩 默认同步
//                .glideOverride(160, 160)// glide 加载宽高，越小图片列表越流畅，但会影响列表图片浏览的清晰度
//                .hideBottomControls(false)// 是否显示uCrop工具栏，默认不显示
//                .isGif(true)// 是否显示gif图片
//                .freeStyleCropEnabled(false)// 裁剪框是否可拖拽
//                .showCropFrame(false)// 是否显示裁剪矩形边框 圆形裁剪时建议设为false
//                .showCropGrid(false)// 是否显示裁剪矩形网格 圆形裁剪时建议设为false
//                .openClickSound(true)// 是否开启点击声音
//                .selectionMedia(selectList)// 是否传入已选图片
//                .minimumCompressSize(100)// 小于100kb的图片不压缩
//                .forResult(PictureConfig.CHOOSE_REQUEST);//结果回调onActivityResult code
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
//                case PictureConfig.CHOOSE_REQUEST:
//                    // 图片选择结果回调
//                    selectList = new ArrayList<>();
//                    selectList = PictureSelector.obtainMultipleResult(data);
//                    // 例如 LocalMedia 里面返回三种path
//                    // 1.media.getPath(); 为原图path
//                    // 2.media.getCutPath();为裁剪后path，需判断media.isCut();是否为true
//                    // 3.media.getCompressPath();为压缩后path，需判断media.isCompressed();是否为true
//                    // 如果裁剪并压缩了，已取压缩路径为准，因为是先裁剪后压缩的
//                    setSourcelist();
//                    adapter.setList(selectList);
//                    adapter.notifyDataSetChanged();
//                    break;
            }
        }
    }

    private void loginBefore() {
        RxToast.warning("请先登录");
        startActivity(new Intent(FriendActivity.this, LoginActivity.class));
    }

    public int getScreenH(Context aty) {
        DisplayMetrics dm = aty.getResources().getDisplayMetrics();
        return dm.heightPixels;
    }

    /**
     * Close Activity
     */
    protected void closeActivity() {
        finish();
//        selectList.clear();
//        adapter.notifyDataSetChanged();
//        if (config.camera) {
//            overridePendingTransition(0, com.luck.picture.lib.R.anim.fade_out);
//        } else {
//            overridePendingTransition(0, com.luck.picture.lib.R.anim.a3);
//        }
    }
}

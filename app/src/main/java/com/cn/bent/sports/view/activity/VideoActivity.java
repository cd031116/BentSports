package com.cn.bent.sports.view.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.cn.bent.sports.R;
import com.cn.bent.sports.base.BaseActivity;
import com.dou361.ijkplayer.listener.OnShowThumbnailListener;
import com.dou361.ijkplayer.widget.PlayStateParams;
import com.dou361.ijkplayer.widget.PlayerView;

import butterknife.Bind;

import static android.R.id.list;

public class VideoActivity extends BaseActivity {
    @Bind(R.id.play_view)
    RelativeLayout play_view;
    PlayerView  player ;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_video;
    }



    @Override
    public void initView() {
        super.initView();
        String url1 = "http://9890.vod.myqcloud.com/9890_4e292f9a3dd011e6b4078980237cc3d3.f20.mp4";
        player = new PlayerView(this,play_view)
                .setTitle("什么")
                .setScaleType(PlayStateParams.fitparent)
                .hideMenu(true)
                .forbidTouch(false)
                .showThumbnail(new OnShowThumbnailListener() {
                    @Override
                    public void onShowThumbnail(ImageView ivThumbnail) {
                        /**加载前显示的缩略图*/
//                        Glide.with(VideoActivity.this)
//                                .load("http://pic2.nipic.com/20090413/406638_125424003_2.jpg")
//                                .placeholder(R.color.cl_default)
//                                .error(R.color.cl_error)
//                                .into(ivThumbnail);
                    }
                })
                .setPlaySource(url1)
                .startPlay();



    }

    @Override
    public void initData() {
        super.initData();
    }
}

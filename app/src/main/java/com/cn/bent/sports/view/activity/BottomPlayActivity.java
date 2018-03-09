package com.cn.bent.sports.view.activity;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.cn.bent.sports.R;
import com.cn.bent.sports.base.BaseActivity;
import com.cn.bent.sports.bean.PlayBean;
import com.cn.bent.sports.bean.PlayEvent;
import com.cn.bent.sports.bean.StartEvent;
import com.cn.bent.sports.evevt.DistanceEvent;
import com.cn.bent.sports.evevt.DistanceSubscriber;
import com.cn.bent.sports.utils.Constants;
import com.cn.bent.sports.utils.NiceUtil;
import com.cn.bent.sports.utils.SaveObjectUtils;
import com.cn.bent.sports.utils.SupportMultipleScreensUtil;
import com.cn.bent.sports.view.service.MusicService;

import org.aisen.android.component.eventbus.NotificationCenter;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class BottomPlayActivity extends BaseActivity {
    @Bind(R.id.name_t)
    TextView name_t;
    @Bind(R.id.play_t)
    ImageView play_t;
    @Bind(R.id.curent_time)
    TextView curent_time;
    @Bind(R.id.total_time)
    TextView total_time;
    @Bind(R.id.seekbar)
    SeekBar seekbar;
    @Bind(R.id.distance)
    TextView distance;
    private Handler mHandler;
    ServiceConnection serviceConnection;
    MusicService.MusicController mycontrol;
    @Override
    protected int getLayoutId() {
        return R.layout.activity_bottom_play;
    }

    @Override
    public void initView() {
        super.initView();
        NotificationCenter.defaultCenter().subscriber(DistanceEvent.class, disevent);
        EventBus.getDefault().register(this);
        mHandler = new Handler();
        Intent intent = new Intent(this, MusicService.class);
        startService(intent);
        if (serviceConnection == null) {
            serviceConnection = new ServiceConnection() {
                @Override
                public void onServiceConnected(ComponentName name, IBinder service) {
                    mycontrol = (MusicService.MusicController) service;
                    Log.i("dddd", "mycontrol");
                    checkPause();
                    //设置进度条的最大长度
                    seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                        @Override
                        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                            if (fromUser) {
                                mycontrol.setPosition(progress);
                                if (!mycontrol.isPlay()) {
                                    mycontrol.play();
                                    play_t.setBackgroundResource(R.drawable.bofang);
                                    mHandler.postDelayed(runnable, 100);
                                }
                            }
                        }

                        @Override
                        public void onStartTrackingTouch(SeekBar seekBar) {

                        }

                        @Override
                        public void onStopTrackingTouch(SeekBar seekBar) {

                        }
                    });
                    //连接之后启动子线程设置当前进度
                    mHandler.postDelayed(runnable, 10);
                    if (mycontrol.isPlay()) {
                        play_t.setBackgroundResource(R.drawable.bofang);
                    }
                }

                @Override
                public void onServiceDisconnected(ComponentName name) {

                }
            };
            //以绑定方式连接服务
            bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE);
        }
    }

    @Override
    public void initData() {
        super.initData();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                EventBus.getDefault().post(new PlayEvent("https://yjly.oss-cn-beijing.aliyuncs.com/yjly/power/144533422789324.mp3"));
            }
        }, 100);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(StartEvent event) {
        if (event.isStart()) {
            int max = (int) mycontrol.getMusicDuration();
            seekbar.setMax(max);
            total_time.setText(NiceUtil.formatTime(mycontrol.getMusicDuration()));
            play_t.setBackgroundResource(R.drawable.bofang);
            mHandler.postDelayed(runnable, 100);
        } else {
            seekbar.setProgress(0);
            mHandler.removeCallbacks(runnable);
            curent_time.setText(NiceUtil.formatTime(0));
            play_t.setBackgroundResource(R.drawable.zanting);
        }
    }

    private void checkPause() {
        if (mycontrol != null && mycontrol.isPlay()) {
            Log.i("dddd", "checkPause");
            mHandler.postDelayed(runnable, 100);
            seekbar.setMax((int) mycontrol.getMusicDuration());
            total_time.setText(NiceUtil.formatTime(mycontrol.getMusicDuration()));
            play_t.setBackgroundResource(R.drawable.bofang);
        } else if (mycontrol != null) {
            PlayBean info= SaveObjectUtils.getInstance(getApplicationContext()).getObject(Constants.PLAY_POSION,null);
            if(info!=null){
                seekbar.setMax(info.getTotalPosition());
                seekbar.setProgress(info.getCurentPosition());
                total_time.setText(NiceUtil.formatTime(info.getTotalPosition()));
                curent_time.setText(NiceUtil.formatTime(info.getCurentPosition()));
                play_t.setBackgroundResource(R.drawable.zanting);
            }

        }
    }


    @OnClick({R.id.play_t,R.id.go_daohang,R.id.go_detail,R.id.main_top,R.id.name_line,R.id.line_top})
    void onclick(View v) {
        switch (v.getId()) {
            case R.id.play_t:
                if (mycontrol == null) {
                    break;
                }
                if (mycontrol.isPlay()) {
                    mycontrol.pause();
                    play_t.setBackgroundResource(R.drawable.zanting);
                    mHandler.removeCallbacks(runnable);
                } else {
                    mycontrol.play();
                    play_t.setBackgroundResource(R.drawable.bofang);
                    mHandler.postDelayed(runnable, 100);
                }
                break;
            case R.id.go_daohang:
                startActivity(new Intent(this, WalkNaviActivity.class));
                break;
            case R.id.go_detail:
            case R.id.name_line:
                startActivity(new Intent(this, DetailDotActivity.class));
                break;
            case R.id.main_top:
                BottomPlayActivity.this.finish();
                break;
            case R.id.line_top:

                break;
        }
    }
    private void setprogress() {
        seekbar.setProgress((int) mycontrol.getPosition());
        if (mycontrol.isPlay()) {
            curent_time.setText(NiceUtil.formatTime((int) mycontrol.getPosition()));
        }
    }

    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            // TODO Auto-generated method stub
            //要做的事情，这里再次调用此Runnable对象，以实现每两秒实现一次的定时器操作
            setprogress();
            mHandler.postDelayed(this, 1000);
        }
    };


    //刷新距离
    DistanceSubscriber disevent = new DistanceSubscriber() {
        @Override
        public void onEvent(DistanceEvent event) {
            distance.setText(event.getDistance());
        }
    };

    @Override
    public void onDestroy() {
        super.onDestroy();
        mHandler.removeCallbacks(runnable);
        NotificationCenter.defaultCenter().unsubscribe(DistanceEvent.class, disevent);
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        checkPause();
    }

    @Override
    public void finish() {
        super.finish();
        this.overridePendingTransition(R.anim.pop_exit_anim,0);
    }
}

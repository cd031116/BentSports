package com.cn.bent.sports.view.activity;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.cn.bent.sports.R;
import com.cn.bent.sports.base.BaseActivity;
import com.cn.bent.sports.bean.PlayBean;
import com.cn.bent.sports.bean.PlayEvent;
import com.cn.bent.sports.bean.PointsEntity;
import com.cn.bent.sports.bean.StartEvent;
import com.cn.bent.sports.utils.Constants;
import com.cn.bent.sports.utils.NiceUtil;
import com.cn.bent.sports.utils.SaveObjectUtils;
import com.cn.bent.sports.view.service.MusicService;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.Bind;
import butterknife.OnClick;

public class DetailDotActivity extends BaseActivity {
    @Bind(R.id.seekbar)
    SeekBar seekBar;
    @Bind(R.id.total_time)
    TextView total_time;
    @Bind(R.id.curent_time)
    TextView curent_time;
    @Bind(R.id.paly_t)
    ImageView paly_t;
    @Bind(R.id.title)
    TextView mtitle;

    private Handler mHandler;
    ServiceConnection serviceConnection;
    MusicService.MusicController mycontrol;
    private  PointsEntity pEnty;
    @Override
    protected int getLayoutId() {
        return R.layout.activity_detail_dot;
    }

    @Override
    public void initView() {
        super.initView();
        pEnty = (PointsEntity) getIntent().getSerializableExtra("enty");
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
                    seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                        @Override
                        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                            if (fromUser) {
                                mycontrol.setPosition(progress);
                                if (!mycontrol.isPlay()) {
                                    mycontrol.play();
                                    paly_t.setBackgroundResource(R.drawable.bofang);
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
                        paly_t.setBackgroundResource(R.drawable.bofang);
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

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(StartEvent event) {
        if (event.isStart()) {
            int max = (int) mycontrol.getMusicDuration();
            seekBar.setMax(max);
            total_time.setText(NiceUtil.formatTime(mycontrol.getMusicDuration()));
            paly_t.setBackgroundResource(R.drawable.bofang);
            mHandler.postDelayed(runnable, 100);
        } else {
            seekBar.setProgress(0);
            mHandler.removeCallbacks(runnable);
            curent_time.setText(NiceUtil.formatTime(0));
            paly_t.setBackgroundResource(R.drawable.zanting);
        }
    }


    @Override
    public void onResume() {
        super.onResume();
            pEnty=SaveObjectUtils.getInstance(getApplicationContext()).getObject(Constants.NOW_POION, null);
        if(pEnty!=null){
            mtitle.setText(pEnty.getName());
        }
    }


    private void checkPause() {
        if (mycontrol != null && mycontrol.isPlay()) {
            Log.i("dddd", "checkPause");
            mHandler.postDelayed(runnable, 100);
            seekBar.setMax((int) mycontrol.getMusicDuration());
            total_time.setText(NiceUtil.formatTime(mycontrol.getMusicDuration()));
            paly_t.setBackgroundResource(R.drawable.bofang);
        } else if (mycontrol != null) {
            PlayBean info= SaveObjectUtils.getInstance(getApplicationContext()).getObject(Constants.PLAY_POSION,null);
           if(info!=null){
               seekBar.setMax(info.getTotalPosition());
               seekBar.setProgress(info.getCurentPosition());
               total_time.setText(NiceUtil.formatTime(info.getTotalPosition()));
               curent_time.setText(NiceUtil.formatTime(info.getCurentPosition()));
               paly_t.setBackgroundResource(R.drawable.zanting);
           }

        }
    }

    private void setprogress() {
        seekBar.setProgress((int) mycontrol.getPosition());
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


    @OnClick({R.id.paly_t})
    void onclick(View v) {
        switch (v.getId()) {
            case R.id.paly_t:
                if (mycontrol == null) {
                    break;
                }
                if (mycontrol.isPlay()) {
                    mycontrol.pause();
                    paly_t.setBackgroundResource(R.drawable.zanting);
                    mHandler.removeCallbacks(runnable);
                } else if(mycontrol.isHave()){
                    mycontrol.play();
                    paly_t.setBackgroundResource(R.drawable.bofang);
                    mHandler.postDelayed(runnable, 100);
                }
                break;
        }
    }

    @Override
    public void initData() {
        super.initData();
        if(pEnty!=null){
            mtitle.setText(pEnty.getName());
        }

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        mHandler.removeCallbacks(runnable);
        unbindService(serviceConnection);
    }
}

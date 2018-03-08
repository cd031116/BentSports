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
import com.cn.bent.sports.bean.PlayEvent;
import com.cn.bent.sports.utils.NiceUtil;
import com.cn.bent.sports.view.service.MusicService;

import org.greenrobot.eventbus.EventBus;

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

    private Handler mHandler;
    ServiceConnection serviceConnection;
    MusicService.MusicController mycontrol;
    @Override
    protected int getLayoutId() {
        return R.layout.activity_detail_dot;
    }

    @Override
    public void initView() {
        super.initView();
        Intent intent = new Intent(this,MusicService.class);
        startService(intent);
        EventBus.getDefault().post(new PlayEvent("https://yjly.oss-cn-beijing.aliyuncs.com/yjly/power/144533422789324.mp3"));
        if (serviceConnection == null) {
            serviceConnection = new ServiceConnection() {
                @Override
                public void onServiceConnected(ComponentName name, IBinder service) {
                    mycontrol = (MusicService.MusicController) service;
                    mycontrol.setPatss("https://yjly.oss-cn-beijing.aliyuncs.com/yjly/power/144533422789324.mp3",false);
                    //设置进度条的最大长度
                    int max =(int) mycontrol.getMusicDuration();
                    total_time.setText(NiceUtil.formatTime(mycontrol.getMusicDuration()));
                    changeview();
                    seekBar.setMax(max);
                    seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                        @Override
                        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                            if(fromUser){
                                mycontrol.setPosition(progress);
                                if(!mycontrol.isPlay()){
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
                    mHandler.postDelayed(runnable, 1000);
                    //连接之后启动子线程设置当前进度
                    Log.i("tttt","mHandler=");
                }
                @Override
                public void onServiceDisconnected(ComponentName name) {

                }
            };
            //以绑定方式连接服务
            bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE);
        }

    }

    private void setprogress(){

        seekBar.setProgress((int)mycontrol.getPosition());
      if(mycontrol.isPlay()){
          curent_time.setText(NiceUtil.formatTime((int)mycontrol.getPosition()));
      }
        if((mycontrol.getMusicDuration()-mycontrol.getPosition())<=800){
            seekBar.setProgress(0);
            Log.i("tttt","removeCallbacks=");
            mHandler.removeCallbacks(runnable);
            changeview();
        }
    }

    Runnable runnable=new Runnable(){
        @Override
        public void run() {
            // TODO Auto-generated method stub
            //要做的事情，这里再次调用此Runnable对象，以实现每两秒实现一次的定时器操作
            setprogress();
            mHandler.postDelayed(this, 1000);
        }
    };

private void changeview(){
    new Handler().postDelayed(new Runnable() {
        @Override
        public void run() {
            curent_time.setText(NiceUtil.formatTime(0));
            if(mycontrol.isPlay()){
                paly_t.setBackgroundResource(R.drawable.bofang);
            }else {
                paly_t.setBackgroundResource(R.drawable.zanting);
            }
        }
    },600);

}

    @OnClick({R.id.paly_t})
    void onclick(View v){
        switch (v.getId()){
            case R.id.paly_t:
                if(mycontrol==null){break;}
                if(mycontrol.isPlay()){
                    mycontrol.pause();
                    paly_t.setBackgroundResource(R.drawable.zanting);
                    mHandler.removeCallbacks(runnable);
                }else {
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
        mHandler=new Handler();
    }
    @Override
    public void onDestroy() {
        super.onDestroy();

    }
}

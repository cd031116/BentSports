package com.cn.bent.sports.view.service;

import android.app.Service;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import com.cn.bent.sports.base.BaseConfig;
import com.cn.bent.sports.bean.PlayBean;
import com.cn.bent.sports.bean.PlayEvent;
import com.cn.bent.sports.bean.StartEvent;
import com.cn.bent.sports.utils.Constants;
import com.cn.bent.sports.utils.SaveObjectUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

/**
 * Created by lyj on 2018/3/7 0007.
 * description
 */

public class MusicService extends Service {

    private MediaPlayer mPlayer;
    private boolean isHave = false;

    public MusicService() {
    }

    /*
    * 绑定服务的实现流程：
    * 1.服务 onCreate， onBind， onDestroy 方法
    * 2.onBind 方法需要返回一个 IBinder 对象
    * 3.如果 Activity 绑定，Activity 就可以取到 IBinder 对象，可以直接调用对象的方法
    */

    // 相同应用内部不同组件绑定，可以使用内部类以及Binder对象来返回。
    public class MusicController extends Binder {

        public boolean isPlay() {
            return mPlayer.isPlaying();//正在播放
        }

        public void play() {
            mPlayer.start();//开启音乐
            SaveObjectUtils.getInstance(getApplicationContext()).setObject(Constants.PLAY_POSION, null);
        }

        public boolean isHave() {
            return isHave;
        }

        public void pause() {
            mPlayer.pause();//暂停音乐
            Log.i("dddd", "mPlayer.pause");
            PlayBean bean = new PlayBean();
            bean.setTotalPosition(mPlayer.getDuration());
            bean.setCurentPosition(mPlayer.getCurrentPosition());
            SaveObjectUtils.getInstance(getApplicationContext()).setObject(Constants.PLAY_POSION, bean);
        }

        public void stop() {
            mPlayer.stop();//暂停音乐
        }

        public long getMusicDuration() {
            return mPlayer.getDuration();//获取文件的总长度
        }

        public long getPosition() {
            return mPlayer.getCurrentPosition();//获取当前播放进度
        }

        public void setPosition(int position) {
            mPlayer.seekTo(position);//重新设定播放进度
        }

        public void setPatss(String paths, boolean ischange) {
            played(paths, ischange);
        }

    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(PlayEvent event) {
        played(event.getPaths(), event.isHuan());
        Log.i("dddd", "onEvent");
    }


    private void played(final String paths, boolean qiehuan) {
        if (mPlayer == null) {
            mPlayer = new MediaPlayer();
        }
        if (mPlayer.isPlaying() && qiehuan) {
            mPlayer.stop();
            mPlayer.reset();
        }
        if (mPlayer.isPlaying() && !qiehuan) {
            return;
        }
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    mPlayer.setDataSource(paths);
                    BaseConfig bg=BaseConfig.getInstance(getApplicationContext());
                    bg.setStringValue(Constants.NOW_PLAY,paths);
                    isHave = true;
                    mPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
                    mPlayer.prepare();
                    mPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                        @Override
                        public void onPrepared(MediaPlayer mp) {
                            mPlayer.start();
                            Log.i("dddd", "onPrepared");
                            EventBus.getDefault().post(new StartEvent(true));
                        }

                    });
                    mPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                        @Override
                        public void onCompletion(MediaPlayer mp) {
                            EventBus.getDefault().post(new StartEvent(false));
                            SaveObjectUtils.getInstance(getApplicationContext()).setObject(Constants.PLAY_POSION, null);
                            if (mPlayer.isPlaying()) {
                                mPlayer.stop();
                            }
                            mPlayer.release();
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }


            }
        }).start();


    }


    /**
     * 当绑定服务的时候，自动回调这个方法
     * 返回的对象可以直接操作Service内部的内容
     *
     * @param intent
     * @return
     */
    @Override
    public IBinder onBind(Intent intent) {
        return new MusicController();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        EventBus.getDefault().register(this);
        if (mPlayer == null) {
            mPlayer = new MediaPlayer();
        }
    }

    /**
     * 任意一次unbindService()方法，都会触发这个方法
     * 用于释放一些绑定时使用的资源
     *
     * @param intent
     * @return
     */
    @Override
    public boolean onUnbind(Intent intent) {
        return super.onUnbind(intent);
    }

    @Override
    public void onDestroy() {
        Log.i("dddd", "onDestroy=service");
        EventBus.getDefault().unregister(this);
        if (mPlayer.isPlaying()) {
            mPlayer.stop();
        }
        mPlayer.release();
        mPlayer = null;
        SaveObjectUtils.getInstance(getApplicationContext()).setObject(Constants.PLAY_POSION, null);
        super.onDestroy();
    }

}

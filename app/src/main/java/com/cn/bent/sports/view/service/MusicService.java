package com.cn.bent.sports.view.service;

import android.app.Service;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import com.cn.bent.sports.base.BaseConfig;
import com.cn.bent.sports.base.MyApplication;
import com.cn.bent.sports.bean.PlayBean;
import com.cn.bent.sports.bean.PlayEvent;
import com.cn.bent.sports.bean.StartEvent;
import com.cn.bent.sports.database.TaskCationManager;
import com.cn.bent.sports.utils.Constants;
import com.cn.bent.sports.utils.ImageUtils;
import com.cn.bent.sports.utils.SaveObjectUtils;
import com.danikula.videocache.HttpProxyCacheServer;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import tv.danmaku.ijk.media.player.IMediaPlayer;
import tv.danmaku.ijk.media.player.IjkMediaPlayer;

/**
 * Created by lyj on 2018/3/7 0007.
 * description
 */

public class MusicService extends Service {
    private IjkMediaPlayer mPlayer;
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
            boolean isplay=false;
            try {
                isplay=  mPlayer.isPlaying();
            }catch (Exception e){

            }
            return isplay;//正在播放
        }

        public void play() {
            try {
                mPlayer.start();//开启音乐
            }catch (Exception e){

            }

            SaveObjectUtils.getInstance(getApplicationContext()).setObject(Constants.PLAY_POSION, null);
        }

        public boolean isHave() {
            return isHave;
        }

        public void pause() {
            mPlayer.pause();//暂停音乐
            Log.i("dddd", "mPlayer.pause");
            PlayBean bean = new PlayBean();
            bean.setTotalPosition((int)mPlayer.getDuration());
            bean.setCurentPosition((int)mPlayer.getCurrentPosition());
            SaveObjectUtils.getInstance(getApplicationContext()).setObject(Constants.PLAY_POSION, bean);
        }

        public void stop() {
            mPlayer.stop();//暂停音乐
        }

        public long getMusicDuration() {
            long total=0;
            try {
                total=mPlayer.getDuration();//开启音乐
            }catch (Exception e){

            }
            return total;//获取当前播放进度
        }

        public long getPosition() {
            long curent=0;
            try {
                curent=mPlayer.getCurrentPosition();//开启音乐
            }catch (Exception e){

            }
            return curent;//获取当前播放进度
        }

        public void setPosition(int position) {
            mPlayer.seekTo(position);//重新设定播放进度
        }

        public void setPatss(String paths, boolean ischange ,String namse) {
            played(paths, ischange);
        }

    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(PlayEvent event) {
        Log.i("dddd", "onEvent");
        played(event.getPaths(), event.isHuan());
    }


    private void played(final String paths, boolean qiehuan) {
        if (mPlayer == null) {
            mPlayer = new IjkMediaPlayer();
        }
        if (qiehuan) {
            mPlayer.reset();
        }
        if (mPlayer.isPlaying() && !qiehuan) {
            return;
        }
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    mPlayer.reset();
                    HttpProxyCacheServer proxy = MyApplication.getProxy(getApplicationContext());
                    String proxyUrl = proxy.getProxyUrl(ImageUtils.getImageUrl(paths));
                    mPlayer.setDataSource(proxyUrl);
                    Log.i("dddd", "setDataSource="+ImageUtils.getImageUrl(paths));
                    isHave = true;
                    mPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
                    mPlayer.prepareAsync();
                    mPlayer.setOnPreparedListener(new IMediaPlayer.OnPreparedListener() {
                        @Override
                        public void onPrepared(IMediaPlayer iMediaPlayer) {
                            Log.i("dddd", "onPrepared");
                            mPlayer.start();
                            TaskCationManager.sethavePlay(paths);
                            EventBus.getDefault().post(new StartEvent(true,paths));
                        }
                    });
                    mPlayer.setOnCompletionListener(new IMediaPlayer.OnCompletionListener() {
                        @Override
                        public void onCompletion(IMediaPlayer iMediaPlayer) {
                            if (mPlayer.isPlaying()) {
                                mPlayer.stop();
                            }
                            Log.i("dddd", "setOnCompletionListener");
                            EventBus.getDefault().post(new StartEvent(false,paths));
                            TaskCationManager.updatePlay(paths);
                            SaveObjectUtils.getInstance(getApplicationContext()).setObject(Constants.PLAY_POSION, null);
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
            mPlayer = new IjkMediaPlayer();
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
        if (mPlayer != null) {
            mPlayer.stop();
            mPlayer.release();
            mPlayer = null;
        }
        SaveObjectUtils.getInstance(getApplicationContext()).setObject(Constants.PLAY_POSION, null);
        super.onDestroy();
    }

}

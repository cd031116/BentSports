package com.cn.bent.sports.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.cn.bent.sports.MainActivity;
import com.cn.bent.sports.R;
import com.cn.bent.sports.api.BaseApi;
import com.cn.bent.sports.base.BaseActivity;
import com.cn.bent.sports.bean.AddScoreEntity;
import com.cn.bent.sports.bean.GameEntity;
import com.cn.bent.sports.bean.LoginBase;
import com.cn.bent.sports.bean.ReFreshEvent;
import com.cn.bent.sports.database.TaskCationManager;
import com.cn.bent.sports.utils.Constants;
import com.cn.bent.sports.utils.DataUtils;
import com.cn.bent.sports.utils.SaveObjectUtils;
import com.cn.bent.sports.utils.ToastUtils;
import com.zhl.network.RxObserver;
import com.zhl.network.RxSchedulers;
import com.zhl.network.huiqu.HuiquRxFunction;
import com.zhl.network.huiqu.HuiquRxTBFunction;


import org.greenrobot.eventbus.EventBus;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by dawn on 2018/1/31.
 */

public class ContinueActivity extends BaseActivity {

    @Bind(R.id.card_img)
    ImageView card_img;
    @Bind(R.id.scord)
    TextView scord;
    @Bind(R.id.go_ahead)
    TextView go_ahead;

    private String uid;
    private int score;
    private int gameId;
    private int MAX_REQUEST = 2;
    private int isRequestNum = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.finish_game_layout;
    }

    @Override
    public void initView() {
        super.initView();
        GameEntity game = (GameEntity) getIntent().getSerializableExtra("game");
        uid = game.getUid();
        score = game.getScord();
        gameId = game.getGameId();
    }

    private void setView(int scores, int game_id) {
        scord.setText(scores + "");
        switch (game_id) {
            case 1:
                card_img.setBackground(getResources().getDrawable(R.drawable.hongbaoyu));
                break;
            case 2:
                card_img.setBackground(getResources().getDrawable(R.drawable.liunianshou));
                break;
            case 3:
                card_img.setBackground(getResources().getDrawable(R.drawable.diandenglong));
                break;
            case 4:
                card_img.setBackground(getResources().getDrawable(R.drawable.jixiangqian));
                break;
            case 5:
                card_img.setBackground(getResources().getDrawable(R.drawable.caidengmi));
                break;
        }
    }

    @Override
    public void initData() {
        super.initData();
        addScroe();
    }

    private void addScroe() {
        showAlert("正在上传积分。。", true);
        BaseApi.getDefaultService(this).addScore(uid, score, gameId)
                .map(new HuiquRxTBFunction<AddScoreEntity>())
                .compose(RxSchedulers.<AddScoreEntity>io_main())
                .subscribe(new RxObserver<AddScoreEntity>(this, "addScore", 1, false) {
                    @Override
                    public void onSuccess(int whichRequest, AddScoreEntity addScoreEntity) {
                        if (addScoreEntity.getBody().getAddStatus() == 1) {
                            setView(score, gameId);
                            dismissAlert();
                            TaskCationManager.update(gameId + "", DataUtils.getlongs());
                            LoginBase user = (LoginBase) SaveObjectUtils.getInstance(ContinueActivity.this).getObject(Constants.USER_INFO, null);
                            setScore(user);
                            EventBus.getDefault().post(new ReFreshEvent());
                        } else {
                            ToastUtils.showShortToast(ContinueActivity.this, addScoreEntity.getMsg());
                            dismissAlert();
                        }
                        isRequestNum = 1;
                    }

                    @Override
                    public void onError(int whichRequest, Throwable e) {
                        dismissAlert();
                        ToastUtils.showShortToast(ContinueActivity.this, "积分上传失败");
                        if (isRequestNum >= MAX_REQUEST) {
                            isRequestNum++;
                            addScroe();
                        }
                    }
                });
    }

    @OnClick(R.id.go_ahead)
    void onClick(View view) {
        startActivity(new Intent(ContinueActivity.this, MainActivity.class));
    }

    private void setScore(LoginBase user) {
        if (user.getScore() != null)
            user.setScore(Integer.parseInt(user.getScore()) + score + "");
        else
            user.setScore(score + "");
        SaveObjectUtils.getInstance(ContinueActivity.this).setObject(Constants.USER_INFO, user);
    }

    @Override
    public void onBackPressed() {
    }
}

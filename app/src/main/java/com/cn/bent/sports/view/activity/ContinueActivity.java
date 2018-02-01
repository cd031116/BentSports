package com.cn.bent.sports.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.cn.bent.sports.MainActivity;
import com.cn.bent.sports.R;
import com.cn.bent.sports.api.BaseApi;
import com.cn.bent.sports.base.BaseActivity;
import com.cn.bent.sports.bean.AddScoreEntity;
import com.cn.bent.sports.bean.GameEntity;
import com.zhl.network.RxObserver;
import com.zhl.network.RxSchedulers;
import com.zhl.network.huiqu.HuiquRxFunction;


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
        scord.setText(score + "");
        switch (game.getGameId()) {
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
                card_img.setBackground(getResources().getDrawable(R.drawable.liunianshou));
                break;
            case 5:
                card_img.setBackground(getResources().getDrawable(R.drawable.caidengmi));
                break;
        }
    }

    @Override
    public void initData() {
        super.initData();
    }

    @OnClick(R.id.go_ahead)
    void onClick(View view) {
        BaseApi.getDefaultService(this).addScore(uid, score,gameId)
                .map(new HuiquRxFunction<AddScoreEntity>())
                .compose(RxSchedulers.<AddScoreEntity>io_main())
                .subscribe(new RxObserver<AddScoreEntity>(this, "addScore", 1, false) {
                    @Override
                    public void onSuccess(int whichRequest, AddScoreEntity addScoreEntity) {
                        if (addScoreEntity.getAddStatus() == 1) {
                            startActivity(new Intent(ContinueActivity.this, MainActivity.class));
                        }else
                            startActivity(new Intent(ContinueActivity.this, MainActivity.class));
                    }

                    @Override
                    public void onError(int whichRequest, Throwable e) {
                        startActivity(new Intent(ContinueActivity.this, MainActivity.class));
                    }
                });
    }

    @Override
    public void onBackPressed() {
    }
}

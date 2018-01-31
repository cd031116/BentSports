package com.cn.bent.sports.view.activity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.cn.bent.sports.R;
import com.cn.bent.sports.api.BaseApi;
import com.cn.bent.sports.base.BaseActivity;
import com.cn.bent.sports.bean.AddScoreEntity;
import com.cn.bent.sports.bean.GameEntity;
import com.zhl.network.RxObserver;
import com.zhl.network.RxSchedulers;
import com.zhl.network.huiqu.HuiquRxFunction;

import java.io.Serializable;

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

    private String  uid;
    private int  score;

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
        uid=game.getUid();
        score=game.getScord();
        scord.setText(score+"");
        switch (game.getGameId()){
            case 2:
                card_img.setBackground(getResources().getDrawable(R.drawable.liunianshou));
                break;
        }
    }

    @Override
    public void initData() {
        super.initData();
    }

    @OnClick(R.id.go_ahead)
    void onClick(View view){
        BaseApi.getDefaultService(this).addScore(uid,score)
                .map(new HuiquRxFunction<AddScoreEntity>())
                .compose(RxSchedulers.<AddScoreEntity>io_main())
                .subscribe(new RxObserver<AddScoreEntity>(this,"addScore",1,false) {
                    @Override
                    public void onSuccess(int whichRequest, AddScoreEntity addScoreEntity) {
                        if (addScoreEntity.getAddStatus()==1){
                            Log.e("dasa", "onSuccess: 上传成功");
                        }
                    }

                    @Override
                    public void onError(int whichRequest, Throwable e) {

                    }
                });
    }
}

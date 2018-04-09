package com.cn.bent.sports.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cn.bent.sports.MainActivity;
import com.cn.bent.sports.R;
import com.cn.bent.sports.base.BaseActivity;
import com.cn.bent.sports.bean.AllFinishEntity;
import com.cn.bent.sports.bean.LookRankEvent;
import com.cn.bent.sports.recyclebase.CommonAdapter;
import com.cn.bent.sports.recyclebase.ViewHolder;

import org.greenrobot.eventbus.EventBus;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by dawn on 2018/2/2.
 */

public class AllFinishActivity extends BaseActivity {

    @Bind(R.id.all_time)
    TextView all_time;
    @Bind(R.id.all_jifen)
    TextView all_jifen;
    @Bind(R.id.shensu_name)
    TextView shensu_name;
    @Bind(R.id.shensu_jifen)
    TextView shensu_jifen;
    @Bind(R.id.shensu)
    RelativeLayout shensu;
    @Bind(R.id.game_list)
    RecyclerView game_list;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.all_finished_layout;
    }

    @Override
    public void initView() {
        super.initView();
        game_list.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    public void initData() {
        super.initData();
    }

    private void setRecView(AllFinishEntity allFinishEntity) {
        if (!TextUtils.isEmpty(allFinishEntity.getDone_time()))
            all_time.setText("通关时间：" + allFinishEntity.getDone_time());
        all_jifen.setText(String.valueOf(allFinishEntity.getTotal_score()));
        CommonAdapter<AllFinishEntity.GameRecordBean> mAdapter = new CommonAdapter<AllFinishEntity.GameRecordBean>(this, R.layout.item_all_finish, allFinishEntity.getGame_record()) {
            @Override
            protected void convert(ViewHolder holder, AllFinishEntity.GameRecordBean gameRecordBean, int position) {
                holder.setText(R.id.game_jifen, gameRecordBean.getScore() + "");
                holder.setText(R.id.game_name, gameRecordBean.getName());
            }
        };
        game_list.setAdapter(mAdapter);
    }

    @OnClick(R.id.look_range)
    void onClick() {
        startActivity(new Intent(this, MainActivity.class));
        EventBus.getDefault().post(new LookRankEvent());
    }
}

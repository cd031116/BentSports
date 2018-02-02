package com.cn.bent.sports.view.activity;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import com.cn.bent.sports.R;
import com.cn.bent.sports.api.BaseApi;
import com.cn.bent.sports.base.BaseActivity;
import com.cn.bent.sports.bean.AllFinishEntity;
import com.cn.bent.sports.bean.LoginBase;
import com.cn.bent.sports.recyclebase.CommonAdapter;
import com.cn.bent.sports.recyclebase.ViewHolder;
import com.cn.bent.sports.utils.Constants;
import com.cn.bent.sports.utils.SaveObjectUtils;
import com.zhl.network.RxObserver;
import com.zhl.network.RxSchedulers;
import com.zhl.network.huiqu.HuiquRxFunction;

import butterknife.Bind;

/**
 * Created by dawn on 2018/2/2.
 */

public class AllFinishActivity extends BaseActivity {

    @Bind(R.id.all_time)
    TextView all_time;
    @Bind(R.id.all_jifen)
    TextView all_jifen;
    @Bind(R.id.game_list)
    RecyclerView game_list;

    private LoginBase user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int getLayoutId() {
        return 0;
    }

    @Override
    public void initView() {
        super.initView();
        user = (LoginBase) SaveObjectUtils.getInstance(this).getObject(Constants.USER_INFO, null);
        game_list.setLayoutManager(new LinearLayoutManager(this));
        all_time.setText("通关时间：" + getIntent().getLongExtra("time", 0));
    }

    @Override
    public void initData() {
        super.initData();
        BaseApi.getDefaultService(this).getGameRecord(user.getMember_id()).map(new HuiquRxFunction<AllFinishEntity>())
                .compose(RxSchedulers.<AllFinishEntity>io_main())
                .subscribe(new RxObserver<AllFinishEntity>(this, "getGameRecord", 1, false) {
                    @Override
                    public void onSuccess(int whichRequest, AllFinishEntity allFinishEntity) {
                        if (allFinishEntity.getGame_record() != null && allFinishEntity.getGame_record().size() > 0)
                            setRecView(allFinishEntity);
                    }

                    @Override
                    public void onError(int whichRequest, Throwable e) {

                    }
                });
    }

    private void setRecView(AllFinishEntity allFinishEntity) {
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
}

package com.cn.bent.sports.widget;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cn.bent.sports.R;
import com.cn.bent.sports.bean.GameTeamScoreEntity;
import com.cn.bent.sports.bean.LoginBase;
import com.cn.bent.sports.recyclebase.CommonAdapter;
import com.cn.bent.sports.recyclebase.ViewHolder;
import com.cn.bent.sports.utils.SupportMultipleScreensUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dawn on 2018/3/24.
 */

public class OneTaskFinishDialog extends Dialog implements View.OnClickListener{
    private  OnClickListener onClickListener;
    private RecyclerView game_list;
    private TextView game_name;
    private TextView game_finish_num;
    private Context mContext;
    private LinearLayout dialog_layout;
    private List<GameTeamScoreEntity> gameTeamScoreEntityList;


    public OneTaskFinishDialog(Context context) {
        super(context);
        this.mContext = context;
    }
    public OneTaskFinishDialog(Context context, int themeResId) {
        super(context,themeResId);
        this.mContext = context;
    }


    public OneTaskFinishDialog(Context context, int themeResId, OnClickListener onClickListener) {
        super(context, themeResId);
        this.mContext = context;
        this.onClickListener=onClickListener;
    }


    public OneTaskFinishDialog setListData(List<GameTeamScoreEntity> gameTeamScoreEntityList) {
        this.gameTeamScoreEntityList = gameTeamScoreEntityList;
        return this;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.one_task_dialog_layout);
        View rootView = findViewById(android.R.id.content);
        SupportMultipleScreensUtil.init(mContext);
        SupportMultipleScreensUtil.scale(rootView);
        setCanceledOnTouchOutside(false);
        setCancelable(false);
        initView();
    }

    private void initView() {
        game_list = (RecyclerView) findViewById(R.id.game_list);
        game_name = (TextView) findViewById(R.id.game_name);
        game_finish_num = (TextView) findViewById(R.id.game_finish_num);
        RelativeLayout white_layout = (RelativeLayout) findViewById(R.id.white_layout);
         dialog_layout = (LinearLayout) findViewById(R.id.dialog_layout);
        white_layout.setOnClickListener(null);
        dialog_layout.setOnClickListener(this);
        game_name.setOnClickListener(this);

        game_list.setLayoutManager(new LinearLayoutManager(mContext));

        if (gameTeamScoreEntityList != null&&gameTeamScoreEntityList.size()>0) {
            CommonAdapter mAdapter = new CommonAdapter<GameTeamScoreEntity>(mContext, R.layout.one_task_finish_item, gameTeamScoreEntityList) {
                @Override
                protected void convert(ViewHolder holder, GameTeamScoreEntity s, int position) {
                    holder.setText(R.id.item_name, s.getScore()+"");
                }
            };
            game_list.setAdapter(mAdapter);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.game_name:
                onClickListener.onClick(this,1);
                break;
            case R.id.dialog_layout:
                onClickListener.onClick(this,2);
                break;
        }
    }


    public interface OnClickListener {
        void onClick(Dialog dialog, int index);
    }
}



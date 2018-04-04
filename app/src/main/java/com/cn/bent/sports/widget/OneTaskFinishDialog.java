package com.cn.bent.sports.widget;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.cn.bent.sports.R;
import com.cn.bent.sports.api.BaseApi;
import com.cn.bent.sports.bean.GameTeamScoreEntity;
import com.cn.bent.sports.bean.LoginBase;
import com.cn.bent.sports.bean.MemberDataEntity;
import com.cn.bent.sports.bean.TeamGame;
import com.cn.bent.sports.database.PlayUserManager;
import com.cn.bent.sports.recyclebase.CommonAdapter;
import com.cn.bent.sports.recyclebase.ViewHolder;
import com.cn.bent.sports.utils.SupportMultipleScreensUtil;
import com.cn.bent.sports.view.activity.youle.PlayActivity;
import com.cn.bent.sports.view.activity.youle.play.MemberEditActivity;
import com.vondear.rxtools.view.RxToast;
import com.zhl.network.RxObserver;
import com.zhl.network.RxSchedulers;
import com.zhl.network.huiqu.JavaRxFunction;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by dawn on 2018/3/24.
 */

public class OneTaskFinishDialog extends Dialog implements View.OnClickListener {
    private OnClickListener onClickListener;
    private RecyclerView game_list;
    private TextView game_name;
    private TextView game_finish_num;
    private Context mContext;
    private LinearLayout dialog_layout;
    private List<GameTeamScoreEntity> gameTeamScoreEntityList;
    private TeamGame teamGame;
    long gamePointId;

    public OneTaskFinishDialog(Context context) {
        super(context);
        this.mContext = context;
    }

    public OneTaskFinishDialog(Context context, int themeResId) {
        super(context, themeResId);
        this.mContext = context;
    }


    public OneTaskFinishDialog(Context context, int themeResId, OnClickListener onClickListener) {
        super(context, themeResId);
        this.mContext = context;
        this.onClickListener = onClickListener;
    }


    public OneTaskFinishDialog setListData(TeamGame teamId, long gamePointId) {
        this.teamGame = teamId;
        this.gamePointId = gamePointId;
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
        setRecyData(teamGame, gamePointId);
    }

    private void setRecyData(final TeamGame teamGame, long gamePointId) {
        final List<MemberDataEntity> history = PlayUserManager.getHistory();
        BaseApi.getJavaLoginDefaultService(mContext).getPointTask(teamGame.getGameId(), gamePointId)
                .map(new JavaRxFunction<List<GameTeamScoreEntity>>())
                .compose(RxSchedulers.<List<GameTeamScoreEntity>>io_main())
                .subscribe(new RxObserver<List<GameTeamScoreEntity>>(mContext, "getPointTask", 1, false) {
                    @Override
                    public void onSuccess(int whichRequest, List<GameTeamScoreEntity> gameTeamScoreEntities) {
                        if (gameTeamScoreEntities != null && gameTeamScoreEntities.size() > 0) {
                            int finish_num = ((teamGame.getPassRate() * teamGame.getTeamMemberMax()) / 100) - gameTeamScoreEntities.size();
                            if (finish_num > 0) {
                                game_finish_num.setVisibility(View.VISIBLE);
                                String finish_num_str = "还需" + finish_num + "人完成";
                                SpannableStringBuilder builder = new SpannableStringBuilder(finish_num_str);
                                builder.setSpan(new ForegroundColorSpan(mContext.getResources().getColor(R.color.color_fd7d6f)), 2, finish_num_str.length() - 3, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                                game_finish_num.setText(builder);
                            } else
                                game_finish_num.setVisibility(View.GONE);
                            for (GameTeamScoreEntity gameTeamScoreEntity : gameTeamScoreEntities) {
                                for (MemberDataEntity memberDataEntity : history) {
                                    if (gameTeamScoreEntity.getUserId() == memberDataEntity.getUserId()) {
                                        memberDataEntity.setScore(gameTeamScoreEntity.getScore());
                                        break;
                                    }
                                }
                            }
                            compareDaXiao(history);
                            setRecyView(history);
                        }
                    }

                    @Override
                    public void onError(int whichRequest, Throwable e) {
                        RxToast.error(e.getMessage());
                    }
                });
    }

    /**
     * 从大到小排序
     *
     * @param history
     */
    private void compareDaXiao(List<MemberDataEntity> history) {
        Collections.sort(history, new Comparator<MemberDataEntity>() {
            @Override
            public int compare(MemberDataEntity user1, MemberDataEntity user2) {
                Integer id1 = user1.getScore();
                Integer id2 = user2.getScore();
                //可以按User对象的其他属性排序，只要属性支持compareTo方法
                return id2.compareTo(id1);
            }
        });
    }

    private void setRecyView(List<MemberDataEntity> historyList) {
        game_list.setLayoutManager(new LinearLayoutManager(mContext));
        if (historyList != null && historyList.size() > 0) {
            CommonAdapter mAdapter = new CommonAdapter<MemberDataEntity>(mContext, R.layout.one_task_finish_item, historyList) {
                @Override
                protected void convert(ViewHolder holder, MemberDataEntity memberDataEntity, int position) {
                    if (memberDataEntity.getScore() == 0)
                        holder.setText(R.id.item_score, "未完成");
                    else
                        holder.setText(R.id.item_score, memberDataEntity.getScore() + "");
                    holder.setText(R.id.item_name, memberDataEntity.getNickname() + "");
                    ImageView view = (ImageView) holder.getView(R.id.item_img);
                    Glide.with(mContext).load(memberDataEntity.getAvatar()).into(view);

                }
            };
            game_list.setAdapter(mAdapter);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.game_name:
                onClickListener.onClick(this, 1);
                break;
            case R.id.dialog_layout:
                onClickListener.onClick(this, 2);
                break;
        }
    }


    public interface OnClickListener {
        void onClick(Dialog dialog, int index);
    }
}



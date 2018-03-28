package com.cn.bent.sports.view.activity.youle;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;

import com.cn.bent.sports.R;
import com.cn.bent.sports.view.activity.StartActivity;
import com.cn.bent.sports.widget.CompletionDialog;
import com.cn.bent.sports.widget.GameErrorDialog;
import com.cn.bent.sports.widget.GameFailDialog;
import com.cn.bent.sports.widget.OneTaskFinishDialog;
import com.cn.bent.sports.widget.TaskFinishDialog;
import com.cn.bent.sports.widget.TimeOverDialog;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dawn on 2018/3/28.
 */

public class DialogManager {

    private static DialogManager instance = null;
    private Context mContext;

    private DialogManager(Context context) {
        this.mContext = context;
    }

    public static DialogManager getInstance(Context context) {
        synchronized (DialogManager.class) {
            if (instance == null) {
                instance = new DialogManager(context);
            }
        }
        return instance;
    }

    /**
     * 路线单任务完成
     */
    public void showOneFinishDialog(List<String> mlist) {
        new OneTaskFinishDialog(mContext, R.style.dialog, new OneTaskFinishDialog.OnClickListener() {
            @Override
            public void onClick(Dialog dialog, int index) {
                dialog.dismiss();
            }
        }).setListData(mlist).show();
    }

    /**
     * 路线_提示弹窗
     */
    public void showPromptDialog() {
        new GameErrorDialog(mContext, R.style.dialog, new GameErrorDialog.OnCloseListener() {
            @Override
            public void onClick(Dialog dialog, int index) {
                dialog.dismiss();
            }
        }).show();
    }

    /**
     * 路线_时间到dialog
     */
    public void showTimeOverDialog(String time,String score) {
        new TimeOverDialog(mContext, R.style.dialog, new TimeOverDialog.OnCloseListener() {
            @Override
            public void onClick(Dialog dialog, int index) {
                dialog.dismiss();
                if (index == 1)
                    mContext.startActivity(new Intent(mContext, RankingListActivity.class));
            }
        }).setTime(time).setScore(score).show();
    }

    /**
     * 路线_任务失败
     */
    public void showTaskFailDialog(String name,String score) {
        new GameFailDialog(mContext, R.style.dialog, new GameFailDialog.OnCloseListener() {
            @Override
            public void onClick(Dialog dialog, String index) {
                dialog.dismiss();
            }
        }).setName(name).setScore(score).show();
    }

    /**
     * 路线_任务通关
     */
    public void showTaskOneFinishDialog(String name,String score) {
        new CompletionDialog(mContext, R.style.dialog, new CompletionDialog.OnCloseListener() {
            @Override
            public void onClick(Dialog dialog, String index) {
                dialog.dismiss();
            }
        }).setName(name).setScore(score).show();
    }

    /**
     * 路线_任务完成
     */
    public void showTaskAllFinishDialog(String time,String score) {
        new TaskFinishDialog(mContext, R.style.dialog, new TaskFinishDialog.OnCloseListener() {
            @Override
            public void onClick(Dialog dialog, String index) {
                dialog.dismiss();
            }
        }).setTime(time).setScore(score).show();
    }
}

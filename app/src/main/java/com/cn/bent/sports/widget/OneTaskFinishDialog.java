package com.cn.bent.sports.widget;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.cn.bent.sports.R;
import com.cn.bent.sports.recyclebase.CommonAdapter;
import com.cn.bent.sports.recyclebase.ViewHolder;
import com.cn.bent.sports.utils.SupportMultipleScreensUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dawn on 2018/3/24.
 */

public class OneTaskFinishDialog extends Dialog {
    private RecyclerView game_list;
    private TextView game_name;
    private TextView game_finish_num;
    private Context mContext;


    public OneTaskFinishDialog(Context context) {
        super(context);
        this.mContext = context;
    }


    public OneTaskFinishDialog(Context context, int themeResId) {
        super(context, themeResId);
        this.mContext = context;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.game_error_dialog_layout);
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

        game_list.setLayoutManager(new LinearLayoutManager(mContext));
        List<String> mList = new ArrayList<>();
        mList.add("小拓");
        CommonAdapter mAdapter = new CommonAdapter<String>(mContext, R.layout.one_task_finish_item, mList) {
            @Override
            protected void convert(ViewHolder holder, String s, int position) {
                holder.setText(R.id.item_name, s);
            }
        };
        game_list.setAdapter(mAdapter);

    }

}



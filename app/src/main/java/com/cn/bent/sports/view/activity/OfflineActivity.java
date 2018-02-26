package com.cn.bent.sports.view.activity;

import android.content.Intent;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.cn.bent.sports.R;
import com.cn.bent.sports.base.BaseActivity;
import com.cn.bent.sports.base.BaseConfig;
import com.cn.bent.sports.utils.Constants;
import com.cn.bent.sports.utils.DataUtils;
import com.cn.bent.sports.utils.ToastUtils;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by dawn on 2018/2/26.
 */

public class OfflineActivity extends BaseActivity {

    @Bind(R.id.commit)
    TextView commit;
    @Bind(R.id.commit_edit)
    TextView commit_edit;
    @Bind(R.id.cut_down)
    TextView timer;
    private Handler handler2;

    @Override
    protected int getLayoutId() {
        return R.layout.down_game_layout;
    }

    @Override
    public void initView() {
        super.initView();
        handler2 = new Handler();
        setTimes();
    }

    @Override
    public void initData() {
        super.initData();
    }

    @OnClick({R.id.top_left, R.id.top_image, R.id.commit})
    void onClick(View view) {
        switch (view.getId()) {
            case R.id.top_left:
            case R.id.top_image:
                finish();
                break;
            case R.id.commit:
                if ("123".equals(commit_edit.getText().toString().trim())) {
                    startActivity(new Intent(this, LastActivity.class));
                } else {
                    ToastUtils.showShortToast(this,"游戏失败，再玩一次");
                }
                break;
        }
    }

    private void setTimes() {
        handler2.postDelayed(runnable2, 1000);
    }

    Runnable runnable2 = new Runnable() {
        long longValue = BaseConfig.getInstance(OfflineActivity.this).getLongValue(Constants.IS_TIME, 0);

        @Override
        public void run() {
            handler2.postDelayed(this, 1000);
            Log.i("tttt", "currentTimeMillis=" + (System.currentTimeMillis() - longValue) / 1000);
            if (((System.currentTimeMillis() - longValue) / 1000) >= 2 * 60 * 60) {
                handler2.removeCallbacks(runnable2);
                timer.setText("02.00.00");
            } else {
                timer.setText(DataUtils.getDateToTime(System.currentTimeMillis() - longValue));
            }

        }
    };
}

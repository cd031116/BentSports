package com.cn.bent.sports.view.activity;

import android.graphics.Color;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.cn.bent.sports.MainActivity;
import com.cn.bent.sports.R;
import com.cn.bent.sports.api.BaseApi;
import com.cn.bent.sports.base.BaseActivity;
import com.cn.bent.sports.base.BaseConfig;
import com.cn.bent.sports.bean.StepInfo;
import com.cn.bent.sports.recyclebase.CommonAdapter;
import com.cn.bent.sports.recyclebase.ViewHolder;
import com.cn.bent.sports.utils.Constants;
import com.cn.bent.sports.utils.SaveObjectUtils;
import com.cn.bent.sports.view.activity.youle.bean.UserInfo;
import com.vondear.rxtools.view.RxToast;
import com.zhl.network.RxObserver;
import com.zhl.network.RxSchedulers;
import com.zhl.network.huiqu.JavaRxFunction;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;

public class WalkRankListActivity extends BaseActivity {
    @Bind(R.id.walk_list)
    RecyclerView walk_list;
    @Bind(R.id.fuli_text)
    TextView fuli_text;
    @Bind(R.id.walk_num)
    TextView walk_num;
    @Bind(R.id.walk_rank)
    TextView walk_rank;

    private CommonAdapter<StepInfo.ListBean> mAdapter;
    private List<StepInfo.ListBean> mList = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
    }
    @Override
    protected int getLayoutId() {
        return R.layout.activity_walk_rank_list;
    }

    @Override
    public void initView() {
        super.initView();
        getGameDetail();
        sertview();
    }

    @Override
    public void initData() {
        super.initData();
    }

    @OnClick({R.id.return_ima})
    void onclik(View view){
        switch (view.getId()){
            case R.id.return_ima:
                WalkRankListActivity.this.finish();
                break;
        }
    }

    //获取websocket连接
    private void getGameDetail() {
        showAlert("正在获取....", true);
        BaseApi.getJavaLoginDefaultService(WalkRankListActivity.this).getStepList()
                .map(new JavaRxFunction<StepInfo>())
                .compose(RxSchedulers.<StepInfo>io_main())
                .subscribe(new RxObserver<StepInfo>(WalkRankListActivity.this, TAG, 1, false) {
                    @Override
                    public void onSuccess(int whichRequest, StepInfo info) {
                        dismissAlert();
                        if(mList!=null){
                            mList.clear();
                        }
                        mList.addAll(info.getList());
                        mAdapter.notifyDataSetChanged();
                        setMyView(info);
                        Log.i("tttt", "info=" + info);
                    }

                    @Override
                    public void onError(int whichRequest, Throwable e){
                        dismissAlert();
                        RxToast.error(e.getMessage());
                    }
                });
    }

    private void sertview() {
        mAdapter=new CommonAdapter<StepInfo.ListBean>(WalkRankListActivity.this,R.layout.walk_rank_list_item,mList) {
            @Override
            protected void convert(ViewHolder holder, StepInfo.ListBean listBean, int position) {
                if (position==0){
                    holder.setBackgroundRes(R.id.state,R.drawable.r_gold);
                }else  if (position==1){
                    holder.setBackgroundRes(R.id.state,R.drawable.r_silver);
                }else if (position==2){
                    holder.setBackgroundRes(R.id.state,R.drawable.r_copper);
                }else {
                    holder.setText(R.id.state,String.valueOf(position+1));
                }
                holder.setBitmapWithUrl(R.id.user_photo,"");
                holder.setText(R.id.walk_num,listBean.getStep()+"");
            }
        };
        walk_list.setLayoutManager(new LinearLayoutManager(WalkRankListActivity.this));
        walk_list.setAdapter(mAdapter);
        walk_list.setNestedScrollingEnabled(false);
    }

    private void setMyView( StepInfo infos){
        boolean ishave=false;
        UserInfo users= SaveObjectUtils.getInstance(WalkRankListActivity.this).getObject(Constants.USER_BASE,null);
         for (StepInfo.ListBean bean: infos.getList()) {
             for (int i=0;i<infos.getList().size();i++){
                 if (bean.getUserId()==users.getId()){
                     walk_num.setText(bean.getStep()+"");
                     walk_rank.setText("当前第"+(i+1)+"名");
                     ishave=true;
                     break;
                 }
             }
                if(!ishave){
                    walk_rank.setText("未上榜");
                }
        }
    }


}

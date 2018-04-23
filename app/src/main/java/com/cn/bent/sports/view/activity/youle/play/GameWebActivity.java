package com.cn.bent.sports.view.activity.youle.play;

import android.app.Dialog;
import android.graphics.PixelFormat;
import android.util.Log;
import android.view.View;
import android.webkit.JavascriptInterface;

import com.cn.bent.sports.R;
import com.cn.bent.sports.api.BaseApi;
import com.cn.bent.sports.api.ConstantValues;
import com.cn.bent.sports.api.RequestLisler;
import com.cn.bent.sports.api.RxRequest;
import com.cn.bent.sports.base.BaseActivity;
import com.cn.bent.sports.base.MyX5WebView;
import com.cn.bent.sports.bean.LoginResult;
import com.cn.bent.sports.bean.YouleGameEntity;
import com.cn.bent.sports.utils.Constants;
import com.cn.bent.sports.utils.SaveObjectUtils;
import com.cn.bent.sports.widget.CompletionDialog;
import com.cn.bent.sports.widget.GameErrorDialog;
import com.cn.bent.sports.widget.GameFailDialog;
import com.cn.bent.sports.widget.ToastDialog;
import com.google.gson.Gson;
import com.zhl.network.RxSchedulers;
import com.zhl.network.huiqu.JavaResult;
import com.zhl.network.huiqu.JavaRxFunction;
import butterknife.Bind;
import butterknife.OnClick;
import io.reactivex.Observable;

/**
 * Created by dawn on 2018/4/2.
 * 游戏web页面
 */

public class GameWebActivity extends BaseActivity {

    @Bind(R.id.webview)
    MyX5WebView mWebView;
    private String teamId, gamePointId,type,gameName;
    private String access_token;


    @Override
    protected int getLayoutId() {
        return R.layout.game_web_layout;
    }

    @Override
    public void initView() {
        LoginResult user = SaveObjectUtils.getInstance(this).getObject(Constants.USER_INFO, null);
        access_token = user.getAccess_token();
        teamId = getIntent().getStringExtra("teamId");
        gamePointId = getIntent().getStringExtra("gamePointId");
        type = getIntent().getStringExtra("type");
        gameName = getIntent().getStringExtra("gameName");

        getWindow().setFormat(PixelFormat.TRANSLUCENT);
        mWebView.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        mWebView.setDrawingCacheEnabled(true);
    }



    @Override
    public void initData() {
        if(type.equals("1")) {
            mWebView.loadUrl(ConstantValues.GAME_ONLINE_URL + "?teamId=" + teamId + "&etype=android&gamePointId=" + gamePointId + "&type=" + type + "&token=" + access_token+"&apiUrl="+ConstantValues.JAVA_URL+"&staticUrl="+ConstantValues.JAVA_YUN_URL+"/");
            Log.d(TAG, "initView: "+ConstantValues.GAME_ONLINE_URL + "?teamId=" + teamId + "&etype=android&gamePointId="+gamePointId+"&type=" + type+"&token=" + access_token+"&apiUrl="+ConstantValues.JAVA_URL+"&staticUrl="+ConstantValues.JAVA_YUN_URL+"/");
        }else {
            mWebView.loadUrl(ConstantValues.GAME_OFFLINE_URL + "?teamId=" + teamId + "&etype=android&gamePointId=" + gamePointId + "&type=" + type + "&token=" + access_token+"&apiUrl="+ConstantValues.JAVA_URL+"&staticUrl="+ConstantValues.JAVA_YUN_URL+"/");
            Log.d(TAG, "initView: "+ConstantValues.GAME_OFFLINE_URL + "?teamId=" + teamId + "&etype=android&gamePointId="+gamePointId+"&type=" + type+"&token=" + access_token+"&apiUrl="+ConstantValues.JAVA_URL+"&staticUrl="+ConstantValues.JAVA_YUN_URL+"/");
        }
        mWebView.addJavascriptInterface(new JSInterface(), "native");
    }

    class JSInterface {
        @JavascriptInterface
        public void h5Result(String ss) {
            LoginResult user = SaveObjectUtils.getInstance(GameWebActivity.this).getObject(Constants.USER_INFO, null);
            String access_token = user.getAccess_token();
            Log.e("dasa", "h5Result: " + ss);
            Gson gson = new Gson();
            YouleGameEntity youleGameEntity = gson.fromJson(ss, YouleGameEntity.class);

            if(youleGameEntity.getScord()>=youleGameEntity.getPassScore()){
                finishTask(youleGameEntity);
            }else {
                showErrorDialog(gameName, youleGameEntity.getScord());
            }
        }
    }

    private void finishTask(final YouleGameEntity youleGameEntity) {
        Observable<JavaResult<Boolean>> javaResultObservable;
        showAlert("正在获取...", true);
        if (youleGameEntity.getType() == 2)
            BaseApi.getJavaLoginDefaultService(this)
                    .finishOfflineGame(youleGameEntity.getTeamId(), youleGameEntity.getGamePointId(), youleGameEntity.getScord())
                    .map(new JavaRxFunction<Boolean>())
                    .compose(RxSchedulers.<Boolean>io_main())
                    .subscribe(new RxRequest<>(this, TAG, 1, new RequestLisler<Boolean>() {
                        @Override
                        public void onSucess(int whichRequest, Boolean aBoolean) {
                            dismissAlert();
                            if (aBoolean)
                                showSuccessDialog(gameName, youleGameEntity.getScord());
                            else
                                showErrorDialog(gameName, youleGameEntity.getScord());
                        }

                        @Override
                        public void on_error(int whichRequest, Throwable e) {
                            dismissAlert();
                            if (e.getMessage().equals("回答错误"))
                                showAlertDialog();
                        }
                    }));
        else
            BaseApi.getJavaLoginDefaultService(this)
                    .finishOnlineGame(youleGameEntity.getTeamId(), youleGameEntity.getGamePointId(), youleGameEntity.getScord())
                    .map(new JavaRxFunction<String>())
                .compose(RxSchedulers.<String>io_main())
                .subscribe(new RxRequest<>(this, TAG, 1,new RequestLisler<String>() {
            @Override
            public void onSucess(int whichRequest, String s) {
                dismissAlert();
                showSuccessDialog(gameName, youleGameEntity.getScord());
            }

            @Override
            public void on_error(int whichRequest, Throwable e) {
                dismissAlert();
                if (e.getMessage().equals("回答错误"))
                    showAlertDialog();
            }
        }));
    }

    private void showAlertDialog() {
        new GameErrorDialog(GameWebActivity.this, R.style.dialog, new GameErrorDialog.OnCloseListener() {
            @Override
            public void onClick(Dialog dialog, int index) {
                dialog.dismiss();
                finish();
            }
        }).show();
    }

    private void showErrorDialog(String game_name, int score) {
        new GameFailDialog(GameWebActivity.this, R.style.dialog, new GameFailDialog.OnCloseListener() {
            @Override
            public void onClick(Dialog dialog, String index) {
                dialog.dismiss();
                finish();
            }
        }).setName(game_name).setScore(score).show();
    }

    private void showSuccessDialog(String game_name, int score) {
        new CompletionDialog(GameWebActivity.this, R.style.dialog, new CompletionDialog.OnCloseListener() {
            @Override
            public void onClick(Dialog dialog, String index) {
                dialog.dismiss();
                finish();
            }
        }).setName(game_name).setScore(score).show();
    }

    @OnClick(R.id.map_return)
    void onClick(View view) {
        showDialog();
    }

    @Override
    public void onBackPressed() {
        showDialog();
    }

    private void showDialog() {
        new ToastDialog(this, R.style.dialog, "是否离开游戏", new ToastDialog.OnCloseListener() {
            @Override
            public void onClick(Dialog dialog, boolean confirm) {
                if (confirm) {
                    finish();
                } else {
                }
                dialog.dismiss();
            }
        }).setTitle("提示").show();
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mWebView!= null) {
            mWebView.destroy();
        }
    }
}

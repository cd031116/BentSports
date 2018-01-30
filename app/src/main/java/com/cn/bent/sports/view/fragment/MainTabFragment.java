package com.cn.bent.sports.view.fragment;

import android.content.Context;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.cn.bent.sports.R;
import com.cn.bent.sports.base.BaseFragment;
import com.cn.bent.sports.bean.RangeEntity;
import com.cn.bent.sports.recyclebase.CommonAdapter;
import com.cn.bent.sports.recyclebase.ViewHolder;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

/**
 * Created by lyj on 2018/1/29 0029.
 * description
 */

public class MainTabFragment extends BaseFragment {
    @Bind(R.id.range_list)
    RecyclerView range_list;

    public static MainTabFragment newInstance(int type) {
        MainTabFragment fragment = new MainTabFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("type", type);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.main_fragment;
    }

    @Override
    protected void initView(View view, Bundle savedInstanceState) {
        range_list.setLayoutManager(new LinearLayoutManager(getActivity()));
        setRecyclerView();
    }

    private void setRecyclerView() {
        List<RangeEntity> list = new ArrayList<>();
        RangeEntity rangeEntity = new RangeEntity();
        rangeEntity.setName("sss");
        rangeEntity.setJifen("1234");
        rangeEntity.setNum("2");
        rangeEntity.setHead_img("http://pic4.40017.cn/scenery/destination/2016/08/15/14/3aPCtM_740x350_00.jpg");
        list.add(rangeEntity);
        list.add(rangeEntity);
        list.add(rangeEntity);
        list.add(rangeEntity);
        CommonAdapter<RangeEntity> mAdapter = new CommonAdapter<RangeEntity>(getActivity(), R.layout.item_range, list) {
            @Override
            protected void convert(ViewHolder holder, RangeEntity rangeEntity, int position) {
                holder.setText(R.id.range_num, rangeEntity.getNum());
                holder.setText(R.id.range_name, rangeEntity.getName());
                holder.setText(R.id.range_jifen, rangeEntity.getJifen());
                ImageView NormalInfoImg = (ImageView) holder.getView(R.id.img_head);
                RequestOptions requestOptions = RequestOptions.circleCropTransform();
                Glide.with(NormalInfoImg.getContext()).load(rangeEntity.getHead_img())
                        .apply(requestOptions)
                        .into(NormalInfoImg);
            }
        };
        range_list.setAdapter(mAdapter);
    }

    @Override
    protected void initData() {
        Log.e("dasa", "initData: " + Environment.getExternalStorageDirectory().toString() + "/style.data");
        copyFilesFromAssets(getActivity(), "1.txt", Environment.getExternalStorageDirectory().toString() + "/style");
    }

    public static void copyFilesFromAssets(Context context, String assetsPath, String savePath) {
        try {
            String fileNames[] = context.getAssets().list(assetsPath);// 获取assets目录下的所有文件及目录名
            if (fileNames.length > 0) {// 如果是目录
                File file = new File(savePath);
                if (!file.exists())
                    file.mkdirs();// 如果文件夹不存在，则递归
                for (String fileName : fileNames) {
                    copyFilesFromAssets(context, assetsPath + "/" + fileName,
                            savePath + "/" + fileName);
                }
            } else {// 如果是文件
                InputStream is = context.getAssets().open(assetsPath);
                FileOutputStream fos = new FileOutputStream(new File(savePath));
                byte[] buffer = new byte[1024];
                int byteCount = 0;
                while ((byteCount = is.read(buffer)) != -1) {// 循环从输入流读取
                    // buffer字节
                    fos.write(buffer, 0, byteCount);// 将读取的输入流写入到输出流
                }
                fos.flush();// 刷新缓冲区
                is.close();
                fos.close();
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

}

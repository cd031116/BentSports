package com.cn.bent.sports.view.activity;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.AnimationDrawable;
import android.location.LocationManager;
import android.view.View;

import com.amap.api.maps.model.BitmapDescriptor;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.cn.bent.sports.R;

import java.util.ArrayList;

/**
 * Created by dawn on 2018/3/10.
 */

public class BitmapManager {

    private static BitmapManager instance = null;

    private BitmapManager() {
    }

    public static BitmapManager getInstance() {
        synchronized (BitmapManager.class) {
            if (instance == null) {
                instance = new BitmapManager();
            }
        }

        return instance;
    }

    /**
     * marker点图标
     *
     * @param index
     * @return
     */
    public BitmapDescriptor getBitmapDescriptor(int index) {
        BitmapDescriptor bitmapDescriptor = null;
        switch (index) {
            case 1:
                bitmapDescriptor = BitmapDescriptorFactory
                        .fromResource(R.drawable.yuyin_1);
                break;
            case 2:
                bitmapDescriptor = BitmapDescriptorFactory
                        .fromResource(R.drawable.yuyin_1);
                break;
            case 3:
                bitmapDescriptor = BitmapDescriptorFactory
                        .fromResource(R.drawable.wc1);
                break;
            case 4:
                bitmapDescriptor = BitmapDescriptorFactory
                        .fromResource(R.drawable.gouwu1);
                break;
            case 5:
                bitmapDescriptor = BitmapDescriptorFactory
                        .fromResource(R.drawable.canting1);
                break;
            case 6:
                bitmapDescriptor = BitmapDescriptorFactory
                        .fromResource(R.drawable.jiudian1);
                break;
            case 7:
                bitmapDescriptor = BitmapDescriptorFactory
                        .fromResource(R.drawable.tichec1);
                break;
            case 8:
                bitmapDescriptor = BitmapDescriptorFactory
                        .fromResource(R.drawable.damen1);
                break;
            default:
                bitmapDescriptor = BitmapDescriptorFactory
                        .fromResource(R.drawable.yuyin_1);
                break;
        }
        return bitmapDescriptor;
    }
    /**
     * 游戏点marker点图标
     *
     * @param index
     * @return
     */
    public BitmapDescriptor getGameBitmapDescriptor(int index) {
        BitmapDescriptor bitmapDescriptor = null;
        switch (index) {
            case 1:
                bitmapDescriptor = BitmapDescriptorFactory
                        .fromResource(R.drawable.game_commplete);
                break;
            case -1:
                bitmapDescriptor = BitmapDescriptorFactory
                        .fromResource(R.drawable.uncomplete);
                break;
            default:
                bitmapDescriptor = BitmapDescriptorFactory
                        .fromResource(R.drawable.uncomplete);
                break;
        }
        return bitmapDescriptor;
    }

    public ArrayList<BitmapDescriptor> getBitmapDescriptorOverlay() {
        ArrayList<BitmapDescriptor> iconList = new ArrayList<>();
        iconList.add(BitmapDescriptorFactory.fromResource(R.drawable.tour_play1));
        iconList.add(BitmapDescriptorFactory.fromResource(R.drawable.tour_play2));
        iconList.add(BitmapDescriptorFactory.fromResource(R.drawable.tour_play3));
        iconList.add(BitmapDescriptorFactory.fromResource(R.drawable.tour_play4));
        iconList.add(BitmapDescriptorFactory.fromResource(R.drawable.tour_play5));
        return iconList;
    }

    public BitmapDescriptor getBitmapDescriptor4View(View view) {
        BitmapDescriptor bitmapDescriptor =BitmapDescriptorFactory
                .fromBitmap(getViewBitmap(view));
        return bitmapDescriptor;
    }

    private Bitmap getViewBitmap(View addViewContent) {

        addViewContent.setDrawingCacheEnabled(true);
        addViewContent.measure(
                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
        addViewContent.layout(0, 0,
                addViewContent.getMeasuredWidth(),
                addViewContent.getMeasuredHeight());
        addViewContent.buildDrawingCache();

        Bitmap cacheBitmap = addViewContent.getDrawingCache();
        Bitmap bitmap = Bitmap.createBitmap(cacheBitmap);

        return bitmap;
    }
}

package com.cn.bent.sports.view.activity;

import com.amap.api.maps.model.BitmapDescriptor;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.cn.bent.sports.R;

/**
 * Created by dawn on 2018/3/10.
 */

public class BitmapManager {

    private static BitmapManager instance = null;

    private BitmapManager(){
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
}

package com.cn.bent.sports.widget;

import android.content.Context;

import com.amap.api.maps.AMap;
import com.amap.api.maps.model.BitmapDescriptor;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.route.WalkPath;
import com.cn.bent.sports.R;
import com.cn.bent.sports.overlay.WalkRouteOverlay;

/**
 * Created by dawn on 2018/3/17.
 */

public class RouteTool  extends WalkRouteOverlay {
    public int color;//路线颜色
    public float lineWidth;//路线宽度
    private int resId;

    /*修改路线宽度*/
    @Override
    protected float getRouteWidth() {
        return lineWidth;
    }


    /*修改路线颜色*/
    @Override
    protected int getWalkColor() {
        return color;
    }


    /*
    修改终点marker样式，这里的R.drawable.none是我自己画的一个PNG图片，图片什么都看不到，而这么修改就等于是把这些marker都去掉了，只留下一条规划的路线，当然可以把BitmapDescriptor 的起点、终点等做成域封装起来供别的类修改，现在我比较懒，就用汉字说明就好了
    */

    protected BitmapDescriptor getEndBitmapDescriptor(int resId) {
        BitmapDescriptor reBitmapDescriptor=new BitmapDescriptorFactory().fromResource(resId);
        return reBitmapDescriptor;
    }
    /*修改起点marker样式*/

    @Override
    protected BitmapDescriptor getStartBitmapDescriptor(int resId) {
        BitmapDescriptor reBitmapDescriptor=new BitmapDescriptorFactory().fromResource(resId);
        return reBitmapDescriptor;
    }
    /*修改中间点marker样式*/

    protected BitmapDescriptor getWalkBitmapDescriptor(int resId) {
        BitmapDescriptor reBitmapDescriptor=new BitmapDescriptorFactory().fromResource(resId);

        return reBitmapDescriptor;
    }
    /*一个无聊的构造*/
    public RouteTool(Context arg0, AMap arg1, WalkPath arg2, LatLonPoint arg3,
            LatLonPoint arg4) {
        super(arg0, arg1, arg2, arg3, arg4);
    }
    /*一个工具方法，修改颜色和宽度*/
    public void setView(int color,float width) {
        this.color=color;
        lineWidth=width;
    }

}

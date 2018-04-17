package com.cn.bent.sports.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import com.cn.bent.sports.R;

/**
 * Created by Administrator on 2018/4/16/016.
 */

public class Webviewprogressbar extends View {
    private int progress = 1;//进度默认为1
    private final static int height = 5;//进度条高度为5
    private Paint paint;//进度条的画笔
    // 渐变颜色数组
    private final static int colors[] = new int[]{0xff7ad237, 0xff8ac14a, 0x35b056 }; //int类型颜色值格式：0x+透明值+颜色的rgb值
    public Webviewprogressbar(Context context) {
        this (context,null);
    }
    public Webviewprogressbar(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }
    public Webviewprogressbar(Context context, AttributeSet attrs, int defstyleattr) {
        super(context, attrs, defstyleattr);
        initpaint(context);
    }
    private void initpaint(Context context) {
        //颜色渐变从colors[0]到colors[2],透明度从0到1
//    lineargradient shader = new lineargradient(
//        0, 0,
//        100, height,
//        colors,
//        new float[]{0 , 0.5f, 1.0f},
//        shader.tilemode.mirror);
        paint=new Paint(Paint.DITHER_FLAG);
        paint.setStyle(Paint.Style.STROKE);// 填充方式为描边
        paint.setStrokeWidth(height);//设置画笔的宽度
        paint.setAntiAlias(true);// 抗锯齿
        paint.setDither(true);// 使用抖动效果
        paint.setColor(context.getResources().getColor(R.color.blue_text));//画笔设置颜色
//    paint.setshader(shader);//画笔设置渐变
    }
    /**
     * 设置进度
     * @param progress 进度值
     */
    public void setprogress(int progress){
        this.progress = progress;
        invalidate();//刷新画笔
    }
    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawRect(0, 0, getWidth() * progress / 100, height, paint);//画矩形从（0.0）开始到（progress,height）的区域
    }
}

package com.cn.bent.sports.widget;

import android.content.Context;
import android.graphics.Matrix;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewTreeObserver;
import android.widget.ImageView;

/**
 * Created by dawn on 2018/1/31.
 */

public class MyZoomImageView extends ImageView implements ViewTreeObserver.OnGlobalLayoutListener, ScaleGestureDetector.OnScaleGestureListener, View.OnTouchListener {
    private boolean mOnce;//init一次
    /**
     * 初始缩放比例
     */
    private float mInitScale;
    /**
     * 双击放大比例
     */
    private float mMidScale;
    /**
     * 最大放大比例
     */
    private float mMaxScale;
    private Matrix mMatrix;
    /**
     * 多点触摸检测
     */
    private ScaleGestureDetector mScaleGestureDetector;
    //---------多点触控--------
    private int mLastPointCount;
    private float mLastX;
    private float mLastY;
    private boolean isCanDrag;
    private int mTouchSlop;
    private boolean isCheckTopAndBottom;
    private boolean isCheckLeftAndRight;

    //-------------双击放大缩小
    private GestureDetector mGestureDetector;
    private boolean isAutoScale;

    public MyZoomImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mMatrix = new Matrix();
        setScaleType(ScaleType.MATRIX);
        setOnTouchListener(this);
        mScaleGestureDetector = new ScaleGestureDetector(context, this);
        mTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
        isCheckTopAndBottom = true;
        isCheckLeftAndRight = true;
        mGestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onDoubleTap(MotionEvent e) {
                if (isAutoScale) {
                    return true;
                }
                if (getScale() < mMidScale) {
                    //放大
//                    mMatrix.postScale(mMidScale / getScale(), mMidScale / getScale(), e.getX(), e.getY());
                    postDelayed(new AutoScaleRunnable(mMidScale, e.getX(), e.getY()), 16);
                    isAutoScale = true;
                } else {
                    //缩小
//                    mMatrix.postScale(mInitScale / getScale(), mInitScale / getScale(), e.getX(), e.getY());
                    postDelayed(new AutoScaleRunnable(mInitScale, e.getX(), e.getY()), 16);
                    isAutoScale = true;
                }
                return true;
            }
        });
    }

    class AutoScaleRunnable implements Runnable {
        private float x, y;
        private float targetScale;
        private float tempScale;
        private final float BIGGER = 1.07f;
        private final float SMALL = 0.93f;

        public AutoScaleRunnable(float targetScale, float x, float y) {
            this.targetScale = targetScale;
            this.x = x;
            this.y = y;
            if (getScale() < targetScale) {
                tempScale = BIGGER;
            }
            if (getScale() > targetScale) {
                tempScale = SMALL;
            }
        }

        @Override
        public void run() {
            mMatrix.postScale(tempScale, tempScale, x, y);
            checkBorderAndCenterWhenScale();
            setImageMatrix(mMatrix);
            float currentScale = getScale();
            if ((currentScale < targetScale && tempScale > 1.0f) || (currentScale > targetScale && tempScale < 1.0f)) {
                postDelayed(this, 15);
            } else {
                mMatrix.postScale(targetScale / currentScale, targetScale / currentScale, x, y);
                checkBorderAndCenterWhenScale();
                setImageMatrix(mMatrix);
                isAutoScale = false;
            }
        }
    }

    public MyZoomImageView(Context context) {
        this(context, null);

    }

    public MyZoomImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        getViewTreeObserver().addOnGlobalLayoutListener(this);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        getViewTreeObserver().removeGlobalOnLayoutListener(this);
    }

    @Override
    public void onGlobalLayout() {
        if (!mOnce) {
            int width = getWidth();
            int height = getHeight();
            Drawable drawable = getDrawable();
            int dw = drawable.getIntrinsicWidth();
            int dh = drawable.getIntrinsicHeight();
            float scale = 1.0f;
            /**
             * 图片宽度大于屏幕宽度，高小于屏幕高
             */
            if (dw > width && dh < height) {
                scale = 1.0f * width / dw;
            }
            if (dw < width && dh > height) {
                scale = 1.0f * height / dh;
            }
            if ((dw > width && dh > height) || (dw < width && dh < height)) {
                scale = Math.min(1.0f * width / dw, 1.0f * height / dh);
            }
            //得到缩放比例
            mInitScale = scale;
            mMidScale = scale * 3;
            mMaxScale = scale * 5;
            //图片平移到中心
            int dx = width / 2 - dw / 2;
            int dy = height / 2 - dh / 2;
            mMatrix.postTranslate(dx, dy);
            mMatrix.postScale(scale, scale, width / 2, height / 2);
            setImageMatrix(mMatrix);
            mOnce = true;
        }

    }

    /**
     * 获取当前图片的缩放值
     *
     * @return
     */
    float getScale() {
        float values[] = new float[9];
        mMatrix.getValues(values);
        return values[Matrix.MSCALE_X];
    }

    @Override
    public boolean onScale(ScaleGestureDetector detector) {
        float scale = getScale();
        float scaleFactor = detector.getScaleFactor();
        if (getDrawable() == null)
            return true;
        //可缩放的范围
        if ((scale < mMaxScale && scaleFactor > 1.0f) || (scale > mInitScale && scaleFactor < 1.0f)) {
            if (scale * scaleFactor > mMaxScale) {
                scaleFactor = mMaxScale / scale;
            }
            if (scale * scaleFactor < mInitScale) {
                scaleFactor = mInitScale / scale;
            }
            mMatrix.postScale(scaleFactor, scaleFactor, detector.getFocusX(), detector.getFocusY());
            checkBorderAndCenterWhenScale();
            setImageMatrix(mMatrix);
        }
        return false;
    }

    private void checkBorderAndCenterWhenScale() {
        RectF rect = getMatrixRectf();
        float deltaX = 0;
        float deltaY = 0;
        int width = getWidth();
        int height = getHeight();
        //边界检测防止出现白边,这里的比较是以本控件的左上角作为坐标原点
        if (rect.width() >= width) {
            if (rect.left > 0) {
                deltaX = -rect.left;
            }
            if (rect.right < width) {
                deltaX = width - rect.right;
            }
        }
        if (rect.height() >= height) {
            if (rect.top > 0) {
                deltaY = -rect.top;
            }
            if (rect.bottom < height) {
                deltaY = height - rect.bottom;
            }
        }
        //如果宽度或高度小于控件的宽度或高度，则让图片居中
        if (rect.width() < width) {
            deltaX = width / 2f - rect.right + rect.width() / 2;
        }
        if (rect.height() < height) {
            deltaY = height / 2f - rect.bottom + rect.height() / 2;
        }
        mMatrix.postTranslate(deltaX, deltaY);
    }

    /**
     * 左右上下移动白边检测
     */
    private void checkBorderWhenTranslate() {
        RectF rect = getMatrixRectf();
        float deltaX = 0;
        float deltaY = 0;
        int width = getWidth();
        int height = getHeight();
        if (rect.left > 0 && isCheckLeftAndRight) {
            deltaX = -rect.left;
        }
        if (rect.right < width && isCheckLeftAndRight) {
            deltaX = width - rect.right;
        }
        if (rect.top > 0 && isCheckTopAndBottom) {
            deltaY = -rect.top;
        }
        if (rect.bottom < height && isCheckTopAndBottom) {
            deltaY = height - rect.bottom;
        }
        mMatrix.postTranslate(deltaX, deltaY);
    }

    /**
     * 获取图片放大以后的l,t,r,b;
     *
     * @return
     */
    private RectF getMatrixRectf() {
        Matrix matrix = mMatrix;
        Drawable d = getDrawable();
        RectF rectF = new RectF();
        if (d != null) {
            rectF.set(0, 0, d.getIntrinsicWidth(), d.getIntrinsicHeight());
            matrix.mapRect(rectF);
        }
        return rectF;
    }

    @Override
    public boolean onScaleBegin(ScaleGestureDetector detector) {
        return true;
    }

    @Override
    public void onScaleEnd(ScaleGestureDetector detector) {

    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (mGestureDetector.onTouchEvent(event)) {
            return true;
        }
        mScaleGestureDetector.onTouchEvent(event);
        float x = 0;
        float y = 0;
        int pointerCount = event.getPointerCount();
        for (int i = 0; i < pointerCount; i++) {
            x += event.getX(i);
            y += event.getY(i);
        }
        x /= pointerCount;
        y /= pointerCount;
        if (mLastPointCount != pointerCount) {
            isCanDrag = false;
            mLastX = x;
            mLastY = y;
        }
        mLastPointCount = pointerCount;
        RectF rectF = getMatrixRectf();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (rectF.width() > getWidth() || rectF.height() > getHeight()) {
                    //请求父view不要拦截事件，也就是与viewpage结合使用的时候防止事件冲突
                    getParent().requestDisallowInterceptTouchEvent(true);
                }
                break;
            case MotionEvent.ACTION_MOVE:
                /**
                 * 处理图片自由移动
                 */
//                if (rectF.width() > getWidth() || rectF.height() > getHeight()) {
//                    //请求父view不要拦截事件
//                    getParent().requestDisallowInterceptTouchEvent(true);
//                }
                float dx = x - mLastX;
                float dy = y - mLastY;
                if (!isCanDrag) {
                    isCanDrag = isMoveAction(dx, dy);
                }
                if (getDrawable() != null) {
                    if (isCanDrag) {
                        //如果宽度小于控件宽度，不能横向移动
                        if (rectF.width() < getWidth()) {
                            isCheckLeftAndRight = false;
                            dx = 0;
                        } else {
                            isCheckLeftAndRight = true;
                        }
                        if (rectF.height() < getHeight()) {
                            isCheckTopAndBottom = false;
                            dy = 0;
                        } else {
                            isCheckTopAndBottom = true;
                        }
                        mMatrix.postTranslate(dx, dy);
                        checkBorderWhenTranslate();
                        setImageMatrix(mMatrix);
                    }
                }
                mLastX = x;
                mLastY = y;
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                mLastPointCount = 0;
                getParent().requestDisallowInterceptTouchEvent(false);
                break;
            default:
                break;
        }
        return true;
    }


    /**
     * 判断是否是move
     *
     * @param dx
     * @param dy
     * @return
     */
    private boolean isMoveAction(float dx, float dy) {
        return Math.sqrt(dx * dx + dy * dy) > mTouchSlop;
    }
}
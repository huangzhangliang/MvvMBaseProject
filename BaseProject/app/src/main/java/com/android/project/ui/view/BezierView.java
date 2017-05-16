package com.android.project.ui.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by leon on 17/5/10.
 */

public class BezierView extends View {

    private Paint mPaint;
    private Path mPath;
    private int mWidth,mHeight;


    public BezierView(Context context) {
        this(context,null);
    }

    public BezierView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public BezierView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mPath = new Path();
        mPaint = new Paint();
        mPaint.setColor(Color.WHITE);
        mPaint.setStyle(Paint.Style.FILL);
        // 抗锯齿
        mPaint.setAntiAlias(true);
        // 防抖动
        mPaint.setDither(true);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        calculatePath();
        // 2.画
        canvas.drawPath(mPath,mPaint);
//        LogUtils.sf("BezierView-onDraw:"+getWidth()+"----"+getHeight());
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
//        LogUtils.sf("BezierView-onLayout:"+getWidth()+"----"+getHeight());
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        mWidth = getMeasuredWidth();
        mHeight = getMeasuredHeight();
//        LogUtils.sf("BezierView-onMeasure:"+getWidth()+"----"+getHeight()+"----"+getMeasuredWidth()+"----"+getMeasuredHeight());
    }

    private void calculatePath() {

        mPath.reset();
        mPath.moveTo(0,mHeight - 1); // 落笔点
        mPath.quadTo(mWidth / 2,0,mWidth,mHeight - 1); // 画曲线
//        mPath.lineTo(p2x,p2y); // 画直线
//        mPath.quadTo(anchorX,anchorY,p3x,p3y); // 画曲线
        mPath.lineTo(mWidth,mHeight);
        mPath.lineTo(0,mHeight);
        mPath.close(); // 封闭
    }


}

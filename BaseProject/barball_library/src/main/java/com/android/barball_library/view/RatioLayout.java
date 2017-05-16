package com.android.barball_library.view;


import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.widget.FrameLayout;

import com.android.barball_library.R;


public class RatioLayout extends FrameLayout {

    public static final int RELATIVE_WIDTH = 0; // 相对于宽
    public static final int RELATIVE_HEIGHT = 1; // 相对于高

    public float mPicRatio = 0; // 图片宽高比
    public int relative = RELATIVE_WIDTH; // 相对于宽或高进行缩放

    public RatioLayout(Context context) {
        this(context,null);
    }

    public RatioLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray typeArray = context.obtainStyledAttributes(attrs, R.styleable.RatioLayout);
        mPicRatio = typeArray.getFloat(R.styleable.RatioLayout_picRatio,0); // 获取自定义缩放比例
        relative = typeArray.getInt(R.styleable.RatioLayout_relative,0); // 获取自定义缩放方式(相对于宽或相对于高)
        typeArray.recycle();
    }




    public void setPicRatio(float picRatio) {
        mPicRatio = picRatio;
    }


    public void setRelative(int relative) {
        this.relative = relative;
    }



    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        // 宽固定求高
        int parentWidthMode = MeasureSpec.getMode(widthMeasureSpec);
        // 高固定求宽
        int parentHeightMode = MeasureSpec.getMode(heightMeasureSpec);

        if(parentWidthMode == MeasureSpec.EXACTLY && mPicRatio != 0   && relative == RELATIVE_WIDTH){ // 如果宽固定
            // 得到父容器的宽度
            int parentWidth = MeasureSpec.getSize(widthMeasureSpec);
            // 得到子控件的宽度
            int childWidth = parentWidth - getPaddingLeft() - getPaddingRight();

            // 得到子控件的高度
            int childHeight = (int) (childWidth / mPicRatio + .5f);
            // 得到父容器的高度
            int parentHeight = childHeight + getPaddingBottom() + getPaddingTop();

            // 主动测绘子控件的大小
            int childWidthMeasureSpec = MeasureSpec.makeMeasureSpec(childWidth, MeasureSpec.EXACTLY);
            int childHeightMeasureSpec = MeasureSpec.makeMeasureSpec(childHeight, MeasureSpec.EXACTLY);
            measureChildren(childWidthMeasureSpec,childHeightMeasureSpec);

            setMeasuredDimension(parentWidth,parentHeight);
        }else if(parentHeightMode == MeasureSpec.EXACTLY && mPicRatio != 0 && relative == RELATIVE_HEIGHT){ // 如果高固定
            // 得到父容器的高度
            int parentHeight = MeasureSpec.getSize(heightMeasureSpec);
            // 得到子控件的高度
            int childHeight = parentHeight - getPaddingTop() - getPaddingBottom();
            // 得到子控件的宽度
            int childWidth = (int) (childHeight * mPicRatio + .5f);
            // 得到父容器的宽度
            int parentWidth = childWidth + getPaddingRight() + getPaddingLeft();

            // 主动测绘子控件的大小
            int childWidthMeasureSpec = MeasureSpec.makeMeasureSpec(childWidth, MeasureSpec.EXACTLY);
            int childHeightMeasureSpec = MeasureSpec.makeMeasureSpec(childHeight, MeasureSpec.EXACTLY);
            measureChildren(childWidthMeasureSpec,childHeightMeasureSpec);

            setMeasuredDimension(parentWidth,parentHeight);
        }else {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        }
    }
}

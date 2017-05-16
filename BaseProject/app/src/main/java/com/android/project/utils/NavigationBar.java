package com.android.project.utils;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.annotation.ColorInt;
import android.support.annotation.ColorRes;
import android.support.annotation.DrawableRes;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.project.R;
import com.android.project.base.BaseActivity;
import com.android.project.databinding.LayoutNavigationBinding;


/**
 * 作　　者：Leon（黄长亮）
 * 创建日期：2017/3/31
 */
public class NavigationBar extends FrameLayout implements View.OnClickListener {

    private String mNavTitle;
    public LayoutNavigationBinding mNavigationBinding;
    private OnRightItemClickListener mListener;

    public void setNavTitle(String navTitle) {
        mNavTitle = navTitle;
//        mNavigationBinding.tvTitle.setText(navTitle);
        mNavigationBinding.tvNavTitle.setText(navTitle);
    }

    public String getNavTitle() {
        return mNavTitle;
    }

    public NavigationBar(BaseActivity activity) {
        super(activity);
        initView(activity);
    }

    private void initView(BaseActivity activity) {
        View v = View.inflate(activity, R.layout.layout_navigation, null);
        mNavigationBinding = DataBindingUtil.bind(v);
        initLayout(mNavigationBinding, activity);
        initListener(activity, mNavigationBinding);
//        Toolbar toolbar = mNavigationBinding.toolbar;
//        activity.setSupportActionBar(toolbar);
//        ActionBar actionBar = activity.getSupportActionBar();
//        actionBar.setDisplayShowTitleEnabled(false);
//        actionBar.setDisplayHomeAsUpEnabled(true);
        this.addView(v);
    }


    public void initLayout(LayoutNavigationBinding navigationBinding, Context context) {

    }

    public void initListener(final BaseActivity activity, LayoutNavigationBinding navigationBinding) {
//        navigationBinding.toolbar.setNavigationOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                activity.finish();
//            }
//        });
        navigationBinding.btnNavBack.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.finish();
            }
        });
    }

    public void addRightButton(String text) {
        addRightButton(text, R.color.white, R.drawable.btn_theme_selector, UIUtils.dip2Px(56));
    }

    public void addRightButton(String text, @ColorRes int textColor, @DrawableRes int bgDrawable, int height) {
        if (TextUtils.isEmpty(text)) {
            return;
        }
        TextView tv = new TextView(getContext());
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, height);
        layoutParams.setMargins(0, 0, UIUtils.dip2Px(15), 0);
        tv.setLayoutParams(layoutParams);
        tv.setBackground(ContextCompat.getDrawable(UIUtils.getContext(), bgDrawable));
        tv.setGravity(Gravity.CENTER);
        tv.setPadding(UIUtils.dip2Px(12), 0, UIUtils.dip2Px(12), 0);
        tv.setText(text);
        tv.setClickable(true);
        tv.setTextColor(ContextCompat.getColorStateList(getContext(), textColor));
        tv.setTextSize(12);
        tv.setTag(mNavigationBinding.headerRightLayout.getChildCount());
        tv.setOnClickListener(this);
        mNavigationBinding.headerRightLayout.addView(tv, 0);
    }

    public void addRightButton(@DrawableRes int drawable) {
        addRightButton(drawable, 0);
    }

    public void addRightButton(@DrawableRes int drawable, int padding) {
        addRightButton(drawable, padding, padding, padding, padding);
    }

    public void addRightButton(@DrawableRes int drawable, int pl, int pt, int pr, int pb) {
        ImageButton iv = new ImageButton(getContext());
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(UIUtils.dip2Px(56), UIUtils.dip2Px(56));
        layoutParams.setMargins(0, 0, 0, 0);
        iv.setLayoutParams(layoutParams);
        iv.setImageResource(drawable);
        iv.setBackground(UIUtils.getDrawable(R.drawable.btn_theme_selector));
        iv.setPadding(pl, pt, pr, pb);
        iv.setScaleType(ImageView.ScaleType.CENTER_CROP);
        iv.setOnClickListener(this);
        iv.setTag(mNavigationBinding.headerRightLayout.getChildCount());
        mNavigationBinding.headerRightLayout.addView(iv, 0);
    }


    @Override
    public void onClick(View v) {
        if (mListener != null) {
            mListener.onClick(v);
        }
    }

    public void setOnRightItemClickListener(OnRightItemClickListener listener) {
        mListener = listener;
    }

    public interface OnRightItemClickListener {
        // 根据view.getTag()判断是哪个控件，最右边的为0，向左++
        void onClick(View v);
    }
}